package com.example.internship_java.service;

import com.example.internship_java.model.*;
import com.example.internship_java.model.Error;
import com.example.internship_java.repository.InterviewTypeRepository;
import com.example.internship_java.repository.InterviewerRepository;
import com.example.internship_java.repository.InterviewsInterface;
import com.example.internship_java.repository.TimeslotRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class TimeslotsServiceImpl implements TimeslotsService{
    final
    InterviewsInterface interviewsInterface;
    final
    TimeslotRepository timeslotRepository;

    final
    InterviewerRepository interviewerRepository;
    final
    InterviewTypeRepository interviewTypeRepository;
    public TimeslotsServiceImpl(InterviewsInterface interviewsInterface, TimeslotRepository timeslotRepository, InterviewerRepository interviewerRepository, InterviewTypeRepository interviewTypeRepository) {
        this.interviewsInterface = interviewsInterface;
        this.timeslotRepository = timeslotRepository;
        this.interviewerRepository = interviewerRepository;
        this.interviewTypeRepository = interviewTypeRepository;
    }

    @Override
    public ResponseEntity<Collection<Error>> addTimeslot(String interviewId, Timeslot timeslotToAdd) {
        try {
            Interview interview = interviewsInterface.findById(interviewId);
            List<Timeslot> timeslots = interview.getTimeslots();
            if(timeslots.size()>49){
                Error error = new Error("409", "TIMESLOTS_SIZE_EXCEEDED");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
            }
            System.out.println("Da");
            interviewerRepository.saveAll(timeslotToAdd.getInterviewers());
            interviewTypeRepository.save(timeslotToAdd.getInterviewType());
            timeslots.add(timeslotToAdd);
            timeslotRepository.save(timeslotToAdd);
            interview.setTimeslots(timeslots);
            interviewsInterface.save(interview);
            return new ResponseEntity<>( HttpStatus.OK);
        }
        catch(NullPointerException e){
            Error error = new Error("404", "INTERVIEW_NOT_FOUND");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
        }
        catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Collection<Error>> deleteTimeslot(String interviewId, String timeslotId) {
        try {
            Interview interview = interviewsInterface.findById(interviewId);
            List<Timeslot> timeslotList= interview.getTimeslots();
            if(timeslotList.size()==1) {
                Error error = new Error("409", "CANNOT_DELETE_LAST_TIMESLOT");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
            }
            int found=0;
            for(Timeslot e: timeslotList)
                if(e.getId().equals(timeslotId)) {
                    timeslotList.remove(e);
                    found=1;
                    break;
                }
            if(found==0){
                Error error = new Error("404", "TIMESLOT_NOT_FOUND");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
            }
            interviewsInterface.save(interview);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(NullPointerException e){
            Error error = new Error("404", "INTERVIEW_NOT_FOUND");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Timeslot> getTimeslot(String interviewId, String timeslotId) {
        try {
            Interview interview = interviewsInterface.findById(interviewId);
            List<Timeslot> timeslotList = interview.getTimeslots();
            for(Timeslot e: timeslotList)
                if(e.getId().equals(timeslotId))
                    return new ResponseEntity<>(e, HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        catch(NullPointerException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Collection<Error>> updateTimeslot(String interviewId, String timeslotId, Timeslot updatedTimeslot) {
        try {
            Interview interview = interviewsInterface.findById(interviewId);
            List<Timeslot> timeslotList = interview.getTimeslots();
            int found=0;
            for(Timeslot e: timeslotList)
                if(e.getId().equals(timeslotId)) {
                    timeslotList.remove(e);
                    found=1;
                    break;
                }
            if(found==0){
                Error error = new Error("404", "TIMESLOT_NOT_FOUND");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
            }
            updatedTimeslot.setId(timeslotId);
            timeslotList.add(updatedTimeslot);
            timeslotRepository.save(updatedTimeslot);
            interview.setTimeslots(timeslotList);
            interviewsInterface.save(interview);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NullPointerException e) {
            Error error = new Error("404", "INTERVIEW_NOT_FOUND");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
