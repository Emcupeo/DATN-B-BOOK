package org.example.datnbbook.dto;

import lombok.Data;

@Data
public class TheLoaiDTO {
    private Integer id;
    private String maTheLoai;

    private String tenTheLoai;

    private Boolean deleted;
} 