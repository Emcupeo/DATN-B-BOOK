package org.example.datnbbook.repository;

import org.example.datnbbook.model.PhieuGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Long> {
    @Query("SELECT p FROM PhieuGiamGia p WHERE p.deleted = false ")
    List<PhieuGiamGia> getAllActive();
}
