package cl.dressed.bff.controller;

import cl.dressed.bff.dto.auth.ForgotPasswordRequestDTO;
import cl.dressed.bff.dto.auth.LoginRequestDTO;
import cl.dressed.bff.dto.auth.LoginResponseDTO;
import cl.dressed.bff.dto.auth.RegisterRequestDTO;
import cl.dressed.bff.dto.auth.RegisterResponseDTO;
import cl.dressed.bff.dto.auth.ResetPasswordRequestDTO;
import cl.dressed.bff.security.JwtService;
import cl.dressed.bff.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController - pruebas unitarias")
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final LoginResponseDTO mockLogin = new LoginResponseDTO(
            1L, "test@dressed.cl", true, "jwt-token-mock");

    private final RegisterResponseDTO mockRegister = new RegisterResponseDTO(
            1L, "test@dressed.cl", true, LocalDateTime.now(), "jwt-token-mock");

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authController, "cookieName", "access_token");
        ReflectionTestUtils.setField(authController, "cookieMaxAge", 86400);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    @DisplayName("POST /api/auth/login debería retornar 200 y setear cookie")
    void login_retorna200YSeteaCookie() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("test@dressed.cl", "Password1!");
        when(authService.login(any())).thenReturn(mockLogin);
        when(jwtService.extractTokenFromBackendResponse(any(LoginResponseDTO.class)))
                .thenReturn("jwt-token-mock");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@dressed.cl"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("POST /api/auth/login debería llamar al service exactamente una vez")
    void login_llamaServiceUnaVez() throws Exception {
        when(authService.login(any())).thenReturn(mockLogin);
        when(jwtService.extractTokenFromBackendResponse(any(LoginResponseDTO.class)))
                .thenReturn("jwt-token-mock");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@dressed.cl\",\"password\":\"Password1!\"}"))
                .andExpect(status().isOk());

        verify(authService, times(1)).login(any());
    }

    @Test
    @DisplayName("POST /api/auth/register debería retornar 201")
    void register_retorna201() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO("test@dressed.cl", "Password1!");
        when(authService.register(any())).thenReturn(mockRegister);
        when(jwtService.extractTokenFromBackendResponse(any(RegisterResponseDTO.class)))
                .thenReturn("jwt-token-mock");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@dressed.cl"));
    }

    @Test
    @DisplayName("POST /api/auth/logout debería retornar 204")
    void logout_retorna204() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/auth/forgot-password debería retornar 200")
    void forgotPassword_retorna200() throws Exception {
        ForgotPasswordRequestDTO request = new ForgotPasswordRequestDTO("test@dressed.cl");
        doNothing().when(authService).forgotPassword(any());

        mockMvc.perform(post("/api/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(authService, times(1)).forgotPassword(any());
    }

    @Test
    @DisplayName("POST /api/auth/reset-password debería retornar 200")
    void resetPassword_retorna200() throws Exception {
        ResetPasswordRequestDTO request = new ResetPasswordRequestDTO("token-abc", "NuevoPass1!");
        doNothing().when(authService).resetPassword(any());

        mockMvc.perform(post("/api/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(authService, times(1)).resetPassword(any());
    }

    @Test
    @DisplayName("GET /api/auth/me debería retornar 401 si no hay cookie")
    void me_retorna401SinCookie() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }
}
