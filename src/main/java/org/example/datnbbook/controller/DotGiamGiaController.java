package org.example.datnbbook.controller;

import lombok.RequiredArgsConstructor;
//import org.example.datnbbook.dto.DotGiamGiaRequest;
import org.example.datnbbook.dto.DotGiamGiaRequest;
import org.example.datnbbook.model.DotGiamGia;
import org.example.datnbbook.service.DotGiamGiaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.datnbbook.dto.ActiveDiscountDetailDTO;

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

    // Lấy chi tiết giảm giá đang hoạt động cho một CTSP
    @GetMapping("/active-detail/{ctspId}")
    public ResponseEntity<ActiveDiscountDetailDTO> getActiveDetail(@PathVariable Integer ctspId) {
        var detail = dotGiamGiaService.getActiveDetail(ctspId);
        return ResponseEntity.ok(detail);
    }

    // Lấy chi tiết giảm giá đang hoạt động cho một bộ sách
    @GetMapping("/active-bosach-detail/{boSachId}")
    public ResponseEntity<ActiveDiscountDetailDTO> getActiveBoSachDetail(@PathVariable Integer boSachId) {
        var detail = dotGiamGiaService.getActiveBoSachDetail(boSachId);
        return ResponseEntity.ok(detail);
    }

    @PostMapping
    public ResponseEntity<DotGiamGia> create(@RequestBody DotGiamGiaRequest request) {
        DotGiamGia dotGiamGia = new DotGiamGia();
        dotGiamGia.setTenDotGiamGia(request.getTenDotGiamGia());
        dotGiamGia.setLoaiGiamGia(request.getLoaiGiamGia()); // Lấy từ request
        dotGiamGia.setSoPhanTramGiam(request.getLoaiGiamGia().equals("Phần trăm") ? request.getSoPhanTramGiam() : null);
        dotGiamGia.setGiaTriGiam(request.getLoaiGiamGia().equals("Tiền mặt") ? request.getGiaTriGiam() : null);
        dotGiamGia.setMoTa(null);
        dotGiamGia.setNgayBatDau(request.getNgayBatDau());
        dotGiamGia.setNgayKetThuc(request.getNgayKetThuc());
        dotGiamGia.setTrangThai(true);

        // Ưu tiên dùng chiTietSanPhamIds nếu có, fallback selectedProducts
        var ids = request.getChiTietSanPhamIds() != null && !request.getChiTietSanPhamIds().isEmpty()
                ? request.getChiTietSanPhamIds()
                : request.getSelectedProducts();
        
        // Lấy danh sách bộ sách nếu có
        var boSachIds = request.getBoSachIds();
        
        DotGiamGia created = dotGiamGiaService.create(dotGiamGia, ids, boSachIds);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DotGiamGia> update(@PathVariable Integer id, @RequestBody DotGiamGiaRequest request) {
        DotGiamGia dotGiamGia = new DotGiamGia();
        dotGiamGia.setId(id);
        dotGiamGia.setTenDotGiamGia(request.getTenDotGiamGia());
        dotGiamGia.setLoaiGiamGia(request.getLoaiGiamGia()); // Lấy từ request
        dotGiamGia.setSoPhanTramGiam(request.getLoaiGiamGia().equals("Phần trăm") ? request.getSoPhanTramGiam() : null);
        dotGiamGia.setGiaTriGiam(request.getLoaiGiamGia().equals("Tiền mặt") ? request.getGiaTriGiam() : null);
        dotGiamGia.setMoTa(null);
        dotGiamGia.setNgayBatDau(request.getNgayBatDau());
        dotGiamGia.setNgayKetThuc(request.getNgayKetThuc());
        dotGiamGia.setTrangThai(true);

        var ids = request.getChiTietSanPhamIds() != null && !request.getChiTietSanPhamIds().isEmpty()
                ? request.getChiTietSanPhamIds()
                : request.getSelectedProducts();
        DotGiamGia updated = dotGiamGiaService.update(dotGiamGia, ids);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        dotGiamGiaService.delete(id);
        return ResponseEntity.ok().build();
    }

    // Endpoint khôi phục giá sau khi hết hạn giảm giá
    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Integer id) {
        dotGiamGiaService.restoreOriginalPrices(id);
        return ResponseEntity.ok().build();
    }

    // Apply all discounts (both products and book sets)
    @PostMapping("/apply-all")
    public ResponseEntity<Void> applyAllDiscounts() {
        dotGiamGiaService.applyAllDiscounts();
        return ResponseEntity.ok().build();
    }

    // Revert all expired discounts
    @PostMapping("/revert-all")
    public ResponseEntity<Void> revertAllExpiredDiscounts() {
        dotGiamGiaService.revertAllExpiredDiscounts();
        return ResponseEntity.ok().build();
    }
}