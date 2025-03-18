package org.example.datnbbook.dto;

import lombok.Data;

import java.util.List;

@Data
public class SanPhamRequest {
    private String tenSanPham;
    private String moTaSanPham;
    private Integer idTacGia;
    private Integer idNhaXuatBan;
    private Integer idNguoiDich;
    private Integer idTheLoai;
    private Integer idNgonNgu;
    private Double trongLuong;
    private Double kichThuoc;
    private String moTaChiTiet;
    private List<ChiTietSanPhamDTO> chiTietSanPhamList;

    @Data
    public static class ChiTietSanPhamDTO {
        private String tenChiTietSanPham;
        private Integer idLoaiBia;
        private Integer idChatLieu;
        private Double gia;
        private Integer soLuongTon;
    }
}
