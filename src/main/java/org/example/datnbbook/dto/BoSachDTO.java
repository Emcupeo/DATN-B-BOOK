package org.example.datnbbook.dto;


import java.math.BigDecimal;
import java.time.Instant;

public class BoSachDTO {

    private Integer id;
    private String maBoSach;
    private String tenBoSach;
    private BigDecimal giaTien;
    private String moTa;
    private Integer soLuong;
    private Boolean deleted;
    private Instant createdAt;
    private Instant updatedAt;


    private String createdBy;
    private String updatedBy;
}
