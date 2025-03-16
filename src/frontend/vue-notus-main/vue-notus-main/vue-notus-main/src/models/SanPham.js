export default class SanPham {
    constructor(data = {}) {
        this.id = data.id || null;
        this.maSanPham = data.maSanPham || '';
        this.tenSanPham = data.tenSanPham || '';
        this.moTa = data.moTa || '';
        this.tongSoLuongTon = data.tongSoLuongTon
        this.deleted = data.deleted !== undefined ? data.deleted : true;
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