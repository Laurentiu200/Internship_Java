package com.example.internship_java.service;


import com.example.internship_java.model.Timeslot;
import com.example.internship_java.response.TimeslotResponse;
import org.springframework.http.ResponseEntity;


public interface TimeslotsService {
    ResponseEntity<TimeslotResponse> addTimeslot(String interviewId, Timeslot timeslotToAdd);

    ResponseEntity<TimeslotResponse> deleteTimeslot(String interviewId, String timeslotId);

    ResponseEntity<TimeslotResponse> getTimeslot(String interviewId, String timeslotId);

    ResponseEntity<TimeslotResponse> updateTimeslot(String interviewId, String timeslotId, Timeslot updatedTimeslot);
}
