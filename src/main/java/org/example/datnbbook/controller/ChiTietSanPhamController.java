package org.example.datnbbook.controller;


import lombok.RequiredArgsConstructor;
import org.example.datnbbook.dto.ChiTietSanPhamDTO;
import org.example.datnbbook.model.ChiTietSanPham;
import org.example.datnbbook.model.SanPham;
import org.example.datnbbook.service.ChiTietSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chi-tiet-san-pham")
public class ChiTietSanPhamController {

    private final ChiTietSanPhamService chiTietSanPhamService;

    @GetMapping("/all")
    public ResponseEntity<List<ChiTietSanPham>> getAll() {
        return ResponseEntity.ok(chiTietSanPhamService.getAll());
    }

    @PostMapping
    public ResponseEntity<ChiTietSanPham> create(@RequestBody ChiTietSanPham chiTietSanPham) {
        ChiTietSanPham created = chiTietSanPhamService.create(chiTietSanPham);
        return ResponseEntity.ok(created);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ChiTietSanPham> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(chiTietSanPhamService.getById(id));
    }

    @GetMapping("/ma/{ma}")
    public ResponseEntity<ChiTietSanPham> getByMa(@PathVariable String ma) {
        return ResponseEntity.ok(chiTietSanPhamService.getByMa(ma));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChiTietSanPham> update(@PathVariable Integer id, @RequestBody ChiTietSanPham chiTietSanPham) {
        chiTietSanPham.setId(id); // Đảm bảo ID khớp với path variable
        ChiTietSanPham updated = chiTietSanPhamService.update(id, chiTietSanPham);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        chiTietSanPhamService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ChiTietSanPham>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(chiTietSanPhamService.search(keyword));
    }

    @GetMapping("/by-san-pham/{idSanPham}")
    public ResponseEntity<List<ChiTietSanPham>> getBySanPhamId(@PathVariable Integer idSanPham) {

        List<ChiTietSanPham> chiTietSanPhams = chiTietSanPhamService.getBySanPhamId(idSanPham);
        if (chiTietSanPhams.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(chiTietSanPhams);
    }


}
