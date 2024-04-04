package com.example.lamcagym.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "calendars")
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer calendarId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "sessions_id")
    private Integer sessionsId;

    @Column(name = "booking_time")
    private Date bookingTime;

    public Calendar(Integer userId, Integer sessionsId, Date bookingTime) {
        this.userId = userId;
        this.sessionsId = sessionsId;
        this.bookingTime = bookingTime;
    }
// Standard constructors, getters, and setters
}