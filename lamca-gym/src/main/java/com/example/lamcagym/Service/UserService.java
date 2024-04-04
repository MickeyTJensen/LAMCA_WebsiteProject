package com.example.lamcagym.Service;

import com.example.lamcagym.Repository.UserRepository;
import com.example.lamcagym.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean createUser(User user) {
        // Skapa en ny rad i databasen med save().
        // Det returnerar det nyligen skapade objektet i databasen, vilket kan
        // användas för att kontrollera vilket id som just genererats för objektet.
        User newUser = userRepository.save(user);

        // Eftersom vi använder Lombok och har bytt till camelCase i Java-koden,
        // uppdatera så att den använder getUserId() istället.
        System.out.println("Got id " + newUser.getUserId() + "!");

        return true;
    }

    public User getUser(int id) {
        if (userRepository.existsById(id)) {
            return userRepository.findById(id).get();
        }
        return null;
    }
    public User getUserByEmail(String email){
        if(userRepository.existsByEmail(email)){
            return userRepository.findByEmail(email);
        }
        return null;
    }

    public boolean deleteUser(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean updateUser(User user) {
        // Kontrollera om en användare med angivet ID finns
        if (userRepository.existsById(user.getUserId())) {
            // Uppdatera användaren i databasen
            userRepository.save(user);
            return true;
        }
        // Om användaren inte finns, returnera false
        return false;
    }

    public ArrayList<User> getAll() {
        return (ArrayList<User>) userRepository.findAll();
    }
}

