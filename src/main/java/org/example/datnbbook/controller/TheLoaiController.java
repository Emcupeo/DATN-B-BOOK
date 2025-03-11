package org.example.datnbbook.controller;

import org.example.datnbbook.dto.TheLoaiDTO;
import org.example.datnbbook.model.TheLoai;
import org.example.datnbbook.service.TheLoaiService;
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
@RequestMapping("/api/the-loai")
public class TheLoaiController {


    @Autowired
    TheLoaiService theLoaiService;

    @GetMapping
    public List<TheLoai> getAlTheLoai() {
        return theLoaiService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheLoai> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(theLoaiService.getById(id));
    }

    @PostMapping
    public ResponseEntity<TheLoai> create(@RequestBody TheLoai theLoai) {
        return ResponseEntity.ok(theLoaiService.create(theLoai));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TheLoaiDTO> update(@PathVariable Integer id, @RequestBody TheLoaiDTO theLoai) {
        return ResponseEntity.ok(theLoaiService.update(id, theLoai));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        theLoaiService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<TheLoai>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(theLoaiService.search(keyword));
    }


}

