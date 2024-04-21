package com.example.lamcagym.Repository;

import com.example.lamcagym.Entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository-gränssnitt för att hantera dataåtkomst för entiteten Calendar
@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Integer> {

}