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
    
    // Tạo mã nhân viên tự động bằng cách đếm số bản ghi
    @Query(value = "SELECT 'NV' + RIGHT('00000' + CAST((SELECT ISNULL(MAX(CAST(SUBSTRING(ma_nhan_vien, 3, 5) AS INT)), 0) + 1 FROM nhan_vien WHERE ma_nhan_vien LIKE 'NV%' AND LEN(ma_nhan_vien) = 7 AND ISNUMERIC(SUBSTRING(ma_nhan_vien, 3, 5)) = 1) AS VARCHAR(5)), 5)", nativeQuery = true)
    String getNextMaNhanVien();
    
    // Tìm mã nhân viên lớn nhất hiện tại
    @Query(value = "SELECT MAX(ma_nhan_vien) FROM nhan_vien WHERE ma_nhan_vien LIKE 'NV%' AND LEN(ma_nhan_vien) = 7 AND ISNUMERIC(SUBSTRING(ma_nhan_vien, 3, 5)) = 1", nativeQuery = true)
    String getMaxMaNhanVien();
    
    // Kiểm tra xem mã nhân viên đã tồn tại chưa
    boolean existsByMaNhanVien(String maNhanVien);
    
    // Tìm nhân viên theo mã
    List<NhanVien> findByMaNhanVien(String maNhanVien);
}

