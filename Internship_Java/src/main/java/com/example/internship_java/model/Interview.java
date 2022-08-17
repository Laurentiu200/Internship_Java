package com.example.internship_java.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Entity
public class Interview {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    @ManyToOne
    @JoinColumn(name = "fk_candidate_id")
    private Candidate candidate;
    private String jobId;
    private String organizerId;
    private String location;
    private TimeZone timezone;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_timeslots_id")
    private List<Timeslot> timeslots = new ArrayList<>(50);
    private String createdOn;
    private String refUrl;
    private String source;

    public Interview(Candidate candidate, String jobId, String location, String organizerId, TimeZone timezone, List<Timeslot> timeslots, String createdOn, String refUrl, String source) {

        this.candidate = candidate;
        this.jobId = jobId;
        this.organizerId = organizerId;
        this.location = location;
        this.timezone = timezone;
        this.timeslots = timeslots;
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

    public List<Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<Timeslot> timeSlots) {
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
}
