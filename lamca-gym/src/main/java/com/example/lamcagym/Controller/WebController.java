package com.example.lamcagym.Controller;

import com.example.lamcagym.Entity.Login;
import com.example.lamcagym.Entity.User;
import com.example.lamcagym.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {
    @Autowired
    UserService userService;
    Login login = new Login();
    @GetMapping("/")
    public String homePage(){
        return "index";
    }
    @GetMapping("/index.html")
    public String indexPage(){
        return "index";
    }
    @GetMapping("/who-are-we.html")
    public String whoAreWePage(){
        return "who-are-we";
    }
    @GetMapping("/ourgym.html")
    public String ourgymPage(){
        return "ourgym";
    }
    @GetMapping("/membership.html")
    public String membershipPage(){
        return "membership";
    }
    @GetMapping("/personalpage.html")
    public String personalPage(){
        return "personalpage";
    }
    @GetMapping("/trainer.html")
    public String trainerPage(){
        return "trainer";
    }
    @GetMapping("/login.html")
    public String loginPage(){
        return "Login";
    }
    @PostMapping("/login")
    public String loginRequest(Model model, String email, String password){
        User user = getUserByEmail(email);
        if(user != null) {
            System.out.println(user.getEmail() + " " + user.getPassword());
            if(user.getPassword().equals(password)){
                model.addAttribute("message","Successfully login " + user.getName());
                //return "calender";
                return"personalpage";
            }
            else {
                model.addAttribute("message", "Invalid password");
            }
        } else {
            model.addAttribute("message", "User not found");
        }
        return "Login";
    }
    private User getUserByEmail(String email){
        User user = userService.getUserByEmail(email);
        if (user != null) {
            return user;
        }
        return null;
    }
}
