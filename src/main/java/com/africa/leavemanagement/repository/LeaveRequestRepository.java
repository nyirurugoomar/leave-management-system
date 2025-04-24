package com.africa.leavemanagement.repository;

import com.africa.leavemanagement.model.LeaveRequest;
import com.africa.leavemanagement.model.LeaveStatus;
import com.africa.leavemanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByUser(User user);
    List<LeaveRequest> findByUserAndStatus(User user, LeaveStatus status);
    List<LeaveRequest> findByStatus(LeaveStatus status);
    List<LeaveRequest> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<LeaveRequest> findByUserAndStartDateBetween(User user, LocalDate startDate, LocalDate endDate);
} 