package org.example.datnbbook.controller;

import org.example.datnbbook.dto.NguoiDichDTO;
import org.example.datnbbook.model.NguoiDich;
import org.example.datnbbook.service.NguoiDichService;
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
@RequestMapping("/api/nguoi-dich")
public class NguoiDichController {


    @Autowired
    NguoiDichService nguoiDichService;

    @GetMapping
    public List<NguoiDich> getAlNguoiDich() {
        return nguoiDichService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NguoiDich> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(nguoiDichService.getById(id));
    }

    @PostMapping
    public ResponseEntity<NguoiDich> create(@RequestBody NguoiDich nguoiDich) {
        return ResponseEntity.ok(nguoiDichService.create(nguoiDich));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NguoiDichDTO> update(@PathVariable Integer id, @RequestBody NguoiDichDTO nguoiDich) {
        return ResponseEntity.ok(nguoiDichService.update(id, nguoiDich));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        nguoiDichService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<NguoiDich>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(nguoiDichService.search(keyword));
    }


}

