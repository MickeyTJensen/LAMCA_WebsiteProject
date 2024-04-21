package com.example.lamcagym;

import lombok.Data;

@Data // Lombok-annotation för att generera getter, setter och toString automatiskt
public class BookingRequest {
    private int userId; // Användar-ID för bokningen
    private int sessionId; // Session-ID för bokningen
}