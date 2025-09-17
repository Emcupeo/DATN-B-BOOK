package org.example.datnbbook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "dot_giam_gia_chi_tiet")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DotGiamGiaChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chi_tiet_san_pham")
    // @JsonIgnore // Temporarily ignore to avoid circular reference
    private ChiTietSanPham idChiTietSanPham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dot_giam_gia")
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

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Boolean trangThai;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;

    @OneToMany(mappedBy = "idDotGiamGiaChiTiet")
    private Set<ChiTietSanPhamGiamGia> chiTietSanPhamGiamGias = new LinkedHashSet<>();

}