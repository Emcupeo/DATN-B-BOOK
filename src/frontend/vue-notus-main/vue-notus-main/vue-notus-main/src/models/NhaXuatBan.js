export default class NhaXuatBan {
    constructor(data = {}) {
        this.id = data.id || null;
        this.maNhaXuatBan = data.maNhaXuatBan || '';
        this.tenNhaXuatBan = data.tenNhaXuatBan || '';
        this.deleted = data.deleted !== undefined ? data.deleted : true;
    }

    toJSON() {
        return {
            id: this.id,
            tenNhaXuatBan: this.tenNhaXuatBan,
            deleted: this.deleted
        };
    }
} 