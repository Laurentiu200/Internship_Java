package com.example.internship_java.response;

import com.example.internship_java.model.Error;

import java.util.Collection;

public class StatusResponse {
    Collection<Error> errors;

    public StatusResponse(Collection<Error> errors) {
        this.errors = errors;
    }

    public Collection<Error> getErrors() {
        return errors;
    }

    public void setErrors(Collection<Error> errors) {
        this.errors = errors;
    }
}
