package org.example.datnbbook.controller;

import org.example.datnbbook.dto.KhachHangDTO;
import org.example.datnbbook.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khach-hang")
@CrossOrigin("*")
public class KhachHangController {

    @Autowired
    private KhachHangService khachHangService;

    @GetMapping
    public ResponseEntity<List<KhachHangDTO>> getAll() {
        return ResponseEntity.ok(khachHangService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KhachHangDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(khachHangService.getById(id));
    }

    @PostMapping
    public ResponseEntity<KhachHangDTO> create(@RequestBody KhachHangDTO khachHangDTO) {
        return ResponseEntity.ok(khachHangService.create(khachHangDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KhachHangDTO> update(@PathVariable Long id, @RequestBody KhachHangDTO khachHangDTO) {
        return ResponseEntity.ok(khachHangService.update(id, khachHangDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        khachHangService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<KhachHangDTO>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(khachHangService.search(keyword));
    }
}
