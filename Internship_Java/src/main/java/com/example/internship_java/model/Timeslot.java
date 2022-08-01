package com.example.internship_java.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Timeslot {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private InterviewType interviewType;
    private String title;
    private String place;
    private String startsOn;
    private String endOn;
    private Interviewer[] interviewers = new Interviewer[50];

    public Timeslot(InterviewType interviewType, String title, String place, String startsOn, String endOn, Interviewer[] interviewers) {
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

    public Interviewer[] getInterviewers() {
        return interviewers;
    }

    public void setInterviewers(Interviewer[] interviewers) {
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
