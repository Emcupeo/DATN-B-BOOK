package org.example.datnbbook.controller;

import jakarta.validation.Valid;
import org.example.datnbbook.dto.BoSachDTO;
import org.example.datnbbook.model.BoSach;
import org.example.datnbbook.model.BoSachChiTiet;
import org.example.datnbbook.service.BoSachService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping("/api/bo-sach")
public class BoSachController {
    private static final Logger logger = LoggerFactory.getLogger(BoSachController.class);

    @Autowired
    private BoSachService boSachService;

    @GetMapping
    public ResponseEntity<List<BoSach>> getAll() {
        logger.info("[INFO] Fetching all BoSach");
        List<BoSach> boSachs = boSachService.getAll();
        return ResponseEntity.ok(boSachs);
    }

    @PostMapping
    public ResponseEntity<BoSachDTO> create(@Valid @RequestBody BoSachDTO boSachDTO) {
        logger.info("[INFO] Creating new BoSach: {}", boSachDTO.getTenBoSach());
        try {
            BoSach createdBoSach = boSachService.create(boSachDTO);
            BoSachDTO responseDTO = mapToDTO(createdBoSach);
            logger.debug("[DEBUG] Created BoSach: {}", responseDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("[ERROR] Failed to create BoSach: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BoSachDTO() {{ setTenBoSach("Error: " + e.getMessage()); }});
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoSachDTO> update(@PathVariable Integer id, @Valid @RequestBody BoSachDTO boSachDTO) {
        logger.info("[INFO] Updating BoSach with id: {}", id);
        try {
            BoSach updatedBoSach = boSachService.update(id, boSachDTO);
            BoSachDTO responseDTO = mapToDTO(updatedBoSach);
            logger.debug("[DEBUG] Updated BoSach: {}", responseDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            logger.error("[ERROR] Failed to update BoSach: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BoSachDTO() {{ setTenBoSach("Error: " + e.getMessage()); }});
        }
    }

    private BoSachDTO mapToDTO(BoSach boSach) {
        BoSachDTO dto = new BoSachDTO();
        dto.setId(boSach.getId());
        dto.setMaBoSach(boSach.getMaBoSach());
        dto.setTenBoSach(boSach.getTenBoSach());
        dto.setGiaTien(boSach.getGiaTien().doubleValue());
        dto.setMoTa(boSach.getMoTa());
        dto.setSoLuong(boSach.getSoLuong());
        dto.setUrl(boSach.getUrl());
        dto.setCreatedAt(boSach.getCreatedAt());
        dto.setUpdatedAt(boSach.getUpdatedAt());
        return dto;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoSach> getById(@PathVariable Integer id) {
        logger.info("[INFO] Fetching BoSach with id: {}", id);
        try {
            BoSach boSach = boSachService.getById(id);
            return ResponseEntity.ok(boSach);
        } catch (RuntimeException e) {
            logger.error("[ERROR] Failed to fetch BoSach: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        logger.info("[INFO] Deleting BoSach with id: {}", id);
        try {
            boSachService.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.error("[ERROR] Failed to delete BoSach: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<BoSach>> search(@RequestParam String keyword) {
        logger.info("[INFO] Searching BoSach with keyword: {}", keyword);
        List<BoSach> boSachs = boSachService.search(keyword);
        return ResponseEntity.ok(boSachs);
    }

    @GetMapping("/{id}/chi-tiet")
    public ResponseEntity<List<BoSachChiTiet>> getBoSachChiTietByBoSachId(@PathVariable Integer id) {
        logger.info("[INFO] Fetching BoSachChiTiet for idBoSach: {}", id);
        try {
            List<BoSachChiTiet> chiTietList = boSachService.getBoSachChiTietByBoSachId(id);
            if (chiTietList.isEmpty()) {
                logger.warn("[WARN] No BoSachChiTiet found for idBoSach: {}", id);
                return ResponseEntity.ok(chiTietList); // Return empty list instead of 404
            }
            logger.debug("[DEBUG] Found {} BoSachChiTiet entries", chiTietList.size());
            return ResponseEntity.ok(chiTietList);
        } catch (RuntimeException e) {
            logger.error("[ERROR] Failed to fetch BoSachChiTiet: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}/update-stock")
    public ResponseEntity<BoSachDTO> updateStock(@PathVariable Integer id, @RequestBody Map<String, Object> request) {
        logger.info("[INFO] Updating stock for BoSach with id: {}", id);
        try {
            Integer soLuong = (Integer) request.get("soLuong");
            if (soLuong == null) {
                return ResponseEntity.badRequest().build();
            }
            
            BoSach updatedBoSach = boSachService.updateStock(id, soLuong);
            BoSachDTO responseDTO = mapToDTO(updatedBoSach);
            logger.debug("[DEBUG] Updated BoSach stock: {}", responseDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            logger.error("[ERROR] Failed to update BoSach stock: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{idBoSach}/{idChiTietSanPham}")
    public ResponseEntity<BoSachChiTiet> updateBoSachChiTiet(
            @PathVariable Integer idBoSach,
            @PathVariable Integer idChiTietSanPham,
            @Valid @RequestBody BoSachChiTiet updatedData) {
        logger.info("[INFO] Updating BoSachChiTiet for idBoSach: {} and idChiTietSanPham: {}", idBoSach, idChiTietSanPham);
        try {
            BoSachChiTiet updatedChiTiet = boSachService.updateBoSachChiTiet(idBoSach, idChiTietSanPham, updatedData);
            logger.debug("[DEBUG] Updated BoSachChiTiet: {}", updatedChiTiet);
            return ResponseEntity.ok(updatedChiTiet);
        } catch (RuntimeException e) {
            logger.error("[ERROR] Failed to update BoSachChiTiet: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{idBoSach}/{idChiTietSanPham}")
    public ResponseEntity<Void> deleteBoSachChiTiet(
            @PathVariable Integer idBoSach,
            @PathVariable Integer idChiTietSanPham) {
        logger.info("[INFO] Deleting BoSachChiTiet for idBoSach: {} and idChiTietSanPham: {}", idBoSach, idChiTietSanPham);
        try {
            boSachService.deleteBoSachChiTiet(idBoSach, idChiTietSanPham);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.error("[ERROR] Failed to delete BoSachChiTiet: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}