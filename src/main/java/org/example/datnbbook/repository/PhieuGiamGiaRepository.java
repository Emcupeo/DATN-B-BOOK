package org.example.datnbbook.repository;

import org.example.datnbbook.model.PhieuGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Long> {
    @Query("SELECT p FROM PhieuGiamGia p WHERE p.deleted = false ")
    List<PhieuGiamGia> getAllActive();

    @Query(value = "SELECT 'PGG' + RIGHT('-000' + CONVERT(NVARCHAR(5), NEXT VALUE FOR [dbo].[PGGSeq]), 5)", nativeQuery = true)
    String getNextSequenceValue();
}
