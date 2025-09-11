package org.example.datnbbook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.datnbbook.dto.SanPhamDTO;
import org.example.datnbbook.dto.SanPhamRequest;
import org.example.datnbbook.dto.ErrorResponse;
import org.example.datnbbook.model.*;
import org.example.datnbbook.repository.ChiTietSanPhamAnhRepository;
import org.example.datnbbook.repository.ChiTietSanPhamRepository;
import org.example.datnbbook.repository.LoaiBiaRepository;
import org.example.datnbbook.service.AnhSanPhamService;
import org.example.datnbbook.service.ChiTietSanPhamService;
import org.example.datnbbook.service.SanPhamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api/san-pham")
public class SanPhamController {
    private static final Logger logger = LoggerFactory.getLogger(SanPhamController.class);

    private final SanPhamService sanPhamService;
    private final ChiTietSanPhamService chiTietSanPhamService;

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;
    @Autowired
    private AnhSanPhamService anhSanPhamService;
    @Autowired
    private LoaiBiaRepository loaiBiaRepository;
    @Autowired
    private ChiTietSanPhamAnhRepository chiTietSanPhamAnhRepository;

    public SanPhamController(SanPhamService sanPhamService, ChiTietSanPhamService chiTietSanPhamService) {
        this.sanPhamService = sanPhamService;
        this.chiTietSanPhamService = chiTietSanPhamService;
    }

