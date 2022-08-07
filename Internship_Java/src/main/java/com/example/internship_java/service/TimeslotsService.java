package com.example.internship_java.service;


import com.example.internship_java.model.Timeslot;
import org.springframework.http.ResponseEntity;


public interface TimeslotsService {
    ResponseEntity<Object> addTimeslot(String interviewId, Timeslot timeslotToAdd);

    ResponseEntity<Object> deleteTimeslot(String interviewId, String timeslotId);

    ResponseEntity<Object> getTimeslot(String interviewId, String timeslotId);

    ResponseEntity<Object> updateTimeslot(String interviewId, String timeslotId, Timeslot updatedTimeslot);
}
