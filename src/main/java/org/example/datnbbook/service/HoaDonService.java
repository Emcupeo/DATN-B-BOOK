package org.example.datnbbook.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
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
                .filter(hd -> "Đã thanh toán".equals(hd.getTrangThai()))
                .toList();

        System.out.println("Số lượng hóa đơn: " + hoaDons.size());
        if (hoaDons.isEmpty()) {
            System.out.println("Không có hóa đơn nào để xuất.");
            throw new IllegalStateException("Không có hóa đơn nào để xuất");
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Danh sách hóa đơn");

            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Mã HĐ", "Ngày Tạo", "Ngày Thanh Toán", "Tổng Thanh Tiền", "Mã Nhân Viên", "Tên Khách Hàng", "Địa Chỉ", "Số Điện Thoại", "Trạng Thái"};
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

            for (HoaDon hoaDon : hoaDons) {
                System.out.println("Hóa đơn ID: " + hoaDon.getId());
                System.out.println("Mã HĐ: " + hoaDon.getMaHoaDon());
                System.out.println("Ngày Tạo: " + hoaDon.getNgayTao());
                System.out.println("Ngày Thanh Toán: " + hoaDon.getNgayDatHang()); // Sử dụng ngayDatHang thay vì ngayThanhToan
                System.out.println("Nhân Viên: " + (hoaDon.getNhanVien() != null ? hoaDon.getNhanVien().getMaNhanVien() : "N/A"));
                System.out.println("Tên Khách Hàng: " + hoaDon.getTenNguoiNhan());
                System.out.println("Địa Chỉ: " + hoaDon.getDiaChi());
                System.out.println("Số Điện Thoại: " + hoaDon.getSoDienThoaiNguoiNhan());
                System.out.println("Trạng Thái: " + hoaDon.getTrangThai());
                System.out.println("Số lượng chi tiết hóa đơn: " + (hoaDon.getHoaDonChiTiets() != null ? hoaDon.getHoaDonChiTiets().size() : "null"));

                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(hoaDon.getId());
                row.createCell(1).setCellValue(hoaDon.getMaHoaDon() != null ? hoaDon.getMaHoaDon() : "N/A");

                // Chuyển đổi Instant sang Date để định dạng
                row.createCell(2).setCellValue(hoaDon.getNgayTao() != null ? dateFormat.format(Date.from(hoaDon.getNgayTao())) : "N/A");

                // Sử dụng ngayDatHang thay vì ngayThanhToan (vì không có field ngayThanhToan trong entity)
                row.createCell(3).setCellValue(hoaDon.getNgayDatHang() != null ? dateFormat.format(Date.from(hoaDon.getNgayDatHang())) : "N/A");

                BigDecimal tongThanhTien = hoaDon.getHoaDonChiTiets() != null ?
                        hoaDon.getHoaDonChiTiets().stream()
                                .map(item -> item.getGiaSanPham() != null ? item.getGiaSanPham().multiply(BigDecimal.valueOf(item.getSoLuong())) : BigDecimal.ZERO)
                                .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;
                row.createCell(4).setCellValue(tongThanhTien.doubleValue());
                System.out.println("Tổng Thanh Tiền: " + tongThanhTien);

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
                        System.out.println("Chi tiết hóa đơn - ID Hóa Đơn: " + (chiTiet.getHoaDon() != null ? chiTiet.getHoaDon().getId() : "N/A"));
                        System.out.println("Tên Sản Phẩm: " + (chiTiet.getChiTietSanPham() != null && chiTiet.getChiTietSanPham().getIdSanPham() != null ?
                                chiTiet.getChiTietSanPham().getIdSanPham().getTenSanPham() : "N/A"));
                        System.out.println("Chất liệu: " + (chiTiet.getChiTietSanPham() != null && chiTiet.getChiTietSanPham().getIdChatLieu() != null ?
                                chiTiet.getChiTietSanPham().getIdChatLieu().getTenChatLieu() : "N/A"));
                        System.out.println("Loại bìa: " + (chiTiet.getChiTietSanPham() != null && chiTiet.getChiTietSanPham().getIdLoaiBia() != null ?
                                chiTiet.getChiTietSanPham().getIdLoaiBia().getTenLoaiBia() : "N/A"));
                        System.out.println("Ngôn ngữ: " + (chiTiet.getChiTietSanPham() != null && chiTiet.getChiTietSanPham().getIdNgonNgu() != null ?
                                chiTiet.getChiTietSanPham().getIdNgonNgu().getTenNgonNgu() : "N/A"));
                        System.out.println("Tác giả: " + (chiTiet.getChiTietSanPham() != null && chiTiet.getChiTietSanPham().getIdTacGia() != null ?
                                chiTiet.getChiTietSanPham().getIdTacGia().getTenTacGia() : "N/A"));
                        System.out.println("Số Lượng: " + chiTiet.getSoLuong());
                        System.out.println("Giá Bán: " + (chiTiet.getGiaSanPham() != null ? chiTiet.getGiaSanPham().doubleValue() : "N/A"));
                        System.out.println("Tổng Tiền: " + (chiTiet.getGiaSanPham() != null ?
                                chiTiet.getGiaSanPham().multiply(BigDecimal.valueOf(chiTiet.getSoLuong())).doubleValue() : "N/A"));

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
                } else {
                    System.out.println("Hóa đơn " + hoaDon.getId() + " không có chi tiết hóa đơn.");
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