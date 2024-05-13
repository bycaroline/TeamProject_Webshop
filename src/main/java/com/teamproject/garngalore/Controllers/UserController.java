package com.teamproject.garngalore.Controllers;

import com.teamproject.garngalore.Models.Cart;
import com.teamproject.garngalore.Models.User;
import com.teamproject.garngalore.Repositories.CartRepository;
import com.teamproject.garngalore.Repositories.UserRepository;
import com.teamproject.garngalore.Services.CartService;
import com.teamproject.garngalore.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController {

    // En instans av UserService injiceras här för att hantera användarrelaterad logik
    private final UserService userService;

    @Autowired
    private UserRepository userRepository;
    // Konstruktör för att injicera UserService genom beroendeinjektion
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Metod för att visa formulärsidan
    @GetMapping("/registerUser")
    public String showUserForm(Model model) {
        model.addAttribute("user", new User());
        return "RegisterPage";
    }

    // Metod för att hantera inkommande POST-begäranden från formuläret
    @PostMapping("/registerUser")
    public String saveUser(User user, Model model) {
        // Kontrollera om e-postadressen redan finns i databasen
        User existingUser = userService.getUserByEmail(user.getEmail());
        if (existingUser != null) {
            // E-postadressen finns redan, visa felmeddelande
            model.addAttribute("error", "E-postadressen finns redan i databasen. Vänligen välj en annan e-postadress.");
            return "RegisterPage";
        }

        // Sparar användaren om e-postadressen inte finns redan
        userRepository.save(user);

        // Meddelande för lyckad registrering
        model.addAttribute("success", "Ditt konto hos Garn Galore är nu skapat.");

        // Returnera samma vy för registrering
        return "RegisterPage";
    }

    // Metod för att visa inloggningsformuläret
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        // Lägg till ett tomt användarobjekt i modellen för formuläret
        model.addAttribute("user", new User());
        // Returnera vyn för inloggningsformuläret
        return "LogInPage";
    }

    // Metod för att hantera inloggning av användare
    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            Model model,
                            HttpSession session) {
        // Hämta användaren från databasen med hjälp av UserService
        User existingUser = userService.getUserByEmail(email);
        // Om användaren inte finns, lägg till ett felmeddelande och visa inloggningsformuläret igen
        if (existingUser == null) {
            model.addAttribute("error", "E-postadressen finns inte i vår databas.");
            return "LogInPage";
        }

        // Om lösenordet inte matchar, lägg till ett felmeddelande och visa inloggningsformuläret igen
        if (!existingUser.getPassword().equals(password)) {
            model.addAttribute("error", "Fel lösenord.");
            return "LogInPage";
        }

        // Lagra användarens ID i sessionen för att hålla dem inloggade
        session.setAttribute("loggedInUserId", existingUser.getId());
        // Skriv ut i konsolen för att testa att inloggningen har lyckats
        System.out.println("Användare med ID " + existingUser.getId() + " loggades in.");

        // Omdirigera till startsidan efter inloggningen
        return "redirect:/";
    }

    // Metod för att logga ut användaren
    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.setAttribute("loggedInUserId", null);
        // Skriv ut i konsolen för att indikera att användaren har loggats ut
        System.out.println("Användaren har loggats ut.");
        // Omdirigera till startsidan efter utloggningen
        return "redirect:/";
    }

    @GetMapping("/myPage")
    public String myPage(Model model, HttpSession session) {
        // Hämta användar-ID från sessionen
        String loggedInUserId = (String) session.getAttribute("loggedInUserId");

        // Kontrollera om användaren är inloggad
        if (loggedInUserId != null) {
            // Hämta den inloggade användaren från databasen
            User loggedInUser = userService.getUserById(loggedInUserId); // Anpassa detta beroende på din implementation

            // Lägg till den inloggade användaren i modellen för att skicka till vyn
            model.addAttribute("loggedInUser", loggedInUser);

            // Returnera vyn för Min sida
            return "MinSidaPage";
        } else {
            // Om användaren inte är inloggad, vidarebefordra till inloggningssidan eller visa ett felmeddelande
            return "redirect:/login"; // Anpassa detta beroende på din implementation
        }
    }}
