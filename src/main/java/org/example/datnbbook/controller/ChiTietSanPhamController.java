package org.example.datnbbook.controller;


import org.example.datnbbook.model.ChiTietSanPham;
import org.example.datnbbook.service.ChiTietSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api")
public class ChiTietSanPhamController {
    @Autowired
    private ChiTietSanPhamService chiTietSanPhamService;

    @GetMapping("/chi-tiet-san-pham")
    public List<ChiTietSanPham> getAllChiTietSanPham() {
        return chiTietSanPhamService.getAllChiTietSanPham();
    }

}
