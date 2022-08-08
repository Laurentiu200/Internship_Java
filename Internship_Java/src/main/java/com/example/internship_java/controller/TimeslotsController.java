package com.example.internship_java.controller;

import com.example.internship_java.model.Timeslot;
import com.example.internship_java.response.TimeslotResponse;
import com.example.internship_java.service.TimeslotsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class TimeslotsController {
    final
    TimeslotsService timeslotsService;

    public TimeslotsController(TimeslotsService timeslotsService) {
        this.timeslotsService = timeslotsService;
    }

    @PatchMapping(value = "/interviews/{interviewId}/timeslots/{timeslotId}", consumes = "application/json")
    public ResponseEntity<TimeslotResponse> patchInterviewTypes
            (@PathVariable String interviewId,
             @PathVariable String timeslotId,
             @RequestBody Timeslot updateData) {
        return timeslotsService.updateTimeslot(interviewId, timeslotId, updateData);
    }

    @GetMapping(value = "/interviews/{interviewId}/timeslots/{timeslotId}")
    public ResponseEntity<TimeslotResponse> getTimeslot
            (@PathVariable String interviewId,
             @PathVariable String timeslotId) {
        return timeslotsService.getTimeslot(interviewId, timeslotId);
    }

    @PostMapping(value = "/interviews/{interviewId}/timeslots", consumes = "application/json")
    public ResponseEntity<TimeslotResponse>addTimeslot
            (@PathVariable String interviewId,
             @RequestBody Timeslot timeslotToAdd) {
        return timeslotsService.addTimeslot(interviewId, timeslotToAdd);
    }

    @DeleteMapping(value = "/interviews/{interviewId}/timeslots/{timeslotId}")
    public ResponseEntity<TimeslotResponse> deleteTimeslot
            (@PathVariable String interviewId,
             @PathVariable String timeslotId) {
        return timeslotsService.deleteTimeslot(interviewId, timeslotId);
    }
}
