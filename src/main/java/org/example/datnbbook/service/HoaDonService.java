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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    private NhanVienRepository nhanVienRepository; // Thêm repository cho NhanVien

    @Autowired
    private SpringTemplateEngine templateEngine;

    private String generateMaHinhThucThanhToan() {
        String uuidPart = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5);
        return "HTTT-" + uuidPart;
    }

    private String generateMaLichSuHoaDon() {
        return "LSHD-" + UUID.randomUUID().toString();
    }

    private String generateMaHoaDon() {
        String uuidPart = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        return "HD-" + uuidPart;
    }

    private String generateMaHoaDonChiTiet() {
        return "HDCT-" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
    }

    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> hoaDons = hoaDonRepository.findAllByOrderByCreatedAtDesc();
        for (HoaDon hoaDon : hoaDons) {
            if (hoaDon.getTongTien() == null && hoaDon.getHoaDonChiTiets() != null) {
                BigDecimal tongTien = hoaDon.getHoaDonChiTiets().stream()
                        .map(item -> item.getGiaSanPham() != null ? item.getGiaSanPham().multiply(BigDecimal.valueOf(item.getSoLuong())) : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                hoaDon.setTongTien(tongTien);
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
        lichSu.setMaLichSuHoaDon(generateMaLichSuHoaDon());
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
        lichSu.setMaLichSuHoaDon(generateMaLichSuHoaDon());
        lichSuHoaDonRepository.save(lichSu);

        return hoaDon;
    }

    @Transactional
    public HoaDon updatePayment(int id, BigDecimal tienMat, BigDecimal chuyenKhoan, Integer phuongThucThanhToanId, String ghiChu, String loaiHoaDon, BigDecimal tienKhachDua) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + id));

        BigDecimal tongTienHang = hoaDon.getHoaDonChiTiets() != null ?
                hoaDon.getHoaDonChiTiets().stream()
                        .map(item -> item.getGiaSanPham().multiply(BigDecimal.valueOf(item.getSoLuong())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;
        BigDecimal tienGiamGia = hoaDon.getPhieuGiamGia() != null ? hoaDon.getPhieuGiamGia().getSoPhanTramGiam()
                .multiply(tongTienHang).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal phiShip = hoaDon.getPhiShip() != null ? hoaDon.getPhiShip() : BigDecimal.ZERO;
        BigDecimal thanhTien = tongTienHang.subtract(tienGiamGia).add(phiShip);

        BigDecimal tongTienThanhToan = tienMat.add(chuyenKhoan);
        if (tongTienThanhToan.compareTo(thanhTien) != 0) {
            throw new IllegalArgumentException("Tổng tiền thanh toán không khớp với tổng hóa đơn!");
        }

        if ("Tại quầy".equals(loaiHoaDon) && tienMat.compareTo(BigDecimal.ZERO) > 0) {
            if (tienKhachDua.compareTo(thanhTien) < 0) {
                throw new IllegalArgumentException("Tiền khách đưa không đủ để thanh toán!");
            }
        }

        HinhThucThanhToan hinhThucThanhToan = hoaDon.getHinhThucThanhToan();
        if (hinhThucThanhToan == null) {
            hinhThucThanhToan = new HinhThucThanhToan();
            hinhThucThanhToan.setCreatedAt(Instant.now());
            hinhThucThanhToan.setMaHinhThucThanhToan(generateMaHinhThucThanhToan());
        }

        hinhThucThanhToan.setTienMat(tienMat);
        hinhThucThanhToan.setChuyenKhoan(chuyenKhoan);
        hinhThucThanhToan.setUpdatedAt(Instant.now());

        PhuongThucThanhToan phuongThucThanhToan = phuongThucThanhToanRepository.findById(phuongThucThanhToanId)
                .orElseThrow(() -> new IllegalArgumentException("Phương thức thanh toán không tồn tại với ID: " + phuongThucThanhToanId));
        hinhThucThanhToan.setPhuongThucThanhToan(phuongThucThanhToan);

        hinhThucThanhToan = hinhThucThanhToanRepository.save(hinhThucThanhToan);
        hoaDon.setHinhThucThanhToan(hinhThucThanhToan);
        hoaDon.setGhiChu(ghiChu);
        hoaDon.setTongTien(thanhTien);
        hoaDon.setUpdatedAt(Instant.now());

        // Set trạng thái dựa trên loại hóa đơn
        String newTrangThai = "Tại quầy".equals(loaiHoaDon) ? "Hoàn thành" : "Chờ xác nhận";
        hoaDon.setTrangThai(newTrangThai);

        HoaDon updatedHoaDon = hoaDonRepository.save(hoaDon);

        LichSuHoaDon lichSu = new LichSuHoaDon();
        lichSu.setHoaDon(updatedHoaDon);
        lichSu.setTrangThaiCu("Tạo hóa đơn");
        lichSu.setTrangThaiMoi(newTrangThai);
        lichSu.setCreatedAt(Instant.now());
        lichSu.setMaLichSuHoaDon(generateMaLichSuHoaDon());
        lichSuHoaDonRepository.save(lichSu);

        return updatedHoaDon;
    }

    @Transactional
    public HoaDon createHoaDon(String loaiHoaDon) {
        if (loaiHoaDon == null || loaiHoaDon.isEmpty()) {
            throw new IllegalArgumentException("Loại hóa đơn không được để trống!");
        }

        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDon(generateMaHoaDon());
        hoaDon.setLoaiHoaDon(loaiHoaDon);
        hoaDon.setTrangThai("Tạo hóa đơn");
        hoaDon.setCreatedAt(Instant.now());
        hoaDon.setNgayTao(Instant.now());
        hoaDon.setDeleted(false);

        // Gán nhân viên mặc định với id = 1
        NhanVien nhanVien = nhanVienRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên với ID: 1"));
        hoaDon.setNhanVien(nhanVien);

        HoaDon savedHoaDon = hoaDonRepository.save(hoaDon);

        LichSuHoaDon lichSu = new LichSuHoaDon();
        lichSu.setHoaDon(savedHoaDon);
        lichSu.setTrangThaiCu(null);
        lichSu.setTrangThaiMoi("Tạo hóa đơn");
        lichSu.setCreatedAt(Instant.now());
        lichSu.setMaLichSuHoaDon(generateMaLichSuHoaDon());
        lichSuHoaDonRepository.save(lichSu);

        return savedHoaDon;
    }

    @Transactional
    public HoaDon updateCustomerInfo(int id, Long idKhachHang, String tenNguoiNhan, String soDienThoaiNguoiNhan, String diaChiGiaoHang) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + id));

        if (idKhachHang != null) {
            KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với ID: " + idKhachHang));
            hoaDon.setKhachHang(khachHang);
        } else {
            hoaDon.setKhachHang(null);
        }

        hoaDon.setTenNguoiNhan(tenNguoiNhan != null ? tenNguoiNhan : "Khách lẻ");
        hoaDon.setSoDienThoaiNguoiNhan(soDienThoaiNguoiNhan);
        hoaDon.setDiaChi(diaChiGiaoHang);
        hoaDon.setUpdatedAt(Instant.now());

        HoaDon updatedHoaDon = hoaDonRepository.save(hoaDon);

        return updatedHoaDon;
    }

    @Transactional
    public HoaDonChiTiet addProductToOrder(int orderId, int chiTietSanPhamId, int soLuong, BigDecimal giaSanPham) {
        HoaDon hoaDon = hoaDonRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + orderId));

        if (!"Tạo hóa đơn".equals(hoaDon.getTrangThai())) {
            throw new IllegalStateException("Chỉ có thể thêm sản phẩm vào hóa đơn ở trạng thái 'Tạo hóa đơn'");
        }

        ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(chiTietSanPhamId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với ID: " + chiTietSanPhamId));

        if (chiTietSanPham.getSoLuongTon() < soLuong) {
            throw new IllegalArgumentException("Số lượng tồn kho không đủ!");
        }

        HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
        hoaDonChiTiet.setHoaDon(hoaDon);
        hoaDonChiTiet.setChiTietSanPham(chiTietSanPham);
        hoaDonChiTiet.setSoLuong(soLuong);
        hoaDonChiTiet.setGiaSanPham(giaSanPham);
        hoaDonChiTiet.setThanhTien(giaSanPham.multiply(BigDecimal.valueOf(soLuong)));
        hoaDonChiTiet.setMaHoaDonChiTiet(generateMaHoaDonChiTiet());

        chiTietSanPham.setSoLuongTon(chiTietSanPham.getSoLuongTon() - soLuong);
        chiTietSanPhamRepository.save(chiTietSanPham);

        return hoaDonChiTietRepository.save(hoaDonChiTiet);
    }

    @Transactional
    public HoaDonChiTiet updateProductQuantity(int orderId, int chiTietId, int soLuong) {
        HoaDon hoaDon = hoaDonRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + orderId));

        if (!"Tạo hóa đơn".equals(hoaDon.getTrangThai())) {
            throw new IllegalStateException("Chỉ có thể cập nhật số lượng sản phẩm ở trạng thái 'Tạo hóa đơn'");
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
}
