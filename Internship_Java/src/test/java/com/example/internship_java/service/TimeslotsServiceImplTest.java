package com.example.internship_java.service;

import com.example.internship_java.model.*;
import com.example.internship_java.model.Error;
import com.example.internship_java.repository.InterviewRepository;
import com.example.internship_java.repository.InterviewTypeRepository;
import com.example.internship_java.repository.InterviewerRepository;
import com.example.internship_java.repository.TimeslotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeslotsServiceImplTest {

    @InjectMocks
    TimeslotsServiceImpl timeslotsServiceImpl;

    @Mock
    InterviewRepository interviewRepository;

    @Mock
    InterviewerRepository interviewerRepository;
    @Mock
    InterviewTypeRepository interviewTypeRepository;
    @Mock
    TimeslotRepository timeslotRepository;

    @Test
    void addTimeslotSuccessNonExistingInterviewType() {
        List<Interviewer> interviewers = new ArrayList<>(50);
        interviewers.add(new Interviewer("str", AttendeeStatusValue.pending));
        List<Timeslot> timeslots = new ArrayList<>(50);
        Timeslot timeslot = new Timeslot(new InterviewType("stringr"), null, null, null, null, interviewers);
        timeslot.setId("generatedId");
        timeslots.add(timeslot);
        Interview interview = new Interview
                (new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, timeslots, null, null, null);

        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));

        assertEquals(new ResponseEntity<>(HttpStatus.OK).getStatusCode(), timeslotsServiceImpl.addTimeslot(interview.getId(), timeslot).getStatusCode());
        verify(interviewRepository, times(1)).findById(interview.getId());
        verify(interviewRepository, times(1)).save(interview);
    }

    @Test
    void addTimeslotSuccessExistingInterviewType() {
        List<Interviewer> interviewers = new ArrayList<>(50);
        interviewers.add(new Interviewer("str", AttendeeStatusValue.pending));
        List<Timeslot> timeslots = new ArrayList<>(50);
        InterviewType interviewType = new InterviewType("stringr");
        Timeslot timeslot = new Timeslot(interviewType, null, null, null, null, interviewers);
        timeslot.setId("generatedId");
        timeslots.add(timeslot);
        Interview interview = new Interview(new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, timeslots, null, null, null);

        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));
        when(interviewTypeRepository.findByName(interviewType.getName())).thenReturn(interviewType);

        assertEquals(new ResponseEntity<>(HttpStatus.OK).getStatusCode(), timeslotsServiceImpl.addTimeslot(interview.getId(), timeslot).getStatusCode());
        assertEquals(timeslot, timeslotsServiceImpl.addTimeslot(interview.getId(), timeslot).getBody().getTimeslot());
        verify(interviewRepository, times(2)).findById(interview.getId());
        verify(interviewRepository, times(2)).save(interview);
    }

    @Test
    void addTimeslotInterviewNotFound() {
        Error error = new Error("500", "INTERVIEW_NOT_FOUND");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        Timeslot timeslot = new Timeslot(new InterviewType("stringr"), null, null, null, null, null);

        when(interviewRepository.findById(eq("string"))).thenReturn(Optional.empty());

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode(), timeslotsServiceImpl.addTimeslot("string", timeslot).getStatusCode());
        assertEquals(errors.get(0).getMessage(), timeslotsServiceImpl.addTimeslot("string", timeslot).getBody().getErrors().iterator().next().getMessage());
        verify(interviewRepository, times(2)).findById("string");
    }

    @Test
    void addTimeslotInternalError() {
        Error error = new Error("500", "UNEXPECTED_ERROR");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        Timeslot timeslot = new Timeslot(new InterviewType("stringr"), null, null, null, null, null);

        when(interviewRepository.findById(eq("ad"))).thenReturn(Optional.of(new Interview(null, null, null, null, null, null, null, null, null)));

        assertEquals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR).getStatusCode(), timeslotsServiceImpl.addTimeslot("ad", timeslot).getStatusCode());
        assertEquals(errors.get(0).getMessage(), timeslotsServiceImpl.addTimeslot("ad", timeslot).getBody().getErrors().iterator().next().getMessage());
        verify(interviewRepository, times(2)).findById("ad");
    }

    @Test
    void addTimeslotSizeExceeded() {
        Error error = new Error("500", "TIMESLOTS_SIZE_EXCEEDED");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        List<Interviewer> interviewers = new ArrayList<>(50);
        interviewers.add(new Interviewer("str", AttendeeStatusValue.pending));
        List<Timeslot> timeslots = new ArrayList<>(50);
        Timeslot timeslot = new Timeslot(new InterviewType("stringr"), null, null, null, null, interviewers);
        timeslot.setId("generatedId");
        timeslots.add(timeslot);
        Interview interview = new Interview(new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, timeslots, null, null, null);
        for (int i = 1; i <= 60; i++)
            timeslots.add(timeslot);
        interview.setTimeslots(timeslots);

        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));

        assertEquals(new ResponseEntity<>(HttpStatus.CONFLICT).getStatusCode(), timeslotsServiceImpl.addTimeslot(interview.getId(), timeslot).getStatusCode());
        assertEquals(errors.get(0).getMessage(), timeslotsServiceImpl.addTimeslot(interview.getId(), timeslot).getBody().getErrors().iterator().next().getMessage());
        verify(interviewRepository, times(2)).findById(interview.getId());
    }


    @Test
    void deleteTimeslotSuccessWhenDeleteJustTimeslot() {
        Error error = new Error("500", "CANNOT_DELETE_LAST_TIMESLOT");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        List<Interviewer> interviewers = new ArrayList<>(50);
        interviewers.add(new Interviewer("str", AttendeeStatusValue.pending));
        List<Timeslot> timeslots = new ArrayList<>(50);
        InterviewType interviewType =new InterviewType("stringr");
        Timeslot timeslot = new Timeslot(interviewType, null, null, null, null, interviewers);
        timeslot.setId("generatedId");
        timeslots.add(timeslot);
        Interview interview = new Interview(new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, timeslots, null, null, null);
        timeslots.add(timeslot);
        interview.setTimeslots(timeslots);

        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));
        when(timeslotRepository.findTimeslotByInterviewType(interviewType)).thenReturn(timeslots);
        when(timeslotRepository.findTimeslotByInterviewers(interviewers.get(0))).thenReturn(timeslots);

        assertEquals(new ResponseEntity<>(HttpStatus.NO_CONTENT).getStatusCode(), timeslotsServiceImpl.deleteTimeslot(interview.getId(), timeslot.getId()).getStatusCode());
        verify(timeslotRepository, times(1)).delete(timeslot);
        verify(interviewRepository, times(1)).save(interview);
    }

    @Test
    void deleteTimeslotSuccessWhenDeleteAll() {
        Error error = new Error("500", "CANNOT_DELETE_LAST_TIMESLOT");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        List<Interviewer> interviewers = new ArrayList<>(50);
        interviewers.add(new Interviewer("str", AttendeeStatusValue.pending));
        List<Timeslot> timeslots = new ArrayList<>(50);
        InterviewType interviewType =new InterviewType("stringr");
        Timeslot timeslot = new Timeslot(interviewType, null, null, null, null, interviewers);
        timeslot.setId("generatedId");
        timeslots.add(timeslot);
        Interview interview = new Interview(new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, timeslots, null, null, null);
        timeslots.add(timeslot);
        interview.setTimeslots(timeslots);

        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));
        when(timeslotRepository.findTimeslotByInterviewType(interviewType)).thenReturn(Collections.emptyList());
        when(timeslotRepository.findTimeslotByInterviewers(interviewers.get(0))).thenReturn(Collections.emptyList());

        assertEquals(new ResponseEntity<>(HttpStatus.NO_CONTENT).getStatusCode(), timeslotsServiceImpl.deleteTimeslot(interview.getId(), timeslot.getId()).getStatusCode());

        verify(timeslotRepository, times(1)).delete(timeslot);
        verify(interviewRepository, times(1)).save(interview);
        verify(interviewTypeRepository,times(1)).delete(interviewType);
        verify(interviewerRepository,times(1)).delete(interviewers.get(0));
    }

    @Test
    void deleteTimeslotInterviewNotFound() {
        Error error = new Error("500", "INTERVIEW_NOT_FOUND");
        List<Error> errors = new ArrayList<>();
        errors.add(error);

        when(interviewRepository.findById(eq("string"))).thenReturn(Optional.empty());

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode(), timeslotsServiceImpl.deleteTimeslot("string", "id").getStatusCode());
        assertEquals(errors.get(0).getMessage(), timeslotsServiceImpl.deleteTimeslot("string", "id").getBody().getErrors().iterator().next().getMessage());
        verify(interviewRepository, times(2)).findById("string");
    }

    @Test
    void deleteTimeslotNotFound() {
        Error error = new Error("500", "TIMESLOT_NOT_FOUND");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        List<Interviewer> interviewers = new ArrayList<>(50);
        interviewers.add(new Interviewer("str", AttendeeStatusValue.pending));
        List<Timeslot> timeslots = new ArrayList<>(50);
        Timeslot timeslot = new Timeslot(new InterviewType("stringr"), null, null, null, null, interviewers);
        timeslot.setId("generatedId");
        timeslots.add(timeslot);
        Interview interview = new Interview(new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, timeslots, null, null, null);
        timeslots.add(timeslot);
        interview.setTimeslots(timeslots);

        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode(), timeslotsServiceImpl.deleteTimeslot(interview.getId(), "id").getStatusCode());
        assertEquals(errors.get(0).getMessage(), timeslotsServiceImpl.deleteTimeslot(interview.getId(), "id").getBody().getErrors().iterator().next().getMessage());
        verify(interviewRepository, times(2)).findById(interview.getId());
    }

    @Test
    void deleteTimeslotInternalError() {
        Error error = new Error("500", "UNEXPECTED_ERROR");
        List<Error> errors = new ArrayList<>();
        errors.add(error);

        when(interviewRepository.findById(eq("ad"))).thenReturn(Optional.of(new Interview(null, null, null, null, null, null, null, null, null)));

        assertEquals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR).getStatusCode(), timeslotsServiceImpl.deleteTimeslot("ad", "id").getStatusCode());
        assertEquals(errors.get(0).getMessage(), timeslotsServiceImpl.deleteTimeslot("ad", "id").getBody().getErrors().iterator().next().getMessage());
        verify(interviewRepository, times(2)).findById("ad");
    }

    @Test
    void deleteTimeslotLastTimeslotConflict() {
        Error error = new Error("500", "CANNOT_DELETE_LAST_TIMESLOT");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        List<Interviewer> interviewers = new ArrayList<>(50);
        interviewers.add(new Interviewer("str", AttendeeStatusValue.pending));
        List<Timeslot> timeslots = new ArrayList<>(50);
        Timeslot timeslot = new Timeslot(new InterviewType("stringr"), null, null, null, null, interviewers);
        timeslot.setId("generatedId");
        timeslots.add(timeslot);
        Interview interview = new Interview(new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, timeslots, null, null, null);
        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));

        assertEquals(new ResponseEntity<>(HttpStatus.CONFLICT).getStatusCode(), timeslotsServiceImpl.deleteTimeslot(interview.getId(), timeslot.getId()).getStatusCode());
        assertEquals(errors.get(0).getMessage(), timeslotsServiceImpl.deleteTimeslot(interview.getId(), timeslot.getId()).getBody().getErrors().iterator().next().getMessage());
        verify(interviewRepository, times(2)).findById(interview.getId());
    }

    @Test
    void getTimeslotSuccess() {
        List<Interviewer> interviewers = new ArrayList<>(50);
        interviewers.add(new Interviewer("str", AttendeeStatusValue.pending));
        List<Timeslot> timeslots = new ArrayList<>(50);
        Timeslot timeslot = new Timeslot(new InterviewType("stringr"), null, null, null, null, interviewers);
        timeslot.setId("generatedId");
        timeslots.add(timeslot);
        Interview interview = new Interview(new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, timeslots, null, null, null);
        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));

        assertEquals(new ResponseEntity<>(HttpStatus.OK).getStatusCode(), timeslotsServiceImpl.getTimeslot(interview.getId(), timeslot.getId()).getStatusCode());
        verify(interviewRepository, times(1)).findById(interview.getId());
    }

    @Test
    void getTimeslotInterviewNotFound() {
        Error error = new Error("500", "INTERVIEW_NOT_FOUND");
        List<Error> errors = new ArrayList<>();
        errors.add(error);

        when(interviewRepository.findById(eq("string"))).thenReturn(Optional.empty());

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode(), timeslotsServiceImpl.getTimeslot("string", "id").getStatusCode());
        assertEquals(errors.get(0).getMessage(), timeslotsServiceImpl.getTimeslot("string", "id").getBody().getErrors().iterator().next().getMessage());
        verify(interviewRepository, times(2)).findById("string");
    }

    @Test
    void getTimeslotInternalError() {
        Error error = new Error("500", "UNEXPECTED_ERROR");
        List<Error> errors = new ArrayList<>();
        errors.add(error);

        when(interviewRepository.findById(eq("ad"))).thenReturn(Optional.of(new Interview(null, null, null, null, null, null, null, null, null)));

        assertEquals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR).getStatusCode(), timeslotsServiceImpl.getTimeslot("ad", "id").getStatusCode());
        assertEquals(errors.get(0).getMessage(), timeslotsServiceImpl.getTimeslot("ad", "id").getBody().getErrors().iterator().next().getMessage());
        verify(interviewRepository, times(2)).findById("ad");
    }

    @Test
    void getTimeslotNotFound() {
        Error error = new Error("500", "TIMESLOT_NOT_FOUND");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        List<Interviewer> interviewers = new ArrayList<>(50);
        interviewers.add(new Interviewer("str", AttendeeStatusValue.pending));
        List<Timeslot> timeslots = new ArrayList<>(50);
        Timeslot timeslot = new Timeslot(new InterviewType("stringr"), null, null, null, null, interviewers);
        timeslot.setId("generatedId");
        timeslots.add(timeslot);
        Interview interview = new Interview(new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, timeslots, null, null, null);

        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode(), timeslotsServiceImpl.getTimeslot(interview.getId(), "id").getStatusCode());
        assertEquals(errors.get(0).getMessage(), timeslotsServiceImpl.getTimeslot(interview.getId(), "id").getBody().getErrors().iterator().next().getMessage());
        verify(interviewRepository, times(2)).findById(interview.getId());
    }

    @Test
    void updateTimeslotSuccessExistingInterviewType() {
        List<Interviewer> interviewers = new ArrayList<>(50);
        interviewers.add(new Interviewer("str", AttendeeStatusValue.pending));
        List<Timeslot> timeslots = new ArrayList<>(50);
        InterviewType interviewType = new InterviewType("stringr");
        Timeslot timeslot = new Timeslot(interviewType, null, null, "20-02-2022", "20-02-2022", interviewers);
        timeslot.setId("generatedId");
        timeslots.add(timeslot);
        Interview interview = new Interview(new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, timeslots, null, null, null);
        interview.setId("generatedId");

        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));
        when(interviewerRepository.findById(interviewers.get(0).getId())).thenReturn(Optional.of(interviewers.get(0)));
        when(interviewTypeRepository.findByName(eq("stringr"))).thenReturn(interviewType);

        assertEquals(new ResponseEntity<>(HttpStatus.NO_CONTENT).getStatusCode(), timeslotsServiceImpl.updateTimeslot(interview.getId(), timeslot.getId(), timeslot).getStatusCode());
        verify(interviewRepository, times(1)).save(interview);
        verify(interviewerRepository, times(1)).saveAll(interviewers);
        verify(timeslotRepository, times(1)).save(timeslot);
    }

    @Test
    void updateTimeslotSuccessNonExistingInterviewType() {
        List<Interviewer> interviewers = new ArrayList<>(50);
        interviewers.add(new Interviewer("str", AttendeeStatusValue.pending));
        List<Timeslot> timeslots = new ArrayList<>(50);
        InterviewType interviewType = new InterviewType("stringr");
        Timeslot timeslot = new Timeslot(interviewType, null, null, "20-02-2022", "20-02-2022", interviewers);
        timeslot.setId("generatedId");
        timeslots.add(timeslot);
        Interview interview = new Interview(new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, timeslots, null, null, null);
        interview.setId("generatedId");

        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));
        when(interviewerRepository.findById(interviewers.get(0).getId())).thenReturn(Optional.of(interviewers.get(0)));
        when(interviewTypeRepository.findByName(eq("stringr"))).thenReturn(null);

        assertEquals(new ResponseEntity<>(HttpStatus.NO_CONTENT).getStatusCode(), timeslotsServiceImpl.updateTimeslot(interview.getId(), timeslot.getId(), timeslot).getStatusCode());
        verify(interviewRepository, times(1)).save(interview);
        verify(interviewerRepository, times(1)).saveAll(interviewers);
        verify(timeslotRepository, times(1)).save(timeslot);
        verify(interviewTypeRepository, times(1)).save(timeslot.getInterviewType());
    }

    @Test
    void updateTimeslotInterviewNotFound() {
        Error error = new Error("500", "INTERVIEW_NOT_FOUND");
        List<Error> errors = new ArrayList<>();
        errors.add(error);

        when(interviewRepository.findById("id")).thenReturn(Optional.empty());

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode(), timeslotsServiceImpl.updateTimeslot("id", "id", new Timeslot()).getStatusCode());
        assertEquals(errors.get(0).getMessage(), timeslotsServiceImpl.updateTimeslot("id", "id", new Timeslot()).getBody().getErrors().iterator().next().getMessage());
        verify(interviewRepository, times(2)).findById("id");
    }

    @Test
    void updateTimeslotInternalError() {
        Error error = new Error("500", "UNEXPECTED_ERROR");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        
        when(interviewRepository.findById("id")).thenThrow(new IndexOutOfBoundsException());

        assertEquals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR).getStatusCode(), timeslotsServiceImpl.updateTimeslot("id", "id", new Timeslot()).getStatusCode());
        assertEquals(errors.get(0).getMessage(), timeslotsServiceImpl.updateTimeslot("id", "id", new Timeslot()).getBody().getErrors().iterator().next().getMessage());
        verify(interviewRepository, times(2)).findById("id");
    }

    @Test
    void updateTimeslotEndDateBeforeStartDate() {
        Error error = new Error("500", "END_DATE_BEFORE_START_DATE");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        List<Interviewer> interviewers = new ArrayList<>(50);
        interviewers.add(new Interviewer("str", AttendeeStatusValue.pending));
        List<Timeslot> timeslots = new ArrayList<>(50);
        InterviewType interviewType = new InterviewType("stringr");
        Timeslot timeslot = new Timeslot(interviewType, null, null, "20-03-2022", "20-02-2022", interviewers);
        timeslot.setId("generatedId");
        timeslots.add(timeslot);
        Interview interview = new Interview(new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, timeslots, null, null, null);
        interview.setId("generatedId");

        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));

        assertEquals(new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY).getStatusCode(), timeslotsServiceImpl.updateTimeslot(interview.getId(), timeslot.getId(), timeslot).getStatusCode());
        assertEquals(errors.get(0).getMessage(), timeslotsServiceImpl.updateTimeslot(interview.getId(), timeslot.getId(), timeslot).getBody().getErrors().iterator().next().getMessage());
        verify(interviewRepository, times(2)).findById(interview.getId());
    }

    @Test
    void updateTimeslotNonExistingInterviewers() {
        Error error = new Error("500", "TIMESLOT_NOT_FOUND");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        List<Interviewer> interviewers = new ArrayList<>(50);
        interviewers.add(new Interviewer("str", AttendeeStatusValue.pending));
        List<Timeslot> timeslots = new ArrayList<>(50);
        InterviewType interviewType = new InterviewType("stringr");
        Timeslot timeslot = new Timeslot(interviewType, null, null, "20-02-2022", "20-02-2022", interviewers);
        timeslot.setId("generatedId");
        timeslots.add(timeslot);
        Interview interview = new Interview(new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, timeslots, null, null, null);
        interview.setId("generatedId");

        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));
        when(interviewerRepository.findById(interviewers.get(0).getId())).thenReturn(Optional.empty());

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode(), timeslotsServiceImpl.updateTimeslot(interview.getId(), timeslot.getId(), timeslot).getStatusCode());
        assertEquals(errors.get(0).getMessage(), timeslotsServiceImpl.updateTimeslot(interview.getId(), timeslot.getId(), timeslot).getBody().getErrors().iterator().next().getMessage());
        verify(interviewRepository, times(2)).findById(interview.getId());
    }

    @Test
    void updateTimeslotNotFound() {
        Error error = new Error("500", "TIMESLOT_NOT_FOUND");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        List<Interviewer> interviewers = new ArrayList<>(50);
        interviewers.add(new Interviewer("str", AttendeeStatusValue.pending));
        List<Timeslot> timeslots = new ArrayList<>(50);
        InterviewType interviewType = new InterviewType("stringr");
        Timeslot timeslot = new Timeslot(interviewType, null, null, "20-02-2022", "20-02-2022", interviewers);
        timeslot.setId("generatedId");
        timeslots.add(timeslot);
        Interview interview = new Interview(new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, timeslots, null, null, null);
        interview.setId("generatedId");

        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));


        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode(), timeslotsServiceImpl.updateTimeslot(interview.getId(), "id", timeslot).getStatusCode());
        assertEquals(errors.get(0).getMessage(), timeslotsServiceImpl.updateTimeslot(interview.getId(), "id", timeslot).getBody().getErrors().iterator().next().getMessage());
        verify(interviewRepository, times(2)).findById(interview.getId());
    }
}