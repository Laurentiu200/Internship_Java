package com.example.internship_java.service;

import com.example.internship_java.model.*;
import com.example.internship_java.model.Error;
import com.example.internship_java.repository.CandidateRepository;
import com.example.internship_java.repository.InterviewRepository;
import com.example.internship_java.repository.InterviewerRepository;
import com.example.internship_java.repository.TimeslotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatusServiceImplTest {
    @InjectMocks
    StatusServiceImpl statusServiceImpl;

    @Mock
    InterviewRepository interviewRepository;
    @Mock
    CandidateRepository candidateRepository;
    @Mock
    InterviewerRepository interviewerRepository;

    @Test
    void updateCandidateStatusSuccess() {
        Interview interview = new Interview(new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, null, null, null, null);
        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));
        when(candidateRepository.findById(eq("string"))).thenReturn(Optional.of(new Candidate("string", AttendeeStatusValue.pending)));

        assertEquals(new ResponseEntity<>(HttpStatus.NO_CONTENT).getStatusCode(), statusServiceImpl.updateCandidateStatus(interview.getId(), "{\n" +
                "  \"status\": \"accepted\"\n" +
                "}").getStatusCode());
        verify(interviewRepository, times(1)).findById(interview.getId());
        verify(candidateRepository, times(1)).findById("string");
    }

    @Test
    void updateCandidateStatusBadInput() {
        Error error = new Error("500", "BAD_INPUT");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        assertEquals(new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY).getStatusCode(), statusServiceImpl.updateCandidateStatus("id", "string").getStatusCode());
        assertEquals(errors.get(0).getMessage(), statusServiceImpl.updateCandidateStatus("id", "string").getBody().getErrors().iterator().next().getMessage());
    }

    @Test
    void updateCandidateStatusInterviewNotFound() {
        Error error = new Error("500", "INTERVIEW_NOT_FOUND");
        List<Error> errors = new ArrayList<>();
        errors.add(error);

        when(interviewRepository.findById(eq("string"))).thenReturn(Optional.empty());

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode(), statusServiceImpl.updateCandidateStatus("string", "{\n" +
                "  \"status\": \"accepted\"\n" +
                "}").getStatusCode());
        assertEquals(errors.get(0).getMessage(), statusServiceImpl.updateCandidateStatus("string", "{\n" +
                "  \"status\": \"accepted\"\n" +
                "}").getBody().getErrors().iterator().next().getMessage());
        verify(interviewRepository, times(2)).findById("string");
    }

    @Test
    void updateCandidateStatusCandidateNotFound() {
        Error error = new Error("500", "CANDIDATE_NOT_FOUND");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        Interview interview = new Interview(new Candidate("string3", AttendeeStatusValue.pending), null, null, null, null, null, null, null, null);

        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));
        when(candidateRepository.findById(eq("string3"))).thenReturn(Optional.empty());

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode(), statusServiceImpl.updateCandidateStatus(interview.getId(), "{\n" +
                "  \"status\": \"accepted\"\n" +
                "}").getStatusCode());
        assertEquals(errors.get(0).getMessage(), statusServiceImpl.updateCandidateStatus(interview.getId(), "{\n" +
                "  \"status\": \"accepted\"\n" +
                "}").getBody().getErrors().iterator().next().getMessage());

        verify(interviewRepository, times(2)).findById(interview.getId());
        verify(candidateRepository, times(2)).findById("string3");
    }

    @Test
    void updateCandidateStatusInternalError() {
        Error error = new Error("500", "UNEXPECTED_ERROR");
        List<Error> errors = new ArrayList<>();
        errors.add(error);

        when(interviewRepository.findById(eq("ad"))).thenReturn(Optional.of(new Interview()));

        assertEquals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR).getStatusCode(), statusServiceImpl.updateCandidateStatus("ad", "{\n" +
                "  \"status\": \"accepted\"\n" +
                "}").getStatusCode());
        assertEquals(errors.get(0).getMessage(), statusServiceImpl.updateCandidateStatus("ad", "{\n" +
                "  \"status\": \"accepted\"\n" +
                "}").getBody().getErrors().iterator().next().getMessage());

        verify(interviewRepository, times(2)).findById("ad");
    }

    @Test
    void updateInterviewerStatusSuccess() {
        List<Interviewer> interviewers = new ArrayList<>(50);
        interviewers.add(new Interviewer("str", AttendeeStatusValue.pending));
        List<Timeslot> timeslots = new ArrayList<>(50);
        Timeslot timeslot = new Timeslot(null, null, null, null, null, interviewers);
        timeslot.setId("generatedId");
        timeslots.add(timeslot);

        Interview interview = new Interview(new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, timeslots, null, null, null);
        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));
        assertEquals(new ResponseEntity<>(HttpStatus.NO_CONTENT).getStatusCode(), statusServiceImpl.updateInterviewerStatus(interview.getId(), timeslot.getId(), "str", "{\n" +
                "  \"status\": \"accepted\"\n" +
                "}").getStatusCode());

        verify(interviewRepository, times(1)).findById(interview.getId());
        verify(interviewerRepository, times(1)).save(timeslots.get(0).getInterviewers().get(0));
    }

    @Test
    void updateInterviewerStatusInterviewNotFound() {
        Error error = new Error("500", "INTERVIEW_NOT_FOUND");
        List<Error> errors = new ArrayList<>();
        errors.add(error);

        when(interviewRepository.findById(eq("string"))).thenReturn(Optional.empty());

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode(), statusServiceImpl.updateInterviewerStatus("string", "id", "str", "{\n" +
                "  \"status\": \"accepted\"\n" +
                "}").getStatusCode());
        assertEquals(errors.get(0).getMessage(), statusServiceImpl.updateInterviewerStatus("string", "id", "str", "{\n" +
                "  \"status\": \"accepted\"\n" +
                "}").getBody().getErrors().iterator().next().getMessage());
        verify(interviewRepository, times(2)).findById("string");
    }

    @Test
    void updateInterviewerStatusInternalError() {
        Error error = new Error("500", "UNEXPECTED_ERROR");
        List<Error> errors = new ArrayList<>();
        errors.add(error);

        when(interviewRepository.findById(eq("ad"))).thenReturn(Optional.of(new Interview(new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, null, null, null, null)));

        assertEquals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR).getStatusCode(), statusServiceImpl.updateInterviewerStatus("ad", "id", "str", "{\n" +
                "  \"status\": \"accepted\"\n" +
                "}").getStatusCode());
        assertEquals(errors.get(0).getMessage(), statusServiceImpl.updateInterviewerStatus("ad", "id", "str", "{\n" +
                "  \"status\": \"accepted\"\n" +
                "}").getBody().getErrors().iterator().next().getMessage());
        verify(interviewRepository, times(2)).findById("ad");
    }

    @Test
    void updateInterviewerStatusBadInput() {
        Error error = new Error("500", "BAD_INPUT");
        List<Error> errors = new ArrayList<>();
        errors.add(error);

        assertEquals(new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY).getStatusCode(), statusServiceImpl.updateInterviewerStatus("string", "id", "str", "string").getStatusCode());
        assertEquals(errors.get(0).getMessage(), statusServiceImpl.updateInterviewerStatus("string", "id", "str", "string").getBody().getErrors().iterator().next().getMessage());

    }

    @Test
    void updateInterviewerStatusTimeslotNotFound() {
        Error error = new Error("500", "TIMESLOT_NOT_FOUND");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        List<Interviewer> interviewers = new ArrayList<>(50);
        interviewers.add(new Interviewer("str", AttendeeStatusValue.pending));
        List<Timeslot> timeslots = new ArrayList<>(50);
        Timeslot timeslot = new Timeslot(null, null, null, null, null, interviewers);
        timeslot.setId("generatedId");
        timeslots.add(timeslot);
        Interview interview = new Interview(new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, timeslots, null, null, null);

        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode(), statusServiceImpl.updateInterviewerStatus(interview.getId(), "id", "str", "{\n" +
                "  \"status\": \"accepted\"\n" +
                "}").getStatusCode());
        assertEquals(errors.get(0).getMessage(), statusServiceImpl.updateInterviewerStatus(interview.getId(), "id", "str", "{\n" +
                "  \"status\": \"accepted\"\n" +
                "}").getBody().getErrors().iterator().next().getMessage());

        verify(interviewRepository, times(2)).findById(interview.getId());
    }

    @Test
    void updateInterviewerStatusNonExistingInterviewers() {
        Error error = new Error("500", "NON_EXISTING_INTERVIEWERS");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        List<Interviewer> interviewers = new ArrayList<>(50);
        interviewers.add(new Interviewer("str", AttendeeStatusValue.pending));
        List<Timeslot> timeslots = new ArrayList<>(50);
        Timeslot timeslot = new Timeslot(null, null, null, null, null, interviewers);
        timeslot.setId("generatedId");
        timeslots.add(timeslot);
        Interview interview = new Interview(new Candidate("string", AttendeeStatusValue.pending), null, null, null, null, timeslots, null, null, null);

        when(interviewRepository.findById(eq(interview.getId()))).thenReturn(Optional.of(interview));

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND).getStatusCode(), statusServiceImpl.updateInterviewerStatus(interview.getId(), timeslot.getId(), "bad", "{\n" +
                "  \"status\": \"accepted\"\n" +
                "}").getStatusCode());
        assertEquals(errors.get(0).getMessage(), statusServiceImpl.updateInterviewerStatus(interview.getId(), timeslot.getId(), "bad", "{\n" +
                "  \"status\": \"accepted\"\n" +
                "}").getBody().getErrors().iterator().next().getMessage());

        verify(interviewRepository, times(2)).findById(interview.getId());
    }
}