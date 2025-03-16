package org.example.datnbbook.repository;

import org.example.datnbbook.model.ChatLieu;
import org.example.datnbbook.model.LoaiBia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoaiBiaRepository extends JpaRepository<LoaiBia, Integer> {
    @Query("SELECT sp FROM LoaiBia sp WHERE sp.deleted = false AND " +
            "(LOWER(sp.tenLoaiBia) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(sp.maLoaiBia) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<LoaiBia> search(String keyword);

    @Query(value = "SELECT 'LB' + RIGHT('-000' + CONVERT(NVARCHAR(5), NEXT VALUE FOR [dbo].[LBSeq]), 5)", nativeQuery = true)
    String getNextSequenceValue();

    List<LoaiBia> findByDeletedFalse();
}
