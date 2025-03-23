package org.example.datnbbook.controller;

import org.example.datnbbook.model.HoaDon;
import org.example.datnbbook.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api")
public class HoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    // Lấy danh sách tất cả hóa đơn
    @GetMapping("/hoa-don")
    public ResponseEntity<List<HoaDon>> getHoaDon() {
        List<HoaDon> hoaDons = hoaDonService.getAllHoaDon();
        if (hoaDons.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(hoaDons, HttpStatus.OK);
    }

    // Lấy chi tiết hóa đơn theo ID
    @GetMapping("/hoa-don/{id}")
    public ResponseEntity<HoaDon> getHoaDon(@PathVariable int id) {
        HoaDon hoaDon = hoaDonService.getHoaDonById(id);
        if (hoaDon == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(hoaDon, HttpStatus.OK);
    }

    // Tạo QR Code từ id hóa đơn
    private String generateQRCodeImage(int id) throws WriterException, IOException {
        String qrCodeData = "HoaDonID:" + id; // Dữ liệu mã QR (có thể tùy chỉnh, ví dụ: URL)
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, 100, 100); // Kích thước 100x100

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        // Chuyển thành chuỗi Base64 để nhúng vào HTML
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(pngData);
    }

    // In hóa đơn dưới dạng PDF
    @GetMapping("/invoice/{id}")
    public ResponseEntity<byte[]> getInvoice(@PathVariable int id) throws Exception {
        // Lấy dữ liệu hóa đơn
        HoaDon hoaDon = hoaDonService.getHoaDonById(id);
        if (hoaDon == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Tính tổng tiền hàng
        BigDecimal tongTienHang = hoaDon.getHoaDonChiTiets().stream()
                .map(item -> item.getGiaSanPham().multiply(BigDecimal.valueOf(item.getSoLuong())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tính tiền giảm giá
        BigDecimal tienGiamGia = BigDecimal.ZERO;
        if (hoaDon.getPhieuGiamGia() != null && hoaDon.getPhieuGiamGia().getSoPhanTramGiam() != null) {
            BigDecimal soPhanTramGiam = hoaDon.getPhieuGiamGia().getSoPhanTramGiam();
            tienGiamGia = tongTienHang.multiply(soPhanTramGiam).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
        }

        // Tính thành tiền
        BigDecimal phiShip = (hoaDon.getPhiShip() != null) ? hoaDon.getPhiShip() : BigDecimal.ZERO;
        BigDecimal thanhTien = tongTienHang.subtract(tienGiamGia).add(phiShip);

        // Tạo QR Code
        String qrCodeImage;
        try {
            qrCodeImage = generateQRCodeImage(id);
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Lỗi khi tạo QR Code: " + e.getMessage(), e);
        }

        // Tạo HTML từ template
        Context context = new Context();
        context.setVariable("hoaDon", hoaDon);
        context.setVariable("tongTienHang", tongTienHang);
        context.setVariable("tienGiamGia", tienGiamGia);
        context.setVariable("thanhTien", thanhTien);
        context.setVariable("qrCodeImage", qrCodeImage); // Truyền QR code vào template
        String html = templateEngine.process("invoice", context);

        // Chuyển HTML thành PDF bằng iText 7
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setBaseUri("http://localhost:8080"); // Thiết lập base URI
            HtmlConverter.convertToPdf(html, out, converterProperties);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo PDF: " + e.getMessage(), e);
        }

        // Trả về file PDF
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "hoa_don_" + id + ".pdf");
        return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
    }
}