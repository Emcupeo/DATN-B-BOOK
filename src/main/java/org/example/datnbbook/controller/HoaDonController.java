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
    public ResponseEntity<List<HoaDon>> getHoaDon(@RequestParam(required = false) String trangThai) {
        List<HoaDon> hoaDons;
        if (trangThai != null && !trangThai.isEmpty()) {
            hoaDons = hoaDonService.getAllHoaDon().stream()
                    .filter(hd -> trangThai.equals(hd.getTrangThai()))
                    .toList();
        } else {
            hoaDons = hoaDonService.getAllHoaDon();
        }
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
            String loaiHoaDon = (String) paymentData.get("loaiHoaDon");
            BigDecimal tienKhachDua = paymentData.get("tienKhachDua") != null ? new BigDecimal(paymentData.get("tienKhachDua").toString()) : BigDecimal.ZERO;

            if (phuongThucThanhToanId == null) {
                throw new IllegalArgumentException("Phương thức thanh toán ID không được để trống!");
            }
            if (loaiHoaDon == null || loaiHoaDon.isEmpty()) {
                throw new IllegalArgumentException("Loại hóa đơn không được để trống!");
            }

            HoaDon updatedHoaDon = hoaDonService.updatePayment(id, tienMat, chuyenKhoan, phuongThucThanhToanId, ghiChu, loaiHoaDon, tienKhachDua);
            return new ResponseEntity<>(updatedHoaDon, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/hoa-don/{id}/cap-nhat-thong-tin-khach-hang")
    public ResponseEntity<HoaDon> updateCustomerInfo(@PathVariable int id, @RequestBody Map<String, Object> customerData) {
        try {
            Long idKhachHang = customerData.get("idKhachHang") != null ? Long.parseLong(customerData.get("idKhachHang").toString()) : null;
            String tenNguoiNhan = (String) customerData.get("tenNguoiNhan");
            String soDienThoaiNguoiNhan = (String) customerData.get("soDienThoaiNguoiNhan");
            String diaChiGiaoHang = (String) customerData.get("diaChiGiaoHang");

            HoaDon updatedHoaDon = hoaDonService.updateCustomerInfo(id, idKhachHang, tenNguoiNhan, soDienThoaiNguoiNhan, diaChiGiaoHang);
            return new ResponseEntity<>(updatedHoaDon, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/hoa-don")
    public ResponseEntity<HoaDon> createHoaDon(@RequestBody Map<String, String> request) {
        try {
            String loaiHoaDon = request.get("loaiHoaDon");
            if (loaiHoaDon == null || loaiHoaDon.isEmpty()) {
                throw new IllegalArgumentException("Loại hóa đơn không được để trống!");
            }
            HoaDon newHoaDon = hoaDonService.createHoaDon(loaiHoaDon);
            return new ResponseEntity<>(newHoaDon, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/hoa-don/{id}/add-product")
    public ResponseEntity<HoaDonChiTiet> addProductToOrder(@PathVariable int id, @RequestBody Map<String, Object> productData) {
        try {
            int chiTietSanPhamId = Integer.parseInt(productData.get("chiTietSanPhamId").toString());
            int soLuong = Integer.parseInt(productData.get("soLuong").toString());
            BigDecimal giaSanPham = new BigDecimal(productData.get("giaSanPham").toString());
            HoaDonChiTiet hoaDonChiTiet = hoaDonService.addProductToOrder(id, chiTietSanPhamId, soLuong, giaSanPham);
            return new ResponseEntity<>(hoaDonChiTiet, HttpStatus.OK);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/hoa-don/{id}/update-product/{chiTietId}")
    public ResponseEntity<HoaDonChiTiet> updateProductQuantity(@PathVariable int id, @PathVariable int chiTietId, @RequestBody Map<String, Object> updatedData) {
        try {
            int soLuong = Integer.parseInt(updatedData.get("soLuong").toString());
            HoaDonChiTiet updatedChiTiet = hoaDonService.updateProductQuantity(id, chiTietId, soLuong);
            return new ResponseEntity<>(updatedChiTiet, HttpStatus.OK);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/hoa-don/{id}/remove-product/{chiTietId}")
    public ResponseEntity<Void> removeProductFromOrder(@PathVariable int id, @PathVariable int chiTietId) {
        try {
            hoaDonService.removeProductFromOrder(id, chiTietId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}