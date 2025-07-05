package org.example.datnbbook.repository;

import org.example.datnbbook.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    @Query("SELECT sp FROM SanPham sp WHERE sp.deleted = false AND " +
            "(LOWER(sp.tenSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(sp.maSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(sp.moTa) LIKE LOWER(CONCAT('%', :keyword, '%')))")

    List<SanPham> search(String keyword);

    @Query(value = "SELECT 'SP' + RIGHT('-00' + CONVERT(NVARCHAR(5), NEXT VALUE FOR [dbo].[SPSeq]), 5)", nativeQuery = true)
    String getNextSequenceValue();

    @Query(value = "SELECT 'CTSP' + RIGHT('-00' + CONVERT(NVARCHAR(5), NEXT VALUE FOR [dbo].[CTSPSeq]), 5)", nativeQuery = true)
    String getNextCTSPSequenceValue();

    List<SanPham> findAllByDeletedFalseOrderByIdDesc();

    List<SanPham> findByDanhMuc_Id(Integer danhMucId);


    @Modifying
    @Transactional
    @Query(value = "DECLARE @NewId INT; " +
            "INSERT INTO san_pham (ma_san_pham, ten_san_pham, mo_ta, deleted) " +
            "VALUES ('SP' + RIGHT('-000' + CONVERT(NVARCHAR(5), NEXT VALUE FOR [dbo].[SPSeq]), 5), :tenSanPham, :moTa, 0); " +
            "SET @NewId = SCOPE_IDENTITY(); " +
            "INSERT INTO chi_tiet_san_pham (ten_chi_tiet_san_pham, id_san_pham) " +
            "VALUES (:tenCTSP, @NewId);",
            nativeQuery = true)
    void createSanPhamAndChiTiet(@Param("tenSanPham") String tenSanPham,
                                 @Param("moTa") String moTa,
                                 @Param("tenCTSP") String tenCTSP);
}
