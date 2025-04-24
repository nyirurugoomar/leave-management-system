package com.africa.leavemanagement.dto;

import com.africa.leavemanagement.model.LeaveRequest;
import com.africa.leavemanagement.model.LeaveStatus;
import com.africa.leavemanagement.model.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestResponseDto {
    private Long id;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfDays;
    private boolean halfDay;
    private LeaveStatus status;
    private String reason;
    private String approverComments;
    private LocalDateTime createdAt;

    public static LeaveRequestResponseDto fromEntity(LeaveRequest leaveRequest) {
        return LeaveRequestResponseDto.builder()
                .id(leaveRequest.getId())
                .leaveType(leaveRequest.getLeaveType())
                .startDate(leaveRequest.getStartDate())
                .endDate(leaveRequest.getEndDate())
                .numberOfDays(leaveRequest.getNumberOfDays())
                .halfDay(leaveRequest.isHalfDay())
                .status(leaveRequest.getStatus())
                .reason(leaveRequest.getReason())
                .approverComments(leaveRequest.getApproverComment())
                .createdAt(leaveRequest.getCreatedAt())
                .build();
    }
} 