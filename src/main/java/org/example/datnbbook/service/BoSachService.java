package org.example.datnbbook.service;

import org.example.datnbbook.dto.BoSachDTO;
import org.example.datnbbook.model.BoSach;
import org.example.datnbbook.model.BoSachChiTiet;
import org.example.datnbbook.model.BoSachChiTietId;
import org.example.datnbbook.model.ChiTietSanPham;
import org.example.datnbbook.repository.BoSachChiTietRepository;
import org.example.datnbbook.repository.BoSachRepository;
import org.example.datnbbook.repository.ChiTietSanPhamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@Transactional
public class BoSachService {
    private static final Logger logger = LoggerFactory.getLogger(BoSachService.class);

    @Autowired
    private BoSachRepository boSachRepository;
    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;
    @Autowired
    private BoSachChiTietRepository boSachChiTietRepository;

    public List<BoSach> getAll() {
        logger.info("[INFO] Fetching all BoSach");
        return boSachRepository.findAllByDeletedFalseOrderByIdDesc();
    }

    public BoSach getById(Integer id) {
        logger.info("[INFO] Fetching BoSach with id: {}", id);
        return boSachRepository.findById(id)
                .filter(bs -> !bs.getDeleted())
                .orElseThrow(() -> new RuntimeException("Bộ sách không tồn tại với id: " + id));
    }

    @Transactional
    public BoSach create(BoSachDTO dto) {
        logger.info("[INFO] Creating BoSach: {}", dto.getTenBoSach());
        BoSach boSach = new BoSach();
        if (dto.getMaBoSach() == null || dto.getMaBoSach().isEmpty()) {
            String nextMaBoSach = boSachRepository.getNextSequenceValue();
            boSach.setMaBoSach(nextMaBoSach);
        } else {
            boSach.setMaBoSach(dto.getMaBoSach());
        }

        boSach.setTenBoSach(dto.getTenBoSach());
        boSach.setGiaTien(BigDecimal.valueOf(dto.getGiaTien()));
        boSach.setMoTa(dto.getMoTa());
        boSach.setSoLuong(dto.getSoLuong());
        boSach.setUrl(dto.getUrl());
        boSach.setDeleted(false);
        boSach.setCreatedBy("Admin");
        boSach.setUpdatedBy("Admin");

        Instant now = LocalDateTime.now().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
        boSach.setCreatedAt(now);
        boSach.setUpdatedAt(now);

        BoSach savedBoSach = boSachRepository.save(boSach);

        List<BoSachDTO.BoSachChiTietDTO> chiTietList = dto.getBoSachChiTiets();
        if (chiTietList != null && !chiTietList.isEmpty()) {
            for (BoSachDTO.BoSachChiTietDTO chiTietDTO : chiTietList) {
                BoSachChiTiet boSachChiTiet = new BoSachChiTiet();
                BoSachChiTietId id = new BoSachChiTietId();
                id.setIdBoSach(savedBoSach.getId());
                id.setIdChiTietSanPham(chiTietDTO.getIdChiTietSanPham());
                boSachChiTiet.setId(id);

                boSachChiTiet.setIdBoSach(savedBoSach);
                ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(chiTietDTO.getIdChiTietSanPham())
                        .orElseThrow(() -> new RuntimeException("ChiTietSanPham không tồn tại với id: " + chiTietDTO.getIdChiTietSanPham()));
                boSachChiTiet.setIdChiTietSanPham(chiTietSanPham);

                boSachChiTiet.setSoLuong(chiTietDTO.getSoLuong());
                boSachChiTiet.setDeleted(false);
                boSachChiTiet.setCreatedBy("Admin");
                boSachChiTiet.setUpdatedBy("Admin");
                boSachChiTiet.setCreatedAt(now);
                boSachChiTiet.setUpdatedAt(now);

                boSachChiTietRepository.save(boSachChiTiet);
            }
        } else {
            throw new RuntimeException("Danh sách BoSachChiTiet không được để trống");
        }

        return savedBoSach;
    }

    public void delete(Integer id) {
        logger.info("[INFO] Deleting BoSach with id: {}", id);
        BoSach boSach = boSachRepository.findById(id)
                .filter(bs -> !bs.getDeleted())
                .orElseThrow(() -> new RuntimeException("Bộ sách không tồn tại với id: " + id));
        boSach.setDeleted(true);
        boSachRepository.save(boSach);
    }

    public List<BoSach> search(String keyword) {
        logger.info("[INFO] Searching BoSach with keyword: {}", keyword);
        return boSachRepository.search(keyword);
    }

    public List<BoSachChiTiet> getBoSachChiTietByBoSachId(Integer idBoSach) {
        logger.info("[INFO] Fetching BoSachChiTiet for idBoSach: {}", idBoSach);
        BoSach boSach = boSachRepository.findById(idBoSach)
                .filter(bs -> !bs.getDeleted())
                .orElseThrow(() -> new RuntimeException("Bộ sách không tồn tại với id: " + idBoSach));
        List<BoSachChiTiet> chiTietList = boSachChiTietRepository.findByIdBoSachId(idBoSach);
        logger.debug("[DEBUG] Found {} BoSachChiTiet entries for idBoSach: {}", chiTietList.size(), idBoSach);
        return chiTietList;
    }

    @Transactional
    public BoSachChiTiet updateBoSachChiTiet(Integer idBoSach, Integer idChiTietSanPham, BoSachChiTiet updatedData) {
        logger.info("[INFO] Updating BoSachChiTiet for idBoSach: {} and idChiTietSanPham: {}", idBoSach, idChiTietSanPham);
        BoSachChiTiet existingChiTiet = boSachChiTietRepository.findByIdBoSachIdAndIdChiTietSanPhamId(idBoSach, idChiTietSanPham);
        if (existingChiTiet == null || existingChiTiet.getDeleted()) {
            throw new RuntimeException("BoSachChiTiet không tồn tại với idBoSach: " + idBoSach + " và idChiTietSanPham: " + idChiTietSanPham);
        }

        existingChiTiet.setSoLuong(updatedData.getSoLuong());
        existingChiTiet.setUpdatedBy("Admin");
        existingChiTiet.setUpdatedAt(LocalDateTime.now().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant());

        return boSachChiTietRepository.save(existingChiTiet);
    }

    @Transactional
    public void deleteBoSachChiTiet(Integer idBoSach, Integer idChiTietSanPham) {
        logger.info("[INFO] Deleting BoSachChiTiet for idBoSach: {} and idChiTietSanPham: {}", idBoSach, idChiTietSanPham);
        BoSachChiTiet chiTiet = boSachChiTietRepository.findByIdBoSachIdAndIdChiTietSanPhamId(idBoSach, idChiTietSanPham);
        if (chiTiet == null || chiTiet.getDeleted()) {
            throw new RuntimeException("BoSachChiTiet không tồn tại với idBoSach: " + idBoSach + " và idChiTietSanPham: " + idChiTietSanPham);
        }
        chiTiet.setDeleted(true);
        chiTiet.setUpdatedBy("Admin");
        chiTiet.setUpdatedAt(LocalDateTime.now().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant());
        boSachChiTietRepository.save(chiTiet);
    }
}