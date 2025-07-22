import { reactive, computed } from 'vue'
import ShopService from '../../../service/ShopService'
import BoSachService from '../../../service/BoSachService'

// Store data
const state = reactive({
  products: [],
  categories: [],
  cart: [],
  loading: false,
  error: null
})

// Computed properties
const cartItemCount = computed(() => {
  return state.cart.reduce((total, item) => total + item.quantity, 0)
})

const cartTotal = computed(() => {
  return state.cart.reduce((total, item) => total + (item.price * item.quantity), 0)
})

// Helper function to process author information
const processAuthorInfo = (productDetailItems) => {
  if (!productDetailItems || productDetailItems.length === 0) {
    return 'Chưa có thông tin'
  }

  const authors = new Set()
  productDetailItems.forEach(item => {
    if (item.idTacGia && item.idTacGia.tenTacGia) {
      authors.add(item.idTacGia.tenTacGia)
    }
  })

  if (authors.size === 0) {
    return 'Chưa có thông tin'
  } else if (authors.size === 1) {
    return Array.from(authors)[0]
  } else {
    return 'Nhiều tác giả'
  }
}

// Helper function to get product image
const getProductImage = (productDetailItems) => {
  if (!productDetailItems || productDetailItems.length === 0) {
    console.log('No product detail items found')
    return 'https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=400'
  }

  // Lấy ảnh từ chi tiết sản phẩm đầu tiên
  const firstItem = productDetailItems[0]
  console.log('First item:', firstItem)
  console.log('AnhSanPhams:', firstItem.anhSanPhams)
  
  if (firstItem.anhSanPhams && firstItem.anhSanPhams.length > 0) {
    const firstImage = firstItem.anhSanPhams[0]
    console.log('First image:', firstImage)
    if (firstImage && firstImage.url) {
      console.log('Image URL:', firstImage.url)
      return firstImage.url
    }
  }

  console.log('Using default image')
  return 'https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=400'
}

// Helper function to get product price
const getProductPrice = (productDetailItems) => {
  if (!productDetailItems || productDetailItems.length === 0) {
    return 0
  }

  // Lấy giá thấp nhất
  const prices = productDetailItems
    .filter(item => item.gia && item.gia > 0)
    .map(item => item.gia)

  return prices.length > 0 ? Math.min(...prices) : 0
}

// Helper function to check if product is in stock
const isProductInStock = (productDetailItems) => {
  if (!productDetailItems || productDetailItems.length === 0) {
    return false
  }

  return productDetailItems.some(item => item.soLuongTon > 0)
}

