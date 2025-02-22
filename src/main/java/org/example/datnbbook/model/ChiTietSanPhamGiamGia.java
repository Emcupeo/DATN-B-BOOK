package org.example.datnbbook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "chi_tiet_san_pham_giam_gia")
public class ChiTietSanPhamGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dot_giam_gia_chi_tiet")
    private DotGiamGiaChiTiet idDotGiamGiaChiTiet;

    @Nationalized
    @ColumnDefault("'CTSPGG'+right('-000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[CTSPGGSeq]), 5)")
    @Column(name = "ma_chi_tiet_san_pham_giam_gia", length = 15)
    private String maChiTietSanPhamGiamGia;

    @Column(name = "tong_tien_giam", precision = 10, scale = 2)
    private BigDecimal tongTienGiam;

    @Column(name = "tong_phan_tram_giam", precision = 5, scale = 2)
    private BigDecimal tongPhanTramGiam;

    @Column(name = "gia_sau_giam", precision = 10, scale = 2)
    private BigDecimal giaSauGiam;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Boolean trangThai;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;

}