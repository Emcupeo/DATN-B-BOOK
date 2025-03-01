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

@Getter
@Setter
@Entity
@Table(name = "dia_chi")
public class DiaChi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_khach_hang", nullable = false)
    private KhachHang idKhachHang;

    @ColumnDefault("'DC'+right('-000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[DCSeq]), 5)")
    @Column(name = "ma_dia_chi", length = 10)
    private String maDiaChi;

    @Nationalized
    @Column(name = "ten_nguoi_nhan")
    private String tenNguoiNhan;

    @Nationalized
    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @ColumnDefault("0")
    @Column(name = "dia_chi_mac_dinh", columnDefinition = "BIT DEFAULT 0")
    private Boolean diaChiMacDinh;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;

}