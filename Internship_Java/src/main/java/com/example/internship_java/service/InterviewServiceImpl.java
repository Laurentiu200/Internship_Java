package com.example.internship_java.service;

import com.example.internship_java.model.*;
import com.example.internship_java.model.Error;
import com.example.internship_java.repository.*;
import com.example.internship_java.response.InterviewResponse;
import com.google.gson.JsonSyntaxException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Time;
import java.util.*;


@Service
public class InterviewServiceImpl implements InterviewService {

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

    public InterviewServiceImpl(CandidateRepository candidateRepository, TimeslotRepository timeslotRepository, InterviewerRepository interviewerRepository, InterviewRepository interviewsInterface, InterviewTypeRepository interviewTypeRepository) {
        this.candidateRepository = candidateRepository;
        this.timeslotRepository = timeslotRepository;
        this.interviewerRepository = interviewerRepository;
        this.interviewsInterface = interviewsInterface;
        this.interviewTypeRepository = interviewTypeRepository;
    }



    private void modifyInterview(Interview interview, String interviewID)
    {
        Interview new_interview = interviewsInterface.findById(interviewID).get();
        if(interview.getCandidate().getStatus() != null) {
            new_interview.getCandidate().setStatus(interview.getCandidate().getStatus());
        }
        if(interview.getLocation() != null) {
            new_interview.setLocation(interview.getLocation());
        }
        if(interview.getTimezone() != null) {
            new_interview.setTimezone(interview.getTimezone());
        }
        if(interview.getOrganizerId() != null) {
            new_interview.setOrganizerId(interview.getOrganizerId());
        }
        interviewsInterface.save(new_interview);
    }

    public boolean validTimeZone(String timezone) {
        return Set.of(TimeZone.getAvailableIDs()).contains(timezone);
    }

