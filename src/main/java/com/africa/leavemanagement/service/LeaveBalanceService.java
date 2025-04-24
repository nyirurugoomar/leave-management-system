package com.africa.leavemanagement.service;

import com.africa.leavemanagement.model.LeaveBalance;
import com.africa.leavemanagement.model.User;
import com.africa.leavemanagement.repository.LeaveBalanceRepository;
import com.africa.leavemanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class LeaveBalanceService {
    private static final Logger logger = LoggerFactory.getLogger(LeaveBalanceService.class);
    private static final float MONTHLY_ACCRUAL_RATE = 1.66f; // 20 days per year / 12 months
    private static final int MAX_CARRY_FORWARD_DAYS = 5;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private UserRepository userRepository;

    public LeaveBalance getUserLeaveBalance() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return leaveBalanceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));
    }

    @Transactional
    public LeaveBalance updateSickLeaveBalance(float days) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LeaveBalance leaveBalance = leaveBalanceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        leaveBalance.setSickLeaveBalance(days);
        return leaveBalanceRepository.save(leaveBalance);
    }

    @Transactional
    public void processMonthlyLeaveAccrual() {
        logger.info("Starting monthly leave accrual process");
        Iterable<User> users = userRepository.findAll();

        for (User user : users) {
            Optional<LeaveBalance> optionalBalance = leaveBalanceRepository.findByUser(user);
            if (optionalBalance.isPresent()) {
                LeaveBalance balance = optionalBalance.get();
                LocalDate lastAccrualDate = balance.getLastAccrualDate();
                LocalDate now = LocalDate.now();

                if (lastAccrualDate == null || lastAccrualDate.isBefore(now)) {
                    int monthsSinceLastAccrual = lastAccrualDate == null ? 1 :
                            (now.getYear() - lastAccrualDate.getYear()) * 12 +
                            (now.getMonthValue() - lastAccrualDate.getMonthValue());

                    float accruedDays = monthsSinceLastAccrual * MONTHLY_ACCRUAL_RATE;
                    balance.setPtoBalance(balance.getPtoBalance() + accruedDays);
                    balance.setLastAccrualDate(now);
                    leaveBalanceRepository.save(balance);

                    logger.info("Accrued {} days of leave for user {}", accruedDays, user.getEmail());
                }
            }
        }
        logger.info("Monthly leave accrual process completed");
    }

    @Transactional
    public void processAnnualCarryForward() {
        logger.info("Starting annual leave carry forward process");
        LocalDate now = LocalDate.now();
        if (now.getMonthValue() == 1) { // Process in January
            Iterable<User> users = userRepository.findAll();

            for (User user : users) {
                Optional<LeaveBalance> optionalBalance = leaveBalanceRepository.findByUser(user);
                if (optionalBalance.isPresent()) {
                    LeaveBalance balance = optionalBalance.get();
                    float unusedDays = balance.getPtoBalance();
                    float carryForwardDays = Math.min(unusedDays, MAX_CARRY_FORWARD_DAYS);

                    if (carryForwardDays > 0) {
                        balance.setPtoBalance(carryForwardDays);
                        balance.setLastAccrualDate(now);
                        leaveBalanceRepository.save(balance);

                        logger.info("Carried forward {} days of leave for user {}", carryForwardDays, user.getEmail());
                    }
                }
            }
        }
        logger.info("Annual leave carry forward process completed");
    }
} 