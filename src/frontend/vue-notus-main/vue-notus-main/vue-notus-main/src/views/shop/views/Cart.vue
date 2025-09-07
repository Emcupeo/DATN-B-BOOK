<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header with Progress Steps -->
    <div class="bg-white shadow-sm border-b">
      <div class="container mx-auto px-4 py-4">
        <div class="flex items-center justify-between">
          <h1 class="text-xl font-semibold text-gray-900">Giỏ hàng</h1>
          <div class="flex items-center space-x-2 text-sm">
            <div class="flex items-center">
              <div class="w-6 h-6 bg-blue-600 text-white rounded-full flex items-center justify-center text-xs font-medium">1</div>
              <span class="ml-2 text-blue-600 font-medium">Giỏ hàng</span>
            </div>
            <i class="fas fa-chevron-right text-gray-400 text-xs"></i>
            <div class="flex items-center">
              <div class="w-6 h-6 bg-gray-300 text-gray-600 rounded-full flex items-center justify-center text-xs font-medium">2</div>
              <span class="ml-2 text-gray-500">Thanh toán</span>
            </div>
            <i class="fas fa-chevron-right text-gray-400 text-xs"></i>
            <div class="flex items-center">
              <div class="w-6 h-6 bg-gray-300 text-gray-600 rounded-full flex items-center justify-center text-xs font-medium">3</div>
              <span class="ml-2 text-gray-500">Hoàn thành</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="container mx-auto px-4 py-8">

    <div v-if="cartItems.length === 0" class="bg-white rounded-xl shadow-sm border border-gray-200 p-12 text-center">
      <div class="w-20 h-20 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-6">
        <svg class="w-10 h-10 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-2.5 5M7 13l2.5 5m6-5v6a2 2 0 01-2 2H9a2 2 0 01-2-2v-6m8 0V9a2 2 0 00-2-2H9a2 2 0 00-2 2v4.01"></path>
        </svg>
      </div>
      <h2 class="text-2xl font-semibold text-gray-800 mb-3">Giỏ hàng trống</h2>
      <p class="text-gray-600 mb-8 max-w-md mx-auto">Bạn chưa có sản phẩm nào trong giỏ hàng. Hãy khám phá và thêm những cuốn sách yêu thích vào giỏ hàng của bạn.</p>
      <router-link 
        to="/" 
        class="inline-flex items-center px-8 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium"
      >
        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
        </svg>
        Tiếp tục mua sắm
      </router-link>
    </div>

    <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-8">
      <!-- Cart Items -->
      <div class="lg:col-span-2">
        <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <div class="flex items-center justify-between mb-6">
            <h2 class="text-xl font-semibold text-gray-900">Sản phẩm trong giỏ hàng</h2>
            <button 
              v-if="cartItems.length > 0"
              @click="clearCart"
              class="text-red-500 hover:text-red-700 text-sm flex items-center"
            >
              <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
              </svg>
              Xóa tất cả
            </button>
          </div>
          <div class="space-y-4">
            <div 
              v-for="item in cartItems" 
              :key="item.id"
              class="flex items-center space-x-4 p-4 border border-gray-200 rounded-lg hover:shadow-md transition-shadow"
            >
              <img 
                :src="item.image" 
                :alt="item.title"
                class="w-16 h-20 object-cover rounded-lg shadow-sm"
              >
              
              <div class="flex-1 min-w-0">
                <h3 class="font-semibold text-gray-900 truncate">{{ item.title }}</h3>
                <p class="text-gray-600 text-sm">{{ item.author }}</p>
                <p class="text-blue-600 font-semibold">{{ formatPrice(item.price) }}</p>
                <p v-if="item.stockQuantity" class="text-xs text-gray-500">Tồn kho: {{ item.stockQuantity }}</p>
              </div>

              <div class="flex items-center space-x-3">
                <button 
                  @click="decreaseQuantity(item.id)"
                  class="w-8 h-8 rounded-full border border-gray-300 flex items-center justify-center hover:bg-gray-50 transition-colors"
                  :disabled="item.quantity <= 1"
                  :class="{ 'opacity-50 cursor-not-allowed': item.quantity <= 1 }"
                >
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 12H4"></path>
                  </svg>
                </button>
                <span class="w-8 text-center font-medium">{{ item.quantity }}</span>
                <button 
                  @click="increaseQuantity(item.id)"
                  class="w-8 h-8 rounded-full border border-gray-300 flex items-center justify-center hover:bg-gray-50 transition-colors"
                  :disabled="item.stockQuantity && item.quantity >= item.stockQuantity"
                  :class="{ 'opacity-50 cursor-not-allowed': item.stockQuantity && item.quantity >= item.stockQuantity }"
                >
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
                  </svg>
                </button>
              </div>

              <div class="text-right min-w-0">
                <p class="font-bold text-lg text-gray-900">{{ formatPrice(item.price * item.quantity) }}</p>
                <button 
                  @click="removeFromCart(item.id)"
                  class="text-red-500 hover:text-red-700 text-sm mt-1 flex items-center"
                >
                  <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
                  </svg>
                  Xóa
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Order Summary -->
      <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6 h-fit sticky top-4">
        <h2 class="text-xl font-semibold text-gray-900 mb-6">Tóm tắt đơn hàng</h2>
        
        <div class="space-y-4 mb-6">
          <div class="flex justify-between text-gray-600">
            <span>Tạm tính ({{ cartItems.length }} sản phẩm):</span>
            <span class="font-medium">{{ formatPrice(cartTotal) }}</span>
          </div>
          <div class="flex justify-between text-gray-600">
            <span>Phí vận chuyển:</span>
            <span class="text-green-600 font-medium">Miễn phí</span>
          </div>
          <div class="border-t border-gray-200 pt-4">
            <div class="flex justify-between text-lg font-bold text-gray-900">
              <span>Tổng cộng:</span>
              <span class="text-blue-600">{{ formatPrice(cartTotal) }}</span>
            </div>
          </div>
        </div>

        <div class="space-y-3">
          <router-link 
            to="/checkout"
            class="w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700 transition-colors block text-center font-semibold flex items-center justify-center"
          >
            <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z"></path>
            </svg>
            Tiến hành thanh toán
          </router-link>

          <router-link 
            to="/"
            class="w-full border border-gray-300 text-gray-700 py-3 rounded-lg hover:bg-gray-50 transition-colors block text-center flex items-center justify-center"
          >
            <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
            </svg>
            Tiếp tục mua sắm
          </router-link>
        </div>
      </div>
    </div>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'
