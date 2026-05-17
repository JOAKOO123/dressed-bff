package cl.dressed.bff.service;

import cl.dressed.bff.client.ProfileClient;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileClient profileClient;

    // =============================================
    // PROFILE
    // =============================================

    public ProfileResponseDTO getProfile(String token) {
        log.info("Obteniendo perfil de usuario");
        return profileClient.getProfile(token);
    }

    public ProfileResponseDTO updateProfile(String token, ProfileUpdateRequestDTO request) {
        log.info("Actualizando perfil de usuario");
        return profileClient.updateProfile(token, request);
    }

    public ProfileResponseDTO updateSkin(String token, SkinUpdateRequestDTO request) {
        log.info("Actualizando tono de piel de usuario");
        return profileClient.updateSkin(token, request);
    }

    public CompletenessResponseDTO getCompleteness(String token) {
        log.info("Obteniendo completitud de perfil");
        return profileClient.getCompleteness(token);
    }

    // =============================================
    // STYLES
    // =============================================

    public StyleResponseDTO getStyles(String token) {
        log.info("Obteniendo estilos de usuario");
        return profileClient.getStyles(token);
    }

    public StyleResponseDTO updateStyles(String token, StyleRequestDTO request) {
        log.info("Actualizando estilos de usuario");
        return profileClient.updateStyles(token, request);
    }

    // =============================================
    // SIZES
    // =============================================

    public SizeResponseDTO getSizes(String token) {
        log.info("Obteniendo tallas de usuario");
        return profileClient.getSizes(token);
    }

    public SizeResponseDTO updateSizes(String token, SizeRequestDTO request) {
        log.info("Actualizando tallas de usuario");
        return profileClient.updateSizes(token, request);
    }

    // =============================================
    // MEASUREMENTS
    // =============================================

    public MeasurementResponseDTO getMeasurements(String token) {
        log.info("Obteniendo medidas de usuario");
        return profileClient.getMeasurements(token);
    }

    public MeasurementResponseDTO updateMeasurements(String token, MeasurementRequestDTO request) {
        log.info("Actualizando medidas de usuario");
        return profileClient.updateMeasurements(token, request);
    }
}
