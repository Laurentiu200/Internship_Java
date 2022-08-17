package com.example.internship_java.service;

import com.example.internship_java.model.*;
import com.example.internship_java.model.Error;
import com.example.internship_java.repository.*;
import com.example.internship_java.response.InterviewResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)


class InterviewServiceImplTest {

    @InjectMocks
    InterviewServiceImpl interviewServiceImpl;

    @Mock
    InterviewServiceImpl MockInterviewServiceImpl;

    @Mock
    InterviewRepository interviewRepository;

    @Mock
    TimeslotRepository timeslotRepository;

    @Mock
    InterviewerRepository interviewerRepository;

    @Mock
    CandidateRepository candidateRepository;

    @Mock
    Interview interview;

    @Mock
    InterviewTypeRepository interviewTypeRepository;




    @Test
    void validTimeZone() {

        assertTrue(interviewServiceImpl.validTimeZone("GMT"));
        assertFalse(interviewServiceImpl.validTimeZone("Invalid"));
    }

    @Test
    void putInterview() {
        Candidate candidate = new Candidate("id1", AttendeeStatusValue.pending);

        Interviewer interviewer = new Interviewer("ID1", AttendeeStatusValue.tentative);

        List<Interviewer> interviewers = new ArrayList<>();
        interviewers.add(interviewer);

        InterviewType interviewType = new InterviewType("Formal");

        Timeslot timeslot = new Timeslot(interviewType,"Electrician", "Timisoara", "2022-08-10T10:26:34.759Z","2022-08-10T10:26:34.759Z", interviewers);

        List<Timeslot> timeslots =new ArrayList<>();
        timeslots.add(timeslot);


        Interview interview = new Interview(candidate,"jobId","Timisoara","Cineva",TimeZone.getTimeZone("GMT"),timeslots,"2022-08-10T10:26:34.759Z","string","string");

        when(interviewRepository.save(interview)).thenReturn(interview);
        when(interviewTypeRepository.findByName("Formal")).thenReturn(interviewType);
        Collection<Interview> interviews = new ArrayList<>();
        Interview resultInterview = interviewRepository.save(interview);
        interviews.add(resultInterview);

        ResponseEntity<Collection<Interview>> expected = new ResponseEntity<>(interviews,HttpStatus.CREATED);
        ResponseEntity<InterviewResponse> actual = interviewServiceImpl.putInterview(interview);
        assertEquals(actual.getStatusCode(),expected.getStatusCode());
        verify(interviewRepository, times(2)).save(interview);


    }



    @Test
    void putInterview_InterviewerNotFound()
    {
        Candidate candidate = new Candidate("id1", AttendeeStatusValue.pending);

        Interviewer interviewer = new Interviewer("ID1", AttendeeStatusValue.tentative);

        List<Interviewer> interviewers = new ArrayList<>();

        InterviewType interviewType = new InterviewType("Formal");

        Timeslot timeslot = new Timeslot(interviewType,"Electrician", "Timisoara", "2022-08-10T10:26:34.759Z","2022-08-10T10:26:34.759Z",interviewers );

        List<Timeslot> timeslots =new ArrayList<>();
        timeslots.add(timeslot);

        Interview interview = new Interview(candidate,"jobId","Timisoara",null,TimeZone.getTimeZone("GMT"),timeslots,"2022-08-10T10:26:34.759Z","string","string");

        Error error = new Error("422", "INTERVIEWER NOT FOUND");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        InterviewResponse interviewResponse  = new InterviewResponse(errors,null);

        ResponseEntity<InterviewResponse> expected = new ResponseEntity<>(interviewResponse,HttpStatus.NOT_ACCEPTABLE);
        ResponseEntity<InterviewResponse> actual = interviewServiceImpl.putInterview(interview);
        assertEquals(actual.getStatusCode(),expected.getStatusCode());
        verify(interviewRepository, times(0)).save(interview);
        verify(interviewerRepository, times(0)).saveAll(interviewers);
        verify(timeslotRepository, times(0)).save(timeslot);
        verify(candidateRepository, times(0)).save(candidate);
    }

