package com.example.internship_java.service;

import com.example.internship_java.model.InterviewType;
import com.example.internship_java.repository.InterviewTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
            while (name.charAt(0) != ']') {
                String nameFormat;
                System.out.println(name);
                if (name.indexOf(',') != -1)
                    nameFormat = name.substring(name.indexOf('"') + 1, name.indexOf(',') - 1);
                else
                    nameFormat = name.substring(name.indexOf('"') + 1, name.indexOf(']') - 1);
                System.out.println(nameFormat);
                interviewTypeRepository.save(new InterviewType(nameFormat));
                System.out.println(name);
                if(name.indexOf(',')!=-1)
                name = name.substring(name.indexOf(',') + 1);
                else
                    name = name.substring(name.indexOf(']'));
            }
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
