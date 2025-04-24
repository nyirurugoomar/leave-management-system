package com.africa.leavemanagement.controller;

import com.africa.leavemanagement.dto.LeaveRequestDto;
import com.africa.leavemanagement.dto.LeaveRequestResponseDto;
import com.africa.leavemanagement.model.LeaveRequest;
import com.africa.leavemanagement.model.User;
import com.africa.leavemanagement.service.LeaveRequestService;
import com.africa.leavemanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<LeaveRequest> createLeaveRequest(
            @Valid @RequestBody LeaveRequestDto requestDto
    ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(leaveRequestService.createLeaveRequest(requestDto, user));
    }

    @GetMapping("/my-requests")
    public ResponseEntity<List<LeaveRequestResponseDto>> getMyLeaveRequests() {
        List<LeaveRequest> leaveRequests = leaveRequestService.getUserLeaveRequests();
        List<LeaveRequestResponseDto> response = leaveRequests.stream()
                .map(LeaveRequestResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<LeaveRequestResponseDto>> getPendingLeaveRequests() {
        List<LeaveRequest> leaveRequests = leaveRequestService.getPendingLeaveRequests();
        List<LeaveRequestResponseDto> response = leaveRequests.stream()
                .map(LeaveRequestResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{requestId}/approve")
    public ResponseEntity<LeaveRequestResponseDto> approveLeaveRequest(
            @PathVariable Long requestId,
            @RequestParam(required = false) String comment
    ) {
        LeaveRequest leaveRequest = leaveRequestService.approveLeaveRequest(requestId, comment);
        return ResponseEntity.ok(LeaveRequestResponseDto.fromEntity(leaveRequest));
    }

    @PostMapping("/{requestId}/reject")
    public ResponseEntity<LeaveRequestResponseDto> rejectLeaveRequest(
            @PathVariable Long requestId,
            @RequestParam(required = false) String comment
    ) {
        LeaveRequest leaveRequest = leaveRequestService.rejectLeaveRequest(requestId, comment);
        return ResponseEntity.ok(LeaveRequestResponseDto.fromEntity(leaveRequest));
    }
} 