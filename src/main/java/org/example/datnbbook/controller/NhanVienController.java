package org.example.datnbbook.controller;

import org.example.datnbbook.dto.NhanVienDTO;
import org.example.datnbbook.service.NhanVienService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api/nhan-vien")
public class NhanVienController {
    private final NhanVienService nhanVienService;

    public NhanVienController(NhanVienService nhanVienService) {
        this.nhanVienService = nhanVienService;
    }

    @GetMapping
    public ResponseEntity<List<NhanVienDTO>> getAll() {
        return ResponseEntity.ok(nhanVienService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NhanVienDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(nhanVienService.getById(id));
    }

    @PostMapping
    public ResponseEntity<NhanVienDTO> create(@RequestBody NhanVienDTO nhanVienDTO) {
        return ResponseEntity.ok(nhanVienService.create(nhanVienDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NhanVienDTO> update(@PathVariable Integer id, @RequestBody NhanVienDTO nhanVienDTO) {
        return ResponseEntity.ok(nhanVienService.update(id, nhanVienDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        nhanVienService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<NhanVienDTO>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(nhanVienService.search(keyword));
    }
}
