package com.example.lamcagym.Controller;

import com.example.lamcagym.Entity.Login;
import com.example.lamcagym.Entity.User;
import com.example.lamcagym.Service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Annotering som definierar klassen som en REST-kontroller med en specifik bas-URL för alla dess hanterade anrop.
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    // Logger för att logga information, varningar och fel.
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    // Konstruktor som injicerar en instans av UserService.
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Hanterar GET-förfrågningar till "/user/all". Returnerar en lista av alla användare.
    @GetMapping("/all")
    public ArrayList<User> getAllUsers() {
        logger.info("Fetching all users from the database.");
        return (ArrayList<User>) userService.getAll();
    }

    // Hanterar GET-förfrågningar till "/user/{id}". Returnerar en användare med specifikt ID eller en 404 om användaren inte finns.
    @GetMapping("/{id}")
    public ResponseEntity<User> userGetById(@PathVariable int id) {
        logger.info("Requested user by ID: {}", id);
        User user = userService.getUser(id);
        if (user != null) {
            logger.info("User with ID: {} found.", id);
            return ResponseEntity.ok(user);
        } else {
            logger.warn("User with ID: {} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Hanterar POST-förfrågningar till "/user/". Skapar en ny användare om inga valideringsfel uppstår.
    @PostMapping("/")
    public ResponseEntity<?> createUser(@Valid @RequestBody User newUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Om valideringsfel uppstår, logga och returnera dessa som ett felmeddelande
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            logger.warn("Validation errors occurred while creating a new user: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }
        logger.info("Creating user with name: {}", newUser.getName());
        boolean success = userService.createUser(newUser);
        if (success) {
            logger.info("User successfully created with email: {}", newUser.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } else {
            logger.error("Failed to create user with email: {}", newUser.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create user.");
        }
    }

    // Hanterar POST-förfrågningar för att logga in användare genom att verifiera deras e-post och lösenord
    @PostMapping("/login")
    public ResponseEntity<?> loginRequest(@RequestBody Map<String, String> loginDetails) {
        String email = loginDetails.get("email");
        String password = loginDetails.get("password");
        User user = userService.getUserByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            // Loggar inloggningen
            logger.info(user.getName() + " successfully logged in");

            // Skapar ett svar med användarinformation
            Map<String, String> response = new HashMap<>();
            response.put("message", "Successfully login");
            response.put("userId", user.getUserId().toString());
            response.put("name", user.getName());

            // Returnerar ett OK-svar med användardata
            return ResponseEntity.ok().body(response);
        }
        // Om användaren inte finns eller lösenordet är fel
        logger.warn("Login attempt failed for email: " + email);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Authentication failed");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }



    // Hanterar POST-förfrågningar till "/register". Registrerar en ny användare om inga valideringsfel uppstår.
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User newUser, BindingResult bindingResult) {
        Logger logger = LoggerFactory.getLogger(getClass());

        if (bindingResult.hasErrors()) {
            // Om valideringsfel uppstår, logga och returnera dessa som ett felmeddelande
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            logger.warn("Validation errors occurred while registering a new user: {}", errors);
            System.out.println(errors);
            return ResponseEntity.badRequest().body(errors);
        }
        logger.info("Attempting to register new user with email: {}", newUser.getEmail());
        boolean success = userService.createUser(newUser);
        if (success) {
            logger.info("Successfully registered user with email: {}", newUser.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } else {
            logger.warn("Failed to register user with email: {}", newUser.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failure to register user.");
        }
    }

    // Hanterar DELETE-förfrågningar till "/user/". Tar bort en användare med specifikt ID om användaren finns.
    @DeleteMapping("/")
    public ResponseEntity<?> deleteUser(@RequestParam int id) {
        logger.info("Attempting to delete user with ID: {}", id);
        boolean success = userService.deleteUser(id);
        if (success) {
            logger.info("User with ID: {} successfully deleted.", id);
            return ResponseEntity.ok().body("User with ID: " + id + " successfully deleted.");
        } else {
            logger.warn("User with ID: {} not found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID: " + id + " not found.");
        }
    }

    // Hanterar PUT-förfrågningar för att uppdatera en användare baserat på dess ID
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody User userDetails,
            BindingResult bindingResult) {

        // Kontrollerar om det finns valideringsfel i de medskickade användardetaljerna
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            logger.warn("Validation errors occurred while updating user with ID {}: {}", id, errors);
            return ResponseEntity.badRequest().body(errors);
        }
        // Loggar försök att uppdatera användaren
        logger.info("Attempting to update user with ID: {}", id);
        User existingUser = userService.getUser(id);  // Hämtar den befintliga användaren baserat på ID
        // Kontrollerar om användaren faktiskt finns
        if (existingUser != null) {
            // Uppdaterar användarens detaljer
            existingUser.setName(userDetails.getName());
            existingUser.setEmail(userDetails.getEmail());
            existingUser.setPhoneNumber(userDetails.getPhoneNumber());
            existingUser.setPassword(userDetails.getPassword());

            // Försöker spara de uppdaterade användardetaljerna
            boolean success = userService.updateUser(existingUser);
            if (success) {
                logger.info("User with ID: {} successfully updated.", id);
                return ResponseEntity.ok(existingUser);  // Returnerar den uppdaterade användaren om lyckad
            } else {
                // Loggar ett fel om uppdateringen inte lyckades
                logger.warn("Could not update user with ID: {}.", id);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not update user.");
            }
        }
        // Loggar och returnerar ett felmeddelande om användaren inte kunde hittas
        logger.warn("User with ID: {} not found.", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID: " + id + " not found.");
    }
}
