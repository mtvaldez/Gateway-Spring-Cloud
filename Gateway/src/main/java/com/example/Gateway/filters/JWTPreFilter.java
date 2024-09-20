package com.example.Gateway.filters;

import com.example.Gateway.Values;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;

@Component
@Slf4j
public class JWTPreFilter implements GlobalFilter {

    private final String ROL = "USER";

    SignatureAlgorithm sa = SignatureAlgorithm.HS256;
    SecretKeySpec secretKeySpec = new SecretKeySpec(Values.SECRET_KEY.getBytes(), sa.getJcaName());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        List<String> authorization = request.getHeaders().get("Authorization");
//        try {
//            String bearerToken = authorization.get(0);
//
//            Claims jwtBody = Jwts.parserBuilder().setSigningKey(secretKeySpec).build().parseClaimsJws(bearerToken).getBody();
//
//            List<String> roles = (List<String>) jwtBody.get("roles");
//            if (!roles.contains(ROL)) {
//                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//                throw new RuntimeException("Unauthorized call");
//            }
//
//        } catch (NullPointerException e) {
//            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//            throw new RuntimeException("No token found");
//        } catch (ClassCastException castException) {
//            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//            throw new RuntimeException("Something is wrong with the token");
//        }

        return chain.filter(exchange);
    }
}
