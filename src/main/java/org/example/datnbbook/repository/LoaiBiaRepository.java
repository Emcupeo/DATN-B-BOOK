package org.example.datnbbook.repository;

import org.example.datnbbook.model.LoaiBia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface LoaiBiaRepository extends JpaRepository<LoaiBia, Integer> {
}
