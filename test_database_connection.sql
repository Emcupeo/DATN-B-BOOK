-- Test kết nối database
-- Chạy các câu lệnh này trong SQL Server Management Studio hoặc Azure Data Studio

-- 1. Kiểm tra database có tồn tại không
SELECT name FROM sys.databases WHERE name = 'BBook';

-- 2. Kiểm tra các bảng trong database
USE BBook;
SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE';

-- 3. Kiểm tra kết nối
SELECT @@VERSION as SQL_Server_Version;
SELECT DB_NAME() as Current_Database;

-- 4. Kiểm tra một số bảng quan trọng
SELECT COUNT(*) as Total_NhanVien FROM nhan_vien;
SELECT COUNT(*) as Total_NguoiDung FROM nguoi_dung;
SELECT COUNT(*) as Total_ChiTietSanPham FROM chi_tiet_san_pham; 