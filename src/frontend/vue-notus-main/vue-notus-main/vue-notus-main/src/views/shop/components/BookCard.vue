<template>
  <div 
    @click="viewDetails"
    :class="[
      'group bg-white rounded-xl shadow-sm hover:shadow-xl transition-all duration-300 overflow-hidden border border-gray-100 hover:border-blue-200 cursor-pointer',
      viewMode === 'list' ? 'flex' : 'block'
    ]"
  >
    <!-- Image Container -->
    <div 
      :class="[
        'relative overflow-hidden bg-gray-100',
        viewMode === 'list' ? 'w-48 flex-shrink-0' : 'w-full'
      ]"
    >
      <img 
        :src="book.image" 
        :alt="book.title"
        :class="[
          'object-cover group-hover:scale-110 transition-transform duration-500',
          viewMode === 'list' ? 'w-full h-48' : 'w-full h-64'
        ]"
      >
      


      <!-- Quick Actions Overlay -->
      <div class="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-20 transition-all duration-300 flex items-center justify-center">
        <div class="transform translate-y-4 group-hover:translate-y-0 transition-transform duration-300 flex space-x-2">
          <button 
            @click.stop="toggleWishlist"
            :class="[
              'w-10 h-10 rounded-full flex items-center justify-center shadow-lg transition-colors',
              isInWishlist
                ? 'bg-red-500 text-white hover:bg-red-600'
                : 'bg-white text-gray-600 hover:bg-red-50 hover:text-red-500'
            ]"
          >
            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
              <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
            </svg>
          </button>
          <button 
            @click.stop="quickView"
            class="w-10 h-10 bg-white rounded-full flex items-center justify-center shadow-lg hover:bg-blue-50 hover:text-blue-500 transition-colors"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path>
            </svg>
          </button>
        </div>
      </div>

      <!-- Stock Status (moved to price row for better visibility) -->
    </div>
    
    <!-- Content -->
    <div 
      :class="[
        'flex-1',
        viewMode === 'list' ? 'p-6 flex flex-col justify-between' : 'p-4'
      ]"
    >
      <div>
        <!-- Category -->
        <div class="mb-2">
          <span class="text-xs text-blue-600 font-semibold bg-blue-50 px-2 py-1 rounded-full">
            {{ book.category }}
          </span>
        </div>
        
        <!-- Title -->
        <h3 class="text-lg font-bold text-gray-800 mb-2 line-clamp-2 group-hover:text-blue-600 transition-colors">
          {{ book.title }}
        </h3>
        
        <!-- Author -->
        <p class="text-sm text-gray-600 mb-3 flex items-center">
          <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
          </svg>
          {{ book.author }}
        </p>

        <!-- Additional Info for List View -->
        <div v-if="viewMode === 'list'" class="grid grid-cols-2 gap-4 mb-4">
          <div class="text-sm text-gray-600">
            <span class="font-medium">Nhà xuất bản:</span> {{ book.publisher || 'NXB Trẻ' }}
          </div>
          <div class="text-sm text-gray-600">
            <span class="font-medium">Ngôn ngữ:</span> {{ book.language || 'Tiếng Việt' }}
          </div>
          <div class="text-sm text-gray-600">
            <span class="font-medium">Định dạng:</span> {{ book.format || 'Bìa mềm' }}
          </div>
          <div class="text-sm text-gray-600">
            <span class="font-medium">Số trang:</span> {{ book.pages || '300' }}
          </div>
        </div>
        
        <!-- Description for List View -->
        <div v-if="viewMode === 'list'" class="mb-4">
          <p class="text-sm text-gray-600 line-clamp-3">
            {{ book.description }}
          </p>
        </div>
        

      </div>

      <div>
        <!-- Price + Stock -->
        <div class="flex items-center justify-between mb-4">
          <div class="flex items-baseline space-x-2">
            <span class="text-xl font-bold text-red-600">
              {{ formatPrice(book.price) }}
            </span>
            <span v-if="book.hasDiscount && book.originalPrice && book.originalPrice > book.price" class="text-sm text-gray-400 line-through">
              {{ formatPrice(book.originalPrice) }}
            </span>
            <span v-if="book.hasDiscount && book.discountPercentage" class="ml-2 bg-red-100 text-red-600 px-2 py-1 rounded-full text-xs font-semibold">
              -{{ Math.round(book.discountPercentage) }}%
            </span>
          </div>
          <div>
            <span 
              v-if="book.inStock" 
              class="inline-flex items-center bg-green-100 text-green-700 px-3 py-1 rounded-full text-xs font-semibold"
            >
              <svg class="w-3 h-3 mr-1" fill="currentColor" viewBox="0 0 20 20">
                <path d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.707a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"/>
              </svg>
              Còn {{ book.stockQuantity || 0 }}
            </span>
            <span 
              v-else 
              class="inline-flex items-center bg-red-100 text-red-700 px-3 py-1 rounded-full text-xs font-semibold"
            >
              <svg class="w-3 h-3 mr-1" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM7 9a1 1 0 012 0v2a1 1 0 11-2 0V9zm5 0a1 1 0 10-2 0v2a1 1 0 002 0V9z" clip-rule="evenodd"/>
              </svg>
              Hết hàng
            </span>
          </div>
        </div>
        
        <!-- Actions -->
        <div class="flex space-x-2">
          <button 
            @click.stop="addToCart"
            :disabled="!book.inStock"
            class="flex-1 bg-gradient-to-r from-blue-600 to-purple-600 text-white px-3 py-2 rounded-lg hover:from-blue-700 hover:to-purple-700 transition-all duration-200 text-sm font-medium flex items-center justify-center disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-2.5 5M7 13l2.5 5m6-5v6a2 2 0 01-2 2H9a2 2 0 01-2-2v-6m8 0V9a2 2 0 00-2-2H9a2 2 0 00-2 2v4.01"></path>
            </svg>
            {{ book.inStock ? 'Thêm vào giỏ' : 'Hết hàng' }}
          </button>
          <button 
            @click.stop="buyNow"
            :disabled="!book.inStock"
            class="flex-1 bg-gradient-to-r from-green-600 to-emerald-600 text-white px-3 py-2 rounded-lg hover:from-green-700 hover:to-emerald-700 transition-all duration-200 text-sm font-medium flex items-center justify-center disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path>
            </svg>
            {{ book.inStock ? 'Mua ngay' : 'Hết hàng' }}
          </button>
        </div>

        <!-- Additional Info -->
        <div class="mt-3 pt-3 border-t border-gray-100">
          <div class="flex items-center justify-between text-xs text-gray-500">
            <span>📦 Miễn phí vận chuyển</span>
            <span>⚡ Giao trong 2h</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useRealDataStore } from '../store/realDataStore'
