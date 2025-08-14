-- Script để sửa kiểu dữ liệu datetime trong database
-- Chạy script này để đảm bảo tất cả các cột datetime khớp với entity

USE BBook;

-- Kiểm tra và sửa các bảng có cột created_at và updated_at
-- Bảng bo_sach
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'bo_sach' AND COLUMN_NAME = 'created_at')
BEGIN
    PRINT 'Checking bo_sach.created_at column type...';
    SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'bo_sach' AND COLUMN_NAME IN ('created_at', 'updated_at');
END

-- Bảng bo_sach_chi_tiet
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'bo_sach_chi_tiet' AND COLUMN_NAME = 'created_at')
BEGIN
    PRINT 'Checking bo_sach_chi_tiet.created_at column type...';
    SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'bo_sach_chi_tiet' AND COLUMN_NAME IN ('created_at', 'updated_at');
END

-- Bảng chi_tiet_san_pham
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'chi_tiet_san_pham' AND COLUMN_NAME = 'created_at')
BEGIN
    PRINT 'Checking chi_tiet_san_pham.created_at column type...';
    SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'chi_tiet_san_pham' AND COLUMN_NAME IN ('created_at', 'updated_at');
END

-- Bảng hoa_don
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'hoa_don' AND COLUMN_NAME = 'created_at')
BEGIN
    PRINT 'Checking hoa_don.created_at column type...';
    SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'hoa_don' AND COLUMN_NAME IN ('created_at', 'updated_at');
END

-- Bảng nhan_vien
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'nhan_vien' AND COLUMN_NAME = 'created_at')
BEGIN
    PRINT 'Checking nhan_vien.created_at column type...';
    SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'nhan_vien' AND COLUMN_NAME IN ('created_at', 'updated_at');
END

-- Bảng san_pham
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'san_pham' AND COLUMN_NAME = 'created_at')
BEGIN
    PRINT 'Checking san_pham.created_at column type...';
    SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'san_pham' AND COLUMN_NAME IN ('created_at', 'updated_at');
END 