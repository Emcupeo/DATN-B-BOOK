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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "hoa_don")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_khach_hang")
    private KhachHang idKhachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nhan_vien")
    private NhanVien idNhanVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_phieu_giam_gia")
    private PhieuGiamGia idPhieuGiamGia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hinh_thuc_thanh_toan")
    private HinhThucThanhToan idHinhThucThanhToan;

    @ColumnDefault("'HD'+right('-000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[HDSeq]), 5)")
    @Column(name = "ma_hoa_don", length = 20)
    private String maHoaDon;

    @Nationalized
    @Column(name = "loai_hoa_don", length = 50)
    private String loaiHoaDon;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private Instant ngayTao;

    @Column(name = "ngay_dat_hang")
    private Instant ngayDatHang;

    @Column(name = "ngay_giao")
    private Instant ngayGiao;

    @Column(name = "ngay_du_kien")
    private Instant ngayDuKien;

    @Column(name = "ngay_nhan")
    private Instant ngayNhan;

    @Nationalized
    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "phi_ship", precision = 10, scale = 2)
    private BigDecimal phiShip;

    @Column(name = "tong_tien", precision = 10, scale = 2)
    private BigDecimal tongTien;

    @Nationalized
    @Column(name = "ten_nguoi_nhan")
    private String tenNguoiNhan;

    @Column(name = "so_dien_thoai_nguoi_nhan", length = 15)
    private String soDienThoaiNguoiNhan;

    @Nationalized
    @Column(name = "ghi_chu", length = 100)
    private String ghiChu;

    @Nationalized
    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    @ColumnDefault("getdate()")
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @ColumnDefault("getdate()")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;

    @OneToMany(mappedBy = "idHoaDon")
    private Set<HoaDonChiTiet> hoaDonChiTiets = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idHoaDon")
    private Set<LichSuHoaDon> lichSuHoaDons = new LinkedHashSet<>();

}