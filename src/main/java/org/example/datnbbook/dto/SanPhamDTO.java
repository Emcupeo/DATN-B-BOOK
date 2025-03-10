package org.example.datnbbook.dto;

import lombok.Data;

@Data
public class SanPhamDTO {
    private Integer id;
    private String maSanPham;
    private String tenSanPham;
    private String moTa;
    private Boolean deleted;
} 