package org.example.datnbbook.service;

import org.example.datnbbook.model.HoaDon;
import org.example.datnbbook.model.LichSuHoaDon;
import org.example.datnbbook.repository.LichSuHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LichSuHoaDonService {
    @Autowired
    private LichSuHoaDonRepository lichSuHoaDonRepository;
    public List<LichSuHoaDon> getAllLichSuHoaDon() {
        return lichSuHoaDonRepository.findAll();
    }

    public LichSuHoaDon getLichSuHoaDonById(Integer id) {
        return lichSuHoaDonRepository.findByIdWithHoaDon(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch sử hóa đơn!"));
    }

    @Transactional
    public void addLichSuHoaDon(HoaDon hoaDon, String trangThaiCu, String trangThaiMoi) {
        LichSuHoaDon lichSu = new LichSuHoaDon();
        lichSu.setHoaDon(hoaDon);
        lichSu.setTrangThaiCu(trangThaiCu);
        lichSu.setTrangThaiMoi(trangThaiMoi);
        lichSu.setCreatedAt(Instant.now());
        lichSuHoaDonRepository.save(lichSu);
    }
}
