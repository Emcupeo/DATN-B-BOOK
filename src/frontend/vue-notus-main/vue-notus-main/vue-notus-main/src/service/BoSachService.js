import api from './api';
import BoSach from '@/models/BoSach';

class BoSachService {
    async getAll() {
        try {
            const response = await api.get('/bo-sach');
            console.log('Response từ GET /api/bo-sach:', response.data);
            return response.data.map(item => new BoSach(item));
        } catch (error) {
            console.error('Lỗi khi gọi /api/bo-sach:', error.response || error);
            throw error;
        }
    }

    async getById(id) {
        try {
            const response = await api.get(`/bo-sach/${id}`);
            return new BoSach(response.data);
        } catch (error) {
            console.error('Lỗi khi gọi /bo-sach/${id}:', error.response || error);
            throw error;
        }
    }

    async create(boSach) {
        try {
            const response = await api.post('/bo-sach', boSach);
            return new BoSach(response.data);
        } catch (error) {
            console.error('Lỗi khi tạo bộ sách:', error.response || error);
            throw error;
        }
    }

    async delete(id) {
        try {
            await api.delete(`/bo-sach/${id}`);
        } catch (error) {
            console.error('Lỗi khi xóa /bo-sach/${id}:', error.response || error);
            throw error;
        }
    }

    async search(keyword) {
        try {
            const response = await api.get('/bo-sach/search', {
                params: { keyword }
            });
            return response.data.map(item => new BoSach(item));
        } catch (error) {
            console.error('Lỗi khi tìm kiếm /bo-sach/search:', error);
            throw error;
        }
    }
    async getChiTietSanPhams() {
        try {
            const response = await api.get('/chi-tiet-san-pham/all');
            return response.data;
        } catch (error) {
            console.error('Lỗi khi lấy danh sách chi tiết sản phẩm:', error.response || error);
            throw error;
        }
    }
}

export default new BoSachService();