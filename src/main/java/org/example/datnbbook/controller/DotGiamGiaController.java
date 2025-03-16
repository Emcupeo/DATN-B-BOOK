package org.example.datnbbook.controller;

import org.example.datnbbook.model.DotGiamGia;
import org.example.datnbbook.service.DotGiamGiaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@CrossOrigin(origins = "http://localhost:3000")
public class DotGiamGiaController {
    private static final Logger logger = LoggerFactory.getLogger(DotGiamGiaController.class);

    @Autowired
    private DotGiamGiaService service;

    @GetMapping
    public ResponseEntity<List<DotGiamGia>> getAll() {
        logger.info("Lấy danh sách tất cả đợt giảm giá");
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DotGiamGia dotGiamGia) {
        logger.info("Nhận yêu cầu tạo mới: {}", dotGiamGia);
        try {
            DotGiamGia saved = service.create(dotGiamGia);
            logger.info("Tạo thành công: {}", saved);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            logger.error("Lỗi khi tạo mới: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Lỗi khi tạo mới: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody DotGiamGia dotGiamGia) {
        logger.info("Nhận yêu cầu cập nhật ID {}: {}", id, dotGiamGia);
        try {
            dotGiamGia.setId(id);
            DotGiamGia updated = service.update(dotGiamGia);
            logger.info("Cập nhật thành công: {}", updated);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Lỗi khi cập nhật: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        logger.info("Nhận yêu cầu xóa ID: {}", id);
        try {
            service.delete(id);
            logger.info("Xóa thành công ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Lỗi khi xóa: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}