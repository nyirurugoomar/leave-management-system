package com.africa.leavemanagement.controller;

import com.africa.leavemanagement.model.LeaveBalance;
import com.africa.leavemanagement.service.LeaveBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/leave-balance")
@RequiredArgsConstructor
public class LeaveBalanceController {
    private final LeaveBalanceService leaveBalanceService;

    @GetMapping
    public ResponseEntity<LeaveBalance> getMyLeaveBalance() {
        return ResponseEntity.ok(leaveBalanceService.getUserLeaveBalance());
    }

    @PostMapping("/update-sick-leave")
    public ResponseEntity<LeaveBalance> updateSickLeaveBalance(@RequestParam float days) {
        return ResponseEntity.ok(leaveBalanceService.updateSickLeaveBalance(days));
    }
} 