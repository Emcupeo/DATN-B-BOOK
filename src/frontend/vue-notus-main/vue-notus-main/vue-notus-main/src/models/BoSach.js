export default class BoSach {
    constructor(data = {}) {
        this.id = data.id || null;
        this.maBoSach = data.maBoSach || '';
        this.tenBoSach = data.tenBoSach || '';
        this.moTa = data.moTa || '';
        this.soLuong = data.soLuong || 0;
        this.giaTien = data.giaTien || 0;
        this.deleted = data.deleted !== undefined ? data.deleted : true;
    }

    toJSON() {
        return {
            id: this.id,
            maBoSach: this.maBoSach,
            tenBoSach: this.tenBoSach,
            moTa: this.moTa,
            soLuong: this.soLuong,
            giaTien: this.giaTien,
            deleted: this.deleted
        };
    }
}