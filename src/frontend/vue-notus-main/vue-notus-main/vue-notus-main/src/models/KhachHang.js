export default class KhachHang {
    constructor(data = {}) {
        this.id = data.id || null;
        this.hoTen = data.hoTen || '';
        this.email = data.email || '';
        this.soDienThoai = data.soDienThoai || '';
        this.ngaySinh = data.ngaySinh || null;
        this.gioiTinh = data.gioiTinh !== undefined ? data.gioiTinh : true;
        this.trangThai = data.trangThai !== undefined ? data.trangThai : true;
        this.createdAt = data.createdAt || null;
        this.createdBy = data.createdBy || null;
        this.updatedAt = data.updatedAt || null;
        this.updatedBy = data.updatedBy || null;
        this.deleted = data.deleted !== undefined ? data.deleted : false;
        this.danhSachDiaChi = Array.isArray(data.danhSachDiaChi) ? data.danhSachDiaChi.map(diaChi => ({
            id: diaChi.id || null,
            diaChiChiTiet: diaChi.diaChiChiTiet || '',
            tinhThanh: diaChi.tinhThanh || '',
            quanHuyen: diaChi.quanHuyen || '',
            xaPhuong: diaChi.xaPhuong || '',
            macDinh: diaChi.macDinh !== undefined ? diaChi.macDinh : false,
            khachHangId: diaChi.khachHangId || this.id
        })) : [];
        // Only set maKhachHang if it exists in the data (for existing customers)
        if (data.maKhachHang) {
            this.maKhachHang = data.maKhachHang;
        }
    }

    toJSON() {
        const json = {
            id: this.id,
            hoTen: this.hoTen,
            email: this.email,
            soDienThoai: this.soDienThoai,
            ngaySinh: this.ngaySinh,
            gioiTinh: this.gioiTinh,
            trangThai: this.trangThai,
            createdAt: this.createdAt,
            createdBy: this.createdBy,
            updatedAt: this.updatedAt,
            updatedBy: this.updatedBy,
            deleted: this.deleted,
            danhSachDiaChi: this.danhSachDiaChi
        };
        // Only include maKhachHang in JSON if it exists (for existing customers)
        if (this.maKhachHang) {
            json.maKhachHang = this.maKhachHang;
        }
        return json;
    }
} 