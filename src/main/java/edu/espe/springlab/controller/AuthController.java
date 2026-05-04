package edu.espe.springlab.controller;

import edu.espe.springlab.dto.AuthRequest;
import edu.espe.springlab.dto.AuthResponse;
import edu.espe.springlab.repository.StudentRepository;
import edu.espe.springlab.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final StudentRepository studentRepo;

    private static final String LAB_PASSWORD = "1234";

    public AuthController(JwtUtil jwtUtil, StudentRepository studentRepo) {
        this.jwtUtil = jwtUtil;
        this.studentRepo = studentRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {

        // 1. Verificar que el estudiante exista por email
        boolean exists = studentRepo.existsByEmail(request.getEmail());
        if (!exists) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Credenciales invalidas"));
        }

        // 2. Verificar password (fija para el lab)
        if (!LAB_PASSWORD.equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Credenciales invalidas"));
        }

        // 3. Generar token JWT
        String token = jwtUtil.generateToken(request.getEmail());

        return ResponseEntity.ok(new AuthResponse(token, request.getEmail()));
    }
}
