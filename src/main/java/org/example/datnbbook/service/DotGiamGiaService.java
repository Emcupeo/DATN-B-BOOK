package org.example.datnbbook.service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DotGiamGiaService {
    @RequestMapping("/dot-giam-gia")
    public String dotGiamGia() {
        return "dot-giam-gia";
    }
}