import { useWishlist } from '../store/wishlist'
import { computed } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'BookCard',
  props: {
    book: {
      type: Object,
      required: true
    },
    viewMode: {
      type: String,
      default: 'grid',
      validator: value => ['grid', 'list'].includes(value)
    }
  },
  setup(props) {
    const router = useRouter()
    const store = useRealDataStore()
    const wishlist = useWishlist()

    const isInWishlist = computed(() => wishlist.isIn(props.book.id))

    const formatPrice = (price) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(price)
    }

    const addToCart = () => {
      if (props.book.inStock) {
        // Kiểm tra số lượng tồn kho - xử lý cả sản phẩm thường và bộ sách
        const stockQuantity = props.book.stockQuantity || 0
        if (stockQuantity > 0) {
          const success = store.addToCart(props.book)
          if (success) {
            alert('Đã thêm vào giỏ hàng thành công!')
          } else {
            alert('Không thể thêm sản phẩm: vượt quá số lượng tồn kho!')
          }
        } else {
          alert('Sản phẩm này đã hết hàng!')
        }
      }
    }

    const buyNow = async () => {
      if (props.book.inStock) {
        console.log('🛒 Buy Now clicked for product:', props.book.title)
        
        // Xóa buyNowCart cũ và thêm sản phẩm mới
        store.clearBuyNowCart()
        console.log('🧹 Cleared buyNowCart')
        
        await store.addToBuyNowCart(props.book)
        console.log('➕ Added to buyNowCart:', props.book.title)
        console.log('📦 buyNowCart after add:', store.buyNowCart.value)
        
        // Backup vào localStorage để đảm bảo không mất dữ liệu
        localStorage.setItem('buyNowCartBackup', JSON.stringify(store.buyNowCart.value))
        console.log('💾 Backed up buyNowCart to localStorage')
        
        // Redirect đến trang checkout với buyNowCart
        router.push('/checkout?mode=buynow')
      }
    }

    const viewDetails = () => {
      if (props.book.category === 'Bộ sách') {
        window.location.href = `/bo-sach/${props.book.id}`
      } else {
        window.location.href = `/book/${props.book.id}`
      }
    }

    const toggleWishlist = () => {
      if (isInWishlist.value) {
        wishlist.remove(props.book.id)
      } else {
        wishlist.add(props.book.id)
      }
    }

    const quickView = () => {
      // Quick view functionality
      console.log('Quick view:', props.book.title)
    }

    return {
      formatPrice,
      addToCart,
      buyNow,
      viewDetails,
      toggleWishlist,
      quickView,
      isInWishlist
    }
  }
}
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>