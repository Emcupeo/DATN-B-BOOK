package org.example.datnbbook.dto;

import lombok.Data;

@Data
public class TacGiaDTO {
    private Integer id;
    private String maTacGia;

    private String tenTacGia;

    private Boolean deleted;
} 