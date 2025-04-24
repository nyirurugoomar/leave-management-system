package com.africa.leavemanagement.service;

import com.africa.leavemanagement.dto.UpdateUserRoleRequest;
import com.africa.leavemanagement.dto.UserListDTO;
import com.africa.leavemanagement.model.User;
import com.africa.leavemanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public List<UserListDTO> getAllUsers() {
        logger.info("Starting getAllUsers() method");
        return userRepository.findAll().stream()
                .map(UserListDTO::fromEntity)
                .toList();
    }

    @Transactional
    public User updateUserRole(Long userId, UpdateUserRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        user.setRole(request.getRole());
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
} 