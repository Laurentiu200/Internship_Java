package com.example.internship_java.response;

import com.example.internship_java.model.Error;
import com.example.internship_java.model.Interview;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;
import java.util.List;

public class InterviewResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Collection<Error> errors;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Collection<Interview> interviews;

    public InterviewResponse(Collection<Error> errors, Collection<Interview> interviews) {
        this.errors = errors;
        this.interviews = interviews;
    }

    public InterviewResponse() {
    }

    public Collection<Error> getErrors() {
        return errors;
    }

    public void setErrors(Collection<Error> errors) {
        this.errors = errors;
    }

    public Collection<Interview> getInterviews() {
        return interviews;
    }

    public void setInterviews(Collection<Interview> interviews) {
        this.interviews = interviews;
    }
}
