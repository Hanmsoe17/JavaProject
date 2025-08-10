package com.example.qrattendance.repository;

import com.example.qrattendance.model.SessionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<SessionData, String> {
}
