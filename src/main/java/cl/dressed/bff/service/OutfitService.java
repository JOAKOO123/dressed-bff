package cl.dressed.bff.service;

import cl.dressed.bff.client.OutfitClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutfitService {

    private final OutfitClient outfitClient;

    public Map<String, Object> generateOutfit(String token) {
        log.info("Generando outfit para usuario");
        return outfitClient.generateOutfit(token);
    }

    public List<Map<String, Object>> getMyOutfits(String token) {
        log.info("Obteniendo outfits del usuario");
        return outfitClient.getMyOutfits(token);
    }
}