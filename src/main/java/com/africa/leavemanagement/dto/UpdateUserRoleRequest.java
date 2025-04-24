package com.africa.leavemanagement.dto;

import com.africa.leavemanagement.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRoleRequest {
    @NotNull(message = "Role is required")
    private Role role;
} 