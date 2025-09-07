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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "http://localhost:8081", "http://localhost:8082"})
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

    @GetMapping("/hoa-don/{id}/debug")
    public ResponseEntity<Map<String, Object>> getHoaDonDebug(@PathVariable int id) {
        HoaDon hoaDon = hoaDonService.getHoaDonById(id);
        if (hoaDon == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        Map<String, Object> debugInfo = new HashMap<>();
        debugInfo.put("id", hoaDon.getId());
        debugInfo.put("maHoaDon", hoaDon.getMaHoaDon());
        debugInfo.put("ngayDatHang", hoaDon.getNgayDatHang());
        debugInfo.put("tongTien", hoaDon.getTongTien());
        debugInfo.put("hoaDonChiTiets", hoaDon.getHoaDonChiTiets());
        debugInfo.put("hoaDonChiTietsSize", hoaDon.getHoaDonChiTiets() != null ? hoaDon.getHoaDonChiTiets().size() : 0);
        debugInfo.put("trangThai", hoaDon.getTrangThai());
        // Loại bỏ khachHang để tránh lỗi ByteBuddyInterceptor
        // debugInfo.put("khachHang", hoaDon.getKhachHang());
        
        return new ResponseEntity<>(debugInfo, HttpStatus.OK);
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
    public ResponseEntity<HoaDon> updatePayment(@PathVariable int id, @RequestBody Map<String, Object> request) {
        try {
            BigDecimal tienMat = new BigDecimal(request.get("tienMat").toString());
            BigDecimal chuyenKhoan = new BigDecimal(request.get("chuyenKhoan").toString());
            Integer phuongThucThanhToanId = Integer.valueOf(request.get("phuongThucThanhToanId").toString());
            String ghiChu = request.get("ghiChu") != null ? request.get("ghiChu").toString() : "";
            String loaiHoaDon = request.get("loaiHoaDon").toString();
            BigDecimal tienKhachDua = new BigDecimal(request.get("tienKhachDua").toString());
            Long phieuGiamGiaId = request.get("phieuGiamGiaId") != null ? 
                Long.valueOf(request.get("phieuGiamGiaId").toString()) : null;
            
            HoaDon updatedHoaDon = hoaDonService.updatePayment(id, tienMat, chuyenKhoan, phuongThucThanhToanId, ghiChu, loaiHoaDon, tienKhachDua, phieuGiamGiaId);
            return ResponseEntity.ok(updatedHoaDon);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/hoa-don/{id}/cap-nhat-thong-tin-khach-hang")
    public ResponseEntity<HoaDon> updateCustomerInfo(@PathVariable int id, @RequestBody Map<String, Object> customerData) {
        try {
            System.out.println("DEBUG: updateCustomerInfo called for order ID: " + id);
            System.out.println("DEBUG: Received customerData: " + customerData);
            
            Long idKhachHang = customerData.get("idKhachHang") != null ? Long.parseLong(customerData.get("idKhachHang").toString()) : null;
            String tenNguoiNhan = (String) customerData.get("tenNguoiNhan");
            String soDienThoaiNguoiNhan = (String) customerData.get("soDienThoaiNguoiNhan");
            // Nhận cả diaChi và diaChiGiaoHang để tương thích ngược
            String diaChi = (String) customerData.get("diaChi");
            String diaChiGiaoHang = (String) customerData.get("diaChiGiaoHang");
            // Ưu tiên diaChi, nếu không có thì dùng diaChiGiaoHang
            String finalDiaChi = diaChi != null ? diaChi : diaChiGiaoHang;
            
            System.out.println("DEBUG: Parsed data - idKhachHang: " + idKhachHang);
            System.out.println("DEBUG: Parsed data - tenNguoiNhan: " + tenNguoiNhan);
            System.out.println("DEBUG: Parsed data - soDienThoaiNguoiNhan: " + soDienThoaiNguoiNhan);
            System.out.println("DEBUG: Parsed data - diaChi: " + diaChi);
            System.out.println("DEBUG: Parsed data - diaChiGiaoHang: " + diaChiGiaoHang);
            System.out.println("DEBUG: Final diaChi to save: " + finalDiaChi);

            HoaDon updatedHoaDon = hoaDonService.updateCustomerInfo(id, idKhachHang, tenNguoiNhan, soDienThoaiNguoiNhan, finalDiaChi);
            System.out.println("DEBUG: Order updated successfully, diaChi in DB: " + updatedHoaDon.getDiaChi());
            return new ResponseEntity<>(updatedHoaDon, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            System.out.println("DEBUG: IllegalArgumentException in updateCustomerInfo: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("DEBUG: Exception in updateCustomerInfo: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/hoa-don/{id}")
    public ResponseEntity<HoaDon> updateHoaDon(@PathVariable int id, @RequestBody Map<String, Object> data) {
        try {
            HoaDon hoaDon = hoaDonService.updateHoaDon(id, data);
            return new ResponseEntity<>(hoaDon, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            System.out.println("DEBUG: addProductToOrder called with id=" + id + ", productData=" + productData);
            int chiTietSanPhamId = Integer.parseInt(productData.get("chiTietSanPhamId").toString());
            int soLuong = Integer.parseInt(productData.get("soLuong").toString());
            BigDecimal giaSanPham = new BigDecimal(productData.get("giaSanPham").toString());
            HoaDonChiTiet hoaDonChiTiet = hoaDonService.addProductToOrder(id, chiTietSanPhamId, soLuong, giaSanPham);
            return new ResponseEntity<>(hoaDonChiTiet, HttpStatus.OK);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("DEBUG: addProductToOrder error: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("DEBUG: addProductToOrder unexpected error: " + e.getMessage());
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

    // Test endpoint
    @GetMapping("/hoa-don/test")
    public ResponseEntity<String> testEndpoint() {
        return new ResponseEntity<>("Test endpoint working!", HttpStatus.OK);
    }

    // Tra cứu đơn hàng cho khách hàng chưa đăng nhập
    @PostMapping("/hoa-don/lookup-order")
    public ResponseEntity<HoaDon> lookupOrder(@RequestBody Map<String, String> request) {
        try {
            System.out.println("🔍 DEBUG: Received lookup request: " + request);
            
            String orderCode = request.get("orderCode");
            String phoneNumber = request.get("phoneNumber");
            
            System.out.println("🔍 DEBUG: orderCode = " + orderCode);
            System.out.println("🔍 DEBUG: phoneNumber = " + phoneNumber);
            
            if (orderCode == null || orderCode.trim().isEmpty()) {
                System.out.println("❌ DEBUG: orderCode is null or empty");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                System.out.println("❌ DEBUG: phoneNumber is null or empty");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            System.out.println("🔍 DEBUG: Calling service with orderCode=" + orderCode + ", phoneNumber=" + phoneNumber);
            HoaDon hoaDon = hoaDonService.lookupOrderByCodeAndPhone(orderCode.trim(), phoneNumber.trim());
            
            if (hoaDon == null) {
                System.out.println("❌ DEBUG: No order found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            System.out.println("✅ DEBUG: Order found: " + hoaDon.getMaHoaDon());
            return new ResponseEntity<>(hoaDon, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("❌ DEBUG: Exception in lookupOrder: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}