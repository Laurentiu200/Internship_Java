package com.example.internship_java.controller;

import com.example.internship_java.model.*;
import com.example.internship_java.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.example.internship_java.model.AttendeeStatusValue.accepted;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class InterviewsController {

    @Autowired
    InterviewsInterface interviewsInterface;

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    TimeslotRepository timeslotRepository;
    @Autowired
    InterviewerRepository interviewerRepository;

    @Autowired
    InterviewTypeRepository interviewTypeRepository;

    @GetMapping(path = "/interviews", consumes = "application/json")
    public List<Interview> getInterviews()
    {
        return interviewsInterface.findAll();
    }

    @PostMapping(path = "/interviews",  consumes = "application/json")
    public Interview addInterview(@RequestBody Interview interview)
    {
        candidateRepository.save(interview.getCandidate());
        //       interview.getTimeSlots().forEach((t)->interviewerRepository.saveAll(t.getInterviewers()));
//        interview.getTimeSlots().forEach((t)->interviewTypeRepository.save(t.getInterviewType()));
        timeslotRepository.saveAll(interview.getTimeSlots());
        return interviewsInterface.save(interview);
    }
}
