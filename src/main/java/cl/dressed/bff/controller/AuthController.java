package cl.dressed.bff.controller;

import cl.dressed.bff.dto.auth.ForgotPasswordRequestDTO;
import cl.dressed.bff.dto.auth.LoginRequestDTO;
import cl.dressed.bff.dto.auth.LoginResponseDTO;
import cl.dressed.bff.dto.auth.RegisterRequestDTO;
import cl.dressed.bff.dto.auth.RegisterResponseDTO;
import cl.dressed.bff.dto.auth.ResetPasswordRequestDTO;
import cl.dressed.bff.security.JwtService;
import cl.dressed.bff.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @Value("${jwt.cookie.name}")
    private String cookieName;

    @Value("${jwt.cookie.max-age}")
    private int cookieMaxAge;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request,
            HttpServletResponse response) {

        LoginResponseDTO loginResponse = authService.login(request);

        String token = jwtService.extractTokenFromBackendResponse(loginResponse);
        addAuthCookie(response, token);

        return ResponseEntity.ok(new LoginResponseDTO(
                loginResponse.id(),
                loginResponse.email(),
                loginResponse.active(),
                loginResponse.token()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO request,
            HttpServletResponse response) {

        RegisterResponseDTO registerResponse = authService.register(request);

        String token = jwtService.extractTokenFromBackendResponse(registerResponse);
        addAuthCookie(response, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponseDTO(
                registerResponse.id(),
                registerResponse.email(),
                registerResponse.active(),
                registerResponse.createdAt(),
                registerResponse.token()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        log.info("Logout exitoso");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDTO request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody ResetPasswordRequestDTO request) {
        authService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponseDTO> me(HttpServletRequest request) {
        String token = jwtService.extractTokenFromCookie(request);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtService.extractEmailFromToken(token);
        Long userId = jwtService.extractUserIdFromToken(token);

        return ResponseEntity.ok(new LoginResponseDTO(userId, email, true, token));
    }

    private void addAuthCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(cookieMaxAge)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
