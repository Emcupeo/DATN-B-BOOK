package org.example.datnbbook.controller;

import org.example.datnbbook.dto.LoaiBiaDTO;
import org.example.datnbbook.model.LoaiBia;
import org.example.datnbbook.service.LoaiBiaService;
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
@RequestMapping("/api/loai-bia")
public class LoaiBiaController {


    @Autowired
    LoaiBiaService loaiBiaService;

    @GetMapping
    public List<LoaiBia> getAlLoaiBia() {
        return loaiBiaService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoaiBia> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(loaiBiaService.getById(id));
    }

    @PostMapping
    public ResponseEntity<LoaiBia> create(@RequestBody LoaiBia loaiBia) {
        return ResponseEntity.ok(loaiBiaService.create(loaiBia));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoaiBiaDTO> update(@PathVariable Integer id, @RequestBody LoaiBiaDTO loaiBia) {
        return ResponseEntity.ok(loaiBiaService.update(id, loaiBia));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        loaiBiaService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<LoaiBia>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(loaiBiaService.search(keyword));
    }


}

