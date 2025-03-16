package org.example.datnbbook.controller;

import org.example.datnbbook.dto.SanPhamDTO;
import org.example.datnbbook.model.ChiTietSanPham;
import org.example.datnbbook.model.SanPham;
import org.example.datnbbook.service.ChiTietSanPhamService;
import org.example.datnbbook.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api/san-pham")
public class SanPhamController {
    private final SanPhamService sanPhamService;
    private final ChiTietSanPhamService chiTietSanPhamService;

    public SanPhamController(SanPhamService sanPhamService, ChiTietSanPhamService chiTietSanPhamService) {
        this.sanPhamService = sanPhamService;
        this.chiTietSanPhamService = chiTietSanPhamService;
    }

    @GetMapping
    public ResponseEntity<List<SanPham>> getAll() {
        return ResponseEntity.ok(sanPhamService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanPham> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(sanPhamService.getById(id));
    }

    @PostMapping
    public ResponseEntity<SanPham> create(@RequestBody SanPham sanPham) {
        return ResponseEntity.ok(sanPhamService.create(sanPham));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanPhamDTO> update(@PathVariable Integer id, @RequestBody SanPhamDTO sanPham) {
        return ResponseEntity.ok(sanPhamService.update(id, sanPham));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        sanPhamService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<SanPham>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(sanPhamService.search(keyword));
    }

    @PostMapping("/create-with-detail")
    public ResponseEntity<ChiTietSanPham> createSanPhamAndChiTiet(
            @RequestParam String tenSanPham,
            @RequestParam(required = false) String moTaSanPham,
            @RequestParam(required = false) Integer idLoaiBia,
            @RequestParam(required = false) Integer idTacGia,
            @RequestParam(required = false) Integer idNhaXuatBan,
            @RequestParam(required = false) Integer idChatLieu,
            @RequestParam(required = false) Integer idNguoiDich,
            @RequestParam(required = false) Integer idTheLoai,
            @RequestParam(required = false) Integer idNgonNgu,
            @RequestParam(required = false) BigDecimal gia,
            @RequestParam(required = false) Integer soLuongTon,
            @RequestParam(required = false) BigDecimal trongLuong,
            @RequestParam(required = false) BigDecimal kichThuoc,
            @RequestParam(required = false) String moTaChiTiet) {
        ChiTietSanPham result = chiTietSanPhamService.createSanPhamAndChiTiet(
                tenSanPham, moTaSanPham, idLoaiBia, idTacGia, idNhaXuatBan,
                idChatLieu, idNguoiDich, idTheLoai, idNgonNgu, gia, soLuongTon,
                trongLuong, kichThuoc, moTaChiTiet);
        return ResponseEntity.ok(result);
    }


}
