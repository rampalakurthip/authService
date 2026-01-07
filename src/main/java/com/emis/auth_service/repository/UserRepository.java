package com.emis.auth_service.repository;

import com.emis.auth_service.enums.UserRole;
import com.emis.auth_service.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {

    Optional<UserModel> findByUsernameIgnoreCase(String username);
    Optional<UserModel> findByLoginEmailIgnoreCase(String loginEmail);
    Optional<UserModel> findByStaffId(String staffId);
    Optional<UserModel> findByStaffEmailIgnoreCase(String staffEmail);
    Optional<UserModel> findByMobileNumber(String mobileNumber);
    default Optional<UserModel> findByUsernameOrEmail(String usernameOrEmail) {
        return findByUsernameIgnoreCase(usernameOrEmail)
                .or(() -> findByLoginEmailIgnoreCase(usernameOrEmail));
    }
    Optional<UserModel> findByUserId(String userId);

    // ✅ NEW: For activeUsers count in RoleService
    @Query("SELECT COUNT(u) FROM UserModel u WHERE u.status = :status")
    long countByStatus(@Param("status") String status);

    // ✅ BONUS: Count active users directly (most common)
    default long countActiveUsers() {
        return countByStatus("ACTIVE");
    }

    @Query("""
       SELECT u FROM UserModel u
       WHERE (:search IS NULL OR 
              LOWER(u.fullName)    LIKE LOWER(CONCAT('%', :search, '%')) OR
              LOWER(u.username)    LIKE LOWER(CONCAT('%', :search, '%')) OR
              LOWER(u.loginEmail)  LIKE LOWER(CONCAT('%', :search, '%')) OR
              LOWER(u.staffEmail)  LIKE LOWER(CONCAT('%', :search, '%')))
         AND (:status IS NULL OR u.status = :status)
         AND (:roleCode IS NULL OR EXISTS (
                SELECT 1 FROM u.roles r
                WHERE r = :roleCode
           ))
       """)
    Page<UserModel> findUsersWithFilters(@Param("search") String search,
                                         @Param("roleCode") UserRole roleCode,
                                         @Param("status") String status,
                                         Pageable pageable);
}
