package org.example.datnbbook.repository;

import org.example.datnbbook.model.NguoiDich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface NguoiDichRepository extends JpaRepository<NguoiDich, Integer> {
}
