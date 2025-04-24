package com.africa.leavemanagement.repository;

import com.africa.leavemanagement.model.Document;
import com.africa.leavemanagement.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByLeaveRequest(LeaveRequest leaveRequest);
} 