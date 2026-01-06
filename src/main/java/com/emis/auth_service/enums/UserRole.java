package com.emis.auth_service.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum UserRole {

    SCHOOL_ADMINISTRATOR("School Administrator"),
    TEACHER("Teacher"),
    DATA_ENTRY_CLERK("Data Entry Clerk");
    private final String label;

    UserRole(String label) {
        this.label = label;
    }

    /**
     * Factory method: Convert human-readable label to Role enum
     *
     * @param label e.g. "School Administrator"
     * @return matching Role enum
     * @throws IllegalArgumentException if no match found
     */
    public static UserRole fromLabel(String label) {
        return Arrays.stream(values())
                .filter(r -> r.label.equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown role label: '" + label + "'. " +
                        "Valid: " + Arrays.stream(values()).map(UserRole::getLabel).toList()));
    }

    /**
     * ✅ NEW: Convert List<String> → List<Role> for UserModel
     *
     * @param roleLabels Frontend sends: ["School Administrator", "Teacher"]
     * @return List<Role>: [SCHOOL_ADMINISTRATOR, TEACHER]
     */

    public static List<UserRole> fromLabels(List<String> roleLabels) {
        if (roleLabels == null || roleLabels.isEmpty()) {
            return List.of();
        }
        return roleLabels.stream()
                .map(UserRole::fromLabel)
                .collect(Collectors.toList());
    }

    /**
     * Convert List<Role> → List<String> for frontend response
     */
    public static List<String> toLabels(List<UserRole> roles) {
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }
        return roles.stream()
                .map(UserRole::getLabel)
                .toList();
    }

    /**
     * Get all role labels for frontend dropdowns
     */
    public static List<String> getAllLabels() {
        return Arrays.stream(values())
                .map(UserRole::getLabel)
                .toList();
    }


    // Interpret input as roleCode
    public static UserRole fromCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Role code cannot be null or empty");
        }
        try {
            // Accept some variations if needed
            String normalized = code.trim()
                    .toUpperCase()
                    .replace(" ", "_")
                    .replace("-", "_");
            return UserRole.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unknown role code: '" + code + "'. Valid: " +
                            Arrays.toString(values())
            );
        }
    }

    public static List<UserRole> fromCodes(List<String> codes) {
        if (codes == null || codes.isEmpty()) return List.of();
        return codes.stream()
                .map(UserRole::fromCode)
                .toList();
    }

    public static List<String> toCodes(List<UserRole> roles) {
        if (roles == null || roles.isEmpty()) return List.of();
        return roles.stream()
                .map(Enum::name)  // "SCHOOL_ADMINISTRATOR"
                .toList();

    }
}

