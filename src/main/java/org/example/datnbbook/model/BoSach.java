package org.example.datnbbook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "bo_sach")
public class BoSach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Nationalized
    @ColumnDefault("'BS'+right('-000'+CONVERT([nvarchar](7), NEXT VALUE FOR [dbo].[BSSeq]), 5)")
    @Column(name = "ma_bo_sach", nullable = false, length = 50)
    private String maBoSach;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ten_bo_sach", nullable = false)
    private String tenBoSach;

    @NotNull
    @Column(name = "gia_tien", nullable = false, precision = 10, scale = 2)
    private BigDecimal giaTien;

    @Size(max = 255)
    @Nationalized
    @Column(name = "mo_ta")
    private String moTa;

    @ColumnDefault("0")
    @Column(name = "so_luong")
    private Integer soLuong;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;

    @ColumnDefault("getdate()")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("getdate()")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Size(max = 100)
    @Nationalized
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Size(max = 100)
    @Nationalized
    @Column(name = "updated_by", length = 100)
    private String updatedBy;

}