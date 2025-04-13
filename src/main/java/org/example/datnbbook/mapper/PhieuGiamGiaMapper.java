package org.example.datnbbook.mapper;

import org.example.datnbbook.dto.PhieuGiamGiaDTO;
import org.example.datnbbook.model.PhieuGiamGia;
import org.example.datnbbook.model.PhieuGiamGiaKhachHang;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PhieuGiamGiaMapper {

    public PhieuGiamGiaDTO toDTO(PhieuGiamGia entity, List<PhieuGiamGiaKhachHang> danhSachKhachHang) {
        if (entity == null) {
            return null;
        }

        PhieuGiamGiaDTO dto = new PhieuGiamGiaDTO();
        dto.setId(entity.getId());
        dto.setMaPhieuGiamGia(entity.getMaPhieuGiamGia());
        dto.setTenPhieuGiamGia(entity.getTenPhieuGiamGia());
        dto.setSoPhanTramGiam(entity.getSoPhanTramGiam());
        dto.setGiaTriGiam(entity.getGiaTriGiam());
        dto.setGiaTriDonHangToiThieu(entity.getGiaTriDonHangToiThieu());
        dto.setMoTa(entity.getMoTa());
        dto.setTrangThai(entity.getTrangThai());
        dto.setNgayBatDau(entity.getNgayBatDau());
        dto.setNgayKetThuc(entity.getNgayKetThuc());
        dto.setSoLuong(entity.getSoLuong());
        dto.setLoaiApDung(entity.getLoaiApDung());
        dto.setLoaiPhieu(entity.getLoaiPhieu());
        dto.setTinhTrang(entity.getTinhTrang());

        if (danhSachKhachHang != null && !danhSachKhachHang.isEmpty()) {
            var kh = danhSachKhachHang.get(0).getKhachHang();
            dto.setKhachHangId(Long.valueOf(kh.getId()));
            dto.setTenKhachHang(kh.getHoTen());
            dto.setEmail(kh.getEmail());
            dto.setSdt(kh.getSoDienThoai());
        }

        return dto;
    }

    public PhieuGiamGia toEntity(PhieuGiamGiaDTO dto) {
        if (dto == null) {
            return null;
        }

        PhieuGiamGia entity = new PhieuGiamGia();
        entity.setId(dto.getId());
        entity.setMaPhieuGiamGia(dto.getMaPhieuGiamGia());
        entity.setTenPhieuGiamGia(dto.getTenPhieuGiamGia());
        entity.setSoPhanTramGiam(dto.getSoPhanTramGiam());
        entity.setGiaTriGiam(dto.getGiaTriGiam());
        entity.setGiaTriDonHangToiThieu(dto.getGiaTriDonHangToiThieu());
        entity.setMoTa(dto.getMoTa());
        entity.setTrangThai(dto.getTrangThai());
        entity.setNgayBatDau(dto.getNgayBatDau());
        entity.setNgayKetThuc(dto.getNgayKetThuc());
        entity.setSoLuong(dto.getSoLuong());
        entity.setLoaiApDung(dto.getLoaiApDung());
        entity.setLoaiPhieu(dto.getLoaiPhieu());
        // Xóa dòng sau để không ánh xạ tinh_trang
        // entity.setTinhTrang(dto.getTinhTrang());

        return entity;
    }
}