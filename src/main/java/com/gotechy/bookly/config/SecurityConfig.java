package com.gotechy.bookly.config;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gotechy.bookly.core.exception.ApiError;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String CLIENTE_ROLE = "CLIENTE";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
            )
            .authorizeHttpRequests(auth -> auth
                // Auth público
                .requestMatchers("/api/v1/auth/**").permitAll()

                // Catálogo público (GET)
                .requestMatchers(HttpMethod.GET, "/api/v1/productos", "/api/v1/productos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/categorias", "/api/v1/categorias/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/autores", "/api/v1/autores/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/editoriales", "/api/v1/editoriales/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/tipos-producto", "/api/v1/tipos-producto/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/rangos-etarios", "/api/v1/rangos-etarios/**").permitAll()

                // Usuarios: solo ADMIN
                .requestMatchers("/api/v1/usuarios/**").hasRole(ADMIN_ROLE)

                // Catálogo escritura: solo ADMIN
                .requestMatchers(HttpMethod.POST, "/api/v1/productos/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.PATCH, "/api/v1/productos/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**").hasRole(ADMIN_ROLE)

                .requestMatchers(HttpMethod.POST, "/api/v1/categorias/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.PUT, "/api/v1/categorias/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.PATCH, "/api/v1/categorias/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.DELETE, "/api/v1/categorias/**").hasRole(ADMIN_ROLE)

                .requestMatchers(HttpMethod.POST, "/api/v1/autores/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.PUT, "/api/v1/autores/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.PATCH, "/api/v1/autores/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.DELETE, "/api/v1/autores/**").hasRole(ADMIN_ROLE)

                .requestMatchers(HttpMethod.POST, "/api/v1/editoriales/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.PUT, "/api/v1/editoriales/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.PATCH, "/api/v1/editoriales/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.DELETE, "/api/v1/editoriales/**").hasRole(ADMIN_ROLE)

                .requestMatchers(HttpMethod.POST, "/api/v1/tipos-producto/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.PUT, "/api/v1/tipos-producto/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.PATCH, "/api/v1/tipos-producto/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.DELETE, "/api/v1/tipos-producto/**").hasRole(ADMIN_ROLE)

                .requestMatchers(HttpMethod.POST, "/api/v1/rangos-etarios/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.PUT, "/api/v1/rangos-etarios/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.PATCH, "/api/v1/rangos-etarios/**").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.DELETE, "/api/v1/rangos-etarios/**").hasRole(ADMIN_ROLE)

                // Envíos: lectura ADMIN/CLIENTE (ownership en servicio), escritura ADMIN
                .requestMatchers(HttpMethod.GET, "/api/v1/envios", "/api/v1/envios/**")
                    .hasAnyRole(ADMIN_ROLE, CLIENTE_ROLE)
                .requestMatchers(HttpMethod.POST, "/api/v1/envios").hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.PATCH, "/api/v1/envios/**").hasRole(ADMIN_ROLE)

                // Ventas: checkout y mis-ordenes para CLIENTE/ADMIN, listar todas solo ADMIN
                .requestMatchers(HttpMethod.POST, "/api/v1/ventas/checkout")
                    .hasAnyRole(ADMIN_ROLE, CLIENTE_ROLE)
                .requestMatchers(HttpMethod.GET, "/api/v1/ventas/mis-ordenes")
                    .hasAnyRole(ADMIN_ROLE, CLIENTE_ROLE)
                .requestMatchers(HttpMethod.GET, "/api/v1/ventas/**")
                    .hasAnyRole(ADMIN_ROLE, CLIENTE_ROLE)
                .requestMatchers(HttpMethod.GET, "/api/v1/ventas").hasRole(ADMIN_ROLE)

                // Carrito: solo CLIENTE/ADMIN autenticado
                .requestMatchers("/api/v1/carrito/**").hasAnyRole(ADMIN_ROLE, CLIENTE_ROLE)

                // Todo lo demás requiere autenticación
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            String code = "AUTH_REQUIRED";
            String message = "Debes autenticarte para acceder a este recurso.";

            Object errorCode = request.getAttribute("jwt.error.code");
            Object errorMessage = request.getAttribute("jwt.error.message");

            if (errorCode != null) {
                code = errorCode.toString();
                message = errorMessage != null ? errorMessage.toString() : message;
            }

            ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                code,
                message,
                request.getRequestURI(),
                null
            );

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.writeValue(response.getWriter(), apiError);
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                "INSUFFICIENT_ROLE",
                "No tienes permisos para acceder a este recurso.",
                request.getRequestURI(),
                java.util.List.of("Se requiere rol ADMIN.")
            );

            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.writeValue(response.getWriter(), apiError);
        };
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
