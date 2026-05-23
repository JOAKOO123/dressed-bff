package cl.dressed.bff.client;

import cl.dressed.bff.dto.profile.*;
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

    public ProfileResponseDTO getProfile(String token) {
        return backendClient.get()
                .uri("/api/users/profile")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.NOT_FOUND::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Perfil no encontrado", HttpStatus.NOT_FOUND)))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Error interno del servidor", HttpStatus.BAD_GATEWAY)))
                .bodyToMono(ProfileResponseDTO.class)
                .block();
    }

    public ProfileResponseDTO updateProfile(String token, ProfileUpdateRequestDTO request) {
        return backendClient.put()
                .uri("/api/users/profile")
                .header("Authorization", "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.BAD_REQUEST::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Datos inválidos", HttpStatus.BAD_REQUEST)))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Error interno del servidor", HttpStatus.BAD_GATEWAY)))
                .bodyToMono(ProfileResponseDTO.class)
                .block();
    }

    public ProfileResponseDTO updateSkin(String token, SkinUpdateRequestDTO request) {
        return backendClient.put()
                .uri("/api/users/profile/skin")
                .header("Authorization", "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.BAD_REQUEST::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Datos inválidos", HttpStatus.BAD_REQUEST)))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Error interno del servidor", HttpStatus.BAD_GATEWAY)))
                .bodyToMono(ProfileResponseDTO.class)
                .block();
    }

    public CompletenessResponseDTO getCompleteness(String token) {
        return backendClient.get()
                .uri("/api/users/profile/completeness")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Error interno del servidor", HttpStatus.BAD_GATEWAY)))
                .bodyToMono(CompletenessResponseDTO.class)
                .block();
    }

    public StyleResponseDTO getStyles(String token) {
        return backendClient.get()
                .uri("/api/users/profile/styles")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Error interno del servidor", HttpStatus.BAD_GATEWAY)))
                .bodyToMono(StyleResponseDTO.class)
                .block();
    }

    public StyleResponseDTO updateStyles(String token, StyleRequestDTO request) {
        return backendClient.put()
                .uri("/api/users/profile/styles")
                .header("Authorization", "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.BAD_REQUEST::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Datos inválidos", HttpStatus.BAD_REQUEST)))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Error interno del servidor", HttpStatus.BAD_GATEWAY)))
                .bodyToMono(StyleResponseDTO.class)
                .block();
    }

    public SizeResponseDTO getSizes(String token) {
        return backendClient.get()
                .uri("/api/users/sizes")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Error interno del servidor", HttpStatus.BAD_GATEWAY)))
                .bodyToMono(SizeResponseDTO.class)
                .block();
    }

    public SizeResponseDTO updateSizes(String token, SizeRequestDTO request) {
        return backendClient.put()
                .uri("/api/users/sizes")
                .header("Authorization", "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.BAD_REQUEST::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Datos inválidos", HttpStatus.BAD_REQUEST)))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Error interno del servidor", HttpStatus.BAD_GATEWAY)))
                .bodyToMono(SizeResponseDTO.class)
                .block();
    }

    public MeasurementResponseDTO getMeasurements(String token) {
        return backendClient.get()
                .uri("/api/users/proportions")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.NOT_FOUND::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Medidas no encontradas", HttpStatus.NOT_FOUND)))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Error interno del servidor", HttpStatus.BAD_GATEWAY)))
                .bodyToMono(MeasurementResponseDTO.class)
                .block();
    }

    public MeasurementResponseDTO updateMeasurements(String token, MeasurementRequestDTO request) {
        return backendClient.put()
                .uri("/api/users/proportions")
                .header("Authorization", "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.BAD_REQUEST::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Datos inválidos", HttpStatus.BAD_REQUEST)))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Error interno del servidor", HttpStatus.BAD_GATEWAY)))
                .bodyToMono(MeasurementResponseDTO.class)
                .block();
    }
}