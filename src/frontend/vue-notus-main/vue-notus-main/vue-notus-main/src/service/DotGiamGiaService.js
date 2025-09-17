import axios from 'axios';

const API_URL = 'http://localhost:8080/api/discounts';
const PRODUCT_API_URL = 'http://localhost:8080/api/chi-tiet-san-pham';
const SAN_PHAM_API_URL = 'http://localhost:8080/api/san-pham';
const BO_SACH_API_URL = 'http://localhost:8080/api/bo-sach';

const axiosInstance = axios.create({
  baseURL: API_URL,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
});

const productAxiosInstance = axios.create({
  baseURL: PRODUCT_API_URL,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
});

const sanPhamAxiosInstance = axios.create({
  baseURL: SAN_PHAM_API_URL,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
});

const boSachAxiosInstance = axios.create({
  baseURL: BO_SACH_API_URL,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
});

export default {
  async getListDiscounts() {
    try {
      const response = await axiosInstance.get('');
      console.log('DEBUG: Full axios response:', response);
      console.log('DEBUG: Response data:', response.data);
      console.log('DEBUG: Response data type:', typeof response.data);
      console.log('DEBUG: Response data is array:', Array.isArray(response.data));
      
      // Fix: Parse JSON string if needed
      let data = response.data;
      if (typeof data === 'string') {
        console.log('DEBUG: Parsing JSON string...');
        console.log('DEBUG: String length:', data.length);
        console.log('DEBUG: First 500 chars:', data.substring(0, 500));
        console.log('DEBUG: Last 500 chars:', data.substring(data.length - 500));
        
        try {
          data = JSON.parse(data);
          console.log('DEBUG: Parsed data:', data);
          console.log('DEBUG: Parsed data type:', typeof data);
          console.log('DEBUG: Parsed data is array:', Array.isArray(data));
        } catch (parseError) {
          console.error('DEBUG: JSON parse error:', parseError);
          console.error('DEBUG: Problematic JSON string:', data);
          console.error('DEBUG: String length:', data.length);
          
          // Try to find where the JSON breaks
          const errorIndex = parseError.message.match(/position (\d+)/);
          if (errorIndex) {
            const pos = parseInt(errorIndex[1]);
            console.error('DEBUG: Error at position:', pos);
            console.error('DEBUG: Context around error:', data.substring(Math.max(0, pos - 100), pos + 100));
          }
          
          throw new Error('Không thể parse dữ liệu từ server! JSON không hợp lệ.');
        }
      }
      
      return data;
    } catch (error) {
      console.error('DEBUG: Error in getListDiscounts:', error);
      throw new Error(error.response?.data?.message || 'Không thể lấy danh sách đợt giảm giá!');
    }
  },

  async getDiscountById(id) {
    try {
      const response = await axiosInstance.get(`/${id}`);
      
      // Fix: Parse JSON string if needed
      let data = response.data;
      if (typeof data === 'string') {
        try {
          data = JSON.parse(data);
        } catch (parseError) {
          console.error('DEBUG: JSON parse error in getDiscountById:', parseError);
          throw new Error('Không thể parse dữ liệu từ server!');
        }
      }
      
      return data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể lấy chi tiết đợt giảm giá!');
    }
  },

  async getDiscountProducts(id) {
    try {
      const response = await axiosInstance.get(`/${id}/products`);
      
      // Fix: Parse JSON string if needed
      let data = response.data;
      if (typeof data === 'string') {
        try {
          data = JSON.parse(data);
        } catch (parseError) {
          console.error('DEBUG: JSON parse error in getDiscountProducts:', parseError);
          throw new Error('Không thể parse dữ liệu từ server!');
        }
      }
      
      return data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể lấy danh sách sản phẩm của đợt giảm giá!');
    }
  },

  async getDiscountBookSets(id) {
    try {
      const response = await axiosInstance.get(`/${id}/book-sets`);
      
      // Fix: Parse JSON string if needed
      let data = response.data;
      if (typeof data === 'string') {
        try {
          data = JSON.parse(data);
        } catch (parseError) {
          console.error('DEBUG: JSON parse error in getDiscountBookSets:', parseError);
          throw new Error('Không thể parse dữ liệu từ server!');
        }
      }
      
      return data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể lấy danh sách bộ sách của đợt giảm giá!');
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

  async getActiveDetail(ctspId) {
    try {
      const response = await axiosInstance.get(`/active-detail/${ctspId}`)
      return response.data
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể lấy thông tin giảm giá!');
    }
  },

  async getActiveBoSachDetail(boSachId) {
    try {
      const response = await axiosInstance.get(`/active-bosach-detail/${boSachId}`)
      return response.data
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể lấy thông tin giảm giá bộ sách!');
    }
  },

  async getAllProducts() {
    try {
      const response = await productAxiosInstance.get('/all');
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể lấy danh sách sản phẩm!');
    }
  },

  async getProductById(id) {
    try {
      const response = await productAxiosInstance.get(`/${id}`);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể lấy chi tiết sản phẩm!');
    }
  },

 async getAllSanPham() {
  try {
    const response = await sanPhamAxiosInstance.get('');
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || 'Không thể lấy danh sách sản phẩm từ bảng sanpham!');
  }
},

  async getChiTietSanPhamBySanPhamId(idSanPham) {
    try {
      const response = await productAxiosInstance.get(`/by-san-pham/${idSanPham}`);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể lấy chi tiết sản phẩm theo sản phẩm!');
    }
  },

  async getAllBoSach() {
    try {
      const response = await boSachAxiosInstance.get('');
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể lấy danh sách bộ sách!');
    }
  },

  // Lấy danh sách voucher khả dụng cho POS
  async getAvailableVouchersForPos(totalAmount = null) {
    try {
      // Sử dụng API phieu-giam-gia thay vì discounts
      const response = await axios.get('http://localhost:8080/api/admin/phieu-giam-gia', {
        params: {
          loaiApDung: 'PUBLIC', // Chỉ lấy voucher công khai
          trangThai: 'true',
          page: 0,
          size: 100
        }
      });
      
      // Lọc theo điều kiện
      let vouchers = response.data.content || [];
      const now = new Date();
      
      vouchers = vouchers.filter(voucher => {
        // Kiểm tra loại áp dụng
        if (voucher.loaiApDung !== 'PUBLIC') {
          return false;
        }
        
        // Kiểm tra thời gian hoạt động
        const startDate = new Date(voucher.ngayBatDau);
        const endDate = new Date(voucher.ngayKetThuc);
        if (now < startDate || now > endDate) {
          return false;
        }
        
        // Kiểm tra số lượng còn lại
        if (!voucher.soLuong || voucher.soLuong <= 0) {
          return false;
        }
        
        // Kiểm tra giá trị đơn hàng tối thiểu
        if (totalAmount && voucher.giaTriDonHangToiThieu && totalAmount < voucher.giaTriDonHangToiThieu) {
          return false;
        }
        
        return true;
      });
      
      return vouchers;
    } catch (error) {
      console.error('Lỗi khi lấy danh sách voucher:', error);
      throw new Error(error.response?.data?.message || 'Không thể lấy danh sách voucher!');
    }
  },

  // Lấy voucher cá nhân của khách hàng
  async getPersonalVouchersForCustomer(customerId, totalAmount = null) {
    try {
      // Sử dụng API phieu-giam-gia với filter theo khách hàng
      const response = await axios.get('http://localhost:8080/api/admin/phieu-giam-gia', {
        params: {
          loaiApDung: 'CUSTOMER', // Voucher cá nhân
          trangThai: 'true',
          page: 0,
          size: 100
        }
      });
      
      // Lọc theo điều kiện
      let vouchers = response.data.content || [];
      const now = new Date();
      
      vouchers = vouchers.filter(voucher => {
        // Kiểm tra loại áp dụng
        if (voucher.loaiApDung !== 'CUSTOMER') {
          return false;
        }
        
        // Kiểm tra khách hàng
        if (voucher.khachHangId !== customerId) {
          return false;
        }
        
        // Kiểm tra thời gian hoạt động
        const startDate = new Date(voucher.ngayBatDau);
        const endDate = new Date(voucher.ngayKetThuc);
        if (now < startDate || now > endDate) {
          return false;
        }
        
        // Kiểm tra số lượng còn lại
        if (!voucher.soLuong || voucher.soLuong <= 0) {
          return false;
        }
        
        // Kiểm tra giá trị đơn hàng tối thiểu
        if (totalAmount && voucher.giaTriDonHangToiThieu && totalAmount < voucher.giaTriDonHangToiThieu) {
          return false;
        }
        
        return true;
      });
      
      return vouchers;
    } catch (error) {
      console.error('Lỗi khi lấy voucher cá nhân:', error);
      throw new Error(error.response?.data?.message || 'Không thể lấy voucher cá nhân!');
    }
  },

  // Trừ số lượng voucher sau thanh toán
  async deductVoucherQuantity(voucherId) {
    try {
      // Sử dụng API phieu-giam-gia để cập nhật số lượng
      const response = await axios.put(`http://localhost:8080/api/admin/phieu-giam-gia/${voucherId}`, {
        soLuong: -1 // Giảm 1
      });
      return response.data;
    } catch (error) {
      console.error('Lỗi khi trừ số lượng voucher:', error);
      throw new Error(error.response?.data?.message || 'Không thể trừ số lượng voucher!');
    }
  },
};