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
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookings_id")
    private Integer bookingId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "sessions_id")
    private Integer sessionsId;

    @Column(name = "booking_time")
    private Date bookingTime;

    public Booking(Integer userId, Integer sessionsId, Date bookingTime) {
        this.userId = userId;
        this.sessionsId = sessionsId;
        this.bookingTime = bookingTime;
    }
}