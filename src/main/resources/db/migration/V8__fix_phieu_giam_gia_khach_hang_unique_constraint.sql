-- Fix UNIQUE constraint issue for phieu_giam_gia_khach_hang table
-- Allow multiple personal vouchers for the same customer

-- Drop the existing UNIQUE constraint if it exists
IF EXISTS (SELECT * FROM sys.indexes WHERE name = 'UQ__phieu_gi__49A1D56459E11548' AND object_id = OBJECT_ID('phieu_giam_gia_khach_hang'))
BEGIN
    ALTER TABLE phieu_giam_gia_khach_hang DROP CONSTRAINT UQ__phieu_gi__49A1D56459E11548
END
GO

-- Create a new UNIQUE constraint that allows multiple vouchers per customer
-- but ensures each voucher-customer combination is unique
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'UK_phieu_giam_gia_khach_hang_unique' AND object_id = OBJECT_ID('phieu_giam_gia_khach_hang'))
BEGIN
    ALTER TABLE phieu_giam_gia_khach_hang 
    ADD CONSTRAINT UK_phieu_giam_gia_khach_hang_unique 
    UNIQUE (id_phieu_giam_gia, id_khach_hang, deleted)
END
GO

-- Update the sequence to ensure unique codes
-- Reset the sequence to avoid conflicts
IF EXISTS (SELECT * FROM sys.sequences WHERE name = 'PGGKHSeq')
BEGIN
    -- Get the current maximum value and set sequence to continue from there
    DECLARE @maxValue INT
    SELECT @maxValue = ISNULL(MAX(CAST(SUBSTRING(ma_phieu_giam_gia_khach_hang, 7, 5) AS INT)), 0)
    FROM phieu_giam_gia_khach_hang 
    WHERE ma_phieu_giam_gia_khach_hang LIKE 'PGGKH-%'
    
    -- Reset sequence to continue from max value
    DECLARE @nextValue INT = @maxValue + 1
    DECLARE @sql NVARCHAR(MAX) = 'ALTER SEQUENCE PGGKHSeq RESTART WITH ' + CAST(@nextValue AS NVARCHAR(10))
    EXEC sp_executesql @sql
END
GO
