package org.example.datnbbook.service;

import org.example.datnbbook.dto.ActiveDiscountDetailDTO;
import org.example.datnbbook.model.BoSach;
import org.example.datnbbook.model.ChiTietSanPham;
import org.example.datnbbook.model.DotGiamGia;
import org.example.datnbbook.model.DotGiamGiaChiTiet;
import org.example.datnbbook.repository.BoSachRepository;
import org.example.datnbbook.repository.ChiTietSanPhamRepository;
import org.example.datnbbook.repository.DotGiamGiaChiTietRepository;
import org.example.datnbbook.repository.DotGiamGiaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DotGiamGiaService {
    private static final Logger logger = LoggerFactory.getLogger(DotGiamGiaService.class);
    @Autowired
    private DotGiamGiaRepository repository;

    @Autowired
    private DotGiamGiaChiTietRepository dotGiamGiaChiTietRepository;

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BoSachRepository boSachRepository;

    @Autowired
    private DotGiamGiaBoSachChiTietService dotGiamGiaBoSachChiTietService;

    public List<DotGiamGia> getAll() {
        return repository.findAllActive();
    }

    public org.example.datnbbook.dto.ActiveDiscountDetailDTO getActiveDetail(Integer ctspId) {
        var d = dotGiamGiaChiTietRepository.findActiveByChiTietSanPhamId(ctspId);
        if (d == null) return null;
        return new org.example.datnbbook.dto.ActiveDiscountDetailDTO(
                d.getIdChiTietSanPham() != null ? d.getIdChiTietSanPham().getId() : null,
                d.getIdDotGiamGia() != null ? d.getIdDotGiamGia().getId() : null,
                d.getGiaBanDau(),
                d.getGiaSauGiam(),
                d.getSoTienGiam()
        );
    }

    @Transactional
    public DotGiamGia create(DotGiamGia dotGiamGia, List<Integer> selectedProductIds, List<Integer> selectedBoSachIds) {
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

                // Validate không trùng đợt giảm giá đang hoạt động
                if (dotGiamGiaChiTietRepository.existsActiveByChiTietSanPhamId(productId)) {
                    throw new IllegalArgumentException("Chi tiết sản phẩm ID " + productId + " đã có đợt giảm giá đang hoạt động");
                }

                DotGiamGiaChiTiet chiTiet = new DotGiamGiaChiTiet();
                chiTiet.setIdDotGiamGia(savedDotGiamGia);
                chiTiet.setIdChiTietSanPham(chiTietSanPham);
                chiTiet.setSoPhanTramGiam(dotGiamGia.getSoPhanTramGiam());
                // Ghi nhận giá bán đầu
                chiTiet.setGiaBanDau(chiTietSanPham.getGia());
                // Tính giá sau giảm
                BigDecimal giaSauGiam = calculateGiaSauGiam(chiTietSanPham.getGia(), dotGiamGia);
                // Số tiền giảm
                chiTiet.setSoTienGiam(chiTietSanPham.getGia().subtract(giaSauGiam));
                chiTiet.setGiaSauGiam(giaSauGiam);
                chiTiet.setTrangThai(true);
                chiTiet.setDeleted(false);

                // Lưu chi tiết đợt giảm giá
                dotGiamGiaChiTietRepository.save(chiTiet);
                // Cập nhật giá của chi tiết sản phẩm về giá sau giảm
                chiTietSanPham.setGia(giaSauGiam);
                chiTietSanPhamRepository.save(chiTietSanPham);
            }
        }

        // Lưu các bộ sách được chọn vào dot_giam_gia_bo_sach_chi_tiet
        if (selectedBoSachIds != null && !selectedBoSachIds.isEmpty()) {
            for (Integer boSachId : selectedBoSachIds) {
                // Validate không trùng đợt giảm giá đang hoạt động
                if (dotGiamGiaBoSachChiTietService.existsActiveDiscountForBoSach(boSachId)) {
                    BoSach boSach = boSachRepository.findById(boSachId)
                            .filter(bs -> !bs.getDeleted())
                            .orElse(null);
                    String boSachName = boSach != null ? boSach.getTenBoSach() : "ID " + boSachId;
                    throw new IllegalArgumentException("Bộ sách \"" + boSachName + "\" đã có đợt giảm giá đang hoạt động");
                }

                // Tạo DTO cho bộ sách
                org.example.datnbbook.dto.DotGiamGiaBoSachChiTietDTO dto = new org.example.datnbbook.dto.DotGiamGiaBoSachChiTietDTO();
                dto.setIdBoSach(boSachId);
                dto.setIdDotGiamGia(savedDotGiamGia.getId());
                dto.setSoPhanTramGiam(dotGiamGia.getSoPhanTramGiam());
                dto.setSoTienGiam(dotGiamGia.getGiaTriGiam());
                dto.setTrangThai(true);

                // Tạo chi tiết đợt giảm giá cho bộ sách
                dotGiamGiaBoSachChiTietService.create(dto);
                
                // Áp dụng giá giảm ngay lập tức vào bo_sach nếu đợt giảm giá đang hoạt động
                if (savedDotGiamGia.getTrangThai() && isDiscountActive(savedDotGiamGia)) {
                    applyDiscountToBoSach(boSachId, dotGiamGia);
                }
            }
        }

        return savedDotGiamGia;
    }

    // Overload method để tương thích với code cũ
    @Transactional
    public DotGiamGia create(DotGiamGia dotGiamGia, List<Integer> selectedProductIds) {
        return create(dotGiamGia, selectedProductIds, null);
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

        // TODO: Có thể cân nhắc xóa/điều chỉnh chi tiết cũ nếu nghiệp vụ yêu cầu
        if (selectedProductIds != null && !selectedProductIds.isEmpty()) {
            for (Integer productId : selectedProductIds) {
                ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết sản phẩm với ID: " + productId));

                if (dotGiamGiaChiTietRepository.existsActiveByChiTietSanPhamId(productId)) {
                    throw new IllegalArgumentException("Chi tiết sản phẩm ID " + productId + " đã có đợt giảm giá đang hoạt động");
                }

                DotGiamGiaChiTiet chiTiet = new DotGiamGiaChiTiet();
                chiTiet.setIdDotGiamGia(updatedDotGiamGia);
                chiTiet.setIdChiTietSanPham(chiTietSanPham);
                chiTiet.setSoPhanTramGiam(updatedDotGiamGia.getSoPhanTramGiam());
                chiTiet.setGiaBanDau(chiTietSanPham.getGia());
                BigDecimal giaSauGiam = calculateGiaSauGiam(chiTietSanPham.getGia(), updatedDotGiamGia);
                chiTiet.setSoTienGiam(chiTietSanPham.getGia().subtract(giaSauGiam));
                chiTiet.setGiaSauGiam(giaSauGiam);
                chiTiet.setTrangThai(true);
                chiTiet.setDeleted(false);

                dotGiamGiaChiTietRepository.save(chiTiet);
                chiTietSanPham.setGia(giaSauGiam);
                chiTietSanPhamRepository.save(chiTietSanPham);
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

    private BigDecimal calculateGiaSauGiam(BigDecimal giaGoc, DotGiamGia dotGiamGia) {
        if (dotGiamGia.getLoaiGiamGia() != null && dotGiamGia.getLoaiGiamGia().equals("Tiền mặt") && dotGiamGia.getGiaTriGiam() != null) {
            BigDecimal result = giaGoc.subtract(dotGiamGia.getGiaTriGiam());
            return result.compareTo(BigDecimal.ZERO) > 0 ? result : BigDecimal.ZERO;
        }
        if (dotGiamGia.getSoPhanTramGiam() != null) {
            BigDecimal percent = dotGiamGia.getSoPhanTramGiam().divide(BigDecimal.valueOf(100));
            BigDecimal result = giaGoc.multiply(BigDecimal.ONE.subtract(percent));
            return result.compareTo(BigDecimal.ZERO) > 0 ? result : BigDecimal.ZERO;
        }
        return giaGoc;
    }

    // Khôi phục giá sau khi hết hạn giảm giá cho một đợt
    @Transactional
    public void restoreOriginalPrices(Integer dotGiamGiaId) {
        DotGiamGia dot = repository.findById(dotGiamGiaId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đợt giảm giá với ID: " + dotGiamGiaId));
        
        // Duyệt qua tất cả chi tiết sản phẩm, gắn lại giá_ban_dau vào CTSP
        dot.getDotGiamGiaChiTiets().forEach(chiTiet -> {
            ChiTietSanPham sp = chiTiet.getIdChiTietSanPham();
            if (chiTiet.getGiaBanDau() != null) {
                sp.setGia(chiTiet.getGiaBanDau());
                chiTietSanPhamRepository.save(sp);
            }
            chiTiet.setTrangThai(false);
            dotGiamGiaChiTietRepository.save(chiTiet);
        });
        
        // Revert discounts for BoSach
        dotGiamGiaBoSachChiTietService.revertDiscounts();
        
        dot.setTrangThai(false);
        repository.save(dot);
    }

    // Apply discounts for both products and book sets
    @Transactional
    public void applyAllDiscounts() {
        // Apply discounts for products (if method exists)
        // applyDiscounts(); // Comment out until method is implemented
        
        // Apply discounts for book sets
        dotGiamGiaBoSachChiTietService.applyDiscounts();
    }

    // Revert all expired discounts
    @Transactional
    public void revertAllExpiredDiscounts() {
        // Revert expired product discounts (if method exists)
        // revertExpiredDiscounts(); // Comment out until method is implemented
        
        // Revert expired book set discounts
        dotGiamGiaBoSachChiTietService.revertDiscounts();
    }

    // Helper method to check if discount is currently active
    private boolean isDiscountActive(DotGiamGia dotGiamGia) {
        java.time.Instant now = java.time.Instant.now();
        return dotGiamGia.getNgayBatDau() != null && 
               dotGiamGia.getNgayKetThuc() != null &&
               dotGiamGia.getNgayBatDau().isBefore(now) && 
               dotGiamGia.getNgayKetThuc().isAfter(now);
    }

    // Helper method to apply discount to BoSach immediately
    @Transactional
    public void applyDiscountToBoSach(Integer boSachId, DotGiamGia dotGiamGia) {
        try {
            BoSach boSach = boSachRepository.findById(boSachId)
                    .filter(bs -> !bs.getDeleted())
                    .orElseThrow(() -> new RuntimeException("Bộ sách không tồn tại với id: " + boSachId));

            // Calculate discounted price
            BigDecimal giaSauGiam = calculateGiaSauGiam(boSach.getGiaTien(), dotGiamGia);
            
            // Apply discount to BoSach
            boSach.setGiaTien(giaSauGiam);
            boSach.setUpdatedAt(java.time.Instant.now());
            boSachRepository.save(boSach);
            
            logger.info("[INFO] Applied discount to BoSach {}: {} -> {}", 
                       boSachId, boSach.getGiaTien(), giaSauGiam);
        } catch (Exception e) {
            logger.error("[ERROR] Failed to apply discount to BoSach {}: {}", boSachId, e.getMessage());
        }
    }

    // Get active discount detail for BoSach
    public ActiveDiscountDetailDTO getActiveBoSachDetail(Integer boSachId) {
        return dotGiamGiaBoSachChiTietService.getActiveDiscountByBoSachId(boSachId);
    }
}