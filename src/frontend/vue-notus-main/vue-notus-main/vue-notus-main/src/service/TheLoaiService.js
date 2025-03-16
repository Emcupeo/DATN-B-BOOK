import api from './api';
import TheLoai from '../models/TheLoai';

class TheLoaiService {
  async getAll() {
    const response = await api.get('/the-loai');
    return response.data.map(item => new TheLoai(item));
  }

  async getById(id) {
    const response = await api.get(`/the-loai/${id}`);
    return new TheLoai(response.data);
  }

  async create(theLoai) {
    const response = await api.post('/the-loai', theLoai.toJSON());
    return new TheLoai(response.data);
  }

  async update(id, theLoai) {
    const response = await api.put(`/the-loai/${id}`, theLoai.toJSON());
    return new TheLoai(response.data);
  }

  async delete(id) {
    await api.delete(`/the-loai/${id}`);
  }

  async search(keyword) {
    const response = await api.get('/the-loai/search', {
      params: { keyword }
    });
    return response.data.map(item => new TheLoai(item));
  }
}

export default new TheLoaiService();