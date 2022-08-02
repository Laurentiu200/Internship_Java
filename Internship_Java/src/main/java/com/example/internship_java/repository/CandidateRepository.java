package com.example.internship_java.repository;

import com.example.internship_java.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate,String> {
}
