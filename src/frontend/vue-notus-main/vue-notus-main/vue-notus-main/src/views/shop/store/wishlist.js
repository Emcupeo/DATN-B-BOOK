import { reactive, computed } from 'vue'

// Wishlist state
const wishlistState = reactive({
  items: []
})

// Load wishlist từ localStorage khi khởi tạo
const loadWishlist = () => {
  try {
    const saved = localStorage.getItem('wishlist')
    if (saved) {
      wishlistState.items = JSON.parse(saved)
    }
  } catch (error) {
    console.error('Error loading wishlist:', error)
    wishlistState.items = []
  }
}

// Save wishlist vào localStorage
const saveWishlist = () => {
  try {
    localStorage.setItem('wishlist', JSON.stringify(wishlistState.items))
  } catch (error) {
    console.error('Error saving wishlist:', error)
  }
}

// Thêm sản phẩm vào wishlist
const addToWishlist = (bookId) => {
  if (!wishlistState.items.includes(bookId)) {
    wishlistState.items.push(bookId)
    saveWishlist()
    // Emit custom event để các component khác biết
    window.dispatchEvent(new CustomEvent('wishlist-updated', { 
      detail: { count: wishlistState.items.length } 
    }))
  }
}

// Xóa sản phẩm khỏi wishlist
const removeFromWishlist = (bookId) => {
  const index = wishlistState.items.indexOf(bookId)
  if (index > -1) {
    wishlistState.items.splice(index, 1)
    saveWishlist()
    // Emit custom event để các component khác biết
    window.dispatchEvent(new CustomEvent('wishlist-updated', { 
      detail: { count: wishlistState.items.length } 
    }))
  }
}

// Kiểm tra sản phẩm có trong wishlist không
const isInWishlist = (bookId) => {
  return wishlistState.items.includes(bookId)
}

// Xóa toàn bộ wishlist
const clearWishlist = () => {
  wishlistState.items = []
  saveWishlist()
  window.dispatchEvent(new CustomEvent('wishlist-updated', { 
    detail: { count: 0 } 
  }))
}

// Lấy số lượng items trong wishlist
const getWishlistCount = () => {
  return wishlistState.items.length
}

// Load wishlist khi module được import
loadWishlist()

// Listen for storage changes (đồng bộ giữa các tab)
window.addEventListener('storage', (e) => {
  if (e.key === 'wishlist') {
    loadWishlist()
    window.dispatchEvent(new CustomEvent('wishlist-updated', { 
      detail: { count: wishlistState.items.length } 
    }))
  }
})

export const useWishlist = () => {
  return {
    items: computed(() => wishlistState.items),
    count: computed(() => wishlistState.items.length),
    add: addToWishlist,
    remove: removeFromWishlist,
    isIn: isInWishlist,
    clear: clearWishlist,
    getCount: getWishlistCount
  }
} 