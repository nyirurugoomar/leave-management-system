package com.africa.leavemanagement.service;

import com.africa.leavemanagement.dto.LeaveRequestDto;
import com.africa.leavemanagement.model.LeaveRequest;
import com.africa.leavemanagement.model.LeaveBalance;
import com.africa.leavemanagement.model.User;
import com.africa.leavemanagement.model.LeaveStatus;
import com.africa.leavemanagement.model.LeaveType;
import com.africa.leavemanagement.repository.LeaveRequestRepository;
import com.africa.leavemanagement.repository.LeaveBalanceRepository;
import com.africa.leavemanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;

    @Transactional
    public LeaveRequest createLeaveRequest(LeaveRequestDto leaveRequestDto, User user) {
        // Validate dates
        if (leaveRequestDto.getStartDate().isAfter(leaveRequestDto.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        // Calculate number of days
        long daysBetween = ChronoUnit.DAYS.between(leaveRequestDto.getStartDate(), leaveRequestDto.getEndDate()) + 1;
        int numberOfDays = (int) daysBetween;
        
        // If it's a half-day leave, adjust the number of days
        if (leaveRequestDto.isHalfDay()) {
            numberOfDays = (int) Math.ceil(daysBetween * 0.5);
        }

        // Check leave balance
        LeaveBalance leaveBalance = leaveBalanceRepository.findByUserAndLeaveType(user, leaveRequestDto.getLeaveType().name())
                .orElseThrow(() -> new IllegalArgumentException("No leave balance found for this leave type"));

        if (leaveBalance.getRemainingDays(leaveRequestDto.getLeaveType()) < numberOfDays) {
            throw new IllegalArgumentException("Insufficient leave balance");
        }

        // Create leave request
        LeaveRequest leaveRequest = LeaveRequest.builder()
                .user(user)
                .leaveType(leaveRequestDto.getLeaveType())
                .startDate(leaveRequestDto.getStartDate())
                .endDate(leaveRequestDto.getEndDate())
                .halfDay(leaveRequestDto.isHalfDay())
                .reason(leaveRequestDto.getReason())
                .status(LeaveStatus.PENDING)
                .numberOfDays(numberOfDays)
                .build();

        return leaveRequestRepository.save(leaveRequest);
    }

    public List<LeaveRequest> getUserLeaveRequests() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return leaveRequestRepository.findByUser(user);
    }

    public List<LeaveRequest> getPendingLeaveRequests() {
        return leaveRequestRepository.findByStatus(LeaveStatus.PENDING);
    }

    @Transactional
    public LeaveRequest approveLeaveRequest(Long requestId, String comment) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        leaveRequest.setStatus(LeaveStatus.APPROVED);
        leaveRequest.setApproverComment(comment);
        return leaveRequestRepository.save(leaveRequest);
    }

    @Transactional
    public LeaveRequest rejectLeaveRequest(Long requestId, String comment) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        leaveRequest.setStatus(LeaveStatus.REJECTED);
        leaveRequest.setApproverComment(comment);
        return leaveRequestRepository.save(leaveRequest);
    }

    private double getAvailableBalance(LeaveBalance leaveBalance, LeaveType leaveType) {
        if (leaveBalance == null) {
            throw new IllegalArgumentException("Leave balance not found");
        }
        
        float balance = switch (leaveType) {
            case PTO -> leaveBalance.getPtoBalance();
            case SICK_LEAVE -> leaveBalance.getSickLeaveBalance();
            case MATERNITY -> leaveBalance.getMaternityBalance();
            case COMPASSIONATE -> leaveBalance.getCompassionateBalance();
        };
        
        return (double) balance;
    }
} 