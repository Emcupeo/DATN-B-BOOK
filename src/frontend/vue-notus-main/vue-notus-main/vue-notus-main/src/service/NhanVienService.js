import axios from 'axios';
import { API_URL } from '../config';
import NhanVien from '../models/NhanVien';

class NhanVienService {
    async getAll() {
        try {
            const response = await axios.get(`${API_URL}/nhan-vien`);
            return response.data.map(item => new NhanVien(item));
        } catch (error) {
            console.error('Error fetching nhan vien:', error);
            throw error;
        }
    }

    async getById(id) {
        try {
            const response = await axios.get(`${API_URL}/nhan-vien/${id}`);
            return new NhanVien(response.data);
        } catch (error) {
            console.error('Error fetching nhan vien by id:', error);
            throw error;
        }
    }

    async toggleStatus(id) {
        const response = await axios.put(`${API_URL}/nhan-vien/${id}/toggle-status`);
        return new NhanVien(response.data);
    }

    async create(nhanVien) {
        try {
            const response = await axios.post(`${API_URL}/nhan-vien`, nhanVien);
            return new NhanVien(response.data);
        } catch (error) {
            console.error('Error creating nhan vien:', error);
            throw error;
        }
    }

    async update(id, nhanVien) {
        try {
            const response = await axios.put(`${API_URL}/nhan-vien/${id}`, nhanVien);
            return new NhanVien(response.data);
        } catch (error) {
            console.error('Error updating nhan vien:', error);
            throw error;
        }
    }

    async delete(id) {
        try {
            const response = await axios.delete(`${API_URL}/nhan-vien/${id}`);
            return response.data;
        } catch (error) {
            console.error('Error deleting nhan vien:', error);
            throw error;
        }
    }

    async search(keyword) {
        try {
            const response = await axios.get(`${API_URL}/nhan-vien/search?keyword=${keyword}`);
            return response.data.map(item => new NhanVien(item));
        } catch (error) {
            console.error('Error searching nhan vien:', error);
            throw error;
        }
    }

    async updateStatus(id) {
        try {
            const response = await axios.put(`${API_URL}/nhan-vien/${id}/status`);
            return response.data;
        } catch (error) {
            console.error('Error updating nhan vien status:', error);
            throw error;
        }
    }
}

export default new NhanVienService();