package com.example.lamcagym.Entity;

// Importera nödvändiga bibliotek och moduler
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data // Lombok-annotering för att automatiskt generera getter, setter, toString, equals och hashCode metoder
@Entity // Anger att denna klass är en entitetsklass som motsvarar en tabell i databasen
@NoArgsConstructor // Skapar en konstruktor utan argument
@AllArgsConstructor // Skapar en konstruktor med argument för alla fält

@Table(name = "sessions") // Anger tabellnamnet i databasen
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autogenererar ID med databasens autoincrement
    @Column(name = "session_id") // Anger kolumnnamnet i databasen för fältet sessionId
    private Integer sessionId; // Sessions-ID som unikt identifierar sessionen

    @NotBlank(message = "Session type cannot be blank") // Validering: sessionstyp får inte vara tomt
    @Column(name = "session_type", length = 50) // Anger kolumnnamnet och maxlängden för sessionstypen
    private String sessionType; // Typ av session (ex. Yoga, Spinning)

    @Column(name = "time", nullable = false) // Anger kolumnnamnet och att tid inte får vara null
    private Date time; // Tidpunkt för sessionen

    @Column(name = "duration") // Anger kolumnnamnet för varaktigheten
    private Integer duration; // Varaktighet av sessionen i minuter

    @Column(name = "capacity") // Anger kolumnnamnet för kapaciteten
    private Integer capacity; // Max antal deltagare i sessionen

    @Column(name = "instructor", length = 100) // Anger kolumnnamnet och maxlängden för instruktörens namn
    private String instructor; // Namnet på instruktören för sessionen

    @OneToMany(mappedBy = "session", fetch = FetchType.LAZY) // Relation till bokningar, laddas "late" (vid behov)
    @JsonIgnore // Ignorera detta fält i JSON-svar för att förhindra oändliga rekursioner
    private List<Booking> bookings; // Lista över bokningar kopplade till sessionen

    @OneToMany(mappedBy = "session", fetch = FetchType.LAZY) // Relation till kalendrar, laddas "late" (vid behov)
    @JsonIgnore // Ignorera detta fält i JSON-svar
    private List<Calendar> calendars; // Lista över kalendrar kopplade till sessionen
    @Transient // För att inte persistensfältet i databasen
    private Integer booked;

    // Extra konstruktor som är användbar vid skapandet av en ny session
    public Session(String sessionType, Date time, Integer duration, Integer capacity, String instructor) {
        this.sessionType = sessionType;
        this.time = time;
        this.duration = duration;
        this.capacity = capacity;
        this.instructor = instructor;
    }
    public void setBooked(Integer bookedCount) {
        this.booked = bookedCount;
    }
}