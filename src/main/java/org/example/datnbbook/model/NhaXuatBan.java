package org.example.datnbbook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "nha_xuat_ban")
public class NhaXuatBan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ColumnDefault("'NXB'+right('-000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[NXBSeq]), 5)")
    @Column(name = "ma_nha_xuat_ban", length = 10)
    private String maNhaXuatBan;

    @Nationalized
    @Column(name = "ten_nha_xuat_ban", length = 100)
    private String tenNhaXuatBan;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;

    @OneToMany(mappedBy = "idNhaXuatBan")
    private Set<ChiTietSanPham> chiTietSanPhams = new LinkedHashSet<>();

}