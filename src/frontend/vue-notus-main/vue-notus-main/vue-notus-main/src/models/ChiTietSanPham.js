export default class ChiTietSanPham {
    constructor(data = {}) {
        this.id = data.id || null;
        this.maChiTietSanPham = data.maChiTietSanPham || '';
        this.tenChiTietSanPham = data.tenChiTietSanPham || '';
        this.idSanPham = data.idSanPham || null;
        this.idLoaiBia = data.idLoaiBia || null;
        this.idNguoiDich = data.idNguoiDich || null;
        this.idTacGia = data.idTacGia || null;
        this.idChatLieu = data.idChatLieu || null;
        this.idNhaXuatBan = data.idNhaXuatBan || null;
        this.idTheLoai = data.idTheLoai || null;
        this.idNgonNgu = data.idNgonNgu || null;
        this.images = data.images || [];
        this.trongLuong = data.trongLuong || 0;
        this.kichThuoc = data.kichThuoc || 0;
        this.moTa = data.moTa || '';
        this.gia = data.gia || 0;
        this.soLuongTon = data.soLuongTon || 0;
        this.trangThai = data.trangThai !== undefined ? data.trangThai : true;
        this.ghiChu = data.ghiChu || '';
        this.createdAt = data.createdAt || null;
        this.createdBy = data.createdBy || '';
        this.updatedAt = data.updatedAt || null;
        this.updatedBy = data.updatedBy || '';
        this.deleted = data.deleted !== undefined ? data.deleted : false;
    }

    toJSON() {
        return {
            id: this.id,
            maChiTietSanPham: this.maChiTietSanPham,
            tenChiTietSanPham: this.tenChiTietSanPham,
            idSanPham: this.idSanPham,
            idLoaiBia: this.idLoaiBia,
            idNguoiDich: this.idNguoiDich,
            idTacGia: this.idTacGia,
            idChatLieu: this.idChatLieu,
            idNhaXuatBan: this.idNhaXuatBan,
            idTheLoai: this.idTheLoai,
            idNgonNgu: this.idNgonNgu,
            images: this.images,
            trongLuong: this.trongLuong,
            kichThuoc: this.kichThuoc,
            moTa: this.moTa,
            gia: this.gia,
            soLuongTon: this.soLuongTon,
            trangThai: this.trangThai,
            ghiChu: this.ghiChu,
            createdAt: this.createdAt,
            createdBy: this.createdBy,
            updatedAt: this.updatedAt,
            updatedBy: this.updatedBy,
            deleted: this.deleted
        };
    }
} 