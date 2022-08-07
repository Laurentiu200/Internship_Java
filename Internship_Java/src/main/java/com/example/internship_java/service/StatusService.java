package com.example.internship_java.service;

import org.springframework.http.ResponseEntity;

public interface StatusService {
    ResponseEntity<Object> updateCandidateStatus(String interviewId, String statusValue);

    ResponseEntity<Object> updateInterviewerStatus(String interviewId, String timeslotId, String userId, String statusValue);
}
