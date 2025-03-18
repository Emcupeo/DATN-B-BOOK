package org.example.datnbbook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "san_pham")
@ToString
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ColumnDefault("'SP'+right('-00'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[SPSeq]), 5)")
    @Column(name = "ma_san_pham", length = 10, nullable = false)
    private String maSanPham;

    @Nationalized
    @Column(name = "ten_san_pham", nullable = false)
    private String tenSanPham;

    @Nationalized
    @Column(name = "mo_ta")
    private String moTa;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted = false;

    @Transient // Không lưu vào database
    private int tongSoLuongTon;
//
//    @OneToMany(mappedBy = "idSanPham")
//    private Set<ChiTietSanPham> chiTietSanPhams = new LinkedHashSet<>();



}