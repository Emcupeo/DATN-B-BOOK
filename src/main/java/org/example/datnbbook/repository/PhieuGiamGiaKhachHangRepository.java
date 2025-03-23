package org.example.datnbbook.repository;

import org.example.datnbbook.model.PhieuGiamGiaKhachHang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhieuGiamGiaKhachHangRepository extends JpaRepository<PhieuGiamGiaKhachHang, Integer> {

    @Query(value = "SELECT 'PGGKH-' + RIGHT('00000' + CONVERT(NVARCHAR, NEXT VALUE FOR [dbo].[PGGKHSeq]), 5)", nativeQuery = true)
    String getNextMaPhieuGiamGiaKhachHang();

    @Query("SELECT p FROM PhieuGiamGiaKhachHang p WHERE p.deleted = false")
    List<PhieuGiamGiaKhachHang> findAllActive();

    // Lấy theo ID phiếu giảm giá
    @Query("SELECT p FROM PhieuGiamGiaKhachHang p WHERE p.phieuGiamGia.id = :phieuId AND p.deleted = false")
    List<PhieuGiamGiaKhachHang> findByPhieuGiamGiaId(Long phieuId);

    // Lấy theo khách hàng
    @Query("SELECT p FROM PhieuGiamGiaKhachHang p WHERE p.khachHang.id = :khachHangId AND p.deleted = false")
    List<PhieuGiamGiaKhachHang> findByKhachHangId(Long khachHangId);

    List<PhieuGiamGiaKhachHang> findByPhieuGiamGiaIdAndDeletedFalse(Long phieuId);

}
