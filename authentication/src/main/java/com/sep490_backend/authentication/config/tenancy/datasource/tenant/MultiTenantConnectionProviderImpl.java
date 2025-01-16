package com.sep490_backend.authentication.config.tenancy.datasource.tenant;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sep490_backend.authentication.entity.common.Tenant;
import com.sep490_backend.authentication.repository.common.TenantRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
@RequiredArgsConstructor
public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl<String> {
    private final DataSource commonDataSource;

    private final TenantRepository tenantRepository;

    private LoadingCache<String, DataSource> TENANT_DATASOURCES;

    @PostConstruct
    private void init() {
        TENANT_DATASOURCES = CacheBuilder.newBuilder()
                .removalListener(removal -> {
                    HikariDataSource ds = (HikariDataSource) removal.getValue();
                    if (ds != null) {
                        ds.close();
                        System.out.println("Closed datasource: {}" + ds.getPoolName());
                    }
                })
                .build(new CacheLoader<>() {
                           @Override
                           public DataSource load(String tenantIdKey) {
                               Tenant tenant = tenantRepository.findById(Long.valueOf(tenantIdKey))
                                       .orElseThrow();
                               return createAndConfigureDataSource(tenant);
                           }
                       }
                );
        TENANT_DATASOURCES.put("MASTER-DS", commonDataSource);
    }

    protected DataSource createAndConfigureDataSource(Tenant tenant) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(tenant.getUrl());
        config.setUsername(tenant.getName());
        config.setPassword(tenant.getPassword());
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(10);
        try{
            DataSource dataSource = new HikariDataSource(config);
            return dataSource;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return commonDataSource;
    }

    @Override
    protected DataSource selectDataSource(String tenantId) {
        try {
            return TENANT_DATASOURCES.get(tenantId);
        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to load DataSource for tenantId: " + tenantId);
        }
    }
}
