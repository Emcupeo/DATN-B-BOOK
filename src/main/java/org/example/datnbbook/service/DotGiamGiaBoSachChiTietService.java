package org.example.datnbbook.service;

import org.example.datnbbook.dto.ActiveDiscountDetailDTO;
import org.example.datnbbook.dto.DotGiamGiaBoSachChiTietDTO;
import org.example.datnbbook.model.BoSach;
import org.example.datnbbook.model.DotGiamGia;
import org.example.datnbbook.model.DotGiamGiaBoSachChiTiet;
import org.example.datnbbook.repository.BoSachRepository;
import org.example.datnbbook.repository.DotGiamGiaBoSachChiTietRepository;
import org.example.datnbbook.repository.DotGiamGiaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DotGiamGiaBoSachChiTietService {
    private static final Logger logger = LoggerFactory.getLogger(DotGiamGiaBoSachChiTietService.class);

    @Autowired
    private DotGiamGiaBoSachChiTietRepository repository;

    @Autowired
    private BoSachRepository boSachRepository;

    @Autowired
    private DotGiamGiaRepository dotGiamGiaRepository;

    @Autowired
    private BoSachService boSachService;

    public List<DotGiamGiaBoSachChiTietDTO> getAll() {
        logger.info("[INFO] Fetching all DotGiamGiaBoSachChiTiet");
        return repository.findAll().stream()
                .filter(item -> !item.getDeleted())
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<DotGiamGiaBoSachChiTietDTO> getByIdDotGiamGia(Integer idDotGiamGia) {
        logger.info("[INFO] Fetching DotGiamGiaBoSachChiTiet by idDotGiamGia: {}", idDotGiamGia);
        return repository.findByIdDotGiamGia(idDotGiamGia).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<DotGiamGiaBoSachChiTietDTO> getByIdBoSach(Integer idBoSach) {
        logger.info("[INFO] Fetching DotGiamGiaBoSachChiTiet by idBoSach: {}", idBoSach);
        return repository.findByIdBoSach(idBoSach).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<DotGiamGiaBoSachChiTietDTO> getActiveDiscountsByIdBoSach(Integer idBoSach) {
        logger.info("[INFO] Fetching active discounts for BoSach: {}", idBoSach);
        return repository.findActiveDiscountsByIdBoSach(idBoSach).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ActiveDiscountDetailDTO getActiveDiscountByBoSachId(Integer idBoSach) {
        logger.info("[INFO] Fetching active discount for BoSach: {}", idBoSach);
        try {
            DotGiamGiaBoSachChiTiet activeDiscount = repository.findActiveDiscountByBoSachId(idBoSach);
            if (activeDiscount != null) {
                return new ActiveDiscountDetailDTO(
                    idBoSach, // chiTietSanPhamId (sử dụng idBoSach thay thế)
                    activeDiscount.getIdDotGiamGia().getId(), // dotGiamGiaId
                    activeDiscount.getGiaBanDau(), // giaBanDau
                    activeDiscount.getGiaSauGiam(), // giaSauGiam
                    activeDiscount.getSoTienGiam() // soTienGiam
                );
            }
            return null;
        } catch (Exception e) {
            logger.error("[ERROR] Failed to get active discount for BoSach {}: {}", idBoSach, e.getMessage());
            return null;
        }
    }

    public boolean existsActiveDiscountForBoSach(Integer idBoSach) {
        logger.info("[INFO] Checking if BoSach {} has active discount", idBoSach);
        try {
            return repository.existsActiveDiscountForBoSach(idBoSach);
        } catch (Exception e) {
            logger.error("[ERROR] Failed to check active discount for BoSach {}: {}", idBoSach, e.getMessage());
            return false;
        }
    }

    @Transactional
    public DotGiamGiaBoSachChiTietDTO create(DotGiamGiaBoSachChiTietDTO dto) {
        logger.info("[INFO] Creating DotGiamGiaBoSachChiTiet for BoSach: {} and DotGiamGia: {}", 
                   dto.getIdBoSach(), dto.getIdDotGiamGia());

        // Validate BoSach exists
        BoSach boSach = boSachRepository.findById(dto.getIdBoSach())
                .filter(bs -> !bs.getDeleted())
                .orElseThrow(() -> new RuntimeException("Bộ sách không tồn tại với id: " + dto.getIdBoSach()));

        // Validate DotGiamGia exists
        DotGiamGia dotGiamGia = dotGiamGiaRepository.findById(dto.getIdDotGiamGia())
                .filter(dot -> !dot.getDeleted())
                .orElseThrow(() -> new RuntimeException("Đợt giảm giá không tồn tại với id: " + dto.getIdDotGiamGia()));

        // Check if already exists
        DotGiamGiaBoSachChiTiet existing = repository.findByIdBoSachAndIdDotGiamGia(dto.getIdBoSach(), dto.getIdDotGiamGia());
        if (existing != null && !existing.getDeleted()) {
            throw new RuntimeException("Bộ sách này đã có trong đợt giảm giá này");
        }

        // Set gia ban dau from current BoSach price
        BigDecimal giaBanDau = boSach.getGiaTien();
        dto.setGiaBanDau(giaBanDau);

        // Calculate gia sau giam
        BigDecimal giaSauGiam = calculateGiaSauGiam(giaBanDau, dto.getSoTienGiam(), dto.getSoPhanTramGiam());
        dto.setGiaSauGiam(giaSauGiam);

        DotGiamGiaBoSachChiTiet entity = mapToEntity(dto);
        entity.setIdBoSach(boSach);
        entity.setIdDotGiamGia(dotGiamGia);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        DotGiamGiaBoSachChiTiet saved = repository.save(entity);
        logger.info("[INFO] Created DotGiamGiaBoSachChiTiet with id: {}", saved.getId());

        return mapToDTO(saved);
    }

    @Transactional
    public DotGiamGiaBoSachChiTietDTO update(Long id, DotGiamGiaBoSachChiTietDTO dto) {
        logger.info("[INFO] Updating DotGiamGiaBoSachChiTiet with id: {}", id);
        
        DotGiamGiaBoSachChiTiet existing = repository.findById(id)
                .filter(item -> !item.getDeleted())
                .orElseThrow(() -> new RuntimeException("Chi tiết đợt giảm giá bộ sách không tồn tại với id: " + id));

        // Update fields
        existing.setSoTienGiam(dto.getSoTienGiam());
        existing.setSoPhanTramGiam(dto.getSoPhanTramGiam());
        existing.setTrangThai(dto.getTrangThai());
        existing.setUpdatedAt(LocalDateTime.now());

        // Recalculate gia sau giam
        BigDecimal giaSauGiam = calculateGiaSauGiam(existing.getGiaBanDau(), dto.getSoTienGiam(), dto.getSoPhanTramGiam());
        existing.setGiaSauGiam(giaSauGiam);

        DotGiamGiaBoSachChiTiet saved = repository.save(existing);
        logger.info("[INFO] Updated DotGiamGiaBoSachChiTiet with id: {}", saved.getId());

        return mapToDTO(saved);
    }

    @Transactional
    public void delete(Long id) {
        logger.info("[INFO] Deleting DotGiamGiaBoSachChiTiet with id: {}", id);
        
        DotGiamGiaBoSachChiTiet entity = repository.findById(id)
                .filter(item -> !item.getDeleted())
                .orElseThrow(() -> new RuntimeException("Chi tiết đợt giảm giá bộ sách không tồn tại với id: " + id));

        entity.setDeleted(true);
        entity.setUpdatedAt(LocalDateTime.now());
        repository.save(entity);
        
        logger.info("[INFO] Deleted DotGiamGiaBoSachChiTiet with id: {}", id);
    }

    @Transactional
    public void applyDiscounts() {
        logger.info("[INFO] Applying active discounts to BoSach");
        
        List<DotGiamGiaBoSachChiTiet> activeDiscounts = repository.findAllActiveDiscounts();
        
        for (DotGiamGiaBoSachChiTiet discount : activeDiscounts) {
            BoSach boSach = discount.getIdBoSach();
            // Only update if current price is still the original price
            if (boSach.getGiaTien().equals(discount.getGiaBanDau())) {
                boSach.setGiaTien(discount.getGiaSauGiam());
                boSach.setUpdatedAt(java.time.Instant.now());
                boSachRepository.save(boSach);
                logger.info("[INFO] Applied discount to BoSach {}: {} -> {}", 
                           boSach.getId(), discount.getGiaBanDau(), discount.getGiaSauGiam());
            }
        }
    }

    @Transactional
    public void revertDiscounts() {
        logger.info("[INFO] Reverting discounts for expired campaigns");
        
        List<DotGiamGiaBoSachChiTiet> expiredDiscounts = repository.findAll().stream()
                .filter(item -> !item.getDeleted())
                .filter(item -> item.getIdDotGiamGia().getNgayKetThuc().isBefore(java.time.Instant.now()))
                .collect(Collectors.toList());
        
        for (DotGiamGiaBoSachChiTiet discount : expiredDiscounts) {
            BoSach boSach = discount.getIdBoSach();
            // Revert to original price
            boSach.setGiaTien(discount.getGiaBanDau());
            boSach.setUpdatedAt(java.time.Instant.now());
            boSachRepository.save(boSach);
            
            // Mark discount as inactive
            discount.setTrangThai(false);
            discount.setUpdatedAt(LocalDateTime.now());
            repository.save(discount);
            
            logger.info("[INFO] Reverted discount for BoSach {}: {} -> {}", 
                       boSach.getId(), discount.getGiaSauGiam(), discount.getGiaBanDau());
        }
    }

    private BigDecimal calculateGiaSauGiam(BigDecimal giaBanDau, BigDecimal soTienGiam, BigDecimal soPhanTramGiam) {
        BigDecimal giaSauGiam = giaBanDau;
        
        // Apply percentage discount first
        if (soPhanTramGiam != null && soPhanTramGiam.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal giamTheoPhanTram = giaBanDau.multiply(soPhanTramGiam).divide(new BigDecimal("100"));
            giaSauGiam = giaSauGiam.subtract(giamTheoPhanTram);
        }
        
        // Apply fixed amount discount
        if (soTienGiam != null && soTienGiam.compareTo(BigDecimal.ZERO) > 0) {
            giaSauGiam = giaSauGiam.subtract(soTienGiam);
        }
        
        // Ensure price doesn't go below 0
        if (giaSauGiam.compareTo(BigDecimal.ZERO) < 0) {
            giaSauGiam = BigDecimal.ZERO;
        }
        
        return giaSauGiam;
    }

    private DotGiamGiaBoSachChiTietDTO mapToDTO(DotGiamGiaBoSachChiTiet entity) {
        DotGiamGiaBoSachChiTietDTO dto = new DotGiamGiaBoSachChiTietDTO();
        dto.setId(entity.getId());
        dto.setIdBoSach(entity.getIdBoSach().getId());
        dto.setTenBoSach(entity.getIdBoSach().getTenBoSach());
        dto.setMaBoSach(entity.getIdBoSach().getMaBoSach());
        dto.setIdDotGiamGia(entity.getIdDotGiamGia().getId());
        dto.setTenDotGiamGia(entity.getIdDotGiamGia().getTenDotGiamGia());
        dto.setSoTienGiam(entity.getSoTienGiam());
        dto.setSoPhanTramGiam(entity.getSoPhanTramGiam());
        dto.setGiaSauGiam(entity.getGiaSauGiam());
        dto.setGiaBanDau(entity.getGiaBanDau());
        dto.setTrangThai(entity.getTrangThai());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private DotGiamGiaBoSachChiTiet mapToEntity(DotGiamGiaBoSachChiTietDTO dto) {
        DotGiamGiaBoSachChiTiet entity = new DotGiamGiaBoSachChiTiet();
        entity.setId(dto.getId());
        entity.setSoTienGiam(dto.getSoTienGiam());
        entity.setSoPhanTramGiam(dto.getSoPhanTramGiam());
        entity.setGiaSauGiam(dto.getGiaSauGiam());
        entity.setGiaBanDau(dto.getGiaBanDau());
        entity.setTrangThai(dto.getTrangThai());
        entity.setDeleted(false);
        return entity;
    }
}
