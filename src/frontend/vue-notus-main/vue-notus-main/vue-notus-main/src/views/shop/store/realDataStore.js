import { reactive, computed, watch } from 'vue'
import ShopService from '../../../service/ShopService'
import DotGiamGiaService from '../../../service/DotGiamGiaService'
import BoSachService from '../../../service/BoSachService'
import SalesService from '../../../service/SalesService'

// Store data
const state = reactive({
  products: [],
  categories: [],
  cart: [], // Giỏ hàng chính cho "Thêm vào giỏ"
  buyNowCart: [], // Giỏ hàng riêng cho "Mua ngay"
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

// Computed properties cho Buy Now Cart
const buyNowCartItemCount = computed(() => {
  return state.buyNowCart.reduce((total, item) => total + item.quantity, 0)
})

const buyNowCartTotal = computed(() => {
  return state.buyNowCart.reduce((total, item) => total + (item.price * item.quantity), 0)
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

// Tính giá hiển thị và giá gốc (nếu đang giảm) dựa theo chi tiết sản phẩm
const getMinAdjustedPrices = async (productDetailItems) => {
  if (!productDetailItems || productDetailItems.length === 0) {
    return { price: 0, originalPrice: 0 }
  }
  // Gọi song song để lấy giảm giá cho từng CTSP (nếu có)
  const adjusted = await Promise.all(productDetailItems.map(async (it) => {
    try {
      const detail = await DotGiamGiaService.getActiveDetail(it.id)
      if (detail && detail.giaBanDau && detail.giaSauGiam) {
        return { effectivePrice: Number(detail.giaSauGiam), originalPrice: Number(detail.giaBanDau) }
      }
      return { effectivePrice: Number(it.gia) || 0, originalPrice: Number(it.gia) || 0 }
    } catch (_) {
      return { effectivePrice: Number(it.gia) || 0, originalPrice: Number(it.gia) || 0 }
    }
  }))
  // Lấy mức giá hiển thị thấp nhất và giá gốc tương ứng (để gạch ngang)
  let min = { effectivePrice: Infinity, originalPrice: 0 }
  for (const x of adjusted) {
    if (x.effectivePrice < min.effectivePrice) {
      min = x
    }
  }
  if (!isFinite(min.effectivePrice)) {
    return { price: 0, originalPrice: 0 }
  }
  return { price: min.effectivePrice, originalPrice: min.originalPrice }
}

// Helper function to check if product is in stock
const isProductInStock = (productDetailItems) => {
  if (!productDetailItems || productDetailItems.length === 0) {
    return false
  }

  return productDetailItems.some(item => item.soLuongTon > 0)
}

// Helper to sum total stock quantity
const getTotalStockQuantity = (productDetailItems) => {
  if (!productDetailItems || productDetailItems.length === 0) return 0
  return productDetailItems.reduce((sum, item) => sum + (Number(item.soLuongTon) || 0), 0)
}

// Helper to update sales data for products
const updateSalesData = async (processedProducts) => {
  try {
    const salesData = await SalesService.getSanPhamBanChay()
    const salesMap = new Map()
    
    // Tạo map để tra cứu nhanh dữ liệu bán chạy
    salesData.forEach(item => {
      if (item.loai === 'Sản phẩm lẻ') {
        salesMap.set(`product_${item.id}`, item.soLuongBan || 0)
      } else if (item.loai === 'Bộ sách') {
        salesMap.set(`bosach_${item.id}`, item.soLuongBan || 0)
      }
    })
    
    // Cập nhật sales cho từng sản phẩm
    processedProducts.forEach(product => {
      if (product.category === 'Bộ sách') {
        product.sales = salesMap.get(`bosach_${product.id}`) || 0
      } else {
        // Tính tổng sales cho sản phẩm từ các chi tiết sản phẩm
        let totalSales = 0
        if (product.productDetailItems) {
          for (const item of product.productDetailItems) {
            const itemSales = salesMap.get(`product_${item.id}`) || 0
            totalSales += itemSales
          }
        }
        product.sales = totalSales
      }
    })
  } catch (error) {
    console.error('Error updating sales data:', error)
  }
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
        const { price: adjustedPrice, originalPrice } = await getMinAdjustedPrices(productDetailItems)
        // Lấy ngày tạo từ trường createdAt của sản phẩm
        let sortCreatedAt = 0;
        if (product.createdAt && !isNaN(new Date(product.createdAt).getTime())) {
          sortCreatedAt = new Date(product.createdAt).getTime();
        }
        processedProducts.push({
          id: product.id,
          title: product.tenSanPham,
          author: processAuthorInfo(productDetailItems),
          price: adjustedPrice,
          originalPrice,
          image: getProductImage(productDetailItems),
          description: product.moTa || 'Chưa có mô tả',
          category: product.danhMuc ? product.danhMuc.tenDanhMuc : 'Chưa phân loại',
          categoryId: product.danhMuc ? product.danhMuc.id : null,
          inStock: isProductInStock(productDetailItems),
          stockQuantity: getTotalStockQuantity(productDetailItems),
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
          stockQuantity: 0,
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
        } catch (e) { // eslint-disable-next-line no-empty
          chiTietList = [];
        }
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
        // Lấy thông tin giảm giá cho bộ sách
        let discountInfo = null;
        try {
          discountInfo = await DotGiamGiaService.getActiveBoSachDetail(boSach.id);
        } catch (e) {
          // Không có giảm giá hoặc lỗi
          discountInfo = null;
        }

        // Map bộ sách về object hiển thị
        let sortCreatedAt = 0;
        if (boSach.createdAt && !isNaN(new Date(boSach.createdAt).getTime())) {
          sortCreatedAt = new Date(boSach.createdAt).getTime();
        } else {
          sortCreatedAt = 0;
        }
        console.log('[DEBUG] BoSach', boSach.id, 'createdAt:', boSach.createdAt, '-> sortCreatedAt:', sortCreatedAt);
        
        // Xác định giá hiển thị và giá gốc
        let displayPrice = boSach.giaTien;
        let originalPrice = null;
        
        if (discountInfo && discountInfo.giaBanDau && discountInfo.giaSauGiam) {
          displayPrice = discountInfo.giaSauGiam;
          originalPrice = discountInfo.giaBanDau;
        }
        
        processedProducts.push({
          id: boSach.id,
          title: boSach.tenBoSach,
          author,
          price: displayPrice,
          originalPrice: originalPrice,
          image: boSach.url || 'https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=400',
          description: boSach.moTa || 'Chưa có mô tả',
          category: 'Bộ sách',
          categoryId: null,
          inStock: boSach.soLuong > 0,
          stockQuantity: Number(boSach.soLuong) || 0,
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
    
    // Cập nhật dữ liệu bán chạy
    await updateSalesData(state.products)
    
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
  // Kiểm tra tồn kho
  const stockQuantity = product.stockQuantity || 0
  if (stockQuantity <= 0) {
    return false // Không thể thêm vào giỏ hàng
  }
  
  const existingItem = state.cart.find(item => item.id === product.id)
  if (existingItem) {
    // Kiểm tra xem có vượt quá tồn kho không
    if (existingItem.quantity >= stockQuantity) {
      return false // Đã đạt giới hạn tồn kho
    }
    existingItem.quantity++
  } else {
    state.cart.push({ ...product, quantity: 1 })
  }
  return true // Thêm thành công
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

// Tạo giỏ hàng tạm thời cho "Mua ngay" - không ảnh hưởng giỏ hàng chính
const createBuyNowCart = (product) => {
  // Lưu giỏ hàng hiện tại
  const currentCart = [...state.cart]
  
  // Tạo giỏ hàng mới chỉ với sản phẩm được chọn
  const buyNowProduct = { ...product, quantity: 1 }
  state.cart = [buyNowProduct]
  
  // Trả về giỏ hàng cũ để có thể khôi phục sau
  return currentCart
}

// Khôi phục giỏ hàng cũ
const restoreCart = (previousCart) => {
  state.cart = previousCart
}

// Buy Now Cart functions
const addToBuyNowCart = async (product) => {
  console.log('🛒 addToBuyNowCart called with product:', product)
  console.log('📦 Current buyNowCart before add:', state.buyNowCart)
  
  const existingItem = state.buyNowCart.find(item => item.id === product.id)
  
  if (existingItem) {
    existingItem.quantity += 1
    console.log('➕ Updated existing item quantity:', existingItem)
  } else {
    // Đảm bảo sản phẩm có đầy đủ thông tin như sản phẩm trong cart thông thường
    let enrichedProduct = { ...product }
    
    // Nếu sản phẩm thiếu productDetailItems, thử lấy từ store
    if (!enrichedProduct.productDetailItems || enrichedProduct.productDetailItems.length === 0) {
      console.log('🔍 Product missing productDetailItems, trying to enrich...')
      
      // Tìm sản phẩm tương ứng trong products list
      const fullProduct = state.products.find(p => p.id === product.id)
      if (fullProduct && fullProduct.productDetailItems) {
        enrichedProduct.productDetailItems = fullProduct.productDetailItems
        console.log('✅ Enriched product with productDetailItems:', enrichedProduct.productDetailItems)
      } else {
        console.warn('⚠️ Could not find productDetailItems for product:', product.title)
      }
    }
    
    const newItem = {
      ...enrichedProduct,
      quantity: 1
    }
    state.buyNowCart.push(newItem)
    console.log('➕ Added new item to buyNowCart:', newItem)
  }
  
  console.log('📦 buyNowCart after add:', state.buyNowCart)
}

const removeFromBuyNowCart = (productId) => {
  state.buyNowCart = state.buyNowCart.filter(item => item.id !== productId)
}

const updateBuyNowCartQuantity = ({ productId, quantity }) => {
  const item = state.buyNowCart.find(item => item.id === productId)
  if (item) {
    item.quantity = quantity
  }
}

const clearBuyNowCart = () => {
  console.log('🧹 clearBuyNowCart called, clearing:', state.buyNowCart)
  state.buyNowCart = []
  console.log('🧹 buyNowCart after clear:', state.buyNowCart)
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

// Load cart from localStorage khi khởi tạo
const loadCart = () => {
  try {
    const saved = localStorage.getItem('cart')
    if (saved) {
      state.cart = JSON.parse(saved)
    }
  } catch (e) {
    state.cart = []
  }
}
loadCart()

// Save cart vào localStorage mỗi khi thay đổi
watch(() => state.cart, (val) => {
  try {
    localStorage.setItem('cart', JSON.stringify(val))
  // eslint-disable-next-line no-empty
  } catch (e) {}
}, { deep: true })

// Export store
export const useRealDataStore = () => {
  return {
    // State
    products: computed(() => state.products),
    categories: computed(() => state.categories),
    cart: computed(() => state.cart),
    buyNowCart: computed(() => state.buyNowCart),
    loading: computed(() => state.loading),
    error: computed(() => state.error),
    
    // Computed
    cartItemCount,
    cartTotal,
    buyNowCartItemCount,
    buyNowCartTotal,
    
    // Actions
    loadProducts,
    loadCategories,
    addToCart,
    removeFromCart,
    updateCartQuantity,
    clearCart,
    createBuyNowCart,
    restoreCart,
    // Buy Now Cart actions
    addToBuyNowCart,
    removeFromBuyNowCart,
    updateBuyNowCartQuantity,
    clearBuyNowCart,
    getProductById,
    getProductsByCategory
  }
} 