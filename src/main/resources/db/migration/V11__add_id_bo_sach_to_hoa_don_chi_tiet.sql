-- Thêm trường id_bo_sach vào bảng hoa_don_chi_tiet
ALTER TABLE hoa_don_chi_tiet 
ADD id_bo_sach INT NULL;
GO

-- Thêm foreign key constraint
ALTER TABLE hoa_don_chi_tiet 
ADD CONSTRAINT fk_hoa_don_chi_tiet_bo_sach 
FOREIGN KEY (id_bo_sach) REFERENCES bo_sach(id);
GO
-- Thêm comment để giải thích
EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'ID của bộ sách nếu đơn hàng chi tiết là bộ sách, NULL nếu là sản phẩm thường', 
    @level0type = N'SCHEMA', @level0name = N'dbo', 
    @level1type = N'TABLE', @level1name = N'hoa_don_chi_tiet', 
    @level2type = N'COLUMN', @level2name = N'id_bo_sach';
