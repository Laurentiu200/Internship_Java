package com.example.internship_java.service;

import com.example.internship_java.model.*;
import com.example.internship_java.model.Error;
import com.example.internship_java.repository.InterviewTypeRepository;
import com.example.internship_java.repository.InterviewerRepository;
import com.example.internship_java.repository.InterviewRepository;
import com.example.internship_java.repository.TimeslotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TimeslotsServiceImpl implements TimeslotsService {
    final
    InterviewRepository interviewRepository;
    final
    TimeslotRepository timeslotRepository;
    final
    InterviewerRepository interviewerRepository;
    final
    InterviewTypeRepository interviewTypeRepository;

    public TimeslotsServiceImpl(InterviewRepository interviewRepository, TimeslotRepository timeslotRepository, InterviewerRepository interviewerRepository, InterviewTypeRepository interviewTypeRepository) {
        this.interviewRepository = interviewRepository;
        this.timeslotRepository = timeslotRepository;
        this.interviewerRepository = interviewerRepository;
        this.interviewTypeRepository = interviewTypeRepository;
    }
    @Override
    public ResponseEntity<Object> addTimeslot(String interviewId, Timeslot timeslotToAdd) {
        try {
            Optional<Interview> interviewOptional = interviewRepository.findById(interviewId);
            if (interviewOptional.isPresent()) {
                List<Timeslot> timeslots = interviewOptional.get().getTimeslots();
                if (timeslots.size() > 49) {
                    Error error = new Error("409", "TIMESLOTS_SIZE_EXCEEDED");
                    List<Error> errors = new ArrayList<>();
                    errors.add(error);
                    return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
                }
                interviewerRepository.saveAll(timeslotToAdd.getInterviewers());
                if (interviewTypeRepository.findByName(timeslotToAdd.getInterviewType().getName()) != null) {
                    timeslotToAdd
                            .getInterviewType()
                            .setId(interviewTypeRepository
                                    .findByName(timeslotToAdd.getInterviewType().getName()).getId());
                }
                interviewTypeRepository.save(timeslotToAdd.getInterviewType());
                timeslots.add(timeslotToAdd);
                timeslotRepository.save(timeslotToAdd);
                interviewOptional.get().setTimeslots(timeslots);
                interviewRepository.save(interviewOptional.get());
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                Error error = new Error("404", "INTERVIEW_NOT_FOUND");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Object> deleteTimeslot(String interviewId, String timeslotId) {
        try {
            Optional<Interview> interviewOptional = interviewRepository.findById(interviewId);
            if (interviewOptional.isPresent()) {
                List<Timeslot> timeslotList = interviewOptional.get().getTimeslots();
                if (timeslotList.size() == 1) {
                    Error error = new Error("409", "CANNOT_DELETE_LAST_TIMESLOT");
                    List<Error> errors = new ArrayList<>();
                    errors.add(error);
                    return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
                }
                int found = 0;
                for (Timeslot e : timeslotList) {
                    if (e.getId().equals(timeslotId)) {
                        interviewerRepository.deleteAll(e.getInterviewers());
                        timeslotList.remove(e);
                        timeslotRepository.delete(e);
                        interviewTypeRepository.delete(e.getInterviewType());
                        found = 1;
                        break;
                    }
                }
                if (found == 0) {
                    Error error = new Error("404", "TIMESLOT_NOT_FOUND");
                    List<Error> errors = new ArrayList<>();
                    errors.add(error);
                    return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
                }
                interviewRepository.save(interviewOptional.get());
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                Error error = new Error("404", "INTERVIEW_NOT_FOUND");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Object> getTimeslot(String interviewId, String timeslotId) {
        try {
            Optional<Interview> interviewOptional = interviewRepository.findById(interviewId);
            if (interviewOptional.isPresent()) {
                List<Timeslot> timeslotList = interviewOptional.get().getTimeslots();
                for (Timeslot e : timeslotList) {
                    if (e.getId().equals(timeslotId)) {
                        return new ResponseEntity<>(e, HttpStatus.OK);
                    }
                }
                Error error = new Error("404", "TIMESLOT_NOT_FOUND");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);

            } else {
                Error error = new Error("404", "INTERVIEW_NOT_FOUND");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Object> updateTimeslot(String interviewId, String timeslotId, Timeslot updatedTimeslot) {
        try {
            Optional<Interview> interviewOptional = interviewRepository.findById(interviewId);
            if (interviewOptional.isPresent()) {
                if (updatedTimeslot.getStartsOn().compareTo(updatedTimeslot.getEndsOn()) > 0) {
                    Error error = new Error("422", "END_DATE_BEFORE_START_DATE");
                    List<Error> errors = new ArrayList<>();
                    errors.add(error);
                    return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
                }
                List<Timeslot> timeslotList = interviewOptional.get().getTimeslots();
                int found = 0;
                for (Timeslot e : timeslotList) {
                    if (e.getId().equals(timeslotId)) {
                        timeslotList.remove(e);
                        found = 1;
                        break;
                    }
                }
                if (found == 0) {
                    Error error = new Error("404", "TIMESLOT_NOT_FOUND");
                    List<Error> errors = new ArrayList<>();
                    errors.add(error);
                    return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
                }
                for (Interviewer interviewer : updatedTimeslot.getInterviewers()) {
                    if (interviewerRepository.findById(interviewer.getId()).isEmpty()) {
                        Error error = new Error("422", "NON_EXISTING_INTERVIEWERS");
                        List<Error> errors = new ArrayList<>();
                        errors.add(error);
                        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
                    }
                }
                updatedTimeslot.setId(timeslotId);
                timeslotList.add(updatedTimeslot);
                if (interviewTypeRepository.findByName(updatedTimeslot.getInterviewType().getName()) == null) {
                    interviewTypeRepository.save(updatedTimeslot.getInterviewType());
                } else {
                    updatedTimeslot
                            .getInterviewType()
                            .setId(interviewTypeRepository
                                    .findByName(updatedTimeslot.getInterviewType().getName()).getId());
                    interviewTypeRepository.save(updatedTimeslot.getInterviewType());
                }
                interviewerRepository.saveAll(updatedTimeslot.getInterviewers());
                timeslotRepository.save(updatedTimeslot);
                interviewOptional.get().setTimeslots(timeslotList);
                interviewRepository.save(interviewOptional.get());
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                Error error = new Error("404", "INTERVIEW_NOT_FOUND");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);

            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
