package org.example.datnbbook.service;

import jakarta.mail.MessagingException;
import org.example.datnbbook.dto.PhieuGiamGiaDTO;
import org.example.datnbbook.mapper.PhieuGiamGiaMapper;
import org.example.datnbbook.model.PhieuGiamGia;
import org.example.datnbbook.model.PhieuGiamGiaKhachHang;
import org.example.datnbbook.repository.KhachHangRepository;
import org.example.datnbbook.repository.PhieuGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PhieuGiamGiaService {

    private static final Logger logger = LoggerFactory.getLogger(PhieuGiamGiaService.class);

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private PhieuGiamGiaKhachHangService phieuGiamGiaKhachHangService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PhieuGiamGiaMapper phieuGiamGiaMapper;

    private static final Map<String, String> SEARCH_MAPPING = new HashMap<>();

    static {
        SEARCH_MAPPING.put("công khai", "PUBLIC");
        SEARCH_MAPPING.put("khách hàng cụ thể", "CUSTOMER");
        SEARCH_MAPPING.put("giảm theo phần trăm", "PERCENT");
        SEARCH_MAPPING.put("giảm theo giá trị tiền", "AMOUNT");
        SEARCH_MAPPING.put("cong khai", "PUBLIC");
        SEARCH_MAPPING.put("khach hang cu the", "CUSTOMER");
        SEARCH_MAPPING.put("giam theo phan tram", "PERCENT");
        SEARCH_MAPPING.put("giam theo gia tri tien", "AMOUNT");
    }

    public boolean existsById(Long id) {
        return phieuGiamGiaRepository.existsById(id);
    }

    public void deleteById(Long id) {
        PhieuGiamGia phieu = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy!"));
        phieu.setDeleted(true);
        phieuGiamGiaRepository.save(phieu);
    }

    public Page<PhieuGiamGiaDTO> getAllDTO(Pageable pageable, String loaiApDung, String loaiPhieu, Boolean trangThai, String tinhTrang, String searchQuery, String fromDate, String toDate) {
        logger.info("Filtering with loaiApDung: {}, loaiPhieu: {}, trangThai: {}, tinhTrang: {}, searchQuery: {}, fromDate: {}, toDate: {}",
                loaiApDung, loaiPhieu, trangThai, tinhTrang, searchQuery, fromDate, toDate);

        String mappedSearchQuery = searchQuery != null ? mapSearchQuery(searchQuery.toLowerCase()) : null;

        // Parse fromDate và toDate
        LocalDateTime fromDateTime = null;
        LocalDateTime toDateTime = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (fromDate != null && !fromDate.isEmpty()) {
            LocalDate parsedFromDate = LocalDate.parse(fromDate, formatter);
            fromDateTime = parsedFromDate.atStartOfDay(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();
        }
        if (toDate != null && !toDate.isEmpty()) {
            LocalDate parsedToDate = LocalDate.parse(toDate, formatter);
            toDateTime = parsedToDate.atTime(23, 59, 59).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();
        }

        return phieuGiamGiaRepository.findByFilters(loaiApDung, loaiPhieu, trangThai, tinhTrang, mappedSearchQuery, fromDateTime, toDateTime, pageable)
                .map(phieu -> {
                    List<PhieuGiamGiaKhachHang> dsKhachHang = phieuGiamGiaKhachHangService.findByPhieuGiamGiaId(phieu.getId());
                    return phieuGiamGiaMapper.toDTO(phieu, dsKhachHang);
                });
    }

    private String mapSearchQuery(String query) {
        for (Map.Entry<String, String> entry : SEARCH_MAPPING.entrySet()) {
            if (query.contains(entry.getKey())) {
                logger.info("Mapping search query '{}' to '{}'", query, entry.getValue());
                return entry.getValue();
            }
        }
        return query;
    }

    public Optional<PhieuGiamGiaDTO> getDTOById(Long id) {
        return phieuGiamGiaRepository.findById(id)
                .filter(p -> p.getDeleted() == null || !p.getDeleted())
                .map(phieu -> {
                    List<PhieuGiamGiaKhachHang> ds = phieuGiamGiaKhachHangService.findByPhieuGiamGiaId(phieu.getId());
                    return phieuGiamGiaMapper.toDTO(phieu, ds);
                });
    }

    public PhieuGiamGiaDTO createPhieuGiamGiaAndReturnDTO(PhieuGiamGiaDTO dto) {
        PhieuGiamGia phieu = new PhieuGiamGia();

        if (dto.getMaPhieuGiamGia() != null && !dto.getMaPhieuGiamGia().isEmpty()) {
            phieu.setMaPhieuGiamGia(dto.getMaPhieuGiamGia());
        } else {
            String nextCode = phieuGiamGiaRepository.getNextSequenceValue();
            phieu.setMaPhieuGiamGia(nextCode);
        }

        phieu.setTenPhieuGiamGia(dto.getTenPhieuGiamGia());
        phieu.setSoPhanTramGiam(dto.getSoPhanTramGiam());
        phieu.setGiaTriGiam(dto.getGiaTriGiam());
        phieu.setGiaTriDonHangToiThieu(dto.getGiaTriDonHangToiThieu());
        phieu.setMoTa(dto.getMoTa());
        phieu.setTrangThai(dto.getTrangThai());
        phieu.setNgayBatDau(dto.getNgayBatDau().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime());
        phieu.setNgayKetThuc(dto.getNgayKetThuc().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime());
        phieu.setSoLuong(dto.getSoLuong());
        phieu.setLoaiApDung(dto.getLoaiApDung());
        phieu.setLoaiPhieu(dto.getLoaiPhieu());
        phieu.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        phieu.setDeleted(false);

        phieu = phieuGiamGiaRepository.save(phieu);

        if (dto.getKhachHangId() != null) {
            var khach = khachHangRepository.findById(dto.getKhachHangId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

            PhieuGiamGiaKhachHang link = new PhieuGiamGiaKhachHang();
            link.setPhieuGiamGia(phieu);
            link.setKhachHang(khach);
            link.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            link.setDeleted(false);
            link.setTrangThai(true);
            link.setSoLuong(1);
            phieuGiamGiaKhachHangService.save(link);

            try {
                emailService.sendVoucherEmail(khach.getEmail(), khach.getHoTen(),
                        dto.getSoPhanTramGiam() != null ? "Giảm theo phần trăm" : "Giảm theo giá trị tiền",
                        dto.getGiaTriGiam().doubleValue());
            } catch (MessagingException e) {
                logger.error("Lỗi khi gửi email cho {}: {}", khach.getEmail(), e.getMessage());
            }
        }

        List<PhieuGiamGiaKhachHang> danhSach = phieuGiamGiaKhachHangService.findByPhieuGiamGiaId(phieu.getId());
        return phieuGiamGiaMapper.toDTO(phieu, danhSach);
    }

    public PhieuGiamGiaDTO updatePhieuGiamGia(PhieuGiamGiaDTO dto) throws MessagingException {
        PhieuGiamGia existingPhieu = phieuGiamGiaRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu giảm giá"));

        if (dto.getMaPhieuGiamGia() != null && !dto.getMaPhieuGiamGia().isEmpty()) {
            existingPhieu.setMaPhieuGiamGia(dto.getMaPhieuGiamGia());
        } else {
            String nextCode = phieuGiamGiaRepository.getNextSequenceValue();
            existingPhieu.setMaPhieuGiamGia(nextCode);
        }

        existingPhieu.setTenPhieuGiamGia(dto.getTenPhieuGiamGia());
        existingPhieu.setSoPhanTramGiam(dto.getSoPhanTramGiam());
        existingPhieu.setGiaTriGiam(dto.getGiaTriGiam());
        existingPhieu.setGiaTriDonHangToiThieu(dto.getGiaTriDonHangToiThieu());
        existingPhieu.setMoTa(dto.getMoTa());
        existingPhieu.setTrangThai(dto.getTrangThai());
        existingPhieu.setNgayBatDau(dto.getNgayBatDau().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime());
        existingPhieu.setNgayKetThuc(dto.getNgayKetThuc().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime());
        existingPhieu.setSoLuong(dto.getSoLuong());
        existingPhieu.setLoaiApDung(dto.getLoaiApDung());
        existingPhieu.setLoaiPhieu(dto.getLoaiPhieu());

        List<PhieuGiamGiaKhachHang> danhSachCu = phieuGiamGiaKhachHangService.findByPhieuGiamGiaId(existingPhieu.getId());
        Long oldCustomerId = danhSachCu.isEmpty() ? null : Long.valueOf(danhSachCu.get(0).getKhachHang().getId());

        if (dto.getKhachHangId() != null && !dto.getKhachHangId().equals(oldCustomerId)) {
            var kh = khachHangRepository.findById(dto.getKhachHangId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

            if (danhSachCu.isEmpty()) {
                PhieuGiamGiaKhachHang link = new PhieuGiamGiaKhachHang();
                link.setPhieuGiamGia(existingPhieu);
                link.setKhachHang(kh);
                link.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                link.setDeleted(false);
                link.setTrangThai(true);
                link.setSoLuong(1);
                phieuGiamGiaKhachHangService.save(link);
            } else {
                PhieuGiamGiaKhachHang link = danhSachCu.get(0);
                link.setKhachHang(kh);
                phieuGiamGiaKhachHangService.save(link);
            }

            emailService.sendVoucherEmail(kh.getEmail(), kh.getHoTen(), existingPhieu.getTenPhieuGiamGia(), existingPhieu.getGiaTriGiam().doubleValue());
        } else if (dto.getKhachHangId() == null && !danhSachCu.isEmpty()) {
            for (PhieuGiamGiaKhachHang link : danhSachCu) {
                phieuGiamGiaKhachHangService.delete(link);
            }
        }

        PhieuGiamGia updated = phieuGiamGiaRepository.save(existingPhieu);
        List<PhieuGiamGiaKhachHang> danhSachMoi = phieuGiamGiaKhachHangService.findByPhieuGiamGiaId(updated.getId());
        return phieuGiamGiaMapper.toDTO(updated, danhSachMoi);
    }

    public boolean existsByMaPhieuGiamGia(String maPhieuGiamGia) {
        return phieuGiamGiaRepository.existsByMaPhieuGiamGia(maPhieuGiamGia);
    }

    public Optional<PhieuGiamGia> getById(Long id) {
        return phieuGiamGiaRepository.findById(id);
    }
}