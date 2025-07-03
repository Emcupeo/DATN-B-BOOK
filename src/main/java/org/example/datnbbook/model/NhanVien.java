package org.example.datnbbook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "nhan_vien")
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_chuc_vu")
    private ChucVu idChucVu;

    @Column(name = "ma_nhan_vien", length = 10, nullable = false, unique = true)
    private String maNhanVien;

    @Nationalized
    @Column(name = "ho_ten")
    private String hoTen;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;

    @Column(name = "nam_sinh")
    private Integer namSinh;

    @Column(name = "so_can_cuoc", length = 20)
    private String soCanCuoc;

    @Column(name = "so_dien_thoai", length = 15)
    private String soDienThoai;

    @Nationalized
    @Column(name = "tinh_thanh")
    private String tinhThanh;

    @Nationalized
    @Column(name = "quan_huyen")
    private String quanHuyen;

    @Nationalized
    @Column(name = "xa_phuong")
    private String xaPhuong;

    @Nationalized
    @Column(name = "dia_chi_chi_tiet")
    private String diaChiChiTiet;

    @Column(name = "email")
    private String email;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "ten_tai_khoan", length = 50)
    private String tenTaiKhoan;

    @Column(name = "mat_khau")
    private String matKhau;

    @ColumnDefault("getdate()")
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @ColumnDefault("getdate()")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;

    @OneToMany(mappedBy = "nhanVien")
    private List<HoaDon> hoaDons;

    @OneToMany(mappedBy = "nhanVien")
    private Set<LichSuHoaDon> lichSuHoaDons = new LinkedHashSet<>();

}