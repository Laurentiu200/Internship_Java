package com.example.internship_java.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Interviewer {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private AttendeStatusValue status;

    public AttendeStatusValue getStatus() {
        return status;
    }

    public void setStatus(AttendeStatusValue status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }
}
