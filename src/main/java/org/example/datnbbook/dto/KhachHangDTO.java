package org.example.datnbbook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhachHangDTO {
    private Long id;
    private String maKhachHang;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private LocalDate ngaySinh;
    private Boolean gioiTinh;
    private Boolean trangThai;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private List<DiaChiDTO> danhSachDiaChi = new ArrayList<>();
} 