package org.example.datnbbook.dto;

import lombok.Data;

@Data
public class LoaiBiaDTO {
    private Integer id;
    private String maLoaiBia;

    private String tenLoaiBia;

    private Boolean deleted;
} 