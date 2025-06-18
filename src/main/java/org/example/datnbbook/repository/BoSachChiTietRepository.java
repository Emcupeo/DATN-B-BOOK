package org.example.datnbbook.repository;

import org.example.datnbbook.model.BoSach;
import org.example.datnbbook.model.BoSachChiTiet;
import org.example.datnbbook.model.ChatLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public interface BoSachChiTietRepository extends JpaRepository<BoSachChiTiet, Integer> {

    
}
