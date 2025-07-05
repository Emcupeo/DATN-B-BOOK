export default class DanhMuc {
    constructor(data = {}) {
        this.id = data.id || null;
        this.maDanhMuc = data.maDanhMuc || '';
        this.tenDanhMuc = data.tenDanhMuc || '';
        this.deleted = data.deleted !== undefined ? data.deleted : false;
    }

    toJSON() {
        return {
            id: this.id,
            tenDanhMuc: this.tenDanhMuc,
            deleted: this.deleted
        };
    }
} 