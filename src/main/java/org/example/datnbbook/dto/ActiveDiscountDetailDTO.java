package org.example.datnbbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveDiscountDetailDTO {
    private Integer chiTietSanPhamId;
    private Integer dotGiamGiaId;
    private BigDecimal giaBanDau;
    private BigDecimal giaSauGiam;
    private BigDecimal soTienGiam;
}


