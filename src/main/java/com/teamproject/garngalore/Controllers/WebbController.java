package com.teamproject.garngalore.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebbController {
    @GetMapping("/info")
    public String info() {
        return "InfoPage";
    }
    @GetMapping("/customerService")
    public String contact() {
        return "KundtjänstPage";
    }
}
