package org.example.datnbbook.controller;

import org.example.datnbbook.model.HoaDonChiTiet;
import org.example.datnbbook.service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api")
public class HoaDonChiTietController {

    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    @GetMapping("/hoa-don-chi-tiet")
    public List<HoaDonChiTiet> getHoaDonChiTiet() {
        return hoaDonChiTietService.getAllHoaDonChiTiet();
    }

    @PostMapping("/hoa-don/{hoaDonId}/chi-tiet")
    public ResponseEntity<?> addProductToOrder(
            @PathVariable Integer hoaDonId,
            @RequestBody AddProductRequest request) {
        try {
            HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietService.addProductToOrder(
                    hoaDonId,
                    request.getChiTietSanPhamId(),
                    request.getSoLuong(),
                    request.getGiaSanPham()
            );
            return ResponseEntity.ok(hoaDonChiTiet);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Lỗi server: " + e.getMessage()));
        }
    }

    public static class AddProductRequest {
        private Integer chiTietSanPhamId;
        private Integer soLuong;
        private BigDecimal giaSanPham;

        public Integer getChiTietSanPhamId() {
            return chiTietSanPhamId;
        }

        public void setChiTietSanPhamId(Integer chiTietSanPhamId) {
            this.chiTietSanPhamId = chiTietSanPhamId;
        }

        public Integer getSoLuong() {
            return soLuong;
        }

        public void setSoLuong(Integer soLuong) {
            this.soLuong = soLuong;
        }

        public BigDecimal getGiaSanPham() {
            return giaSanPham;
        }

        public void setGiaSanPham(BigDecimal giaSanPham) {
            this.giaSanPham = giaSanPham;
        }
    }

    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}