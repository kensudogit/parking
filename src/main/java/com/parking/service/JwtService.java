package com.parking.service;

import com.parking.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * JWTサービス
 * JWTトークンの生成、検証、解析を担当
 */
@Service
public class JwtService {

    @Value("${jwt.secret:defaultSecretKeyForDevelopment}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration; // 24時間（ミリ秒）

    /**
     * シークレットキーを取得
     * @return SecretKey
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * JWTトークンを生成
     * @param user ユーザー情報
     * @return JWTトークン
     */
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .claim("roles", user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toList()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * JWTトークンからユーザー名を取得
     * @param token JWTトークン
     * @return ユーザー名
     */
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * JWTトークンからユーザーIDを取得
     * @param token JWTトークン
     * @return ユーザーID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * JWTトークンから権限を取得
     * @param token JWTトークン
     * @return 権限コレクション
     */
    @SuppressWarnings("unchecked")
    public Collection<? extends GrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        Collection<String> roles = claims.get("roles", Collection.class);
        
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

    /**
     * JWTトークンの有効期限を取得
     * @param token JWTトークン
     * @return 有効期限
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    /**
     * JWTトークンが有効期限切れかチェック
     * @param token JWTトークン
     * @return 有効期限切れの場合true
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * JWTトークンを検証
     * @param token JWTトークン
     * @param username ユーザー名
     * @return 有効な場合true
     */
    public boolean validateToken(String token, String username) {
        try {
            String tokenUsername = getUsernameFromToken(token);
            return (username.equals(tokenUsername) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * JWTトークンからクレームを取得
     * @param token JWTトークン
     * @return Claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * トークンの有効性をチェック
     * @param token JWTトークン
     * @return 有効な場合true
     */
    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * トークンの残り有効時間を取得（ミリ秒）
     * @param token JWTトークン
     * @return 残り有効時間（ミリ秒）
     */
    public long getRemainingTime(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            Date now = new Date();
            return Math.max(0, expiration.getTime() - now.getTime());
        } catch (Exception e) {
            return 0;
        }
    }
} 