package com.africa.leavemanagement.dto;

import com.africa.leavemanagement.model.Role;
import com.africa.leavemanagement.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private String department;

    public static UserListDTO fromEntity(User user) {
        return UserListDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .department(user.getDepartment())
                .build();
    }
}
