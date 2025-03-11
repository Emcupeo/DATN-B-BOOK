export default class NguoiDich {
    constructor(data = {}) {
        this.id = data.id || null;
        this.maNguoiDich = data.maNguoiDich || '';
        this.tenNguoiDich = data.tenNguoiDich || '';
        this.deleted = data.deleted !== undefined ? data.deleted : true;
    }

    toJSON() {
        return {
            id: this.id,
            tenNguoiDich: this.tenNguoiDich,
            deleted: this.deleted
        };
    }
} 