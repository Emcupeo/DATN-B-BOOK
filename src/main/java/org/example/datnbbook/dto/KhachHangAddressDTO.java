package org.example.datnbbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhachHangAddressDTO {
    private Long id;
    private String maKhachHang;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String ngaySinh;
    private Integer gioiTinh;
    private Integer trangThai;
    private List<DiaChiDTO> danhSachDiaChi;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiaChiDTO {
        private Long id;
        private String diaChiChiTiet;
        private String tinhThanh;
        private String quanHuyen;
        private String xaPhuong;
        private Boolean macDinh;
    }
} 