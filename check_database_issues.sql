-- Kiểm tra và khắc phục các vấn đề database

-- 1. Kiểm tra SQL Server có chạy không
SELECT @@VERSION as SQL_Server_Version;

-- 2. Kiểm tra database BBook có tồn tại không
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'BBook')
BEGIN
    PRINT 'Database BBook không tồn tại!';
    PRINT 'Vui lòng tạo database BBook trước khi chạy ứng dụng.';
END
ELSE
BEGIN
    PRINT 'Database BBook tồn tại.';
END

-- 3. Kiểm tra user sa có quyền truy cập không
USE BBook;
IF EXISTS (SELECT * FROM sys.database_principals WHERE name = 'sa')
BEGIN
    PRINT 'User sa có quyền truy cập database BBook.';
END
ELSE
BEGIN
    PRINT 'User sa không có quyền truy cập database BBook!';
END

-- 4. Kiểm tra các bảng cần thiết
DECLARE @missing_tables TABLE (table_name VARCHAR(100));
INSERT INTO @missing_tables (table_name)
SELECT 'nhan_vien' WHERE NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'nhan_vien')
UNION ALL
SELECT 'nguoi_dung' WHERE NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'nguoi_dung')
UNION ALL
SELECT 'chi_tiet_san_pham' WHERE NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'chi_tiet_san_pham');

IF EXISTS (SELECT * FROM @missing_tables)
BEGIN
    PRINT 'Các bảng bị thiếu:';
    SELECT table_name FROM @missing_tables;
END
ELSE
BEGIN
    PRINT 'Tất cả các bảng cần thiết đều tồn tại.';
END

-- 5. Kiểm tra kết nối JDBC
-- Thử kết nối với connection string
-- jdbc:sqlserver://localhost:1433;databaseName=BBook;encrypt=true;trustServerCertificate=true 