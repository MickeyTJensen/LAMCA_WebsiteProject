package com.example.lamcagym.Entity;

import lombok.*;

@Data // Lombok-annotation för att generera getter, setter och toString automatiskt
@NoArgsConstructor // Lombok-annotation för att generera en standardkonstruktor utan argument
@AllArgsConstructor // Lombok-annotation för att generera en konstruktor med alla fält

public class Login {
    private String email; // Användarens e-postadress för inloggning
    private String password; // Användarens lösenord för inloggning
}