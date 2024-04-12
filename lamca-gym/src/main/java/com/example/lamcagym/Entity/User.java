package com.example.lamcagym.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") // Det här är kolumnnamnet i din databas
    private Integer userId; // Det här är fältet i din Java-klass

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, message = "Name must be at least 2 characters long")
    @Column(name = "name", length = 255)
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Column(name = "email", length = 255)
    private String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Size(min = 10, message = "Phone number must be at least 10 digits long")
    @Column(name = "phone_number", length = 45)
    private String phoneNumber;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Column(name = "password")
    private String password;

    // One-to-many relation with Booking
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    // One-to-one relation with Calendar
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Calendar calendar;

    // Den här konstruktorn är inte nödvändig om du använder Lomboks @AllArgsConstructor
    // Men om du behöver skräddarsy vilka fält som inkluderas i konstruktorn, kan du behålla den
    public User(String name, String email, String phoneNumber, String password) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}
