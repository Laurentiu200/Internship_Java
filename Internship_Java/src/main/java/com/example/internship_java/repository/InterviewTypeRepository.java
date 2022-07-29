package com.example.internship_java.repository;

import com.example.internship_java.model.InterviewType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewTypeRepository extends JpaRepository<InterviewType, String> {
    InterviewType findByInterviewType(String interviewType);
}