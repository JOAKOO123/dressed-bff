package cl.dressed.bff.security;

import cl.dressed.bff.dto.auth.LoginResponseDTO;
import cl.dressed.bff.dto.auth.RegisterResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String extractTokenFromBackendResponse(LoginResponseDTO response) {
        return response.token();
    }

    public String extractTokenFromBackendResponse(RegisterResponseDTO response) {
        return response.token();
    }

    public String extractEmailFromToken(String token) {
        Claims claims = parseClaims(token);
        // try common places
        if (claims.get("email") != null) {
            return claims.get("email", String.class);
        }
        return claims.getSubject();
    }

    public Long extractUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        Object id = claims.get("userId");
        if (id == null) {
            id = claims.get("id");
        }
        if (id instanceof Number) {
            return ((Number) id).longValue();
        }
        if (id instanceof String) {
            try {
                return Long.parseLong((String) id);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private Claims parseClaims(String token) {
        Jws<Claims> jws = Jwts.parser()
            .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
            .parseClaimsJws(token);
        return jws.getBody();
    }
}
