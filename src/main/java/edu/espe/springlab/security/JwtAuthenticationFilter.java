package edu.espe.springlab.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extraer el header "Authorization"
        String authHeader = request.getHeader("Authorization");

        // 2. Verificar que empiece con "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // quitar "Bearer "

            // 3. Validar el token
            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.getEmailFromToken(token);

                // 4. Crear la autenticacion y meterla al contexto de Spring Security
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,           // principal (el email)
                                null,            // credentials
                                Collections.emptyList()  // authorities/roles
                        );
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 5. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}

///http://localhost:8080/api/auth/login
//{
//  "email": "jenn.nueva@espe.edu.ec",
//  "password": "1234"
//}