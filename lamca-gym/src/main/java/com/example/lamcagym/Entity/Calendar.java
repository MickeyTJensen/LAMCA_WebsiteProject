package com.example.lamcagym.Entity;

// Importera nödvändiga bibliotek och moduler för databasinteraktion och validering
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data // Lombok-annotering för att automatiskt generera getter, setter, toString, equals och hashCode metoder
@Entity // Anger att denna klass är en entitetsklass som motsvarar en tabell i databasen
@NoArgsConstructor // Lombok-annotering för att skapa en konstruktor utan argument
@AllArgsConstructor // Lombok-annotering för att skapa en konstruktor som inkluderar alla fält

@Table(name = "calendars") // Anger namnet på tabellen i databasen
public class Calendar {

    @Id // Märker detta fält som primärnyckel
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Anger att ID ska genereras automatiskt av databasen
    private Integer calendarId; // Unikt ID för varje kalenderpost

    @Column(name = "booking_time") // Anger kolumnnamnet i databasen och att detta fält representerar bokningstiden
    private Date bookingTime; // Tiden för bokningen

    // Many-to-one relation med User. Varje Calendar-post måste ha en associerad User.
    @ManyToOne // Definierar en många-till-en-relation mellan kalenderposter och användare
    @JoinColumn(name = "user_id", nullable = false) // Anger vilken kolumn som fungerar som främmande nyckel
    private User user; // Associerad användare

    @ManyToOne // Definierar en många-till-en-relation mellan kalenderposter och sessioner
    @JoinColumn(name = "sessions_id", nullable = false) // Anger vilken kolumn som fungerar som främmande nyckel
    private Session session; // Associerad session

    // Definiera konstruktorn som tar parametrar för alla attribut
    public Calendar(Date bookingTime, User user, Session session) {
        this.bookingTime = bookingTime;
        this.user = user;
        this.session = session;
    }
}
