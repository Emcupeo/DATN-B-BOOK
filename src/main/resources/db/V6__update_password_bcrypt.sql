-- Cập nhật mật khẩu các tài khoản hiện tại sang mã hóa BCrypt
-- BCrypt hash cho các mật khẩu thông dụng:
-- admin123 -> $2a$10$YourHashedPasswordHere
-- 123456 -> $2a$10$YourHashedPasswordHere

-- Cập nhật mật khẩu admin (admin123) 
UPDATE [dbo].[nguoi_dung] 
SET [mat_khau] = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.' 
WHERE [ten_dang_nhap] = 'admin' AND [mat_khau] = 'admin123';

-- Cập nhật mật khẩu nhân viên (123456)
UPDATE [dbo].[nguoi_dung] 
SET [mat_khau] = '$2a$10$N9qo8uLOickgx2ZMRZoMye/mZXVEjb4R8UyXJhEZdl7/h5RfhlOZK' 
WHERE [ten_dang_nhap] = 'nhanvien1' AND [mat_khau] = '123456';

-- Cập nhật mật khẩu khách hàng (123456)
UPDATE [dbo].[nguoi_dung] 
SET [mat_khau] = '$2a$10$N9qo8uLOickgx2ZMRZoMye/mZXVEjb4R8UyXJhEZdl7/h5RfhlOZK' 
WHERE [ten_dang_nhap] = 'khachhang1' AND [mat_khau] = '123456';