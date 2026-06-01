package cl.dressed.bff.service;

import cl.dressed.bff.client.OutfitClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OutfitService - pruebas unitarias")
class OutfitServiceTest {

    @Mock
    private OutfitClient outfitClient;

    @InjectMocks
    private OutfitService outfitService;

    @Test
    @DisplayName("generateOutfit() debería delegar en OutfitClient y retornar respuesta")
    void generateOutfit_delegaYRetornaRespuesta() {
        Map<String, Object> mockResponse = Map.of("outfitId", 1, "name", "Casual look");
        when(outfitClient.generateOutfit("jwt-token-mock")).thenReturn(mockResponse);

        Map<String, Object> resultado = outfitService.generateOutfit("jwt-token-mock");

        assertThat(resultado).containsEntry("name", "Casual look");
        verify(outfitClient, times(1)).generateOutfit("jwt-token-mock");
    }

    @Test
    @DisplayName("getMyOutfits() debería delegar en OutfitClient y retornar lista")
    void getMyOutfits_delegaYRetornaRespuesta() {
        List<Map<String, Object>> mockResponse = List.of(Map.of("outfitId", 1, "name", "Casual look"));
        when(outfitClient.getMyOutfits("jwt-token-mock")).thenReturn(mockResponse);

        List<Map<String, Object>> resultado = outfitService.getMyOutfits("jwt-token-mock");

        assertThat(resultado).hasSize(1);
        verify(outfitClient, times(1)).getMyOutfits("jwt-token-mock");
    }
}