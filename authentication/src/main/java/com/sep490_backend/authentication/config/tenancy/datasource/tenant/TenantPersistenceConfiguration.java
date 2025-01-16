package com.sep490_backend.authentication.config.tenancy.datasource.tenant;

import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.sep490_backend.authentication.repository.tenant",
        entityManagerFactoryRef = "tenantEntityManager",
        transactionManagerRef = "tenantTransactionManager"
)
@Component
@RequiredArgsConstructor
public class TenantPersistenceConfiguration {
    private final Environment env;

    private final CurrentTenantIdentifierResolver<String> multiTenantIdentifierResolver;

    private final MultiTenantConnectionProviderImpl tenantDatasourceProvider;

    private final JpaProperties jpaProperties;

    @Bean
    public LocalContainerEntityManagerFactoryBean tenantEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setPackagesToScan(
                new String[] { "com.sep490_backend.authentication.entity.tenant"});

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>(this.jpaProperties.getProperties());
        properties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, tenantDatasourceProvider);
        properties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, multiTenantIdentifierResolver);
        properties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY, CamelCaseToUnderscoresNamingStrategy.class.getName());
        properties.put(AvailableSettings.IMPLICIT_NAMING_STRATEGY, ImplicitNamingStrategyLegacyJpaImpl.class.getName());

        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager tenantTransactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                tenantEntityManager().getObject());
        return transactionManager;
    }
}
