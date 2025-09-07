-- Add ten_nguoi_nhan and so_dien_thoai_nguoi_nhan columns to dia_chi table
ALTER TABLE dia_chi 
ADD ten_nguoi_nhan NVARCHAR(255);

ALTER TABLE dia_chi 
ADD so_dien_thoai_nguoi_nhan NVARCHAR(15);
