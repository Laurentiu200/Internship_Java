package com.example.internship_java.response;

import com.example.internship_java.model.Error;
import com.example.internship_java.model.InterviewType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;
import java.util.List;

public class InterviewTypeResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Collection<InterviewType> interviewTypes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Collection<Error> errors;

    public InterviewTypeResponse(Collection<InterviewType> interviewTypes, Collection<Error> errors) {
        this.interviewTypes = interviewTypes;
        this.errors = errors;
    }

    public Collection<InterviewType> getInterviewTypes() {
        return interviewTypes;
    }

    public void setInterviewTypes(Collection<InterviewType> interviewTypes) {
        this.interviewTypes = interviewTypes;
    }

    public Collection<Error> getErrors() {
        return errors;
    }

    public void setErrors(Collection<Error> errors) {
        this.errors = errors;
    }
}
