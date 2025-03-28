// src/service/DotGiamGiaService.js
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/discounts';

const axiosInstance = axios.create({
  baseURL: API_URL,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
});

export default {
  async getListDiscounts() {
    try {
      const response = await axiosInstance.get('');
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể lấy danh sách đợt giảm giá!');
    }
  },

  async createDiscount(discount) {
    try {
      const response = await axiosInstance.post('', discount);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể tạo đợt giảm giá!');
    }
  },

  async updateDiscount(id, discount) {
    try {
      const response = await axiosInstance.put(`/${id}`, discount);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể cập nhật đợt giảm giá!');
    }
  },

  async deleteDiscount(id) {
    try {
      await axiosInstance.delete(`/${id}`);
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể xóa đợt giảm giá!');
    }
  },
};