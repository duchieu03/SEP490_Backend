package com.sep490_backend.authentication.config.tenancy.datasource.tenant;

import com.sep490_backend.authentication.config.tenancy.context.TenantContextHolder;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MultitenantIdentifierResolver implements CurrentTenantIdentifierResolver<String> {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContextHolder.currentTenant();
        if (StringUtils.hasText(tenantId)) {
            return tenantId;
        } else {
            return "MASTER_DS";
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
