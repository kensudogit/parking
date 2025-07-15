package com.parking.config;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

/**
 * Doma2設定クラス
 * Doma2フレームワークの設定を管理
 */
@Configuration
public class DomaConfig {
    
    @Autowired
    private DataSource dataSource;
    
    /**
     * Doma2の設定Bean
     * PostgreSQL方言とトランザクション対応データソースを設定
     * @return Doma2設定
     */
    @Bean
    public Config domaConfig() {
        return new Config() {
            @Override
            public Dialect getDialect() {
                return new PostgresDialect();
            }
            
            @Override
            public DataSource getDataSource() {
                return new TransactionAwareDataSourceProxy(dataSource);
            }
        };
    }
} 