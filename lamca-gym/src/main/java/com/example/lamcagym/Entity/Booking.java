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

    @Column(name = "booking_time")
    private Date bookingTime;

    // Many-to-one relation with User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Many-to-one relation with Session
    @ManyToOne
    @JoinColumn(name = "sessions_id")
    private Session session;
}