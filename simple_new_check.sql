-- Kiểm tra logic mới
SELECT TOP 5
    id,
    maHoaDon,
    soDienThoaiNguoiNhan,
    tenNguoiNhan
FROM hoa_don
WHERE soDienThoaiNguoiNhan IS NOT NULL
ORDER BY id DESC;
