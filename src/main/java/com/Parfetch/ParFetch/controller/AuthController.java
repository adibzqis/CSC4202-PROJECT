package com.Parfetch.ParFetch.controller;

import com.Parfetch.ParFetch.model.Sender;
import com.Parfetch.ParFetch.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final SenderService senderService;

    @Autowired
    public AuthController(SenderService senderService) {
        this.senderService = senderService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("sender", new Sender()); // ✅ required for Thymeleaf form
        return "register";
    }

    @PostMapping("/register")
    public String registerSender(@ModelAttribute Sender sender, Model model) {
        if (senderService.findByPhone(sender.getPhone()) != null) {
            model.addAttribute("error", "Phone number already registered");
            return "register";
        }
        sender.setRole("ROLE_SENDER"); // ✅ Make sure role is set
        senderService.registerSender(sender);
        return "redirect:/login?registered";
    }
}
