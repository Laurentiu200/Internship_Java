package com.example.internship_java.controller;

import com.example.internship_java.response.InterviewTypeResponse;
import com.example.internship_java.service.InterviewTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class InterviewTypeController {
    final
    InterviewTypeService interviewTypeService;

    public InterviewTypeController(InterviewTypeService interviewTypeService) {
        this.interviewTypeService = interviewTypeService;
    }

    @GetMapping("/interview-types")
    public ResponseEntity<InterviewTypeResponse> getAllInterviewTypes() {
        return interviewTypeService.getInterviewTypes();
    }

    @PatchMapping("/interview-types")
    public ResponseEntity<InterviewTypeResponse> patchInterviewTypes(@RequestBody String name) {
        return interviewTypeService.patchInterviewType(name);
    }


    @DeleteMapping("/interview-types/{interviewType}")
    public ResponseEntity<InterviewTypeResponse> deleteInterviewType(@PathVariable String interviewType) {
        return interviewTypeService.deleteInterviewType(interviewType);

    }

}
