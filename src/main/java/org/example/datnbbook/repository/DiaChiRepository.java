package org.example.datnbbook.repository;

import org.example.datnbbook.model.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi, Long> {

    List<DiaChi> findByKhachHangIdAndDeletedFalse(Long khachHangId);

    @Query("SELECT d FROM DiaChi d WHERE d.khachHang.id = :khachHangId AND d.macDinh = true AND d.deleted = false")
    DiaChi findDefaultAddress(Long khachHangId);
} 