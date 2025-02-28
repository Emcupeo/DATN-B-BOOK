package org.example.datnbbook.repository;

import org.example.datnbbook.model.TacGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface TacGiaRepository extends JpaRepository<TacGia, Integer> {
}
