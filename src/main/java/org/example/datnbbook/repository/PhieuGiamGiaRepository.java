package org.example.datnbbook.repository;

import org.example.datnbbook.model.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Long> {
    @Query(value = "SELECT * FROM phieu_giam_gia p WHERE p.deleted = 0 " +
            "AND (:loaiApDung IS NULL OR p.loai_ap_dung = :loaiApDung) " +
            "AND (:loaiPhieu IS NULL OR p.loai_phieu = :loaiPhieu) " +
            "AND (:trangThai IS NULL OR p.trang_thai = :trangThai) " +
            "AND (:tinhTrang IS NULL OR p.tinh_trang = :tinhTrang) " +
            "AND (:fromDate IS NULL OR p.ngay_bat_dau >= :fromDate) " +
            "AND (:toDate IS NULL OR p.ngay_ket_thuc <= :toDate) " +
            "AND (:searchQuery IS NULL OR (" +
            "p.ma_phieu_giam_gia LIKE CONCAT('%', :searchQuery, '%') OR " +
            "p.ten_phieu_giam_gia LIKE CONCAT('%', :searchQuery, '%') OR " +
            "p.mo_ta LIKE CONCAT('%', :searchQuery, '%') OR " +
            "p.loai_ap_dung LIKE CONCAT('%', :searchQuery, '%') OR " +
            "p.loai_phieu LIKE CONCAT('%', :searchQuery, '%') OR " +
            "p.tinh_trang LIKE CONCAT('%', :searchQuery, '%') OR " +
            "CAST(p.so_luong AS CHAR) LIKE CONCAT('%', :searchQuery, '%') OR " +
            "CAST(p.gia_tri_giam AS CHAR) LIKE CONCAT('%', :searchQuery, '%') OR " +
            "CAST(p.so_phan_tram_giam AS CHAR) LIKE CONCAT('%', :searchQuery, '%') OR " +
            "CAST(p.gia_tri_don_hang_toi_thieu AS CHAR) LIKE CONCAT('%', :searchQuery, '%')" +
            "))",
            nativeQuery = true)
    Page<PhieuGiamGia> findByFilters(
            @Param("loaiApDung") String loaiApDung,
            @Param("loaiPhieu") String loaiPhieu,
            @Param("trangThai") Boolean trangThai,
            @Param("tinhTrang") String tinhTrang,
            @Param("searchQuery") String searchQuery,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable
    );

    @Query("SELECT p FROM PhieuGiamGia p WHERE p.deleted = false")
    Page<PhieuGiamGia> findAllByDeletedFalse(Pageable pageable);

    @Query(value = "SELECT 'PGG-' + RIGHT('00000' + CONVERT(NVARCHAR, NEXT VALUE FOR [dbo].[PGGSeq]), 5)", nativeQuery = true)
    String getNextSequenceValue();

    boolean existsByMaPhieuGiamGia(String maPhieuGiamGia);
}