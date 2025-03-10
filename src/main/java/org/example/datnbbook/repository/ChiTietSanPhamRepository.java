package org.example.datnbbook.repository;

import org.example.datnbbook.model.ChiTietSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Integer> {
    Optional<ChiTietSanPham> findByMaChiTietSanPham(String maChiTietSanPham);

    List<ChiTietSanPham> findByDeletedFalse();

    @Query("SELECT c FROM ChiTietSanPham c WHERE " +
            "c.deleted = false AND " +
            "(LOWER(c.maChiTietSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.tenChiTietSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.moTa) LIKE LOWER(CONCAT('%', :keyword, '%')))")

    List<ChiTietSanPham> search(String keyword);

    boolean existsByMaChiTietSanPham(String maChiTietSanPham);
}
