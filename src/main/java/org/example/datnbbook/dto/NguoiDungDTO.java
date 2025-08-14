package org.example.datnbbook.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.datnbbook.model.NguoiDung;

@Data
@Getter
@Setter
public class NguoiDungDTO {
    private Long id;
    private String maNguoiDung;
    private String tenDangNhap;
    private String matKhau;
    private String email;
    private String hoTen;
    private String soDienThoai;
    private NguoiDung.LoaiNguoiDung loaiNguoiDung;
    private Boolean trangThai;
    private Long khachHangId;
    private Integer nhanVienId;
}