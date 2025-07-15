package com.parking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security設定クラス
 * 認証・認可、CORS、パスワードエンコーダーの設定を管理
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * セキュリティフィルターチェーンの設定
     * @param http HttpSecurityオブジェクト
     * @return SecurityFilterChain
     * @throws Exception 設定エラー時
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF保護を無効化（APIベースのため）
            .csrf(AbstractHttpConfigurer::disable)
            // CORS設定を有効化
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 認証設定
            .authorizeHttpRequests(authz -> authz
                // パブリックエンドポイント
                .requestMatchers("/api/public/**", "/api/auth/login", "/api/auth/register").permitAll()
                // 管理者専用エンドポイント
                .requestMatchers("/api/admin/**", "/api/dashboard/**").hasRole("ADMIN")
                // ユーザー専用エンドポイント
                .requestMatchers("/api/user/**").hasRole("USER")
                // その他のAPIエンドポイントは認証が必要
                .requestMatchers("/api/**").authenticated()
                // 静的リソースは許可
                .requestMatchers("/", "/index.html", "/static/**").permitAll()
                // その他は認証が必要
                .anyRequest().authenticated()
            )
            // フォームログインを無効化（APIベースのため）
            .formLogin(AbstractHttpConfigurer::disable)
            // HTTP Basic認証を無効化
            .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * CORS設定
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * パスワードエンコーダー
     * BCryptを使用してパスワードをハッシュ化
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 