package cl.dressed.bff.controller;

import cl.dressed.bff.service.CatalogService;
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

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("CatalogController - pruebas unitarias")
class CatalogControllerTest {

    @Mock
    private CatalogService catalogService;

    @InjectMocks
    private CatalogController catalogController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(catalogController).build();
    }

    @Test
    @DisplayName("GET /api/catalog/products debería retornar 200 con listado")
    void getProducts_retorna200ConListado() throws Exception {
        when(catalogService.getProducts(any(), any(), any(), anyInt(), anyInt(), any()))
                .thenReturn(Map.of("content", List.of(Map.of("id", 1, "name", "Polera")), "page", 0));

        mockMvc.perform(get("/api/catalog/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.content[0].name").value("Polera"));

        verify(catalogService, times(1)).getProducts(null, null, null, 0, 20, null);
    }
}