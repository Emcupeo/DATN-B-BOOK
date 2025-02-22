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
@Table(name = "ngon_ngu")
public class NgonNgu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ColumnDefault("'NN'+right('-000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[NNSeq]), 5)")
    @Column(name = "ma_ngon_ngu", length = 10)
    private String maNgonNgu;

    @Nationalized
    @Column(name = "ngon_ngu", length = 100)
    private String ngonNgu;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;

    @OneToMany(mappedBy = "idNgonNgu")
    private Set<ChiTietSanPham> chiTietSanPhams = new LinkedHashSet<>();

}