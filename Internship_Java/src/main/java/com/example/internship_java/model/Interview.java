package com.example.internship_java.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
@Entity
@Table(name = "Interviews")
public class Interview {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    @OneToOne(targetEntity = Candidate.class,  cascade = CascadeType.ALL)
    @JoinColumn(name="id",  referencedColumnName = "id")
    private Candidate candidate;
    private String jobId;
    @Column
    private String organizerId;
    @Column
    private String location;
    @Column
    private TimeZone timezone;

    @OneToMany(targetEntity = Timeslot.class,  cascade = CascadeType.ALL)
    @JoinColumn(name="id",  referencedColumnName = "id")
    private List<Timeslot> timeslots = new ArrayList<>(50);
    private String createdOn;
    private String refUrl;
    private String source;



    public Interview(Candidate candidate, String jobId, String location, String organizerId, TimeZone timezone, List<Timeslot> timeSlots, String createdOn, String refUrl, String source) {
        this.candidate = candidate;
        this.jobId = jobId;
        this.organizerId = organizerId;
        this.location = location;
        this.timezone = timezone;
        this.timeslots = timeSlots;
        this.createdOn = createdOn;
        this.refUrl = refUrl;
        this.source = source;

    }

    public Interview() {

    }


    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public TimeZone getTimezone() {
        return timezone;
    }

    public void setTimezone(TimeZone timezone) {
        this.timezone = timezone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public List<Timeslot> getTimeSlots() {
        return timeslots;
    }

    public void setTimeSlots(List<Timeslot> timeSlots) {
        this.timeslots = timeSlots;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getRefUrl() {
        return refUrl;
    }

    public void setRefUrl(String refUrl) {
        this.refUrl = refUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Interview{" +
                "id='" + id + '\'' +
                ", candidate=" + candidate +
                ", jobId='" + jobId + '\'' +
                ", organizerId='" + organizerId + '\'' +
                ", location='" + location + '\'' +
                ", timezone=" + timezone +
                ", timeSlots=" + timeslots +
                ", createdOn='" + createdOn + '\'' +
                ", refUrl='" + refUrl + '\'' +
                ", source='" + source + '\'' +
                '}';
    }

}
