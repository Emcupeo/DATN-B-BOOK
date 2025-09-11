package org.example.datnbbook.controller;

import org.example.datnbbook.dto.DotGiamGiaBoSachChiTietDTO;
import org.example.datnbbook.service.DotGiamGiaBoSachChiTietService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping("/api/dot-giam-gia-bo-sach-chi-tiet")
public class DotGiamGiaBoSachChiTietController {
    private static final Logger logger = LoggerFactory.getLogger(DotGiamGiaBoSachChiTietController.class);

    @Autowired
    private DotGiamGiaBoSachChiTietService service;

    @GetMapping
    public ResponseEntity<List<DotGiamGiaBoSachChiTietDTO>> getAll() {
        logger.info("[INFO] Fetching all DotGiamGiaBoSachChiTiet");
        try {
            List<DotGiamGiaBoSachChiTietDTO> result = service.getAll();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("[ERROR] Failed to fetch DotGiamGiaBoSachChiTiet: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/dot-giam-gia/{idDotGiamGia}")
    public ResponseEntity<List<DotGiamGiaBoSachChiTietDTO>> getByIdDotGiamGia(@PathVariable Integer idDotGiamGia) {
        logger.info("[INFO] Fetching DotGiamGiaBoSachChiTiet by idDotGiamGia: {}", idDotGiamGia);
        try {
            List<DotGiamGiaBoSachChiTietDTO> result = service.getByIdDotGiamGia(idDotGiamGia);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("[ERROR] Failed to fetch DotGiamGiaBoSachChiTiet by idDotGiamGia: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/bo-sach/{idBoSach}")
    public ResponseEntity<List<DotGiamGiaBoSachChiTietDTO>> getByIdBoSach(@PathVariable Integer idBoSach) {
        logger.info("[INFO] Fetching DotGiamGiaBoSachChiTiet by idBoSach: {}", idBoSach);
        try {
            List<DotGiamGiaBoSachChiTietDTO> result = service.getByIdBoSach(idBoSach);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("[ERROR] Failed to fetch DotGiamGiaBoSachChiTiet by idBoSach: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/bo-sach/{idBoSach}/active")
    public ResponseEntity<List<DotGiamGiaBoSachChiTietDTO>> getActiveDiscountsByIdBoSach(@PathVariable Integer idBoSach) {
        logger.info("[INFO] Fetching active discounts for BoSach: {}", idBoSach);
        try {
            List<DotGiamGiaBoSachChiTietDTO> result = service.getActiveDiscountsByIdBoSach(idBoSach);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("[ERROR] Failed to fetch active discounts for BoSach: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<DotGiamGiaBoSachChiTietDTO> create(@RequestBody DotGiamGiaBoSachChiTietDTO dto) {
        logger.info("[INFO] Creating DotGiamGiaBoSachChiTiet for BoSach: {} and DotGiamGia: {}", 
                   dto.getIdBoSach(), dto.getIdDotGiamGia());
        try {
            DotGiamGiaBoSachChiTietDTO result = service.create(dto);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("[ERROR] Failed to create DotGiamGiaBoSachChiTiet: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("[ERROR] Failed to create DotGiamGiaBoSachChiTiet: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DotGiamGiaBoSachChiTietDTO> update(@PathVariable Long id, @RequestBody DotGiamGiaBoSachChiTietDTO dto) {
        logger.info("[INFO] Updating DotGiamGiaBoSachChiTiet with id: {}", id);
        try {
            DotGiamGiaBoSachChiTietDTO result = service.update(id, dto);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            logger.error("[ERROR] Failed to update DotGiamGiaBoSachChiTiet: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("[ERROR] Failed to update DotGiamGiaBoSachChiTiet: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("[INFO] Deleting DotGiamGiaBoSachChiTiet with id: {}", id);
        try {
            service.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.error("[ERROR] Failed to delete DotGiamGiaBoSachChiTiet: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("[ERROR] Failed to delete DotGiamGiaBoSachChiTiet: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/apply-discounts")
    public ResponseEntity<Void> applyDiscounts() {
        logger.info("[INFO] Applying active discounts to BoSach");
        try {
            service.applyDiscounts();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("[ERROR] Failed to apply discounts: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/revert-discounts")
    public ResponseEntity<Void> revertDiscounts() {
        logger.info("[INFO] Reverting expired discounts");
        try {
            service.revertDiscounts();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("[ERROR] Failed to revert discounts: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
