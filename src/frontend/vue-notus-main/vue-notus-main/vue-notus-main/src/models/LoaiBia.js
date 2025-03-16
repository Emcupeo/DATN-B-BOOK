export default class LoaiBia {
    constructor(data = {}) {
        this.id = data.id || null;
        this.maLoaiBia = data.maLoaiBia || '';
        this.tenLoaiBia = data.tenLoaiBia || '';
        this.deleted = data.deleted !== undefined ? data.deleted : true;
    }

    toJSON() {
        return {
            id: this.id,
            tenLoaiBia: this.tenLoaiBia,
            deleted: this.deleted
        };
    }
} 