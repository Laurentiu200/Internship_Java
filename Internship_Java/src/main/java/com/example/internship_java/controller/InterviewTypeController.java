package com.example.internship_java.controller;

import com.example.internship_java.service.InterviewTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class InterviewTypeController {
    final
    InterviewTypeService interviewTypeService;

    public InterviewTypeController(InterviewTypeService interviewTypeService) {
        this.interviewTypeService = interviewTypeService;
    }

    @GetMapping("/interview-types")
    public ResponseEntity<Object> getAllInterviewTypes() {
        return interviewTypeService.getInterviewTypes();
    }

    @PatchMapping("/interview-types")
    public ResponseEntity<Object> patchInterviewTypes(@RequestBody String name) {
        return interviewTypeService.patchInterviewType(name);
    }

    @DeleteMapping("/interview-types")
    public ResponseEntity<Object> deleteInterviewType(@RequestParam String name) {
        return interviewTypeService.deleteInterviewType(name);
    }

}
