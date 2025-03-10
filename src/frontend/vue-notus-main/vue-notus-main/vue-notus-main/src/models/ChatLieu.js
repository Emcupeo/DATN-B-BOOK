export default class ChatLieu {
    constructor(data = {}) {
        this.id = data.id || null;
        this.maChatLieu = data.maChatLieu || '';
        this.tenChatLieu = data.tenChatLieu || '';
        this.deleted = data.deleted !== undefined ? data.deleted : true;
    }

    toJSON() {
        return {
            id: this.id,
            tenChatLieu: this.tenChatLieu,
            deleted: this.deleted
        };
    }
} 