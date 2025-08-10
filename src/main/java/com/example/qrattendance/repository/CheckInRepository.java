package com.example.qrattendance.repository;

import com.example.qrattendance.model.CheckInRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckInRepository extends JpaRepository<CheckInRecord, Long> {
    List<CheckInRecord> findBySessionId(String sessionId);
    List<CheckInRecord> findBySessionIdAndStudentId(String sessionId, String studentId);
}
