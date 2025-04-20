package org.example.datnbbook.controller;

import lombok.RequiredArgsConstructor;
import org.example.datnbbook.dto.DotGiamGiaRequest;
import org.example.datnbbook.model.DotGiamGia;
import org.example.datnbbook.service.DotGiamGiaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/discounts")
public class DotGiamGiaController {

    private final DotGiamGiaService dotGiamGiaService;

    @GetMapping
    public ResponseEntity<List<DotGiamGia>> getAll() {
        return ResponseEntity.ok(dotGiamGiaService.getAll());
    }

    @PostMapping
    public ResponseEntity<DotGiamGia> create(@RequestBody DotGiamGiaRequest request) {
        DotGiamGia dotGiamGia = new DotGiamGia();
        dotGiamGia.setTenDotGiamGia(request.getTenDotGiamGia());
        dotGiamGia.setLoaiGiamGia("Phần trăm");
        dotGiamGia.setSoPhanTramGiam(request.getSoPhanTramGiam());
        dotGiamGia.setGiaTriGiam(null);
        dotGiamGia.setMoTa(null);
        dotGiamGia.setNgayBatDau(request.getNgayBatDau());
        dotGiamGia.setNgayKetThuc(request.getNgayKetThuc());
        dotGiamGia.setTrangThai(true);

        DotGiamGia created = dotGiamGiaService.create(dotGiamGia, request.getSelectedProducts());
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DotGiamGia> update(@PathVariable Integer id, @RequestBody DotGiamGiaRequest request) {
        DotGiamGia dotGiamGia = new DotGiamGia();
        dotGiamGia.setId(id);
        dotGiamGia.setTenDotGiamGia(request.getTenDotGiamGia());
        dotGiamGia.setLoaiGiamGia("Phần trăm");
        dotGiamGia.setSoPhanTramGiam(request.getSoPhanTramGiam());
        dotGiamGia.setGiaTriGiam(null);
        dotGiamGia.setMoTa(null);
        dotGiamGia.setNgayBatDau(request.getNgayBatDau());
        dotGiamGia.setNgayKetThuc(request.getNgayKetThuc());
        dotGiamGia.setTrangThai(true);

        DotGiamGia updated = dotGiamGiaService.update(dotGiamGia, request.getSelectedProducts());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        dotGiamGiaService.delete(id);
        return ResponseEntity.ok().build();
    }
}