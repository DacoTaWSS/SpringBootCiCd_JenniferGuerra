package edu.espe.springlab.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Desactivar CSRF (no se necesita con JWT stateless)
            .csrf(csrf -> csrf.disable())

            // Politica de sesion: STATELESS (sin sesiones en servidor)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Reglas de autorizacion
            .authorizeHttpRequests(auth -> auth
                // Endpoints publicos (login)
                    .requestMatchers("/api/students/**").permitAll()
                // Todo lo demas requiere autenticacion
                .anyRequest().authenticated()
            )
            // Agregar nuestro filtro JWT ANTES del filtro de autenticacion de Spring
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
