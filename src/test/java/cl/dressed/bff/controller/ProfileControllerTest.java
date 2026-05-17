package cl.dressed.bff.controller;

import cl.dressed.bff.dto.profile.CompletenessResponseDTO;
import cl.dressed.bff.dto.profile.MeasurementResponseDTO;
import cl.dressed.bff.dto.profile.ProfileResponseDTO;
import cl.dressed.bff.dto.profile.SizeResponseDTO;
import cl.dressed.bff.dto.profile.StyleResponseDTO;
import cl.dressed.bff.security.JwtService;
import cl.dressed.bff.service.ProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProfileController - pruebas unitarias")
class ProfileControllerTest {

    @Mock
    private ProfileService profileService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private ProfileController profileController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TOKEN = "jwt-token-mock";

    private final ProfileResponseDTO mockProfile = new ProfileResponseDTO(
            1L, 1L, "Juan Pérez", LocalDate.of(2000, 1, 1),
            25, "MASCULINO", "MEDIO", "FRIO");

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(profileController).build();
    }

    @Test
    @DisplayName("GET /api/users/profile debería retornar 200 con perfil")
    void getProfile_retorna200ConPerfil() throws Exception {
        when(jwtService.extractTokenFromCookie(any())).thenReturn(TOKEN);
        when(profileService.getProfile(TOKEN)).thenReturn(mockProfile);

        mockMvc.perform(get("/api/users/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Juan Pérez"))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    @DisplayName("PUT /api/users/profile debería retornar 200 con perfil actualizado")
    void updateProfile_retorna200() throws Exception {
        when(jwtService.extractTokenFromCookie(any())).thenReturn(TOKEN);
        when(profileService.updateProfile(eq(TOKEN), any())).thenReturn(mockProfile);

        mockMvc.perform(put("/api/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Juan\",\"gender\":\"MASCULINO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Juan Pérez"));
    }

    @Test
    @DisplayName("GET /api/users/profile/completeness debería retornar porcentaje")
    void getCompleteness_retornaPorcentaje() throws Exception {
        CompletenessResponseDTO mock = new CompletenessResponseDTO(
                75, List.of("sizes"), "Perfil incompleto");
        when(jwtService.extractTokenFromCookie(any())).thenReturn(TOKEN);
        when(profileService.getCompleteness(TOKEN)).thenReturn(mock);

        mockMvc.perform(get("/api/users/profile/completeness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.percentage").value(75));
    }

    @Test
    @DisplayName("GET /api/users/profile/styles debería retornar estilos")
    void getStyles_retornaEstilos() throws Exception {
        StyleResponseDTO mock = new StyleResponseDTO(Set.of("CASUAL"));
        when(jwtService.extractTokenFromCookie(any())).thenReturn(TOKEN);
        when(profileService.getStyles(TOKEN)).thenReturn(mock);

        mockMvc.perform(get("/api/users/profile/styles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.styles").isArray());
    }

    @Test
    @DisplayName("GET /api/users/sizes debería retornar tallas")
    void getSizes_retornaTallas() throws Exception {
        SizeResponseDTO mock = new SizeResponseDTO("M", "32", "42");
        when(jwtService.extractTokenFromCookie(any())).thenReturn(TOKEN);
        when(profileService.getSizes(TOKEN)).thenReturn(mock);

        mockMvc.perform(get("/api/users/sizes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.top").value("M"))
                .andExpect(jsonPath("$.bottom").value("32"));
    }

    @Test
    @DisplayName("GET /api/users/proportions debería retornar medidas")
    void getMeasurements_retornaMedidas() throws Exception {
        MeasurementResponseDTO mock = new MeasurementResponseDTO(
                new BigDecimal("175.0"), new BigDecimal("45.0"),
                new BigDecimal("95.0"), new BigDecimal("80.0"),
                new BigDecimal("95.0"), new BigDecimal("60.0"),
                new BigDecimal("90.0"));
        when(jwtService.extractTokenFromCookie(any())).thenReturn(TOKEN);
        when(profileService.getMeasurements(TOKEN)).thenReturn(mock);

        mockMvc.perform(get("/api/users/proportions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.heightCm").value(175.0));
    }
}
