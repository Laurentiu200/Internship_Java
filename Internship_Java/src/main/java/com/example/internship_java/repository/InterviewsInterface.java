package com.example.internship_java.repository;

import com.example.internship_java.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewsInterface extends JpaRepository<Interview,String> {
}
