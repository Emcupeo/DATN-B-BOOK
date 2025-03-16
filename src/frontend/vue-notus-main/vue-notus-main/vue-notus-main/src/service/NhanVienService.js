import api from './api';
import NhanVien from '../models/NhanVien';

class NhanVienService {
    async getAll() {
        const response = await api.get('/nhan-vien');
        return response.data.map(item => new NhanVien(item));
    }

    async getById(id) {
        const response = await api.get(`/nhan-vien/${id}`);
        return new NhanVien(response.data);
    }

    async create(nhanVien) {
        const response = await api.post('/nhan-vien', nhanVien.toJSON());
        return new NhanVien(response.data);
    }

    async update(id, nhanVien) {
        const response = await api.put(`/nhan-vien/${id}`, nhanVien.toJSON());
        return new NhanVien(response.data);
    }

    async delete(id) {
        await api.delete(`/nhan-vien/${id}`);
    }

    async search(keyword) {
        const response = await api.get('/nhan-vien/search', {
            params: { keyword }
        });
        return response.data.map(item => new NhanVien(item));
    }
}

export default new NhanVienService();