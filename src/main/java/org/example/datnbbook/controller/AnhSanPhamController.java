package org.example.datnbbook.controller;

import org.example.datnbbook.model.AnhSanPham;
import org.example.datnbbook.model.LoaiBia;
import org.example.datnbbook.repository.LoaiBiaRepository;
import org.example.datnbbook.service.AnhSanPhamService;
import org.example.datnbbook.service.ImgBBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api/anh-san-pham")
public class AnhSanPhamController {
    private static final Logger logger = LoggerFactory.getLogger(AnhSanPhamController.class);

    @Autowired
    private ImgBBService imgBBService;
    @Autowired
    private AnhSanPhamService anhSanPhamService;
    @Autowired
    private LoaiBiaRepository loaiBiaRepository;

    @GetMapping("/by-chi-tiet-san-pham/{chiTietSanPhamId}")
    public ResponseEntity<List<AnhSanPham>> getImagesByChiTietSanPhamId(@PathVariable Integer chiTietSanPhamId) {
        List<AnhSanPham> images = anhSanPhamService.getImagesByChiTietSanPhamId(chiTietSanPhamId);
        return ResponseEntity.ok(images);
    }

    @GetMapping("/by-loai-bia/{idLoaiBia}")
    public ResponseEntity<List<AnhSanPham>> getByLoaiBia(@PathVariable Integer idLoaiBia) {
        List<AnhSanPham> images = anhSanPhamService.findByIdLoaiBia(idLoaiBia);
        return ResponseEntity.ok(images);
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<AnhSanPham> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("idLoaiBia") Integer idLoaiBia
    ) throws IOException {
        // Kiểm tra file
        if (!file.getContentType().startsWith("image/")) {
            throw new IOException("File is not an image");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IOException("File size exceeds 5MB");
        }

        // Upload lên ImgBB
        String url = imgBBService.uploadImage(file);

        // Kiểm tra URL trong DB
        AnhSanPham existingByUrl = anhSanPhamService.findByUrlAndIdLoaiBia(url, idLoaiBia);
        if (existingByUrl != null) {
            logger.info("Using existing AnhSanPham with ID: {} for URL: {}", existingByUrl.getId(), url);
            return ResponseEntity.ok(existingByUrl);
        }

        // Tìm ảnh với URL này nhưng khác idLoaiBia
        AnhSanPham existingByUrlDifferentLoaiBia = anhSanPhamService.findByUrl(url);
        if (existingByUrlDifferentLoaiBia != null) {
            // Sử dụng lại URL nhưng tạo bản ghi mới với idLoaiBia hiện tại
            AnhSanPham anhSanPham = new AnhSanPham();
            anhSanPham.setUrl(url);
            LoaiBia loaiBia = loaiBiaRepository.findById(idLoaiBia)
                    .orElseThrow(() -> new RuntimeException("LoaiBia not found for id: " + idLoaiBia));
            anhSanPham.setIdLoaiBia(loaiBia);
            AnhSanPham savedAnh = anhSanPhamService.save(anhSanPham);
            logger.info("Created new AnhSanPham with ID: {} for existing URL: {} and idLoaiBia: {}", savedAnh.getId(), url, idLoaiBia);
            return ResponseEntity.ok(savedAnh);
        }

        // Lưu ảnh mới
        AnhSanPham anhSanPham = new AnhSanPham();
        anhSanPham.setUrl(url);
        LoaiBia loaiBia = loaiBiaRepository.findById(idLoaiBia)
                .orElseThrow(() -> new RuntimeException("LoaiBia not found for id: " + idLoaiBia));
        anhSanPham.setIdLoaiBia(loaiBia);
        AnhSanPham savedAnh = anhSanPhamService.save(anhSanPham);
        logger.info("Created new AnhSanPham with ID: {} for URL: {} and idLoaiBia: {}", savedAnh.getId(), url, idLoaiBia);
        return ResponseEntity.ok(savedAnh);
    }
}