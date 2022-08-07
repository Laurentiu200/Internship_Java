package com.example.internship_java.service;

import com.example.internship_java.model.Interview;
import org.springframework.http.ResponseEntity;

public interface InterviewService {
    ResponseEntity<Object> getAllInterviewers();
    ResponseEntity<Object> getInterview(String interview_id);
    ResponseEntity<Object> deleteInterview(String interview_ID);
    ResponseEntity<Object> patchInterview(Interview interviewToAdd, String interviewID);
    ResponseEntity<Object> putInterview(Interview interviewToAdd);
}
