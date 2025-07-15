package com.parking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * 多言語対応設定クラス
 * 国際化（i18n）機能を提供する
 * 
 * @author Parking System
 * @version 1.0
 */
@Configuration
public class LocaleConfig implements WebMvcConfigurer {
    
    /**
     * ロケールリゾルバーを設定
     * デフォルトロケールを日本語に設定
     * 
     * @return ロケールリゾルバー
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.JAPANESE);
        return resolver;
    }
    
    /**
     * ロケール変更インターセプターを設定
     * URLパラメータでロケールを変更可能にする
     * 
     * @return ロケール変更インターセプター
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }
    
    /**
     * メッセージソースを設定
     * 多言語メッセージファイルを読み込む
     * 
     * @return メッセージソース
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("messages", "labels", "errors");
        source.setDefaultEncoding("UTF-8");
        return source;
    }
    
    /**
     * インターセプターを登録
     * 
     * @param registry インターセプターレジストリ
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
} 