package org.example.datnbbook.controller;

import org.example.datnbbook.model.HoaDonChiTiet;
import org.example.datnbbook.model.LichSuHoaDon;
import org.example.datnbbook.service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api")
public class HoaDonChiTietController {
    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    @GetMapping("/hoa-don-chi-tiet")
    public List<HoaDonChiTiet> getHoaDonChiTiet() {
        return hoaDonChiTietService.getAllHoaDonChiTiet();
    }
}
