package org.example.datnbbook.controller;

import org.example.datnbbook.dto.DanhMucDTO;
import org.example.datnbbook.model.DanhMuc;
import org.example.datnbbook.service.DanhMucService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api/danh-muc")
public class DanhMucController {

    @Autowired
    DanhMucService danhMucService;

    @GetMapping
    public List<DanhMuc> getAllDanhMuc() {
        return danhMucService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DanhMuc> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(danhMucService.getById(id));
    }

    @PostMapping
    public ResponseEntity<DanhMuc> create(@RequestBody DanhMuc danhMuc) {
        return ResponseEntity.ok(danhMucService.create(danhMuc));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DanhMucDTO> update(@PathVariable Integer id, @RequestBody DanhMucDTO danhMuc) {
        return ResponseEntity.ok(danhMucService.update(id, danhMuc));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        danhMucService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<DanhMuc>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(danhMucService.search(keyword));
    }

    @GetMapping("/active")
    public ResponseEntity<List<DanhMuc>> getActiveCategories() {
        return ResponseEntity.ok(danhMucService.getAll());
    }
} 