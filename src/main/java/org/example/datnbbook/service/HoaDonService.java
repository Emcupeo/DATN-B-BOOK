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
    private KhachHangRepository khachHangRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

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
        return hoaDonRepository.findById(id).orElse(null);
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
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + id));

        String trangThaiCu = hoaDon.getTrangThai();

        hoaDon.setTrangThai(trangThaiMoi);
        hoaDonRepository.save(hoaDon);

        LichSuHoaDon lichSu = new LichSuHoaDon();
        lichSu.setHoaDon(hoaDon);
        lichSu.setTrangThaiCu(trangThaiCu);
        lichSu.setTrangThaiMoi(trangThaiMoi);
        lichSu.setCreatedAt(Instant.now());
        lichSuHoaDonRepository.save(lichSu);

        return hoaDon;
    }

    @Transactional
    public HoaDon huyDon(int id) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + id));

        String trangThaiCu = hoaDon.getTrangThai();
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

    @Transactional
    public HoaDon updatePayment(int id, BigDecimal tienMat, BigDecimal chuyenKhoan, Integer phuongThucThanhToanId, String ghiChu, String loaiHoaDon, BigDecimal tienKhachDua, Long phieuGiamGiaId) {
        System.out.println("DEBUG: updatePayment called with id=" + id + ", tienMat=" + tienMat + ", chuyenKhoan=" + chuyenKhoan + ", phuongThucThanhToanId=" + phuongThucThanhToanId + ", phieuGiamGiaId=" + phieuGiamGiaId);
        
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + id));
        
        System.out.println("DEBUG: Found HoaDon: " + hoaDon.getMaHoaDon() + ", trangThai=" + hoaDon.getTrangThai() + ", tongTien=" + hoaDon.getTongTien());

        // Sử dụng tongTien đã được set từ frontend, nếu chưa có thì tính lại
        BigDecimal thanhTien;
        if (hoaDon.getTongTien() != null) {
            thanhTien = hoaDon.getTongTien();
            System.out.println("DEBUG: Using existing tongTien: " + thanhTien);
        } else {
            System.out.println("DEBUG: Calculating tongTien from HoaDonChiTiet...");
            // Tính tổng tiền từ HoaDonChiTiet
            Set<HoaDonChiTiet> hoaDonChiTiets = hoaDon.getHoaDonChiTiets();
            System.out.println("DEBUG: Found " + (hoaDonChiTiets != null ? hoaDonChiTiets.size() : 0) + " HoaDonChiTiet items");
            
            BigDecimal tongTienHoaDonChiTiet = hoaDonChiTiets != null ? hoaDonChiTiets.stream()
                    .map(HoaDonChiTiet::getThanhTien)
                    .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;
            System.out.println("DEBUG: tongTienHoaDonChiTiet: " + tongTienHoaDonChiTiet);
            
            // Tính phí ship
            BigDecimal phiShip = hoaDon.getPhiShip() != null ? hoaDon.getPhiShip() : BigDecimal.ZERO;
            System.out.println("DEBUG: phiShip: " + phiShip);
            
            thanhTien = tongTienHoaDonChiTiet.add(phiShip);
            System.out.println("DEBUG: Calculated thanhTien: " + thanhTien);
        }

        BigDecimal tongTienThanhToan = tienMat.add(chuyenKhoan);
        if (tongTienThanhToan.compareTo(thanhTien) != 0) {
            throw new IllegalArgumentException("Tổng tiền thanh toán không khớp với tổng hóa đơn! Expected: " + thanhTien + ", Got: " + tongTienThanhToan);
        }

        if ("Tại quầy".equals(loaiHoaDon) && tienMat.compareTo(BigDecimal.ZERO) > 0) {
            if (tienKhachDua.compareTo(thanhTien) < 0) {
                throw new IllegalArgumentException("Tiền khách đưa không đủ để thanh toán!");
            }
        }

        HinhThucThanhToan hinhThucThanhToan = hoaDon.getHinhThucThanhToan();
        System.out.println("DEBUG: Current hinhThucThanhToan: " + hinhThucThanhToan);
        
        // Luôn tạo mới HinhThucThanhToan cho mỗi hóa đơn
        hinhThucThanhToan = new HinhThucThanhToan();
        hinhThucThanhToan.setCreatedAt(Instant.now());
        String timestamp = String.valueOf(System.currentTimeMillis());
        String suffix = timestamp.substring(Math.max(0, timestamp.length() - 5)); // Giảm xuống 5 ký tự
        String maHinhThucThanhToan = "HT" + suffix; // Giảm xuống 2 ký tự prefix
        hinhThucThanhToan.setMaHinhThucThanhToan(maHinhThucThanhToan);
        System.out.println("DEBUG: Set maHinhThucThanhToan: " + maHinhThucThanhToan);

        hinhThucThanhToan.setTienMat(tienMat);
        System.out.println("DEBUG: Set tienMat: " + tienMat);
        hinhThucThanhToan.setChuyenKhoan(chuyenKhoan);
        System.out.println("DEBUG: Set chuyenKhoan: " + chuyenKhoan);
        hinhThucThanhToan.setUpdatedAt(Instant.now());
        System.out.println("DEBUG: Set updatedAt: " + Instant.now());
        hinhThucThanhToan.setDeleted(false); // Explicitly set deleted field
        System.out.println("DEBUG: Set deleted: false");

        System.out.println("DEBUG: Looking for PhuongThucThanhToan with ID: " + phuongThucThanhToanId);
        // Kiểm tra tất cả PhuongThucThanhToan trong database
        List<PhuongThucThanhToan> allPhuongThucThanhToan = phuongThucThanhToanRepository.findAll();
        System.out.println("DEBUG: Total PhuongThucThanhToan in DB: " + allPhuongThucThanhToan.size());
        for (PhuongThucThanhToan ptt : allPhuongThucThanhToan) {
            System.out.println("DEBUG: PhuongThucThanhToan - ID: " + ptt.getId() + ", Kieu: " + ptt.getKieuThanhToan() + ", Ma: " + ptt.getMaPhuongThucThanhToan());
        }
        
        PhuongThucThanhToan phuongThucThanhToan = phuongThucThanhToanRepository.findById(phuongThucThanhToanId)
                .orElseThrow(() -> new IllegalArgumentException("Phương thức thanh toán không tồn tại với ID: " + phuongThucThanhToanId));
        System.out.println("DEBUG: Found PhuongThucThanhToan: " + phuongThucThanhToan.getKieuThanhToan() + " (ID: " + phuongThucThanhToan.getId() + ")");
        hinhThucThanhToan.setPhuongThucThanhToan(phuongThucThanhToan);
        System.out.println("DEBUG: Set phuongThucThanhToan to HinhThucThanhToan");

        System.out.println("DEBUG: Saving HinhThucThanhToan: " + hinhThucThanhToan);
        try {
            hinhThucThanhToan = hinhThucThanhToanRepository.save(hinhThucThanhToan);
            hinhThucThanhToanRepository.flush(); // Explicitly flush changes to DB
            System.out.println("DEBUG: Saved HinhThucThanhToan ID: " + hinhThucThanhToan.getId()); // Added for debugging
        } catch (Exception e) {
            System.out.println("DEBUG: Error saving HinhThucThanhToan: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        System.out.println("DEBUG: Setting hinhThucThanhToan to HoaDon: " + hinhThucThanhToan.getId());
        hoaDon.setHinhThucThanhToan(hinhThucThanhToan);
        // Set bidirectional relationship
        if (hinhThucThanhToan.getHoaDons() != null) {
            hinhThucThanhToan.getHoaDons().add(hoaDon);
        }
        hoaDon.setGhiChu(ghiChu);
        hoaDon.setTongTien(thanhTien);
        hoaDon.setUpdatedAt(Instant.now());

        // Set phieuGiamGia nếu có
        if (phieuGiamGiaId != null) {
            PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(phieuGiamGiaId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu giảm giá với ID: " + phieuGiamGiaId));
            hoaDon.setPhieuGiamGia(phieuGiamGia);
            System.out.println("DEBUG: Set phieuGiamGia ID: " + phieuGiamGiaId);
        }

        // Set trạng thái dựa trên loại hóa đơn và phương thức thanh toán
        String newTrangThai;
        if ("Tại quầy".equals(loaiHoaDon)) {
            newTrangThai = "Hoàn thành";
        } else if ("Online".equals(loaiHoaDon) && phuongThucThanhToanId == 1) {
            // VNPAY (phuongThucThanhToanId = 1) - bắt đầu từ "Đã thanh toán"
            newTrangThai = "Đã thanh toán";
        } else if ("Online".equals(loaiHoaDon) && phuongThucThanhToanId == 4) {
            // COD (phuongThucThanhToanId = 4) - bắt đầu từ "Chờ xác nhận"
            newTrangThai = "Chờ xác nhận";
        } else {
            // Mặc định
            newTrangThai = "Chờ xác nhận";
        }
        hoaDon.setTrangThai(newTrangThai);

        // Đảm bảo hóa đơn được lưu với id_hinh_thuc_thanh_toan
        hoaDon.setHinhThucThanhToan(hinhThucThanhToan);
        if (hinhThucThanhToan.getHoaDons() != null) {
            hinhThucThanhToan.getHoaDons().add(hoaDon);
        }
        HoaDon updatedHoaDon;
        try {
            updatedHoaDon = hoaDonRepository.saveAndFlush(hoaDon); // flush ngay lập tức
            System.out.println("DEBUG: Saved HoaDon with hinhThucThanhToan ID: " + (updatedHoaDon.getHinhThucThanhToan() != null ? updatedHoaDon.getHinhThucThanhToan().getId() : "NULL"));
        } catch (Exception e) {
            System.out.println("DEBUG: Error saving HoaDon: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        // Đọc lại từ DB thay vì refresh
        updatedHoaDon = hoaDonRepository.findById(updatedHoaDon.getId()).orElse(null);
        System.out.println("DEBUG: After reload - HoaDon hinhThucThanhToan ID: " + (updatedHoaDon != null && updatedHoaDon.getHinhThucThanhToan() != null ? updatedHoaDon.getHinhThucThanhToan().getId() : "NULL"));

        LichSuHoaDon lichSu = new LichSuHoaDon();
        lichSu.setHoaDon(updatedHoaDon);
        lichSu.setTrangThaiCu("Tạo hóa đơn");
        lichSu.setTrangThaiMoi(newTrangThai);
        lichSu.setCreatedAt(Instant.now());
        lichSuHoaDonRepository.save(lichSu);

        // Trừ số lượng phiếu giảm giá khi đơn hàng thành công
        if (phieuGiamGiaId != null && ("Hoàn thành".equals(newTrangThai) || "Đã thanh toán".equals(newTrangThai))) {
            try {
                PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(phieuGiamGiaId).orElse(null);
                if (phieuGiamGia != null && phieuGiamGia.getSoLuong() > 0) {
                    phieuGiamGia.setSoLuong(phieuGiamGia.getSoLuong() - 1);
                    phieuGiamGiaRepository.save(phieuGiamGia);
                    System.out.println("DEBUG: Decreased voucher quantity for ID: " + phieuGiamGiaId + ", remaining: " + phieuGiamGia.getSoLuong());
                }
            } catch (Exception e) {
                System.out.println("DEBUG: Error updating voucher quantity: " + e.getMessage());
                // Không throw exception để không ảnh hưởng đến việc tạo đơn hàng
            }
        }

        return updatedHoaDon;
    }

    @Transactional
    public HoaDon createHoaDon(String loaiHoaDon) {
        if (loaiHoaDon == null || loaiHoaDon.isEmpty()) {
            throw new IllegalArgumentException("Loại hóa đơn không được để trống!");
        }

        long soLuongHoaDonMoi = hoaDonRepository.countByTrangThai("Chờ xác nhận");
        if (soLuongHoaDonMoi >= 5) {
            throw new IllegalStateException("Đã đạt giới hạn tối đa 5 hóa đơn mới. Không thể tạo thêm!");
        }

        HoaDon hoaDon = new HoaDon();
        hoaDon.setLoaiHoaDon(loaiHoaDon);
        hoaDon.setTrangThai("Chờ xác nhận");
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
        lichSu.setTrangThaiMoi("Chờ xác nhận");
        lichSu.setCreatedAt(Instant.now());
        lichSuHoaDonRepository.save(lichSu);

        return savedHoaDon;
    }

    @Transactional
    public HoaDon updateCustomerInfo(int id, Long idKhachHang, String tenNguoiNhan, String soDienThoaiNguoiNhan, String diaChiGiaoHang) {
        System.out.println("DEBUG: HoaDonService.updateCustomerInfo called");
        System.out.println("DEBUG: id=" + id + ", idKhachHang=" + idKhachHang + ", tenNguoiNhan=" + tenNguoiNhan + ", soDienThoaiNguoiNhan=" + soDienThoaiNguoiNhan + ", diaChiGiaoHang=" + diaChiGiaoHang);
        
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
        hoaDon.setUpdatedAt(Instant.now());

        System.out.println("DEBUG: Before save - diaChi: " + hoaDon.getDiaChi());
        HoaDon updatedHoaDon = hoaDonRepository.save(hoaDon);
        System.out.println("DEBUG: After save - diaChi: " + updatedHoaDon.getDiaChi());

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

        if (!"Tạo hóa đơn".equals(hoaDon.getTrangThai())) {
            throw new IllegalStateException("Chỉ có thể xóa sản phẩm ở trạng thái 'Tạo hóa đơn'");
        }

        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(chiTietId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết hóa đơn với ID: " + chiTietId));

        ChiTietSanPham chiTietSanPham = hoaDonChiTiet.getChiTietSanPham();
        chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() + hoaDonChiTiet.getSoLuong());
        chiTietSanPhamRepository.save(chiTietSanPham);

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
}