// Actions
const loadProducts = async () => {
  try {
    state.loading = true
    state.error = null
    
    const products = await ShopService.getAllProducts()
    const processedProducts = []

    for (const product of products) {
      try {
        const productDetailItems = await ShopService.getProductDetailItems(product.id)
        // Lấy ngày tạo từ trường createdAt của sản phẩm
        let sortCreatedAt = 0;
        if (product.createdAt && !isNaN(new Date(product.createdAt).getTime())) {
          sortCreatedAt = new Date(product.createdAt).getTime();
        }
        processedProducts.push({
          id: product.id,
          title: product.tenSanPham,
          author: processAuthorInfo(productDetailItems),
          price: getProductPrice(productDetailItems),
          image: getProductImage(productDetailItems),
          description: product.moTa || 'Chưa có mô tả',
          category: product.danhMuc ? product.danhMuc.tenDanhMuc : 'Chưa phân loại',
          categoryId: product.danhMuc ? product.danhMuc.id : null,
          inStock: isProductInStock(productDetailItems),
          publisher: productDetailItems.length > 0 && productDetailItems[0].idNhaXuatBan 
            ? productDetailItems[0].idNhaXuatBan.tenNhaXuatBan 
            : 'Chưa có thông tin',
          language: productDetailItems.length > 0 && productDetailItems[0].idNgonNgu 
            ? productDetailItems[0].idNgonNgu.tenNgonNgu 
            : 'Chưa có thông tin',
          format: productDetailItems.length > 0 && productDetailItems[0].idLoaiBia 
            ? productDetailItems[0].idLoaiBia.tenLoaiBia 
            : 'Chưa có thông tin',
          pages: 0, // Chưa có trường này trong database
          publishedDate: 'Chưa có thông tin', // Chưa có trường này trong database
          sales: 0, // Chưa có trường này trong database
          productDetailItems: productDetailItems, // Lưu chi tiết sản phẩm để sắp xếp
          sortCreatedAt
        })
      } catch (error) {
        console.error(`Error processing product ${product.id}:`, error)
        // Vẫn thêm sản phẩm với thông tin cơ bản
        processedProducts.push({
          id: product.id,
          title: product.tenSanPham,
          author: 'Chưa có thông tin',
          price: 0,
          image: 'https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=400',
          description: product.moTa || 'Chưa có mô tả',
          category: product.danhMuc ? product.danhMuc.tenDanhMuc : 'Chưa phân loại',
          categoryId: product.danhMuc ? product.danhMuc.id : null,
          inStock: false,
          publisher: 'Chưa có thông tin',
          language: 'Chưa có thông tin',
          format: 'Chưa có thông tin',
          pages: 0,
          publishedDate: 'Chưa có thông tin',
          sales: 0,
          productDetailItems: [],
          sortCreatedAt: 0
        })
      }
    }

    // Lấy danh sách bộ sách và thêm vào processedProducts
    try {
      const boSachs = await BoSachService.getAll();
      for (const boSach of boSachs) {
        // Lấy chi tiết bộ sách
        let chiTietList = [];
        try {
          chiTietList = await BoSachService.getBoSachChiTietByBoSachId(boSach.id);
        } catch (e) { chiTietList = []; }
        // Lấy danh sách tác giả từ các chi tiết sách
        const tacGiaSet = new Set();
        for (const chiTiet of chiTietList) {
          if (chiTiet.idChiTietSanPham && chiTiet.idChiTietSanPham.idTacGia && chiTiet.idChiTietSanPham.idTacGia.tenTacGia) {
            tacGiaSet.add(chiTiet.idChiTietSanPham.idTacGia.tenTacGia);
          }
        }
        let author = 'Chưa có thông tin';
        if (tacGiaSet.size === 1) author = Array.from(tacGiaSet)[0];
        else if (tacGiaSet.size > 1) author = 'Nhiều tác giả';
        // Map bộ sách về object hiển thị
        let sortCreatedAt = 0;
        if (boSach.createdAt && !isNaN(new Date(boSach.createdAt).getTime())) {
          sortCreatedAt = new Date(boSach.createdAt).getTime();
        } else {
          sortCreatedAt = 0;
        }
        console.log('[DEBUG] BoSach', boSach.id, 'createdAt:', boSach.createdAt, '-> sortCreatedAt:', sortCreatedAt);
        processedProducts.push({
          id: boSach.id,
          title: boSach.tenBoSach,
          author,
          price: boSach.giaTien,
          image: boSach.url || 'https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=400',
          description: boSach.moTa || 'Chưa có mô tả',
          category: 'Bộ sách',
          categoryId: null,
          inStock: boSach.soLuong > 0,
          publisher: '',
          language: '',
          format: '',
          pages: 0,
          publishedDate: '',
          sales: 0,
          createdAt: boSach.createdAt,
          sortCreatedAt
        });
      }
    } catch (e) {
      console.error('Lỗi khi tải bộ sách:', e);
    }

    state.products = processedProducts
    // Sắp xếp theo sortCreatedAt giảm dần, đan xen bộ sách và sản phẩm
    state.products.sort((a, b) => b.sortCreatedAt - a.sortCreatedAt);
    // Log kiểm tra thứ tự hiển thị
    console.log('[DEBUG] Thứ tự hiển thị (title, sortCreatedAt, type):',
      state.products.map(item => ({
        title: item.title,
        sortCreatedAt: item.sortCreatedAt,
        type: item.category
      }))
    );
  } catch (error) {
    console.error('Error loading products:', error)
    state.error = 'Không thể tải danh sách sản phẩm'
  } finally {
    state.loading = false
  }
}

