package org.example.datnbbook.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "khach_hang")
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ColumnDefault("'KH'+right('-000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[KHSeq]), 5)")
    @Column(name = "ma_khach_hang", length = 10)
    private String maKhachHang;

    @Nationalized
    @Column(name = "ho_ten")
    private String hoTen;

    @Column(name = "email")
    private String email;

    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Boolean trangThai;

    @ColumnDefault("getdate()")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("getdate()")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;

//    @OneToMany(mappedBy = "idKhachHang")
//    private Set<DiaChi> diaChis = new LinkedHashSet<>();
//
//    @OneToMany(mappedBy = "idKhachHang")
//    private Set<GioHang> gioHangs = new LinkedHashSet<>();
//
    @OneToMany(mappedBy = "khachHang")
    @JsonBackReference
    private Set<HoaDon> hoaDons = new LinkedHashSet<>();

    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DiaChi> danhSachDiaChi = new ArrayList<>();

    public void themDiaChi(DiaChi diaChi) {
        danhSachDiaChi.add(diaChi);
        diaChi.setKhachHang(this);
    }

    public void xoaDiaChi(DiaChi diaChi) {
        danhSachDiaChi.remove(diaChi);
        diaChi.setKhachHang(null);
    }
//
//    @OneToMany(mappedBy = "idKhachHang")
//    private Set<PhieuGiamGiaKhachHang> phieuGiamGiaKhachHangs = new LinkedHashSet<>();

}