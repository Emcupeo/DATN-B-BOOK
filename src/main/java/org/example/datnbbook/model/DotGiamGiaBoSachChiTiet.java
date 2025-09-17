package org.example.datnbbook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "dot_giam_gia_bo_sach_chi_tiet")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DotGiamGiaBoSachChiTiet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bo_sach", nullable = false)
    // @JsonIgnore // Temporarily ignore to avoid circular reference
    private BoSach idBoSach;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dot_giam_gia", nullable = false)
    @JsonIgnore // Temporarily ignore to avoid circular reference
    private DotGiamGia idDotGiamGia;

    @Column(name = "so_tien_giam", precision = 10, scale = 2)
    private BigDecimal soTienGiam;

    @Column(name = "so_phan_tram_giam", precision = 5, scale = 2)
    private BigDecimal soPhanTramGiam;

    @Column(name = "gia_sau_giam", precision = 10, scale = 2)
    private BigDecimal giaSauGiam;

    @Column(name = "gia_ban_dau", precision = 10, scale = 2)
    private BigDecimal giaBanDau;

    @Column(name = "trang_thai")
    private Boolean trangThai = true;

    @Column(name = "deleted")
    private Boolean deleted = false;
}
