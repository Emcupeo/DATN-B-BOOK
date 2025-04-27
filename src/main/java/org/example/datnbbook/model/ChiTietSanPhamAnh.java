package org.example.datnbbook.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "chi_tiet_san_pham_anh")
public class ChiTietSanPhamAnh {
    @EmbeddedId
    private ChiTietSanPhamAnhId id;

    @MapsId("chiTietSanPhamId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "chi_tiet_san_pham_id", nullable = false)
    private ChiTietSanPham chiTietSanPham;

    @MapsId("anhId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "anh_id", nullable = false)
    @JsonBackReference
    private AnhSanPham anh;

}