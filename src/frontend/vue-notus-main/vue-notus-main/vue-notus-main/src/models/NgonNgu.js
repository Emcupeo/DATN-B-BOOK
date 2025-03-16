export default class NgonNgu {
    constructor(data = {}) {
        this.id = data.id || null;
        this.maNgonNgu = data.maNgonNgu || '';
        this.tenNgonNgu = data.tenNgonNgu || '';
        this.deleted = data.deleted !== undefined ? data.deleted : true;
    }

    toJSON() {
        return {
            id: this.id,
            tenNgonNgu: this.tenNgonNgu,
            deleted: this.deleted
        };
    }
} 