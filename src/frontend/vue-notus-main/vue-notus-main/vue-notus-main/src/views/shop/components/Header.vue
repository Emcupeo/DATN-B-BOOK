<template>
  <header class="bg-white shadow-lg sticky top-0 z-50">
    <!-- Top Bar -->
    <div class="bg-gray-900 text-white py-2">
      <div class="container mx-auto px-4">
        <div class="flex items-center justify-between text-sm">
          <div class="flex items-center space-x-4">
            <span>📞 Hotline: 1900-1234</span>
            <span>📧 Email: support@bbook.com</span>
          </div>
          <div class="flex items-center space-x-4">
            <template v-if="!authStore.isAuthenticated">
              <router-link to="/auth/login" class="hover:text-blue-400 transition-colors">Đăng nhập</router-link>
              <router-link to="/auth/register" class="hover:text-blue-400 transition-colors">Đăng ký</router-link>
            </template>
            <template v-else>
              <span class="text-blue-400">Xin chào, {{ userDisplayName }}!</span>
              <button @click="handleLogout" class="hover:text-red-400 transition-colors">Đăng xuất</button>
            </template>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Header -->
    <div class="container mx-auto px-4">
      <div class="flex items-center justify-between h-20">
        <!-- Logo -->
        <router-link to="/" class="flex items-center space-x-3">
          <img src="@/assets/img/bbook-logo.png" alt="BBook Logo" class="w-12 h-12 object-contain" />
          <div>
            <span class="text-2xl font-bold text-gray-800">BBook</span>
            <p class="text-xs text-gray-500 -mt-1">Khám phá tri thức</p>
          </div>
        </router-link>

        <!-- Search Bar -->
        <div class="flex-1 max-w-2xl mx-8">
          <div class="relative">
            <input 
              type="text" 
              placeholder="Tìm kiếm sách, tác giả, nhà xuất bản..."
              class="w-full px-6 py-3 pl-14 pr-16 text-gray-700 bg-gray-50 border border-gray-200 rounded-full focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent focus:bg-white transition-all duration-200 shadow-sm hover:shadow-md"
            >
            <div class="absolute inset-y-0 left-0 flex items-center pl-5">
              <svg class="w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
              </svg>
            </div>
            <button class="absolute inset-y-0 right-0 flex items-center pr-5">
              <svg class="w-5 h-5 text-gray-400 hover:text-blue-500 transition-colors" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
              </svg>
            </button>
          </div>
        </div>

        <!-- Right Actions -->
        <div class="flex items-center space-x-4">
          <!-- Wishlist -->
          <router-link to="/wishlist" class="relative p-3 text-gray-600 hover:text-red-500 hover:bg-red-50 transition-all duration-200 rounded-full group">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="1.5" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12z" />
            </svg>
            <span v-if="wishlistCount > 0"
              class="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center font-semibold shadow-sm">
              {{ wishlistCount }}
            </span>
            <span class="absolute -bottom-10 left-1/2 transform -translate-x-1/2 bg-gray-800 text-white text-xs px-3 py-2 rounded-lg opacity-0 group-hover:opacity-100 transition-opacity whitespace-nowrap shadow-lg">
              Yêu thích
            </span>
          </router-link>

          <!-- Cart -->
          <router-link to="/cart" class="relative p-3 text-gray-600 hover:text-blue-600 hover:bg-blue-50 transition-all duration-200 rounded-full group">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="1.5" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 3h1.386c.51 0 .955.343 1.087.835l.383 1.437M7.5 14.25a3 3 0 00-3 3h15.75m-12.75-3h11.218c1.121-2.3 2.1-4.684 2.924-7.138a60.114 60.114 0 00-16.536-1.84M7.5 14.25L5.106 5.272M6 20.25a.75.75 0 11-1.5 0 .75.75 0 011.5 0zm12.75 0a.75.75 0 11-1.5 0 .75.75 0 011.5 0z" />
            </svg>
            <span v-if="cartItemCount > 0"
              class="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center font-semibold shadow-sm">
              {{ cartItemCount }}
            </span>
            <span class="absolute -bottom-10 left-1/2 transform -translate-x-1/2 bg-gray-800 text-white text-xs px-3 py-2 rounded-lg opacity-0 group-hover:opacity-100 transition-opacity whitespace-nowrap shadow-lg">
              Giỏ hàng
            </span>
          </router-link>

          <!-- User Menu -->
          <div v-if="authStore.isAuthenticated" class="relative group">
            <button class="flex items-center space-x-3 p-3 rounded-full hover:bg-gray-100 transition-all duration-200">
              <div class="w-8 h-8 bg-gradient-to-r from-blue-500 to-purple-500 rounded-full flex items-center justify-center shadow-sm">
                <span class="text-white font-semibold text-sm">{{ userInitial }}</span>
              </div>
              <span class="hidden md:block text-sm font-medium text-gray-700">{{ userDisplayName }}</span>
              <svg class="w-4 h-4 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
              </svg>
            </button>
            
            <!-- Dropdown Menu -->
            <div class="absolute right-0 mt-3 w-56 bg-white rounded-xl shadow-2xl border border-gray-200 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 z-50">
              <div class="py-3">
                <button @click="openProfileModal" class="flex items-center px-4 py-3 text-sm text-gray-700 hover:bg-blue-50 hover:text-blue-600 transition-colors w-full text-left">
                  <svg class="w-4 h-4 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                  </svg>
                  Thông tin cá nhân
                </button>
                <button @click="openChangePasswordModal" class="flex items-center px-4 py-3 text-sm text-gray-700 hover:bg-blue-50 hover:text-blue-600 transition-colors w-full text-left">
                  <svg class="w-4 h-4 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 7a2 2 0 012 2m-2-2a2 2 0 00-2 2m2-2h2a2 2 0 012 2v6a2 2 0 01-2 2H9a2 2 0 01-2-2V9a2 2 0 012-2h2m0 0V6a3 3 0 016 0v1m-6 0a3 3 0 106 0v1m-6 0h6"></path>
                  </svg>
                  Đổi mật khẩu
                </button>
                <router-link to="/profile?tab=orders" class="flex items-center px-4 py-3 text-sm text-gray-700 hover:bg-blue-50 hover:text-blue-600 transition-colors">
                  <svg class="w-4 h-4 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                  </svg>
                  Đơn hàng của tôi
                </router-link>
                <router-link to="/profile?tab=addresses" class="flex items-center px-4 py-3 text-sm text-gray-700 hover:bg-blue-50 hover:text-blue-600 transition-colors">
                  <svg class="w-4 h-4 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"></path>
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"></path>
                  </svg>
                  Sổ địa chỉ
                </router-link>
                <router-link to="/wishlist" class="flex items-center px-4 py-3 text-sm text-gray-700 hover:bg-blue-50 hover:text-blue-600 transition-colors">
                  <svg class="w-4 h-4 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"></path>
                  </svg>
                  Yêu thích
                </router-link>
                <hr class="my-2 border-gray-100">
                <button @click="handleLogout" class="flex items-center px-4 py-3 text-sm text-gray-700 hover:bg-red-50 hover:text-red-600 transition-colors w-full text-left">
                  <svg class="w-4 h-4 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path>
                  </svg>
                  Đăng xuất
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Navigation Menu -->
    <nav class="border-t border-gray-200 bg-white">
      <div class="container mx-auto px-4">
        <div class="flex items-center justify-start h-16 space-x-4">
          <!-- Categories Dropdown -->
          <div class="relative" 
               @mouseenter="handleMouseEnter" 
               @mouseleave="handleMouseLeave">
            <button 
              @click="toggleCategories"
              class="flex items-center space-x-3 px-6 py-3 text-gray-700 hover:text-blue-600 hover:bg-blue-50 transition-all duration-200 rounded-lg font-medium"
            >
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>
              </svg>
              <span class="font-semibold">Danh mục</span>
              <svg 
                class="w-4 h-4 transition-transform duration-200" 
                :class="{ 'rotate-180': showCategories }"
                fill="none" 
                stroke="currentColor" 
                viewBox="0 0 24 24"
              >
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
              </svg>
            </button>
            
            <!-- Categories Dropdown -->
            <div 
              v-show="showCategories"
              @mouseenter="handleDropdownEnter"
              @mouseleave="handleDropdownLeave"
              class="absolute left-0 mt-2 w-[500px] bg-white rounded-xl shadow-2xl border border-gray-200 z-50 animate-fadeIn"
            >
              <div class="p-6">
                <div class="grid grid-cols-2 gap-3">
                  <div 
                    v-for="category in categories" 
                    :key="category.name"
                    class="group"
                  >
                    <router-link 
                      :to="`/products?category=${encodeURIComponent(category.name)}`"
                      class="flex items-center justify-between p-4 rounded-xl hover:bg-gradient-to-r hover:from-blue-50 hover:to-indigo-50 hover:text-blue-700 transition-all duration-200 border border-transparent hover:border-blue-200"
                      @click="showCategories = false"
                    >
                      <div class="flex items-center space-x-4">
                        <div class="w-10 h-10 bg-gradient-to-r from-blue-100 to-indigo-100 rounded-xl flex items-center justify-center group-hover:from-blue-200 group-hover:to-indigo-200 transition-all duration-200 shadow-sm">
                          <span class="text-blue-600 text-lg">{{ category.icon }}</span>
                        </div>
                        <div>
                          <span class="font-semibold text-sm text-gray-800 group-hover:text-blue-700">{{ category.name }}</span>
                          <p class="text-xs text-gray-500 mt-1">{{ category.count }} sản phẩm</p>
                        </div>
                      </div>
                      <svg class="w-4 h-4 text-gray-400 group-hover:text-blue-500 transition-colors" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
                      </svg>
                    </router-link>
                  </div>
                </div>
                
                <!-- View All Categories -->
                <div class="mt-6 pt-6 border-t border-gray-100">
                  <router-link 
                    to="/products" 
                    class="flex items-center justify-center w-full py-3 px-6 bg-gradient-to-r from-blue-600 to-indigo-600 text-white rounded-xl hover:from-blue-700 hover:to-indigo-700 transition-all duration-200 font-semibold text-sm shadow-lg hover:shadow-xl transform hover:-translate-y-0.5"
                    @click="showCategories = false"
                  >
                    <span>Xem tất cả danh mục</span>
                    <svg class="w-4 h-4 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"></path>
                    </svg>
                  </router-link>
                </div>
              </div>
            </div>
          </div>

          <!-- Main Navigation -->
          <router-link 
            to="/" 
            class="px-6 py-3 text-gray-700 hover:text-blue-600 hover:bg-blue-50 font-medium transition-all duration-200 rounded-lg"
          >
            Trang chủ
          </router-link>
          <router-link 
            to="/products" 
            class="px-6 py-3 text-gray-700 hover:text-blue-600 hover:bg-blue-50 font-medium transition-all duration-200 rounded-lg"
          >
            Sản phẩm
          </router-link>
          <router-link 
            to="/wishlist" 
            class="px-6 py-3 text-gray-700 hover:text-blue-600 hover:bg-blue-50 font-medium transition-all duration-200 rounded-lg"
          >
            Yêu thích
          </router-link>
          <router-link 
            to="/contact" 
            class="px-6 py-3 text-gray-700 hover:text-blue-600 hover:bg-blue-50 font-medium transition-all duration-200 rounded-lg"
          >
            Liên hệ
          </router-link>
          <router-link 
            to="/order-lookup" 
            class="px-6 py-3 text-gray-700 hover:text-blue-600 hover:bg-blue-50 font-medium transition-all duration-200 rounded-lg"
          >
            Tra cứu đơn hàng
          </router-link>
        </div>
      </div>
    </nav>

    <!-- Profile Modal -->
    <ProfileModal
      :isOpen="showProfileModal"
      @close="showProfileModal = false"
    />

    <!-- Change Password Modal -->
    <ChangePasswordModal
      :isOpen="showChangePasswordModal"
      @close="showChangePasswordModal = false"
    />
  </header>
