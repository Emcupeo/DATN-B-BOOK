package org.example.datnbbook.repository;

import org.example.datnbbook.model.BoSach;
import org.example.datnbbook.model.ChiTietSanPhamAnh;
import org.example.datnbbook.model.ChiTietSanPhamAnhId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoSachRepository extends JpaRepository<BoSach, Integer> {
    @Query("SELECT sp FROM BoSach sp WHERE sp.deleted = false AND " +
            "(LOWER(sp.tenBoSach) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(sp.maBoSach) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<BoSach> search(String keyword);

    @Query(value = "SELECT 'BS' + RIGHT('-000' + CONVERT(NVARCHAR(5), NEXT VALUE FOR [dbo].[BSSeq]), 5)", nativeQuery = true)
    String getNextSequenceValue();


    List<BoSach> findAllByDeletedFalseOrderByIdDesc();
}
