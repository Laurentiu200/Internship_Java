package com.example.internship_java.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.TimeZone;

public class Interview {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private Candidate candidate;
    private String location = "Romania";
    private String organizerId;
    private TimeZone timezone = TimeZone.getTimeZone(location);

    public Interview(Candidate candidate, String location, String organizerId, TimeZone timezone) {
        this.candidate = candidate;
        this.location = location;
        this.organizerId = organizerId;
        this.timezone = timezone;
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
}
