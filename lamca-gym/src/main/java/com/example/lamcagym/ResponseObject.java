package com.example.lamcagym;

import lombok.Data;

@Data // Lombok-annotation för att generera getter, setter och toString automatiskt
public class ResponseObject {
    private String status; // Status för svar (t.ex. "success" eller "error")
    private String message; // Meddelande för svar (t.ex. en beskrivning av statusen)
    private Object data; // Data för svar (kan vara vilken typ av objekt som helst)

    // Konstruktor för att skapa ett ResponseObject med angiven status, meddelande och data
    public ResponseObject(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}