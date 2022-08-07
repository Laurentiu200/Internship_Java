package com.example.internship_java.service;

import com.example.internship_java.model.*;
import com.example.internship_java.model.Error;
import com.example.internship_java.repository.CandidateRepository;
import com.example.internship_java.repository.InterviewRepository;
import com.example.internship_java.repository.InterviewerRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StatusServiceImpl implements StatusService {

    final
    InterviewRepository interviewRepository;
    final
    CandidateRepository candidateRepository;
    final
    InterviewerRepository interviewerRepository;

    public StatusServiceImpl(InterviewRepository interviewRepository, CandidateRepository candidateRepository, InterviewerRepository interviewerRepository) {
        this.interviewRepository = interviewRepository;
        this.candidateRepository = candidateRepository;
        this.interviewerRepository = interviewerRepository;
    }

    @Override
    public ResponseEntity<Object> updateCandidateStatus(String interviewId, String statusValue) {
        try {
            JsonObject convertedObject = new Gson().fromJson(statusValue, JsonObject.class);
            String status = convertedObject.get("status").getAsString();
            Optional<Interview> interviewOptional = interviewRepository.findById(interviewId);
            if (interviewOptional.isPresent()) {
                Optional<Candidate> candidateOptional = candidateRepository.findById(interviewOptional.get().getCandidate().getId());
                if (candidateOptional.isPresent()) {
                    candidateOptional.get().setStatus(AttendeeStatusValue.valueOf(status));
                    candidateRepository.save(candidateOptional.get());
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } else {
                    Error error = new Error("404", "CANDIDATE_NOT_FOUND");
                    List<Error> errors = new ArrayList<>();
                    errors.add(error);
                    return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
                }
            } else {
                Error error = new Error("404", "INTERVIEW_NOT_FOUND");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
            }
        } catch (JsonSyntaxException e) {
            Error error = new Error("422", "BAD_INPUT");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Object> updateInterviewerStatus(String interviewId, String timeslotId, String userId, String statusValue) {
        try {
            JsonObject convertedObject = new Gson().fromJson(statusValue, JsonObject.class);
            String status = convertedObject.get("status").getAsString();
            Optional<Interview> interviewOptional = interviewRepository.findById(interviewId);
            if (interviewOptional.isPresent()) {
                int found = 0;
                List<Timeslot> timeslotList = interviewOptional.get().getTimeslots();
                Timeslot timeslotToSearch = new Timeslot();
                for (Timeslot e : timeslotList) {
                    if (e.getId().equals(timeslotId)) {
                        found = 1;
                        timeslotToSearch = e;
                        break;
                    }
                }
                if (found == 0) {
                    Error error = new Error("404", "TIMESLOT_NOT_FOUND");
                    List<Error> errors = new ArrayList<>();
                    errors.add(error);
                    return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
                }
                List<Interviewer> interviewerList = timeslotToSearch.getInterviewers();
                found = 0;
                Interviewer interviewerToUpdate = new Interviewer();
                for (Interviewer e : interviewerList) {
                    if (e.getId().equals(userId)) {
                        found = 1;
                        interviewerToUpdate = e;
                        break;
                    }
                }
                if (found == 0) {
                    Error error = new Error("404", "NON_EXISTING_INTERVIEWERS");
                    List<Error> errors = new ArrayList<>();
                    errors.add(error);
                    return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
                }
                interviewerToUpdate.setStatus(AttendeeStatusValue.valueOf(status));
                interviewerRepository.save(interviewerToUpdate);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                Error error = new Error("404", "INTERVIEW_NOT_FOUND");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);

            }
        } catch (JsonSyntaxException e) {
            Error error = new Error("422", "BAD_INPUT");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
