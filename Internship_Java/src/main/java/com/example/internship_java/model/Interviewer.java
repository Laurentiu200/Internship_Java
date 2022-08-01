package com.example.internship_java.model;

import com.google.gson.annotations.SerializedName;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="interviewer")
public class Interviewer {
    @Id
    private String id;
    AttendeeStatusValue status;

    public Interviewer() {
    }

    public Interviewer(String id, AttendeeStatusValue status) {
        this.id = id;
        this.status = status;
    }

    public AttendeeStatusValue getStatus() {
        return status;
    }

    public void setStatus(AttendeeStatusValue status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