    @PostMapping
    public ResponseEntity<InterviewResponse> putInterview(Interview interview) {
        List<Error> errors = new ArrayList<>();
        try {
            if (interviewsInterface.findById(interview.getId()).isPresent()) {
                Error error = new Error("403", "INTERVIEW ALREADY EXIST");
                errors.add(error);
                return new ResponseEntity<>(new InterviewResponse(errors,null), HttpStatus.FORBIDDEN);
            }

            if(interview.getOrganizerId() == null) {
                Error error = new Error("422", "ORGANIZER ID NOT FOUND");
                errors.add(error);
            }
            if(!interview.getTimeslots().isEmpty())
            {
                List<Timeslot> timeslots = interview.getTimeslots();
                for(Timeslot e : timeslots)
                {
                    if(timeslotRepository.findById(e.getId()).isPresent()) {
                        Error error = new Error("422", "TIMESLOT ALREADY EXIST");
                        errors.add(error);
                    }
                    if(e.getInterviewers().isEmpty()) {
                        Error error = new Error("422", "INTERVIEWERS NOT SET");
                        errors.add(error);
                    }
                    if(e.getStartsOn().compareTo(e.getEndsOn()) > 0)
                    {
                        Error error = new Error("422", "END_DATE_BEFORE_START_DATE");
                        errors.add(error);
                    }
                    if(e.getInterviewers().size() > 50) {
                        Error error = new Error("422", "INTERVIEWERS SIZE EXCEEDED");
                        errors.add(error);
                    }
                }
            }

            if(interview.getTimeslots().size() > 50) {
                Error error = new Error("422", "TIMESLOTS SIZE EXCEEDED");
                errors.add(error);
            }

            if(!errors.isEmpty())
                return new ResponseEntity<>(new InterviewResponse(errors,null), HttpStatus.NOT_ACCEPTABLE);

        } catch (JsonSyntaxException e) {
            Error error = new Error("422", "BAD_INPUT");
            errors.add(error);
            return new ResponseEntity<>(new InterviewResponse(errors,null), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            Error error = new Error("500", "UNEXPECTED ERROR");
            errors.add(error);
            return new ResponseEntity<>(new InterviewResponse(errors,null),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(!validTimeZone(interview.getTimezone().getID()))
        {
            interview.setTimezone(TimeZone.getDefault());
        }
        Candidate candidate = interview.getCandidate();
        candidateRepository.save(candidate);
        for (Timeslot e : interview.getTimeslots()) {
            interviewerRepository.saveAll(e.getInterviewers());
            if(interviewTypeRepository.findByName(e.getInterviewType().getName()) != null)
                e.setInterviewType(interviewTypeRepository.findByName(e.getInterviewType().getName()));
            interviewTypeRepository.save(e.getInterviewType());
        }
        timeslotRepository.saveAll(interview.getTimeslots());
        interviewsInterface.save(interview);
        Collection<Interview> interviews = new ArrayList<>();
        interviews.add(interview);
        return new ResponseEntity<>(new InterviewResponse(null, interviews),HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<InterviewResponse> patchInterview(Interview interviewToAdd, String interviewID) {
        List<Error> errors = new ArrayList<>();
        try {
            if (interviewsInterface.findById(interviewID).isEmpty()) {
                Error error = new Error("404", "INTERVIEW NOT FOUND");
                errors.add(error);
                return new ResponseEntity<>(new InterviewResponse(errors,null), HttpStatus.NOT_FOUND);
            }

            if(interviewToAdd.getTimezone() != null){
                System.out.println(interviewToAdd.getTimezone());
                if (!validTimeZone(interviewToAdd.getTimezone().getID())) {
                    Error error = new Error("400", "INVALID TIMEZONE");
                    errors.add(error);
                    return new ResponseEntity<>(new InterviewResponse(errors,null), HttpStatus.CONFLICT);
                }
            }

        } catch (JsonSyntaxException e) {
            Error error = new Error("422", "BAD_INPUT");
            errors.add(error);
            return new ResponseEntity<>(new InterviewResponse(errors,null), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            Error error = new Error("500", "UNEXPECTED ERROR");
            errors.add(error);
            return new ResponseEntity<>(new InterviewResponse(errors,null),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        modifyInterview(interviewToAdd, interviewID);
        Error error = new Error("204", "INTERVIEW UPDATED");
        errors.add(error);
        return new ResponseEntity<>(new InterviewResponse(errors,null), HttpStatus.CREATED);

    }


    @Override
    public ResponseEntity<InterviewResponse> getAllInterviewers() {

        Collection<Interview> interviewResponse = new ArrayList<>();
        try {
            if (!interviewsInterface.findAll().isEmpty())
                interviewResponse = interviewsInterface.findAll();
        } catch (Exception e) {
            Error error = new Error("500", "Unexpected error");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(new InterviewResponse(errors, null), HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<>(new InterviewResponse(null,interviewResponse), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<InterviewResponse> getInterview(String interview_ID) {

        try {
            if (interviewsInterface.findById(interview_ID).isEmpty()) {
                Error error = new Error("404", "INTERVIEW NOT FOUND");
                List<Error> errors = new ArrayList<>();
                errors.add(error);

                return new ResponseEntity<>(new InterviewResponse(errors,null), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Error error = new Error("500", "Unexpected error");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(new InterviewResponse(errors,null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Collection<Interview> interviews = new ArrayList<>();
        interviews.add(interviewsInterface.findById(interview_ID).get());

        return new ResponseEntity<>(new InterviewResponse(null,interviews), HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<InterviewResponse> deleteInterview(String interview_ID) {
        try {
            if (interviewsInterface.findById(interview_ID).isEmpty()) {
                Error error = new Error("404", "INTERVIEW NOT FOUND");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(new InterviewResponse(errors,null), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Error error = new Error("500", "Unexpected error");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(new InterviewResponse(errors,null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Interview interview;
        interview = interviewsInterface.findById(interview_ID).get();
        Candidate candidate = interview.getCandidate();
        List<Timeslot> timeslot = interview.getTimeslots();
        interviewsInterface.delete(interview);
        candidateRepository.delete(candidate);
        for(Timeslot t : timeslot) {
            List<Interviewer> interviewers = t.getInterviewers();
            InterviewType interviewType = t.getInterviewType();
            t.setInterviewers(null);
            timeslotRepository.delete(t);
            interviewerRepository.deleteAll(interviewers);
            interviewTypeRepository.delete(interviewType);
        }
        Error error = new Error("204", "INTERVIEW WAS DELETED");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        return new ResponseEntity<>(new InterviewResponse(errors, null), HttpStatus.CREATED);
    }


}
