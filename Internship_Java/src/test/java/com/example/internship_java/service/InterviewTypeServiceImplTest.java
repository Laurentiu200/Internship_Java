package com.example.internship_java.service;

import com.example.internship_java.model.Error;
import com.example.internship_java.model.InterviewType;
import com.example.internship_java.model.Timeslot;
import com.example.internship_java.repository.InterviewTypeRepository;
import com.example.internship_java.repository.TimeslotRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterviewTypeServiceImplTest {

    @InjectMocks
    InterviewTypeServiceImpl interviewTypeService;

    @Mock
    InterviewTypeRepository interviewTypeRepository;

    @Mock
    TimeslotRepository timeslotRepository;

    @Test
    void patchInterviewTypeSuccess() {
       when(interviewTypeRepository.count()).thenReturn(200L);
        assertEquals(new ResponseEntity<>(HttpStatus.NO_CONTENT).getStatusCode(), interviewTypeService.patchInterviewType("[\n" +
                "  \"string\"\n" +
                "]").getStatusCode());
        verify(interviewTypeRepository,times(1)).count();
    }

    @Test
    void patchInterviewTypeBadInput() {
        when(interviewTypeRepository.count()).thenReturn(200L);

        assertEquals(new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY).getStatusCode(), interviewTypeService.patchInterviewType("string").getStatusCode());
        verify(interviewTypeRepository,times(1)).count();
    }

    @Test
    void patchInterviewTypeSizeExceeded() {
        when(interviewTypeRepository.count()).thenReturn(2000L);

        assertEquals(new ResponseEntity<>(HttpStatus.CONFLICT).getStatusCode(), interviewTypeService.patchInterviewType("[\n" +
                "  \"string\"\n" +
                "]").getStatusCode());
        verify(interviewTypeRepository,times(1)).count();
    }

    @Test
    void patchInterviewTypeInternalError() {

        when(interviewTypeRepository.count()).thenThrow(new IndexOutOfBoundsException());
        assertEquals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR).getStatusCode(), interviewTypeService.patchInterviewType("[\n" +
                "  \"string\"\n" +
                "]").getStatusCode());
        verify(interviewTypeRepository,times(1)).count();
    }

    @Test
    void deleteInterviewTypeSuccess() {
        InterviewType interviewType =  new InterviewType("string");
        Timeslot timeslot = new Timeslot(interviewType,null,null,null,null,null);
        timeslot.setId("generatedId");
        List<Timeslot> timeslots = new ArrayList<>();
        timeslots.add(timeslot);
        InterviewType interviewType1 =  new InterviewType("strings");

        when(interviewTypeRepository.findByName(eq("strings"))).thenReturn(interviewType1);
        when(timeslotRepository.findAll()).thenReturn(timeslots);

        assertEquals(new ResponseEntity<>(HttpStatus.NO_CONTENT).getStatusCode(), interviewTypeService.deleteInterviewType("strings").getStatusCode());
        verify(interviewTypeRepository,times(2)).findByName(eq("strings"));
        verify(timeslotRepository,times(1)).findAll();
    }

    @Test
    void deleteInterviewTypeReferredConflict() {
        InterviewType interviewType =  new InterviewType("string");
        Timeslot timeslot = new Timeslot(interviewType,null,null,null,null,null);
        timeslot.setId("generatedId");
        List<Timeslot> timeslots = new ArrayList<>();
        timeslots.add(timeslot);

        when(interviewTypeRepository.findByName(eq("string"))).thenReturn(interviewType);
        when(timeslotRepository.findAll()).thenReturn(timeslots);

        assertEquals(new ResponseEntity<>(HttpStatus.CONFLICT).getStatusCode(), interviewTypeService.deleteInterviewType("string").getStatusCode());
        verify(interviewTypeRepository,times(1)).findByName(eq("string"));
        verify(timeslotRepository,times(1)).findAll();

    }
    @Test
    void deleteInterviewTypeNotFound() {
        when(interviewTypeRepository.findByName((eq("stringer")))).thenReturn(null);

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode(), interviewTypeService.deleteInterviewType("stringer").getStatusCode());
        verify(interviewTypeRepository,times(1)).findByName(eq("stringer"));
        verify(timeslotRepository,times(0)).findAll();

    }

    @Test
    void deleteInterviewTypeInternalError() {
        when(interviewTypeRepository.findByName((eq("stringer")))).thenThrow(new IndexOutOfBoundsException());

        assertEquals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR).getStatusCode(), interviewTypeService.deleteInterviewType("stringer").getStatusCode());
        verify(interviewTypeRepository,times(1)).findByName(eq("stringer"));
        verify(timeslotRepository,times(0)).findAll();

    }

    @Test
    void getInterviewTypesSuccess()  {
        List<InterviewType> interviewTypes = new ArrayList<>();
        InterviewType interviewType1 =  new InterviewType("InterviewType1");
        interviewTypes.add(interviewType1);

        when(interviewTypeRepository.findAll()).thenReturn(interviewTypes);

       assertEquals(new ResponseEntity<>(HttpStatus.OK).getStatusCode(), interviewTypeService.getInterviewTypes().getStatusCode());
       assertEquals(interviewTypes, interviewTypeService.getInterviewTypes().getBody().getInterviewTypes());
        verify(interviewTypeRepository,times(2)).findAll();
    }

    @Test
    void getInterviewTypesInternalError()  {
        Error error = new Error("500", "UNEXPECTED_ERROR");
        List<Error> errors = new ArrayList<>();
        errors.add(error);

        when(interviewTypeRepository.findAll()).thenThrow(new IndexOutOfBoundsException());

        assertEquals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR).getStatusCode(), interviewTypeService.getInterviewTypes().getStatusCode());
        assertEquals(errors.get(0).getMessage(),interviewTypeService.getInterviewTypes().getBody().getErrors().iterator().next().getMessage());
        verify(interviewTypeRepository,times(2)).findAll();
    }
}