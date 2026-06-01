package cl.dressed.bff.controller;

import cl.dressed.bff.security.JwtService;
import cl.dressed.bff.service.OutfitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("OutfitController - pruebas unitarias")
class OutfitControllerTest {

    @Mock
    private OutfitService outfitService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private OutfitController outfitController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(outfitController).build();
    }

    @Test
    @DisplayName("POST /api/outfits/generate debería retornar 200 con outfit generado")
    void generateOutfit_retorna200() throws Exception {
        when(jwtService.extractTokenFromCookie(any())).thenReturn("jwt-token-mock");
        when(outfitService.generateOutfit("jwt-token-mock"))
                .thenReturn(Map.of("outfitId", 1, "name", "Casual look"));

        mockMvc.perform(post("/api/outfits/generate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Casual look"));

        verify(outfitService, times(1)).generateOutfit("jwt-token-mock");
    }

    @Test
    @DisplayName("GET /api/outfits debería retornar 200 con lista de outfits")
    void getMyOutfits_retorna200() throws Exception {
        when(jwtService.extractTokenFromCookie(any())).thenReturn("jwt-token-mock");
        when(outfitService.getMyOutfits("jwt-token-mock"))
                .thenReturn(List.of(Map.of("outfitId", 1, "name", "Casual look")));

        mockMvc.perform(get("/api/outfits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Casual look"));

        verify(outfitService, times(1)).getMyOutfits("jwt-token-mock");
    }

    @Test
    @DisplayName("POST /api/outfits/generate debería retornar 401 si no hay token")
    void generateOutfit_retorna401SinToken() throws Exception {
        when(jwtService.extractTokenFromCookie(any())).thenReturn(null);

        mockMvc.perform(post("/api/outfits/generate"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/outfits debería retornar 401 si no hay token")
    void getMyOutfits_retorna401SinToken() throws Exception {
        when(jwtService.extractTokenFromCookie(any())).thenReturn("   ");

        mockMvc.perform(get("/api/outfits"))
                .andExpect(status().isUnauthorized());
    }
}