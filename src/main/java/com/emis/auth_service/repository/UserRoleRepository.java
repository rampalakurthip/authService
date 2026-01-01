package com.emis.auth_service.repository;


import com.emis.auth_service.model.UserRoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRoleModel, String> {

    Optional<UserRoleModel> findByNameIgnoreCase(String name);
}