package org.example.datnbbook.repository;

import org.example.datnbbook.model.AnhSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnhSanPhamRepository extends JpaRepository<AnhSanPham, Integer> {
    List<AnhSanPham> findByUrlAndIdLoaiBiaId(String url, Integer idLoaiBia);
    List<AnhSanPham> findByIdLoaiBiaId(Integer idLoaiBia);

    @Query("SELECT a FROM AnhSanPham a WHERE a.url = :url")
    List<AnhSanPham> findByUrl(String url);

}