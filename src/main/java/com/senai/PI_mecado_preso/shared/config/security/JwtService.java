/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.shared.config.security;

import com.senai.PI_mecado_preso.iam.internal.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys; // Importante para chaves modernas
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 *
 * @author Cansei2
 */

@Service
public class JwtService {

    // A chave deve ter pelo menos 256 bits para HS256
    private String secretKeyString = "SUA_CHAVE_SECRETA_SUPER_SEGURA_COM_PELO_MENOS_32_CARACTERES"; 
    private long jwtExpiration = 86400000; // 1 dia

    // Transforma a String em uma Key segura
    private Key getSigningKey() {
        byte[] keyBytes = secretKeyString.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getUsername()) // Usa o email como subject[cite: 6, 10]
                .claim("roles", usuario.getAuthorities()) 
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Forma moderna de assinar[cite: 2, 10]
                .compact();
    }

    // Método genérico para extrair qualquer claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
    return Jwts.parser() // Na 0.12.x usa-se parser() diretamente para iniciar o builder
            .verifyWith((SecretKey) getSigningKey()) // .verifyWith substitui .setSigningKey
            .build()
            .parseSignedClaims(token) // .parseSignedClaims substitui .parseClaimsJws
            .getPayload(); // .getPayload() substitui .getBody()
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
}
