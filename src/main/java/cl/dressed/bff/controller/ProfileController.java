package cl.dressed.bff.controller;

import cl.dressed.bff.dto.profile.*;
import cl.dressed.bff.exception.BffException;
import cl.dressed.bff.security.JwtService;
import cl.dressed.bff.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final JwtService jwtService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponseDTO> getProfile(HttpServletRequest request) {
        String token = requireToken(request);
        return ResponseEntity.ok(profileService.getProfile(token));
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileResponseDTO> updateProfile(
            @Valid @RequestBody ProfileUpdateRequestDTO dto,
            HttpServletRequest request) {
        String token = requireToken(request);
        return ResponseEntity.ok(profileService.updateProfile(token, dto));
    }

    @PutMapping("/profile/skin")
    public ResponseEntity<ProfileResponseDTO> updateSkin(
            @RequestBody SkinUpdateRequestDTO dto,
            HttpServletRequest request) {
        String token = requireToken(request);
        return ResponseEntity.ok(profileService.updateSkin(token, dto));
    }

    @GetMapping("/profile/completeness")
    public ResponseEntity<CompletenessResponseDTO> getCompleteness(HttpServletRequest request) {
        String token = requireToken(request);
        return ResponseEntity.ok(profileService.getCompleteness(token));
    }

    @GetMapping("/profile/styles")
    public ResponseEntity<StyleResponseDTO> getStyles(HttpServletRequest request) {
        String token = requireToken(request);
        return ResponseEntity.ok(profileService.getStyles(token));
    }

    @PutMapping("/profile/styles")
    public ResponseEntity<StyleResponseDTO> updateStyles(
            @Valid @RequestBody StyleRequestDTO dto,
            HttpServletRequest request) {
        String token = requireToken(request);
        return ResponseEntity.ok(profileService.updateStyles(token, dto));
    }

    @GetMapping("/sizes")
    public ResponseEntity<SizeResponseDTO> getSizes(HttpServletRequest request) {
        String token = requireToken(request);
        return ResponseEntity.ok(profileService.getSizes(token));
    }

    @PutMapping("/sizes")
    public ResponseEntity<SizeResponseDTO> updateSizes(
            @Valid @RequestBody SizeRequestDTO dto,
            HttpServletRequest request) {
        String token = requireToken(request);
        return ResponseEntity.ok(profileService.updateSizes(token, dto));
    }

    @GetMapping("/proportions")
    public ResponseEntity<MeasurementResponseDTO> getMeasurements(HttpServletRequest request) {
        String token = requireToken(request);
        return ResponseEntity.ok(profileService.getMeasurements(token));
    }

    @PutMapping("/proportions")
    public ResponseEntity<MeasurementResponseDTO> updateMeasurements(
            @Valid @RequestBody MeasurementRequestDTO dto,
            HttpServletRequest request) {
        String token = requireToken(request);
        return ResponseEntity.ok(profileService.updateMeasurements(token, dto));
    }

    private String requireToken(HttpServletRequest request) {
        String token = jwtService.extractTokenFromCookie(request);
        if (token == null || token.isBlank()) {
            throw new BffException("No autenticado", HttpStatus.UNAUTHORIZED);
        }
        return token;
    }
}