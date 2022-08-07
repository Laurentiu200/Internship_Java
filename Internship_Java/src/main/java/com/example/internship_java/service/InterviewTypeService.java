package com.example.internship_java.service;

import org.springframework.http.ResponseEntity;

public interface InterviewTypeService {
    ResponseEntity<Object> patchInterviewType(String name);

    ResponseEntity<Object> deleteInterviewType(String name);

    ResponseEntity<Object> getInterviewTypes();
}
