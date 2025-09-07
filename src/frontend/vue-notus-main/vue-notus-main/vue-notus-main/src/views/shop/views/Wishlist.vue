<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Breadcrumb -->
    <div class="bg-white border-b border-gray-200">
      <div class="container mx-auto px-4 py-4">
        <nav class="flex" aria-label="Breadcrumb">
          <ol class="inline-flex items-center space-x-1 md:space-x-3">
            <li class="inline-flex items-center">
              <router-link to="/" class="inline-flex items-center text-sm font-medium text-gray-700 hover:text-blue-600">
                <svg class="w-4 h-4 mr-2" fill="currentColor" viewBox="0 0 20 20">
                  <path d="M10.707 2.293a1 1 0 00-1.414 0l-7 7a1 1 0 001.414 1.414L4 10.414V17a1 1 0 001 1h2a1 1 0 001-1v-2a1 1 0 011-1h2a1 1 0 011 1v2a1 1 0 001 1h2a1 1 0 001-1v-6.586l.293.293a1 1 0 001.414-1.414l-7-7z"></path>
                </svg>
                Trang chủ
              </router-link>
            </li>
            <li>
              <div class="flex items-center">
                <svg class="w-6 h-6 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd"></path>
                </svg>
                <span class="ml-1 text-sm font-medium text-gray-500 md:ml-2">Danh sách yêu thích</span>
              </div>
            </li>
          </ol>
        </nav>
      </div>
    </div>

    <div class="container mx-auto px-4 py-8">
      <!-- Header -->
      <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6 mb-8">
        <div class="flex items-center justify-between">
          <div class="flex items-center space-x-4">
            <div class="w-12 h-12 bg-gradient-to-r from-red-500 to-pink-500 rounded-xl flex items-center justify-center">
              <svg class="w-6 h-6 text-white" fill="currentColor" viewBox="0 0 24 24">
                <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
              </svg>
            </div>
            <div>
              <h1 class="text-2xl font-bold text-gray-800">Danh sách yêu thích</h1>
              <p class="text-gray-600">{{ wishlist.length }} sản phẩm trong danh sách</p>
            </div>
          </div>
          <div class="flex items-center space-x-3">
            <button 
              v-if="wishlist.length > 0"
              @click="addAllToCart"
              class="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition-colors font-medium"
            >
              Thêm tất cả vào giỏ hàng
            </button>
            <button 
              v-if="wishlist.length > 0"
              @click="clearWishlist"
              class="bg-gray-100 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-200 transition-colors"
            >
              Xóa tất cả
            </button>
          </div>
        </div>
      </div>

      <!-- Empty State -->
      <div v-if="wishlist.length === 0" class="text-center py-16">
        <div class="w-24 h-24 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-6">
          <svg class="w-12 h-12 text-gray-400" fill="currentColor" viewBox="0 0 24 24">
            <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
          </svg>
        </div>
        <h3 class="text-xl font-semibold text-gray-800 mb-2">Danh sách yêu thích trống</h3>
        <p class="text-gray-600 mb-6">Bạn chưa có sản phẩm nào trong danh sách yêu thích</p>
        <router-link 
          to="/products"
          class="inline-flex items-center px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium"
        >
          <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
          </svg>
          Khám phá sản phẩm
        </router-link>
      </div>

      <!-- Wishlist Items -->
      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        <div 
          v-for="book in wishlist" 
          :key="book.id"
          @click="viewDetails(book)"
          class="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden hover:shadow-md transition-shadow group cursor-pointer"
        >
          <!-- Book Image -->
          <div class="relative">
            <img 
              :src="book.image" 
              :alt="book.title"
              class="w-full h-48 object-cover"
            >
            <div class="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-10 transition-all duration-200"></div>
            
            <!-- Quick Actions -->
            <div class="absolute top-3 right-3 flex flex-col space-y-2">
              <button 
                @click.stop="removeFromWishlist(book.id)"
                class="w-8 h-8 bg-white rounded-full flex items-center justify-center shadow-md hover:bg-red-50 transition-colors"
              >
                <svg class="w-4 h-4 text-red-500" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
                </svg>
              </button>
            </div>

            <!-- Stock Status -->
            <div class="absolute bottom-3 left-3">
              <span 
                :class="[
                  'px-2 py-1 rounded-full text-xs font-medium',
                  book.inStock 
                    ? 'bg-green-100 text-green-800' 
                    : 'bg-red-100 text-red-800'
                ]"
              >
                {{ book.inStock ? 'Còn hàng' : 'Hết hàng' }}
              </span>
            </div>
          </div>

          <!-- Book Info -->
          <div class="p-4">
            <h3 class="font-semibold text-gray-800 mb-2 line-clamp-2">{{ book.title }}</h3>
            <p class="text-sm text-gray-600 mb-2">{{ book.author }}</p>
            
            <!-- Rating -->
            <div class="flex items-center mb-3">
              <div class="flex items-center">
                <span v-for="star in 5" :key="star" class="text-yellow-400">
                  <svg v-if="star <= Math.floor(book.rating)" class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                  </svg>
                  <svg v-else class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z" fill="none" stroke="currentColor" stroke-width="1"/>
                  </svg>
                </span>
              </div>
              <span class="text-sm text-gray-500 ml-2">({{ book.reviewCount }})</span>
            </div>

            <!-- Price -->
            <div class="flex items-center justify-between mb-4">
              <div>
                <span class="text-lg font-bold text-gray-800">{{ formatPrice(book.price) }}</span>
                <span v-if="book.originalPrice > book.price" class="text-sm text-gray-500 line-through ml-2">
                  {{ formatPrice(book.originalPrice) }}
                </span>
              </div>
              <span v-if="book.originalPrice > book.price" class="text-xs bg-red-100 text-red-800 px-2 py-1 rounded">
                -{{ Math.round(((book.originalPrice - book.price) / book.originalPrice) * 100) }}%
              </span>
            </div>

            <!-- Actions -->
            <div class="flex space-x-2">
              <button 
                @click.stop="addToCart(book)"
                :disabled="!book.inStock"
                :class="[
                  'flex-1 py-2 px-3 rounded-lg font-medium transition-colors',
                  book.inStock
                    ? 'bg-blue-600 text-white hover:bg-blue-700'
                    : 'bg-gray-100 text-gray-400 cursor-not-allowed'
                ]"
              >
                {{ book.inStock ? 'Thêm vào giỏ' : 'Hết hàng' }}
              </button>
              <button 
                @click.stop="buyNow(book)"
                :disabled="!book.inStock"
                :class="[
                  'flex-1 py-2 px-3 rounded-lg font-medium transition-colors',
                  book.inStock
                    ? 'bg-green-600 text-white hover:bg-green-700'
                    : 'bg-gray-100 text-gray-400 cursor-not-allowed'
                ]"
              >
                {{ book.inStock ? 'Mua ngay' : 'Hết hàng' }}
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Recommendations -->
      <div v-if="wishlist.length > 0" class="mt-16">
        <h2 class="text-xl font-bold text-gray-800 mb-6">Có thể bạn cũng thích</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          <div 
            v-for="book in recommendations" 
            :key="book.id"
            class="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden hover:shadow-md transition-shadow group"
          >
            <div class="relative">
              <img 
                :src="book.image" 
                :alt="book.title"
                class="w-full h-48 object-cover"
              >
              <div class="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-10 transition-all duration-200"></div>
              
              <div class="absolute top-3 right-3">
                <button 
                  @click="toggleWishlist(book)"
                  :class="[
                    'w-8 h-8 rounded-full flex items-center justify-center shadow-md transition-colors',
                    isInWishlist(book.id)
                      ? 'bg-red-500 text-white hover:bg-red-600'
                      : 'bg-white text-gray-400 hover:text-red-500'
                  ]"
                >
                  <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
                  </svg>
                </button>
              </div>
            </div>

            <div class="p-4">
              <h3 class="font-semibold text-gray-800 mb-2 line-clamp-2">{{ book.title }}</h3>
              <p class="text-sm text-gray-600 mb-2">{{ book.author }}</p>
              
              <div class="flex items-center justify-between mb-3">
                <div class="flex items-center">
                  <span v-for="star in 5" :key="star" class="text-yellow-400">
                    <svg v-if="star <= Math.floor(book.rating)" class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
                      <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                    </svg>
                    <svg v-else class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
                      <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z" fill="none" stroke="currentColor" stroke-width="1"/>
                    </svg>
                  </span>
                </div>
                <span class="text-sm text-gray-500">({{ book.reviewCount }})</span>
              </div>

              <div class="flex items-center justify-between">
                <span class="text-lg font-bold text-gray-800">{{ formatPrice(book.price) }}</span>
                <button 
                  @click="addToCart(book)"
                  class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors text-sm"
                >
                  Thêm vào giỏ
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useRealDataStore } from '../store/realDataStore'
import { useWishlist } from '../store/wishlist'

