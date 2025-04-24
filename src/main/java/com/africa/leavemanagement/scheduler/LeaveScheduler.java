package com.africa.leavemanagement.scheduler;

import com.africa.leavemanagement.service.LeaveBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeaveScheduler {
    private final LeaveBalanceService leaveBalanceService;

    @Scheduled(cron = "0 0 0 1 * ?") // Run at midnight on the first day of every month
    public void processMonthlyLeaveAccrual() {
        leaveBalanceService.processMonthlyLeaveAccrual();
    }

    @Scheduled(cron = "0 0 0 1 1 ?") // Run at midnight on January 1st every year
    public void processAnnualCarryForward() {
        leaveBalanceService.processAnnualCarryForward();
    }
} 