package org.example.datnbbook.controller;

import org.example.datnbbook.model.KhachHang;
import org.example.datnbbook.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api")
public class KhachHangController {
    @Autowired
    private KhachHangService khachHangService;

    @GetMapping("/khach-hang")
    public List<KhachHang> getAllKhachHang() {
        return khachHangService.getAllKhachHang();
    }

}
