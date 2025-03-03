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

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "chuc_vu")
public class ChucVu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ColumnDefault("'CV'+right('-000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[CVSeq]), 5)")
    @Column(name = "ma_chuc_vu", length = 10)
    private String maChucVu;

    @Column(name = "ten_chuc_vu")
    private String tenChucVu;

    @ColumnDefault("0")
    @Column(name = "Deleted")
    private Boolean deleted;


}