package org.example.datnbbook.service;

import org.example.datnbbook.model.PhieuGiamGia;
import org.example.datnbbook.repository.PhieuGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhieuGiamGiaService {
    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    public List<PhieuGiamGia> getAll() {
        return phieuGiamGiaRepository.getAllActive();
    }



    // Lấy phiếu giảm giá theo ID
    public Optional<PhieuGiamGia> findById(Long id) {
        return phieuGiamGiaRepository.findById(id);
    }

    // Thêm hoặc cập nhật phiếu giảm giá
    public PhieuGiamGia save(PhieuGiamGia phieuGiamGia) {
        return phieuGiamGiaRepository.save(phieuGiamGia);
    }

    // Kiểm tra xem phiếu giảm giá có tồn tại không
    public boolean existsById(Long id) {
        return phieuGiamGiaRepository.existsById(id);
    }

    // Xóa phiếu giảm giá
    public void deleteById(Long id) {
        PhieuGiamGia phieu = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy!"));
        phieu.setDeleted(true); // Nếu deleted là Integer thì đổi thành 1
        phieuGiamGiaRepository.save(phieu);
    }

}
