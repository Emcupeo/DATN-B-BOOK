export default class TacGia {
    constructor(data = {}) {
        this.id = data.id || null;
        this.maTacGia = data.maTacGia || '';
        this.tenTacGia = data.tenTacGia || '';
        this.deleted = data.deleted !== undefined ? data.deleted : true;
    }

    toJSON() {
        return {
            id: this.id,
            tenTacGia: this.tenTacGia,
            deleted: this.deleted
        };
    }
} 