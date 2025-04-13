package org.example.datnbbook.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDateTime; // <-- sửa thành LocalDateTime
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

    @ColumnDefault("'PGG'+right('-000'+CONVERT( , NEXT VALUE FOR [dbo].[PGGSeq]), 5)")
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") // <-- Thêm định dạng JSON
    private LocalDateTime ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") // <-- Thêm định dạng JSON
    private LocalDateTime ngayKetThuc;

    @ColumnDefault("getdate()")
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @ColumnDefault("getdate()")
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "so_luong")
    private Long  soLuong;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;

    @OneToMany(mappedBy = "phieuGiamGia")
    @JsonIgnore
    private Set<HoaDon> hoaDons;
    
    @JsonIgnore
    @OneToMany(mappedBy = "phieuGiamGia", fetch = FetchType.LAZY)
    private Set<PhieuGiamGiaKhachHang> phieuGiamGiaKhachHangs = new LinkedHashSet<>();

    @Column(name = "ten_phieu_giam_gia")
    private String tenPhieuGiamGia;

    @Column(name = "loai_ap_dung")
    private String loaiApDung;

    @Column(name = "loai_phieu")
    private String loaiPhieu;

    @Column(name = "tinh_trang", insertable = false, updatable = false)
    private String tinhTrang;
}