import { useRealDataStore } from '../store/realDataStore'

export default {
  name: 'Cart',
  setup() {
    const store = useRealDataStore()

    const cartItems = computed(() => store.cart.value)
    const cartTotal = computed(() => store.cartTotal.value)

    const formatPrice = (price) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(price)
    }

    const increaseQuantity = (bookId) => {
      const item = cartItems.value.find(item => item.id === bookId)
      if (item) {
        // Kiểm tra số lượng tồn kho
        if (item.stockQuantity && item.quantity >= item.stockQuantity) {
          alert(`Số lượng tồn kho chỉ còn ${item.stockQuantity} sản phẩm. Không thể thêm nữa!`)
          return
        }
        store.updateCartQuantity({ productId: bookId, quantity: item.quantity + 1 })
      }
    }

    const decreaseQuantity = (bookId) => {
      const item = cartItems.value.find(item => item.id === bookId)
      if (item && item.quantity > 1) {
        store.updateCartQuantity({ productId: bookId, quantity: item.quantity - 1 })
      }
    }

    const removeFromCart = (bookId) => {
      store.removeFromCart(bookId)
    }

    const clearCart = () => {
      if (confirm('Bạn có chắc chắn muốn xóa tất cả sản phẩm khỏi giỏ hàng?')) {
        store.clearCart()
      }
    }

    return {
      cartItems,
      cartTotal,
      formatPrice,
      increaseQuantity,
      decreaseQuantity,
      removeFromCart,
      clearCart
    }
  }
}
</script>