-- Xóa foreign key constraint hiện tại nếu có
IF EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK__dia_chi__khach_h__7A3223E8')
    ALTER TABLE dia_chi DROP CONSTRAINT FK__dia_chi__khach_h__7A3223E8;

-- Thay đổi kiểu dữ liệu của cột id trong bảng khach_hang thành BIGINT
ALTER TABLE khach_hang
ALTER COLUMN id BIGINT;

-- Tạo lại foreign key constraint
ALTER TABLE dia_chi
ADD CONSTRAINT FK_dia_chi_khach_hang
FOREIGN KEY (khach_hang_id) REFERENCES khach_hang(id); 