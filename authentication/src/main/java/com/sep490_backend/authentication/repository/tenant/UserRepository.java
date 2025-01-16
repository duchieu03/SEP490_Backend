package com.sep490_backend.authentication.repository.tenant;

import com.sep490_backend.authentication.entity.tenant.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}