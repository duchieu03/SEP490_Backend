package com.sep490_backend.authentication.config.tenancy.datasource.tenant;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "multi-tenancy.tenant.datasource")
@Component
public class TenantDatasourceProperties {
    private String driverClassName;
    private Integer maximumPoolSize;
}
