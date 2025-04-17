package org.example.datnbbook.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SanPhamRequest {
    private String tenSanPham;
    private String moTaSanPham;
    private Integer idTacGia;
    private Integer idNhaXuatBan;
    private Integer idNguoiDich;
    private Integer idTheLoai;
    private Integer idNgonNgu;
    private String moTaChiTiet;
    private List<ChiTietSanPhamDTO> chiTietSanPhamList;

    @Getter
    @Setter
    public static class ChiTietSanPhamDTO {
        private String tenChiTietSanPham;
        private Integer idLoaiBia;
        private Integer idChatLieu;
        private Double gia;
        private Integer soLuongTon;
        private Double trongLuong;
        private Double kichThuoc;
        private List<Integer> imageIds;
    }
}