package com.drsanches.clicker.token;

import com.drsanches.clicker.exception.AuthException;
import com.drsanches.clicker.token.refresh.RefreshToken;
import com.drsanches.clicker.token.refresh.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Service
public class TokenService {

    private static final long ACCESS_TOKEN_PLUS_MINUTES = 30;

    private static final long REFRESH_TOKEN_PLUS_DAYS = 30;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${application.token.secret-base64}")
    private String secretBase64;

    public Mono<Token> createToken(String userId, String deviceId) {
        return refreshTokenRepository.findByDeviceId(deviceId)
                .map(it -> {
                    if (!it.getUserId().equals(userId)) {
                        throw new AuthException("Device id '" + deviceId + "' already exists for user '" + it.getUserId() + "'");
                    }
                    return it;
                })
                .flatMap(it -> refreshTokenRepository.delete(it))
                .then(refreshTokenRepository.save(refreshToken(userId, deviceId))
                        .map(token -> Token.builder()
                                .accessToken(accessToken(userId))
                                .refreshToken(token.getId())
                                .build()));
    }

    public Mono<Token> refreshToken(String refreshTokenId) {
        return refreshTokenRepository.findById(refreshTokenId)
                .switchIfEmpty(Mono.error(new AuthException("Wrong refresh token")))
                .flatMap(it -> refreshTokenRepository.delete(it)
                        .then(refreshTokenRepository.save(refreshToken(it.getUserId(), it.getDeviceId()))
                                .map(token -> Token.builder()
                                        .accessToken(accessToken(it.getUserId()))
                                        .refreshToken(token.getId())
                                        .build())));
    }

    private String accessToken(String userId) {
        Date accessTokenExpires = Date.from(LocalDateTime.now()
                .plusMinutes(ACCESS_TOKEN_PLUS_MINUTES)
                .atZone(ZoneId.systemDefault())
                .toInstant());
        return Jwts.builder()
                .setExpiration(accessTokenExpires)
                .claim("userId", userId)
                .signWith(secret(), SignatureAlgorithm.HS256)
                .compact();
    }

    private RefreshToken refreshToken(String userId, String deviceId) {
        Date refreshTokenExpires = Date.from(LocalDateTime.now()
                .plusDays(REFRESH_TOKEN_PLUS_DAYS)
                .atZone(ZoneId.systemDefault())
                .toInstant());
        return RefreshToken.builder()
                .expiresAt(refreshTokenExpires)
                .userId(userId)
                .deviceId(deviceId)
                .build();
    }

    public TokenInfo validate(String token) {
        try {
            String userId = Jwts.parserBuilder()
                    .setSigningKey(secret())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("userId", String.class);
            return new TokenInfo(userId);
        } catch (SignatureException e) {
            throw new AuthException("Wrong JWT signature", e);
        } catch (ExpiredJwtException e) {
            throw new AuthException("JWT expired", e);
        } catch (Exception e) {
            throw new AuthException("Wrong JWT", e);
        }
    }

    private SecretKey secret() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretBase64));
    }

    @Data
    @AllArgsConstructor
    public static class TokenInfo {

        String userId;
    }
}