    @Test
    void putInterview_StartEnd_Date()
    {
        Candidate candidate = new Candidate("id1", AttendeeStatusValue.pending);

        Interviewer interviewer = new Interviewer("ID1", AttendeeStatusValue.tentative);

        List<Interviewer> interviewers = new ArrayList<>();
        interviewers.add(interviewer);

        InterviewType interviewType = new InterviewType("Formal");

        Timeslot timeslot = new Timeslot(interviewType,"Electrician", "Timisoara", "2022-08-10T10:26:34.759Z","2022-07-10T10:26:34.759Z", interviewers);

        List<Timeslot> timeslots =new ArrayList<>();
        timeslots.add(timeslot);

        Interview interview = new Interview(candidate,"jobId","Timisoara","string",TimeZone.getTimeZone("GMT"),timeslots,"2022-08-10T10:26:34.759Z","string","string");

        Error error = new Error("422", "END_DATE_BEFORE_START_DATE");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        InterviewResponse interviewResponse  = new InterviewResponse(errors,null);

        ResponseEntity<InterviewResponse> expected = new ResponseEntity<>(interviewResponse,HttpStatus.NOT_ACCEPTABLE);
        ResponseEntity<InterviewResponse> actual = interviewServiceImpl.putInterview(interview);
        assertEquals(actual.getStatusCode(),expected.getStatusCode());
        verify(interviewRepository, times(0)).save(interview);
        verify(interviewerRepository, times(0)).saveAll(interviewers);
        verify(timeslotRepository, times(0)).save(timeslot);
        verify(candidateRepository, times(0)).save(candidate);
    }

    @Test
    void putInterviewInterviewersExceeded()
    {
        Candidate candidate = new Candidate("id1", AttendeeStatusValue.pending);

        Interviewer interviewer = new Interviewer("ID1", AttendeeStatusValue.tentative);
        List<Interviewer> interviewers = new ArrayList<>();
        for(int i = 1; i <= 50; i++)
        {
            Interviewer interviewer1 = new Interviewer("id" + 1, AttendeeStatusValue.accepted);
            interviewers.add(interviewer1);
        }

        interviewers.add(interviewer);

        InterviewType interviewType = new InterviewType("Formal");

        Timeslot timeslot = new Timeslot(interviewType,"Electrician", "Timisoara", "2022-08-10T10:26:34.759Z","2022-08-10T10:26:34.759Z", interviewers);

        List<Timeslot> timeslots =new ArrayList<>();
        timeslots.add(timeslot);

        Interview interview = new Interview(candidate,"jobId","Timisoara","string",TimeZone.getTimeZone("GMT"),timeslots,"2022-08-10T10:26:34.759Z","string","string");

        Error error = new Error("422", "INTERVIEWERS SIZE EXCEEDED");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        InterviewResponse interviewResponse  = new InterviewResponse(errors,null);

        ResponseEntity<InterviewResponse> expected = new ResponseEntity<>(interviewResponse,HttpStatus.NOT_ACCEPTABLE);
        ResponseEntity<InterviewResponse> actual = interviewServiceImpl.putInterview(interview);
        assertEquals(actual.getStatusCode(),expected.getStatusCode());
        verify(interviewRepository, times(0)).save(interview);
        verify(interviewerRepository, times(0)).saveAll(interviewers);
        verify(timeslotRepository, times(0)).save(timeslot);
        verify(candidateRepository, times(0)).save(candidate);
    }



