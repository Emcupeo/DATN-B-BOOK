package org.example.datnbbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DotGiamGiaBoSachChiTietDTO {
    private Long id;
    private Integer idBoSach;
    private String tenBoSach;
    private String maBoSach;
    private Integer idDotGiamGia;
    private String tenDotGiamGia;
    private BigDecimal soTienGiam;
    private BigDecimal soPhanTramGiam;
    private BigDecimal giaSauGiam;
    private BigDecimal giaBanDau;
    private Boolean trangThai;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
