package org.example.datnbbook.repository;

import org.example.datnbbook.model.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> {
//    @Query("SELECT COALESCE(SUM(hdct.soLuong), 0) FROM HoaDonChiTiet hdct WHERE hdct.idHoaDon.id = :hoaDonId")
//    Integer getTotalProductQuantityByHoaDonId(@Param("hoaDonId") Integer hoaDonId);
Optional<HoaDonChiTiet> findByHoaDonIdAndChiTietSanPhamId(Integer hoaDonId, Integer chiTietSanPhamId);

    // Lấy sách bán chạy nhất (chỉ sản phẩm lẻ, không bao gồm bộ sách)
    @Query(value = "SELECT TOP (?1) hct.id_chi_tiet_san_pham, SUM(hct.so_luong) as totalSold " +
           "FROM hoa_don_chi_tiet hct " +
           "JOIN hoa_don hd ON hct.id_hoa_don = hd.id " +
           "WHERE hd.trang_thai = 'Hoàn thành' " +
           "AND hct.id_chi_tiet_san_pham IS NOT NULL " +
           "AND hct.id_bo_sach IS NULL " +
           "GROUP BY hct.id_chi_tiet_san_pham " +
           "ORDER BY totalSold DESC", nativeQuery = true)
    List<Object[]> findTopSellingProducts(int limit);

    // Lấy bộ sách bán chạy nhất
    @Query(value = "SELECT TOP (?1) hct.id_bo_sach, SUM(hct.so_luong) as totalSold " +
           "FROM hoa_don_chi_tiet hct " +
           "JOIN hoa_don hd ON hct.id_hoa_don = hd.id " +
           "WHERE hd.trang_thai = 'Hoàn thành' " +
           "AND hct.id_bo_sach IS NOT NULL " +
           "AND hct.id_chi_tiet_san_pham IS NULL " +
           "GROUP BY hct.id_bo_sach " +
           "ORDER BY totalSold DESC", nativeQuery = true)
    List<Object[]> findTopSellingBookSets(int limit);
}
