<template>
  <div v-if="book" class="container mx-auto px-4 py-8">
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-12">
      <!-- Book Image -->
      <div class="space-y-4">
        <div class="aspect-w-3 aspect-h-4 bg-gray-100 rounded-lg overflow-hidden">
          <img 
            :src="book.image" 
            :alt="book.title"
            class="w-full h-96 object-cover"
          >
        </div>
      </div>

      <!-- Book Info -->
      <div class="space-y-6">
        <div>
          <span class="text-sm text-blue-600 font-medium">{{ book.category }}</span>
          <h1 class="text-3xl font-bold text-gray-900 mt-2">{{ book.title }}</h1>
          <p class="text-lg text-gray-600 mt-1">{{ book.author }}</p>
        </div>

        <!-- Rating -->
        <div class="flex items-center space-x-2">
          <div class="flex text-yellow-400">
            <span v-for="star in ratingStars" :key="star.index" class="text-lg">
              {{ star.filled ? '★' : '☆' }}
            </span>
          </div>
          <span class="text-gray-600">({{ book.rating }}/5)</span>
        </div>

        <!-- Price -->
        <div class="space-y-2">
          <div class="flex items-center space-x-4">
            <span class="text-3xl font-bold text-red-600">
              {{ formatPrice(book.price) }}
            </span>
            <span class="text-lg text-gray-500 line-through">
              {{ formatPrice(book.originalPrice) }}
            </span>
            <span class="bg-red-100 text-red-800 px-2 py-1 rounded-full text-sm font-semibold">
              Tiết kiệm {{ formatPrice(book.originalPrice - book.price) }}
            </span>
          </div>
        </div>

        <!-- Stock Status -->
        <div class="flex items-center space-x-2">
          <div class="w-3 h-3 bg-green-500 rounded-full"></div>
          <span class="text-green-600 font-medium">Còn hàng</span>
        </div>

        <!-- Description -->
        <div>
          <h3 class="text-lg font-semibold mb-2">Mô tả sản phẩm</h3>
          <p class="text-gray-700 leading-relaxed">{{ book.description }}</p>
        </div>

        <!-- Quantity and Add to Cart -->
        <div class="space-y-4">
          <div class="flex items-center space-x-4">
            <label class="text-sm font-medium text-gray-700">Số lượng:</label>
            <div class="flex items-center border border-gray-300 rounded-lg">
              <button 
                @click="decreaseQuantity"
                class="px-3 py-2 text-gray-600 hover:bg-gray-100"
                :disabled="quantity <= 1"
              >
                -
              </button>
              <span class="px-4 py-2 border-x border-gray-300">{{ quantity }}</span>
              <button 
                @click="increaseQuantity"
                class="px-3 py-2 text-gray-600 hover:bg-gray-100"
              >
                +
              </button>
            </div>
          </div>

          <div class="flex space-x-4">
            <button 
              @click="addToCart"
              class="flex-1 bg-blue-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-blue-700 transition-colors"
            >
              Thêm vào giỏ hàng
            </button>
            <button class="px-6 py-3 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors">
              ♡ Yêu thích
            </button>
          </div>
        </div>

        <!-- Features -->
        <div class="border-t pt-6">
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 text-sm">
            <div class="flex items-center space-x-2">
              <svg class="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
              </svg>
              <span>Miễn phí giao hàng</span>
            </div>
            <div class="flex items-center space-x-2">
              <svg class="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
              </svg>
              <span>Đổi trả trong 7 ngày</span>
            </div>
            <div class="flex items-center space-x-2">
              <svg class="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
              </svg>
              <span>Sách chính hãng</span>
            </div>
            <div class="flex items-center space-x-2">
              <svg class="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
              </svg>
              <span>Hỗ trợ 24/7</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  
  <div v-else class="container mx-auto px-4 py-8 text-center">
    <p class="text-gray-600">Không tìm thấy sách</p>
  </div>
</template>

<script>
import { ref, computed } from 'vue'
import { useShopStore } from '../store'
import { useRoute } from 'vue-router'

export default {
  name: 'BookDetail',
  setup() {
    const store = useShopStore()
    const route = useRoute()
    const quantity = ref(1)

    const book = computed(() => store.getBookById(route.params.id))

    const formatPrice = (price) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(price)
    }

    const increaseQuantity = () => {
      quantity.value++
    }

    const decreaseQuantity = () => {
      if (quantity.value > 1) {
        quantity.value--
      }
    }

    const addToCart = () => {
      for (let i = 0; i < quantity.value; i++) {
        store.addToCart(book.value)
      }
      quantity.value = 1
    }

    const ratingStars = computed(() => {
      const stars = []
      for (let i = 0; i < 5; i++) {
        stars.push({
          index: i,
          filled: i < Math.floor(book.value.rating)
        })
      }
      return stars
    })

    return {
      book,
      quantity,
      formatPrice,
      increaseQuantity,
      decreaseQuantity,
      addToCart,
      ratingStars
    }
  }
}
</script>