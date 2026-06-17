package cl.dressed.bff.controller;

import cl.dressed.bff.exception.BffException;
import cl.dressed.bff.security.JwtService;
import cl.dressed.bff.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final JwtService   jwtService;

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics(HttpServletRequest request) {
        String token = requireToken(request);
        requireAdmin(token);
        return ResponseEntity.ok(adminService.getMetrics(token));
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getUsers(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        String token = requireToken(request);
        requireAdmin(token);
        return ResponseEntity.ok(adminService.getUsers(token, page, size));
    }

    private String requireToken(HttpServletRequest request) {
        String token = jwtService.extractTokenFromCookie(request);
        if (token == null || token.isBlank()) {
            throw new BffException("No autenticado", HttpStatus.UNAUTHORIZED);
        }
        return token;
    }

    private void requireAdmin(String token) {
        String role = jwtService.extractRoleFromToken(token);
        if (!"admin".equals(role)) {
            throw new BffException("Acceso denegado", HttpStatus.FORBIDDEN);
        }
    }
}
