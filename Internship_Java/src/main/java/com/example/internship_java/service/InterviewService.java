package com.example.internship_java.service;

import com.example.internship_java.model.Error;
import com.example.internship_java.model.Interview;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

public interface InterviewService {
    ResponseEntity getAllInterviewers();
    ResponseEntity getInterview(String interview_id);
    ResponseEntity deleteInterview(String interview_ID);
    ResponseEntity patchInterview(Interview interviewToAdd, String interviewID);
    public ResponseEntity putInterview(Interview interviewToAdd);
}
