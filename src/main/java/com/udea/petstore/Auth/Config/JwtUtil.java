package com.udea.petstore.Auth.Config;
import com.udea.petstore.Rol.Rol;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET = "clave-secreta-supersegura-para-jwt-1234567890";
    private static final long EXPIRATION_TIME = 86400000L; // 1 día en milisegundos

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // 🔐 Generar un JWT con username y rol
    public String generateToken(String username, Rol rol) {
        Map<String, Object> rolMap = new HashMap<>();
        rolMap.put("id", rol.getId());
        rolMap.put("nombre", rol.getNombre());

        return Jwts.builder()
                .setSubject(username)
                .claim("rol", rolMap)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 📤 Extraer claims completos del token
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 📌 Extraer un claim específico (genérico)
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    // 📛 Extraer el username (subject)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 🎭 Extraer el rol del usuario
    public Rol extractRole(String token) {
        Map<String, Object> rolMap = extractClaim(token, claims -> claims.get("role", Map.class));
        if (rolMap == null) return null;
        Integer id = ((Number) rolMap.get("id")).intValue();
        String nombre = (String) rolMap.get("nombre");
        return new Rol(id, nombre);
    }

    // ✅ Validar si el token es correcto y no expiró
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

