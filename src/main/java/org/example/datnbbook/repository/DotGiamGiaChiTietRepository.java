package org.example.datnbbook.repository;

import org.example.datnbbook.model.DotGiamGiaChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DotGiamGiaChiTietRepository extends JpaRepository<DotGiamGiaChiTiet, Integer> {
}