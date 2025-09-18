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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
        logger.info("Incoming pageable sort: {}", pageable.getSort());

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

        // Map sort fields to database column names and remove duplicates
        Set<String> seenFields = new HashSet<>();
        List<Sort.Order> mappedOrders = pageable.getSort().stream()
                .map(order -> new Sort.Order(order.getDirection(), mapSortField(order.getProperty())))
                .filter(order -> seenFields.add(order.getProperty())) // Only include unique fields
                .collect(Collectors.toList());

        // If no valid sort orders, use default
        if (mappedOrders.isEmpty()) {
            logger.warn("No valid sort orders found. Defaulting to updated_at: DESC");
            mappedOrders = Collections.singletonList(new Sort.Order(Sort.Direction.DESC, "updated_at"));
        }

        Sort mappedSort = Sort.by(mappedOrders);
        Pageable mappedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), mappedSort);

        logger.info("Final sort configuration: {}", mappedSort);

        return phieuGiamGiaRepository.findByFilters(
                        loaiApDung,
                        loaiPhieu,
                        trangThai,
                        tinhTrang,
                        mappedSearchQuery,
                        fromDateTime,
                        toDateTime,
                        mappedPageable)
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

    private String mapSortField(String dtoField) {
        return switch (dtoField) {
            case "maPhieuGiamGia" -> "ma_phieu_giam_gia";
            case "tenPhieuGiamGia" -> "ten_phieu_giam_gia";
            case "giaTriGiam" -> "gia_tri_giam";
            case "soPhanTramGiam" -> "so_phan_tram_giam";
            case "soLuong" -> "so_luong";
            case "ngayBatDau" -> "ngay_bat_dau";
            case "ngayKetThuc" -> "ngay_ket_thuc";
            case "createdAt" -> "created_at";
            case "updatedAt" -> "updated_at";
            case "loaiApDung" -> "loai_ap_dung";
            case "loaiPhieu" -> "loai_phieu";
            case "tinhTrang" -> "tinh_trang";
            case "trangThai" -> "trang_thai";
            case "moTa" -> "mo_ta";
            default -> "updated_at"; // Default sort by updated_at
        };
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
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        phieu.setCreatedAt(now);
        phieu.setUpdatedAt(now);
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
                String voucherType = dto.getSoPhanTramGiam() != null ? "Giảm theo phần trăm" : "Giảm theo giá trị tiền";
                Double voucherValue = dto.getSoPhanTramGiam() != null ? 
                    dto.getSoPhanTramGiam().doubleValue() : dto.getGiaTriGiam().doubleValue();
                
                String startDate = dto.getNgayBatDau() != null ? dto.getNgayBatDau().toString() : null;
                String endDate = dto.getNgayKetThuc() != null ? dto.getNgayKetThuc().toString() : null;
                
                emailService.sendVoucherEmail(khach.getEmail(), khach.getHoTen(), voucherType, voucherValue, false, 
                    phieu.getMaPhieuGiamGia(), dto.getTenPhieuGiamGia(), dto.getMoTa(), startDate, endDate);
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
        existingPhieu.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));

        List<PhieuGiamGiaKhachHang> danhSachCu = phieuGiamGiaKhachHangService.findByPhieuGiamGiaId(existingPhieu.getId());
        Long oldCustomerId = danhSachCu.isEmpty() ? null : Long.valueOf(danhSachCu.get(0).getKhachHang().getId());

        if (dto.getKhachHangId() != null) {
            var kh = khachHangRepository.findById(dto.getKhachHangId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

            if (danhSachCu.isEmpty()) {
                // Tạo mới liên kết khách hàng
                PhieuGiamGiaKhachHang link = new PhieuGiamGiaKhachHang();
                link.setPhieuGiamGia(existingPhieu);
                link.setKhachHang(kh);
                link.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                link.setDeleted(false);
                link.setTrangThai(true);
                link.setSoLuong(1);
                phieuGiamGiaKhachHangService.save(link);
            } else {
                // Cập nhật liên kết khách hàng
                PhieuGiamGiaKhachHang link = danhSachCu.get(0);
                link.setKhachHang(kh);
                phieuGiamGiaKhachHangService.save(link);
            }

            // Gửi email thông báo cho khách hàng (cả khi tạo mới và cập nhật)
            try {
                String voucherType = existingPhieu.getSoPhanTramGiam() != null ? "Giảm theo phần trăm" : "Giảm theo giá trị tiền";
                Double voucherValue = existingPhieu.getSoPhanTramGiam() != null ? 
                    existingPhieu.getSoPhanTramGiam().doubleValue() : existingPhieu.getGiaTriGiam().doubleValue();
                boolean isUpdate = !danhSachCu.isEmpty(); // Nếu đã có liên kết khách hàng thì là cập nhật
                
                String startDate = existingPhieu.getNgayBatDau() != null ? existingPhieu.getNgayBatDau().toString() : null;
                String endDate = existingPhieu.getNgayKetThuc() != null ? existingPhieu.getNgayKetThuc().toString() : null;
                
                emailService.sendVoucherEmail(kh.getEmail(), kh.getHoTen(), voucherType, voucherValue, isUpdate, 
                    existingPhieu.getMaPhieuGiamGia(), existingPhieu.getTenPhieuGiamGia(), existingPhieu.getMoTa(), startDate, endDate);
            } catch (MessagingException e) {
                logger.error("Lỗi khi gửi email cho {}: {}", kh.getEmail(), e.getMessage());
            }
        } else if (dto.getKhachHangId() == null && !danhSachCu.isEmpty()) {
            // Xóa liên kết khách hàng nếu không có khách hàng
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
    
    // Lấy voucher công khai
    public List<PhieuGiamGia> getPublicVouchers(Double totalAmount) {
        LocalDateTime now = LocalDateTime.now();
        List<PhieuGiamGia> publicVouchers = phieuGiamGiaRepository.findPublicVouchers(now);
        
        return publicVouchers.stream()
                .filter(voucher -> {
                    // Kiểm tra giá trị đơn hàng tối thiểu
                    if (totalAmount != null && voucher.getGiaTriDonHangToiThieu() != null) {
                        if (totalAmount < voucher.getGiaTriDonHangToiThieu().doubleValue()) {
                            return false;
                        }
                    }
                    
                    // Kiểm tra số lượng còn lại
                    if (voucher.getSoLuong() != null && voucher.getSoLuong() <= 0) {
                        return false;
                    }
                    
                    return true;
                })
                .collect(Collectors.toList());
    }
    
    // Lấy voucher cá nhân của khách hàng
    public List<PhieuGiamGia> getPersonalVouchersForCustomer(Long customerId, Double totalAmount) {
        List<PhieuGiamGia> personalVouchers = phieuGiamGiaRepository.findPersonalVouchersByCustomerId(customerId);
        
        return personalVouchers.stream()
                .filter(voucher -> {
                    // Kiểm tra voucher có đang hoạt động không
                    if (!voucher.getTrangThai()) {
                        return false;
                    }
                    
                    // Kiểm tra thời gian
                    LocalDateTime now = LocalDateTime.now();
                    if (voucher.getNgayBatDau().isAfter(now) || voucher.getNgayKetThuc().isBefore(now)) {
                        return false;
                    }
                    
                    // Kiểm tra giá trị đơn hàng tối thiểu
                    if (totalAmount != null && voucher.getGiaTriDonHangToiThieu() != null) {
                        if (totalAmount < voucher.getGiaTriDonHangToiThieu().doubleValue()) {
                            return false;
                        }
                    }
                    
                    // Kiểm tra số lượng còn lại
                    if (voucher.getSoLuong() != null && voucher.getSoLuong() <= 0) {
                        return false;
                    }
                    
                    return true;
                })
                .collect(Collectors.toList());
    }
    
    // Trừ số lượng voucher sau thanh toán
    @Transactional
    public PhieuGiamGia deductVoucherQuantity(Long voucherId) {
        PhieuGiamGia voucher = phieuGiamGiaRepository.findById(voucherId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy voucher với ID: " + voucherId));
        
        if (voucher.getSoLuong() != null && voucher.getSoLuong() > 0) {
            voucher.setSoLuong(voucher.getSoLuong() - 1);
            voucher.setUpdatedAt(LocalDateTime.now());
            return phieuGiamGiaRepository.save(voucher);
        } else {
            throw new RuntimeException("Voucher đã hết số lượng hoặc không có số lượng");
        }
    }
}