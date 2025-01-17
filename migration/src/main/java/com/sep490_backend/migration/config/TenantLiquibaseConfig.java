package com.sep490_backend.migration.config;

import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@EnableConfigurationProperties(LiquibaseProperties.class)
public class TenantLiquibaseConfig {
    @Bean
    @ConfigurationProperties("multi-tenancy.tenant.liquibase")
    public LiquibaseProperties tenantLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    @DependsOn("masterLiquibase")
    public DynamicDatasourceLiquibaseConfig tenantLiquibase() {
        return new DynamicDatasourceLiquibaseConfig();
    }
}
