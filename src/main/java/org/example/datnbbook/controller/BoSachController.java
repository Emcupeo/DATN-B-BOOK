package org.example.datnbbook.controller;

import org.example.datnbbook.model.BoSach;
import org.example.datnbbook.service.BoSachService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api/bo-sach")
public class BoSachController {
    private static final Logger logger = LoggerFactory.getLogger(BoSachController.class);

    @Autowired
    private BoSachService boSachService;



    @GetMapping
    public ResponseEntity<List<BoSach>> getAll() {
        List<BoSach> boSachs = boSachService.getAll();
        return ResponseEntity.ok(boSachs);
    }

    @PostMapping
    public ResponseEntity<BoSach> create(@RequestBody BoSach boSach) {
        logger.info("[INFO] Creating new BoSach: {}", boSach.getTenBoSach());
        BoSach createdBoSach = boSachService.create(boSach);
        logger.debug("[DEBUG] Created BoSach: {}", createdBoSach);
        return ResponseEntity.ok(createdBoSach);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoSach> getById(@PathVariable Integer id) {
        BoSach boSach = boSachService.getById(id);
        return ResponseEntity.ok(boSach);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boSachService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<BoSach>> search(@RequestParam String keyword) {
        List<BoSach> boSachs = boSachService.search(keyword);
        return ResponseEntity.ok(boSachs);
    }
}