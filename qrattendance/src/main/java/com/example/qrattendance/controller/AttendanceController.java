package com.example.qrattendance.controller;

import com.example.qrattendance.model.SessionData;
import com.example.qrattendance.QRCodeGenerator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//student check in API
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.ArrayList;

//check in record
import com.example.qrattendance.model.CheckInRecord;

//sr
import com.example.qrattendance.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.qrattendance.repository.CheckInRepository;

import java.util.Optional;
@RestController
public class AttendanceController {
	@Autowired
	private SessionRepository sessionRepository;
	@Autowired
	private CheckInRepository checkInRepository;
	

    @PostMapping("/session/start")
    public Map<String, String> startSession() {
        String sessionId = UUID.randomUUID().toString();
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusMinutes(10);

        SessionData session = new SessionData(sessionId, startTime, endTime);
        sessionRepository.save(session);
        String qrContent = "SessionID:" + sessionId + "|Time:" + startTime.toString();

        String qrBase64 = "";
        try {
            qrBase64 = QRCodeGenerator.getQRCodeBase64(qrContent, 300, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> response = new HashMap<>();
        response.put("sessionId", sessionId);
        response.put("startTime", startTime.toString());
        response.put("endTime", endTime.toString());
        response.put("qrContent", qrContent);
        response.put("qrCodeImage", qrBase64);  // Base64 encoded QR image

        return response;
    }

    @GetMapping("/session/validate/{sessionId}")
    public Map<String, Object> validateSession(@PathVariable String sessionId) {
    	Map<String, Object> response = new HashMap<>();

    	Optional<SessionData> sessionOpt = sessionRepository.findById(sessionId);
    	SessionData session=null;
    	if (sessionOpt.isPresent()) {
    	    session = sessionOpt.get();
    	    
    	} 

    	if (session == null) {
    		response.put("valid", false);
    		response.put("message", "Invalid session ID");
    	} else {
    		LocalDateTime now = LocalDateTime.now();
    		if (now.isBefore(session.getStartTime()) || now.isAfter(session.getEndTime())) {
    			response.put("valid", false);
    			response.put("message", "Session expired");
    		} else {
    			response.put("valid", true);
    			response.put("message", "Session is still active");
    		}
    	}

    	return response;
    }
    @PostMapping("/checkin")
    public Map<String, Object> checkIn(@RequestBody Map<String, String> payload) {
        String sessionId = payload.get("sessionId");
        String studentId = payload.get("studentId");

        Map<String, Object> response = new HashMap<>();

        Optional<SessionData> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Invalid session ID");
            return response;
        }

        SessionData session = sessionOpt.get();
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(session.getStartTime()) || now.isAfter(session.getEndTime())) {
            response.put("success", false);
            response.put("message", "Check-in time is outside the session time.");
            return response;
        }

        
        List<CheckInRecord> existing = checkInRepository.findBySessionIdAndStudentId(sessionId, studentId);
        if (!existing.isEmpty()) {
            response.put("success", false);
            response.put("message", "You’ve already checked in!");
            return response;
        }

        // Save ci
        CheckInRecord newRecord = new CheckInRecord(sessionId, studentId, LocalDateTime.now());

        checkInRepository.save(newRecord);

        response.put("success", true);
        response.put("message", "Check-in successful");
        return response;
    }


    //view all check in for a session
    @GetMapping("/session/{sessionId}/checkins")
    public Map<String, Object> getCheckins(@PathVariable String sessionId) {
        Map<String, Object> response = new HashMap<>();

        Optional<SessionData> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Invalid session ID");
            return response;
        }

        List<CheckInRecord> records = checkInRepository.findBySessionId(sessionId);
        response.put("success", true);
        response.put("sessionId", sessionId);
        response.put("checkedInStudents", records);
        return response;
    }


    

}