package org.example.datnbbook.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class PhieuGiamGiaDTO {
    private Long id;
    private String maPhieuGiamGia;
    private String tenPhieuGiamGia;
    private String tenKhachHang;
    private String email;
    private String sdt;
    private String diaChi;
    private BigDecimal soPhanTramGiam;
    private BigDecimal giaTriGiam;
    private BigDecimal giaTriDonHangToiThieu;
    private String moTa;
    private Boolean trangThai;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private Long soLuong;
    private Long khachHangId;
    private Boolean deleted;
    private String loaiApDung;
    private String loaiPhieu;
    private String tinhTrang;
}
