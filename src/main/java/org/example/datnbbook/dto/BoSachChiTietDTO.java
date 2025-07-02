package org.example.datnbbook.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BoSachChiTietDTO {
    private Integer idBoSach;
    private Integer idChiTietSanPham;
    private Integer soLuong;
    private ChiTietSanPhamDTO chiTietSanPham;
}