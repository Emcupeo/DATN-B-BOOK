package org.example.datnbbook.controller;

import jakarta.mail.MessagingException;
import org.example.datnbbook.dto.PhieuGiamGiaDTO;
import org.example.datnbbook.model.EmailRequest;
import org.example.datnbbook.service.EmailService;
import org.example.datnbbook.service.PhieuGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/admin/phieu-giam-gia")
@CrossOrigin(origins = "http://localhost:3000")
public class PhieuGiamGiaController {

    private static final Logger logger = LoggerFactory.getLogger(PhieuGiamGiaController.class);

    @Autowired
    private PhieuGiamGiaService service;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public ResponseEntity<Page<PhieuGiamGiaDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String loaiApDung,
            @RequestParam(required = false) String loaiPhieu,
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) String tinhTrang,
            @RequestParam(required = false) String searchQuery,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir) {

        logger.info("Received request with params: page={}, size={}, sortBy={}, sortDir={}, loaiApDung={}, loaiPhieu={}, trangThai={}, tinhTrang={}, searchQuery={}, fromDate={}, toDate={}",
                page, size, sortBy, sortDir, loaiApDung, loaiPhieu, trangThai, tinhTrang, searchQuery, fromDate, toDate);

        // Xử lý tham số trangThai
        Boolean trangThaiBoolean = null;
        if (trangThai != null && !trangThai.equals("null")) {
            trangThaiBoolean = Boolean.parseBoolean(trangThai);
        }

        // Create pageable with sort
        Sort sort;
        try {
            sort = sortBy != null && !sortBy.isEmpty() ?
                    Sort.by(Sort.Direction.fromString(sortDir), sortBy) :
                    Sort.by(Sort.Direction.DESC, "updatedAt");
            logger.info("Created Sort object: {}", sort);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid sortDir value: {}. Defaulting to updatedAt: DESC", sortDir);
            sort = Sort.by(Sort.Direction.DESC, "updatedAt");
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        logger.info("Created pageable: page={}, size={}, sort={}", page, size, sort);

        // Gọi service với các tham số
        Page<PhieuGiamGiaDTO> result = service.getAllDTO(
                pageable,
                loaiApDung,
                loaiPhieu,
                trangThaiBoolean,
                tinhTrang,
                searchQuery,
                fromDate,
                toDate
        );

        logger.info("Returning {} items for page {}", result.getContent().size(), page);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaDTO> getById(@PathVariable Long id) {
        return service.getDTOById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PhieuGiamGiaDTO> create(@RequestBody PhieuGiamGiaDTO dto) {
        PhieuGiamGiaDTO result = service.createPhieuGiamGiaAndReturnDTO(dto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaDTO> update(@PathVariable Long id, @RequestBody PhieuGiamGiaDTO dto) {
        try {
            dto.setId(id);
            PhieuGiamGiaDTO result = service.updatePhieuGiamGia(dto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.existsById(id)) {
            service.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/send-voucher-email")
    public ResponseEntity<String> sendVoucherEmail(@RequestBody EmailRequest emailRequest) {
        try {
            emailService.sendVoucherEmail(
                    emailRequest.getTo(),
                    emailRequest.getCustomerName(),
                    emailRequest.getVoucherType(),
                    emailRequest.getVoucherValue());
            return ResponseEntity.ok("Email gửi thành công");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi gửi email: " + e.getMessage());
        }
    }
}