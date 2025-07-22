package org.example.datnbbook.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class SanPhamDTO {
    private Integer id;
    private String maSanPham;
    private String tenSanPham;
    private String moTa;
    private Boolean deleted;
    private Integer idDanhMuc;
    private String tenDanhMuc;
    private Instant createdAt;
    private Instant updatedAt;
} 