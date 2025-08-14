-- Batch 1: Thêm cột ISBN
ALTER TABLE dbo.chi_tiet_san_pham
    ADD isbn VARCHAR(20) NULL;
GO

-- Batch 2: Tạo function tính checksum ISBN-13
CREATE FUNCTION dbo.fn_TinhChecksumISBN13(@isbn12 VARCHAR(20))
    RETURNS CHAR(1)
AS
BEGIN
    DECLARE @i INT = 1, @len INT = LEN(@isbn12), @sum INT = 0, @digit INT, @char CHAR(1)

    WHILE @i <= @len
        BEGIN
            SET @char = SUBSTRING(@isbn12, @i, 1)
            SET @digit = TRY_CAST(@char AS INT)
            IF @digit IS NULL SET @digit = 0

            SET @sum += CASE WHEN @i % 2 = 0 THEN @digit * 3 ELSE @digit END
            SET @i += 1
        END

    RETURN CAST((10 - (@sum % 10)) % 10 AS CHAR(1))
END
GO

-- Batch 3: Cập nhật ISBN cho các sản phẩm
UPDATE dbo.chi_tiet_san_pham
SET isbn = '978-604-' +
           CAST(id_nha_xuat_ban AS VARCHAR) + '-' +
           CAST(id AS VARCHAR) + '-' +
           dbo.fn_TinhChecksumISBN13(
                   '978604' +
                   RIGHT('000' + CAST(id_nha_xuat_ban AS VARCHAR), 3) +
                   RIGHT('000000' + CAST(id AS VARCHAR), 6)
           )
WHERE isbn IS NULL
  AND id_nha_xuat_ban IS NOT NULL;
GO 