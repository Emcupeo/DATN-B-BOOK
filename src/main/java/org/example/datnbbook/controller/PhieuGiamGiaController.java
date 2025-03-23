package org.example.datnbbook.controller;

import jakarta.mail.MessagingException;
import org.example.datnbbook.dto.PhieuGiamGiaDTO;
import org.example.datnbbook.model.EmailRequest;
import org.example.datnbbook.model.PhieuGiamGia;
import org.example.datnbbook.repository.KhachHangRepository;
import org.example.datnbbook.service.EmailService;
import org.example.datnbbook.service.PhieuGiamGiaKhachHangService;
import org.example.datnbbook.service.PhieuGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/phieu-giam-gia")
@CrossOrigin(origins = "http://localhost:3000")
public class PhieuGiamGiaController {

    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;

    // ✅ Lấy danh sách DTO kèm thông tin khách hàng nếu có
    @GetMapping
    public ResponseEntity<Page<PhieuGiamGiaDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Page<PhieuGiamGiaDTO> dtoPage = phieuGiamGiaService.getAllDTO(PageRequest.of(page, size));
        return ResponseEntity.ok(dtoPage);
    }

    // ✅ Lấy chi tiết phiếu giảm giá → trả về DTO (có cả thông tin khách nếu có)
    @GetMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaDTO> getById(@PathVariable Long id) {
        return phieuGiamGiaService.getDTOById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Thêm mới
    @PostMapping
    public ResponseEntity<?> create(@RequestBody PhieuGiamGiaDTO dto) {
        try {
            PhieuGiamGiaDTO result = phieuGiamGiaService.createPhieuGiamGiaAndReturnDTO(dto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi tạo phiếu giảm giá: " + e.getMessage());
        }
    }

    // ✅ Cập nhật (nếu cần DTO thì refactor thêm)
    @PutMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaDTO> update(@PathVariable Long id, @RequestBody PhieuGiamGiaDTO dto) {
        if (!phieuGiamGiaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        dto.setId(id);
        PhieuGiamGiaDTO updated = phieuGiamGiaService.updatePhieuGiamGia(dto);
        return ResponseEntity.ok(updated);
    }

    // ✅ Xóa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!phieuGiamGiaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        phieuGiamGiaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    private EmailService emailService;

    // API gửi email thông báo cho khách hàng
    @PostMapping("/send-voucher-email")
    public ResponseEntity<String> sendVoucherEmail(@RequestBody EmailRequest emailRequest) {
        try {
            // Gửi email thông qua EmailService
            emailService.sendVoucherEmail(
                    emailRequest.getTo(),
                    emailRequest.getCustomerName(),
                    emailRequest.getVoucherType(),
                    emailRequest.getVoucherValue()
            );
            return ResponseEntity.ok("Email gửi thành công");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi gửi email: " + e.getMessage());
        }
    }
}

