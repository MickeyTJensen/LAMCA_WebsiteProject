package com.example.lamcagym.Controller;

import com.example.lamcagym.Entity.Booking;
import com.example.lamcagym.Entity.Login;
import com.example.lamcagym.Entity.Session;
import com.example.lamcagym.Entity.User;
import com.example.lamcagym.Service.BookingService;
import com.example.lamcagym.Service.SessionService;
import com.example.lamcagym.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {
    @Autowired
    UserService userService;
    @Autowired
    SessionService sessionService;
    @Autowired
    BookingService bookingService;

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
    @GetMapping("/bookings")
    public String bookPage(){
        return personalPage();
    }
    @PostMapping("/bookings")
    public String bookPass(Integer sessionId,Integer userId, Model model){
        // Anta att du tar emot sessionens och användarens ID från klienten
        // Du behöver sedan hämta sessionen och användaren från databasen baserat på ID:erna
        Session session = sessionService.getSession(sessionId);
        User user = userService.getUser(userId);

        if(session != null && user != null) {
            // Skapa en ny bokning och spara den i databasen
            Booking booking = new Booking();
            booking.setSession(session);
            booking.setUser(user);
            // Sätt andra attribut för bokningen om det behövs
            //bookingService.createBooking(booking);
            // Lägg till meddelande i modellen för att indikera att bokningen lyckades
            model.addAttribute("message", "Booking successful!");
        } else {
            // Lägg till meddelande i modellen för att indikera att sessionen eller användaren inte hittades
            model.addAttribute("message", "Session or user not found!");
        }

        // Returnera vyn eller URL:en som du vill visa efter att bokningen har utförts
        return "personalpage.html";
    }

    private User getUserByEmail(String email){
        User user = userService.getUserByEmail(email);
        if (user != null) {
            return user;
        }
        return null;
    }
}
