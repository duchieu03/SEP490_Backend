package com.sep490_backend.authentication.repository.common;

import com.sep490_backend.authentication.entity.common.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
}
