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
    private List<Integer> selectedProducts;
}