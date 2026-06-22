package com.gotechy.bookly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String PRODUCTOS_PATH = "/api/v1/productos/**";
    private static final String CATEGORIAS_PATH = "/api/v1/categorias/**";
    private static final String API_V1_PATH = "/api/v1/**";
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String CLIENTE_ROLE = "CLIENTE";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, PRODUCTOS_PATH).permitAll()
                .requestMatchers("/api/v1/usuarios/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.GET, CATEGORIAS_PATH).permitAll()
                .requestMatchers(HttpMethod.POST, API_V1_PATH).hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.PUT, API_V1_PATH).hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.PATCH, API_V1_PATH).hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.DELETE, API_V1_PATH).hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.GET, API_V1_PATH).hasAnyRole(ADMIN_ROLE, CLIENTE_ROLE)
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
