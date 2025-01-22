package com.sep490_backend.authentication.config.tenancy.datasource.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "multi-tenancy.common.datasource")
@Component
public class CommonDatasourceProperties {
    private String jdbcUrl;
    private String username;
    private String password;
    private String driverClassName;
    private Integer maximumPoolSize;
}
