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
@Table(name = "chi_tiet_san_pham")
public class ChiTietSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ColumnDefault("'CTSP'+right('-000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[CTSPSeq]), 5)")
    @Column(name = "ma_chi_tiet_san_pham", length = 10)
    private String maChiTietSanPham;

    @Nationalized
    @Column(name = "ten_chi_tiet_san_pham", length = 55)
    private String tenChiTietSanPham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_san_pham")
    private SanPham idSanPham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_loai_bia")
    private LoaiBia idLoaiBia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dich")
    private NguoiDich idNguoiDich;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tac_gia")
    private TacGia idTacGia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chat_lieu")
    private ChatLieu idChatLieu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nha_xuat_ban")
    private NhaXuatBan idNhaXuatBan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_the_loai")
    private TheLoai idTheLoai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ngon_ngu")
    private NgonNgu idNgonNgu;

    @Column(name = "anh", length = 50)
    private String anh;

    @Column(name = "trong_luong", precision = 10, scale = 2)
    private BigDecimal trongLuong;

    @Column(name = "kich_thuoc", precision = 10, scale = 2)
    private BigDecimal kichThuoc;

    @Nationalized
    @Column(name = "mo_ta", length = 100)
    private String moTa;

    @Column(name = "gia", precision = 10, scale = 2)
    private BigDecimal gia;

    @Column(name = "so_luong_ton")
    private Integer soLuongTon;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Nationalized
    @Column(name = "ghi_chu", length = 100)
    private String ghiChu;

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

    @OneToMany(mappedBy = "idChiTietSanPham")
    private Set<DotGiamGiaChiTiet> dotGiamGiaChiTiets = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idChiTietSanPham")
    private Set<GioHang> gioHangs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idChiTietSanPham")
    private Set<HoaDonChiTiet> hoaDonChiTiets = new LinkedHashSet<>();

}