const loadCategories = async () => {
  try {
    const categories = await ShopService.getAllCategories()
    state.categories = categories
  } catch (error) {
    console.error('Error loading categories:', error)
    state.error = 'Không thể tải danh mục'
  }
}

const addToCart = (product) => {
  const existingItem = state.cart.find(item => item.id === product.id)
  if (existingItem) {
    existingItem.quantity++
  } else {
    state.cart.push({ ...product, quantity: 1 })
  }
}

const removeFromCart = (productId) => {
  state.cart = state.cart.filter(item => item.id !== productId)
}

const updateCartQuantity = ({ productId, quantity }) => {
  const item = state.cart.find(item => item.id === productId)
  if (item) {
    item.quantity = quantity
  }
}

const clearCart = () => {
  state.cart = []
}

const getProductById = (id) => {
  return state.products.find(product => product.id === parseInt(id))
}

const getProductsByCategory = async (categoryId) => {
  if (!categoryId) return state.products
  try {
    const products = await ShopService.getProductsByCategory(categoryId)
    const processedProducts = []

    for (const product of products) {
      try {
        const productDetailItems = await ShopService.getProductDetailItems(product.id)
        console.log(`Product ${product.id} - ${product.tenSanPham}:`, productDetailItems)
        
        processedProducts.push({
          id: product.id,
          title: product.tenSanPham,
          author: processAuthorInfo(productDetailItems),
          price: getProductPrice(productDetailItems),
          image: getProductImage(productDetailItems),
          description: product.moTa || 'Chưa có mô tả',
          category: product.danhMuc ? product.danhMuc.tenDanhMuc : 'Chưa phân loại',
          categoryId: product.danhMuc ? product.danhMuc.id : null,
          inStock: isProductInStock(productDetailItems),
          publisher: productDetailItems.length > 0 && productDetailItems[0].idNhaXuatBan 
            ? productDetailItems[0].idNhaXuatBan.tenNhaXuatBan 
            : 'Chưa có thông tin',
          language: productDetailItems.length > 0 && productDetailItems[0].idNgonNgu 
            ? productDetailItems[0].idNgonNgu.tenNgonNgu 
            : 'Chưa có thông tin',
          format: productDetailItems.length > 0 && productDetailItems[0].idLoaiBia 
            ? productDetailItems[0].idLoaiBia.tenLoaiBia 
            : 'Chưa có thông tin',
          pages: 0,
          publishedDate: 'Chưa có thông tin',
          sales: 0,
          productDetailItems: productDetailItems // Lưu chi tiết sản phẩm để sắp xếp
        })
      } catch (error) {
        console.error(`Error processing product ${product.id}:`, error)
        processedProducts.push({
          id: product.id,
          title: product.tenSanPham,
          author: 'Chưa có thông tin',
          price: 0,
          image: 'https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=400',
          description: product.moTa || 'Chưa có mô tả',
          category: product.danhMuc ? product.danhMuc.tenDanhMuc : 'Chưa phân loại',
          categoryId: product.danhMuc ? product.danhMuc.id : null,
          inStock: false,
          publisher: 'Chưa có thông tin',
          language: 'Chưa có thông tin',
          format: 'Chưa có thông tin',
          pages: 0,
          publishedDate: 'Chưa có thông tin',
          sales: 0,
          productDetailItems: [] // Không có chi tiết sản phẩm
        })
      }
    }

    return processedProducts
  } catch (error) {
    console.error('Error fetching products by category:', error)
    return []
  }
}

// Export store
export const useRealDataStore = () => {
  return {
    // State
    products: computed(() => state.products),
    categories: computed(() => state.categories),
    cart: computed(() => state.cart),
    loading: computed(() => state.loading),
    error: computed(() => state.error),
    
    // Computed
    cartItemCount,
    cartTotal,
    
    // Actions
    loadProducts,
    loadCategories,
    addToCart,
    removeFromCart,
    updateCartQuantity,
    clearCart,
    getProductById,
    getProductsByCategory
  }
} 