package org.example.datnbbook.controller;

import lombok.RequiredArgsConstructor;
//import org.example.datnbbook.dto.DotGiamGiaRequest;
import org.example.datnbbook.dto.DotGiamGiaRequest;
import org.example.datnbbook.model.DotGiamGia;
import org.example.datnbbook.model.DotGiamGiaBoSachChiTiet;
import org.example.datnbbook.model.DotGiamGiaChiTiet;
import org.example.datnbbook.service.DotGiamGiaService;
import org.example.datnbbook.service.PhieuGiamGiaService;
import org.example.datnbbook.model.PhieuGiamGia;
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
    private final PhieuGiamGiaService phieuGiamGiaService;

    @GetMapping
    public ResponseEntity<List<DotGiamGia>> getAll() {
        return ResponseEntity.ok(dotGiamGiaService.getAll());
    }

    // Lấy danh sách voucher công khai khả dụng cho POS
    @GetMapping("/available-for-pos")
    public ResponseEntity<List<PhieuGiamGia>> getAvailableVouchersForPos(@RequestParam(required = false) Double totalAmount) {
        return ResponseEntity.ok(phieuGiamGiaService.getPublicVouchers(totalAmount));
    }

    // Lấy voucher cá nhân của khách hàng
    @GetMapping("/personal-vouchers/{customerId}")
    public ResponseEntity<List<PhieuGiamGia>> getPersonalVouchersForCustomer(@PathVariable Long customerId, @RequestParam(required = false) Double totalAmount) {
        return ResponseEntity.ok(phieuGiamGiaService.getPersonalVouchersForCustomer(customerId, totalAmount));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DotGiamGia> getById(@PathVariable Integer id) {
        return dotGiamGiaService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Trừ số lượng voucher sau thanh toán
    @PutMapping("/{id}/deduct-quantity")
    public ResponseEntity<DotGiamGia> deductVoucherQuantity(@PathVariable Integer id) {
        return ResponseEntity.ok(dotGiamGiaService.deductVoucherQuantity(id));
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<DotGiamGiaChiTiet>> getDiscountProducts(@PathVariable Integer id) {
        List<DotGiamGiaChiTiet> products = dotGiamGiaService.getDiscountProducts(id);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}/book-sets")
    public ResponseEntity<List<DotGiamGiaBoSachChiTiet>> getDiscountBookSets(@PathVariable Integer id) {
        List<DotGiamGiaBoSachChiTiet> bookSets = dotGiamGiaService.getDiscountBookSets(id);
        return ResponseEntity.ok(bookSets);
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
    
    // Trừ số lượng voucher sau thanh toán
    @PutMapping("/deduct-voucher/{id}")
    public ResponseEntity<PhieuGiamGia> deductVoucherQuantity(@PathVariable Long id) {
        PhieuGiamGia updatedVoucher = phieuGiamGiaService.deductVoucherQuantity(id);
        return ResponseEntity.ok(updatedVoucher);
    }
}