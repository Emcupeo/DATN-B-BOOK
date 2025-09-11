package org.example.datnbbook.repository;

import org.example.datnbbook.model.BoSachChiTiet;
import org.example.datnbbook.model.BoSachChiTietId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoSachChiTietRepository extends JpaRepository<BoSachChiTiet, BoSachChiTietId> {

    @Query("SELECT b FROM BoSachChiTiet b " +
            "JOIN FETCH b.idChiTietSanPham c " +
            "LEFT JOIN FETCH c.idLoaiBia " +
            "LEFT JOIN FETCH c.idTacGia " +
            "LEFT JOIN FETCH c.idNhaXuatBan " +
            "LEFT JOIN FETCH c.idChatLieu " +
            "LEFT JOIN FETCH c.idNguoiDich " +
            "LEFT JOIN FETCH c.idTheLoai " +
            "LEFT JOIN FETCH c.idNgonNgu " +
            "WHERE b.id.idBoSach = :idBoSach AND b.deleted = false")
    List<BoSachChiTiet> findByIdBoSachId(Integer idBoSach);

    @Query("SELECT b FROM BoSachChiTiet b WHERE b.id.idBoSach = :idBoSach AND b.id.idChiTietSanPham = :idChiTietSanPham AND b.deleted = false")
    BoSachChiTiet findByIdBoSachIdAndIdChiTietSanPhamId(Integer idBoSach, Integer idChiTietSanPham);

    @Query("SELECT CASE WHEN COUNT(b)>0 THEN true ELSE false END FROM BoSachChiTiet b " +
            "JOIN b.idBoSach bs " +
            "WHERE b.deleted = false AND bs.deleted = false AND b.idChiTietSanPham.id = :ctspId")
    boolean existsActiveByChiTietSanPhamId(@Param("ctspId") Integer ctspId);

    @Query("SELECT bs.tenBoSach FROM BoSachChiTiet b " +
            "JOIN b.idBoSach bs " +
            "WHERE b.deleted = false AND bs.deleted = false AND b.idChiTietSanPham.id = :ctspId")
    java.util.List<String> findActiveBoSachNamesByChiTietSanPhamId(@Param("ctspId") Integer ctspId);
}