package com.example.internship_java.service;

import com.example.internship_java.model.Error;
import com.example.internship_java.model.InterviewType;
import com.example.internship_java.model.Timeslot;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

public interface TimeslotsService {
    ResponseEntity<Collection<Error>> addTimeslot(String interviewId, Timeslot timeslotToAdd);

    ResponseEntity<Collection<Error>> deleteTimeslot(String interviewId, String timeslotId);

    ResponseEntity<Timeslot> getTimeslot(String interviewId, String timeslotId);

    ResponseEntity<Collection<Error>> updateTimeslot(String interviewId, String timeslotId, Timeslot updatedTimeslot);
}
