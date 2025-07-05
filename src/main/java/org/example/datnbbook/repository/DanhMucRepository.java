package org.example.datnbbook.repository;

import org.example.datnbbook.model.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {
    
    List<DanhMuc> findAllByDeletedFalseOrderByIdDesc();
    
    @Query("SELECT d FROM DanhMuc d WHERE d.deleted = false AND (d.tenDanhMuc LIKE %:keyword% OR d.maDanhMuc LIKE %:keyword%)")
    List<DanhMuc> search(@Param("keyword") String keyword);
}
