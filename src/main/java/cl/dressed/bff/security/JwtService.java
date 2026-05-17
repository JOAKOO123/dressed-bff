package cl.dressed.bff.security;

import cl.dressed.bff.dto.auth.LoginResponseDTO;
import cl.dressed.bff.dto.auth.RegisterResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public String extractEmailFromToken(String token) {
        try {
            var claims = parseClaimsMap(token);
            if (claims.containsKey("sub")) {
                return String.valueOf(claims.get("sub"));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public Long extractUserIdFromToken(String token) {
        try {
            var claims = parseClaimsMap(token);
            Object id = null;
            if (claims.containsKey("uid"))    id = claims.get("uid");
            if (id == null && claims.containsKey("userId")) id = claims.get("userId");
            if (id == null && claims.containsKey("id"))     id = claims.get("id");
            if (id instanceof Number) return ((Number) id).longValue();
            if (id instanceof String s) {
                try { return Long.parseLong(s); } catch (NumberFormatException ex) { return null; }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private java.util.Map<String, Object> parseClaimsMap(String token) throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length < 2) return java.util.Collections.emptyMap();
        byte[] decoded = java.util.Base64.getUrlDecoder().decode(parts[1]);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(decoded, new TypeReference<java.util.Map<String, Object>>() {});
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
}
