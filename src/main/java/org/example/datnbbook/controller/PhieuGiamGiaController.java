package org.example.datnbbook.controller;

import org.example.datnbbook.model.PhieuGiamGia;
import org.example.datnbbook.service.PhieuGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/phieu-giam-gia")
@CrossOrigin(origins = "http://localhost:3000")
public class PhieuGiamGiaController {
    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;

    // Lấy danh sách phiếu giảm giá
    @GetMapping
    public ResponseEntity<List<PhieuGiamGia>> getAll() {
        return ResponseEntity.ok(phieuGiamGiaService.getAll());
    }

    // Lấy chi tiết phiếu giảm giá theo ID
    @GetMapping("/{id}")
    public ResponseEntity<PhieuGiamGia> getById(@PathVariable Long id) {
        return phieuGiamGiaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Thêm mới phiếu giảm giá
    @PostMapping
    public ResponseEntity<PhieuGiamGia> create(@RequestBody PhieuGiamGia phieuGiamGia) {
        return ResponseEntity.ok(phieuGiamGiaService.save(phieuGiamGia));
    }

    // Cập nhật phiếu giảm giá
    @PutMapping("/{id}")
    public ResponseEntity<PhieuGiamGia> update(@PathVariable Long id, @RequestBody PhieuGiamGia phieuGiamGia) {
        if (!phieuGiamGiaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        phieuGiamGia.setId(id);
        return ResponseEntity.ok(phieuGiamGiaService.save(phieuGiamGia));
    }

    // Xóa phiếu giảm giá
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!phieuGiamGiaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        phieuGiamGiaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
