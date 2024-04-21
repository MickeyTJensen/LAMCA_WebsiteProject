package com.example.lamcagym.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

// DTO (Data Transfer Object) för att representera sessioner
@Data // Lombok-annotation för att generera getter, setter och toString automatiskt
@NoArgsConstructor // Lombok-annotation för att generera en standardkonstruktor

public class SessionDTO {
    @NonNull private String id; // Sessionens ID
    @NonNull private String title; // Titel för sessionen
    @NonNull private LocalDateTime start; // Starttid för sessionen
    private Integer duration; // Varaktighet för sessionen i minuter
    private LocalDateTime end; // Sluttid för sessionen

    // Metod för att sätta varaktigheten för sessionen
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    // Metod för att beräkna sluttiden för sessionen baserat på starttiden och varaktigheten
    public void calculateEnd() {
        if (this.start != null && this.duration != null) {
            this.end = this.start.plusMinutes(this.duration);
        }
    }
}