package org.example.datnbbook.controller;

import org.example.datnbbook.dto.TacGiaDTO;
import org.example.datnbbook.model.TacGia;
import org.example.datnbbook.service.TacGiaService;
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
@RequestMapping("/api/tac-gia")
public class TacGiaController {


    @Autowired
    TacGiaService tacGiaService;

    @GetMapping
    public List<TacGia> getAlTacGia() {
        return tacGiaService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TacGia> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(tacGiaService.getById(id));
    }

    @PostMapping
    public ResponseEntity<TacGia> create(@RequestBody TacGia tacGia) {
        return ResponseEntity.ok(tacGiaService.create(tacGia));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TacGiaDTO> update(@PathVariable Integer id, @RequestBody TacGiaDTO tacGia) {
        return ResponseEntity.ok(tacGiaService.update(id, tacGia));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tacGiaService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<TacGia>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(tacGiaService.search(keyword));
    }


}

