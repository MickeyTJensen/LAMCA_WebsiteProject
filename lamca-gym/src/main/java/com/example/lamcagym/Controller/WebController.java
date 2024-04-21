package com.example.lamcagym.Controller;

import com.example.lamcagym.Entity.Login;
import com.example.lamcagym.Entity.User;
import com.example.lamcagym.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller // Indikerar att detta är en kontrollerkomponent för webbanvändargränssnittet
public class WebController {
    @Autowired
    UserService userService; // En autowired instans av UserService för att hantera användardata
    Login login = new Login(); // En instans av Login för att hantera inloggningsuppgifter

    // Request mapping för rotvägen ("/") som returnerar index-sidan
    @GetMapping("/")
    public String homePage(){
        return "index";
    }

    // Request mapping för index-sidan som returnerar index-sidan
    @GetMapping("/index.html")
    public String indexPage(){
        return "index";
    }

    // Request mapping för who-are-we sidan som returnerar who-are-we sidan
    @GetMapping("/who-are-we.html")
    public String whoAreWePage(){
        return "who-are-we";
    }

    // Request mapping för ourgym sidan som returnerar ourgym sidan
    @GetMapping("/ourgym.html")
    public String ourgymPage(){
        return "ourgym";
    }

    // Request mapping för membership sidan som returnerar membership sidan
    @GetMapping("/membership.html")
    public String membershipPage(){
        return "membership";
    }

    // Request mapping för personalpage sidan som returnerar personalpage sidan
    @GetMapping("/personalpage.html")
    public String personalPage(){
        return "personalpage";
    }

    // Request mapping för trainer sidan som returnerar trainer sidan
    @GetMapping("/trainer.html")
    public String trainerPage(){
        return "trainer";
    }

    // Request mapping för login sidan som returnerar login sidan
    @GetMapping("/login.html")
    public String loginPage(){
        return "Login";
    }

}