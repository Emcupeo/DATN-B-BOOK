package org.example.datnbbook.repository;

import org.example.datnbbook.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    @Query("SELECT sp FROM SanPham sp WHERE sp.deleted = false AND " +
            "(LOWER(sp.tenSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(sp.maSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(sp.moTa) LIKE LOWER(CONCAT('%', :keyword, '%')))")

    List<SanPham> search(String keyword);

    @Query(value = "SELECT 'SP' + RIGHT('-000' + CONVERT(NVARCHAR(5), NEXT VALUE FOR [dbo].[SPSeq]), 5)", nativeQuery = true)
    String getNextSequenceValue();

    List<SanPham> findByDeletedFalse();
}
