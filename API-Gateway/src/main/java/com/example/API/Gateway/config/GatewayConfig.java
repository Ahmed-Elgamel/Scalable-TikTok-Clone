package com.example.API.Gateway.config;

import com.example.API.Gateway.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@Configuration
public class GatewayConfig {

    @Bean
    public WebFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}
