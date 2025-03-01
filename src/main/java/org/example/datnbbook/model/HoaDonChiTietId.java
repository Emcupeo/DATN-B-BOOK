package org.example.datnbbook.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class HoaDonChiTietId implements Serializable {
    private Integer idHoaDon;
    private Integer idChiTietSanPham;
}
