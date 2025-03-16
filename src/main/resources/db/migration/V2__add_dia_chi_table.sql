IF EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID('khach_hang') AND name = 'dia_chi')
    ALTER TABLE khach_hang DROP COLUMN dia_chi;

IF EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID('khach_hang') AND name = 'tinh_thanh')
    ALTER TABLE khach_hang DROP COLUMN tinh_thanh;

IF EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID('khach_hang') AND name = 'quan_huyen')
    ALTER TABLE khach_hang DROP COLUMN quan_huyen;

IF EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID('khach_hang') AND name = 'xa_phuong')
    ALTER TABLE khach_hang DROP COLUMN xa_phuong;

-- Tạo bảng dia_chi
CREATE TABLE dia_chi (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    dia_chi_chi_tiet NVARCHAR(255), 
    tinh_thanh NVARCHAR(100),
    quan_huyen NVARCHAR(100),
    xa_phuong NVARCHAR(100),
    mac_dinh BIT DEFAULT 0,
    khach_hang_id BIGINT,
    created_at DATETIME DEFAULT GETDATE(),
    created_by NVARCHAR(50),
    updated_at DATETIME DEFAULT GETDATE(),
    updated_by NVARCHAR(50),
    deleted BIT DEFAULT 0,
    FOREIGN KEY (khach_hang_id) REFERENCES khach_hang(id)
);
