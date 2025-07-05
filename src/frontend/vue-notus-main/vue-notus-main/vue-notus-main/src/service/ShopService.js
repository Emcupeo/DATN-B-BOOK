import api from './api';

class ShopService {
  // Lấy tất cả sản phẩm với thông tin chi tiết
  async getAllProducts() {
    try {
      const response = await api.get('/san-pham');
      return response.data;
    } catch (error) {
      console.error('Error fetching products:', error);
      throw error;
    }
  }

  // Lấy tất cả danh mục
  async getAllCategories() {
    try {
      const response = await api.get('/danh-muc/active');
      return response.data;
    } catch (error) {
      console.error('Error fetching categories:', error);
      throw error;
    }
  }

  // Lấy sản phẩm theo danh mục
  async getProductsByCategory(categoryId) {
    try {
      const response = await api.get(`/san-pham/by-category?danhMucId=${categoryId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching products by category:', error);
      throw error;
    }
  }

  // Lấy chi tiết sản phẩm để xử lý tác giả
  async getProductDetails(productId) {
    try {
      const response = await api.get(`/san-pham/${productId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching product details:', error);
      throw error;
    }
  }

  // Lấy tất cả chi tiết sản phẩm của một sản phẩm
  async getProductDetailItems(productId) {
    try {
      const response = await api.get(`/chi-tiet-san-pham/by-san-pham/${productId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching product detail items:', error);
      throw error;
    }
  }
}

export default new ShopService(); 