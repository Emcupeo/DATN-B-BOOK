package org.example.datnbbook.service;

import org.example.datnbbook.model.AnhSanPham;
import org.example.datnbbook.model.ChiTietSanPham;
import org.example.datnbbook.model.ChiTietSanPhamAnh;
import org.example.datnbbook.repository.AnhSanPhamRepository;
import org.example.datnbbook.repository.ChiTietSanPhamAnhRepository;
import org.example.datnbbook.repository.ChiTietSanPhamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnhSanPhamService {
    private static final Logger logger = LoggerFactory.getLogger(AnhSanPhamService.class);
    private static final String UPLOAD_DIR = "uploads/images/";

    @Autowired
    private AnhSanPhamRepository anhSanPhamRepository;

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Autowired
    private ChiTietSanPhamAnhRepository chiTietSanPhamAnhRepository;

    public Optional<AnhSanPham> findById(Integer id){
        return anhSanPhamRepository.findById(id);
    }

    public AnhSanPham uploadImage(MultipartFile file, Integer idChiTietSanPham) {
        logger.info("[INFO] Uploading image for idChiTietSanPham: {}", idChiTietSanPham);
        try {
            // Validate ChiTietSanPham
            ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(idChiTietSanPham)
                    .filter(ctsp -> !ctsp.getDeleted())
                    .orElseThrow(() -> new RuntimeException("ChiTietSanPham không tồn tại với id: " + idChiTietSanPham));

            // Validate file
            if (file.isEmpty()) {
                throw new RuntimeException("File ảnh không được để trống");
            }
            if (!file.getContentType().startsWith("image/")) {
                throw new RuntimeException("File phải là định dạng ảnh (jpg, png, ...)");
            }

            // Save file to disk
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            // Save AnhSanPham
            AnhSanPham anhSanPham = new AnhSanPham();
            anhSanPham.setUrl("/" + UPLOAD_DIR + fileName);
            AnhSanPham savedImage = anhSanPhamRepository.save(anhSanPham);
            logger.debug("[DEBUG] Saved image: {}", savedImage.getUrl());
            return savedImage;
        } catch (Exception e) {
            logger.error("[ERROR] Failed to upload image: {}", e.getMessage());
            throw new RuntimeException("Lỗi khi tải ảnh: " + e.getMessage());
        }
    }

    public List<AnhSanPham> getImagesByChiTietSanPhamId(Integer idChiTietSanPham) {
        logger.info("[INFO] Fetching images for idChiTietSanPham: {}", idChiTietSanPham);
        List<ChiTietSanPhamAnh> links = chiTietSanPhamAnhRepository.findByChiTietSanPhamId(idChiTietSanPham);
        List<Integer> anhIds = links.stream().map(link -> link.getId().getAnhId()).collect(Collectors.toList());
        List<AnhSanPham> images = anhSanPhamRepository.findAllById(anhIds);
        return images;
    }

    public List<AnhSanPham> uploadImages(List<MultipartFile> files, Integer idChiTietSanPham) {
        List<AnhSanPham> result = new ArrayList<>();
        for (MultipartFile file : files) {
            result.add(uploadImage(file, idChiTietSanPham));
        }
        return result;
    }

    public AnhSanPham save(AnhSanPham anh) {
        return anhSanPhamRepository.save(anh);
    }
}