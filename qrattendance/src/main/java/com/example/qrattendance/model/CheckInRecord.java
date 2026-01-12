package com.example.qrattendance.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CheckInRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId;
    private String studentId;
    private LocalDateTime timestamp;

    public CheckInRecord() {}

    public CheckInRecord(String sessionId, String studentId, LocalDateTime timestamp) {
        this.sessionId = sessionId;
        this.studentId = studentId;
        this.timestamp = timestamp;
    }

    
    public Long getId() { return id; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
