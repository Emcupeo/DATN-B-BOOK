package org.example.datnbbook.dto;

import lombok.Data;

@Data
public class DanhMucDTO {
    private Integer id;
    private String maDanhMuc;
    private String tenDanhMuc;
    private Boolean deleted;
} 