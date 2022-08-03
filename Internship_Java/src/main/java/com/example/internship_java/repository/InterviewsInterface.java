package com.example.internship_java.repository;

import com.example.internship_java.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InterviewsInterface extends JpaRepository<Interview, Object> {
    @Query
    public Interview findById(String id);
}
