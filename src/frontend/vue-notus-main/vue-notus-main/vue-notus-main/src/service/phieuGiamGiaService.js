import axios from 'axios';

const API_URL = 'http://localhost:8080/api/admin/phieu-giam-gia';

const axiosInstance = axios.create({
  baseURL: API_URL,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
});

export default {
  // Lấy danh sách phiếu giảm giá với phân trang và filter
  async getAll(params = {}) {
    try {
      const response = await axiosInstance.get('', { params });
      return response;
    } catch (error) {
      console.error('Lỗi khi lấy danh sách phiếu giảm giá:', error);
      throw new Error(error.response?.data?.message || 'Không thể lấy danh sách phiếu giảm giá!');
    }
  },

  // Lấy phiếu giảm giá theo ID
  async getById(id) {
    try {
      const response = await axiosInstance.get(`/${id}`);
      return response.data;
    } catch (error) {
      console.error('Lỗi khi lấy phiếu giảm giá:', error);
      throw new Error(error.response?.data?.message || 'Không thể lấy phiếu giảm giá!');
    }
  },

  // Tạo phiếu giảm giá mới
  async create(data) {
    try {
      const response = await axiosInstance.post('', data);
      return response.data;
    } catch (error) {
      console.error('Lỗi khi tạo phiếu giảm giá:', error);
      throw new Error(error.response?.data?.message || 'Không thể tạo phiếu giảm giá!');
    }
  },

  // Cập nhật phiếu giảm giá
  async update(id, data) {
    try {
      const response = await axiosInstance.put(`/${id}`, data);
      return response.data;
    } catch (error) {
      console.error('Lỗi khi cập nhật phiếu giảm giá:', error);
      throw new Error(error.response?.data?.message || 'Không thể cập nhật phiếu giảm giá!');
    }
  },

  // Xóa phiếu giảm giá
  async delete(id) {
    try {
      const response = await axiosInstance.delete(`/${id}`);
      return response.data;
    } catch (error) {
      console.error('Lỗi khi xóa phiếu giảm giá:', error);
      throw new Error(error.response?.data?.message || 'Không thể xóa phiếu giảm giá!');
    }
  },

  // Trừ số lượng voucher sau thanh toán
  async deductVoucherQuantity(voucherId) {
    try {
      // Lấy thông tin voucher hiện tại
      const getResponse = await axiosInstance.get(`/${voucherId}`);
      const currentVoucher = getResponse.data;
      
      // Cập nhật số lượng (giảm 1)
      const updateResponse = await axiosInstance.put(`/${voucherId}`, {
        ...currentVoucher,
        soLuong: currentVoucher.soLuong - 1
      });
      
      return updateResponse.data;
    } catch (error) {
      console.error('Lỗi khi trừ số lượng voucher:', error);
      throw new Error(error.response?.data?.message || 'Không thể trừ số lượng voucher!');
    }
  }
};