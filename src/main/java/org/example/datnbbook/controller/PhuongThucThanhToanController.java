package org.example.datnbbook.controller;

import org.example.datnbbook.model.PhuongThucThanhToan;
import org.example.datnbbook.service.PhuongThucThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api")
public class PhuongThucThanhToanController {
    @Autowired
    private PhuongThucThanhToanService phuongThucThanhToanService;

    @GetMapping("/phuong-thuc-thanh-toan/{id}")
    public PhuongThucThanhToan phuongThucThanhToan(@PathVariable int id) {
        return phuongThucThanhToanService.getOne(id);
    }
}
