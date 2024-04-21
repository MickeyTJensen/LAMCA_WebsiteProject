package com.example.lamcagym.Entity;
// Importera nödvändiga bibliotek och moduler för databasinteraktion och validering

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data // Lombok-annotering för att automatiskt generera getter, setter, toString, equals och hashCode metoder
@Entity // Anger att denna klass är en entitetsklass som motsvarar en tabell i databasen
@NoArgsConstructor // Lombok-annotering för att skapa en konstruktor utan argument
@AllArgsConstructor // Lombok-annotering för att skapa en konstruktor som inkluderar alla fält
@Table(name = "bookings") // Anger namnet på tabellen i databasen

public class Booking {
    @Id // Märker detta fält som primärnyckel
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Anger att ID ska genereras automatiskt av databasen
    @Column(name = "bookings_id") // Anger kolumnnamnet i databasen

    private Integer bookingId; // Unikt ID för varje bokningspost
    @ManyToOne // Definierar en många-till-en-relation till User
    @JoinColumn(name = "user_id") // Anger vilken kolumn som fungerar som främmande nyckel
    @NotNull(message = "User must be specified") // Validering: en användare måste vara specificerad

    private User user; // Användaren som gjort bokningen
    @ManyToOne // Definierar en många-till-en-relation till Session
    @JoinColumn(name = "sessions_id") // Anger vilken kolumn som fungerar som främmande nyckel
    @NotNull(message = "Session must be specified") // Validering: en session måste vara specificerad

    private Session session; // Sessionen som är bokad
    @Column(name = "booking_time") // Anger kolumnnamnet för bokningstiden
    @NotNull(message = "Booking time cannot be null") // Validering: bokningstid får inte vara null

    private Date bookingTime; // Tiden då bokningen är gjord

    // Konstruktor som tar alla nödvändiga attribut för att skapa en Booking
    public Booking(User user, Session session, Date bookingTime) {
        this.user = user;
        this.session = session;
        this.bookingTime = bookingTime;
    }
}



