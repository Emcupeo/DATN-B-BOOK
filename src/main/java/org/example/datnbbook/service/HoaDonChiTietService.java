package org.example.datnbbook.service;

import org.example.datnbbook.model.ChiTietSanPham;
import org.example.datnbbook.model.HoaDon;
import org.example.datnbbook.model.HoaDonChiTiet;
import org.example.datnbbook.repository.ChiTietSanPhamRepository;
import org.example.datnbbook.repository.HoaDonChiTietRepository;
import org.example.datnbbook.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HoaDonChiTietService {

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    public List<HoaDonChiTiet> getAllHoaDonChiTiet() {
        return hoaDonChiTietRepository.findAll();
    }

    public HoaDonChiTiet getHoaDonById(Integer id) {
        return hoaDonChiTietRepository.findById(id).orElse(null);
    }

    @Transactional
    public HoaDonChiTiet addProductToOrder(Integer hoaDonId, Integer chiTietSanPhamId, Integer soLuong, BigDecimal giaSanPham) {
        // Kiểm tra hóa đơn tồn tại
        Optional<HoaDon> hoaDonOpt = hoaDonRepository.findById(hoaDonId);
        if (!hoaDonOpt.isPresent()) {
            throw new IllegalArgumentException("Hóa đơn không tồn tại với ID: " + hoaDonId);
        }
        HoaDon hoaDon = hoaDonOpt.get();

        // Kiểm tra trạng thái hóa đơn
        if (hoaDon.getTrangThai().equals("Hoàn thành")) {
            throw new IllegalStateException("Không thể thêm sản phẩm vào hóa đơn đã hoàn thành");
        }

        // Kiểm tra sản phẩm chi tiết tồn tại
        Optional<ChiTietSanPham> chiTietSanPhamOpt = chiTietSanPhamRepository.findById(chiTietSanPhamId);
        if (!chiTietSanPhamOpt.isPresent()) {
            throw new IllegalArgumentException("Sản phẩm chi tiết không tồn tại với ID: " + chiTietSanPhamId);
        }
        ChiTietSanPham chiTietSanPham = chiTietSanPhamOpt.get();

        // Kiểm tra số lượng tồn kho
        if (chiTietSanPham.getSoLuongTon() < soLuong) {
            throw new IllegalArgumentException("Số lượng tồn kho không đủ: " + chiTietSanPham.getSoLuongTon());
        }

        // Kiểm tra sản phẩm đã có trong hóa đơn chi tiết
        Optional<HoaDonChiTiet> existingChiTietOpt = hoaDonChiTietRepository
                .findByHoaDonIdAndChiTietSanPhamId(hoaDonId, chiTietSanPhamId);

        HoaDonChiTiet hoaDonChiTiet;
        Instant now = Instant.now();
        if (existingChiTietOpt.isPresent()) {
            // Cập nhật số lượng nếu sản phẩm đã tồn tại
            hoaDonChiTiet = existingChiTietOpt.get();
            hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + soLuong);
            hoaDonChiTiet.setThanhTien(giaSanPham.multiply(BigDecimal.valueOf(hoaDonChiTiet.getSoLuong())));
            hoaDonChiTiet.setUpdatedAt(now);
        } else {
            // Tạo mới bản ghi HoaDonChiTiet
            hoaDonChiTiet = new HoaDonChiTiet();
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setChiTietSanPham(chiTietSanPham);
            hoaDonChiTiet.setMaHoaDonChiTiet("HDCT" + UUID.randomUUID().toString().substring(0, 6));
            hoaDonChiTiet.setSoLuong(soLuong);
            hoaDonChiTiet.setGiaSanPham(giaSanPham);
            hoaDonChiTiet.setThanhTien(giaSanPham.multiply(BigDecimal.valueOf(soLuong)));
            hoaDonChiTiet.setCreatedAt(now);
            hoaDonChiTiet.setUpdatedAt(now);
            hoaDonChiTiet.setDeleted(false);
        }

        // Cập nhật số lượng tồn kho
        chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() - soLuong);
        chiTietSanPham.setUpdatedAt(now);
        chiTietSanPhamRepository.save(chiTietSanPham);

        // Lưu HoaDonChiTiet
        return hoaDonChiTietRepository.save(hoaDonChiTiet);
    }
}