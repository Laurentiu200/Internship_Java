package com.example.internship_java.controller;
import com.example.internship_java.model.Candidate;
import com.example.internship_java.model.Error;
import com.example.internship_java.model.Interview;
import com.example.internship_java.model.Timeslot;
import com.example.internship_java.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class InterviewsController {
    final
    CandidateRepository candidateRepository;
    final
    TimeslotRepository timeslotRepository;
    final
    InterviewerRepository interviewerRepository;

    final
    InterviewsInterface interviewsInterface;
    final
    InterviewTypeRepository interviewTypeRepository;
    public InterviewsController(CandidateRepository candidateRepository, TimeslotRepository timeslotRepository, InterviewerRepository interviewerRepository, InterviewsInterface interviewsInterface, InterviewTypeRepository interviewTypeRepository) {
        this.candidateRepository = candidateRepository;
        this.timeslotRepository = timeslotRepository;
        this.interviewerRepository = interviewerRepository;
        this.interviewsInterface = interviewsInterface;
        this.interviewTypeRepository = interviewTypeRepository;
    }
    @PostMapping(value = "/interviews",consumes = "application/json")
    public ResponseEntity<Collection<Error>> addTimeslot( @RequestBody Interview interviewToAdd){


            Candidate candidate = interviewToAdd.getCandidate();
            candidateRepository.save(candidate);
            for (Timeslot e : interviewToAdd.getTimeslots()) {
                interviewerRepository.saveAll(e.getInterviewers());
                interviewTypeRepository.save(e.getInterviewType());
            }
            timeslotRepository.saveAll(interviewToAdd.getTimeslots());


        if(interviewsInterface.findById(interviewToAdd.getId()) ==  null)
            interviewsInterface.save(interviewToAdd);
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
