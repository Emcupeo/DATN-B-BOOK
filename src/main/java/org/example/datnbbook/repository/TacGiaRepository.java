package org.example.datnbbook.repository;

import org.example.datnbbook.model.TacGia;
import org.example.datnbbook.model.TacGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TacGiaRepository extends JpaRepository<TacGia, Integer> {
    @Query("SELECT sp FROM TacGia sp WHERE sp.deleted = false AND " +
            "(LOWER(sp.tenTacGia) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(sp.maTacGia) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<TacGia> search(String keyword);

    @Query(value = "SELECT 'TG' + RIGHT('-000' + CONVERT(NVARCHAR(5), NEXT VALUE FOR [dbo].[TGSeq]), 5)", nativeQuery = true)
    String getNextSequenceValue();

    List<TacGia> findByDeletedFalse();
}
