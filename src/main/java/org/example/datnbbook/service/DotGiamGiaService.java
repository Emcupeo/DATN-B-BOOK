package org.example.datnbbook.service;

import org.example.datnbbook.model.ChiTietSanPham;
import org.example.datnbbook.model.DotGiamGia;
import org.example.datnbbook.model.DotGiamGiaChiTiet;
import org.example.datnbbook.repository.ChiTietSanPhamRepository;
import org.example.datnbbook.repository.DotGiamGiaChiTietRepository;
import org.example.datnbbook.repository.DotGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DotGiamGiaService {
    @Autowired
    private DotGiamGiaRepository repository;

    @Autowired
    private DotGiamGiaChiTietRepository dotGiamGiaChiTietRepository;

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<DotGiamGia> getAll() {
        return repository.findAllActive();
    }

    @Transactional
    public DotGiamGia create(DotGiamGia dotGiamGia, List<Integer> selectedProductIds) {
        // Sinh ma_dot_giam_gia nếu null hoặc không hợp lệ
        if (dotGiamGia.getMaDotGiamGia() == null || dotGiamGia.getMaDotGiamGia().trim().isEmpty() || dotGiamGia.getMaDotGiamGia().equals("null")) {
            String nextMaDotGiamGia;
            do {
                Integer nextSeqVal = jdbcTemplate.queryForObject(
                        "SELECT NEXT VALUE FOR [dbo].[DGGSeq]",
                        Integer.class
                );
                nextMaDotGiamGia = String.format("DGG-%04d", nextSeqVal);
            } while (repository.findByMaDotGiamGia(nextMaDotGiamGia).isPresent());
            dotGiamGia.setMaDotGiamGia(nextMaDotGiamGia);
        }

        // Validation
        validateDiscount(dotGiamGia);

        dotGiamGia.setDeleted(false);
        dotGiamGia.setTrangThai(dotGiamGia.getTrangThai() != null ? dotGiamGia.getTrangThai() : true);
        DotGiamGia savedDotGiamGia = repository.save(dotGiamGia);

        // Lưu các chi tiết sản phẩm được chọn vào dot_giam_gia_chi_tiet
        if (selectedProductIds != null && !selectedProductIds.isEmpty()) {
            for (Integer productId : selectedProductIds) {
                ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết sản phẩm với ID: " + productId));

                DotGiamGiaChiTiet chiTiet = new DotGiamGiaChiTiet();
                chiTiet.setIdDotGiamGia(savedDotGiamGia);
                chiTiet.setIdChiTietSanPham(chiTietSanPham);
                chiTiet.setSoPhanTramGiam(dotGiamGia.getSoPhanTramGiam());
                chiTiet.setSoTienGiam(BigDecimal.ZERO); // Có thể tính toán nếu cần
                chiTiet.setGiaSauGiam(chiTietSanPham.getGia().multiply(BigDecimal.valueOf(1).subtract(dotGiamGia.getSoPhanTramGiam().divide(BigDecimal.valueOf(100)))));
                chiTiet.setTrangThai(true);
                chiTiet.setDeleted(false);

                dotGiamGiaChiTietRepository.save(chiTiet);
            }
        }

        return savedDotGiamGia;
    }

    @Transactional
    public DotGiamGia update(DotGiamGia dotGiamGia, List<Integer> selectedProductIds) {
        DotGiamGia existing = repository.findById(dotGiamGia.getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đợt giảm giá với ID: " + dotGiamGia.getId()));

        // Không thay đổi ma_dot_giam_gia khi cập nhật
        dotGiamGia.setMaDotGiamGia(existing.getMaDotGiamGia());

        // Validation
        validateDiscount(dotGiamGia);

        existing.setTenDotGiamGia(dotGiamGia.getTenDotGiamGia());
        existing.setLoaiGiamGia(dotGiamGia.getLoaiGiamGia());
        existing.setSoPhanTramGiam(dotGiamGia.getSoPhanTramGiam());
        existing.setGiaTriGiam(dotGiamGia.getGiaTriGiam());
        existing.setMoTa(dotGiamGia.getMoTa());
        existing.setTrangThai(dotGiamGia.getTrangThai());
        existing.setNgayBatDau(dotGiamGia.getNgayBatDau());
        existing.setNgayKetThuc(dotGiamGia.getNgayKetThuc());

        DotGiamGia updatedDotGiamGia = repository.save(existing);

        // Xóa các chi tiết cũ và thêm mới
        dotGiamGiaChiTietRepository.deleteAll(existing.getDotGiamGiaChiTiets());
        if (selectedProductIds != null && !selectedProductIds.isEmpty()) {
            for (Integer productId : selectedProductIds) {
                ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết sản phẩm với ID: " + productId));

                DotGiamGiaChiTiet chiTiet = new DotGiamGiaChiTiet();
                chiTiet.setIdDotGiamGia(updatedDotGiamGia);
                chiTiet.setIdChiTietSanPham(chiTietSanPham);
                chiTiet.setSoPhanTramGiam(updatedDotGiamGia.getSoPhanTramGiam());
                chiTiet.setSoTienGiam(BigDecimal.ZERO);
                chiTiet.setGiaSauGiam(chiTietSanPham.getGia().multiply(BigDecimal.valueOf(1).subtract(updatedDotGiamGia.getSoPhanTramGiam().divide(BigDecimal.valueOf(100)))));
                chiTiet.setTrangThai(true);
                chiTiet.setDeleted(false);

                dotGiamGiaChiTietRepository.save(chiTiet);
            }
        }

        return updatedDotGiamGia;
    }

    @Transactional
    public void delete(Integer id) {
        DotGiamGia dotGiamGia = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đợt giảm giá với ID: " + id));
        dotGiamGia.setDeleted(true);
        repository.save(dotGiamGia);
    }

    private void validateDiscount(DotGiamGia dotGiamGia) {
        if (dotGiamGia.getTenDotGiamGia() == null || dotGiamGia.getTenDotGiamGia().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đợt giảm giá không được để trống");
        }
        if (dotGiamGia.getLoaiGiamGia() == null || dotGiamGia.getLoaiGiamGia().trim().isEmpty()) {
            throw new IllegalArgumentException("Loại giảm giá không được để trống");
        }
        if (dotGiamGia.getLoaiGiamGia().equals("Phần trăm") &&
                (dotGiamGia.getSoPhanTramGiam() == null || dotGiamGia.getSoPhanTramGiam().compareTo(BigDecimal.ZERO) <= 0)) {
            throw new IllegalArgumentException("Phần trăm giảm phải lớn hơn 0 khi loại là 'Phần trăm'");
        }
        if (dotGiamGia.getLoaiGiamGia().equals("Tiền mặt") &&
                (dotGiamGia.getGiaTriGiam() == null || dotGiamGia.getGiaTriGiam().compareTo(BigDecimal.ZERO) <= 0)) {
            throw new IllegalArgumentException("Giá trị giảm phải lớn hơn 0 khi loại là 'Tiền mặt'");
        }
        if (dotGiamGia.getNgayBatDau() == null || dotGiamGia.getNgayKetThuc() == null) {
            throw new IllegalArgumentException("Ngày bắt đầu và kết thúc không được để trống");
        }
        if (dotGiamGia.getNgayBatDau().isAfter(dotGiamGia.getNgayKetThuc())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }
    }
}