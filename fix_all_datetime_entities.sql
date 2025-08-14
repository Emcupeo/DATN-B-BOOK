-- Script để sửa tất cả các entity có vấn đề datetime
-- Chạy script này để đảm bảo tất cả các cột datetime khớp với database

USE BBook;

-- Kiểm tra tất cả các bảng có cột created_at và updated_at
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE COLUMN_NAME IN ('created_at', 'updated_at')
ORDER BY TABLE_NAME, COLUMN_NAME;

-- Nếu cần thiết, có thể chạy các câu lệnh ALTER TABLE để sửa kiểu dữ liệu
-- Ví dụ:
-- ALTER TABLE bo_sach ALTER COLUMN created_at DATETIME;
-- ALTER TABLE bo_sach ALTER COLUMN updated_at DATETIME; 