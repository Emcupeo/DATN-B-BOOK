package org.example.datnbbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DotGiamGiaController {
    @RequestMapping("/dot-giam-gia")
    public String dotGiamGia() {
        return "dot-giam-gia";
    }
}
