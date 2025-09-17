package org.example.datnbbook.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithDiscountDTO {
    private Integer id;
    private String title;
    private String author;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String image;
    private String category;
    private String publisher;
    private String language;
    private String format;
    private Integer pages;
    private Integer stockQuantity;
    private Boolean inStock;
    private String productCode;
    private String description;
    private Boolean isBookSet;
    
    // Thông tin giảm giá
    private Boolean hasDiscount;
    private BigDecimal discountAmount;
    private BigDecimal discountPercentage;
}
