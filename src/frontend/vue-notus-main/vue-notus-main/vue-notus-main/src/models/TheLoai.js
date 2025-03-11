export default class TheLoai {
    constructor(data = {}) {
        this.id = data.id || null;
        this.maTheLoai = data.maTheLoai || '';
        this.tenTheLoai = data.tenTheLoai || '';
        this.deleted = data.deleted !== undefined ? data.deleted : true;
    }

    toJSON() {
        return {
            id: this.id,
            tenTheLoai: this.tenTheLoai,
            deleted: this.deleted
        };
    }
} 