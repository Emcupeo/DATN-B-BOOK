package org.example.datnbbook.repository;

import org.example.datnbbook.model.DotGiamGiaChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DotGiamGiaChiTietRepository extends JpaRepository<DotGiamGiaChiTiet, Integer> {
    @Query("SELECT CASE WHEN COUNT(d)>0 THEN true ELSE false END FROM DotGiamGiaChiTiet d WHERE d.deleted = false AND d.trangThai = true AND d.idChiTietSanPham.id = :ctspId")
    boolean existsActiveByChiTietSanPhamId(@Param("ctspId") Integer ctspId);

    @Query("SELECT d FROM DotGiamGiaChiTiet d WHERE d.deleted = false AND d.trangThai = true AND d.idChiTietSanPham.id = :ctspId")
    DotGiamGiaChiTiet findActiveByChiTietSanPhamId(@Param("ctspId") Integer ctspId);

    @Query("SELECT d FROM DotGiamGiaChiTiet d WHERE d.idDotGiamGia.id = :idDotGiamGia AND d.deleted = false")
    List<DotGiamGiaChiTiet> findByDotGiamGiaId(@Param("idDotGiamGia") Integer idDotGiamGia);
}