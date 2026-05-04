package edu.espe.springlab.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    // Tomar tiempo de inicio, registrar endpoint Y el usuario autenticado
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        req.setAttribute("t0", System.currentTimeMillis());

        // Obtener el usuario del JWT (ya validado por el filtro de Spring Security)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null && auth.isAuthenticated())
                ? auth.getPrincipal().toString()
                : "anonimo";

        System.out.println("preHandle: " + req.getMethod() + " " + req.getRequestURI()
                + " | usuario: " + user);
        return true;
    }

    // Calcula la duracion total y registrar el status HTTP
    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp,
                                Object handler, Exception ex) {
        Long t0 = (Long) req.getAttribute("t0");
        long elapsed = (t0 == null) ? -1 : (System.currentTimeMillis() - t0);

        System.out.println("afterCompletion -> status: " + resp.getStatus()
                + " tiempo: " + elapsed + " ms");
    }
}