package org.example.datnbbook.repository;

import org.example.datnbbook.model.NhaXuatBan;
import org.example.datnbbook.model.NhaXuatBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhaXuatBanRepository extends JpaRepository<NhaXuatBan, Integer> {
    @Query("SELECT sp FROM NhaXuatBan sp WHERE sp.deleted = false AND " +
            "(LOWER(sp.tenNhaXuatBan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(sp.maNhaXuatBan) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<NhaXuatBan> search(String keyword);

    @Query(value = "SELECT 'NXB' + RIGHT('-000' + CONVERT(NVARCHAR(5), NEXT VALUE FOR [dbo].[NXBSeq]), 5)", nativeQuery = true)
    String getNextSequenceValue();

    List<NhaXuatBan> findByDeletedFalse();
}
