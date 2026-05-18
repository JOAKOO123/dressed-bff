package cl.dressed.bff.service;

import cl.dressed.bff.client.CatalogClient;
import cl.dressed.bff.dto.catalog.GarmentResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CatalogService - pruebas unitarias")
class CatalogServiceTest {

    @Mock
    private CatalogClient catalogClient;

    @InjectMocks
    private CatalogService catalogService;

    @Test
    @DisplayName("getProducts() debería delegar en CatalogClient y retornar respuesta")
    void getProducts_delegaYRetornaRespuesta() {
        Map<String, Object> mock = Map.of("content", List.of(Map.of("id", 1, "name", "Polera")));
        when(catalogClient.getProducts("tops", "M", true, 0, 20, "price,asc")).thenReturn(mock);

        Map<String, Object> resultado = catalogService.getProducts("tops", "M", true, 0, 20, "price,asc");

        assertThat(resultado).containsKey("content");
        verify(catalogClient, times(1)).getProducts("tops", "M", true, 0, 20, "price,asc");
    }

    @Test
    @DisplayName("getProductById() debería delegar en CatalogClient y retornar prenda")
    void getProductById_delegaYRetornaPrenda() {
        GarmentResponseDTO mock = new GarmentResponseDTO(
                1, 2, "Polera", new BigDecimal("12990.00"),
                "https://img", "https://product", "tops",
                List.of("M", "L"), "negro", "regular", "casual", true,
                LocalDateTime.now());
        when(catalogClient.getProductById(1)).thenReturn(mock);

        GarmentResponseDTO resultado = catalogService.getProductById(1);

        assertThat(resultado.name()).isEqualTo("Polera");
        verify(catalogClient, times(1)).getProductById(1);
    }
}