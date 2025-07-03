package org.example.datnbbook.controller;

import org.example.datnbbook.model.AnhSanPham;
import org.example.datnbbook.model.LoaiBia;
import org.example.datnbbook.repository.AnhSanPhamRepository;
import org.example.datnbbook.service.AnhSanPhamService;
import org.example.datnbbook.service.ImgBBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api/anh-san-pham")
public class AnhSanPhamController {
    private static final Logger logger = LoggerFactory.getLogger(AnhSanPhamController.class);

    @Autowired
    private AnhSanPhamService anhSanPhamService;

    @Autowired
    private ImgBBService imgBBService;
    @Autowired
    private AnhSanPhamRepository anhSanPhamRepository;

    @GetMapping("/all")
    public List<AnhSanPham> getAll(){
        return anhSanPhamRepository.findAll();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AnhSanPham> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "idLoaiBia", required = false) Integer idLoaiBia) {
        try {
            // Upload lên ImgBB, lấy url
            String url = imgBBService.uploadToImgBB(file);
            // Lưu vào bảng AnhSanPham
            AnhSanPham anh = new AnhSanPham();
            anh.setUrl(url);
            if (idLoaiBia != null) {
                anh.setIdLoaiBia(new LoaiBia(idLoaiBia));
            }
            AnhSanPham saved = anhSanPhamService.save(anh);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/chi-tiet-san-pham/{idChiTietSanPham}")
    public ResponseEntity<List<AnhSanPham>> getImagesByChiTietSanPhamId(@PathVariable Integer idChiTietSanPham) {
        logger.info("[INFO] Fetching images for idChiTietSanPham: {}", idChiTietSanPham);
        try {
            List<AnhSanPham> images = anhSanPhamService.getImagesByChiTietSanPhamId(idChiTietSanPham);
            logger.debug("[DEBUG] Found {} images", images.size());
            return ResponseEntity.ok(images);
        } catch (RuntimeException e) {
            logger.error("[ERROR] Failed to fetch images: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}