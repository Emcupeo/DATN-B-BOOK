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
import org.example.datnbbook.model.HoaDon;
import org.example.datnbbook.model.HoaDonChiTiet;
import org.example.datnbbook.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

@Service
public class HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public List<HoaDon> getAllHoaDon() {
        return hoaDonRepository.findAll();
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

        System.out.println("Số lượng hóa đơn: " + hoaDons.size());
        if (hoaDons.isEmpty()) {
            System.out.println("Không có hóa đơn nào để xuất.");
            throw new IllegalStateException("Không có hóa đơn nào để xuất");
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Danh sách hóa đơn");

            // Tạo kiểu cho hyperlink
            CellStyle hyperlinkStyle = workbook.createCellStyle();
            Font hyperlinkFont = workbook.createFont();
            hyperlinkFont.setUnderline(Font.U_SINGLE);
            hyperlinkFont.setColor(IndexedColors.BLUE.getIndex());
            hyperlinkStyle.setFont(hyperlinkFont);

            // Tạo header
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

            // Tạo Hyperlink CreationHelper
            CreationHelper creationHelper = workbook.getCreationHelper();

            for (HoaDon hoaDon : hoaDons) {
                Row row = sheet.createRow(rowIndex++);

                // Cột ID với hyperlink
                Cell idCell = row.createCell(0);
                idCell.setCellValue(hoaDon.getId());
                Hyperlink hyperlink = creationHelper.createHyperlink(HyperlinkType.DOCUMENT);
                hyperlink.setAddress("'Chi tiết hóa đơn - " + hoaDon.getId() + "'!A1"); // Liên kết đến ô A1 của sheet chi tiết
                idCell.setHyperlink(hyperlink);
                idCell.setCellStyle(hyperlinkStyle); // Áp dụng kiểu hyperlink

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

                // Tạo sheet chi tiết hóa đơn
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
            System.out.println("File Excel đã được tạo thành công.");
            return out;
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo file Excel: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Lỗi khi tạo file Excel", e);
        }
    }


}