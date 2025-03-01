package org.example.datnbbook.service;

import org.example.datnbbook.model.LichSuHoaDon;
import org.example.datnbbook.repository.LichSuHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
