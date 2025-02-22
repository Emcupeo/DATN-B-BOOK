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

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "lich_su_hoa_don")
public class LichSuHoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hoa_don")
    private HoaDon idHoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nhan_vien")
    private NhanVien idNhanVien;

    @ColumnDefault("'LSHD'+right('-000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[LSHDSeq]), 5)")
    @Column(name = "ma_lich_su_hoa_don", length = 20)
    private String maLichSuHoaDon;

    @Nationalized
    @Column(name = "trang_thai_cu", length = 50)
    private String trangThaiCu;

    @Nationalized
    @Column(name = "trang_thai_moi", length = 50)
    private String trangThaiMoi;

    @Nationalized
    @Column(name = "ghi_chu", length = 200)
    private String ghiChu;

    @Nationalized
    @Column(name = "hanh_dong", length = 200)
    private String hanhDong;

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

}