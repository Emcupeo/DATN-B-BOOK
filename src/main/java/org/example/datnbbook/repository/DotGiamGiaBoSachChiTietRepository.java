package org.example.datnbbook.repository;

import org.example.datnbbook.model.DotGiamGiaBoSachChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DotGiamGiaBoSachChiTietRepository extends JpaRepository<DotGiamGiaBoSachChiTiet, Long> {

    @Query("SELECT d FROM DotGiamGiaBoSachChiTiet d " +
           "JOIN FETCH d.idBoSach b " +
           "JOIN FETCH d.idDotGiamGia dot " +
           "WHERE d.idDotGiamGia.id = :idDotGiamGia AND d.deleted = false")
    List<DotGiamGiaBoSachChiTiet> findByIdDotGiamGia(@Param("idDotGiamGia") Integer idDotGiamGia);

    @Query("SELECT d FROM DotGiamGiaBoSachChiTiet d " +
           "JOIN FETCH d.idBoSach b " +
           "JOIN FETCH d.idDotGiamGia dot " +
           "WHERE d.idBoSach.id = :idBoSach AND d.deleted = false")
    List<DotGiamGiaBoSachChiTiet> findByIdBoSach(@Param("idBoSach") Integer idBoSach);

    @Query("SELECT d FROM DotGiamGiaBoSachChiTiet d " +
           "JOIN FETCH d.idBoSach b " +
           "JOIN FETCH d.idDotGiamGia dot " +
           "WHERE d.idBoSach.id = :idBoSach AND d.idDotGiamGia.id = :idDotGiamGia AND d.deleted = false")
    DotGiamGiaBoSachChiTiet findByIdBoSachAndIdDotGiamGia(@Param("idBoSach") Integer idBoSach, 
                                                          @Param("idDotGiamGia") Integer idDotGiamGia);

    @Query("SELECT d FROM DotGiamGiaBoSachChiTiet d " +
           "JOIN FETCH d.idBoSach b " +
           "JOIN FETCH d.idDotGiamGia dot " +
           "WHERE d.idBoSach.id = :idBoSach AND d.trangThai = true AND d.deleted = false " +
           "AND dot.ngayBatDau <= CURRENT_TIMESTAMP AND dot.ngayKetThuc >= CURRENT_TIMESTAMP")
    List<DotGiamGiaBoSachChiTiet> findActiveDiscountsByIdBoSach(@Param("idBoSach") Integer idBoSach);

    @Query("SELECT d FROM DotGiamGiaBoSachChiTiet d " +
           "JOIN FETCH d.idBoSach b " +
           "JOIN FETCH d.idDotGiamGia dot " +
           "WHERE d.idBoSach.id = :idBoSach AND d.trangThai = true AND d.deleted = false " +
           "AND dot.ngayBatDau <= CURRENT_TIMESTAMP AND dot.ngayKetThuc >= CURRENT_TIMESTAMP")
    DotGiamGiaBoSachChiTiet findActiveDiscountByBoSachId(@Param("idBoSach") Integer idBoSach);

    @Query("SELECT d FROM DotGiamGiaBoSachChiTiet d " +
           "JOIN FETCH d.idBoSach b " +
           "JOIN FETCH d.idDotGiamGia dot " +
           "WHERE d.trangThai = true AND d.deleted = false " +
           "AND dot.ngayBatDau <= CURRENT_TIMESTAMP AND dot.ngayKetThuc >= CURRENT_TIMESTAMP")
    List<DotGiamGiaBoSachChiTiet> findAllActiveDiscounts();

    @Query("SELECT COUNT(d) > 0 FROM DotGiamGiaBoSachChiTiet d " +
           "JOIN d.idDotGiamGia dot " +
           "WHERE d.idBoSach.id = :idBoSach AND d.trangThai = true AND d.deleted = false " +
           "AND dot.ngayBatDau <= CURRENT_TIMESTAMP AND dot.ngayKetThuc >= CURRENT_TIMESTAMP")
    boolean existsActiveDiscountForBoSach(@Param("idBoSach") Integer idBoSach);
}
