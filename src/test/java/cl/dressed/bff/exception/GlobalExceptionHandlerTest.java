package cl.dressed.bff.exception;

import cl.dressed.bff.controller.AuthController;
import cl.dressed.bff.security.JwtService;
import cl.dressed.bff.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler - pruebas unitarias")
class GlobalExceptionHandlerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authController, "cookieName", "access_token");
        ReflectionTestUtils.setField(authController, "cookieMaxAge", 86400);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setValidator(validator)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("debería retornar 500 cuando ocurre RuntimeException")
    void handleRuntimeException_retorna500() throws Exception {
        when(authService.login(any()))
                .thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"email\":\"test@dressed.cl\",\"password\":\"Password1!\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Error inesperado"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("debería retornar status correcto cuando ocurre BffException con 401")
    void handleBffException_retorna401() throws Exception {
        when(authService.login(any()))
                .thenThrow(new BffException("Credenciales incorrectas", HttpStatus.UNAUTHORIZED));

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"email\":\"test@dressed.cl\",\"password\":\"Password1!\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Credenciales incorrectas"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("debería retornar 409 cuando ocurre BffException con CONFLICT")
    void handleBffException_retorna409() throws Exception {
        when(authService.login(any()))
                .thenThrow(new BffException("El email ya está registrado", HttpStatus.CONFLICT));

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"email\":\"test@dressed.cl\",\"password\":\"Password1!\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("El email ya está registrado"));
    }

    @Test
    @DisplayName("debería retornar 400 cuando falla la validación")
    void handleValidation_retorna400() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"email\":\"no-es-email\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fields").exists());
    }

    @Test
    @DisplayName("debería retornar 503 cuando ocurre BffException con SERVICE_UNAVAILABLE")
    void handleBffException_retorna503() throws Exception {
        when(authService.login(any()))
                .thenThrow(new BffException("Servicio no disponible", HttpStatus.SERVICE_UNAVAILABLE));

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"email\":\"test@dressed.cl\",\"password\":\"Password1!\"}"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(503))
                .andExpect(jsonPath("$.message").value("Servicio no disponible"));
    }

    @Test
    @DisplayName("la respuesta de error siempre debería incluir timestamp")
    void errorResponse_siempreIncluyeTimestamp() throws Exception {
        when(authService.login(any()))
                .thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"email\":\"test@dressed.cl\",\"password\":\"Password1!\"}"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }
}
