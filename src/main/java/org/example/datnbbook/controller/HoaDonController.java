package org.example.datnbbook.controller;

import org.example.datnbbook.model.HoaDon;
import org.example.datnbbook.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api")
public class HoaDonController{
    @Autowired
    private HoaDonService hoaDonService;

    @GetMapping("/hoa-don")
    public List<HoaDon> getHoaDon(){
        return hoaDonService.getAllHoaDon();
    }

    @GetMapping("/hoa-don/{id}")
    public HoaDon getHoaDon(@PathVariable int id){
        return hoaDonService.getHoaDonById(id);
    }
}
