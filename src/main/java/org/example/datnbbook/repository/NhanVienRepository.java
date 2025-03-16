package org.example.datnbbook.repository;

import org.example.datnbbook.model.KhachHang;
import org.example.datnbbook.model.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
    @Query("SELECT nv FROM NhanVien nv WHERE nv.deleted = false AND " +
            "(LOWER(nv.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(nv.maNhanVien) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(nv.soDienThoai) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(nv.soCanCuoc) LIKE LOWER(CONCAT('%', :keyword, '%')))")

    List<NhanVien> search(String keyword);

    List<NhanVien> findByDeletedFalse();
//


//
}

