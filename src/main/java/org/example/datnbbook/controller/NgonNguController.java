package org.example.datnbbook.controller;

import org.example.datnbbook.dto.NgonNguDTO;
import org.example.datnbbook.model.NgonNgu;
import org.example.datnbbook.service.NgonNguService;
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
@RequestMapping("/api/ngon-ngu")


public class NgonNguController {
    @Autowired
    NgonNguService ngonNguService;

    @GetMapping
    public List<NgonNgu> getAlNgonNgu() {
        return ngonNguService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NgonNgu> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ngonNguService.getById(id));
    }

    @PostMapping
    public ResponseEntity<NgonNgu> create(@RequestBody NgonNgu ngonNgu) {
        return ResponseEntity.ok(ngonNguService.create(ngonNgu));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NgonNguDTO> update(@PathVariable Integer id, @RequestBody NgonNguDTO ngonNgu) {
        return ResponseEntity.ok(ngonNguService.update(id, ngonNgu));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        ngonNguService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<NgonNgu>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(ngonNguService.search(keyword));
    }


}
