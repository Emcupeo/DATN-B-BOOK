package org.example.datnbbook.controller;

import org.example.datnbbook.dto.DiaChiDTO;
import org.example.datnbbook.service.DiaChiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dia-chi")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class DiaChiController {

    @Autowired
    private DiaChiService diaChiService;

    @GetMapping("/khach-hang/{khachHangId}")
    public ResponseEntity<List<DiaChiDTO>> getAllByKhachHangId(@PathVariable Long khachHangId) {
        return ResponseEntity.ok(diaChiService.getAllByKhachHangId(khachHangId));
    }

    @GetMapping("/khach-hang/{khachHangId}/mac-dinh")
    public ResponseEntity<DiaChiDTO> getDefaultAddress(@PathVariable Long khachHangId) {
        return ResponseEntity.ok(diaChiService.getDefaultAddress(khachHangId));
    }

    @PostMapping("/khach-hang/{khachHangId}")
    public ResponseEntity<DiaChiDTO> create(@PathVariable Long khachHangId, @RequestBody DiaChiDTO diaChiDTO) {
        return ResponseEntity.ok(diaChiService.create(khachHangId, diaChiDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiaChiDTO> update(@PathVariable Long id, @RequestBody DiaChiDTO diaChiDTO) {
        return ResponseEntity.ok(diaChiService.update(id, diaChiDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        diaChiService.delete(id);
        return ResponseEntity.ok().build();
    }
} 