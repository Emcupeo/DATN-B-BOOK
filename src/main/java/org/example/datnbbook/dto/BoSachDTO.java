package org.example.datnbbook.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class BoSachDTO {
    private Integer id;
    private String maBoSach;
    @NotBlank(message = "Tên bộ sách không được để trống")
    private String tenBoSach;
    @NotNull(message = "Giá tiền không được để trống")
    @PositiveOrZero(message = "Giá tiền phải không âm")
    private Double giaTien;
    private String moTa;
    @NotNull(message = "Số lượng không được để trống")
    @PositiveOrZero(message = "Số lượng phải không âm")
    private Integer soLuong;
    @NotEmpty(message = "Danh sách chi tiết bộ sách không được để trống")
    private List<BoSachChiTietDTO> boSachChiTiets;

    @Data
    public static class BoSachChiTietDTO {
        @NotNull(message = "ID chi tiết sản phẩm không được để trống")
        private Integer idChiTietSanPham; // Changed from Long to Integer
        @NotNull(message = "Số lượng không được để trống")
        @Positive(message = "Số lượng phải lớn hơn 0")
        private Integer soLuong;
    }
}