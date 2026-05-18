package cl.dressed.bff.client;

import cl.dressed.bff.dto.auth.ForgotPasswordRequestDTO;
import cl.dressed.bff.dto.auth.LoginRequestDTO;
import cl.dressed.bff.dto.auth.LoginResponseDTO;
import cl.dressed.bff.dto.auth.RegisterRequestDTO;
import cl.dressed.bff.dto.auth.RegisterResponseDTO;
import cl.dressed.bff.dto.auth.ResetPasswordRequestDTO;
import cl.dressed.bff.exception.BffException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthClient {

    private final WebClient backendClient;

    public LoginResponseDTO login(LoginRequestDTO request) {
        return backendClient.post()
                .uri("/api/auth/login")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> {
                                    log.warn("Login fallido para email: {}", request.email());
                                    return new BffException("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
                                }))
                .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("Datos inválidos", HttpStatus.BAD_REQUEST)))
                .bodyToMono(LoginResponseDTO.class)
                .block();
    }

    public RegisterResponseDTO register(RegisterRequestDTO request) {
        return backendClient.post()
                .uri("/api/auth/register")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.CONFLICT::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("El email ya está registrado", HttpStatus.CONFLICT)))
                .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("Datos inválidos", HttpStatus.BAD_REQUEST)))
                .bodyToMono(RegisterResponseDTO.class)
                .block();
    }

    public void forgotPassword(ForgotPasswordRequestDTO request) {
        backendClient.post()
                .uri("/api/auth/forgot-password")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("Datos inválidos", HttpStatus.BAD_REQUEST)))
                .bodyToMono(Void.class)
                .block();
    }

    public void resetPassword(ResetPasswordRequestDTO request) {
        backendClient.post()
                .uri("/api/auth/reset-password")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("Token inválido o expirado", HttpStatus.BAD_REQUEST)))
                .bodyToMono(Void.class)
                .block();
    }
}
