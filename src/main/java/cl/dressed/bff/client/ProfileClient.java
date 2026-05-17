package cl.dressed.bff.client;

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
import cl.dressed.bff.exception.BffException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileClient {

    private final WebClient backendClient;

    // =============================================
    // PROFILE
    // =============================================

    public ProfileResponseDTO getProfile(String token) {
        return backendClient.get()
                .uri("/api/users/profile")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("Perfil no encontrado", HttpStatus.NOT_FOUND)))
                .bodyToMono(ProfileResponseDTO.class)
                .block();
    }

    public ProfileResponseDTO updateProfile(String token, ProfileUpdateRequestDTO request) {
        return backendClient.put()
                .uri("/api/users/profile")
                .header("Authorization", "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("Datos inválidos", HttpStatus.BAD_REQUEST)))
                .bodyToMono(ProfileResponseDTO.class)
                .block();
    }

    public ProfileResponseDTO updateSkin(String token, SkinUpdateRequestDTO request) {
        return backendClient.put()
                .uri("/api/users/profile/skin")
                .header("Authorization", "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("Datos inválidos", HttpStatus.BAD_REQUEST)))
                .bodyToMono(ProfileResponseDTO.class)
                .block();
    }

    public CompletenessResponseDTO getCompleteness(String token) {
        return backendClient.get()
                .uri("/api/users/profile/completeness")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .bodyToMono(CompletenessResponseDTO.class)
                .block();
    }

    // =============================================
    // STYLES
    // =============================================

    public StyleResponseDTO getStyles(String token) {
        return backendClient.get()
                .uri("/api/users/profile/styles")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .bodyToMono(StyleResponseDTO.class)
                .block();
    }

    public StyleResponseDTO updateStyles(String token, StyleRequestDTO request) {
        return backendClient.put()
                .uri("/api/users/profile/styles")
                .header("Authorization", "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("Datos inválidos", HttpStatus.BAD_REQUEST)))
                .bodyToMono(StyleResponseDTO.class)
                .block();
    }

    // =============================================
    // SIZES
    // =============================================

    public SizeResponseDTO getSizes(String token) {
        return backendClient.get()
                .uri("/api/users/sizes")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .bodyToMono(SizeResponseDTO.class)
                .block();
    }

    public SizeResponseDTO updateSizes(String token, SizeRequestDTO request) {
        return backendClient.put()
                .uri("/api/users/sizes")
                .header("Authorization", "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("Datos inválidos", HttpStatus.BAD_REQUEST)))
                .bodyToMono(SizeResponseDTO.class)
                .block();
    }

    // =============================================
    // MEASUREMENTS
    // =============================================

    public MeasurementResponseDTO getMeasurements(String token) {
        return backendClient.get()
                .uri("/api/users/proportions")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .bodyToMono(MeasurementResponseDTO.class)
                .block();
    }

    public MeasurementResponseDTO updateMeasurements(String token, MeasurementRequestDTO request) {
        return backendClient.put()
                .uri("/api/users/proportions")
                .header("Authorization", "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("Datos inválidos", HttpStatus.BAD_REQUEST)))
                .bodyToMono(MeasurementResponseDTO.class)
                .block();
    }
}
