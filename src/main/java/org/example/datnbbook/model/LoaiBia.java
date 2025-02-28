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
@Table(name = "loai_bia")
public class LoaiBia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ColumnDefault("'LB'+right('-000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[LBSeq]), 5)")
    @Column(name = "ma_loai_bia", length = 10)
    private String maLoaiBia;

    @Nationalized
    @Column(name = "ten_loai_bia")
    private String tenLoaiBia;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;
//
//    @OneToMany(mappedBy = "idLoaiBia")
//    private Set<ChiTietSanPham> chiTietSanPhams = new LinkedHashSet<>();

}