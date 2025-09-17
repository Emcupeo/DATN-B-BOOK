package org.example.datnbbook.controller;

import org.example.datnbbook.model.BoSachChiTiet;
import org.example.datnbbook.model.BoSachChiTietId;
import org.example.datnbbook.model.ChiTietSanPham;
import org.example.datnbbook.repository.BoSachChiTietRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping("/api/bo-sach-chi-tiet")
public class BoSachChiTietController {
    private static final Logger logger = LoggerFactory.getLogger(BoSachChiTietController.class);

    @Autowired
    private BoSachChiTietRepository repository;

    @GetMapping("")
    public ResponseEntity<List<BoSachChiTiet>> getAll() {
        logger.info("[INFO] Fetching all BoSachChiTiet");
        List<BoSachChiTiet> allItems = repository.findAll();
        return ResponseEntity.ok(allItems);
    }

    @GetMapping("/{boSachId}/{chiTietSanPhamId}")
    public ResponseEntity<BoSachChiTiet> getById(@PathVariable Integer boSachId, @PathVariable Integer chiTietSanPhamId) {
        logger.info("[INFO] Fetching BoSachChiTiet by boSachId: {} and chiTietSanPhamId: {}", boSachId, chiTietSanPhamId);
        BoSachChiTietId id = new BoSachChiTietId();
        id.setIdBoSach(boSachId);
        id.setIdChiTietSanPham(chiTietSanPhamId);
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/bo-sach/{boSachId}")
    public ResponseEntity<List<BoSachChiTiet>> getByBoSachId(@PathVariable Integer boSachId) {
        logger.info("[INFO] Fetching BoSachChiTiet by boSachId: {}", boSachId);
        List<BoSachChiTiet> items = repository.findByIdBoSachId(boSachId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/detailed/bo-sach/{boSachId}")
    public ResponseEntity<List<BoSachChiTiet>> getDetailedByBoSachId(@PathVariable Integer boSachId) {
        logger.info("[INFO] Fetching detailed BoSachChiTiet by boSachId: {}", boSachId);
        List<BoSachChiTiet> items = repository.findByIdBoSachId(boSachId);
        logger.info("[INFO] Found {} BoSachChiTiet items", items.size());
        
        // Force load relationships
        for (BoSachChiTiet item : items) {
            logger.info("[INFO] Processing BoSachChiTiet item: {}", item.getId());
            
            if (item.getIdChiTietSanPham() != null) {
                ChiTietSanPham chiTietSanPham = item.getIdChiTietSanPham();
                logger.info("[INFO] ChiTietSanPham: {} - {}", chiTietSanPham.getMaChiTietSanPham(), chiTietSanPham.getTenChiTietSanPham());
                
                // Force load all properties
                chiTietSanPham.getTenChiTietSanPham();
                chiTietSanPham.getMaChiTietSanPham();
                chiTietSanPham.getSoLuongTon();
                chiTietSanPham.getGia();
                
                // Force load TacGia
                if (chiTietSanPham.getIdTacGia() != null) {
                    chiTietSanPham.getIdTacGia().getTenTacGia();
                    logger.info("[INFO] TacGia: {}", chiTietSanPham.getIdTacGia().getTenTacGia());
                } else {
                    logger.warn("[WARN] ChiTietSanPham {} has no TacGia", chiTietSanPham.getId());
                }
                
                // Force load other relationships
                if (chiTietSanPham.getIdLoaiBia() != null) {
                    chiTietSanPham.getIdLoaiBia().getTenLoaiBia();
                }
                if (chiTietSanPham.getIdNhaXuatBan() != null) {
                    chiTietSanPham.getIdNhaXuatBan().getTenNhaXuatBan();
                }
                if (chiTietSanPham.getIdChatLieu() != null) {
                    chiTietSanPham.getIdChatLieu().getTenChatLieu();
                }
                if (chiTietSanPham.getIdNguoiDich() != null) {
                    chiTietSanPham.getIdNguoiDich().getTenNguoiDich();
                }
                if (chiTietSanPham.getIdTheLoai() != null) {
                    chiTietSanPham.getIdTheLoai().getTenTheLoai();
                }
                if (chiTietSanPham.getIdNgonNgu() != null) {
                    chiTietSanPham.getIdNgonNgu().getTenNgonNgu();
                }
            } else {
                logger.warn("[WARN] BoSachChiTiet item {} has no ChiTietSanPham", item.getId());
            }
        }
        
        logger.info("[INFO] Returning {} BoSachChiTiet items with loaded relationships", items.size());
        return ResponseEntity.ok(items);
    }
}
