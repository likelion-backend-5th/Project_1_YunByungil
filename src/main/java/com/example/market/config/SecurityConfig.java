package com.example.market.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final MyCustomDsl myCustomDsl;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> web.ignoring()
                .requestMatchers("/static/**"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        http
                .authorizeHttpRequests(authHttp -> authHttp
                        .requestMatchers("/join", "/views/**", "/token/**")
                        .permitAll()
                        .requestMatchers("/", "/css/**", "/js/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/items/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .apply(myCustomDsl);

        return http.build();
    }
}
