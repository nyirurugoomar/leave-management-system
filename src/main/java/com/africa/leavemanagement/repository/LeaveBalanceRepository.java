package com.africa.leavemanagement.repository;

import com.africa.leavemanagement.model.LeaveBalance;
import com.africa.leavemanagement.model.User;
import com.africa.leavemanagement.model.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    Optional<LeaveBalance> findByUser(User user);
    
    @Query("SELECT lb FROM LeaveBalance lb WHERE lb.user = :user AND " +
           "CASE :leaveType " +
           "WHEN 'PTO' THEN lb.ptoBalance > 0 " +
           "WHEN 'SICK_LEAVE' THEN lb.sickLeaveBalance > 0 " +
           "WHEN 'MATERNITY' THEN lb.maternityBalance > 0 " +
           "WHEN 'COMPASSIONATE' THEN lb.compassionateBalance > 0 " +
           "ELSE false END")
    Optional<LeaveBalance> findByUserAndLeaveType(@Param("user") User user, @Param("leaveType") String leaveType);
} 