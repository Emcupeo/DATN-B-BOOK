package org.example.datnbbook.repository;

import org.example.datnbbook.model.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Long> {
    @Query("SELECT p FROM PhieuGiamGia p WHERE p.deleted = false")
    Page<PhieuGiamGia> findAllByDeletedFalse(Pageable pageable);

    @Query(value = "SELECT 'PGG-' + RIGHT('00000' + CONVERT(NVARCHAR, NEXT VALUE FOR [dbo].[PGGSeq]), 5)", nativeQuery = true)
    String getNextSequenceValue();

    boolean existsByMaPhieuGiamGia(String maPhieuGiamGia);
}
