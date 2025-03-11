package org.example.datnbbook.dto;

import lombok.Data;

@Data
public class NguoiDichDTO {
    private Integer id;
    private String maNguoiDich;

    private String tenNguoiDich;

    private Boolean deleted;
} 