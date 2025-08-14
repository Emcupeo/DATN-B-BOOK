package org.example.datnbbook.repository;

import org.example.datnbbook.model.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {
    
    Optional<NguoiDung> findByTenDangNhapAndDeletedFalse(String tenDangNhap);
    
    Optional<NguoiDung> findByTenDangNhapAndTrangThaiTrueAndDeletedFalse(String tenDangNhap);
    
    Optional<NguoiDung> findByEmailAndDeletedFalse(String email);
    
    List<NguoiDung> findAllByDeletedFalseOrderByCreatedAtDesc();
    
    List<NguoiDung> findAllByLoaiNguoiDungAndDeletedFalseOrderByCreatedAtDesc(NguoiDung.LoaiNguoiDung loaiNguoiDung);
    
    boolean existsByTenDangNhapAndDeletedFalse(String tenDangNhap);
    
    boolean existsByEmailAndDeletedFalse(String email);
    
    Optional<NguoiDung> findByKhachHangIdAndDeletedFalse(Long khachHangId);
    
    Optional<NguoiDung> findByNhanVienIdAndDeletedFalse(Integer nhanVienId);
    
    @Query(value = "SELECT 'ND' + RIGHT('00000' + CONVERT(NVARCHAR(5), NEXT VALUE FOR [dbo].[NDUserSeq]), 5)", nativeQuery = true)
    String getNextSequenceValue();
    
    @Query("SELECT nd FROM NguoiDung nd WHERE nd.deleted = false AND " +
           "(LOWER(nd.tenDangNhap) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(nd.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(nd.email) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<NguoiDung> search(@Param("keyword") String keyword);
}