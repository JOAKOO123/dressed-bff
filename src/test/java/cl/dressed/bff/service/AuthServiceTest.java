package cl.dressed.bff.service;

import cl.dressed.bff.client.AuthClient;
import cl.dressed.bff.dto.auth.ForgotPasswordRequestDTO;
import cl.dressed.bff.dto.auth.LoginRequestDTO;
import cl.dressed.bff.dto.auth.LoginResponseDTO;
import cl.dressed.bff.dto.auth.RegisterRequestDTO;
import cl.dressed.bff.dto.auth.RegisterResponseDTO;
import cl.dressed.bff.dto.auth.ResetPasswordRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService - pruebas unitarias")
class AuthServiceTest {

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private AuthService authService;

    private final LoginResponseDTO mockLogin = new LoginResponseDTO(
            1L, "test@dressed.cl", true, "jwt-token-mock");

    private final RegisterResponseDTO mockRegister = new RegisterResponseDTO(
            1L, "test@dressed.cl", true, LocalDateTime.now(), "jwt-token-mock");

    @Test
    @DisplayName("login() debería delegar en AuthClient y retornar respuesta")
    void login_delegaYRetornaRespuesta() {
        LoginRequestDTO request = new LoginRequestDTO("test@dressed.cl", "Password1!");
        when(authClient.login(request)).thenReturn(mockLogin);

        LoginResponseDTO resultado = authService.login(request);

        assertThat(resultado.email()).isEqualTo("test@dressed.cl");
        assertThat(resultado.token()).isEqualTo("jwt-token-mock");
        verify(authClient, times(1)).login(request);
    }

    @Test
    @DisplayName("login() debería llamar al cliente exactamente una vez")
    void login_llamaClienteUnaVez() {
        LoginRequestDTO request = new LoginRequestDTO("test@dressed.cl", "Password1!");
        when(authClient.login(request)).thenReturn(mockLogin);

        authService.login(request);

        verify(authClient, times(1)).login(request);
        verifyNoMoreInteractions(authClient);
    }

    @Test
    @DisplayName("register() debería delegar en AuthClient y retornar usuario creado")
    void register_delegaYRetornaUsuario() {
        RegisterRequestDTO request = new RegisterRequestDTO("test@dressed.cl", "Password1!");
        when(authClient.register(request)).thenReturn(mockRegister);

        RegisterResponseDTO resultado = authService.register(request);

        assertThat(resultado.email()).isEqualTo("test@dressed.cl");
        assertThat(resultado.active()).isTrue();
        verify(authClient, times(1)).register(request);
    }

    @Test
    @DisplayName("register() debería retornar usuario con todos sus campos")
    void register_retornaUsuarioCompleto() {
        RegisterRequestDTO request = new RegisterRequestDTO("test@dressed.cl", "Password1!");
        when(authClient.register(request)).thenReturn(mockRegister);

        RegisterResponseDTO resultado = authService.register(request);

        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.token()).isEqualTo("jwt-token-mock");
        assertThat(resultado.createdAt()).isNotNull();
    }

    @Test
    @DisplayName("forgotPassword() debería delegar en AuthClient sin retornar nada")
    void forgotPassword_delegaEnCliente() {
        ForgotPasswordRequestDTO request = new ForgotPasswordRequestDTO("test@dressed.cl");
        doNothing().when(authClient).forgotPassword(request);

        authService.forgotPassword(request);

        verify(authClient, times(1)).forgotPassword(request);
        verifyNoMoreInteractions(authClient);
    }

    @Test
    @DisplayName("resetPassword() debería delegar en AuthClient sin retornar nada")
    void resetPassword_delegaEnCliente() {
        ResetPasswordRequestDTO request = new ResetPasswordRequestDTO("token-abc", "NuevoPass1!");
        doNothing().when(authClient).resetPassword(request);

        authService.resetPassword(request);

        verify(authClient, times(1)).resetPassword(request);
        verifyNoMoreInteractions(authClient);
    }
}
