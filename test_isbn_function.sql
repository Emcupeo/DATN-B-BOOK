-- Test function ISBN-13
-- Ví dụ: ISBN-13 đầy đủ: 978-604-1-123456-7
-- Trong đó: 978-604 là prefix cho Việt Nam, 1 là mã NXB, 123456 là mã sản phẩm, 7 là checksum

-- Test với một số ISBN-12 để tính checksum
SELECT '978604001000001' as isbn12, dbo.fn_TinhChecksumISBN13('978604001000001') as checksum;
SELECT '978604001000002' as isbn12, dbo.fn_TinhChecksumISBN13('978604001000002') as checksum;
SELECT '978604001000003' as isbn12, dbo.fn_TinhChecksumISBN13('978604001000003') as checksum;

-- Kiểm tra một số ISBN đã được tạo
SELECT TOP 10 
    id,
    id_nha_xuat_ban,
    isbn,
    '978-604-' + CAST(id_nha_xuat_ban AS VARCHAR) + '-' + CAST(id AS VARCHAR) + '-' + 
    dbo.fn_TinhChecksumISBN13('978604' + RIGHT('000' + CAST(id_nha_xuat_ban AS VARCHAR), 3) + RIGHT('000000' + CAST(id AS VARCHAR), 6)) as calculated_isbn
FROM chi_tiet_san_pham 
WHERE isbn IS NOT NULL 
ORDER BY id; 