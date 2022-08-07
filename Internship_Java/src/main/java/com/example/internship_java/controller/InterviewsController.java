package com.example.internship_java.controller;
import com.example.internship_java.model.Error;
import com.example.internship_java.model.Interview;
import com.example.internship_java.repository.*;
import com.example.internship_java.service.InterviewService;
import org.springframework.http.ResponseEntity;
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

    final
    InterviewService interviewService;
    public InterviewsController(CandidateRepository candidateRepository, TimeslotRepository timeslotRepository, InterviewerRepository interviewerRepository, InterviewsInterface interviewsInterface, InterviewTypeRepository interviewTypeRepository, InterviewService interviewService) {
        this.candidateRepository = candidateRepository;
        this.timeslotRepository = timeslotRepository;
        this.interviewerRepository = interviewerRepository;
        this.interviewsInterface = interviewsInterface;
        this.interviewTypeRepository = interviewTypeRepository;
        this.interviewService = interviewService;
    }



    @GetMapping(value = "/interviews")
    public ResponseEntity getInterviews()
    {
        return interviewService.getAllInterviewers();
    }


    @GetMapping(value = "/interviews/{interviewId}")
    public ResponseEntity getInterview(@PathVariable("interviewId") String interviewId)
    {
        return interviewService.getInterview(interviewId);
    }

    @DeleteMapping (value = "/interviews/{interviewId}")
    public ResponseEntity deleteInterview(@PathVariable("interviewId") String interviewId)
    {
        return interviewService.deleteInterview(interviewId);
    }

    @PostMapping(value = "/interviews",consumes = "application/json")
    public ResponseEntity<Collection<Error>> addTimeslot( @RequestBody Interview interviewToAdd){
        return interviewService.putInterview(interviewToAdd);
//
//            Candidate candidate = interviewToAdd.getCandidate();
//            candidateRepository.save(candidate);
//            for (Timeslot e : interviewToAdd.getTimeslots()) {
//                interviewerRepository.saveAll(e.getInterviewers());
//                interviewTypeRepository.save(e.getInterviewType());
//            }
//            timeslotRepository.saveAll(interviewToAdd.getTimeslots());
//
//
//        if(interviewsInterface.findById(interviewToAdd.getId()) ==  null)
//            interviewsInterface.save(interviewToAdd);
//        else
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//
//        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @PatchMapping(value = "interviews/{interviewID}", consumes = "application/json")
    public ResponseEntity moddifyInterview(@PathVariable("interviewID") String interviewID, @RequestBody Interview interview)
    {
        System.out.println(interview.getTimezone());
        return interviewService.patchInterview(interview, interviewID);
    }
}
