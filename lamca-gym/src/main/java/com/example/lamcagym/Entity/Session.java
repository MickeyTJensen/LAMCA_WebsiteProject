package com.example.lamcagym.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id") // Det här är kolumnnamnet i din databas
    private Integer sessionId; // Det här är fältet i din Java-klass

    @NotBlank(message = "Session type cannot be blank")
    @Column(name = "session_type", length = 50)
    private String sessionType;

    @Column(name = "time", nullable = false)
    private Date time;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "instructor", length = 100)
    private String instructor;

    public Session(String sessionType, Date time, Integer duration, Integer capacity, String instructor) {
        this.sessionType = sessionType;
        this.time = time;
        this.duration = duration;
        this.capacity = capacity;
        this.instructor = instructor;
    }
}