export default {
  name: 'Wishlist',
  setup() {
    const router = useRouter()
    const store = useRealDataStore()
    const wishlist = useWishlist()

    // Lấy danh sách sách yêu thích từ shop store dựa trên wishlist IDs
    const wishlistBooks = computed(() => {
      return wishlist.items.value.map(id => store.getProductById(id)).filter(Boolean)
    })

    // Recommendations based on wishlist categories
    const recommendations = computed(() => {
      if (wishlistBooks.value.length === 0) return []
      const categories = [...new Set(wishlistBooks.value.map(book => book.category))]
      const recommended = store.products.value
        .filter(book => categories.includes(book.category) && !wishlist.isIn(book.id))
        .sort((a, b) => b.rating - a.rating)
        .slice(0, 4)
      return recommended
    })

    // Methods
    const isInWishlist = (bookId) => wishlist.isIn(bookId)
    const addToWishlist = (book) => wishlist.add(book.id)
    const removeFromWishlist = (bookId) => wishlist.remove(bookId)
    const toggleWishlist = (book) => isInWishlist(book.id) ? wishlist.remove(book.id) : wishlist.add(book.id)
    const clearWishlist = () => wishlist.clear()
    const addToCart = (book) => store.addToCart(book)
    const buyNow = async (book) => {
      if (book.inStock) {
        // Xóa buyNowCart cũ và thêm sản phẩm mới
        store.clearBuyNowCart()
        await store.addToBuyNowCart(book)
        
        // Backup vào localStorage để đảm bảo không mất dữ liệu
        localStorage.setItem('buyNowCartBackup', JSON.stringify(store.buyNowCart.value))
        
        // Redirect đến trang checkout với buyNowCart
        router.push('/checkout?mode=buynow')
      }
    }
    const viewDetails = (book) => {
      if (book.category === 'Bộ sách') {
        window.location.href = `/bo-sach/${book.id}`
      } else {
        window.location.href = `/book/${book.id}`
      }
    }
    const addAllToCart = () => {
      wishlistBooks.value.forEach(book => {
        if (book.inStock) {
          store.addToCart(book)
        }
      })
    }
    const formatPrice = (price) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(price)
    }

    return {
      wishlist: wishlistBooks,
      recommendations,
      isInWishlist,
      addToWishlist,
      removeFromWishlist,
      toggleWishlist,
      clearWishlist,
      addToCart,
      buyNow,
      viewDetails,
      addAllToCart,
      formatPrice
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
</style> 