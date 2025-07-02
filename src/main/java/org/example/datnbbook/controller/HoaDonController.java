package org.example.datnbbook.controller;

import org.example.datnbbook.model.HoaDon;
import org.example.datnbbook.model.HoaDonChiTiet;
import org.example.datnbbook.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api")
public class HoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    @GetMapping("/hoa-don")
    public ResponseEntity<List<HoaDon>> getHoaDon() {
        List<HoaDon> hoaDons = hoaDonService.getAllHoaDon();
        if (hoaDons.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(hoaDons, HttpStatus.OK);
    }

    @GetMapping("/hoa-don/{id}")
    public ResponseEntity<HoaDon> getHoaDon(@PathVariable int id) {
        HoaDon hoaDon = hoaDonService.getHoaDonById(id);
        if (hoaDon == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(hoaDon, HttpStatus.OK);
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<byte[]> getInvoice(@PathVariable int id) {
        try {
            ByteArrayOutputStream pdfOutput = hoaDonService.printInvoiceToPdf(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "hoa_don_" + id + ".pdf");
            return new ResponseEntity<>(pdfOutput.toByteArray(), headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException | com.google.zxing.WriterException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/hoa-don/export-excel")
    public ResponseEntity<byte[]> exportHoaDonToExcel() {
        try {
            ByteArrayOutputStream excelOutput = hoaDonService.exportHoaDonToExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "danh_sach_hoa_don.xlsx");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<>(excelOutput.toByteArray(), headers, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/hoa-don/{id}/cap-nhat-trang-thai")
    public ResponseEntity<HoaDon> updateTrangThai(@PathVariable int id, @RequestBody String trangThaiMoi) {
        try {
            String cleanedTrangThaiMoi = trangThaiMoi.replace("\"", "");
            HoaDon updatedHoaDon = hoaDonService.updateTrangThai(id, cleanedTrangThaiMoi);
            return new ResponseEntity<>(updatedHoaDon, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/hoa-don/{id}/huy-don")
    public ResponseEntity<HoaDon> huyDon(@PathVariable int id) {
        try {
            HoaDon huyHoaDon = hoaDonService.huyDon(id);
            return new ResponseEntity<>(huyHoaDon, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/hoa-don/{id}/cap-nhat-thanh-toan")
    public ResponseEntity<HoaDon> updatePayment(@PathVariable int id, @RequestBody Map<String, Object> paymentData) {
        try {
            BigDecimal tienMat = paymentData.get("tienMat") != null ? new BigDecimal(paymentData.get("tienMat").toString()) : BigDecimal.ZERO;
            BigDecimal chuyenKhoan = paymentData.get("chuyenKhoan") != null ? new BigDecimal(paymentData.get("chuyenKhoan").toString()) : BigDecimal.ZERO;
            Integer phuongThucThanhToanId = paymentData.get("phuongThucThanhToanId") != null ?
                    Integer.parseInt(paymentData.get("phuongThucThanhToanId").toString()) : null;
            String ghiChu = (String) paymentData.get("ghiChu");

            if (phuongThucThanhToanId == null) {
                throw new IllegalArgumentException("Phương thức thanh toán ID không được để trống!");
            }

            HoaDon updatedHoaDon = hoaDonService.updatePayment(id, tienMat, chuyenKhoan, phuongThucThanhToanId, ghiChu);
            return new ResponseEntity<>(updatedHoaDon, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/hoa-don/{orderId}/chi-tiet")
    public ResponseEntity<HoaDonChiTiet> addProductToOrder(@PathVariable int orderId, @RequestBody HoaDonChiTiet productData) {
        try {
            HoaDonChiTiet addedProduct = hoaDonService.addProductToOrder(orderId, productData);
            return new ResponseEntity<>(addedProduct, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/hoa-don/{orderId}/chi-tiet/{itemId}")
    public ResponseEntity<HoaDonChiTiet> updateProductQuantity(@PathVariable int orderId, @PathVariable int itemId, @RequestBody HoaDonChiTiet updatedData) {
        try {
            HoaDonChiTiet result = hoaDonService.updateProductQuantity(orderId, itemId, updatedData);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/hoa-don/{orderId}/chi-tiet/{itemId}")
    public ResponseEntity<Void> removeProductFromOrder(@PathVariable int orderId, @PathVariable int itemId) {
        try {
            hoaDonService.removeProductFromOrder(orderId, itemId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}