package org.example.datnbbook.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dia_chi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaChi extends BaseEntity {

    @Column(name = "dia_chi_chi_tiet")
    private String diaChiChiTiet;

    @Column(name = "tinh_thanh")
    private String tinhThanh;

    @Column(name = "quan_huyen")
    private String quanHuyen;

    @Column(name = "xa_phuong")
    private String xaPhuong;

    @Column(name = "mac_dinh")
    private Boolean macDinh = false;

    @Column(name = "ten_nguoi_nhan")
    private String tenNguoiNhan;

    @Column(name = "so_dien_thoai_nguoi_nhan")
    private String soDienThoaiNguoiNhan;

    @ManyToOne
    @JoinColumn(name = "khach_hang_id")
    @JsonManagedReference
    private KhachHang khachHang;
}