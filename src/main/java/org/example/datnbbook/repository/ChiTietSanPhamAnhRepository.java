package org.example.datnbbook.repository;

import org.example.datnbbook.model.ChiTietSanPhamAnh;
import org.example.datnbbook.model.ChiTietSanPhamAnhId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChiTietSanPhamAnhRepository extends JpaRepository<ChiTietSanPhamAnh, ChiTietSanPhamAnhId> {
    // Tìm tất cả bản ghi theo chiTietSanPhamId
    List<ChiTietSanPhamAnh> findByChiTietSanPhamId(Integer chiTietSanPhamId);
    
}
