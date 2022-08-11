package com.example.internship_java.service;

import com.example.internship_java.model.InterviewType;
import com.example.internship_java.repository.InterviewTypeRepository;
import com.example.internship_java.response.InterviewTypeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InterviewTypeServiceImplTest {

    @InjectMocks
    InterviewTypeServiceImpl interviewTypeService;

    @Mock
    InterviewTypeService interviewTypeServiceInterface;

    @Mock
    InterviewTypeRepository interviewTypeRepository;


    @Test
    void patchInterviewType() {
    }

    @Test
    void deleteInterviewType() {
    }

    @Test
    void getInterviewTypes()  {
        List<InterviewType> interviewTypes = new ArrayList<>();
        InterviewType interviewType1 =  new InterviewType("InterviewType1");
        InterviewType interviewType2  = new InterviewType("InterviewType2");
        InterviewType interviewType3 = new InterviewType("InterviewType3");

        interviewTypes.add(interviewType1);
        interviewTypes.add(interviewType2);
        interviewTypes.add(interviewType3);
   //     when(interviewTypeServiceInterface.getInterviewTypes()).thenReturn(new ResponseEntity<>(new InterviewTypeResponse(interviewTypes,null),HttpStatus.OK));

        when(interviewTypeRepository.findAll()).thenReturn(interviewTypes);
        //when(interviewTypeServiceInterface.getInterviewTypes()).thenReturn(new ResponseEntity<>(new InterviewTypeResponse(interviewTypes,null),HttpStatus.OK));
        InterviewTypeResponse interviewTypeResponse = new InterviewTypeResponse(interviewTypes,null);
        ResponseEntity<InterviewTypeResponse> responseEntity = new ResponseEntity<>(interviewTypeResponse, HttpStatus.OK);
        ResponseEntity<InterviewTypeResponse> interviewTypeList = interviewTypeService.getInterviewTypes();

        assertEquals (responseEntity.getStatusCode(),interviewTypeList.getStatusCode());
    }
}