package org.example.datnbbook.repository;

import org.example.datnbbook.model.DotGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DotGiamGiaRepository extends JpaRepository<DotGiamGia, Integer> {
    @Query("SELECT d FROM DotGiamGia d WHERE d.deleted = false")
    List<DotGiamGia> findAllActive();

    Optional<DotGiamGia> findByMaDotGiamGia(String maDotGiamGia);

    @Query("SELECT dgg FROM DotGiamGia dgg " +
            "JOIN FETCH dgg.dotGiamGiaChiTiets dggct " +
            "JOIN FETCH dggct.idChiTietSanPham ctsp " +
            "WHERE dgg.deleted = false")
    List<DotGiamGia> findAllWithDetails();

    // Tạm thời bỏ qua method này vì DotGiamGia không có mối quan hệ với PhieuGiamGiaKhachHang
    // Voucher cá nhân sẽ được xử lý trong PhieuGiamGiaRepository
    // @Query("SELECT DISTINCT dgg FROM DotGiamGia dgg " +
    //         "JOIN dgg.phieuGiamGiaKhachHangs pggkh " +
    //         "WHERE pggkh.idKhachHang.id = :customerId " +
    //         "AND pggkh.deleted = false " +
    //         "AND dgg.deleted = false")
    // List<DotGiamGia> findPersonalVouchersByCustomerId(@Param("customerId") Long customerId);
}