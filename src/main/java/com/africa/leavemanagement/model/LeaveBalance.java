package com.africa.leavemanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "leave_balances")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private float ptoBalance;

    @Column(nullable = false)
    private float sickLeaveBalance;

    @Column(nullable = false)
    private float maternityBalance;

    @Column(nullable = false)
    private float compassionateBalance;

    @Column(nullable = false)
    private float carriedForwardDays;

    @Column(nullable = false)
    private LocalDate lastAccrualDate;

    @Column(nullable = false)
    private LocalDate lastCarryForwardDate;

    @PrePersist
    protected void onCreate() {
        if (lastAccrualDate == null) {
            lastAccrualDate = LocalDate.now();
        }
        if (lastCarryForwardDate == null) {
            lastCarryForwardDate = LocalDate.now();
        }
    }

    public float getRemainingDays(LeaveType leaveType) {
        return switch (leaveType) {
            case PTO -> ptoBalance;
            case SICK_LEAVE -> sickLeaveBalance;
            case MATERNITY -> maternityBalance;
            case COMPASSIONATE -> compassionateBalance;
        };
    }
} 