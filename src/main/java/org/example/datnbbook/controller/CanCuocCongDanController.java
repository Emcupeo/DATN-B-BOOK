package org.example.datnbbook.controller;

import org.example.datnbbook.model.NhanVien;
import org.example.datnbbook.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api/cccd")
public class CanCuocCongDanController {
    @Autowired
    private NhanVienRepository nhanVienRepository;

    @PostMapping("/save-cccd")
    public ResponseEntity<?> saveCCCD(@RequestBody Map<String, String> payload) {
        try {
            String qrData = payload.get("qrData");
            // Giả sử qrData có định dạng: "so_can_cuoc:...;ho_ten:...;nam_sinh:...;..."
            Map<String, String> data = parseQRData(qrData);

            NhanVien nv = new NhanVien();
            nv.setSoCanCuoc(data.get("so_can_cuoc"));
            nv.setHoTen(data.get("ho_ten"));
            nv.setNamSinh(data.get("nam_sinh") != null ? Integer.parseInt(data.get("nam_sinh")) : null);
            // Chuyển đổi "gioi_tinh" sang Boolean (ví dụ: "1" hoặc "true" cho Nam)
            nv.setGioiTinh("1".equals(data.get("gioi_tinh")) || "true".equalsIgnoreCase(data.get("gioi_tinh")));
            nv.setTinhThanh(data.get("tinh_thanh"));
            nv.setQuanHuyen(data.get("quan_huyen"));
            nv.setXaPhuong(data.get("xa_phuong"));
            nv.setDiaChiChiTiet(data.get("dia_chi_chi_tiet"));

            nv.setCreatedAt(Instant.now());
            nv.setCreatedBy("system");
            nv.setUpdatedAt(Instant.now());
            nv.setUpdatedBy("system");
            nv.setDeleted(false);
            nv.setTrangThai(true);

            nhanVienRepository.save(nv);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Lưu thông tin CCCD thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Hàm phân tích dữ liệu QR theo định dạng của VNEID
    private Map<String, String> parseQRData(String qrData) {
        Map<String, String> data = new HashMap<>();
        String[] parts = qrData.split(";");
        for (String part : parts) {
            if (part.contains(":")) {
                String[] keyValue = part.split(":", 2);
                data.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return data;
    }
}
