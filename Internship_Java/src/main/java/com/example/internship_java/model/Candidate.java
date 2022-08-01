package com.example.internship_java.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Candidate {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private enum AttendeStatusValue {
        accepted,
        declined,
        pnding,
        tentative
    };

    private AttendeStatusValue status;

    public Candidate() {
    }

    public Candidate(AttendeStatusValue status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AttendeStatusValue getStatus() {
        return status;
    }

    public void setStatus(AttendeStatusValue status) {
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
