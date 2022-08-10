package com.example.internship_java.response;

import com.example.internship_java.model.Error;
import com.example.internship_java.model.Timeslot;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

public class TimeslotResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timeslot timeslot;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Collection<Error> errors;

    public TimeslotResponse(Timeslot timeslot, Collection<Error> errors) {
        this.timeslot = timeslot;
        this.errors = errors;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    public Collection<Error> getErrors() {
        return errors;
    }

    public void setErrors(Collection<Error> errors) {
        this.errors = errors;
    }
}
