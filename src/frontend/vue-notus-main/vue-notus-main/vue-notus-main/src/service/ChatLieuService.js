import api from './api';
import ChatLieu from '../models/ChatLieu';

class ChatLieuService {
  async getAll() {
    const response = await api.get('/chat-lieu');
    return response.data.map(item => new ChatLieu(item));
  }

  async getById(id) {
    const response = await api.get(`/chat-lieu/${id}`);
    return new ChatLieu(response.data);
  }

  async create(chatLieu) {
    const response = await api.post('/chat-lieu', chatLieu.toJSON());
    return new ChatLieu(response.data);
  }

  async update(id, chatLieu) {
    const response = await api.put(`/chat-lieu/${id}`, chatLieu.toJSON());
    return new ChatLieu(response.data);
  }

  async delete(id) {
    await api.delete(`/chat-lieu/${id}`);
  }

  async search(keyword) {
    const response = await api.get('/chat-lieu/search', {
      params: { keyword }
    });
    return response.data.map(item => new ChatLieu(item));
  }
}

export default new ChatLieuService();