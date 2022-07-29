package com.example.internship_java.service;

import com.example.internship_java.model.InterviewType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
@Service
public class InterviewTypeServiceImpl implements InterviewTypeService{
    @Override
    public ResponseEntity<HttpStatus> patchInterviewType(String name) {
        try{

        }
        catch(Exception e){

        }
        return null;
    }

    @Override
    public ResponseEntity<HttpStatus> deleteInterviewType(String name) {
        return null;
    }

    @Override
    public ResponseEntity<Collection<InterviewType>> getInterviewTypes() {
        return null;
    }
}
