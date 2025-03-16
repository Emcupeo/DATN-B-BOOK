package org.example.datnbbook.repository;

import org.example.datnbbook.model.NgonNgu;
import org.example.datnbbook.model.NgonNgu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NgonNguRepository extends JpaRepository<NgonNgu, Integer> {
    @Query("SELECT sp FROM NgonNgu sp WHERE sp.deleted = false AND " +
            "(LOWER(sp.tenNgonNgu) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(sp.maNgonNgu) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<NgonNgu> search(String keyword);

    @Query(value = "SELECT 'NN' + RIGHT('-000' + CONVERT(NVARCHAR(5), NEXT VALUE FOR [dbo].[NNSeq]), 5)", nativeQuery = true)
    String getNextSequenceValue();

    List<NgonNgu> findByDeletedFalse();
}
