export default class SanPham {
    constructor(data = {}) {
        this.id = data.id || null;
        this.maSanPham = data.maSanPham || '';
        this.tenSanPham = data.tenSanPham || '';
        this.moTa = data.moTa || '';
        this.idTacGia = data.idTacGia || null;
        this.idNhaXuatBan = data.idNhaXuatBan || null;
        this.idNguoiDich = data.idNguoiDich || null;
        this.idTheLoai = data.idTheLoai || null;
        this.idNgonNgu = data.idNgonNgu || null;
        this.chiTietSanPham = data.chiTietSanPham || null;
        this.deleted = data.deleted !== undefined ? data.deleted : false;
        this.tongSoLuongTon = data.tongSoLuongTon
        this.createdAt = data.createdAt || null;
        this.createdBy = data.createdBy || '';
        this.updatedAt = data.updatedAt || null;
        this.updatedBy = data.updatedBy || '';
    }

    toJSON() {
        return {
            id: this.id,
            tenSanPham: this.tenSanPham,
            moTa: this.moTa,
            deleted: this.deleted
        };
    }
} 