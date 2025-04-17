package org.example.datnbbook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
@NoArgsConstructor

public class ChiTietSanPhamAnhId implements Serializable {
    private static final long serialVersionUID = -4077030898401487729L;
    @NotNull
    @Column(name = "chi_tiet_san_pham_id", nullable = false)
    private Integer chiTietSanPhamId;

    @NotNull
    @Column(name = "anh_id", nullable = false)
    private Integer anhId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChiTietSanPhamAnhId entity = (ChiTietSanPhamAnhId) o;
        return Objects.equals(this.anhId, entity.anhId) &&
                Objects.equals(this.chiTietSanPhamId, entity.chiTietSanPhamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(anhId, chiTietSanPhamId);
    }

    public ChiTietSanPhamAnhId(Integer chiTietSanPhamId, Integer anhId) {
        this.chiTietSanPhamId = chiTietSanPhamId;
        this.anhId = anhId;
    }
}