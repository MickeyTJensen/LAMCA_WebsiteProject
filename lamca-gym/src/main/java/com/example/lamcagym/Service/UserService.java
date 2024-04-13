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
        // Här sparar du bara lösenordet som det är, utan att hash:a det.
        userRepository.save(user);
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

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    public ArrayList<User> getAll() {
        return (ArrayList<User>) userRepository.findAll();
    }
}

