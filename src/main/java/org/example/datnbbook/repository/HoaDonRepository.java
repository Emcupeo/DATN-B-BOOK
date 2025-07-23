package org.example.datnbbook.repository;

import org.example.datnbbook.model.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    List<HoaDon> findByTrangThai(String trangThai);
    List<HoaDon> findAllByOrderByCreatedAtDesc();
}
