package org.example.datnbbook.repository;

import org.example.datnbbook.model.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {
    Optional<KhachHang> findByMaKhachHang(String maKhachHang);

    List<KhachHang> findByDeletedFalse();

    boolean existsByMaKhachHang(String maKhachHang);

    boolean existsBySoDienThoai(String soDienThoai);

    boolean existsByEmail(String email);

    @Query("SELECT k FROM KhachHang k JOIN k.danhSachDiaChi d WHERE LOWER(d.tinhThanh) LIKE LOWER(CONCAT('%', :tinhThanh, '%'))")
    List<KhachHang> findByTinhThanhContainingIgnoreCase(@Param("tinhThanh") String tinhThanh);

    @Query("SELECT k FROM KhachHang k JOIN k.danhSachDiaChi d WHERE LOWER(d.quanHuyen) LIKE LOWER(CONCAT('%', :quanHuyen, '%'))")
    List<KhachHang> findByQuanHuyenContainingIgnoreCase(@Param("quanHuyen") String quanHuyen);

    @Query("SELECT k FROM KhachHang k JOIN k.danhSachDiaChi d WHERE LOWER(d.xaPhuong) LIKE LOWER(CONCAT('%', :xaPhuong, '%'))")
    List<KhachHang> findByXaPhuongContainingIgnoreCase(@Param("xaPhuong") String xaPhuong);

    @Query("SELECT k FROM KhachHang k WHERE k.deleted = false AND " +
            "(LOWER(k.maKhachHang) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(k.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(k.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(k.soDienThoai) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<KhachHang> search(String keyword);
}
