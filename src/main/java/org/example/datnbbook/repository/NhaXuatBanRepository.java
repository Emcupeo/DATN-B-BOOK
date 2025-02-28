package org.example.datnbbook.repository;

import org.example.datnbbook.model.NhaXuatBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface NhaXuatBanRepository extends JpaRepository<NhaXuatBan, Integer> {
}
