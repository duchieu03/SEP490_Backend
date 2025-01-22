package com.sep490_backend.authentication.config.tenancy.datasource.common;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Configuration
@RequiredArgsConstructor
public class CommonDatasourceConfig {

    private final CommonDatasourceProperties commonDatasourceProperties;

    @Bean
    public DataSource commonDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(commonDatasourceProperties.getJdbcUrl());
        config.setUsername(commonDatasourceProperties.getUsername());
        config.setPassword(commonDatasourceProperties.getPassword());
        config.setDriverClassName(commonDatasourceProperties.getDriverClassName());
        config.setMaximumPoolSize(commonDatasourceProperties.getMaximumPoolSize());
        return new HikariDataSource(config);
    }
}
