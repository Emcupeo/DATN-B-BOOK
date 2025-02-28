package org.example.datnbbook.repository;

import org.example.datnbbook.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
@Component
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
//    List<SanPham> findByDeletedFalse();


}
