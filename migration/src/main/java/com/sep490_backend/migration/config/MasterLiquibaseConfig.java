package com.sep490_backend.migration.config;

import com.sep490_backend.migration.util.LiquibaseUtil;
import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(LiquibaseProperties.class)
@RequiredArgsConstructor
public class MasterLiquibaseConfig {

    private final DataSource dataSource;

    @Bean
    @ConfigurationProperties("multi-tenancy.master.liquibase")
    public LiquibaseProperties masterLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase masterLiquibase(@LiquibaseDataSource ObjectProvider<DataSource> liquibaseDataSource) {
        SpringLiquibase liquibase = LiquibaseUtil.fromProperties(masterLiquibaseProperties());
        liquibase.setDataSource(dataSource);
        return liquibase;
    }
}
