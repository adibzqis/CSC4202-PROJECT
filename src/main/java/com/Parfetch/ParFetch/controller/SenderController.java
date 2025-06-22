package com.Parfetch.ParFetch.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//@Controller
//@RequestMapping("/sender")
//public class SenderController {

    //@GetMapping("/student-home")
    //public String studentHome(Authentication auth, Model model) {
        //model.addAttribute("username", auth.getName()); // Optional display
       // return "student-home";
    //}

@Controller
public class SenderController {

    @GetMapping("/student-home")
    public String studentHome(Model model, Authentication auth) {
        model.addAttribute("username", auth.getName());
        return "student-home"; // Ensure student-home.html exists
    }
}


