package org.example.datnbbook.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class DotGiamGiaRequest {
    private String tenDotGiamGia;
    private String loaiGiamGia; // Thêm trường này
    private BigDecimal soPhanTramGiam;
    private BigDecimal giaTriGiam; // Thêm trường này
    private Instant ngayBatDau;
    private Instant ngayKetThuc;
    private Integer soLuongTon;
    // Frontend đang gửi mảng id với tên chiTietSanPhamIds
    private List<Integer> chiTietSanPhamIds;
    // Tương thích cũ (nếu có nơi khác vẫn dùng selectedProducts)
    private List<Integer> selectedProducts;
    // Thêm trường cho bộ sách
    private List<Integer> boSachIds;
    // Loại đợt giảm giá: "san_pham" hoặc "bo_sach"
    private String loaiDotGiamGia = "san_pham";
}