    @Test
    void putInterview_OrganizerNotFound() {
        Candidate candidate = new Candidate("id1", AttendeeStatusValue.pending);

        Interviewer interviewer = new Interviewer("ID1", AttendeeStatusValue.tentative);
        List<Interviewer> interviewers = new ArrayList<>();
        interviewers.add(interviewer);

        InterviewType interviewType = new InterviewType("Formal");

        Timeslot timeslot = new Timeslot(interviewType, "Electrician", "Timisoara", "2022-08-10T10:26:34.759Z", "2022-08-10T10:26:34.759Z", interviewers);

        List<Timeslot> timeslots = new ArrayList<>();
        timeslots.add(timeslot);

        Interview interview1 = new Interview(candidate, "jobId", "Timisoara", null, TimeZone.getTimeZone("GMT"), timeslots, "2022-08-10T10:26:34.759Z", "string", "string");

        Error error = new Error("422", "ORGANIZER ID NOT FOUND");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        InterviewResponse interviewResponse = new InterviewResponse(errors, null);

        ResponseEntity<InterviewResponse> expected = new ResponseEntity<>(interviewResponse, HttpStatus.NOT_ACCEPTABLE);
        ResponseEntity<InterviewResponse> actual = interviewServiceImpl.putInterview(interview1);
        assertEquals(expected.getStatusCode(),actual.getStatusCode());
        verify(interviewRepository, times(0)).save(interview);
        verify(interviewerRepository, times(0)).saveAll(interviewers);
        verify(timeslotRepository, times(0)).save(timeslot);
        verify(candidateRepository, times(0)).save(candidate);
    }

