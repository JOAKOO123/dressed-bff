package cl.dressed.bff.controller;

import cl.dressed.bff.exception.BffException;
import cl.dressed.bff.security.JwtService;
import cl.dressed.bff.service.OutfitService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/outfits")
@RequiredArgsConstructor
public class OutfitController {

    private final OutfitService outfitService;
    private final JwtService jwtService;

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateOutfit(HttpServletRequest request) {
        String token = requireToken(request);
        return ResponseEntity.ok(outfitService.generateOutfit(token));
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getMyOutfits(HttpServletRequest request) {
        String token = requireToken(request);
        return ResponseEntity.ok(outfitService.getMyOutfits(token));
    }

    private String requireToken(HttpServletRequest request) {
        String token = jwtService.extractTokenFromCookie(request);
        if (token == null || token.isBlank()) {
            throw new BffException("No autenticado", HttpStatus.UNAUTHORIZED);
        }
        return token;
    }
}