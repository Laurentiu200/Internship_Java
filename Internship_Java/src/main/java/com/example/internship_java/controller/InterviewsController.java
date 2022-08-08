package com.example.internship_java.controller;

import com.example.internship_java.model.Interview;
import com.example.internship_java.repository.*;
import com.example.internship_java.response.InterviewResponse;
import com.example.internship_java.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    InterviewRepository interviewsInterface;
    final
    InterviewTypeRepository interviewTypeRepository;

    final
    InterviewService interviewService;
    public InterviewsController(CandidateRepository candidateRepository, TimeslotRepository timeslotRepository, InterviewerRepository interviewerRepository, InterviewRepository interviewsInterface, InterviewTypeRepository interviewTypeRepository, InterviewService interviewService) {
        this.candidateRepository = candidateRepository;
        this.timeslotRepository = timeslotRepository;
        this.interviewerRepository = interviewerRepository;
        this.interviewsInterface = interviewsInterface;
        this.interviewTypeRepository = interviewTypeRepository;
        this.interviewService = interviewService;
    }



    @GetMapping(value = "/interviews")
    public ResponseEntity<InterviewResponse> getInterviews()
    {
        return interviewService.getAllInterviewers();
    }


    @GetMapping(value = "/interviews/{interviewId}")
    public ResponseEntity<InterviewResponse> getInterview(@PathVariable("interviewId") String interviewId)
    {
        return interviewService.getInterview(interviewId);
    }

    @DeleteMapping (value = "/interviews/{interviewId}")
    public ResponseEntity<InterviewResponse> deleteInterview(@PathVariable("interviewId") String interviewId)
    {
        return interviewService.deleteInterview(interviewId);
    }

    @PostMapping(value = "/interviews",consumes = "application/json")
    public ResponseEntity<InterviewResponse> addTimeslot( @RequestBody Interview interviewToAdd){
        return interviewService.putInterview(interviewToAdd);
    }

    @PatchMapping(value = "interviews/{interviewID}", consumes = "application/json")
    public ResponseEntity<InterviewResponse> moddifyInterview(@PathVariable("interviewID") String interviewID, @RequestBody Interview interview)
    {
        System.out.println(interview.getTimezone());
        return interviewService.patchInterview(interview, interviewID);
    }
}
