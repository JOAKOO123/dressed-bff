package cl.dressed.bff.security;

import cl.dressed.bff.dto.auth.LoginResponseDTO;
import cl.dressed.bff.dto.auth.RegisterResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.cookie.name}")
    private String cookieName;

    public String extractTokenFromBackendResponse(LoginResponseDTO response) {
        return response.token();
    }

    public String extractTokenFromBackendResponse(RegisterResponseDTO response) {
        return response.token();
    }

    public String extractTokenFromCookie(HttpServletRequest request) {
        if (request == null) return null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie c : cookies) {
            if (cookieName != null && cookieName.equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }

    public boolean isTokenValid(String token) {
        if (token == null || token.isBlank()) return false;
        String[] parts = token.split("\\.");
        if (parts.length != 3) return false;
        try {
            String signingInput = parts[0] + "." + parts[1];
            String expectedSig = hmacSha256Base64Url(signingInput, jwtSecret);
            if (!constantTimeEquals(parts[2], expectedSig)) {
                log.warn("JWT con firma inválida rechazado");
                return false;
            }
            Map<String, Object> claims = parseClaimsMap(token);
            Object exp = claims.get("exp");
            if (exp instanceof Number) {
                long expEpoch = ((Number) exp).longValue();
                long now = System.currentTimeMillis() / 1000;
                if (now > expEpoch) {
                    log.warn("JWT expirado rechazado");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.warn("Error validando JWT: {}", e.getMessage());
            return false;
        }
    }

    public String extractEmailFromToken(String token) {
        try {
            Map<String, Object> claims = parseClaimsMap(token);
            if (claims.containsKey("sub")) return String.valueOf(claims.get("sub"));
        } catch (Exception e) {
            log.debug("No se pudo extraer email del token");
        }
        return null;
    }

    public Long extractUserIdFromToken(String token) {
        try {
            Map<String, Object> claims = parseClaimsMap(token);
            Object id = claims.getOrDefault("uid", claims.getOrDefault("userId", claims.get("id")));
            if (id instanceof Number) return ((Number) id).longValue();
            if (id instanceof String s) return Long.parseLong(s);
        } catch (Exception e) {
            log.debug("No se pudo extraer userId del token");
        }
        return null;
    }

    private Map<String, Object> parseClaimsMap(String token) throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length < 2) return Collections.emptyMap();
        String payload = parts[1];
        int pad = 4 - (payload.length() % 4);
        if (pad != 4) payload = payload + "=".repeat(pad);
        byte[] decoded = Base64.getUrlDecoder().decode(payload);
        return new ObjectMapper().readValue(decoded, new TypeReference<>() {});
    }

    private String hmacSha256Base64Url(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] sig = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(sig);
    }

    private boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) return false;
        int diff = 0;
        for (int i = 0; i < a.length(); i++) {
            diff |= a.charAt(i) ^ b.charAt(i);
        }
        return diff == 0;
    }

    public String extractRoleFromToken(String token) {
        try {
            Map<String, Object> claims = parseClaimsMap(token);
            Object role = claims.get("role");
            if (role != null) return String.valueOf(role);
        } catch (Exception e) {
            log.debug("No se pudo extraer role del token");
        }
        return "user";
    }
}
