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
@Table(name = "nguoi_dich")
public class NguoiDich {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ColumnDefault("'ND'+right('-000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[NDSeq]), 5)")
    @Column(name = "ma_nguoi_dich", length = 10)
    private String maNguoiDich;

    @Nationalized
    @Column(name = "ten_nguoi_dich", length = 100)
    private String tenNguoiDich;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;

    @OneToMany(mappedBy = "idNguoiDich")
    private Set<ChiTietSanPham> chiTietSanPhams = new LinkedHashSet<>();

}