package org.example.datnbbook.repository;

import org.example.datnbbook.model.ChiTietSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Integer> {
    Optional<ChiTietSanPham> findByMaChiTietSanPham(String maChiTietSanPham);

    List<ChiTietSanPham> findAllByDeletedFalseOrderByIdDesc();

    @Query("SELECT c FROM ChiTietSanPham c WHERE " +
            "c.deleted = false AND " +
            "(LOWER(c.maChiTietSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.tenChiTietSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.moTa) LIKE LOWER(CONCAT('%', :keyword, '%')))")

    List<ChiTietSanPham> search(String keyword);

    boolean existsByMaChiTietSanPham(String maChiTietSanPham);

    @Query(value = "SELECT 'CTSP' + RIGHT('-00' + CONVERT(NVARCHAR(5), NEXT VALUE FOR [dbo].[CTSPSeq]), 5)", nativeQuery = true)
    String getNextCTSPSequenceValue();

    List<ChiTietSanPham> findByIdSanPham_Id(Integer idSanPham);
}
