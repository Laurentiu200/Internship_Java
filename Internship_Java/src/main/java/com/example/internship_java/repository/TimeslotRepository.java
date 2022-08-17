package com.example.internship_java.repository;

import com.example.internship_java.model.InterviewType;
import com.example.internship_java.model.Interviewer;
import com.example.internship_java.model.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeslotRepository extends JpaRepository<Timeslot, Object> {
    List<Timeslot> findTimeslotByInterviewers(Interviewer interviewer);
    List<Timeslot> findTimeslotByInterviewType(InterviewType interviewType);
}
