package org.example.datnbbook.mapper;

import org.example.datnbbook.dto.PhieuGiamGiaDTO;
import org.example.datnbbook.model.PhieuGiamGia;
import org.example.datnbbook.model.PhieuGiamGiaKhachHang;

import java.util.List;

public class PhieuGiamGiaMapper {

    public static PhieuGiamGiaDTO toDTO(PhieuGiamGia entity, List<PhieuGiamGiaKhachHang> danhSachKhachHang) {
        PhieuGiamGiaDTO dto = new PhieuGiamGiaDTO();
        dto.setId(entity.getId());
        dto.setMaPhieuGiamGia(entity.getMaPhieuGiamGia());
        dto.setTenPhieuGiamGia(entity.getTenPhieuGiamGia()); // ✅ FIXED
        dto.setSoPhanTramGiam(entity.getSoPhanTramGiam());
        dto.setGiaTriGiam(entity.getGiaTriGiam());
        dto.setGiaTriDonHangToiThieu(entity.getGiaTriDonHangToiThieu());
        dto.setMoTa(entity.getMoTa());
        dto.setTrangThai(entity.getTrangThai());
        dto.setNgayBatDau(entity.getNgayBatDau());
        dto.setNgayKetThuc(entity.getNgayKetThuc());
        dto.setSoLuong(entity.getSoLuong());

        if (danhSachKhachHang != null && !danhSachKhachHang.isEmpty()) {
            var kh = danhSachKhachHang.get(0).getKhachHang();
            dto.setLoaiApDung("Khách hàng cụ thể");
            dto.setKhachHangId(Long.valueOf(kh.getId()));
            dto.setTenKhachHang(kh.getHoTen());
            dto.setEmail(kh.getEmail());
            dto.setSdt(kh.getSoDienThoai());
        } else {
            dto.setLoaiApDung("Công khai");
        }

        return dto;
    }

}

