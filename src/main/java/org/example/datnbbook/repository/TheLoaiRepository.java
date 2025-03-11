package org.example.datnbbook.repository;

import org.example.datnbbook.model.TheLoai;
import org.example.datnbbook.model.TheLoai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheLoaiRepository extends JpaRepository<TheLoai, Integer> {
    @Query("SELECT sp FROM TheLoai sp WHERE sp.deleted = false AND " +
            "(LOWER(sp.tenTheLoai) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(sp.maTheLoai) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<TheLoai> search(String keyword);

    @Query(value = "SELECT 'TL' + RIGHT('-000' + CONVERT(NVARCHAR(5), NEXT VALUE FOR [dbo].[TLSeq]), 5)", nativeQuery = true)
    String getNextSequenceValue();

    List<TheLoai> findByDeletedFalse();
}
