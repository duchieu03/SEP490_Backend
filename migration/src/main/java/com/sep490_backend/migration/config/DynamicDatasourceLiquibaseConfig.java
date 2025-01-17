package com.sep490_backend.migration.config;

import com.sep490_backend.migration.entity.Tenant;
import com.sep490_backend.migration.repository.TenantRepository;
import com.sep490_backend.migration.util.LiquibaseUtil;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@Getter
@Setter
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicDatasourceLiquibaseConfig implements InitializingBean {
    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private LiquibaseProperties tenantLiquibaseProperties;


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DynamicDataSources based multi-tenancy enabled. Start checking migration!");
        this.runOnAllTenants(tenantRepository.findAll());
    }

    protected void runOnAllTenants(List<Tenant> tenants) {
        for (Tenant tenant : tenants) {
            log.info("Start migrate liquibase for tenant: {}", tenant.getId());
            try (Connection connection = DriverManager.getConnection(tenant.getUrl(), tenant.getName(), tenant.getPassword())) {
                DataSource tenantDataSource = new SingleConnectionDataSource(connection, false);
                this.runLiquibase(tenantDataSource);
            } catch (SQLException | LiquibaseException e) {
                //force quite if cannot migrate database
                log.error("Failed to run Liquibase for tenant: {}", tenant.getId(), e);
                int exitCode = SpringApplication.exit(context, () -> 0);
                System.exit(exitCode);
            }
            log.info("Liquibase ran successfully for tenant: {}", tenant.getId());
        }
    }

    public void runLiquibase(DataSource dataSource) throws LiquibaseException {
        SpringLiquibase liquibase = getSpringLiquibase(dataSource);
        liquibase.afterPropertiesSet();
    }

    protected SpringLiquibase getSpringLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = LiquibaseUtil.fromProperties(tenantLiquibaseProperties);
        liquibase.setResourceLoader(resourceLoader);
        liquibase.setDataSource(dataSource);
        return liquibase;
    }
}
