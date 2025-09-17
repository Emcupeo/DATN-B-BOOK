import axios from 'axios';
import { API_URL } from '@/config';

const axiosInstance = axios.create({
  baseURL: `${API_URL}/bo-sach-chi-tiet`,
  headers: {
    'Content-Type': 'application/json',
  },
});

const BoSachChiTietService = {
  // Lấy tất cả chi tiết bộ sách
  async getAll() {
    try {
      const response = await axiosInstance.get('');
      return response.data;
    } catch (error) {
      console.error('Lỗi khi lấy danh sách chi tiết bộ sách:', error);
      throw error;
    }
  },

  // Lấy chi tiết bộ sách theo ID
  async getById(id) {
    try {
      const response = await axiosInstance.get(`/${id}`);
      return response.data;
    } catch (error) {
      console.error('Lỗi khi lấy chi tiết bộ sách theo ID:', error);
      throw error;
    }
  },

  // Lấy chi tiết bộ sách theo ID bộ sách
  async getByBoSachId(boSachId) {
    try {
      const response = await axiosInstance.get(`/bo-sach/${boSachId}`);
      return response.data;
    } catch (error) {
      console.error('Lỗi khi lấy chi tiết bộ sách theo ID bộ sách:', error);
      throw error;
    }
  },

  // Tạo chi tiết bộ sách mới
  async create(data) {
    try {
      const response = await axiosInstance.post('', data);
      return response.data;
    } catch (error) {
      console.error('Lỗi khi tạo chi tiết bộ sách:', error);
      throw error;
    }
  },

  // Cập nhật chi tiết bộ sách
  async update(id, data) {
    try {
      const response = await axiosInstance.put(`/${id}`, data);
      return response.data;
    } catch (error) {
      console.error('Lỗi khi cập nhật chi tiết bộ sách:', error);
      throw error;
    }
  },

  // Xóa chi tiết bộ sách
  async delete(id) {
    try {
      const response = await axiosInstance.delete(`/${id}`);
      return response.data;
    } catch (error) {
      console.error('Lỗi khi xóa chi tiết bộ sách:', error);
      throw error;
    }
  },

  // Lấy chi tiết sản phẩm trong bộ sách với thông tin đầy đủ
  async getDetailedByBoSachId(boSachId) {
    try {
      const response = await axiosInstance.get(`/detailed/bo-sach/${boSachId}`);
      return response.data;
    } catch (error) {
      console.error('Lỗi khi lấy chi tiết đầy đủ bộ sách:', error);
      throw error;
    }
  }
};

export default BoSachChiTietService;
