export default class NhanVien {
    constructor(data = {}) {
        this.id = data.id || null;
        this.idChucVu = data.idChucVu || null;
        this.maNhanVien = data.maNhanVien || '';
        this.hoTen = data.hoTen || '';
        this.gioiTinh = data.gioiTinh || true;
        this.namSinh = data.namSinh || null;
        this.soCanCuoc = data.soCanCuoc || '';
        this.soDienThoai = data.soDienThoai || '';
        this.trangThai = data.trangThai !== undefined ? data.trangThai : true;
        this.tenTaiKhoan = data.tenTaiKhoan || '';
        this.matKhau = data.matKhau || '';
        this.createdAt = data.createdAt || null;
        this.createdBy = data.createdBy || '';
        this.updatedAt = data.updatedAt || null;
        this.updatedBy = data.updatedBy || '';
        this.deleted = data.deleted !== undefined ? data.deleted : false;
    }

    toJSON() {
        return {
            id: this.id,
            idChucVu: this.idChucVu,
            hoTen: this.hoTen,
            gioiTinh: this.gioiTinh,
            namSinh: this.namSinh,
            soCanCuoc: this.soCanCuoc,
            soDienThoai: this.soDienThoai,
            trangThai: this.trangThai,
            tenTaiKhoan: this.tenTaiKhoan,
            matKhau: this.matKhau,
            createdAt: this.createdAt,
            createdBy: this.createdBy,
            updatedAt: this.updatedAt,
            updatedBy: this.updatedBy,
            deleted: this.deleted
        };
    }
} 