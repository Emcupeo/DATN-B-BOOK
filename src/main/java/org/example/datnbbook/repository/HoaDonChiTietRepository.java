package org.example.datnbbook.repository;

import org.example.datnbbook.model.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> {
//    @Query("SELECT COALESCE(SUM(hdct.soLuong), 0) FROM HoaDonChiTiet hdct WHERE hdct.idHoaDon.id = :hoaDonId")
//    Integer getTotalProductQuantityByHoaDonId(@Param("hoaDonId") Integer hoaDonId);
}
