package org.example.datnbbook.repository;

import org.example.datnbbook.model.ChatLieu;
import org.example.datnbbook.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ChatLieuRepository extends JpaRepository<ChatLieu, Integer> {
    @Query("SELECT sp FROM ChatLieu sp WHERE sp.deleted = false AND " +
            "(LOWER(sp.tenChatLieu) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(sp.maChatLieu) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<ChatLieu> search(String keyword);

    @Query(value = "SELECT 'CL' + RIGHT('-000' + CONVERT(NVARCHAR(5), NEXT VALUE FOR [dbo].[CLSeq]), 5)", nativeQuery = true)
    String getNextSequenceValue();

    List<ChatLieu> findAllByDeletedFalseOrderByIdDesc();
}
