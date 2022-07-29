package com.example.internship_java.service;

import com.example.internship_java.model.InterviewType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

public interface InterviewTypeService {
    public ResponseEntity<HttpStatus> patchInterviewType(String name);
    public ResponseEntity<HttpStatus> deleteInterviewType(String name);
    public ResponseEntity<Collection<InterviewType>> getInterviewTypes();
}
