package org.example.datnbbook.service;

import org.example.datnbbook.model.DotGiamGia;
import org.example.datnbbook.repository.DotGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DotGiamGiaService {
    @Autowired
    private DotGiamGiaRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<DotGiamGia> getAll() {
        return repository.findAllActive();
    }

    public DotGiamGia create(DotGiamGia dotGiamGia) {
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
        return repository.save(dotGiamGia);
    }

    public DotGiamGia update(DotGiamGia dotGiamGia) {
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
        return repository.save(existing);
    }

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