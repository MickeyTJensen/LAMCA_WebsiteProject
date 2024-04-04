package com.example.lamcagym.Controller;

import com.example.lamcagym.Entity.User;
import com.example.lamcagym.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ArrayList<User> getAllUsers() {
        logger.info("Fetching all users from the database.");
        return (ArrayList<User>) userService.getAll();
    }

    @GetMapping("/")
    public ResponseEntity<User> userGetById(@RequestParam int id) {
        logger.info("Requested user by ID: {}", id);
        User user = userService.getUser(id);
        if (user != null) {
            logger.info("User with ID: {} found.", id);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        logger.warn("User with ID: {} not found.", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/")
    public ResponseEntity<?> createUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam("phone_number") String phoneNumber,
            @RequestParam String password) {

        logger.info("Creating user with name: {}", name);
        User newUser = new User(name, email, phoneNumber, password);
        boolean success = userService.createUser(newUser);
        if (success) {
            logger.info("User successfully created.");
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created");
        }
        logger.error("Failed to save user.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to save.");
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginRequest(String email, String password){
        User user = userService.getUserByEmail(email);
        if(user != null && user.getPassword().equals(password)){
            logger.info(user.getName() + " Successfully login");
            return ResponseEntity.status(HttpStatus.OK).body("Successfully login");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exists");
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(String name, String email, String phoneNumber, String password){
        User newUser = new User(name,email,phoneNumber,password);
        boolean success = userService.createUser(newUser);
        if(success){
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failure");
    }
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

    @PutMapping("/")
    public ResponseEntity<?> updateUser(
            @RequestParam int id,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam("phone_number") String phoneNumber,
            @RequestParam String password) {

        logger.info("Attempting to update user with ID: {}", id);
        User user = userService.getUser(id);
        if (user != null) {
            user.setName(name);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            user.setPassword(password);
            boolean success = userService.updateUser(user);
            if (success) {
                logger.info("User with ID: {} successfully updated.", id);
                return ResponseEntity.ok().body("User with ID: " + id + " successfully updated.");
            } else {
                logger.warn("Could not update user with ID: {}.", id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not update user with ID: " + id);
            }
        }
        logger.warn("User with ID: {} not found.", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID: " + id + " not found.");
    }
}

