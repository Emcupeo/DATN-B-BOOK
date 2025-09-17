package org.example.datnbbook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "dot_giam_gia")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DotGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ColumnDefault("'DGG'+right('-000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[DGGSeq]), 5)")
    @Column(name = "ma_dot_giam_gia", length = 10)
    private String maDotGiamGia;

    @Nationalized
    @Column(name = "ten_dot_giam_gia", length = 100)
    private String tenDotGiamGia;

    @Nationalized
    @Column(name = "loai_giam_gia", length = 50)
    private String loaiGiamGia;

    @Column(name = "so_phan_tram_giam", precision = 5, scale = 2)
    private BigDecimal soPhanTramGiam;

    @Column(name = "gia_tri_giam", precision = 10, scale = 2)
    private BigDecimal giaTriGiam;

    @Nationalized
    @Column(name = "mo_ta", length = 100)
    private String moTa;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "ngay_bat_dau")
    private Instant ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private Instant ngayKetThuc;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;

    @OneToMany(mappedBy = "idDotGiamGia")
    @JsonIgnore // Temporarily ignore to avoid circular reference
    private Set<DotGiamGiaChiTiet> dotGiamGiaChiTiets = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idDotGiamGia")
    @JsonIgnore // Temporarily ignore to avoid circular reference
    private Set<DotGiamGiaBoSachChiTiet> dotGiamGiaBoSachChiTiets = new LinkedHashSet<>();

    @Override
    public String toString() {
        return "DotGiamGia{" +
                "id=" + id +
                ", maDotGiamGia='" + maDotGiamGia + '\'' +
                ", tenDotGiamGia='" + tenDotGiamGia + '\'' +
                ", loaiGiamGia='" + loaiGiamGia + '\'' +
                ", soPhanTramGiam=" + soPhanTramGiam +
                ", giaTriGiam=" + giaTriGiam +
                ", moTa='" + moTa + '\'' +
                ", trangThai=" + trangThai +
                ", ngayBatDau=" + ngayBatDau +
                ", ngayKetThuc=" + ngayKetThuc +
                ", deleted=" + deleted +
                '}';
    }
}