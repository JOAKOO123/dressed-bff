package cl.dressed.bff.service;

import cl.dressed.bff.client.AuthClient;
import cl.dressed.bff.dto.auth.ForgotPasswordRequestDTO;
import cl.dressed.bff.dto.auth.GoogleAuthRequestDTO;
import cl.dressed.bff.dto.auth.LoginRequestDTO;
import cl.dressed.bff.dto.auth.LoginResponseDTO;
import cl.dressed.bff.dto.auth.RegisterRequestDTO;
import cl.dressed.bff.dto.auth.RegisterResponseDTO;
import cl.dressed.bff.dto.auth.ResetPasswordRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthClient authClient;

    public LoginResponseDTO login(LoginRequestDTO request) {
        log.info("Intento de login para email: {}", request.email());
        return authClient.login(request);
    }

    public RegisterResponseDTO register(RegisterRequestDTO request) {
        log.info("Registro de nuevo usuario: {}", request.email());
        return authClient.register(request);
    }

    public void forgotPassword(ForgotPasswordRequestDTO request) {
        log.info("Solicitud de recuperación de password para: {}", request.email());
        authClient.forgotPassword(request);
    }

    public void resetPassword(ResetPasswordRequestDTO request) {
        log.info("Reset de password solicitado");
        authClient.resetPassword(request);
    }

    public Map<String, Object> loginWithGoogle(String credential) {
        log.info("Login con Google");
        return authClient.loginWithGoogle(credential);
    }
}
