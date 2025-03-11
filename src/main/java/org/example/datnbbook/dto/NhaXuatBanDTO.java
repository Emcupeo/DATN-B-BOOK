package org.example.datnbbook.dto;

import lombok.Data;

@Data
public class NhaXuatBanDTO {
    private Integer id;
    private String maNhaXuatBan;

    private String tenNhaXuatBan;

    private Boolean deleted;
} 