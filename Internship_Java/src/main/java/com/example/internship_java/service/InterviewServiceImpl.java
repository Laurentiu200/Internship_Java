package com.example.internship_java.service;

import com.example.internship_java.model.*;
import com.example.internship_java.model.Error;
import com.example.internship_java.repository.*;
import com.google.gson.JsonSyntaxException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class InterviewServiceImpl implements InterviewService {

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

    public InterviewServiceImpl(CandidateRepository candidateRepository, TimeslotRepository timeslotRepository, InterviewerRepository interviewerRepository, InterviewsInterface interviewsInterface, InterviewTypeRepository interviewTypeRepository) {
        this.candidateRepository = candidateRepository;
        this.timeslotRepository = timeslotRepository;
        this.interviewerRepository = interviewerRepository;
        this.interviewsInterface = interviewsInterface;
        this.interviewTypeRepository = interviewTypeRepository;
    }


    private boolean checkInterviewer(Interviewer interviewer) {
        return interviewerRepository.findById(interviewer.getId()).isPresent();
    }

    private boolean checkInterviewers(List<Timeslot> timeslot) {
        AtomicInteger ok = new AtomicInteger();
        timeslot.forEach(timeslot1 -> {
            timeslot1.getInterviewers().forEach(interviewer -> {
                if (checkInterviewer(interviewer))
                    ok.set(1);
            });
        });
        return ok.get() != 1;
    }

    private void modifyInterview(Interview interview, String interviewID)
    {
        Interview new_interview = interviewsInterface.getById(interviewID);
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
    public ResponseEntity putInterview(Interview interview) {
        try {
            if (interviewsInterface.findById(interview.getId()).isPresent()) {
                Error error = new Error("403", "INTERVIEW ALREADY EXIST");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
            }
            if(!validTimeZone(interview.getTimezone().getID()))
            {
                Error error = new Error("400", "INVALID TIMEZONE");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
            }
            if(interview.getOrganizerId() == null) {
                Error error = new Error("422", "ORGANIZER ID NOTFOUND");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.NO_CONTENT);
            }
            if(!interview.getTimeslots().isEmpty())
            {
                List<Timeslot> timeslots = interview.getTimeslots();
                for(Timeslot e : timeslots)
                {
                    if(e.getInterviewers().isEmpty()) {
                        Error error = new Error("422", "INTERVIEWERS NOT  SET");
                        List<Error> errors = new ArrayList<>();
                        errors.add(error);
                        return new ResponseEntity<>(errors, HttpStatus.NOT_ACCEPTABLE);
                    }
                    if(e.getInterviewers().size() > 50) {
                        Error error = new Error("422", "INTERVIEWERS SIZE EXCEEDED");
                        List<Error> errors = new ArrayList<>();
                        errors.add(error);
                        return new ResponseEntity<>(errors, HttpStatus.NOT_ACCEPTABLE);
                    }
                }
            }

            if(interview.getTimeslots().size() > 50) {
                Error error = new Error("422", "TIMESLOTS SIZE EXCEEDED");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.NOT_ACCEPTABLE);
            }

        } catch (JsonSyntaxException e) {
            Error error = new Error("422", "BAD_INPUT");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            Error error = new Error("500", "UNEXPECTED ERROR");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(errors,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Candidate candidate = interview.getCandidate();
        candidateRepository.save(candidate);
        for (Timeslot e : interview.getTimeslots()) {
            interviewerRepository.saveAll(e.getInterviewers());
            interviewTypeRepository.save(e.getInterviewType());
        }
        timeslotRepository.saveAll(interview.getTimeslots());
        interviewsInterface.save(interview);
        return new ResponseEntity<>(interview,HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity patchInterview(Interview interviewToAdd, String interviewID) {


        try {
            if (interviewsInterface.findById(interviewID).isEmpty()) {
                Error error = new Error("404", "INTERVIEW NOT FOUND");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
            }

            if(interviewToAdd.getTimezone() != null){
                if (!validTimeZone(interviewToAdd.getTimezone().getID())) {
                    Error error = new Error("400", "INVALID TIMEZONE");
                    List<Error> errors = new ArrayList<>();
                    errors.add(error);
                    return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
                }
            }

        } catch (JsonSyntaxException e) {
            Error error = new Error("422", "BAD_INPUT");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            Error error = new Error("500", "UNEXPECTED ERROR");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(errors,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        modifyInterview(interviewToAdd, interviewID);
        Error error = new Error("204", "INTERVIEW UPDATED");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        return new ResponseEntity<>(errors, HttpStatus.CREATED);

    }


    @Override
    public ResponseEntity getAllInterviewers() {

        List<Interview> interviewResponse = new ArrayList<>();
        try {
            if (!interviewsInterface.findAll().isEmpty())
                interviewResponse = interviewsInterface.findAll();
        } catch (Exception e) {
            Error error = new Error("500", "Unexpected error");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<>(interviewResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity getInterview(String interview_ID) {
        try {
            if (interviewsInterface.findById(interview_ID).isEmpty()) {
                Error error = new Error("404", "INTERVIEW NOT FOUND");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Error error = new Error("500", "Unexpected error");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(interviewsInterface.findById(interview_ID), HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity deleteInterview(String interview_ID) {
        try {
            if (interviewsInterface.findById(interview_ID).isEmpty()) {
                Error error = new Error("404", "INTERVIEW NOT FOUND");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Error error = new Error("500", "Unexpected error");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        interviewsInterface.deleteById(interview_ID);
        Error error = new Error("204", "INTERVIEW WAS DELETED");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        return new ResponseEntity<>(errors, HttpStatus.CREATED);
    }


}
