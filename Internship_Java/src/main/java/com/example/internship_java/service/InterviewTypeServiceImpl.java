package com.example.internship_java.service;

import com.example.internship_java.model.InterviewType;
import com.example.internship_java.repository.InterviewTypeRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class InterviewTypeServiceImpl implements InterviewTypeService {

    final
    InterviewTypeRepository interviewTypeRepository;

    public InterviewTypeServiceImpl(InterviewTypeRepository interviewTypeRepository) {
        this.interviewTypeRepository = interviewTypeRepository;
    }

    @Override
    public ResponseEntity<HttpStatus> patchInterviewType(String name) {
        try {

            JsonArray convertedObject = new Gson().fromJson(name, JsonArray.class);
            Gson gson = new Gson();
            String[] interviewTypes = gson.fromJson(convertedObject , String[].class);
            for (String interviewType : interviewTypes) interviewTypeRepository.save(new InterviewType(interviewType));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<HttpStatus> deleteInterviewType(String name) {
        try {
            interviewTypeRepository.delete(interviewTypeRepository.findByName(name));
            return new ResponseEntity<>(HttpStatus.OK);
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
