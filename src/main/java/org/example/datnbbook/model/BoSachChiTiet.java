package org.example.datnbbook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "bo_sach_chi_tiet")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BoSachChiTiet {
    @EmbeddedId
    private BoSachChiTietId id;

    @MapsId("idBoSach")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_bo_sach", nullable = false)
    private BoSach idBoSach;

    @MapsId("idChiTietSanPham")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_chi_tiet_san_pham", nullable = false)
    private ChiTietSanPham idChiTietSanPham;

    @ColumnDefault("1")
    @Column(name = "so_luong")
    private Integer soLuong;

    @ColumnDefault("0")
    @Column(name = "deleted")
    private Boolean deleted;

    @ColumnDefault("getdate()")
    @Column(name = "created_at", columnDefinition = "DATETIME")
    private Instant createdAt;

    @ColumnDefault("getdate()")
    @Column(name = "updated_at", columnDefinition = "DATETIME")
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