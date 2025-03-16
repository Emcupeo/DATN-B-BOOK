package org.example.datnbbook.service;

import org.example.datnbbook.model.PhieuGiamGia;
import org.example.datnbbook.repository.PhieuGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhieuGiamGiaService {
    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    public Page<PhieuGiamGia> getAll(Pageable pageable) {
        return phieuGiamGiaRepository.findAllByDeletedFalse(pageable);
    }




    // Lấy phiếu giảm giá theo ID
    public Optional<PhieuGiamGia> findById(Long id) {
        return phieuGiamGiaRepository.findById(id);
    }

    // Thêm hoặc cập nhật phiếu giảm giá
    public PhieuGiamGia save(PhieuGiamGia phieuGiamGia) {
        if (phieuGiamGia.getMaPhieuGiamGia() == null || phieuGiamGia.getMaPhieuGiamGia().isEmpty()) {
            String nextPhieuGiamGia = phieuGiamGiaRepository.getNextSequenceValue();
            phieuGiamGia.setMaPhieuGiamGia(nextPhieuGiamGia);
        }
        phieuGiamGia.setDeleted(false); // Đảm bảo sản phẩm mới không bị đánh dấu xóa
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
