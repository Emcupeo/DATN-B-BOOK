import api from './api';
import KhachHang from '@/models/KhachHang';

class KhachHangService {
    async getAll() {
        const response = await api.get('/khach-hang');
        console.log(response.data);
        return Array.isArray(response.data)
            ? response.data.map(item => new KhachHang(item))
            : response.data.content.map(item => new KhachHang(item));
    }

    async getById(id) {
        const response = await api.get(`/khach-hang/${id}`);
        return new KhachHang(response.data);
    }

    async create(khachHang) {
        const response = await api.post('/khach-hang', khachHang.toJSON());
        return new KhachHang(response.data);
    }

    async update(id, khachHang) {
        const response = await api.put(`/khach-hang/${id}`, khachHang.toJSON());
        return new KhachHang(response.data);
    }

    async delete(id) {
        await api.delete(`/khach-hang/${id}`);
    }

    async search(keyword) {
        const response = await api.get('/khach-hang/search', {
            params: { keyword }
        });
        return response.data.map(item => new KhachHang(item));
    }

    async createWithAddresses(khachHangData) {
        // Convert boolean values to integers
        const convertedData = {
            ...khachHangData,
            gioiTinh: khachHangData.gioiTinh ? 1 : 0,
            trangThai: khachHangData.trangThai ? 1 : 0
        };

        const response = await api.post('/khach-hang/create-with-addresses', convertedData);
        return new KhachHang(response.data);
    }

    async updateStatus(id, trangThai) {
        const response = await api.patch(`/khach-hang/${id}/status?trangThai=${trangThai}`);
        return new KhachHang(response.data);
    }
}

export default new KhachHangService();