package org.example.datnbbook.controller;

import org.example.datnbbook.model.LichSuHoaDon;
import org.example.datnbbook.service.LichSuHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api")
public class LichSuaHoaDonController {
    @Autowired
    private LichSuHoaDonService lichSuHoaDonService;

    @GetMapping("/lich-su-hoa-don")
    public List<LichSuHoaDon> getLichSuHoaDon() {
        return lichSuHoaDonService.getAllLichSuHoaDon();
    }

    @GetMapping("/lich-su-hoa-don/{id}")
    public ResponseEntity<LichSuHoaDon> getLichSuHoaDonById(@PathVariable Integer id) {
        return ResponseEntity.ok(lichSuHoaDonService.getLichSuHoaDonById(id));
    }
}
