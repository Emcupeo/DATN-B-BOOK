export default class BoSach {
    constructor(data = {}) {
        this.id = data.id || null;
        this.maBoSach = data.maBoSach || '';
        this.tenBoSach = data.tenBoSach || '';
        this.moTa = data.moTa || '';
        this.soLuong = data.soLuong || 0;
        this.giaTien = data.giaTien || 0;
        this.deleted = data.deleted !== undefined ? data.deleted : true;
        this.url = data.url || '';
        this.createdAt = data.createdAt || null;
        this.updatedAt = data.updatedAt || null;
        this.createdBy = data.createdBy || '';
        this.updatedBy = data.updatedBy || '';
    }

    toJSON() {
        return {
            id: this.id,
            maBoSach: this.maBoSach,
            tenBoSach: this.tenBoSach,
            moTa: this.moTa,
            soLuong: this.soLuong,
            giaTien: this.giaTien,
            deleted: this.deleted,
            url: this.url,
            createdAt: this.createdAt,
            updatedAt: this.updatedAt,
            createdBy: this.createdBy,
            updatedBy: this.updatedBy
        };
    }
}