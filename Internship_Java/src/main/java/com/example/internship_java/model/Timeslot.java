package com.example.internship_java.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="timeslot")
public class Timeslot {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    @OneToOne
    private InterviewType interviewType;
    private String title;
    private String place;
    private String startsOn;
    private String endOn;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Interviewer> interviewers = new ArrayList<>(50);

    public Timeslot(InterviewType interviewType, String title, String place, String startsOn, String endOn, List<Interviewer> interviewers) {
        this.interviewType = interviewType;
        this.title = title;
        this.place = place;
        this.startsOn = startsOn;
        this.endOn = endOn;
        this.interviewers = interviewers;
    }

    public Timeslot(){}

    public String getId() {
        return id;
    }

    public InterviewType getInterviewType() {
        return interviewType;
    }

    public List<Interviewer> getInterviewers() {
        return interviewers;
    }

    public void setInterviewers(List<Interviewer> interviewers) {
        this.interviewers = interviewers;
    }

    public void setInterviewType(InterviewType interviewType) {
        this.interviewType = interviewType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStartsOn() {
        return startsOn;
    }

    public void setStartsOn(String startsOn) {
        this.startsOn = startsOn;
    }

    public String getEndOn() {
        return endOn;
    }

    public void setEndOn(String endOn) {
        this.endOn = endOn;
    }
}
