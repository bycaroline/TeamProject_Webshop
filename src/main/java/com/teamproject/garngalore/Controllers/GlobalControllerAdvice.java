package com.teamproject.garngalore.Controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    //Denna controller ser till att hålla koll på om användaren är inloggad genom
    //hela sessionen medan denne bläddrar runt på hemsidan. En session är satt till 90 min.
    //Går att ändra i application.properties.
    @ModelAttribute("loggedIn")
    public boolean loggedIn(HttpSession session) {
        // Kontrollera om användaren är inloggad
        String loggedInUserId = (String) session.getAttribute("loggedInUserId");
        return loggedInUserId != null;
    }
}

