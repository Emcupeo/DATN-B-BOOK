package org.example.datnbbook.controller;

import org.example.datnbbook.dto.NhaXuatBanDTO;
import org.example.datnbbook.model.NhaXuatBan;
import org.example.datnbbook.service.NhaXuatBanService;
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
@RequestMapping("/api/nha-xuat-ban")
public class NhaXuatBanController {


    @Autowired
    NhaXuatBanService nhaXuatBanService;

    @GetMapping
    public List<NhaXuatBan> getAlNhaXuatBan() {
        return nhaXuatBanService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NhaXuatBan> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(nhaXuatBanService.getById(id));
    }

    @PostMapping
    public ResponseEntity<NhaXuatBan> create(@RequestBody NhaXuatBan nhaXuatBan) {
        return ResponseEntity.ok(nhaXuatBanService.create(nhaXuatBan));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NhaXuatBanDTO> update(@PathVariable Integer id, @RequestBody NhaXuatBanDTO nhaXuatBan) {
        return ResponseEntity.ok(nhaXuatBanService.update(id, nhaXuatBan));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        nhaXuatBanService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<NhaXuatBan>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(nhaXuatBanService.search(keyword));
    }


}

