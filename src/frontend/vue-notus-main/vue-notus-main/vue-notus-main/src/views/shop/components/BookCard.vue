<template>
  <div 
    :class="[
      'group bg-white rounded-xl shadow-sm hover:shadow-xl transition-all duration-300 overflow-hidden border border-gray-100 hover:border-blue-200',
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
      
      <!-- Discount Badge -->
      <div class="absolute top-3 left-3">
        <span class="bg-red-500 text-white px-2 py-1 rounded-full text-xs font-bold shadow-lg">
          -{{ Math.round((1 - book.price / book.originalPrice) * 100) }}%
        </span>
      </div>

      <!-- Quick Actions Overlay -->
      <div class="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-20 transition-all duration-300 flex items-center justify-center">
        <div class="transform translate-y-4 group-hover:translate-y-0 transition-transform duration-300 flex space-x-2">
          <button 
            @click="toggleWishlist"
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
            @click="quickView"
            class="w-10 h-10 bg-white rounded-full flex items-center justify-center shadow-lg hover:bg-blue-50 hover:text-blue-500 transition-colors"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path>
            </svg>
          </button>
        </div>
      </div>

      <!-- Stock Status -->
      <div class="absolute bottom-3 right-3">
        <span v-if="book.inStock" class="bg-green-500 text-white px-2 py-1 rounded-full text-xs font-medium">
          Còn hàng
        </span>
        <span v-else class="bg-red-500 text-white px-2 py-1 rounded-full text-xs font-medium">
          Hết hàng
        </span>
      </div>
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
        
        <!-- Rating -->
        <div class="flex items-center mb-3">
          <div class="flex text-yellow-400 mr-2">
            <span v-for="star in ratingStars" :key="star.index" class="text-sm">
              {{ star.filled ? '★' : '☆' }}
            </span>
          </div>
          <span class="text-sm text-gray-500">({{ book.rating }})</span>
          <span class="text-xs text-gray-400 ml-2">• {{ book.reviewCount || '0' }} đánh giá</span>
        </div>
      </div>

      <div>
        <!-- Price -->
        <div class="flex items-center justify-between mb-4">
          <div>
            <span class="text-xl font-bold text-red-600">
              {{ formatPrice(book.price) }}
            </span>
            <span class="text-sm text-gray-500 line-through ml-2">
              {{ formatPrice(book.originalPrice) }}
            </span>
          </div>
          <div class="text-xs text-gray-400">
            Tiết kiệm {{ formatPrice(book.originalPrice - book.price) }}
          </div>
        </div>
        
        <!-- Actions -->
        <div class="flex space-x-2">
          <router-link 
            :to="`/shop/book/${book.id}`"
            class="flex-1 bg-gray-100 text-gray-800 px-4 py-2 rounded-lg text-center hover:bg-gray-200 transition-colors text-sm font-medium flex items-center justify-center"
          >
            <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path>
            </svg>
            Chi tiết
          </router-link>
          <button 
            @click="addToCart"
            :disabled="!book.inStock"
            class="flex-1 bg-gradient-to-r from-blue-600 to-purple-600 text-white px-4 py-2 rounded-lg hover:from-blue-700 hover:to-purple-700 transition-all duration-200 text-sm font-medium flex items-center justify-center disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-2.5 5M7 13l2.5 5m6-5v6a2 2 0 01-2 2H9a2 2 0 01-2-2v-6m8 0V9a2 2 0 00-2-2H9a2 2 0 00-2 2v4.01"></path>
            </svg>
            {{ book.inStock ? 'Thêm vào giỏ' : 'Hết hàng' }}
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
import { useShopStore } from '../store'
import { useWishlist } from '../store/wishlist'
import { computed } from 'vue'

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
    const store = useShopStore()
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
        store.addToCart(props.book)
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

    const ratingStars = computed(() => {
      const stars = []
      const rating = Math.floor(props.book.rating)
      for (let i = 1; i <= 5; i++) {
        stars.push({
          index: i,
          filled: i <= rating
        })
      }
      return stars
    })

    return {
      formatPrice,
      addToCart,
      toggleWishlist,
      quickView,
      ratingStars,
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