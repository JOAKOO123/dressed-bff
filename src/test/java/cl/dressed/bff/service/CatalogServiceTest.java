package cl.dressed.bff.service;

import cl.dressed.bff.client.CatalogClient;
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
}