</template>

<script>
import { computed, ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useRealDataStore } from '../store/realDataStore'
import { useWishlist } from '../store/wishlist'
import { useAuthStore } from '@/store/auth'
import ProfileModal from '@/components/Modals/ProfileModal.vue'
import ChangePasswordModal from '@/components/Modals/ChangePasswordModal.vue'

export default {
  name: 'Header',
  components: {
    ProfileModal,
    ChangePasswordModal
  },
  setup() {
    const store = useRealDataStore()
    const wishlist = useWishlist()
    const authStore = useAuthStore()
    const router = useRouter()
    const cartItemCount = computed(() => store.cartItemCount.value)
    const showCategories = ref(false)
    const wishlistCount = ref(wishlist.getCount())
    const showProfileModal = ref(false)
    const showChangePasswordModal = ref(false)
    let hideTimeout = null

    // Handle logout
    const handleLogout = async () => {
      try {
        authStore.logout()
        await router.push('/')
      } catch (error) {
        console.error('Logout error:', error)
      }
    }

    // Tạo danh mục từ dữ liệu sách
    const categories = computed(() => {
      const categoryCounts = {}
      const categoryIcons = {
        'Văn học': '📚',
        'Kinh doanh': '💼',
        'Phát triển bản thân': '🚀',
        'Kỹ năng sống': '💡',
        'Giáo dục': '🎓',
        'Lịch sử': '📖',
        'Khoa học': '🔬',
        'Thiếu nhi': '🧸',
        'Ngoại ngữ': '🌍',
        'Công nghệ': '💻',
        'Y học': '🏥',
        'Nghệ thuật': '🎨'
      }

      // Đếm số lượng sách theo danh mục - sử dụng .value để lấy giá trị từ computed
      store.products.value.forEach(book => {
        if (categoryCounts[book.category]) {
          categoryCounts[book.category]++
        } else {
          categoryCounts[book.category] = 1
        }
      })

      // Chuyển đổi thành mảng với icon
      return Object.entries(categoryCounts).map(([name, count]) => ({
        name,
        count,
        icon: categoryIcons[name] || '📖'
      })).sort((a, b) => b.count - a.count) // Sắp xếp theo số lượng giảm dần
    })

    const toggleCategories = () => {
      showCategories.value = !showCategories.value
    }

    const handleMouseEnter = () => {
      if (hideTimeout) {
        clearTimeout(hideTimeout)
        hideTimeout = null
      }
      showCategories.value = true
    }

    const handleMouseLeave = () => {
      hideTimeout = setTimeout(() => {
        showCategories.value = false
      }, 300) // Delay 300ms trước khi đóng
    }

    const handleDropdownEnter = () => {
      if (hideTimeout) {
        clearTimeout(hideTimeout)
        hideTimeout = null
      }
    }

    const handleDropdownLeave = () => {
      hideTimeout = setTimeout(() => {
        showCategories.value = false
      }, 300) // Delay 300ms trước khi đóng
    }

    // Update wishlist count when it changes
    const updateWishlistCount = (event) => {
      wishlistCount.value = event.detail.count
    }

    // Navigation functions
    const openProfileModal = () => {
      router.push('/profile?tab=profile')
    }

    const openChangePasswordModal = () => {
      router.push('/profile?tab=security')
    }

    // Computed properties to safely handle string operations
    const userDisplayName = computed(() => authStore.userName || 'User')
    const userInitial = computed(() => (authStore.userName || 'U').charAt(0).toUpperCase())

    // Setup on mount
    onMounted(() => {
      // Listen for wishlist updates
      window.addEventListener('wishlist-updated', updateWishlistCount)
    })

    // Cleanup on unmount
    onUnmounted(() => {
      // Remove event listener
      window.removeEventListener('wishlist-updated', updateWishlistCount)
    })

    return {
      store,
      authStore,
      cartItemCount,
      showCategories,
      categories,
      wishlistCount,
      showProfileModal,
      showChangePasswordModal,
      toggleCategories,
      handleMouseEnter,
      handleMouseLeave,
      handleDropdownEnter,
      handleDropdownLeave,
      handleLogout,
      openProfileModal,
      openChangePasswordModal,
      userDisplayName,
      userInitial
    }
  }
}
</script>

<style scoped>
.animate-fadeIn {
  animation: fadeIn 0.2s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>