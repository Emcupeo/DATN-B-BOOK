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
@Table(name = "the_loai")
public class TheLoai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ColumnDefault("'TL'+right('-000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[TLSeq]), 5)")
    @Column(name = "ma_the_loai", length = 10)
    private String maTheLoai;

    @Nationalized
    @Column(name = "ten_the_loai", length = 100)
    private String tenTheLoai;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;

    @OneToMany(mappedBy = "idTheLoai")
    private Set<ChiTietSanPham> chiTietSanPhams = new LinkedHashSet<>();

}