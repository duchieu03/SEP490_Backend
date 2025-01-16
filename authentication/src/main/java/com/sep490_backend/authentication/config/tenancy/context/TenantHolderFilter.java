package com.sep490_backend.authentication.config.tenancy.context;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class TenantHolderFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String tenantId = null;

        if (!StringUtils.isEmpty(request.getHeader(TenantConstant.TENANT_ID))) {
            tenantId = request.getHeader(TenantConstant.TENANT_ID);
        } else {
            //todo: extract tenantId from domain name if need
        }

        TenantContextHolder.setTenantId(tenantId);

        // for logging purposes
        MDC.put("tenantId", tenantId);

        filterChain.doFilter(request, response);

        TenantContextHolder.clearTenant();
        TenantContextHolder.clearBrand();
    }
}
