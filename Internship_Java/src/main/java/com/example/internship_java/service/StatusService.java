package com.example.internship_java.service;

import com.example.internship_java.response.StatusResponse;
import org.springframework.http.ResponseEntity;

public interface StatusService {
    ResponseEntity<StatusResponse> updateCandidateStatus(String interviewId, String statusValue);

    ResponseEntity<StatusResponse> updateInterviewerStatus(String interviewId, String timeslotId, String userId, String statusValue);
}
