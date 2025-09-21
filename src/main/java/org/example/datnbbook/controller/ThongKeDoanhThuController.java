package org.example.datnbbook.controller;

import org.example.datnbbook.repository.HoaDonRepository;
import org.example.datnbbook.repository.HoaDonChiTietRepository;
import org.example.datnbbook.repository.KhachHangRepository;
import org.example.datnbbook.repository.SanPhamRepository;
import org.example.datnbbook.repository.ChiTietSanPhamRepository;
import org.example.datnbbook.repository.BoSachRepository;
import org.example.datnbbook.model.HoaDon;
import org.example.datnbbook.model.HoaDonChiTiet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/thong-ke-doanh-thu")
@CrossOrigin(origins = "*")
public class ThongKeDoanhThuController {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;
    
    @Autowired
    private SanPhamRepository sanPhamRepository;
    
    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;
    
    @Autowired
    private BoSachRepository boSachRepository;

    // Thống kê tổng quan
    @GetMapping(value = "/tong-quan", produces = "application/json; charset=UTF-8")
    public Map<String, Object> getThongKeTongQuan() {
        Map<String, Object> result = new HashMap<>();
        
        // Tổng doanh thu (chỉ tính đơn hàng đã hoàn thành)
        List<HoaDon> allHoaDon = hoaDonRepository.findAll();
        BigDecimal tongDoanhThu = allHoaDon.stream()
                .filter(hd -> hd.getTongTien() != null &&
                             (hd.getTrangThai() != null && 
                              (hd.getTrangThai().trim().equals("Hoàn thành") || 
                               hd.getTrangThai().trim().equals("Đã giao") || 
                               hd.getTrangThai().trim().equals("Đã thanh toán") ||
                               hd.getTrangThai().trim().equals("DA_GIAO"))))
                .map(HoaDon::getTongTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Tổng số đơn hàng
        long tongDonHang = allHoaDon.size();
        
        // Đơn hàng hôm nay (cả ngay_dat_hang và ngay_tao)
        LocalDate today = LocalDate.now();
        long donHangHomNay = allHoaDon.stream()
                .filter(hd -> {
                    if (hd.getNgayDatHang() != null && 
                        hd.getNgayDatHang().atZone(ZoneId.systemDefault()).toLocalDate().equals(today)) {
                        return true;
                    }
                    if (hd.getNgayTao() != null && 
                        hd.getNgayTao().atZone(ZoneId.systemDefault()).toLocalDate().equals(today)) {
                        return true;
                    }
                    return false;
                })
                .count();
        
        // Doanh thu hôm nay (chỉ tính đơn hàng đã hoàn thành)
        BigDecimal doanhThuHomNay = allHoaDon.stream()
                .filter(hd -> {
                    // Kiểm tra ngày (cả ngay_dat_hang và ngay_tao)
                    boolean isToday = false;
                    if (hd.getNgayDatHang() != null && 
                        hd.getNgayDatHang().atZone(ZoneId.systemDefault()).toLocalDate().equals(today)) {
                        isToday = true;
                    } else if (hd.getNgayTao() != null && 
                               hd.getNgayTao().atZone(ZoneId.systemDefault()).toLocalDate().equals(today)) {
                        isToday = true;
                    }
                    
                    return isToday && hd.getTongTien() != null &&
                           (hd.getTrangThai() != null && 
                            (hd.getTrangThai().trim().equals("Hoàn thành") || 
                             hd.getTrangThai().trim().equals("Đã giao") || 
                             hd.getTrangThai().trim().equals("Đã thanh toán") ||
                             hd.getTrangThai().trim().equals("DA_GIAO")));
                })
                .map(HoaDon::getTongTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Tổng khách hàng
        long tongKhachHang = khachHangRepository.count();
        
        // Tổng sản phẩm (tách riêng từng loại, chỉ đếm sản phẩm chưa bị xóa)
        long tongSanPhamChinh = sanPhamRepository.findAll().stream()
                .filter(sp -> sp.getDeleted() == null || !sp.getDeleted())
                .count();
        long tongChiTietSanPham = chiTietSanPhamRepository.findAll().stream()
                .filter(ctsp -> ctsp.getDeleted() == null || !ctsp.getDeleted())
                .count();
        long tongBoSach = boSachRepository.findAll().stream()
                .filter(bs -> bs.getDeleted() == null || !bs.getDeleted())
                .count();
        long tongSanPham = tongSanPhamChinh + tongChiTietSanPham + tongBoSach;
        
        result.put("tongDoanhThu", tongDoanhThu);
        result.put("tongDonHang", tongDonHang);
        result.put("donHangHomNay", donHangHomNay);
        result.put("doanhThuHomNay", doanhThuHomNay);
        result.put("tongKhachHang", tongKhachHang);
        result.put("tongSanPham", tongSanPham);
        result.put("tongSanPhamChinh", tongSanPhamChinh);
        result.put("tongChiTietSanPham", tongChiTietSanPham);
        result.put("tongBoSach", tongBoSach);
        
        return result;
    }


    // Thống kê doanh thu theo ngày (14 ngày gần nhất)
    @GetMapping(value = "/doanh-thu-theo-ngay", produces = "application/json; charset=UTF-8")
    public List<Map<String, Object>> getDoanhThuTheoNgay() {
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(13); // 14 ngày gần nhất (bao gồm hôm nay)
        
        // Lấy 14 ngày gần nhất, sắp xếp từ cũ đến mới
        for (LocalDate date = startDate; !date.isAfter(today); date = date.plusDays(1)) {
            final LocalDate currentDate = date; // Tạo biến final cho lambda
            List<HoaDon> hoaDonNgay = hoaDonRepository.findAll().stream()
                    .filter(hd -> {
                        // Kiểm tra ngay_dat_hang
                        if (hd.getNgayDatHang() != null && 
                            hd.getNgayDatHang().atZone(ZoneId.systemDefault()).toLocalDate().equals(currentDate)) {
                            return true;
                        }
                        // Kiểm tra ngay_tao (cho đơn hàng tại quầy)
                        if (hd.getNgayTao() != null && 
                            hd.getNgayTao().atZone(ZoneId.systemDefault()).toLocalDate().equals(currentDate)) {
                            return true;
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
            
            // Tính doanh thu từ các đơn hàng đã hoàn thành hoặc đã thanh toán
            BigDecimal doanhThu = hoaDonNgay.stream()
                    .filter(hd -> hd.getTongTien() != null && 
                                 (hd.getTrangThai() != null && 
                                  (hd.getTrangThai().trim().equals("Hoàn thành") || 
                                   hd.getTrangThai().trim().equals("Đã giao") || 
                                   hd.getTrangThai().trim().equals("Đã thanh toán") ||
                                   hd.getTrangThai().trim().equals("DA_GIAO"))))
                    .map(HoaDon::getTongTien)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            Map<String, Object> dayData = new HashMap<>();
            // Hiển thị "Hôm nay" cho ngày hiện tại, ngày khác hiển thị dd/MM
            String dayLabel = date.equals(today) ? "Hôm nay" : date.format(DateTimeFormatter.ofPattern("dd/MM"));
            dayData.put("ngay", dayLabel);
            dayData.put("doanhThu", doanhThu);
            dayData.put("soDonHang", hoaDonNgay.size());
            
            result.add(dayData);
        }
        
        return result;
    }

    // Thống kê doanh thu theo tháng (12 tháng trong năm hiện tại)
    @GetMapping(value = "/doanh-thu-theo-thang", produces = "application/json; charset=UTF-8")
    public List<Map<String, Object>> getDoanhThuTheoThang() {
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        
        // Lấy tất cả 12 tháng trong năm hiện tại, sắp xếp từ cũ đến mới
        for (int month = 1; month <= 12; month++) {
            LocalDate monthStart = LocalDate.of(currentYear, month, 1);
            LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());
            
            List<HoaDon> hoaDonThang = hoaDonRepository.findAll().stream()
                    .filter(hd -> {
                        // Kiểm tra ngay_dat_hang
                        if (hd.getNgayDatHang() != null) {
                            LocalDate orderDate = hd.getNgayDatHang().atZone(ZoneId.systemDefault()).toLocalDate();
                            if (orderDate.isAfter(monthStart.minusDays(1)) && orderDate.isBefore(monthEnd.plusDays(1))) {
                                return true;
                            }
                        }
                        // Kiểm tra ngay_tao (cho đơn hàng tại quầy)
                        if (hd.getNgayTao() != null) {
                            LocalDate createDate = hd.getNgayTao().atZone(ZoneId.systemDefault()).toLocalDate();
                            if (createDate.isAfter(monthStart.minusDays(1)) && createDate.isBefore(monthEnd.plusDays(1))) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
            
            // Tính doanh thu từ các đơn hàng đã hoàn thành hoặc đã thanh toán
            BigDecimal doanhThu = hoaDonThang.stream()
                    .filter(hd -> hd.getTongTien() != null && 
                                 (hd.getTrangThai() != null && 
                                  (hd.getTrangThai().trim().equals("Hoàn thành") || 
                                   hd.getTrangThai().trim().equals("Đã giao") || 
                                   hd.getTrangThai().trim().equals("Đã thanh toán") ||
                                   hd.getTrangThai().trim().equals("DA_GIAO"))))
                    .map(HoaDon::getTongTien)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            Map<String, Object> monthData = new HashMap<>();
            // Hiển thị "Tháng này" cho tháng hiện tại
            String monthLabel;
            if (month == today.getMonthValue()) {
                monthLabel = "Tháng này";
            } else {
                monthLabel = monthStart.format(DateTimeFormatter.ofPattern("MM/yyyy"));
            }
            monthData.put("thang", monthLabel);
            monthData.put("doanhThu", doanhThu);
            monthData.put("soDonHang", hoaDonThang.size());
            
            result.add(monthData);
        }
        
        return result;
    }

    // Top 10 đơn hàng có giá trị cao nhất
    @GetMapping("/top-don-hang-cao")
    public List<Map<String, Object>> getTopDonHangCao() {
        return hoaDonRepository.findAll().stream()
                .filter(hd -> hd.getTongTien() != null)
                .sorted((hd1, hd2) -> hd2.getTongTien().compareTo(hd1.getTongTien()))
                .limit(10)
                .map(hd -> {
                    Map<String, Object> orderData = new HashMap<>();
                    orderData.put("id", hd.getId());
                    orderData.put("maHoaDon", hd.getMaHoaDon());
                    orderData.put("tongTien", hd.getTongTien());
                    orderData.put("ngayDatHang", hd.getNgayDatHang());
                    orderData.put("trangThai", hd.getTrangThai());
                    if (hd.getKhachHang() != null) {
                        orderData.put("tenKhachHang", hd.getKhachHang().getHoTen());
                    }
                    return orderData;
                })
                .collect(Collectors.toList());
    }

    // Thống kê đơn hàng theo trạng thái
    @GetMapping("/don-hang-theo-trang-thai")
    public Map<String, Long> getDonHangTheoTrangThai() {
        List<HoaDon> allHoaDon = hoaDonRepository.findAll();
        
        Map<String, Long> result = new HashMap<>();
        
        // Đếm theo trạng thái thực tế có trong database (trim whitespace)
        result.put("choXacNhan", allHoaDon.stream().filter(hd -> {
            String status = hd.getTrangThai() != null ? hd.getTrangThai().trim() : "";
            return "Chờ xác nhận".equals(status) || "CHO_XAC_NHAN".equals(status);
        }).count());
            
        result.put("daXacNhan", allHoaDon.stream().filter(hd -> {
            String status = hd.getTrangThai() != null ? hd.getTrangThai().trim() : "";
            return "Đã xác nhận".equals(status) || "DA_XAC_NHAN".equals(status);
        }).count());
            
        result.put("dangGiao", allHoaDon.stream().filter(hd -> {
            String status = hd.getTrangThai() != null ? hd.getTrangThai().trim() : "";
            return "Đang giao".equals(status) || "DANG_GIAO".equals(status) || "Chờ giao hàng".equals(status);
        }).count());
            
        result.put("daGiao", allHoaDon.stream().filter(hd -> {
            String status = hd.getTrangThai() != null ? hd.getTrangThai().trim() : "";
            return "Hoàn thành".equals(status) || "Đã giao".equals(status) || "DA_GIAO".equals(status) || "Đã thanh toán".equals(status);
        }).count());
            
        result.put("daHuy", allHoaDon.stream().filter(hd -> {
            String status = hd.getTrangThai() != null ? hd.getTrangThai().trim() : "";
            return "Chờ hủy".equals(status) || "Đã hủy".equals(status) || "DA_HUY".equals(status);
        }).count());
        
        return result;
    }

    // Thống kê sản phẩm bán chạy (theo số lượng) - bao gồm cả sản phẩm lẻ và bộ sách
    @GetMapping("/san-pham-ban-chay")
    public List<Map<String, Object>> getSanPhamBanChay() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Lấy sản phẩm lẻ bán chạy
        List<Object[]> topProducts = hoaDonChiTietRepository.findTopSellingProducts(10);
        for (Object[] row : topProducts) {
            Integer productId = (Integer) row[0];
            Long totalSold = ((Number) row[1]).longValue();
            
            // Lấy thông tin chi tiết sản phẩm
            chiTietSanPhamRepository.findById(productId).ifPresent(ctsp -> {
                Map<String, Object> productData = new HashMap<>();
                productData.put("id", productId);
                productData.put("soLuongBan", totalSold.intValue());
                productData.put("tenSanPham", ctsp.getTenChiTietSanPham());
                productData.put("maSanPham", ctsp.getMaChiTietSanPham());
                productData.put("giaBan", ctsp.getGia());
                productData.put("loai", "Sản phẩm lẻ");
                
                // Lấy ảnh sản phẩm
                if (ctsp.getAnhSanPhams() != null && !ctsp.getAnhSanPhams().isEmpty()) {
                    productData.put("hinhAnh", ctsp.getAnhSanPhams().get(0).getUrl());
                } else {
                    productData.put("hinhAnh", null);
                }
                
                // Tính doanh thu từ các đơn hàng đã hoàn thành
                BigDecimal doanhThu = hoaDonChiTietRepository.findAll().stream()
                    .filter(ct -> ct.getChiTietSanPham() != null && 
                                 ct.getChiTietSanPham().getId().equals(productId) &&
                                 ct.getHoaDon() != null &&
                                 "Hoàn thành".equals(ct.getHoaDon().getTrangThai()))
                    .map(ct -> ct.getGiaSanPham().multiply(BigDecimal.valueOf(ct.getSoLuong())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                productData.put("doanhThu", doanhThu);
                result.add(productData);
            });
        }
        
        // Lấy bộ sách bán chạy
        List<Object[]> topBookSets = hoaDonChiTietRepository.findTopSellingBookSets(10);
        for (Object[] row : topBookSets) {
            Integer boSachId = (Integer) row[0];
            Long totalSold = ((Number) row[1]).longValue();
            
            // Lấy thông tin bộ sách
            boSachRepository.findById(boSachId).ifPresent(boSach -> {
                Map<String, Object> productData = new HashMap<>();
                productData.put("id", boSachId);
                productData.put("soLuongBan", totalSold.intValue());
                productData.put("tenSanPham", boSach.getTenBoSach());
                productData.put("maSanPham", boSach.getMaBoSach());
                productData.put("giaBan", boSach.getGiaTien());
                productData.put("loai", "Bộ sách");
                productData.put("hinhAnh", boSach.getUrl());
                
                // Tính doanh thu từ các đơn hàng đã hoàn thành
                BigDecimal doanhThu = hoaDonChiTietRepository.findAll().stream()
                    .filter(ct -> ct.getBoSach() != null && 
                                 ct.getBoSach().getId().equals(boSachId) &&
                                 ct.getHoaDon() != null &&
                                 "Hoàn thành".equals(ct.getHoaDon().getTrangThai()))
                    .map(ct -> ct.getGiaSanPham().multiply(BigDecimal.valueOf(ct.getSoLuong())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                productData.put("doanhThu", doanhThu);
                result.add(productData);
            });
        }
        
        // Sắp xếp theo số lượng bán và giới hạn 10 sản phẩm
        return result.stream()
                .sorted((a, b) -> ((Integer) b.get("soLuongBan")).compareTo((Integer) a.get("soLuongBan")))
                .limit(10)
                .collect(Collectors.toList());
    }


    // Thống kê doanh thu theo năm (5 năm gần nhất)
    @GetMapping(value = "/doanh-thu-theo-nam", produces = "application/json; charset=UTF-8")
    public List<Map<String, Object>> getDoanhThuTheoNam() {
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        // Sắp xếp từ năm cũ đến năm mới
        for (int i = 4; i >= 0; i--) {
            int year = today.getYear() - i;
            LocalDate yearStart = LocalDate.of(year, 1, 1);
            LocalDate yearEnd = LocalDate.of(year, 12, 31);
            
            List<HoaDon> hoaDonNam = hoaDonRepository.findAll().stream()
                    .filter(hd -> {
                        // Kiểm tra ngay_dat_hang
                        if (hd.getNgayDatHang() != null) {
                            LocalDate orderDate = hd.getNgayDatHang().atZone(ZoneId.systemDefault()).toLocalDate();
                            if (orderDate.isAfter(yearStart.minusDays(1)) && orderDate.isBefore(yearEnd.plusDays(1))) {
                                return true;
                            }
                        }
                        // Kiểm tra ngay_tao (cho đơn hàng tại quầy)
                        if (hd.getNgayTao() != null) {
                            LocalDate createDate = hd.getNgayTao().atZone(ZoneId.systemDefault()).toLocalDate();
                            if (createDate.isAfter(yearStart.minusDays(1)) && createDate.isBefore(yearEnd.plusDays(1))) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
            
            // Tính doanh thu từ các đơn hàng đã hoàn thành hoặc đã thanh toán
            BigDecimal doanhThu = hoaDonNam.stream()
                    .filter(hd -> hd.getTongTien() != null && 
                                 (hd.getTrangThai() != null && 
                                  (hd.getTrangThai().trim().equals("Hoàn thành") || 
                                   hd.getTrangThai().trim().equals("Đã giao") || 
                                   hd.getTrangThai().trim().equals("Đã thanh toán") ||
                                   hd.getTrangThai().trim().equals("DA_GIAO"))))
                    .map(HoaDon::getTongTien)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            Map<String, Object> yearData = new HashMap<>();
            // Hiển thị "Năm này" cho năm hiện tại
            String yearLabel;
            if (i == 0) {
                yearLabel = "Năm này";
            } else {
                yearLabel = String.valueOf(year);
            }
            yearData.put("nam", yearLabel);
            yearData.put("doanhThu", doanhThu);
            yearData.put("soDonHang", hoaDonNam.size());
            
            result.add(yearData);
        }
        
        return result;
    }

    // Thống kê tổng hợp theo thời gian (ngày/tháng/năm)
    @GetMapping("/thong-ke-theo-thoi-gian")
    public Map<String, Object> getThongKeTheoThoiGian(@RequestParam(defaultValue = "ngay") String loai) {
        Map<String, Object> result = new HashMap<>();
        
        switch (loai.toLowerCase()) {
            case "ngay":
                result.put("data", getDoanhThuTheoNgay());
                result.put("loai", "ngay");
                result.put("title", "Doanh thu các ngày trong tháng");
                break;
            case "thang":
                result.put("data", getDoanhThuTheoThang());
                result.put("loai", "thang");
                result.put("title", "Doanh thu 12 tháng trong năm");
                break;
            case "nam":
                result.put("data", getDoanhThuTheoNam());
                result.put("loai", "nam");
                result.put("title", "Doanh thu 5 năm gần nhất");
                break;
            default:
                result.put("data", getDoanhThuTheoNgay());
                result.put("loai", "ngay");
                result.put("title", "Doanh thu các ngày trong tháng");
        }
        
        return result;
    }

    // So sánh doanh thu theo thời gian
    @GetMapping("/so-sanh-doanh-thu")
    public Map<String, Object> getSoSanhDoanhThu(@RequestParam(defaultValue = "thang") String loai) {
        Map<String, Object> result = new HashMap<>();
        LocalDate today = LocalDate.now();
        
        if ("thang".equals(loai)) {
            // So sánh tháng này vs tháng trước
            LocalDate thangNay = today.withDayOfMonth(1);
            LocalDate thangTruoc = thangNay.minusMonths(1);
            LocalDate cuoiThangNay = thangNay.withDayOfMonth(thangNay.lengthOfMonth());
            LocalDate cuoiThangTruoc = thangTruoc.withDayOfMonth(thangTruoc.lengthOfMonth());
            
            BigDecimal doanhThuThangNay = getDoanhThuTrongKhoang(thangNay, cuoiThangNay);
            BigDecimal doanhThuThangTruoc = getDoanhThuTrongKhoang(thangTruoc, cuoiThangTruoc);
            
            result.put("thangNay", doanhThuThangNay);
            result.put("thangTruoc", doanhThuThangTruoc);
            result.put("phanTramThayDoi", tinhPhanTramThayDoi(doanhThuThangTruoc, doanhThuThangNay));
            result.put("loai", "thang");
        } else if ("nam".equals(loai)) {
            // So sánh năm này vs năm trước
            LocalDate namNay = LocalDate.of(today.getYear(), 1, 1);
            LocalDate namTruoc = LocalDate.of(today.getYear() - 1, 1, 1);
            LocalDate cuoiNamNay = LocalDate.of(today.getYear(), 12, 31);
            LocalDate cuoiNamTruoc = LocalDate.of(today.getYear() - 1, 12, 31);
            
            BigDecimal doanhThuNamNay = getDoanhThuTrongKhoang(namNay, cuoiNamNay);
            BigDecimal doanhThuNamTruoc = getDoanhThuTrongKhoang(namTruoc, cuoiNamTruoc);
            
            result.put("namNay", doanhThuNamNay);
            result.put("namTruoc", doanhThuNamTruoc);
            result.put("phanTramThayDoi", tinhPhanTramThayDoi(doanhThuNamTruoc, doanhThuNamNay));
            result.put("loai", "nam");
        }
        
        return result;
    }

    // Helper method để tính doanh thu trong khoảng thời gian
    private BigDecimal getDoanhThuTrongKhoang(LocalDate start, LocalDate end) {
        return hoaDonRepository.findAll().stream()
                .filter(hd -> hd.getNgayDatHang() != null && 
                             hd.getNgayDatHang().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(start.minusDays(1)) &&
                             hd.getNgayDatHang().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(end.plusDays(1)) &&
                             hd.getTongTien() != null)
                .map(HoaDon::getTongTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // API test để kiểm tra tất cả trạng thái có trong database
    @GetMapping("/debug-trang-thai")
    public Map<String, Object> debugTrangThai() {
        List<HoaDon> allHoaDon = hoaDonRepository.findAll();
        
        Map<String, Object> result = new HashMap<>();
        Map<String, Long> statusCount = new HashMap<>();
        
        // Đếm tất cả trạng thái có trong database
        allHoaDon.forEach(hd -> {
            String status = hd.getTrangThai();
            statusCount.put(status, statusCount.getOrDefault(status, 0L) + 1);
        });
        
        result.put("totalOrders", allHoaDon.size());
        result.put("statusCount", statusCount);
        result.put("allStatuses", allHoaDon.stream().map(HoaDon::getTrangThai).distinct().collect(Collectors.toList()));
        
        return result;
    }
    
    // API tạo đơn hàng test hôm nay
    @PostMapping("/create-test-order")
    public Map<String, Object> createTestOrder() {
        try {
            // Tạo đơn hàng test với ngày hôm nay
            HoaDon testOrder = new HoaDon();
            testOrder.setNgayDatHang(Instant.now());
            testOrder.setTrangThai("Hoàn thành");
            testOrder.setTongTien(new BigDecimal("100000"));
            testOrder.setTenNguoiNhan("Test Customer");
            testOrder.setDiaChi("Test Address");
            testOrder.setSoDienThoaiNguoiNhan("0123456789");
            testOrder.setLoaiHoaDon("Tại quầy");
            
            HoaDon savedOrder = hoaDonRepository.save(testOrder);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Đã tạo đơn hàng test thành công");
            result.put("orderId", savedOrder.getId());
            result.put("ngayDatHang", savedOrder.getNgayDatHang());
            result.put("trangThai", savedOrder.getTrangThai());
            result.put("tongTien", savedOrder.getTongTien());
            
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "Lỗi khi tạo đơn hàng test: " + e.getMessage());
            return result;
        }
    }

    // Debug API để kiểm tra đơn hàng hôm nay
    @GetMapping("/debug-hom-nay")
    public Map<String, Object> debugHomNay() {
        LocalDate today = LocalDate.now();
        List<HoaDon> allHoaDon = hoaDonRepository.findAll();
        
        // Lấy tất cả đơn hàng hôm nay (kiểm tra cả ngay_tao và ngay_dat_hang)
        List<HoaDon> hoaDonHomNay = allHoaDon.stream()
                .filter(hd -> {
                    // Kiểm tra ngay_dat_hang
                    if (hd.getNgayDatHang() != null) {
                        LocalDate orderDate = hd.getNgayDatHang().atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate orderDateUTC = hd.getNgayDatHang().atZone(ZoneId.of("UTC")).toLocalDate();
                        if (orderDate.equals(today) || orderDateUTC.equals(today)) {
                            return true;
                        }
                    }
                    
                    // Kiểm tra ngay_tao (cho đơn hàng tại quầy)
                    if (hd.getNgayTao() != null) {
                        LocalDate createDate = hd.getNgayTao().atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate createDateUTC = hd.getNgayTao().atZone(ZoneId.of("UTC")).toLocalDate();
                        if (createDate.equals(today) || createDateUTC.equals(today)) {
                            return true;
                        }
                    }
                    
                    return false;
                })
                .collect(Collectors.toList());
        
        // Lấy đơn hàng hôm nay đã hoàn thành
        List<HoaDon> hoaDonHoanThanh = hoaDonHomNay.stream()
                .filter(hd -> hd.getTrangThai() != null && 
                             (hd.getTrangThai().trim().equals("Hoàn thành") || 
                              hd.getTrangThai().trim().equals("Đã giao") || 
                              hd.getTrangThai().trim().equals("Đã thanh toán") ||
                              hd.getTrangThai().trim().equals("DA_GIAO")))
                .collect(Collectors.toList());
        
        // Tính doanh thu hôm nay
        BigDecimal doanhThuHomNay = hoaDonHoanThanh.stream()
                .filter(hd -> hd.getTongTien() != null)
                .map(HoaDon::getTongTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Lấy 10 đơn hàng gần nhất để debug
        List<HoaDon> recentOrders = allHoaDon.stream()
                .filter(hd -> hd.getNgayDatHang() != null)
                .sorted((hd1, hd2) -> hd2.getNgayDatHang().compareTo(hd1.getNgayDatHang()))
                .limit(10)
                .collect(Collectors.toList());
        
        Map<String, Object> result = new HashMap<>();
        result.put("today", today.toString());
        result.put("totalOrdersToday", hoaDonHomNay.size());
        result.put("completedOrdersToday", hoaDonHoanThanh.size());
        result.put("revenueToday", doanhThuHomNay);
        
        // Chi tiết từng đơn hàng hôm nay
        List<Map<String, Object>> orderDetails = hoaDonHomNay.stream()
                .map(hd -> {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("id", hd.getId());
                    detail.put("ngayDatHang", hd.getNgayDatHang());
                    detail.put("trangThai", hd.getTrangThai());
                    detail.put("tongTien", hd.getTongTien());
                    detail.put("isCompleted", hd.getTrangThai() != null && 
                             (hd.getTrangThai().trim().equals("Hoàn thành") || 
                              hd.getTrangThai().trim().equals("Đã giao") || 
                              hd.getTrangThai().trim().equals("Đã thanh toán") ||
                              hd.getTrangThai().trim().equals("DA_GIAO")));
                    return detail;
                })
                .collect(Collectors.toList());
        
        // Chi tiết 10 đơn hàng gần nhất
        List<Map<String, Object>> recentOrderDetails = recentOrders.stream()
                .map(hd -> {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("id", hd.getId());
                    detail.put("ngayDatHang", hd.getNgayDatHang());
                    detail.put("ngayDatHangLocalDate", hd.getNgayDatHang().atZone(ZoneId.systemDefault()).toLocalDate().toString());
                    detail.put("ngayDatHangUTC", hd.getNgayDatHang().atZone(ZoneId.of("UTC")).toLocalDate().toString());
                    detail.put("trangThai", hd.getTrangThai());
                    detail.put("tongTien", hd.getTongTien());
                    detail.put("isTodayLocal", hd.getNgayDatHang().atZone(ZoneId.systemDefault()).toLocalDate().equals(today));
                    detail.put("isTodayUTC", hd.getNgayDatHang().atZone(ZoneId.of("UTC")).toLocalDate().equals(today));
                    detail.put("isCompleted", hd.getTrangThai() != null && 
                             (hd.getTrangThai().trim().equals("Hoàn thành") || 
                              hd.getTrangThai().trim().equals("Đã giao") || 
                              hd.getTrangThai().trim().equals("Đã thanh toán") ||
                              hd.getTrangThai().trim().equals("DA_GIAO")));
                    return detail;
                })
                .collect(Collectors.toList());
        
        // Lấy tất cả đơn hàng có ngày gần hôm nay (trong vòng 3 ngày)
        List<HoaDon> recentOrders3Days = allHoaDon.stream()
                .filter(hd -> hd.getNgayDatHang() != null)
                .filter(hd -> {
                    LocalDate orderDate = hd.getNgayDatHang().atZone(ZoneId.systemDefault()).toLocalDate();
                    return orderDate.isAfter(today.minusDays(3)) && orderDate.isBefore(today.plusDays(1));
                })
                .sorted((hd1, hd2) -> hd2.getNgayDatHang().compareTo(hd1.getNgayDatHang()))
                .collect(Collectors.toList());
        
        List<Map<String, Object>> recent3DaysDetails = recentOrders3Days.stream()
                .map(hd -> {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("id", hd.getId());
                    detail.put("ngayDatHang", hd.getNgayDatHang());
                    detail.put("ngayDatHangLocalDate", hd.getNgayDatHang().atZone(ZoneId.systemDefault()).toLocalDate().toString());
                    detail.put("ngayDatHangUTC", hd.getNgayDatHang().atZone(ZoneId.of("UTC")).toLocalDate().toString());
                    detail.put("trangThai", hd.getTrangThai());
                    detail.put("tongTien", hd.getTongTien());
                    detail.put("isTodayLocal", hd.getNgayDatHang().atZone(ZoneId.systemDefault()).toLocalDate().equals(today));
                    detail.put("isTodayUTC", hd.getNgayDatHang().atZone(ZoneId.of("UTC")).toLocalDate().equals(today));
                    detail.put("isCompleted", hd.getTrangThai() != null && 
                             (hd.getTrangThai().trim().equals("Hoàn thành") || 
                              hd.getTrangThai().trim().equals("Đã giao") || 
                              hd.getTrangThai().trim().equals("Đã thanh toán") ||
                              hd.getTrangThai().trim().equals("DA_GIAO")));
                    return detail;
                })
                .collect(Collectors.toList());
        
        result.put("orderDetails", orderDetails);
        result.put("recentOrders", recentOrderDetails);
        result.put("recent3DaysOrders", recent3DaysDetails);
        result.put("totalOrdersInDB", allHoaDon.size());
        result.put("todayLocal", today.toString());
        result.put("todayUTC", Instant.now().atZone(ZoneId.of("UTC")).toLocalDate().toString());
        
        return result;
    }

    // Helper method để tính phần trăm thay đổi
    private BigDecimal tinhPhanTramThayDoi(BigDecimal giaTriCu, BigDecimal giaTriMoi) {
        if (giaTriCu.compareTo(BigDecimal.ZERO) == 0) {
            return giaTriMoi.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.valueOf(100) : BigDecimal.ZERO;
        }
        return giaTriMoi.subtract(giaTriCu)
                .divide(giaTriCu, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}
