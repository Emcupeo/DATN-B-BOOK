package org.example.datnbbook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaChiDTO {
    private Long id;
    private String diaChiChiTiet;
    private String tinhThanh;
    private String quanHuyen;
    private String xaPhuong;
    private Boolean macDinh;
} 