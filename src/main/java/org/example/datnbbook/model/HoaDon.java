package org.example.datnbbook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
    @JsonIgnoreProperties({"hibernateLazyInitializer", "hoaDons", "lichSuHoaDons"})
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nhan_vien")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "hoaDons", "lichSuHoaDons"})
    private NhanVien nhanVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_phieu_giam_gia")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "hoaDons", "lichSuHoaDons"})
    private PhieuGiamGia phieuGiamGia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hinh_thuc_thanh_toan")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "hoaDons", "lichSuHoaDons"})
    private HinhThucThanhToan hinhThucThanhToan;


    @ColumnDefault("'HD'+right('-000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[HDSeq]), 5)")
    @Column(name = "ma_hoa_don", length = 20, nullable = false, insertable = false, updatable = false)
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

    @Column(name = "email_nguoi_nhan", length = 100)
    private String emailNguoiNhan;

    @Nationalized
    @Column(name = "ghi_chu", length = 100)
    private String ghiChu;

    @Nationalized
    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    @Nationalized
    @Column(name = "phuong_thuc_thanh_toan", length = 50)
    private String phuongThucThanhToan;

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

    @OneToMany(mappedBy = "hoaDon", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<HoaDonChiTiet> hoaDonChiTiets = new LinkedHashSet<>();

    @OneToMany(mappedBy = "hoaDon", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<LichSuHoaDon> lichSuHoaDons = new LinkedHashSet<>();


}