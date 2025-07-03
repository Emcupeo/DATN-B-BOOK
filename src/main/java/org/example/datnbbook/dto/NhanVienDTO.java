package org.example.datnbbook.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class NhanVienDTO {
    private Integer id;
    private Integer idChucVu;
    private String maNhanVien;
    private String hoTen;
    private Boolean gioiTinh;
    private Integer namSinh;
    private String soCanCuoc;
    private String soDienThoai;
    private String tinhThanh;
    private String quanHuyen;
    private String xaPhuong;
    private String diaChiChiTiet;
    private String email;
    private Boolean trangThai;
    private String tenTaiKhoan;
    private String matKhau;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private Boolean deleted;
} 