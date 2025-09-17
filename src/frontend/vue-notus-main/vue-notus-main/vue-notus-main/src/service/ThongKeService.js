import api from './api'

// Transform dữ liệu sản phẩm từ DTO (đã có thông tin giảm giá)
const transformProduct = (product) => {
  console.log('🔍 Transforming product from DTO:', product)
  
  // DTO đã có đầy đủ thông tin, chỉ cần map lại
  const transformed = {
    id: product.id,
    title: product.title || 'Không có tên',
    author: product.author || 'Không có tác giả',
    price: parseFloat(product.price) || 0,
    originalPrice: parseFloat(product.originalPrice) || 0,
    image: product.image || 'https://via.placeholder.com/300x400?text=No+Image',
    category: product.category || 'Sách lẻ',
    publisher: product.publisher || 'NXB Trẻ',
    language: product.language || 'Tiếng Việt',
    format: product.format || 'Bìa mềm',
    pages: parseInt(product.pages) || 300,
    soLuongTon: parseInt(product.stockQuantity) || 0,
    inStock: product.inStock || false,
    stockQuantity: parseInt(product.stockQuantity) || 0,
    // Thêm các thuộc tính khác nếu cần
    maChiTietSanPham: product.productCode,
    moTa: product.description || '',
    // Thêm thuộc tính để phân biệt sản phẩm lẻ và bộ sách
    isBookSet: product.isBookSet || false,
    // Thông tin giảm giá
    hasDiscount: product.hasDiscount || false,
    discountAmount: parseFloat(product.discountAmount) || 0,
    discountPercentage: product.discountPercentage ? parseFloat(product.discountPercentage) : 0
  }
  
  console.log('🔍 Transformed result:', transformed)
  return transformed
}

// Transform dữ liệu bộ sách từ DTO (đã có thông tin giảm giá)
const transformBookSet = (bookSet) => {
  console.log('🔍 Transforming book set from DTO:', bookSet)
  
  // DTO đã có đầy đủ thông tin, chỉ cần map lại
  const transformed = {
    id: bookSet.id,
    title: bookSet.title || 'Không có tên',
    author: bookSet.author || 'Nhiều tác giả',
    price: parseFloat(bookSet.price) || 0,
    originalPrice: parseFloat(bookSet.originalPrice) || 0,
    image: bookSet.image || 'https://via.placeholder.com/300x400?text=No+Image',
    category: bookSet.category || 'Bộ sách',
    publisher: bookSet.publisher || 'Nhiều NXB',
    language: bookSet.language || 'Tiếng Việt',
    format: bookSet.format || 'Bộ sách',
    pages: bookSet.pages || 'Nhiều tập',
    soLuongTon: parseInt(bookSet.stockQuantity) || 0,
    inStock: bookSet.inStock || false,
    stockQuantity: parseInt(bookSet.stockQuantity) || 0,
    // Thêm các thuộc tính khác nếu cần
    maBoSach: bookSet.productCode,
    moTa: bookSet.description || '',
    // Thêm thuộc tính để phân biệt sản phẩm lẻ và bộ sách
    isBookSet: bookSet.isBookSet || true,
    // Thông tin giảm giá
    hasDiscount: bookSet.hasDiscount || false,
    discountAmount: parseFloat(bookSet.discountAmount) || 0,
    discountPercentage: bookSet.discountPercentage ? parseFloat(bookSet.discountPercentage) : 0
  }
  
  console.log('🔍 Transformed book set result:', transformed)
  return transformed
}

export default {
  // Lấy sách mới nhất
  async getSachMoiNhat(limit = 5) {
    try {
      const response = await api.get('/thong-ke/sach-moi-nhat', {
        params: { limit }
      })
      console.log('🔍 Raw data from API:', response.data)
      const transformed = response.data.map(transformProduct)
      console.log('🔍 Transformed data:', transformed)
      return transformed
    } catch (error) {
      console.error('Lỗi khi lấy sách mới nhất:', error)
      throw error
    }
  },

  // Lấy bộ sách mới nhất
  async getBoSachMoiNhat(limit = 5) {
    try {
      const response = await api.get('/thong-ke/bo-sach-moi-nhat', {
        params: { limit }
      })
      console.log('🔍 Raw book set data from API:', response.data)
      const transformed = response.data.map(transformBookSet)
      console.log('🔍 Transformed book set data:', transformed)
      return transformed
    } catch (error) {
      console.error('Lỗi khi lấy bộ sách mới nhất:', error)
      throw error
    }
  },

  // Lấy sách bán chạy nhất
  async getSachBanChay(limit = 5) {
    try {
      const response = await api.get('/thong-ke/sach-ban-chay', {
        params: { limit }
      })
      console.log('🔍 Raw bestseller data from API:', response.data)
      const transformed = response.data.map(transformProduct)
      console.log('🔍 Transformed bestseller data:', transformed)
      return transformed
    } catch (error) {
      console.error('Lỗi khi lấy sách bán chạy:', error)
      throw error
    }
  },

  // Lấy bộ sách bán chạy nhất
  async getBoSachBanChay(limit = 5) {
    try {
      const response = await api.get('/thong-ke/bo-sach-ban-chay', {
        params: { limit }
      })
      console.log('🔍 Raw book set bestseller data from API:', response.data)
      const transformed = response.data.map(transformBookSet)
      console.log('🔍 Transformed book set bestseller data:', transformed)
      return transformed
    } catch (error) {
      console.error('Lỗi khi lấy bộ sách bán chạy:', error)
      throw error
    }
  }
}
