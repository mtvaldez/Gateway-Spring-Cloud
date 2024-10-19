package com.example.Gateway.filters;

import com.example.Gateway.ForbiddenException;
import com.example.Gateway.Values;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Component
public class AdminAuthFilter extends AbstractGatewayFilterFactory<AdminAuthFilter.Config> {

    private final String ROL = "ADMIN";

    SignatureAlgorithm sa = SignatureAlgorithm.HS256;
    SecretKeySpec secretKeySpec = new SecretKeySpec(Values.SECRET_KEY.getBytes(), sa.getJcaName());

    public AdminAuthFilter() {
        super(AdminAuthFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(AdminAuthFilter.Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            List<String> authorization = request.getHeaders().get("Authorization");
            try {
                String bearerToken = authorization.get(0);

                Claims tokenClaims = Jwts.parserBuilder().setSigningKey(secretKeySpec).build().parseClaimsJws(bearerToken.split(" ")[1]).getBody();


                List<String> roles = (List<String>) tokenClaims.get("roles");
                if (!roles.contains(ROL)) {
                    throw new ForbiddenException("Unauthorized call");
                }

            } catch (NullPointerException e) {
                throw new ForbiddenException("No token found");
            } catch (ClassCastException castException) {
                throw new ForbiddenException("Something is wrong with the token");
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {}
}
