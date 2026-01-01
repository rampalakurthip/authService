package com.emis.auth_service.repository;

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

    @Query("SELECT u FROM UserModel u WHERE " +
            "(:search IS NULL OR " +
            " LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            " LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            " LOWER(u.loginEmail) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            " LOWER(u.staffEmail) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:role IS NULL OR :role MEMBER OF u.roles) " +  // ← NOW WORKS!
            "AND (:status IS NULL OR u.status = :status)")
    Page<UserModel> findUsersWithFilters(@Param("search") String search,
                                         @Param("role") String role,
                                         @Param("status") String status,
                                         Pageable pageable);
}
