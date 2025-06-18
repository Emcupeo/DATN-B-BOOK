package org.example.datnbbook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class BoSachChiTietId implements Serializable {
    private static final long serialVersionUID = -1919599748431740177L;
    @NotNull
    @Column(name = "id_bo_sach", nullable = false)
    private Integer idBoSach;

    @NotNull
    @Column(name = "id_chi_tiet_san_pham", nullable = false)
    private Integer idChiTietSanPham;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BoSachChiTietId entity = (BoSachChiTietId) o;
        return Objects.equals(this.idBoSach, entity.idBoSach) &&
                Objects.equals(this.idChiTietSanPham, entity.idChiTietSanPham);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBoSach, idChiTietSanPham);
    }

}