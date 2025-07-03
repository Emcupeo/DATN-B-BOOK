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
@Table(name = "tac_gia")
public class TacGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ColumnDefault("'TG'+right('-000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[TGSeq]), 5)")
    @Column(name = "ma_tac_gia", length = 10)
    private String maTacGia;

    @Nationalized
    @Column(name = "ten_tac_gia", length = 100)
    private String tenTacGia;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;

//
//    @OneToMany(mappedBy = "idTacGia")
//    private Set<ChiTietSanPham> chiTietSanPhams = new LinkedHashSet<>();

}