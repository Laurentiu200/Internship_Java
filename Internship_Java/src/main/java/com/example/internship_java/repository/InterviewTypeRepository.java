package com.example.internship_java.repository;

import com.example.internship_java.model.InterviewType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewTypeRepository extends JpaRepository<InterviewType, Object> {

    @Query
    InterviewType findByName(String interviewType);
}