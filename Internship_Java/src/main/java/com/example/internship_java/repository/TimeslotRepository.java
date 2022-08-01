package com.example.internship_java.repository;

import com.example.internship_java.model.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TimeslotRepository extends JpaRepository<Timeslot, Object> {
    @Query
    public Timeslot findById(String id);
}