    @GetMapping
    public ResponseEntity<List<SanPham>> getAll() {
        logger.info("[INFO] Fetching all SanPham");
        List<SanPham> sanPhams = sanPhamService.getAll();
        logger.debug("[DEBUG] Retrieved {} SanPham records", sanPhams.size());
        return ResponseEntity.ok(sanPhams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanPham> getById(@PathVariable Integer id) {
        logger.info("[INFO] Fetching SanPham with id: {}", id);
        SanPham sanPham = sanPhamService.getById(id);
        logger.debug("[DEBUG] Retrieved SanPham: {}", sanPham);
        return ResponseEntity.ok(sanPham);
    }

    @PostMapping
    public ResponseEntity<SanPham> create(@RequestBody SanPham sanPham) {
        logger.info("[INFO] Creating new SanPham: {}", sanPham.getTenSanPham());
        SanPham createdSanPham = sanPhamService.create(sanPham);
        logger.debug("[DEBUG] Created SanPham: {}", createdSanPham);
        return ResponseEntity.ok(createdSanPham);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanPhamDTO> update(@PathVariable Integer id, @RequestBody SanPhamDTO sanPham) {
        logger.info("[INFO] Updating SanPham with id: {}", id);
        SanPhamDTO updatedSanPham = sanPhamService.update(id, sanPham);
        logger.debug("[DEBUG] Updated SanPham: {}", updatedSanPham);
        return ResponseEntity.ok(updatedSanPham);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        logger.info("[INFO] Deleting SanPham with id: {}", id);
        try {
            sanPhamService.delete(id);
            logger.debug("[DEBUG] Deleted SanPham with id: {}", id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            logger.error("[ERROR] Failed to delete SanPham: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("[ERROR] Server error when deleting SanPham: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ErrorResponse("Lỗi server: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<SanPham>> search(@RequestParam String keyword) {
        logger.info("[INFO] Searching SanPham with keyword: {}", keyword);
        List<SanPham> sanPhams = sanPhamService.search(keyword);
        logger.debug("[DEBUG] Found {} SanPham records for keyword: {}", sanPhams.size(), keyword);
        return ResponseEntity.ok(sanPhams);
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<SanPham>> getByCategory(@RequestParam Integer danhMucId) {
        logger.info("[INFO] Fetching SanPham by category id: {}", danhMucId);
        List<SanPham> sanPhams = sanPhamService.getByDanhMucId(danhMucId);
        logger.debug("[DEBUG] Found {} SanPham records for category: {}", sanPhams.size(), danhMucId);
        return ResponseEntity.ok(sanPhams);
    }

    @PostMapping(value = "/create-with-details", consumes = "multipart/form-data")
    public ResponseEntity<SanPham> createWithDetails(
            @RequestParam("tenSanPham") String tenSanPham,
            @RequestParam(value = "moTaSanPham", required = false) String moTaSanPham,
            @RequestParam(value = "idDanhMuc", required = false) Integer idDanhMuc,
            @RequestParam(value = "idTacGia", required = false) Integer idTacGia,
            @RequestParam(value = "idNhaXuatBan", required = false) Integer idNhaXuatBan,
            @RequestParam(value = "idNguoiDich", required = false) Integer idNguoiDich,
            @RequestParam(value = "idTheLoai", required = false) Integer idTheLoai,
            @RequestParam(value = "idNgonNgu", required = false) Integer idNgonNgu,
            @RequestParam(value = "moTaChiTiet", required = false) String moTaChiTiet,
            @RequestParam("chiTietSanPhamList") String chiTietSanPhamListJson
    ) throws Exception {
        logger.info("[INFO] Creating SanPham with details: tenSanPham={}", tenSanPham);
        logger.debug("[DEBUG] Request parameters: moTaSanPham={}, idDanhMuc={}, idTacGia={}, idNhaXuatBan={}, idNguoiDich={}, idTheLoai={}, idNgonNgu={}, moTaChiTiet={}",
                moTaSanPham, idDanhMuc, idTacGia, idNhaXuatBan, idNguoiDich, idTheLoai, idNgonNgu, moTaChiTiet);

        try {
            // Tạo SanPhamRequest
            SanPhamRequest requestSP = new SanPhamRequest();
            requestSP.setTenSanPham(tenSanPham);
            requestSP.setMoTaSanPham(moTaSanPham);
            requestSP.setIdDanhMuc(idDanhMuc);
            requestSP.setIdTacGia(idTacGia);
            requestSP.setIdNhaXuatBan(idNhaXuatBan);
            requestSP.setIdNguoiDich(idNguoiDich);
            requestSP.setIdTheLoai(idTheLoai);
            requestSP.setIdNgonNgu(idNgonNgu);
            requestSP.setMoTaChiTiet(moTaChiTiet);

            // Parse chiTietSanPhamList
            ObjectMapper objectMapper = new ObjectMapper();
            List<SanPhamRequest.ChiTietSanPhamDTO> chiTietList = objectMapper.readValue(
                    chiTietSanPhamListJson,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, SanPhamRequest.ChiTietSanPhamDTO.class)
            );
            requestSP.setChiTietSanPhamList(chiTietList);
            logger.debug("[DEBUG] Parsed chiTietSanPhamList: {}", chiTietList);

            // Tạo SanPham
            SanPham createdSanPham = sanPhamService.createWithDetails(requestSP);
            logger.info("[INFO] Created SanPham with id: {}", createdSanPham.getId());

            // Liên kết ảnh với chi tiết sản phẩm
            for (SanPhamRequest.ChiTietSanPhamDTO dto : chiTietList) {
                Integer idLoaiBia = dto.getIdLoaiBia();
                logger.debug("[DEBUG] Processing chiTietSanPham with idLoaiBia: {}", idLoaiBia);

                // Tìm tất cả chiTietSanPham theo idSanPham và idLoaiBia
                List<ChiTietSanPham> chiTietListForLoaiBia = chiTietSanPhamRepository.findAllByIdSanPhamIdAndIdLoaiBiaId(
                        createdSanPham.getId(), idLoaiBia
                );
                if (chiTietListForLoaiBia.isEmpty()) {
                    logger.error("[ERROR] No ChiTietSanPham found for idSanPham: {} and idLoaiBia: {}", createdSanPham.getId(), idLoaiBia);
                    throw new RuntimeException("No ChiTietSanPham found for idSanPham: " + createdSanPham.getId() + " and idLoaiBia: " + idLoaiBia);
                }
                logger.debug("[DEBUG] Found {} chiTietSanPham for idLoaiBia: {}", chiTietListForLoaiBia.size(), idLoaiBia);

                List<Integer> imageIds = dto.getImageIds();
                if (imageIds != null && !imageIds.isEmpty()) {
                    if (imageIds.size() > 3) {
                        logger.error("[ERROR] Maximum 3 images allowed per ChiTietSanPham, received: {}", imageIds.size());
                        throw new RuntimeException("Maximum 3 images allowed per ChiTietSanPham");
                    }
                    for (ChiTietSanPham chiTiet : chiTietListForLoaiBia) {
                        for (Integer imageId : imageIds) {
                            chiTietSanPhamService.linkImageToChiTietSanPham(chiTiet.getId(), imageId);
                            logger.debug("[DEBUG] Linked imageId: {} to chiTietSanPhamId: {}", imageId, chiTiet.getId());
                        }
                    }
                } else {
                    logger.debug("[DEBUG] No imageIds provided for idLoaiBia: {}", idLoaiBia);
                }
            }

            logger.info("[INFO] Successfully created SanPham and linked images");
            return ResponseEntity.ok(createdSanPham);
        } catch (Exception e) {
            logger.error("[ERROR] Error creating SanPham with details: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping(value = "/create-with-imagekit", consumes = "application/json")
    public ResponseEntity<SanPham> createWithImageKit(@RequestBody SanPhamRequest request) throws Exception {
        logger.info("[INFO] Creating SanPham with ImageKit: tenSanPham={}", request.getTenSanPham());
        
        try {
            // Tạo SanPham
            SanPham createdSanPham = sanPhamService.createWithDetails(request);
            logger.info("[INFO] Created SanPham with id: {}", createdSanPham.getId());

            // Xử lý ảnh từ ImageKit URLs
            for (SanPhamRequest.ChiTietSanPhamDTO dto : request.getChiTietSanPhamList()) {
                Integer idLoaiBia = dto.getIdLoaiBia();
                logger.debug("[DEBUG] Processing chiTietSanPham with idLoaiBia: {}", idLoaiBia);

                // Tìm tất cả chiTietSanPham theo idSanPham và idLoaiBia
                List<ChiTietSanPham> chiTietListForLoaiBia = chiTietSanPhamRepository.findAllByIdSanPhamIdAndIdLoaiBiaId(
                        createdSanPham.getId(), idLoaiBia
                );
                if (chiTietListForLoaiBia.isEmpty()) {
                    logger.error("[ERROR] No ChiTietSanPham found for idSanPham: {} and idLoaiBia: {}", createdSanPham.getId(), idLoaiBia);
                    throw new RuntimeException("No ChiTietSanPham found for idSanPham: " + createdSanPham.getId() + " and idLoaiBia: " + idLoaiBia);
                }
                logger.debug("[DEBUG] Found {} chiTietSanPham for idLoaiBia: {}", chiTietListForLoaiBia.size(), idLoaiBia);

                // Xử lý ImageKit URLs
                List<String> imageUrls = dto.getImageUrls();
                if (imageUrls != null && !imageUrls.isEmpty()) {
                    if (imageUrls.size() > 3) {
                        logger.error("[ERROR] Maximum 3 images allowed per ChiTietSanPham, received: {}", imageUrls.size());
                        throw new RuntimeException("Maximum 3 images allowed per ChiTietSanPham");
                    }
                    
                    for (ChiTietSanPham chiTiet : chiTietListForLoaiBia) {
                        for (String imageUrl : imageUrls) {
                            // Tạo AnhSanPham với URL từ ImageKit
                            AnhSanPham anhSanPham = new AnhSanPham();
                            anhSanPham.setUrl(imageUrl);
                            AnhSanPham savedAnh = anhSanPhamService.save(anhSanPham);
                            
                            // Liên kết ảnh với chi tiết sản phẩm
                            chiTietSanPhamService.linkImageToChiTietSanPham(chiTiet.getId(), savedAnh.getId());
                            logger.debug("[DEBUG] Linked ImageKit imageUrl: {} to chiTietSanPhamId: {}", imageUrl, chiTiet.getId());
                        }
                    }
                } else {
                    logger.debug("[DEBUG] No imageUrls provided for idLoaiBia: {}", idLoaiBia);
                }
            }

            logger.info("[INFO] Successfully created SanPham and linked ImageKit images");
            return ResponseEntity.ok(createdSanPham);
        } catch (Exception e) {
            logger.error("[ERROR] Error creating SanPham with ImageKit: {}", e.getMessage(), e);
            throw e;
        }
    }
}