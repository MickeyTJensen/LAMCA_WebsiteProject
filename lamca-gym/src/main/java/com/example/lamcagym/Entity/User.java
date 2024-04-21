package com.example.lamcagym.Entity;

// Importera nödvändiga bibliotek och moduler
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;


@Data // Lombok-annotering för att automatiskt generera getter, setter, toString, equals och hashCode metoder
@Entity // Anger att denna klass är en entitetsklass som motsvarar en tabell i databasen
@NoArgsConstructor  // Skapar en konstruktor utan argument
@AllArgsConstructor // Skapar en konstruktor med argument för alla fält

@Table(name = "users") // Anger tabellnamnet i databasen
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autogenererar ID med databasens autoincrement
    @Column(name = "user_id") // Anger kolumnnamnet i databasen för fältet userId
    private Integer userId; // Användar-ID som unikt identifierar användaren

    @NotBlank(message = "Name cannot be blank") // Validering: namn får inte vara tomt
    @Size(min = 2, message = "Name must be at least 2 characters long") // Validering: namn måste vara minst 2 tecken långt
    @Column(name = "name", length = 255) // Anger kolumnnamnet och maxlängden för namnet
    private String name; // Användarens namn

    @NotBlank(message = "Email cannot be blank") // Validering: e-post får inte vara tom
    @Email(message = "Invalid email format") // Validering: måste vara i giltigt e-postformat
    @Column(name = "email", length = 255) // Anger kolumnnamnet och maxlängden för e-post
    private String email; // Användarens e-postadress

    @NotBlank(message = "Phone number cannot be blank") // Validering: telefonnummer får inte vara tomt
    @Size(min = 10, message = "Phone number must be at least 10 digits long") // Validering: telefonnummer måste vara minst 10 siffror långt
    @Column(name = "phone_number", length = 45) // Anger kolumnnamnet och maxlängden för telefonnummer
    private String phoneNumber; // Användarens telefonnummer

    @NotBlank(message = "Password cannot be blank") // Validering: lösenord får inte vara tomt
    @Size(min = 6, message = "Password must be at least 6 characters long") // Validering: lösenordet måste vara minst 6 tecken långt
    @Column(name = "password") // Anger kolumnnamnet för lösenord
    private String password; // Användarens lösenord

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) // Relation till bokningar, laddas "late" (vid behov)
    @JsonIgnore // Ignorera detta fält i JSON-svar för att förhindra oändliga rekursioner
    private List<Booking> bookings; // Lista över bokningar kopplade till användaren

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY) // En-till-en-relation till användarens kalender, laddas "late"
    @JsonIgnore // Ignorera detta fält i JSON-svar
    private Calendar calendar; // Användarens kalender

    // Extra konstruktor som är användbar vid skapandet av en ny användare
    public User(String name, String email, String phoneNumber, String password) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}
