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

    @Query("SELECT k FROM KhachHang k WHERE " +
            "LOWER(k.maKhachHang) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(k.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(k.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(k.soDienThoai) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<KhachHang> findByHoTenContainingIgnoreCaseOrEmailContainingIgnoreCaseOrSoDienThoaiContaining(
            @Param("keyword") String keyword);

    @Query("SELECT k FROM KhachHang k WHERE " +
            "LOWER(k.maKhachHang) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(k.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(k.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(k.soDienThoai) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<KhachHang> search(@Param("keyword") String keyword);
    
    // Tạo mã khách hàng tự động bằng cách đếm số bản ghi
    @Query(value = "SELECT 'KH' + RIGHT('00000' + CAST((SELECT ISNULL(MAX(CAST(SUBSTRING(ma_khach_hang, 3, 5) AS INT)), 0) + 1 FROM khach_hang WHERE ma_khach_hang LIKE 'KH%') AS VARCHAR(5)), 5)", nativeQuery = true)
    String getNextMaKhachHang();
}
