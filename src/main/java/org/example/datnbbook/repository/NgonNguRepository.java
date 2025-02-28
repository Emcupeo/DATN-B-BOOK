package org.example.datnbbook.repository;

import org.example.datnbbook.model.NgonNgu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface NgonNguRepository extends JpaRepository<NgonNgu, Integer> {
}
