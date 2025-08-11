package org.example.datnbbook.repository;

import org.example.datnbbook.model.ChucVu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChucVuRepository extends JpaRepository<ChucVu,Integer> {
    Optional<ChucVu> findByTenChucVuContainingIgnoreCase(String tenChucVu);
    Optional<ChucVu> findFirstByOrderByIdAsc(); // Lấy chức vụ đầu tiên làm mặc định
}
