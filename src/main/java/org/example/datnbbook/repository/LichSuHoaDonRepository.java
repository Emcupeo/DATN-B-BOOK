package org.example.datnbbook.repository;

import org.example.datnbbook.model.LichSuHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LichSuHoaDonRepository extends JpaRepository<LichSuHoaDon, Integer> {
    @Query("SELECT l FROM LichSuHoaDon l JOIN FETCH l.hoaDon WHERE l.id = :id")
    Optional<LichSuHoaDon> findByIdWithHoaDon(@Param("id") Integer id);

}
