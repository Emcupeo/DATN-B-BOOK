package org.example.datnbbook.repository;

import org.example.datnbbook.model.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    List<HoaDon> findByTrangThai(String trangThai);
    List<HoaDon> findAllByOrderByCreatedAtDesc();
    long countByTrangThai(String trangThai);
    
    // Tìm hóa đơn theo mã hóa đơn và số điện thoại người nhận
    @Query("SELECT h FROM HoaDon h WHERE h.maHoaDon = :orderCode AND h.soDienThoaiNguoiNhan = :phoneNumber")
    Optional<HoaDon> findByMaHoaDonAndSoDienThoaiNguoiNhan(@Param("orderCode") String orderCode, @Param("phoneNumber") String phoneNumber);
}
