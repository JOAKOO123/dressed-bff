package cl.dressed.bff.service;

import cl.dressed.bff.client.ProfileClient;
import cl.dressed.bff.dto.profile.CompletenessResponseDTO;
import cl.dressed.bff.dto.profile.MeasurementRequestDTO;
import cl.dressed.bff.dto.profile.MeasurementResponseDTO;
import cl.dressed.bff.dto.profile.ProfileResponseDTO;
import cl.dressed.bff.dto.profile.ProfileUpdateRequestDTO;
import cl.dressed.bff.dto.profile.SizeRequestDTO;
import cl.dressed.bff.dto.profile.SizeResponseDTO;
import cl.dressed.bff.dto.profile.SkinUpdateRequestDTO;
import cl.dressed.bff.dto.profile.StyleRequestDTO;
import cl.dressed.bff.dto.profile.StyleResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProfileService - pruebas unitarias")
class ProfileServiceTest {

    @Mock
    private ProfileClient profileClient;

    @InjectMocks
    private ProfileService profileService;

    private static final String TOKEN = "jwt-token-mock";

    private final ProfileResponseDTO mockProfile = new ProfileResponseDTO(
            1L, 1L, "Juan Pérez", LocalDate.of(2000, 1, 1),
            25, "MASCULINO", "MEDIO", "FRIO");

    @Test
    @DisplayName("getProfile() debería delegar en ProfileClient y retornar perfil")
    void getProfile_delegaYRetornaPerfil() {
        when(profileClient.getProfile(TOKEN)).thenReturn(mockProfile);

        ProfileResponseDTO resultado = profileService.getProfile(TOKEN);

        assertThat(resultado.name()).isEqualTo("Juan Pérez");
        assertThat(resultado.userId()).isEqualTo(1L);
        verify(profileClient, times(1)).getProfile(TOKEN);
    }

    @Test
    @DisplayName("updateProfile() debería delegar en ProfileClient con los datos correctos")
    void updateProfile_delegaConDatosCorrectos() {
        ProfileUpdateRequestDTO request = new ProfileUpdateRequestDTO(
                "Juan Actualizado", LocalDate.of(2000, 1, 1), "MASCULINO");
        when(profileClient.updateProfile(TOKEN, request)).thenReturn(mockProfile);

        ProfileResponseDTO resultado = profileService.updateProfile(TOKEN, request);

        assertThat(resultado).isNotNull();
        verify(profileClient, times(1)).updateProfile(TOKEN, request);
    }

    @Test
    @DisplayName("updateSkin() debería delegar en ProfileClient")
    void updateSkin_delegaEnCliente() {
        SkinUpdateRequestDTO request = new SkinUpdateRequestDTO("MEDIO", "FRIO");
        when(profileClient.updateSkin(TOKEN, request)).thenReturn(mockProfile);

        ProfileResponseDTO resultado = profileService.updateSkin(TOKEN, request);

        assertThat(resultado.skinTone()).isEqualTo("MEDIO");
        verify(profileClient, times(1)).updateSkin(TOKEN, request);
    }

    @Test
    @DisplayName("getCompleteness() debería retornar porcentaje de completitud")
    void getCompleteness_retornaPorcentaje() {
        CompletenessResponseDTO mock = new CompletenessResponseDTO(
                75, List.of("sizes", "measurements"), "Perfil incompleto");
        when(profileClient.getCompleteness(TOKEN)).thenReturn(mock);

        CompletenessResponseDTO resultado = profileService.getCompleteness(TOKEN);

        assertThat(resultado.percentage()).isEqualTo(75);
        assertThat(resultado.missing()).hasSize(2);
        verify(profileClient, times(1)).getCompleteness(TOKEN);
    }

    @Test
    @DisplayName("getStyles() debería retornar estilos del usuario")
    void getStyles_retornaEstilos() {
        StyleResponseDTO mock = new StyleResponseDTO(Set.of("CASUAL", "STREETWEAR"));
        when(profileClient.getStyles(TOKEN)).thenReturn(mock);

        StyleResponseDTO resultado = profileService.getStyles(TOKEN);

        assertThat(resultado.styles()).contains("CASUAL");
        verify(profileClient, times(1)).getStyles(TOKEN);
    }

