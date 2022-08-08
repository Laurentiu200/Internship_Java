package com.example.internship_java.controller;

import com.example.internship_java.response.StatusResponse;
import com.example.internship_java.service.StatusServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class StatusController {
    final
    StatusServiceImpl statusService;

    public StatusController(StatusServiceImpl statusService) {
        this.statusService = statusService;
    }

    @PutMapping(value = "/interviews/{interviewId}/candidate/status")
    ResponseEntity<StatusResponse> putCandidateStatus
            (@PathVariable String interviewId,
             @RequestBody String statusValue) {
        return statusService.updateCandidateStatus(interviewId, statusValue);
    }

    @PutMapping(value = "/interviews/{interviewId}/timeslots/{timeslotId}/interviewers/{userId}/status")
    ResponseEntity<StatusResponse> putInterviewerStatus
            (@PathVariable String interviewId,
             @PathVariable String timeslotId,
             @PathVariable String userId,
             @RequestBody String statusValue) {
        return statusService.updateInterviewerStatus(interviewId, timeslotId, userId, statusValue);
    }
}
