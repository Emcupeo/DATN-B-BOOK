package org.example.datnbbook.repository;

import org.example.datnbbook.model.TheLoai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface TheLoaiRepository extends JpaRepository<TheLoai, Integer> {
}
