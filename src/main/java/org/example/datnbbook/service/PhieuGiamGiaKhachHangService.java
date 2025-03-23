package org.example.datnbbook.service;

import org.example.datnbbook.model.PhieuGiamGiaKhachHang;
import org.example.datnbbook.repository.PhieuGiamGiaKhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhieuGiamGiaKhachHangService {
    @Autowired
    private PhieuGiamGiaKhachHangRepository repository;

    public PhieuGiamGiaKhachHang save(PhieuGiamGiaKhachHang entity) {
        if (entity.getMaPhieuGiamGiaKhachHang() == null || entity.getMaPhieuGiamGiaKhachHang().isEmpty()) {
            String nextMa = repository.getNextMaPhieuGiamGiaKhachHang();
            entity.setMaPhieuGiamGiaKhachHang(nextMa);
        }
        return repository.save(entity);
    }

    public List<PhieuGiamGiaKhachHang> findByPhieuGiamGiaId(Long phieuId) {
        return repository.findByPhieuGiamGiaIdAndDeletedFalse(phieuId);
    }

    public void deleteByPhieuGiamGiaId(Long phieuGiamGiaId) {
        List<PhieuGiamGiaKhachHang> danhSach = repository.findByPhieuGiamGiaId(phieuGiamGiaId);
        danhSach.forEach(item -> repository.deleteById(item.getId()));

    }

}
