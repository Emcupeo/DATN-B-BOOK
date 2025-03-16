package org.example.datnbbook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "khach_hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhachHang extends BaseEntity {

    @Column(name = "ma_khach_hang", unique = true, nullable = false, length = 10, insertable = false, updatable = false)
    @ColumnDefault("'KH'+right('00000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[KHSeq]), 5)")
    private String maKhachHang;

    @Column(name = "ho_ten")
    private String hoTen;

    @Column(name = "email")
    private String email;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;

    @Column(name = "trang_thai")
    private Boolean trangThai = true;

    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaChi> danhSachDiaChi = new ArrayList<>();

    public void themDiaChi(DiaChi diaChi) {
        danhSachDiaChi.add(diaChi);
        diaChi.setKhachHang(this);
    }

    public void xoaDiaChi(DiaChi diaChi) {
        danhSachDiaChi.remove(diaChi);
        diaChi.setKhachHang(null);
    }
}