    @Test
    @DisplayName("updateStyles() debería delegar en ProfileClient con estilos correctos")
    void updateStyles_delegaConEstilosCorrectos() {
        StyleRequestDTO request = new StyleRequestDTO(Set.of("FORMAL"));
        StyleResponseDTO mock = new StyleResponseDTO(Set.of("FORMAL"));
        when(profileClient.updateStyles(TOKEN, request)).thenReturn(mock);

        StyleResponseDTO resultado = profileService.updateStyles(TOKEN, request);

        assertThat(resultado.styles()).contains("FORMAL");
        verify(profileClient, times(1)).updateStyles(TOKEN, request);
    }

    @Test
    @DisplayName("getSizes() debería retornar tallas del usuario")
    void getSizes_retornaTallas() {
        SizeResponseDTO mock = new SizeResponseDTO("M", "32", "42");
        when(profileClient.getSizes(TOKEN)).thenReturn(mock);

        SizeResponseDTO resultado = profileService.getSizes(TOKEN);

        assertThat(resultado.top()).isEqualTo("M");
        assertThat(resultado.bottom()).isEqualTo("32");
        verify(profileClient, times(1)).getSizes(TOKEN);
    }

    @Test
    @DisplayName("updateSizes() debería delegar en ProfileClient con tallas correctas")
    void updateSizes_delegaConTallasCorrectas() {
        SizeRequestDTO request = new SizeRequestDTO("L", "34", "43");
        SizeResponseDTO mock = new SizeResponseDTO("L", "34", "43");
        when(profileClient.updateSizes(TOKEN, request)).thenReturn(mock);

        SizeResponseDTO resultado = profileService.updateSizes(TOKEN, request);

        assertThat(resultado.top()).isEqualTo("L");
        verify(profileClient, times(1)).updateSizes(TOKEN, request);
    }

    @Test
    @DisplayName("getMeasurements() debería retornar medidas del usuario")
    void getMeasurements_retornaMedidas() {
        MeasurementResponseDTO mock = new MeasurementResponseDTO(
                new BigDecimal("175.0"), new BigDecimal("45.0"), new BigDecimal("95.0"),
                new BigDecimal("80.0"), new BigDecimal("95.0"),
                new BigDecimal("60.0"), new BigDecimal("90.0"));
        when(profileClient.getMeasurements(TOKEN)).thenReturn(mock);

        MeasurementResponseDTO resultado = profileService.getMeasurements(TOKEN);

        assertThat(resultado.heightCm()).isEqualTo(new BigDecimal("175.0"));
        verify(profileClient, times(1)).getMeasurements(TOKEN);
    }

    @Test
    @DisplayName("updateMeasurements() debería delegar en ProfileClient con medidas correctas")
    void updateMeasurements_delegaConMedidasCorrectas() {
        MeasurementRequestDTO request = new MeasurementRequestDTO(
                new BigDecimal("175.0"), new BigDecimal("45.0"), new BigDecimal("95.0"),
                new BigDecimal("80.0"), new BigDecimal("95.0"),
                new BigDecimal("60.0"), new BigDecimal("90.0"));
        MeasurementResponseDTO mock = new MeasurementResponseDTO(
                new BigDecimal("175.0"), new BigDecimal("45.0"), new BigDecimal("95.0"),
                new BigDecimal("80.0"), new BigDecimal("95.0"),
                new BigDecimal("60.0"), new BigDecimal("90.0"));
        when(profileClient.updateMeasurements(TOKEN, request)).thenReturn(mock);

        MeasurementResponseDTO resultado = profileService.updateMeasurements(TOKEN, request);

        assertThat(resultado.heightCm()).isEqualTo(new BigDecimal("175.0"));
        verify(profileClient, times(1)).updateMeasurements(TOKEN, request);
    }
}
