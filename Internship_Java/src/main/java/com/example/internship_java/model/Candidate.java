package com.example.internship_java.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "candidate")
public class Candidate {
    @Id
    private String id;

    private AttendeeStatusValue status;

    public Candidate() {
    }

    public Candidate(String id, AttendeeStatusValue status) {
        this.id=id;
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId(){
        return this.id;
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
