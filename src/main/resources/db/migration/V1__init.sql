-- Thêm cột mới cho bảng khach_hang
ALTER TABLE khach_hang
ADD COLUMN dia_chi varchar(100),
    tinh_thanh varchar(50),
    quan_huyen varchar(50),
    xa_phuong varchar(50),
    created_by varchar(50),
    updated_by varchar(50); 