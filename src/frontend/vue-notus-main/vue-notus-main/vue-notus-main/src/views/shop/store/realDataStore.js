import { reactive, computed } from 'vue'
import ShopService from '../../../service/ShopService'

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
        console.log(`Category Product ${product.id} - ${product.tenSanPham}:`, productDetailItems)
        
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
          sales: 0 // Chưa có trường này trong database
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
          sales: 0
        })
      }
    }

    state.products = processedProducts
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
          sales: 0
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
          sales: 0
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