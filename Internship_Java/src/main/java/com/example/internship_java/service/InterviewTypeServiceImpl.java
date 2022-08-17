package com.example.internship_java.service;

import com.example.internship_java.model.Error;
import com.example.internship_java.model.InterviewType;
import com.example.internship_java.model.Timeslot;
import com.example.internship_java.repository.InterviewTypeRepository;
import com.example.internship_java.repository.TimeslotRepository;
import com.example.internship_java.response.InterviewTypeResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InterviewTypeServiceImpl implements InterviewTypeService {

    final
    InterviewTypeRepository interviewTypeRepository;
    final
    TimeslotRepository timeslotRepository;

    public InterviewTypeServiceImpl(InterviewTypeRepository interviewTypeRepository, TimeslotRepository timeslotRepository) {
        this.interviewTypeRepository = interviewTypeRepository;
        this.timeslotRepository = timeslotRepository;
    }

    @Override
    public ResponseEntity<InterviewTypeResponse> patchInterviewType(String name) {
        try {
            if (interviewTypeRepository.count() > 1999) {
                Error error = new Error("409", "INTERVIEW_TYPES_SIZE_EXCEEDED");
                Collection<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(new InterviewTypeResponse(null, errors), HttpStatus.CONFLICT);
            }
            JsonArray convertedObject = new Gson().fromJson(name, JsonArray.class);
            Gson gson = new Gson();
            String[] newInterviewTypes = gson.fromJson(convertedObject, String[].class);
            for (String interviewType : newInterviewTypes) interviewTypeRepository.save(new InterviewType(interviewType));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (JsonSyntaxException e) {
            Error error = new Error("422", "BAD_INPUT");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(new InterviewTypeResponse(null, errors), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            Error error = new Error("500", "UNEXPECTED_ERROR");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(new InterviewTypeResponse(null, errors), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<InterviewTypeResponse> deleteInterviewType(String name) {
        try {
            if (interviewTypeRepository.findByName(name) != null) {
                for(Timeslot t: timeslotRepository.findAll())
                    if(t.getInterviewType().getName().equals(name)){
                        Error error = new Error("409", "INTERVIEW_TYPE_IS_REFERRED");
                        List<Error> errors = new ArrayList<>();
                        errors.add(error);
                        return new ResponseEntity<>(new InterviewTypeResponse(null, errors), HttpStatus.CONFLICT);
                    }

                interviewTypeRepository.delete(interviewTypeRepository.findByName(name));
            } else {
                Error error = new Error("404", "INTERVIEW_TYPE_NOT_FOUND");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(new InterviewTypeResponse(null, errors), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            Error error = new Error("500", "UNEXPECTED_ERROR");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(new InterviewTypeResponse(null, errors), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<InterviewTypeResponse> getInterviewTypes() {
        try {
            List<InterviewType> interviewTypes = interviewTypeRepository.findAll();
            InterviewTypeResponse interviewTypeResponse = new InterviewTypeResponse(interviewTypes, null);
            return new ResponseEntity<>(interviewTypeResponse, HttpStatus.OK);
        } catch (Exception e) {
            Error error = new Error("500", "UNEXPECTED_ERROR");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(new InterviewTypeResponse(null, errors), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
