package com.example.internship_java.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Candidate {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private AttendeeStatusValue status;

    public Candidate() {
    }

    public Candidate(AttendeeStatusValue status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AttendeeStatusValue getStatus() {
        return status;
    }

    public void setStatus(AttendeeStatusValue status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "id='" + id + '\'' +
                ", status=" + status +
                '}';
    }
}
