package com.example.internship_java.repository;

import com.example.internship_java.model.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeslotRepository extends JpaRepository<Timeslot,String> {
}
