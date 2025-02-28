package org.example.datnbbook.repository;

import org.example.datnbbook.model.ChiTietSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Integer> {

}
