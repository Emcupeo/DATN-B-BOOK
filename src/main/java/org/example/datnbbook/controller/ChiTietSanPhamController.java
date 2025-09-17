package org.example.datnbbook.controller;


import lombok.RequiredArgsConstructor;
import org.example.datnbbook.dto.ChiTietSanPhamDTO;
import org.example.datnbbook.dto.ErrorResponse;
import org.example.datnbbook.model.AnhSanPham;
import org.example.datnbbook.model.ChatLieu;
import org.example.datnbbook.model.ChiTietSanPham;
import org.example.datnbbook.model.LoaiBia;
import org.example.datnbbook.model.NgonNgu;
import org.example.datnbbook.model.NguoiDich;
import org.example.datnbbook.model.NhaXuatBan;
import org.example.datnbbook.model.SanPham;
import org.example.datnbbook.model.TacGia;
import org.example.datnbbook.model.TheLoai;
import org.example.datnbbook.service.ChiTietSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
@RequiredArgsConstructor
@RequestMapping("/api/chi-tiet-san-pham")
public class ChiTietSanPhamController {

    private final ChiTietSanPhamService chiTietSanPhamService;

    @GetMapping("/all")
    public ResponseEntity<List<ChiTietSanPham>> getAll() {
        List<ChiTietSanPham> chiTietSanPhams = chiTietSanPhamService.getAll();
        System.out.println("ChiTietSanPhamController.getAll(): Found " + chiTietSanPhams.size() + " products");
        return ResponseEntity.ok(chiTietSanPhams);
    }

    @PostMapping
    public ResponseEntity<ChiTietSanPham> create(@RequestBody ChiTietSanPhamDTO dto) {
        // Mapping thủ công từ DTO sang Entity
        ChiTietSanPham entity = new ChiTietSanPham();
        entity.setTenChiTietSanPham(dto.getTenChiTietSanPham());
        entity.setMaChiTietSanPham(dto.getMaChiTietSanPham());
        entity.setMoTa(dto.getMoTa());
        entity.setGia(dto.getGia());
        entity.setSoLuongTon(dto.getSoLuongTon());
        entity.setTrongLuong(dto.getTrongLuong());
        entity.setKichThuoc(dto.getKichThuoc());
        entity.setTrangThai(dto.getTrangThai());
        entity.setGhiChu(dto.getGhiChu());
        // mapping các trường liên kết
        if (dto.getIdSanPham() != null) {
            SanPham sanPham = new SanPham();
            sanPham.setId(dto.getIdSanPham());
            entity.setIdSanPham(sanPham);
        }
        if (dto.getIdLoaiBia() != null) entity.setIdLoaiBia(new LoaiBia(dto.getIdLoaiBia()));
        if (dto.getIdNguoiDich() != null) {
            NguoiDich nguoiDich = new NguoiDich();
            nguoiDich.setId(dto.getIdNguoiDich());
            entity.setIdNguoiDich(nguoiDich);
        }
        if (dto.getIdTacGia() != null) {
            TacGia tacGia = new TacGia();
            tacGia.setId(dto.getIdTacGia());
            entity.setIdTacGia(tacGia);
        }
        if (dto.getIdChatLieu() != null) {
            ChatLieu chatLieu = new ChatLieu();
            chatLieu.setId(dto.getIdChatLieu());
            entity.setIdChatLieu(chatLieu);
        }
        if (dto.getIdNhaXuatBan() != null) {
            NhaXuatBan nhaXuatBan = new NhaXuatBan();
            nhaXuatBan.setId(dto.getIdNhaXuatBan());
            entity.setIdNhaXuatBan(nhaXuatBan);
        }
        if (dto.getIdTheLoai() != null) {
            TheLoai theLoai = new TheLoai();
            theLoai.setId(dto.getIdTheLoai());
            entity.setIdTheLoai(theLoai);
        }
        if (dto.getIdNgonNgu() != null) {
            NgonNgu ngonNgu = new NgonNgu();
            ngonNgu.setId(dto.getIdNgonNgu());
            entity.setIdNgonNgu(ngonNgu);
        }
        // mapping các trường thời gian, createdBy, updatedBy nếu cần
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setUpdatedBy(dto.getUpdatedBy());
        entity.setDeleted(dto.getDeleted());

        ChiTietSanPham created = chiTietSanPhamService.create(entity);
        if (dto.getImageIds() != null) {
            for (Integer anhId : dto.getImageIds()) {
                chiTietSanPhamService.linkImageToChiTietSanPham(created.getId(), anhId);
            }
        }
        return ResponseEntity.ok(created);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ChiTietSanPham> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(chiTietSanPhamService.getById(id));
    }

    @GetMapping("/ma/{ma}")
    public ResponseEntity<ChiTietSanPham> getByMa(@PathVariable String ma) {
        return ResponseEntity.ok(chiTietSanPhamService.getByMa(ma));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChiTietSanPham> update(@PathVariable Integer id, @RequestBody ChiTietSanPham chiTietSanPham) {
        chiTietSanPham.setId(id); // Đảm bảo ID khớp với path variable
        ChiTietSanPham updated = chiTietSanPhamService.update(id, chiTietSanPham);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/update-stock")
    public ResponseEntity<ChiTietSanPham> updateStock(@PathVariable Integer id, @RequestBody java.util.Map<String, Object> request) {
        try {
            Integer soLuongTon = (Integer) request.get("soLuongTon");
            if (soLuongTon == null) {
                return ResponseEntity.badRequest().build();
            }
            
            ChiTietSanPham updated = chiTietSanPhamService.updateStock(id, soLuongTon);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            chiTietSanPhamService.delete(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Lỗi server: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ChiTietSanPham>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(chiTietSanPhamService.search(keyword));
    }

    @GetMapping("/by-san-pham/{idSanPham}")
    public ResponseEntity<List<ChiTietSanPham>> getBySanPhamId(@PathVariable Integer idSanPham) {

        List<ChiTietSanPham> chiTietSanPhams = chiTietSanPhamService.getBySanPhamId(idSanPham);
        if (chiTietSanPhams.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        // Debug: Log thông tin ảnh
        for (ChiTietSanPham ctsp : chiTietSanPhams) {
            System.out.println("ChiTietSanPham ID: " + ctsp.getId());
            System.out.println("AnhSanPhams size: " + (ctsp.getAnhSanPhams() != null ? ctsp.getAnhSanPhams().size() : "null"));
            if (ctsp.getAnhSanPhams() != null && !ctsp.getAnhSanPhams().isEmpty()) {
                for (AnhSanPham anh : ctsp.getAnhSanPhams()) {
                    System.out.println("  Anh ID: " + anh.getId() + ", URL: " + anh.getUrl());
                }
            }
        }
        
        return ResponseEntity.ok(chiTietSanPhams);
    }


}
