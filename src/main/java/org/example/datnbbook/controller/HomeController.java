package org.example.datnbbook.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

@RequestMapping("/b-book")
public class HomeController {
    @GetMapping("/home")
    public String home() {

        return "Địt con bà m";
    }
}