    @Test
    void putInterviewInternalServerError() {
        Candidate candidate = new Candidate("id1", AttendeeStatusValue.pending);

        Interviewer interviewer = new Interviewer("ID1", AttendeeStatusValue.tentative);
        List<Interviewer> interviewers = new ArrayList<>();
        interviewers.add(interviewer);

        InterviewType interviewType = new InterviewType("Formal");

        Timeslot timeslot = new Timeslot(interviewType, "Electrician", "Timisoara", "2022-08-10T10:26:34.759Z", "2022-08-10T10:26:34.759Z", null);

        List<Timeslot> timeslots = new ArrayList<>();
        timeslots.add(timeslot);

        Interview interview1 = new Interview(candidate, "jobId", "Timisoara", "string", TimeZone.getTimeZone("GMT"), timeslots, "2022-08-10T10:26:34.759Z", "string", "string");

        Error error = new Error("500", "INTERNAL SERVER ERROR");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        InterviewResponse interviewResponse = new InterviewResponse(errors, null);

        ResponseEntity<InterviewResponse> expected = new ResponseEntity<>(interviewResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<InterviewResponse> actual = interviewServiceImpl.putInterview(interview1);
        assertEquals(expected.getStatusCode(),actual.getStatusCode());
        verify(interviewRepository, times(0)).save(interview);
        verify(interviewerRepository, times(0)).saveAll(interviewers);
        verify(timeslotRepository, times(0)).save(timeslot);
        verify(candidateRepository, times(0)).save(candidate);
    }

    @Test
    void patchInterviewNotFound() {
      String id = "string";
        Candidate candidate = new Candidate("id1", AttendeeStatusValue.pending);

        Interviewer interviewer = new Interviewer("ID1", AttendeeStatusValue.tentative);
        List<Interviewer> interviewers = new ArrayList<>();
        interviewers.add(interviewer);

        InterviewType interviewType = new InterviewType("Formal");

        Timeslot timeslot = new Timeslot(interviewType, "Electrician", "Timisoara", "2022-08-10T10:26:34.759Z", "2022-08-10T10:26:34.759Z", interviewers);

        List<Timeslot> timeslots = new ArrayList<>();
        timeslots.add(timeslot);

        Interview interview1 = new Interview(candidate, "jobId", "Timisoara", "string", TimeZone.getTimeZone("GMT"), timeslots, "2022-08-10T10:26:34.759Z", "string", "string");
        when(interviewRepository.findById(id)).thenReturn(Optional.ofNullable(null));
        Error error = new Error("404", "INTERVIEW NOT FOUND");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        InterviewResponse interviewResponse = new InterviewResponse(errors, null);

        ResponseEntity<InterviewResponse> expected = new ResponseEntity<>(interviewResponse, HttpStatus.NOT_FOUND);
        ResponseEntity<InterviewResponse> actual = interviewServiceImpl.patchInterview(interview1, "string");
        assertEquals(expected.getStatusCode(),actual.getStatusCode());

    }
    @Test
    void patchInterviewException() {
        String id = "string";
        Candidate candidate = new Candidate("id1", AttendeeStatusValue.pending);

        Interviewer interviewer = new Interviewer("ID1", AttendeeStatusValue.tentative);
        List<Interviewer> interviewers = new ArrayList<>();
        interviewers.add(interviewer);

        InterviewType interviewType = new InterviewType("Formal");

        Timeslot timeslot = new Timeslot(interviewType, "Electrician", "Timisoara", "2022-08-10T10:26:34.759Z", "2022-08-10T10:26:34.759Z", null);

        List<Timeslot> timeslots = new ArrayList<>();
        timeslots.add(timeslot);

        Interview interview1 = new Interview(candidate, "jobId", "Timisoara", "string", null, timeslots, "2022-08-10T10:26:34.759Z", "string", "string");
       when(interviewRepository.findById(id)).thenThrow(NullPointerException.class);
        Error error = new Error("500", "UNEXPECTED ERROR");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        InterviewResponse interviewResponse = new InterviewResponse(errors, null);

        ResponseEntity<InterviewResponse> expected = new ResponseEntity<>(interviewResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<InterviewResponse> actual = interviewServiceImpl.patchInterview(interview1, "string");
        assertEquals(expected.getStatusCode(),actual.getStatusCode());
    }

    @Test
    void patchInterviewInvalidTimezone() {
        String id = "string";
        Candidate candidate = new Candidate("id1", AttendeeStatusValue.pending);

        Interviewer interviewer = new Interviewer("ID1", AttendeeStatusValue.tentative);
        List<Interviewer> interviewers = new ArrayList<>();
        interviewers.add(interviewer);

        InterviewType interviewType = new InterviewType("Formal");

        Timeslot timeslot = new Timeslot(interviewType, "Electrician", "Timisoara", "2022-08-10T10:26:34.759Z", "2022-08-10T10:26:34.759Z", null);

        List<Timeslot> timeslots = new ArrayList<>();
        timeslots.add(timeslot);

        Interview interview1 = new Interview(candidate, "jobId", "Timisoara", "string", null, timeslots, "2022-08-10T10:26:34.759Z", "string", "string");
        when(interviewRepository.findById(id)).thenReturn(Optional.of(interview1));
        Error error = new Error("409", "UNEXPECTED ERROR");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        InterviewResponse interviewResponse = new InterviewResponse(errors, null);

        ResponseEntity<InterviewResponse> expected = new ResponseEntity<>(interviewResponse, HttpStatus.CONFLICT);
        ResponseEntity<InterviewResponse> actual = interviewServiceImpl.patchInterview(interview1, "string");

        assertEquals(expected.getStatusCode(),actual.getStatusCode());

    }

    @Test
    void getAllInterviewers() {
        Candidate candidate = new Candidate("id1", AttendeeStatusValue.pending);

        Interviewer interviewer = new Interviewer("ID1", AttendeeStatusValue.tentative);

        List<Interviewer> interviewers = new ArrayList<>();
        interviewers.add(interviewer);

        InterviewType interviewType = new InterviewType("Formal");

        Timeslot timeslot = new Timeslot(interviewType,"Electrician", "Timisoara", "2022-08-10T10:26:34.759Z","2022-08-10T10:26:34.759Z", interviewers);

        List<Timeslot> timeslots =new ArrayList<>();
        timeslots.add(timeslot);


        Interview interview = new Interview(candidate,"jobId","Timisoara","Cineva",TimeZone.getTimeZone("GMT"),timeslots,"2022-08-10T10:26:34.759Z","string","string");
        Interview interview1 = new Interview(candidate,"jobId","Timisoara","Cineva",TimeZone.getTimeZone("UTC"),timeslots,"2022-08-10T10:26:34.759Z","string","string");

        List<Interview> interviews = new ArrayList<>();
        interviews.add(interview);
        interviews.add(interview1);
        when(interviewRepository.findAll()).thenReturn(interviews);


        InterviewResponse interviewResponse  = new InterviewResponse(null, interviews);
        ResponseEntity<InterviewResponse> expectedResponse = new ResponseEntity<>(interviewResponse, HttpStatus.OK);
        ResponseEntity<InterviewResponse> actualResponse = interviewServiceImpl.getAllInterviewers();

        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
        verify(interviewRepository, times(2)).findAll();
    }

    @Test
    void patchInterview() {
        String id = "string";
        Candidate candidate = new Candidate("id1", AttendeeStatusValue.pending);

        Interviewer interviewer = new Interviewer("ID1", AttendeeStatusValue.tentative);
        List<Interviewer> interviewers = new ArrayList<>();
        interviewers.add(interviewer);

        InterviewType interviewType = new InterviewType("Formal");

        Timeslot timeslot = new Timeslot(interviewType, "Electrician", "Timisoara", "2022-08-10T10:26:34.759Z", "2022-08-10T10:26:34.759Z", interviewers);

        List<Timeslot> timeslots = new ArrayList<>();
        timeslots.add(timeslot);

        Interview interview1 = new Interview(candidate, "jobId", "Timisoara", "string", TimeZone.getTimeZone("GMT"), timeslots, "2022-08-10T10:26:34.759Z", "string", "string");
        when(interviewRepository.findById(id)).thenReturn(Optional.ofNullable(interview1));
        Error error = new Error("204", "INTERVIEW UPDATED");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        InterviewResponse interviewResponse = new InterviewResponse(errors, null);

        ResponseEntity<InterviewResponse> expected = new ResponseEntity<>(interviewResponse, HttpStatus.CREATED);
        ResponseEntity<InterviewResponse> actual = interviewServiceImpl.patchInterview(interview1, "string");
        assertEquals(expected.getStatusCode(),actual.getStatusCode());

    }

    @Test
    void getAllInterviewersError() {

        Error error = new Error("500", "Unexpected error");
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        when(interviewRepository.findAll()).thenThrow(NullPointerException.class);
        InterviewResponse interviewResponse = new InterviewResponse(errors,null);
        ResponseEntity<InterviewResponse> actualResponse = interviewServiceImpl.getAllInterviewers();
        ResponseEntity<InterviewResponse> expectedResponse = new ResponseEntity<>(interviewResponse,HttpStatus.INTERNAL_SERVER_ERROR);

        assertEquals(actualResponse.getStatusCode(), expectedResponse.getStatusCode());
    }

    @Test
    void getInterview() {
        Candidate candidate = new Candidate("id1", AttendeeStatusValue.pending);

        Interviewer interviewer = new Interviewer("ID1", AttendeeStatusValue.tentative);

        List<Interviewer> interviewers = new ArrayList<>();
        interviewers.add(interviewer);

        InterviewType interviewType = new InterviewType("Formal");

        Timeslot timeslot = new Timeslot(interviewType,"Electrician", "Timisoara", "2022-08-10T10:26:34.759Z","2022-08-10T10:26:34.759Z", interviewers);

        List<Timeslot> timeslots =new ArrayList<>();
        timeslots.add(timeslot);

        Interview interview = new Interview(candidate,"jobId","Timisoara","Cineva",TimeZone.getTimeZone("GMT"),timeslots,"2022-08-10T10:26:34.759Z","string","string");
        String interview_id = interview.getId();
        List<Interview> interviews = new ArrayList<>();
        interviews.add(interview);
        when(interviewRepository.findById(interview_id)).thenReturn(Optional.of(interview));

        InterviewResponse interviewResponse  = new InterviewResponse(null, interviews);
        ResponseEntity<InterviewResponse> expectedResponse = new ResponseEntity<>(interviewResponse, HttpStatus.FOUND);
        ResponseEntity<InterviewResponse> actualResponse = interviewServiceImpl.getInterview(interview_id);

        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
    }

    @Test
    void getInterviewNotFound() {
        Candidate candidate = new Candidate("id1", AttendeeStatusValue.pending);

        Interviewer interviewer = new Interviewer("ID1", AttendeeStatusValue.tentative);

        List<Interviewer> interviewers = new ArrayList<>();
        interviewers.add(interviewer);

        InterviewType interviewType = new InterviewType("Formal");

        Timeslot timeslot = new Timeslot(interviewType,"Electrician", "Timisoara", "2022-08-10T10:26:34.759Z","2022-08-10T10:26:34.759Z", interviewers);

        List<Timeslot> timeslots =new ArrayList<>();
        timeslots.add(timeslot);

        Interview interview = new Interview(candidate,"jobId","Timisoara","Cineva",TimeZone.getTimeZone("GMT"),timeslots,"2022-08-10T10:26:34.759Z","string","string");
        List<Interview> interviews = new ArrayList<>();
        interviews.add(interview);
        Interview interview1 = null;
        when(interviewRepository.findById("ceva")).thenReturn(Optional.ofNullable(interview1));

        Error error = new Error("404", "INTERVIEW NOT FOUND");
        List<Error>  errors  = new ArrayList<>();
        errors.add(error);

        InterviewResponse interviewResponse = new InterviewResponse(errors,null);
        ResponseEntity<InterviewResponse> expectedResponse = new ResponseEntity<>(interviewResponse,HttpStatus.NOT_FOUND);
        ResponseEntity<InterviewResponse> actualResponse = interviewServiceImpl.getInterview("ceva");

        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());

    }

    @Test
    void GetInterviewUnexpectedError()
    {
        String id = "";
        when(interviewRepository.findById(id)).thenThrow(NullPointerException.class);
        Error error = new Error("500", "UNEXPECTED ERROR");
        List<Error>  errors  = new ArrayList<>();
        errors.add(error);

        InterviewResponse interviewResponse = new InterviewResponse(errors,null);
        ResponseEntity expectedResponse = new ResponseEntity(interviewResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity actualResponse = interviewServiceImpl.getInterview(id);

        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
    }

    @Test
    @Transactional
    void deleteInterview() {
        String id = "id1";
        Candidate candidate = new Candidate("id1", AttendeeStatusValue.pending);

        Interviewer interviewer = new Interviewer("ID1", AttendeeStatusValue.tentative);

        List<Interviewer> interviewers = new ArrayList<>();
        interviewers.add(interviewer);

        InterviewType interviewType = new InterviewType("Formal");

        Timeslot timeslot = new Timeslot(interviewType,"Electrician", "Timisoara", "2022-08-10T10:26:34.759Z","2022-08-10T10:26:34.759Z", interviewers);

        List<Timeslot> timeslots =new ArrayList<>();
        timeslots.add(timeslot);

        Interview interview = new Interview(candidate,"jobId","Timisoara","Cineva",TimeZone.getTimeZone("GMT"),timeslots,"2022-08-10T10:26:34.759Z","string","string");
        List<Interview> interviews = new ArrayList<>();
        interviews.add(interview);
        Integer i = 1;


        when(interviewRepository.findById(id)).thenReturn(Optional.of(interview));
        interviewServiceImpl.deleteInterview("id1");
        verify(interviewRepository, times(1)).delete(interview);
        verify(candidateRepository, times(1)).delete(candidate);
        verify(timeslotRepository, times(timeslots.size())).delete(timeslot);
        verify(interviewerRepository, times(1)).delete(interviewer);


    }

    @Test
    void deleteInterviewNotFound() {
        String id = "string";
        Interview interview1 = null;
        when(interviewRepository.findById(id)).thenReturn(Optional.ofNullable(interview1));

        Error error = new Error("404", "INTERVIEW NOT FOUND");
        List<Error>  errors  = new ArrayList<>();
        errors.add(error);

        InterviewResponse interviewResponse = new InterviewResponse(errors,null);
        ResponseEntity<InterviewResponse> expectedResponse = new ResponseEntity<>(interviewResponse,HttpStatus.NOT_FOUND);
        ResponseEntity<InterviewResponse> actualResponse = interviewServiceImpl.deleteInterview(id);

        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
        verify(interviewRepository, times(0)).delete(interview);
    }

    @Test
    void deleteInterviewException() {
        String id = "string";
        when(interviewRepository.findById(id)).thenThrow(NullPointerException.class);
        interviewServiceImpl.deleteInterview(id);

        Error error = new Error("500", "INTERNAL SERVER ERROR");
        List<Error>  errors  = new ArrayList<>();
        errors.add(error);

        InterviewResponse interviewResponse = new InterviewResponse(errors,null);
        ResponseEntity<InterviewResponse> expectedResponse = new ResponseEntity<>(interviewResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<InterviewResponse> actualResponse = interviewServiceImpl.deleteInterview(id);

        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
        verify(interviewRepository, times(0)).delete(interview);
    }
}