package com.example.internship_java.service;

import com.example.internship_java.model.Error;
import com.example.internship_java.model.InterviewType;
import com.example.internship_java.repository.InterviewTypeRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InterviewTypeServiceImpl implements InterviewTypeService {

    final
    InterviewTypeRepository interviewTypeRepository;

    public InterviewTypeServiceImpl(InterviewTypeRepository interviewTypeRepository) {
        this.interviewTypeRepository = interviewTypeRepository;
    }

    @Override
    public ResponseEntity<Collection<Error>> patchInterviewType(String name) {
        try {
            if (interviewTypeRepository.count() > 1999) {
                Error error = new Error("409", "INTERVIEW_TYPES_SIZE_EXCEEDED");
                List<Error> errors = new ArrayList<>();
                errors.add(error);
                return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
            }
            JsonArray convertedObject = new Gson().fromJson(name, JsonArray.class);
            Gson gson = new Gson();
            String[] newInterviewTypes = gson.fromJson(convertedObject, String[].class);
            List<InterviewType> interviewTypeList = interviewTypeRepository.findAll();
            Set<InterviewType> interviewTypeSet = new HashSet<>(interviewTypeList);

            for (String interviewType : newInterviewTypes) interviewTypeSet.add(new InterviewType(interviewType));
            interviewTypeRepository.saveAll(interviewTypeSet);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (JsonSyntaxException e) {
            Error error = new Error("422", "BAD_INPUT");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Collection<Error>> deleteInterviewType(String name) {
        try {
            interviewTypeRepository.delete(interviewTypeRepository.findByName(name));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidDataAccessApiUsageException e) {
            Error error = new Error("404", "INTERVIEW_TYPE_NOT_FOUND");
            List<Error> errors = new ArrayList<>();
            errors.add(error);
            return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Collection<InterviewType>> getInterviewTypes() {
        try {
            List<InterviewType> listTypes = interviewTypeRepository.findAll();
            return new ResponseEntity<>(listTypes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
