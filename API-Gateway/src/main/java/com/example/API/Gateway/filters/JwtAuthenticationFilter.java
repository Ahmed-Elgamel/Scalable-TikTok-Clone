package com.example.API.Gateway.filters;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.Key;


/*
 This class will be used to intercept the request coming from the user/frontend and
 it will extract the token from request to authenticate the user
 If the user is authenticated and has a valid token then it will get the userid from this token
 and it will inject this userID in the request headers of incoming requests to the services
 This makes the other services stateless and do not need to know or store info about the user
 because the api gateway handles this task
 The userId received by the services is TRUSTED!!!
 */
@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private static final String LOGIN_PATH = "/user/login";
    private static final String SIGNUP_PATH = "/user/signup";
    private static final String SIGNUP_PATH2 = "/user/addUser";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Skip authentication for login and signup paths
        String path = exchange.getRequest().getURI().getPath();
        if (path.equals(LOGIN_PATH) || path.equals(SIGNUP_PATH) || path.equals(SIGNUP_PATH2)) {
            System.out.println("111111111111111");
            return chain.filter(exchange); // Skip the filter for these paths
        }

        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (token == null || !token.startsWith("Bearer ")) {
            System.out.println("22222222222222222 "+ token);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            System.out.println("zzzzzzzzzzzzzzzzzzzzzzz "+ token);
            Claims claims = extractClaims(token);
            System.out.println("33333333333333333");

            String userId = claims.getSubject();

            // Attach userId as a header for downstream services
            exchange.getRequest().mutate()
                    .header("X-User-Id", userId)
                    .build();

        } catch (ExpiredJwtException e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().add("Error-Message", "Token expired");
            return exchange.getResponse().setComplete();
        } catch (MalformedJwtException | SignatureException e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().add("Error-Message", "Invalid token");
            return exchange.getResponse().setComplete();
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().add("Error-Message", "Authentication failed");
            return exchange.getResponse().setComplete();
        }
        System.out.println("fininsinssinsnisnnsisnisnsiisni");
        return chain.filter(exchange);
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey()) // Ensure this returns the key properly
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired");
            throw e;
        } catch (JwtException e) {
            System.out.println("Token is invalid");
            throw e;
        }
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

}
