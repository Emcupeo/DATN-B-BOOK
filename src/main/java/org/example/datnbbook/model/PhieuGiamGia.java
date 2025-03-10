package org.example.datnbbook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "phieu_giam_gia")
public class PhieuGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ColumnDefault("'PGG'+right('-000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[PGGSeq]), 5)")
    @Column(name = "ma_phieu_giam_gia", length = 10)
    private String maPhieuGiamGia;

    @Column(name = "so_phan_tram_giam", precision = 5, scale = 2)
    private BigDecimal soPhanTramGiam;

    @Column(name = "gia_tri_giam", precision = 10, scale = 2)
    private BigDecimal giaTriGiam;

    @Column(name = "gia_tri_don_hang_toi_thieu", precision = 10, scale = 2)
    private BigDecimal giaTriDonHangToiThieu;

    @Nationalized
    @Column(name = "mo_ta", length = 100)
    private String moTa;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @ColumnDefault("getdate()")
    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @ColumnDefault("getdate()")
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "so_luong")
    private Long  soLuong;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;
    @JsonIgnore
    @OneToMany(mappedBy = "idPhieuGiamGia")
    private Set<HoaDon> hoaDons = new LinkedHashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "idPhieuGiamGia")
    private Set<PhieuGiamGiaKhachHang> phieuGiamGiaKhachHangs = new LinkedHashSet<>();

}