import api from './api';
import TacGia from '../models/TacGia';

class TacGiaService {
  async getAll() {
    const response = await api.get('/tac-gia');
    return response.data.map(item => new TacGia(item));
  }

  async getById(id) {
    const response = await api.get(`/tac-gia/${id}`);
    return new TacGia(response.data);
  }

  async create(tacGia) {
    const response = await api.post('/tac-gia', tacGia);
    return new TacGia(response.data);
  }

  async update(id, tacGia) {
    const response = await api.put(`/tac-gia/${id}`, tacGia.toJSON());
    return new TacGia(response.data);
  }

  async delete(id) {
    await api.delete(`/tac-gia/${id}`);
  }

  async search(keyword) {
    const response = await api.get('/tac-gia/search', {
      params: { keyword }
    });
    return response.data.map(item => new TacGia(item));
  }
}

export default new TacGiaService();