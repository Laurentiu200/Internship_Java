package com.example.internship_java.service;

import com.example.internship_java.response.InterviewTypeResponse;
import org.springframework.http.ResponseEntity;

public interface InterviewTypeService {
    ResponseEntity<InterviewTypeResponse> patchInterviewType(String name);

    ResponseEntity<InterviewTypeResponse> deleteInterviewType(String name);

    ResponseEntity<InterviewTypeResponse> getInterviewTypes();
}
