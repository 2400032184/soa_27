package com.klu.security;

import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    public JwtGatewayFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        String path = exchange.getRequest()
                .getURI()
                .getPath();

        // Login and signup do not require JWT
        if (path.equals("/user/signup")
                || path.equals("/user/login")) {

            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        // No token
        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {

            Claims claims = jwtUtil.validateToken(token);

            Long userId = claims.get("id", Long.class);
            String username = claims.get("username", String.class);
            String role = claims.get("role", String.class);

            if (userId == null
                    || username == null
                    || role == null) {

                exchange.getResponse()
                        .setStatusCode(HttpStatus.UNAUTHORIZED);

                return exchange.getResponse().setComplete();
            }

            // Add authenticated user information
            // to the request sent to the microservice
            ServerHttpRequest modifiedRequest =
                    exchange.getRequest()
                            .mutate()
                            .header(
                                "X-User-Id",
                                String.valueOf(userId)
                            )
                            .header(
                                "X-Username",
                                username
                            )
                            .header(
                                "X-User-Role",
                                role
                            )
                            .build();

            ServerWebExchange modifiedExchange =
                    exchange.mutate()
                            .request(modifiedRequest)
                            .build();

            return chain.filter(modifiedExchange);

        } catch (Exception e) {

            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}