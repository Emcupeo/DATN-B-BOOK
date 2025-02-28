package org.example.datnbbook.controller;

import org.example.datnbbook.model.SanPham;
import org.example.datnbbook.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api")
public class SanPhamController {
    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping("/san-pham")
    public List<SanPham> getAllSanPham() {
        return sanPhamService.getAllSanPham();
    }


}
