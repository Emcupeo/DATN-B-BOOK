package org.example.datnbbook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "nguoi_dung")
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ColumnDefault("'ND'+right('00000'+CONVERT([nvarchar](5), NEXT VALUE FOR [dbo].[NDUserSeq]), 5)")
    @Column(name = "ma_nguoi_dung", length = 20, unique = true)
    private String maNguoiDung;

    @Column(name = "ten_dang_nhap", length = 50, unique = true, nullable = false)
    private String tenDangNhap;

    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "ho_ten", length = 100)
    private String hoTen;

    @Column(name = "so_dien_thoai", length = 15)
    private String soDienThoai;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_nguoi_dung", nullable = false)
    private LoaiNguoiDung loaiNguoiDung;

    @Column(name = "trang_thai")
    @ColumnDefault("1")
    private Boolean trangThai = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "deleted")
    @ColumnDefault("0")
    private Boolean deleted = false;

    // Reference IDs to link with existing entities
    @Column(name = "khach_hang_id")
    private Long khachHangId;

    @Column(name = "nhan_vien_id")
    private Integer nhanVienId;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (deleted == null) {
            deleted = false;
        }
        if (trangThai == null) {
            trangThai = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum LoaiNguoiDung {
        ADMIN("Admin"),
        NHAN_VIEN("Nhân viên"),
        KHACH_HANG("Khách hàng");

        private final String displayName;

        LoaiNguoiDung(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}