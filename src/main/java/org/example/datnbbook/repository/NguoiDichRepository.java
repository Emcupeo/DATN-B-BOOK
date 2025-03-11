package org.example.datnbbook.repository;

import org.example.datnbbook.model.NguoiDich;
import org.example.datnbbook.model.NguoiDich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NguoiDichRepository extends JpaRepository<NguoiDich, Integer> {
    @Query("SELECT sp FROM NguoiDich sp WHERE sp.deleted = false AND " +
            "(LOWER(sp.tenNguoiDich) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(sp.maNguoiDich) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<NguoiDich> search(String keyword);

    @Query(value = "SELECT 'ND' + RIGHT('-000' + CONVERT(NVARCHAR(5), NEXT VALUE FOR [dbo].[NDSeq]), 5)", nativeQuery = true)
    String getNextSequenceValue();

    List<NguoiDich> findByDeletedFalse();
}
