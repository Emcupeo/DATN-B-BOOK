package org.example.datnbbook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietSanPhamDTO {
    private Integer id;
    private String maChiTietSanPham;
    private String tenChiTietSanPham;
    private Integer idSanPham;
    private Integer idLoaiBia;
    private Integer idNguoiDich;
    private Integer idTacGia;
    private Integer idChatLieu;
    private Integer idNhaXuatBan;
    private Integer idTheLoai;
    private Integer idNgonNgu;
    private String anh;
    private BigDecimal trongLuong;
    private BigDecimal kichThuoc;
    private String moTa;
    private BigDecimal gia;
    private Integer soLuongTon;
    private Boolean trangThai;
    private String ghiChu;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private Boolean deleted;
    private List<String> images;
    private List<Integer> imageIds;
    private String isbn;
} 