package com.example.Gateway.filters;

import com.example.Gateway.Values;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Component
public class UserAuthFilter extends AbstractGatewayFilterFactory<UserAuthFilter.Config> {

    private final String ROL = "USER";

    SignatureAlgorithm sa = SignatureAlgorithm.HS256;
    SecretKeySpec secretKeySpec = new SecretKeySpec(Values.SECRET_KEY.getBytes(), sa.getJcaName());

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            List<String> authorization = request.getHeaders().get("Authorization");
            try {
                String bearerToken = authorization.get(0);

                Claims tokenClaims = Jwts.parserBuilder().setSigningKey(secretKeySpec).build().parseClaimsJws(bearerToken).getBody();
                

                List<String> roles = (List<String>) tokenClaims.get("roles");
                if (!roles.contains(ROL)) {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    throw new RuntimeException("Unauthorized call");
                }

            } catch (NullPointerException e) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                throw new RuntimeException("No token found");
            } catch (ClassCastException castException) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                throw new RuntimeException("Something is wrong with the token");
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {}
}
