package com.sep490_backend.authentication.config.tenancy.context;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class TenantContextHolder {
    private static final InheritableThreadLocal<String> CURRENT_TENANT = new InheritableThreadLocal<>();

    public static String currentTenant() {
        return CURRENT_TENANT.get();
    }

    public static void setTenantId(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static void clearTenant() {
        CURRENT_TENANT.remove();
    }

    public static void clearBrand() {
        CURRENT_TENANT.remove();
    }

}
