package com.example.internship_java.service;

import com.example.internship_java.model.Interview;
import com.example.internship_java.response.InterviewResponse;
import org.springframework.http.ResponseEntity;

public interface InterviewService {
    ResponseEntity<InterviewResponse> getAllInterviewers();
    ResponseEntity<InterviewResponse> getInterview(String interview_id);
    ResponseEntity<InterviewResponse> deleteInterview(String interview_ID);
    ResponseEntity<InterviewResponse> patchInterview(Interview interviewToAdd, String interviewID);
    ResponseEntity<InterviewResponse> putInterview(Interview interviewToAdd);
}
