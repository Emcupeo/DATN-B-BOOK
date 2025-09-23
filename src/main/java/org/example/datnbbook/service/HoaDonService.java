package org.example.datnbbook.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.datnbbook.model.*;
import org.example.datnbbook.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class HoaDonService {

    @Autowired
    private LichSuHoaDonRepository lichSuHoaDonRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HinhThucThanhToanRepository hinhThucThanhToanRepository;

    @Autowired
    private PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private BoSachRepository boSachRepository;

    @Autowired
    private BoSachChiTietRepository boSachChiTietRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    @Autowired
    private EmailService emailService;

    @PersistenceContext
    private EntityManager entityManager;

    public List<HoaDon> getAllHoaDon() {
        // Sử dụng repository để lấy tất cả hóa đơn
        List<HoaDon> hoaDons = hoaDonRepository.findAllByOrderByCreatedAtDesc();

        // Force load từng hóa đơn với relationships
        for (HoaDon hoaDon : hoaDons) {
            // Force load hoaDonChiTiets bằng cách gọi getHoaDonById
            HoaDon fullHoaDon = getHoaDonById(hoaDon.getId());
            if (fullHoaDon != null) {
                // Copy dữ liệu từ fullHoaDon
                hoaDon.setNgayDatHang(fullHoaDon.getNgayDatHang());
                hoaDon.setTongTien(fullHoaDon.getTongTien());
                hoaDon.setHoaDonChiTiets(fullHoaDon.getHoaDonChiTiets());
                hoaDon.setTrangThai(fullHoaDon.getTrangThai());
            }
        }
        return hoaDons;
    }

    public HoaDon getHoaDonById(int id) {
        HoaDon hoaDon = hoaDonRepository.findById(id).orElse(null);
        if (hoaDon != null) {
            // Force load relationships
            hoaDon.getHoaDonChiTiets().size(); // Force load hoaDonChiTiets
            for (HoaDonChiTiet chiTiet : hoaDon.getHoaDonChiTiets()) {
                // Force load chiTietSanPham và boSach
                if (chiTiet.getChiTietSanPham() != null) {
                    chiTiet.getChiTietSanPham().getTenChiTietSanPham();
                }
                if (chiTiet.getBoSach() != null) {
                    chiTiet.getBoSach().getTenBoSach();
                }
            }
        }
        return hoaDon;
    }

    public String generateQRCodeImage(int id) throws WriterException, IOException {
        String qrCodeData = String.valueOf(id);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, 100, 100);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        return "data:image/png;base64," + Base64.getEncoder().encodeToString(pngData);
    }

    public ByteArrayOutputStream printInvoiceToPdf(int id) throws IOException, WriterException {
        HoaDon hoaDon = getHoaDonById(id);
        if (hoaDon == null) {
            throw new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + id);
        }

        BigDecimal tongTienHang = hoaDon.getHoaDonChiTiets() != null ?
                hoaDon.getHoaDonChiTiets().stream()
                        .map(item -> item.getGiaSanPham() != null ? item.getGiaSanPham().multiply(BigDecimal.valueOf(item.getSoLuong())) : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;

        BigDecimal tienGiamGia = BigDecimal.ZERO;
        if (hoaDon.getPhieuGiamGia() != null && hoaDon.getPhieuGiamGia().getSoPhanTramGiam() != null) {
            BigDecimal soPhanTramGiam = hoaDon.getPhieuGiamGia().getSoPhanTramGiam();
            tienGiamGia = tongTienHang.multiply(soPhanTramGiam)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }

        BigDecimal phiShip = (hoaDon.getPhiShip() != null) ? hoaDon.getPhiShip() : BigDecimal.ZERO;
        BigDecimal thanhTien = tongTienHang.subtract(tienGiamGia).add(phiShip);

        String qrCodeImage = generateQRCodeImage(id);

        Context context = new Context();
        context.setVariable("hoaDon", hoaDon);
        context.setVariable("tongTienHang", tongTienHang);
        context.setVariable("tienGiamGia", tienGiamGia);
        context.setVariable("thanhTien", thanhTien);
        context.setVariable("qrCodeImage", qrCodeImage);
        String html = templateEngine.process("invoice", context);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");
        HtmlConverter.convertToPdf(html, out, converterProperties);

        return out;
    }

    public ByteArrayOutputStream exportHoaDonToExcel() throws IOException {
        List<HoaDon> hoaDons = getAllHoaDon().stream()
                .filter(hd -> "Hoàn thành".equals(hd.getTrangThai()))
                .toList();

        if (hoaDons.isEmpty()) {
            throw new IllegalStateException("Không có hóa đơn nào để xuất");
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Danh sách hóa đơn");

            CellStyle hyperlinkStyle = workbook.createCellStyle();
            Font hyperlinkFont = workbook.createFont();
            hyperlinkFont.setUnderline(Font.U_SINGLE);
            hyperlinkFont.setColor(IndexedColors.BLUE.getIndex());
            hyperlinkStyle.setFont(hyperlinkFont);

            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Mã HĐ", "Ngày Tạo", "Ngày Đặt Hàng", "Tổng Thanh Tiền", "Mã Nhân Viên", "Tên Khách Hàng", "Địa Chỉ", "Số Điện Thoại", "Trạng Thái"};
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            int rowIndex = 1;

            CreationHelper creationHelper = workbook.getCreationHelper();

            for (HoaDon hoaDon : hoaDons) {
                Row row = sheet.createRow(rowIndex++);

                Cell idCell = row.createCell(0);
                idCell.setCellValue(hoaDon.getId());
                Hyperlink hyperlink = creationHelper.createHyperlink(HyperlinkType.DOCUMENT);
                hyperlink.setAddress("'Chi tiết hóa đơn - " + hoaDon.getId() + "'!A1");
                idCell.setHyperlink(hyperlink);
                idCell.setCellStyle(hyperlinkStyle);

                row.createCell(1).setCellValue(hoaDon.getMaHoaDon() != null ? hoaDon.getMaHoaDon() : "N/A");
                row.createCell(2).setCellValue(hoaDon.getNgayTao() != null ? dateFormat.format(Date.from(hoaDon.getNgayTao())) : "N/A");
                row.createCell(3).setCellValue(hoaDon.getNgayDatHang() != null ? dateFormat.format(Date.from(hoaDon.getNgayDatHang())) : "N/A");

                BigDecimal tongThanhTien = hoaDon.getHoaDonChiTiets() != null ?
                        hoaDon.getHoaDonChiTiets().stream()
                                .map(item -> item.getGiaSanPham() != null ? item.getGiaSanPham().multiply(BigDecimal.valueOf(item.getSoLuong())) : BigDecimal.ZERO)
                                .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;
                row.createCell(4).setCellValue(tongThanhTien.doubleValue());
                row.createCell(5).setCellValue(hoaDon.getNhanVien() != null && hoaDon.getNhanVien().getMaNhanVien() != null ? hoaDon.getNhanVien().getMaNhanVien() : "N/A");
                row.createCell(6).setCellValue(hoaDon.getTenNguoiNhan() != null ? hoaDon.getTenNguoiNhan() : "N/A");
                row.createCell(7).setCellValue(hoaDon.getDiaChi() != null ? hoaDon.getDiaChi() : "N/A");
                row.createCell(8).setCellValue(hoaDon.getSoDienThoaiNguoiNhan() != null ? hoaDon.getSoDienThoaiNguoiNhan() : "N/A");
                row.createCell(9).setCellValue(hoaDon.getTrangThai() != null ? hoaDon.getTrangThai() : "N/A");

                Sheet chiTietSheet = workbook.createSheet("Chi tiết hóa đơn - " + hoaDon.getId());
                Row chiTietHeaderRow = chiTietSheet.createRow(0);
                String[] chiTietHeaders = {"ID Hóa Đơn", "Tên Sản Phẩm", "Chất liệu", "Loại bìa", "Ngôn ngữ", "Tác giả", "Số Lượng", "Giá Bán", "Tổng Tiền"};
                for (int i = 0; i < chiTietHeaders.length; i++) {
                    Cell chiTietCell = chiTietHeaderRow.createCell(i);
                    chiTietCell.setCellValue(chiTietHeaders[i]);
                    chiTietCell.setCellStyle(headerStyle);
                }

                int chiTietRowIndex = 1;
                if (hoaDon.getHoaDonChiTiets() != null) {
                    for (HoaDonChiTiet chiTiet : hoaDon.getHoaDonChiTiets()) {
                        Row chiTietRow = chiTietSheet.createRow(chiTietRowIndex++);
                        chiTietRow.createCell(0).setCellValue(chiTiet.getHoaDon() != null ? chiTiet.getHoaDon().getId() : 0);
                        chiTietRow.createCell(1).setCellValue(chiTiet.getChiTietSanPham() != null && chiTiet.getChiTietSanPham().getIdSanPham() != null ?
                                chiTiet.getChiTietSanPham().getIdSanPham().getTenSanPham() : "N/A");
                        chiTietRow.createCell(2).setCellValue(chiTiet.getChiTietSanPham() != null && chiTiet.getChiTietSanPham().getIdChatLieu() != null ?
                                chiTiet.getChiTietSanPham().getIdChatLieu().getTenChatLieu() : "N/A");
                        chiTietRow.createCell(3).setCellValue(chiTiet.getChiTietSanPham() != null && chiTiet.getChiTietSanPham().getIdLoaiBia() != null ?
                                chiTiet.getChiTietSanPham().getIdLoaiBia().getTenLoaiBia() : "N/A");
                        chiTietRow.createCell(4).setCellValue(chiTiet.getChiTietSanPham() != null && chiTiet.getChiTietSanPham().getIdNgonNgu() != null ?
                                chiTiet.getChiTietSanPham().getIdNgonNgu().getTenNgonNgu() : "N/A");
                        chiTietRow.createCell(5).setCellValue(chiTiet.getChiTietSanPham() != null && chiTiet.getChiTietSanPham().getIdTacGia() != null ?
                                chiTiet.getChiTietSanPham().getIdTacGia().getTenTacGia() : "N/A");
                        chiTietRow.createCell(6).setCellValue(chiTiet.getSoLuong());
                        chiTietRow.createCell(7).setCellValue(chiTiet.getGiaSanPham() != null ? chiTiet.getGiaSanPham().doubleValue() : 0.0);
                        chiTietRow.createCell(8).setCellValue(chiTiet.getGiaSanPham() != null ?
                                chiTiet.getGiaSanPham().multiply(BigDecimal.valueOf(chiTiet.getSoLuong())).doubleValue() : 0.0);
                    }
                }

                for (int i = 0; i < chiTietHeaders.length; i++) {
                    chiTietSheet.autoSizeColumn(i);
                }
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out;
        }
    }

    @Transactional
    public HoaDon updateTrangThai(int id, String trangThaiMoi) {
        System.out.println("DEBUG: updateTrangThai called with id=" + id + ", trangThaiMoi=" + trangThaiMoi);

        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + id));

        String trangThaiCu = hoaDon.getTrangThai();
        System.out.println("DEBUG: Current trangThai: " + trangThaiCu + ", new trangThai: " + trangThaiMoi);

        // Nếu hủy hóa đơn, khôi phục số lượng
        if ("Đã hủy".equals(trangThaiMoi) || "Hủy".equals(trangThaiMoi)) {
            System.out.println("DEBUG: Order cancellation detected, calling restoreQuantitiesOnCancel");
            restoreQuantitiesOnCancel(hoaDon);
        }

        hoaDon.setTrangThai(trangThaiMoi);
        hoaDonRepository.save(hoaDon);

        LichSuHoaDon lichSu = new LichSuHoaDon();
        lichSu.setHoaDon(hoaDon);
        lichSu.setTrangThaiCu(trangThaiCu);
        lichSu.setTrangThaiMoi(trangThaiMoi);
        lichSu.setCreatedAt(Instant.now());
        lichSuHoaDonRepository.save(lichSu);

        System.out.println("DEBUG: updateTrangThai completed successfully");
        return hoaDon;
    }

    // Alias method cho frontend
    @Transactional
    public HoaDon updateTrangThaiHoaDon(int id, String trangThaiMoi) {
        return updateTrangThai(id, trangThaiMoi);
    }

    @Transactional
    public HoaDon huyDon(int id) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + id));

        String trangThaiCu = hoaDon.getTrangThai();

        // Khôi phục số lượng sản phẩm/bộ sách trước khi hủy
        restoreQuantitiesOnCancel(hoaDon);

        hoaDon.setTrangThai("Đã hủy");
        hoaDon.setDeleted(true);
        hoaDonRepository.save(hoaDon);

        LichSuHoaDon lichSu = new LichSuHoaDon();
        lichSu.setHoaDon(hoaDon);
        lichSu.setTrangThaiCu(trangThaiCu);
        lichSu.setTrangThaiMoi("Đã hủy");
        lichSu.setCreatedAt(Instant.now());
        lichSuHoaDonRepository.save(lichSu);

        return hoaDon;
    }

    // Method khôi phục số lượng khi hủy hóa đơn
    @Transactional
    protected void restoreQuantitiesOnCancel(HoaDon hoaDon) {
        try {
            System.out.println("DEBUG: Starting restoreQuantitiesOnCancel for HoaDon ID: " + hoaDon.getId());

            // Force load hoaDonChiTiets
            hoaDon.getHoaDonChiTiets().size();
            System.out.println("DEBUG: HoaDonChiTiets size: " + hoaDon.getHoaDonChiTiets().size());

            // Force load phieuGiamGia
            PhieuGiamGia phieuGiamGia = hoaDon.getPhieuGiamGia();
            System.out.println("DEBUG: PhieuGiamGia: " + (phieuGiamGia != null ? "Found ID " + phieuGiamGia.getId() + ", quantity: " + phieuGiamGia.getSoLuong() : "null"));

            for (HoaDonChiTiet chiTiet : hoaDon.getHoaDonChiTiets()) {
                if (chiTiet.getChiTietSanPham() != null) {
                    // Khôi phục số lượng cho sản phẩm lẻ
                    ChiTietSanPham chiTietSanPham = chiTiet.getChiTietSanPham();
                    chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() + chiTiet.getSoLuong());
                    chiTietSanPhamRepository.save(chiTietSanPham);
                    System.out.println("DEBUG: Restored " + chiTiet.getSoLuong() + " units for product: " + chiTietSanPham.getTenChiTietSanPham());
                }

                if (chiTiet.getBoSach() != null) {
                    // Khôi phục số lượng cho bộ sách
                    BoSach boSach = chiTiet.getBoSach();
                    boSach.setSoLuong(boSach.getSoLuong() + chiTiet.getSoLuong());
                    boSachRepository.save(boSach);
                    System.out.println("DEBUG: Restored " + chiTiet.getSoLuong() + " units for book set: " + boSach.getTenBoSach());

                    // Khôi phục số lượng cho các sản phẩm trong bộ sách
                    List<BoSachChiTiet> boSachChiTiets = boSachChiTietRepository.findByIdBoSachId(boSach.getId());
                    for (BoSachChiTiet boSachChiTiet : boSachChiTiets) {
                        ChiTietSanPham chiTietSanPham = boSachChiTiet.getIdChiTietSanPham();
                        int soLuongCanKhoiPhuc = boSachChiTiet.getSoLuong() * chiTiet.getSoLuong();
                        chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() + soLuongCanKhoiPhuc);
                        chiTietSanPhamRepository.save(chiTietSanPham);
                        System.out.println("DEBUG: Restored " + soLuongCanKhoiPhuc + " units for product in book set: " + chiTietSanPham.getTenChiTietSanPham());
                    }
                }
            }

            // Khôi phục số lượng phiếu giảm giá nếu có
            if (phieuGiamGia != null) {
                Long oldQuantity = phieuGiamGia.getSoLuong();
                phieuGiamGia.setSoLuong(oldQuantity + 1L);
                phieuGiamGiaRepository.save(phieuGiamGia);
                System.out.println("DEBUG: Restored voucher quantity for ID: " + phieuGiamGia.getId() + ", old: " + oldQuantity + ", new: " + phieuGiamGia.getSoLuong());
            } else {
                System.out.println("DEBUG: No voucher found for this order");
            }

        } catch (Exception e) {
            System.err.println("ERROR: Failed to restore quantities on cancel: " + e.getMessage());
            e.printStackTrace();
            // Không throw exception để không ảnh hưởng đến việc hủy hóa đơn
        }
    }

    @Transactional
    public HoaDon updatePayment(int id, BigDecimal tienMat, BigDecimal chuyenKhoan, Integer phuongThucThanhToanId, String ghiChu, String loaiHoaDon, BigDecimal tienKhachDua, Long phieuGiamGiaId, String phuongThucThanhToan) {
        System.out.println("DEBUG: updatePayment called with id=" + id + ", tienMat=" + tienMat + ", chuyenKhoan=" + chuyenKhoan + ", phuongThucThanhToanId=" + phuongThucThanhToanId + ", loaiHoaDon=" + loaiHoaDon + ", phuongThucThanhToan=" + phuongThucThanhToan);

        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + id));

        String oldTrangThai = hoaDon.getTrangThai();
        System.out.println("DEBUG: Current trangThai: " + oldTrangThai);

        // Tính toán thanhTien
        BigDecimal thanhTien = tienMat.add(chuyenKhoan);
        BigDecimal tienGiamGia = BigDecimal.ZERO;
        if (phieuGiamGiaId != null) {
            PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(phieuGiamGiaId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu giảm giá với ID: " + phieuGiamGiaId));
            if (phieuGiamGia.getGiaTriGiam() != null) {
                tienGiamGia = phieuGiamGia.getGiaTriGiam();
            } else if (phieuGiamGia.getSoPhanTramGiam() != null) {
                tienGiamGia = thanhTien.multiply(phieuGiamGia.getSoPhanTramGiam()).divide(new BigDecimal("100"));
            }
            thanhTien = thanhTien.subtract(tienGiamGia);
            hoaDon.setPhieuGiamGia(phieuGiamGia);
        }

        // Kiểm tra tiền khách đưa
        if ("Tại quầy".equals(loaiHoaDon) && tienMat.compareTo(BigDecimal.ZERO) > 0) {
            if (tienKhachDua.compareTo(thanhTien) < 0) {
                throw new IllegalArgumentException("Tiền khách đưa không đủ để thanh toán!");
            }
        }

        // Tạo HinhThucThanhToan
        HinhThucThanhToan hinhThucThanhToan = new HinhThucThanhToan();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String suffix = timestamp.substring(Math.max(0, timestamp.length() - 6));
        hinhThucThanhToan.setMaHinhThucThanhToan("HT" + suffix);
        hinhThucThanhToan.setTienMat(tienMat);
        hinhThucThanhToan.setChuyenKhoan(chuyenKhoan);
        hinhThucThanhToan.setCreatedAt(Instant.now());
        hinhThucThanhToan.setUpdatedAt(Instant.now());
        hinhThucThanhToan.setDeleted(false);

        // Xử lý phương thức thanh toán
        if ("COD".equalsIgnoreCase(phuongThucThanhToan)) {
            // Không gán phuongThucThanhToanId cho COD
            hinhThucThanhToan.setPhuongThucThanhToan(null);
        } else {
            if (phuongThucThanhToanId == null) {
                throw new IllegalArgumentException("Phương thức thanh toán ID không được để trống cho các phương thức không phải COD!");
            }
            PhuongThucThanhToan phuongThucThanhToanEntity = phuongThucThanhToanRepository.findById(phuongThucThanhToanId)
                    .orElseThrow(() -> new IllegalArgumentException("Phương thức thanh toán không tồn tại với ID: " + phuongThucThanhToanId));
            hinhThucThanhToan.setPhuongThucThanhToan(phuongThucThanhToanEntity);
        }

        hinhThucThanhToan = hinhThucThanhToanRepository.save(hinhThucThanhToan);

        // Cập nhật hóa đơn
        hoaDon.setHinhThucThanhToan(hinhThucThanhToan);
        if (hinhThucThanhToan.getHoaDons() == null) {
            hinhThucThanhToan.setHoaDons(new LinkedHashSet<>());
        }
        hinhThucThanhToan.getHoaDons().add(hoaDon);
        hoaDon.setGhiChu(ghiChu);
        hoaDon.setTongTien(thanhTien);

        // Chuẩn hóa giá trị phương thức thanh toán
        if (phuongThucThanhToan != null) {
            if (hoaDon.getPhuongThucThanhToan() == null || hoaDon.getPhuongThucThanhToan().isEmpty()) {
                if (phuongThucThanhToan.equalsIgnoreCase("COD")) {
                    hoaDon.setPhuongThucThanhToan("COD");
                } else if (phuongThucThanhToan.equalsIgnoreCase("Tra_Truoc")) {
                    hoaDon.setPhuongThucThanhToan("Tra_Truoc");
                } else if (phuongThucThanhToan.equalsIgnoreCase("TIEN_MAT")) {
                    hoaDon.setPhuongThucThanhToan("TIEN_MAT");
                } else {
                    hoaDon.setPhuongThucThanhToan("");
                }
            }
        } else {
            if (hoaDon.getPhuongThucThanhToan() == null) {
                hoaDon.setPhuongThucThanhToan("");
            }
        }
        hoaDon.setUpdatedAt(Instant.now());

        // Timeline trạng thái
        String newTrangThai;
        if ("COD".equalsIgnoreCase(phuongThucThanhToan)) {
            switch (hoaDon.getTrangThai()) {
                case "Tạo hóa đơn":
                    newTrangThai = "Chờ xác nhận";
                    break;
                case "Chờ xác nhận":
                    newTrangThai = "Chờ giao hàng";
                    break;
                case "Chờ giao hàng":
                    newTrangThai = "Đang vận chuyển";
                    break;
                case "Đang vận chuyển":
                    newTrangThai = "Đã giao hàng";
                    break;
                case "Đã giao hàng":
                    newTrangThai = "Đã thanh toán";
                    break;
                case "Đã thanh toán":
                    newTrangThai = "Hoàn thành";
                    break;
                default:
                    newTrangThai = hoaDon.getTrangThai();
            }
        } else if ("Tra_Truoc".equalsIgnoreCase(phuongThucThanhToan)) {
            switch (hoaDon.getTrangThai()) {
                case "Tạo hóa đơn":
                    newTrangThai = "Chờ xác nhận";
                    break;
                case "Thanh toán thành công":
                    newTrangThai = "Chờ giao hàng";
                    break;
                case "Chờ giao hàng":
                    newTrangThai = "Đang vận chuyển";
                    break;
                case "Đang vận chuyển":
                    newTrangThai = "Đã giao hàng";
                    break;
                case "Đã giao hàng":
                    newTrangThai = "Hoàn thành";
                    break;
                default:
                    newTrangThai = hoaDon.getTrangThai();
            }
        } else if ("TIEN_MAT".equalsIgnoreCase(phuongThucThanhToan)) {
            newTrangThai = "Đã thanh toán";
        } else {
            newTrangThai = hoaDon.getTrangThai();
        }
        hoaDon.setTrangThai(newTrangThai);
        System.out.println("DEBUG: Set trangThai to: " + newTrangThai + " for HoaDon ID: " + hoaDon.getId());

        HoaDon updatedHoaDon = hoaDonRepository.saveAndFlush(hoaDon);

        // Không tạo LichSuHoaDon ở đây vì sẽ được tạo trong updateTrangThai
        System.out.println("DEBUG: Updated HoaDon without creating LichSuHoaDon to avoid duplication");

        // Trừ số lượng phiếu giảm giá
        if (phieuGiamGiaId != null) {
            PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(phieuGiamGiaId).orElse(null);
            if (phieuGiamGia != null && phieuGiamGia.getSoLuong() > 0) {
                phieuGiamGia.setSoLuong(phieuGiamGia.getSoLuong() - 1);
                phieuGiamGiaRepository.save(phieuGiamGia);
                System.out.println("DEBUG: Decreased voucher quantity for ID: " + phieuGiamGiaId);
            }
        }

        // Gửi email xác nhận
        if ("Hoàn thành".equals(newTrangThai) || "Đã thanh toán".equals(newTrangThai) || "Chờ xác nhận".equals(newTrangThai) || "Chờ giao hàng".equals(newTrangThai) || "Thanh toán thành công".equals(newTrangThai)) {
            sendOrderConfirmationEmail(updatedHoaDon);
        }

        return updatedHoaDon;
    }

    @Transactional
    public HoaDon createHoaDon(String loaiHoaDon) {
        if (loaiHoaDon == null || loaiHoaDon.isEmpty()) {
            throw new IllegalArgumentException("Loại hóa đơn không được để trống!");
        }

        HoaDon hoaDon = new HoaDon();
        hoaDon.setLoaiHoaDon(loaiHoaDon);
        hoaDon.setTrangThai("Tạo hóa đơn");
        hoaDon.setCreatedAt(Instant.now());
        hoaDon.setNgayTao(Instant.now());
        hoaDon.setDeleted(false);

        NhanVien nhanVien = nhanVienRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên với ID: 1"));
        hoaDon.setNhanVien(nhanVien);

        HoaDon savedHoaDon = hoaDonRepository.save(hoaDon);

        entityManager.refresh(savedHoaDon);

        LichSuHoaDon lichSu = new LichSuHoaDon();
        lichSu.setHoaDon(savedHoaDon);
        lichSu.setTrangThaiCu(null);
        lichSu.setTrangThaiMoi("Tạo hóa đơn");
        lichSu.setCreatedAt(Instant.now());
        lichSuHoaDonRepository.save(lichSu);

        return savedHoaDon;
    }

    @Transactional
    public HoaDon updateCustomerInfo(int id, Long idKhachHang, String tenNguoiNhan, String soDienThoaiNguoiNhan, String diaChiGiaoHang, String emailNguoiNhan) {
        System.out.println("DEBUG: HoaDonService.updateCustomerInfo called");
        System.out.println("DEBUG: id=" + id + ", idKhachHang=" + idKhachHang + ", tenNguoiNhan=" + tenNguoiNhan + ", soDienThoaiNguoiNhan=" + soDienThoaiNguoiNhan + ", diaChiGiaoHang=" + diaChiGiaoHang + ", emailNguoiNhan=" + emailNguoiNhan);

        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + id));

        System.out.println("DEBUG: Found HoaDon: " + hoaDon.getMaHoaDon() + ", current diaChi: " + hoaDon.getDiaChi());

        if (idKhachHang != null) {
            KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với ID: " + idKhachHang));
            hoaDon.setKhachHang(khachHang);
            System.out.println("DEBUG: Set khachHang: " + khachHang.getHoTen());
        } else {
            hoaDon.setKhachHang(null);
            System.out.println("DEBUG: Set khachHang to null");
        }

        hoaDon.setTenNguoiNhan(tenNguoiNhan != null ? tenNguoiNhan : "Khách lẻ");
        hoaDon.setSoDienThoaiNguoiNhan(soDienThoaiNguoiNhan);
        hoaDon.setDiaChi(diaChiGiaoHang);
        hoaDon.setEmailNguoiNhan(emailNguoiNhan);
        hoaDon.setUpdatedAt(Instant.now());

        System.out.println("DEBUG: Before save - diaChi: " + hoaDon.getDiaChi());
        HoaDon updatedHoaDon = hoaDonRepository.save(hoaDon);
        System.out.println("DEBUG: After save - diaChi: " + updatedHoaDon.getDiaChi());

        // Không gửi email ở đây nữa - sẽ gửi sau khi thanh toán hoàn tất để có thông tin chính xác

        return updatedHoaDon;
    }

    @Transactional
    public HoaDonChiTiet addProductToOrder(int orderId, int chiTietSanPhamId, int soLuong, BigDecimal giaSanPham) {
        System.out.println("DEBUG: addProductToOrder called with orderId=" + orderId + ", chiTietSanPhamId=" + chiTietSanPhamId + ", soLuong=" + soLuong + ", giaSanPham=" + giaSanPham);

        HoaDon hoaDon = hoaDonRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + orderId));

        System.out.println("DEBUG: HoaDon trangThai: " + hoaDon.getTrangThai() + ", maHoaDon: " + hoaDon.getMaHoaDon());

        if (!"Chờ xác nhận".equals(hoaDon.getTrangThai()) && !"Tạo hóa đơn".equals(hoaDon.getTrangThai())) {
            throw new IllegalStateException("Chỉ có thể thêm sản phẩm vào hóa đơn ở trạng thái 'Chờ xác nhận' hoặc 'Tạo hóa đơn'. Hiện tại: " + hoaDon.getTrangThai());
        }

        ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(chiTietSanPhamId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với ID: " + chiTietSanPhamId));

        System.out.println("DEBUG: Found ChiTietSanPham: " + chiTietSanPham.getTenChiTietSanPham() + ", deleted=" + chiTietSanPham.getDeleted() + ", soLuongTon=" + chiTietSanPham.getSoLuongTon() + ", id=" + chiTietSanPham.getId());

        if (chiTietSanPham.getDeleted()) {
            throw new IllegalArgumentException("Sản phẩm đã bị xóa!");
        }

        if (chiTietSanPham.getSoLuongTon() < soLuong) {
            throw new IllegalArgumentException("Số lượng tồn kho không đủ!");
        }

        HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
        hoaDonChiTiet.setHoaDon(hoaDon);
        hoaDonChiTiet.setChiTietSanPham(chiTietSanPham);
        hoaDonChiTiet.setSoLuong(soLuong);
        hoaDonChiTiet.setGiaSanPham(giaSanPham);
        hoaDonChiTiet.setThanhTien(giaSanPham.multiply(BigDecimal.valueOf(soLuong)));

        chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() - soLuong);
        chiTietSanPhamRepository.save(chiTietSanPham);

        return hoaDonChiTietRepository.save(hoaDonChiTiet);
    }

    @Transactional
    public HoaDonChiTiet addBoSachToOrder(int orderId, int boSachId, int soLuong, BigDecimal giaSanPham) {
        System.out.println("DEBUG: addBoSachToOrder called with orderId=" + orderId + ", boSachId=" + boSachId + ", soLuong=" + soLuong + ", giaSanPham=" + giaSanPham);

        HoaDon hoaDon = hoaDonRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + orderId));

        System.out.println("DEBUG: HoaDon trangThai: " + hoaDon.getTrangThai() + ", maHoaDon: " + hoaDon.getMaHoaDon());

        if (!"Chờ xác nhận".equals(hoaDon.getTrangThai()) && !"Tạo hóa đơn".equals(hoaDon.getTrangThai())) {
            throw new IllegalStateException("Chỉ có thể thêm bộ sách vào hóa đơn ở trạng thái 'Chờ xác nhận' hoặc 'Tạo hóa đơn'. Hiện tại: " + hoaDon.getTrangThai());
        }

        BoSach boSach = boSachRepository.findById(boSachId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bộ sách với ID: " + boSachId));

        System.out.println("DEBUG: Found BoSach: " + boSach.getTenBoSach() + ", deleted=" + boSach.getDeleted() + ", soLuong=" + boSach.getSoLuong() + ", id=" + boSach.getId());

        if (boSach.getDeleted()) {
            throw new IllegalArgumentException("Bộ sách đã bị xóa!");
        }

        if (boSach.getSoLuong() < soLuong) {
            throw new IllegalArgumentException("Số lượng tồn kho bộ sách không đủ!");
        }

        HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
        hoaDonChiTiet.setHoaDon(hoaDon);
        hoaDonChiTiet.setBoSach(boSach); // Set bộ sách thay vì chi tiết sản phẩm
        hoaDonChiTiet.setSoLuong(soLuong);
        hoaDonChiTiet.setGiaSanPham(giaSanPham);
        hoaDonChiTiet.setThanhTien(giaSanPham.multiply(BigDecimal.valueOf(soLuong)));

        // Trừ số lượng bộ sách
        boSach.setSoLuong(boSach.getSoLuong() - soLuong);
        boSachRepository.save(boSach);

        // Trừ số lượng chi tiết sản phẩm trong bộ sách
        List<BoSachChiTiet> boSachChiTiets = boSachChiTietRepository.findByIdBoSachId(boSachId);
        for (BoSachChiTiet boSachChiTiet : boSachChiTiets) {
            ChiTietSanPham chiTietSanPham = boSachChiTiet.getIdChiTietSanPham();
            int soLuongCanTru = boSachChiTiet.getSoLuong() * soLuong; // Số lượng trong bộ sách * số lượng bộ sách mua

            if (chiTietSanPham.getSoLuongTon() < soLuongCanTru) {
                throw new IllegalArgumentException(
                        String.format("Không đủ số lượng tồn kho cho chi tiết sản phẩm '%s'. Cần: %d, Có: %d",
                                chiTietSanPham.getTenChiTietSanPham(), soLuongCanTru, chiTietSanPham.getSoLuongTon())
                );
            }

            chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() - soLuongCanTru);
            chiTietSanPhamRepository.save(chiTietSanPham);

            System.out.println(String.format("DEBUG: Trừ %d sản phẩm '%s' (cần %d, còn %d)",
                    soLuongCanTru, chiTietSanPham.getTenChiTietSanPham(), soLuongCanTru, chiTietSanPham.getSoLuongTon()));
        }

        return hoaDonChiTietRepository.save(hoaDonChiTiet);
    }

    @Transactional
    public HoaDonChiTiet updateProductQuantity(int orderId, int chiTietId, int soLuong) {
        HoaDon hoaDon = hoaDonRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + orderId));

        if (!"Chờ xác nhận".equals(hoaDon.getTrangThai())) {
            throw new IllegalStateException("Chỉ có thể cập nhật số lượng sản phẩm ở trạng thái 'Chờ xác nhận'");
        }

        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(chiTietId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết hóa đơn với ID: " + chiTietId));

        ChiTietSanPham chiTietSanPham = hoaDonChiTiet.getChiTietSanPham();
        int soLuongCu = hoaDonChiTiet.getSoLuong();
        int soLuongTonHienTai = chiTietSanPham.getSoLuongTon() + soLuongCu;

        if (soLuongTonHienTai < soLuong) {
            throw new IllegalArgumentException("Số lượng tồn kho không đủ!");
        }

        chiTietSanPham.setSoLuongTon(soLuongTonHienTai - soLuong);
        chiTietSanPhamRepository.save(chiTietSanPham);

        hoaDonChiTiet.setSoLuong(soLuong);
        hoaDonChiTiet.setThanhTien(hoaDonChiTiet.getGiaSanPham().multiply(BigDecimal.valueOf(soLuong)));
        return hoaDonChiTietRepository.save(hoaDonChiTiet);
    }

    @Transactional
    public void removeProductFromOrder(int orderId, int chiTietId) {
        HoaDon hoaDon = hoaDonRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + orderId));

        if (!"Tạo hóa đơn".equals(hoaDon.getTrangThai()) && !"Chờ xác nhận".equals(hoaDon.getTrangThai())) {
            throw new IllegalStateException("Chỉ có thể xóa sản phẩm ở trạng thái 'Tạo hóa đơn' hoặc 'Chờ xác nhận'");
        }

        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(chiTietId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết hóa đơn với ID: " + chiTietId));

        // Khôi phục số lượng cho sản phẩm lẻ
        if (hoaDonChiTiet.getChiTietSanPham() != null) {
            ChiTietSanPham chiTietSanPham = hoaDonChiTiet.getChiTietSanPham();
            chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() + hoaDonChiTiet.getSoLuong());
            chiTietSanPhamRepository.save(chiTietSanPham);
            System.out.println("DEBUG: Restored " + hoaDonChiTiet.getSoLuong() + " units for product: " + chiTietSanPham.getTenChiTietSanPham());
        }

        // Khôi phục số lượng cho bộ sách
        if (hoaDonChiTiet.getBoSach() != null) {
            BoSach boSach = hoaDonChiTiet.getBoSach();
            boSach.setSoLuong(boSach.getSoLuong() + hoaDonChiTiet.getSoLuong());
            boSachRepository.save(boSach);
            System.out.println("DEBUG: Restored " + hoaDonChiTiet.getSoLuong() + " units for book set: " + boSach.getTenBoSach());

            // Khôi phục số lượng cho các sản phẩm trong bộ sách
            List<BoSachChiTiet> boSachChiTiets = boSachChiTietRepository.findByIdBoSachId(boSach.getId());
            for (BoSachChiTiet boSachChiTiet : boSachChiTiets) {
                ChiTietSanPham chiTietSanPham = boSachChiTiet.getIdChiTietSanPham();
                int soLuongCanKhoiPhuc = boSachChiTiet.getSoLuong() * hoaDonChiTiet.getSoLuong();
                chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() + soLuongCanKhoiPhuc);
                chiTietSanPhamRepository.save(chiTietSanPham);
                System.out.println("DEBUG: Restored " + soLuongCanKhoiPhuc + " units for product in book set: " + chiTietSanPham.getTenChiTietSanPham());
            }
        }

        hoaDonChiTietRepository.delete(hoaDonChiTiet);
    }

    @Transactional
    public HoaDon updateHoaDon(int id, Map<String, Object> data) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + id));

        if (data.containsKey("phiShip")) {
            hoaDon.setPhiShip(new java.math.BigDecimal(data.get("phiShip").toString()));
        }
        if (data.containsKey("tongTien")) {
            hoaDon.setTongTien(new java.math.BigDecimal(data.get("tongTien").toString()));
        }
        if (data.containsKey("ngayDatHang")) {
            hoaDon.setNgayDatHang(Instant.parse(data.get("ngayDatHang").toString()));
        }
        if (data.containsKey("ghiChu")) {
            hoaDon.setGhiChu(data.get("ghiChu").toString());
        }

        hoaDon.setUpdatedAt(java.time.Instant.now());
        return hoaDonRepository.save(hoaDon);
    }

    // Tra cứu đơn hàng theo mã hóa đơn và số điện thoại người nhận
    public HoaDon lookupOrderByCodeAndPhone(String orderCode, String phoneNumber) {
        // Sử dụng repository method để tìm kiếm hiệu quả hơn
        Optional<HoaDon> hoaDonOpt = hoaDonRepository.findByMaHoaDonAndSoDienThoaiNguoiNhan(orderCode, phoneNumber);

        if (hoaDonOpt.isPresent()) {
            // Trả về hóa đơn đầy đủ với relationships
            return getHoaDonById(hoaDonOpt.get().getId());
        }

        return null; // Không tìm thấy
    }

    // Gửi email xác nhận đơn hàng
    private void sendOrderConfirmationEmail(HoaDon hoaDon) {
        try {
            System.out.println("📧 DEBUG: sendOrderConfirmationEmail called for order: " + hoaDon.getMaHoaDon());
            System.out.println("📧 DEBUG: hoaDon.getKhachHang(): " + (hoaDon.getKhachHang() != null ? "exists" : "null"));
            System.out.println("📧 DEBUG: hoaDon.getEmailNguoiNhan(): " + hoaDon.getEmailNguoiNhan());

            // Lấy email từ khách hàng đã đăng nhập hoặc từ thông tin người nhận
            String customerEmail = null;
            String customerName = hoaDon.getTenNguoiNhan();

            if (hoaDon.getKhachHang() != null && hoaDon.getKhachHang().getEmail() != null) {
                // Khách hàng đã đăng nhập - lấy email từ tài khoản
                customerEmail = hoaDon.getKhachHang().getEmail();
                System.out.println("📧 DEBUG: Using customer email from account: " + customerEmail);
            } else if (hoaDon.getEmailNguoiNhan() != null && !hoaDon.getEmailNguoiNhan().trim().isEmpty()) {
                // Khách vãng lai - lấy email từ thông tin đơn hàng
                customerEmail = hoaDon.getEmailNguoiNhan();
                System.out.println("📧 DEBUG: Using guest customer email from order: " + customerEmail);
            } else {
                // Không có email
                System.out.println("📧 DEBUG: No email available, skipping email notification");
                return; // Không gửi email nếu không có email
            }

            if (customerEmail == null || customerEmail.trim().isEmpty()) {
                System.out.println("📧 DEBUG: No email available, skipping email notification");
                return;
            }

            // Format dữ liệu cho email
            String orderCode = hoaDon.getMaHoaDon();

            // Sử dụng ngày tạo đơn hàng nếu chưa có ngày đặt hàng
            String orderDate = "N/A";
            if (hoaDon.getNgayDatHang() != null) {
                orderDate = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(
                        java.time.LocalDateTime.ofInstant(hoaDon.getNgayDatHang(), java.time.ZoneId.systemDefault())
                );
            } else if (hoaDon.getNgayTao() != null) {
                orderDate = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(
                        java.time.LocalDateTime.ofInstant(hoaDon.getNgayTao(), java.time.ZoneId.systemDefault())
                );
            }

            // Tính tổng tiền từ chi tiết đơn hàng nếu chưa có tongTien
            String totalAmount = "N/A";
            if (hoaDon.getTongTien() != null) {
                totalAmount = String.format("%,.0f ₫", hoaDon.getTongTien().doubleValue());
            } else if (hoaDon.getHoaDonChiTiets() != null && !hoaDon.getHoaDonChiTiets().isEmpty()) {
                BigDecimal calculatedTotal = hoaDon.getHoaDonChiTiets().stream()
                        .map(item -> item.getGiaSanPham() != null ?
                                item.getGiaSanPham().multiply(BigDecimal.valueOf(item.getSoLuong())) : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                if (calculatedTotal.compareTo(BigDecimal.ZERO) > 0) {
                    totalAmount = String.format("%,.0f ₫", calculatedTotal.doubleValue());
                }
            }

            // Xác định phương thức thanh toán
            String paymentMethod = "Chưa thanh toán";

            // Ưu tiên sử dụng trường mới phuongThucThanhToan
            if (hoaDon.getPhuongThucThanhToan() != null && !hoaDon.getPhuongThucThanhToan().trim().isEmpty()) {
                String phuongThuc = hoaDon.getPhuongThucThanhToan();
                switch (phuongThuc) {
                    case "TraTruoc":
                        paymentMethod = "VNPAY (Thanh toán online)";
                        break;
                    case "COD":
                        paymentMethod = "COD (Thanh toán khi nhận hàng)";
                        break;
                    case "TIEN_MAT":
                        paymentMethod = "Tiền mặt";
                        break;
                    case "CHUYEN_KHOAN":
                        paymentMethod = "Chuyển khoản";
                        break;
                    default:
                        paymentMethod = phuongThuc;
                        break;
                }
            } else if (hoaDon.getHinhThucThanhToan() != null &&
                    hoaDon.getHinhThucThanhToan().getPhuongThucThanhToan() != null) {
                String kieuThanhToan = hoaDon.getHinhThucThanhToan().getPhuongThucThanhToan().getKieuThanhToan();
                if ("Chuyển khoản".equals(kieuThanhToan)) {
                    paymentMethod = "VNPAY (Chuyển khoản)";
                } else if ("Tiền mặt".equals(kieuThanhToan)) {
                    paymentMethod = "COD (Thanh toán khi nhận hàng)";
                } else {
                    paymentMethod = kieuThanhToan;
                }
            }

            String deliveryAddress = hoaDon.getDiaChi() != null ? hoaDon.getDiaChi() : "N/A";
            String phoneNumber = hoaDon.getSoDienThoaiNguoiNhan() != null ? hoaDon.getSoDienThoaiNguoiNhan() : "N/A";

            System.out.println("📧 DEBUG: Sending order confirmation email to: " + customerEmail);
            System.out.println("📧 DEBUG: Order details - Code: " + orderCode + ", Date: " + orderDate + ", Amount: " + totalAmount + ", Payment: " + paymentMethod);

            // Gửi email
            emailService.sendOrderConfirmationEmail(
                    customerEmail,
                    customerName,
                    orderCode,
                    orderDate,
                    totalAmount,
                    paymentMethod,
                    deliveryAddress,
                    phoneNumber
            );

            System.out.println("✅ DEBUG: Order confirmation email sent successfully to: " + customerEmail);

        } catch (Exception e) {
            System.out.println("❌ DEBUG: Error sending order confirmation email: " + e.getMessage());
            e.printStackTrace();
            // Không throw exception để không ảnh hưởng đến việc tạo đơn hàng
        }
    }

    @Transactional
    public HoaDon updateProductQuantity(int hoaDonId, Integer chiTietSanPhamId, Integer soLuong) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + hoaDonId));

        // Kiểm tra hóa đơn có thể chỉnh sửa không
        if (hoaDon.getDeleted() || !hoaDon.getTrangThai().equals("Tạo hóa đơn") && !hoaDon.getTrangThai().equals("Chờ xác nhận")) {
            throw new IllegalArgumentException("Không thể chỉnh sửa hóa đơn đã hủy hoặc đã hoàn thành");
        }

        // Tìm HoaDonChiTiet cần cập nhật
        HoaDonChiTiet hoaDonChiTiet = hoaDon.getHoaDonChiTiets().stream()
                .filter(hct -> hct.getChiTietSanPham() != null && hct.getChiTietSanPham().getId().equals(chiTietSanPhamId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết sản phẩm trong hóa đơn"));

        System.out.println("DEBUG: updateProductQuantity - Before update: soLuong=" + hoaDonChiTiet.getSoLuong() + ", thanhTien=" + hoaDonChiTiet.getThanhTien());

        // Cập nhật số lượng
        hoaDonChiTiet.setSoLuong(soLuong);

        // Cập nhật thành tiền
        BigDecimal giaSanPham = hoaDonChiTiet.getChiTietSanPham().getGia();
        hoaDonChiTiet.setThanhTien(giaSanPham.multiply(new BigDecimal(soLuong)));

        System.out.println("DEBUG: updateProductQuantity - After update: soLuong=" + hoaDonChiTiet.getSoLuong() + ", thanhTien=" + hoaDonChiTiet.getThanhTien());

        return hoaDonRepository.save(hoaDon);
    }

    @Transactional
    public HoaDon updateBoSachQuantity(int hoaDonId, Integer boSachId, Integer soLuong) {
        System.out.println("DEBUG: updateBoSachQuantity called with hoaDonId=" + hoaDonId + ", boSachId=" + boSachId + ", soLuong=" + soLuong);

        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + hoaDonId));

        // Kiểm tra hóa đơn có thể chỉnh sửa không
        if (hoaDon.getDeleted() || !hoaDon.getTrangThai().equals("Tạo hóa đơn") && !hoaDon.getTrangThai().equals("Chờ xác nhận")) {
            throw new IllegalArgumentException("Không thể chỉnh sửa hóa đơn đã hủy hoặc đã hoàn thành");
        }

        System.out.println("DEBUG: Found HoaDon: " + hoaDon.getMaHoaDon() + ", HoaDonChiTiets size: " + hoaDon.getHoaDonChiTiets().size());

        // Tìm HoaDonChiTiet cần cập nhật
        HoaDonChiTiet hoaDonChiTiet = hoaDon.getHoaDonChiTiets().stream()
                .filter(hct -> hct.getBoSach() != null && hct.getBoSach().getId().equals(boSachId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bộ sách trong hóa đơn"));

        System.out.println("DEBUG: Found HoaDonChiTiet for boSachId=" + boSachId + ", current soLuong=" + hoaDonChiTiet.getSoLuong());

        // Cập nhật số lượng
        hoaDonChiTiet.setSoLuong(soLuong);

        // Cập nhật thành tiền
        BigDecimal giaBoSach = hoaDonChiTiet.getBoSach().getGiaTien();
        hoaDonChiTiet.setThanhTien(giaBoSach.multiply(new BigDecimal(soLuong)));

        System.out.println("DEBUG: Updated soLuong=" + soLuong + ", thanhTien=" + hoaDonChiTiet.getThanhTien());

        return hoaDonRepository.save(hoaDon);
    }
}
