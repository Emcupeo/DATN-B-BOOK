package org.example.datnbbook.repository;

import org.example.datnbbook.model.HinhThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HinhThucThanhToanRepository extends JpaRepository<HinhThucThanhToan, Integer> {
    List<HinhThucThanhToan> findByPhuongThucThanhToanId(Integer phuongThucThanhToanId);
}
