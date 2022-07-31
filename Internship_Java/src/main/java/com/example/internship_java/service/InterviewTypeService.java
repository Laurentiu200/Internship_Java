package com.example.internship_java.service;

import com.example.internship_java.model.Error;
import com.example.internship_java.model.InterviewType;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

public interface InterviewTypeService {
    ResponseEntity<Collection<Error>> patchInterviewType(String name);

    ResponseEntity<Collection<Error>> deleteInterviewType(String name);

    ResponseEntity<Collection<InterviewType>> getInterviewTypes();
}
