package org.example.datnbbook.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ctsp")
public class ChiTietSanPhamController {
    @GetMapping("view")
    public String view() {
        return "chiTietSanPham";
    }

}
