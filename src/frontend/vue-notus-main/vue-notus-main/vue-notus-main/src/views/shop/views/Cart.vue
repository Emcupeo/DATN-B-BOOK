<template>
  <div class="container mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold mb-8">Giỏ hàng của bạn</h1>

    <div v-if="cartItems.length === 0" class="text-center py-16">
      <div class="w-24 h-24 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
        <svg class="w-12 h-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-2.5 5M7 13l2.5 5m6-5v6a2 2 0 01-2 2H9a2 2 0 01-2-2v-6m8 0V9a2 2 0 00-2-2H9a2 2 0 00-2 2v4.01"></path>
        </svg>
      </div>
      <h2 class="text-xl font-semibold text-gray-600 mb-2">Giỏ hàng trống</h2>
      <p class="text-gray-500 mb-6">Hãy thêm một số sách vào giỏ hàng của bạn</p>
      <router-link 
        to="/shop" 
        class="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition-colors inline-block"
      >
        Tiếp tục mua sắm
      </router-link>
    </div>

    <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-8">
      <!-- Cart Items -->
      <div class="lg:col-span-2 space-y-4">
        <div 
          v-for="item in cartItems" 
          :key="item.id"
          class="bg-white rounded-lg shadow-md p-6 flex items-center space-x-4"
        >
          <img 
            :src="item.image" 
            :alt="item.title"
            class="w-20 h-24 object-cover rounded"
          >
          
          <div class="flex-1">
            <h3 class="font-semibold text-lg">{{ item.title }}</h3>
            <p class="text-gray-600">{{ item.author }}</p>
            <p class="text-red-600 font-bold">{{ formatPrice(item.price) }}</p>
          </div>

          <div class="flex items-center space-x-2">
            <button 
              @click="decreaseQuantity(item.id)"
              class="w-8 h-8 rounded-full border border-gray-300 flex items-center justify-center hover:bg-gray-100"
              :disabled="item.quantity <= 1"
            >
              -
            </button>
            <span class="w-8 text-center">{{ item.quantity }}</span>
            <button 
              @click="increaseQuantity(item.id)"
              class="w-8 h-8 rounded-full border border-gray-300 flex items-center justify-center hover:bg-gray-100"
            >
              +
            </button>
          </div>

          <div class="text-right">
            <p class="font-bold">{{ formatPrice(item.price * item.quantity) }}</p>
            <button 
              @click="removeFromCart(item.id)"
              class="text-red-500 hover:text-red-700 text-sm mt-1"
            >
              Xóa
            </button>
          </div>
        </div>
      </div>

      <!-- Order Summary -->
      <div class="bg-white rounded-lg shadow-md p-6 h-fit">
        <h2 class="text-xl font-semibold mb-4">Tóm tắt đơn hàng</h2>
        
        <div class="space-y-2 mb-4">
          <div class="flex justify-between">
            <span>Tạm tính:</span>
            <span>{{ formatPrice(cartTotal) }}</span>
          </div>
          <div class="flex justify-between">
            <span>Phí vận chuyển:</span>
            <span class="text-green-600">Miễn phí</span>
          </div>
          <hr class="my-2">
          <div class="flex justify-between text-lg font-bold">
            <span>Tổng cộng:</span>
            <span class="text-red-600">{{ formatPrice(cartTotal) }}</span>
          </div>
        </div>

        <router-link 
          to="/checkout"
          class="w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700 transition-colors block text-center font-semibold"
        >
          Tiến hành thanh toán
        </router-link>

        <router-link 
          to="/shop"
          class="w-full border border-gray-300 text-gray-700 py-3 rounded-lg hover:bg-gray-50 transition-colors block text-center mt-3"
        >
          Tiếp tục mua sắm
        </router-link>
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
      store.updateCartQuantity({ bookId, quantity: item.quantity + 1 })
    }

    const decreaseQuantity = (bookId) => {
      const item = cartItems.value.find(item => item.id === bookId)
      if (item.quantity > 1) {
        store.updateCartQuantity({ bookId, quantity: item.quantity - 1 })
      }
    }

    const removeFromCart = (bookId) => {
      store.removeFromCart(bookId)
    }

    return {
      cartItems,
      cartTotal,
      formatPrice,
      increaseQuantity,
      decreaseQuantity,
      removeFromCart
    }
  }
}
</script>