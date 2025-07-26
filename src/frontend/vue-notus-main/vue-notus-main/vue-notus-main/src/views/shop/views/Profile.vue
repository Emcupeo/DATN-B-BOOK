<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Breadcrumb -->
    <div class="bg-white border-b border-gray-200">
      <div class="container mx-auto px-4 py-4">
        <nav class="flex" aria-label="Breadcrumb">
          <ol class="inline-flex items-center space-x-1 md:space-x-3">
            <li class="inline-flex items-center">
              <router-link to="/shop" class="inline-flex items-center text-sm font-medium text-gray-700 hover:text-blue-600">
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
                <span class="ml-1 text-sm font-medium text-gray-500 md:ml-2">Tài khoản</span>
              </div>
            </li>
          </ol>
        </nav>
      </div>
    </div>

    <div class="container mx-auto px-4 py-8">
      <div class="grid grid-cols-1 lg:grid-cols-4 gap-8">
        <!-- Sidebar -->
        <div class="lg:col-span-1">
          <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6 sticky top-24">
            <!-- User Info -->
            <div class="text-center mb-6">
              <div class="w-20 h-20 bg-gradient-to-r from-blue-500 to-purple-500 rounded-full flex items-center justify-center mx-auto mb-4">
                <span class="text-white font-bold text-2xl">{{ userInfo.name.charAt(0).toUpperCase() }}</span>
              </div>
              <h3 class="font-semibold text-gray-800">{{ userInfo.name }}</h3>
              <p class="text-sm text-gray-500">{{ userInfo.email }}</p>
            </div>

            <!-- Navigation Tabs -->
            <nav class="space-y-2">
              <button 
                v-for="tab in tabs" 
                :key="tab.id"
                @click="activeTab = tab.id"
                :class="[
                  'w-full text-left px-4 py-3 rounded-lg transition-colors flex items-center space-x-3',
                  activeTab === tab.id
                    ? 'bg-blue-50 text-blue-700 border border-blue-200'
                    : 'text-gray-600 hover:bg-gray-50'
                ]"
              >
                <component :is="tab.icon" class="w-5 h-5" />
                <span class="font-medium">{{ tab.name }}</span>
                <span v-if="tab.badge" class="ml-auto bg-red-500 text-white text-xs rounded-full px-2 py-1">
                  {{ tab.badge }}
                </span>
              </button>
            </nav>

            <!-- Logout -->
            <div class="mt-6 pt-6 border-t border-gray-200">
              <button 
                @click="logout"
                class="w-full text-left px-4 py-3 rounded-lg text-red-600 hover:bg-red-50 transition-colors flex items-center space-x-3"
              >
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path>
                </svg>
                <span class="font-medium">Đăng xuất</span>
              </button>
            </div>
          </div>
        </div>

        <!-- Main Content -->
        <div class="lg:col-span-3">
          <!-- Personal Information Tab -->
          <div v-if="activeTab === 'profile'" class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <div class="flex items-center justify-between mb-6">
              <h2 class="text-xl font-bold text-gray-800">Thông tin cá nhân</h2>
              <button 
                @click="toggleEdit"
                class="text-blue-600 hover:text-blue-700 font-medium"
              >
                {{ isEditing ? 'Hủy' : 'Chỉnh sửa' }}
              </button>
            </div>

            <form @submit.prevent="saveProfile" class="space-y-6">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Họ và tên</label>
                  <input 
                    v-model="userInfo.name"
                    :disabled="!isEditing"
                    type="text" 
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent disabled:bg-gray-50"
                  >
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Email</label>
                  <input 
                    v-model="userInfo.email"
                    :disabled="!isEditing"
                    type="email" 
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent disabled:bg-gray-50"
                  >
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Số điện thoại</label>
                  <input 
                    v-model="userInfo.phone"
                    :disabled="!isEditing"
                    type="tel" 
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent disabled:bg-gray-50"
                  >
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Ngày sinh</label>
                  <input 
                    v-model="userInfo.birthday"
                    :disabled="!isEditing"
                    type="date" 
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent disabled:bg-gray-50"
                  >
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">Giới tính</label>
                <select 
                  v-model="userInfo.gender"
                  :disabled="!isEditing"
                  class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent disabled:bg-gray-50"
                >
                  <option value="">Chọn giới tính</option>
                  <option value="male">Nam</option>
                  <option value="female">Nữ</option>
                  <option value="other">Khác</option>
                </select>
              </div>

              <div v-if="isEditing" class="flex space-x-3">
                <button 
                  type="submit"
                  class="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition-colors"
                >
                  Lưu thay đổi
                </button>
                <button 
                  type="button"
                  @click="cancelEdit"
                  class="bg-gray-100 text-gray-700 px-6 py-2 rounded-lg hover:bg-gray-200 transition-colors"
                >
                  Hủy
                </button>
              </div>
            </form>
          </div>

          <!-- Orders Tab -->
          <div v-if="activeTab === 'orders'" class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h2 class="text-xl font-bold text-gray-800 mb-6">Lịch sử đơn hàng</h2>
            
            <div v-if="orders.length === 0" class="text-center py-12">
              <div class="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <svg class="w-8 h-8 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z"></path>
                </svg>
              </div>
              <h3 class="text-lg font-semibold text-gray-800 mb-2">Chưa có đơn hàng nào</h3>
              <p class="text-gray-600 mb-4">Bạn chưa có đơn hàng nào. Hãy bắt đầu mua sắm!</p>
              <router-link 
                to="/products"
                class="inline-flex items-center px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
              >
                Mua sắm ngay
              </router-link>
            </div>

            <div v-else class="space-y-4">
              <div 
                v-for="order in orders" 
                :key="order.id"
                class="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow"
              >
                <div class="flex items-center justify-between mb-3">
                  <div>
                    <span class="text-sm text-gray-500">Đơn hàng #{{ order.id }}</span>
                    <p class="text-sm text-gray-600">{{ formatDate(order.date) }}</p>
                  </div>
                  <span 
                    :class="[
                      'px-3 py-1 rounded-full text-xs font-medium',
                      getStatusClass(order.status)
                    ]"
                  >
                    {{ getStatusText(order.status) }}
                  </span>
                </div>
                
                <div class="flex items-center space-x-4">
                  <img :src="order.items[0].image" :alt="order.items[0].title" class="w-16 h-16 object-cover rounded">
                  <div class="flex-1">
                    <h4 class="font-medium text-gray-800">{{ order.items[0].title }}</h4>
                    <p class="text-sm text-gray-600">{{ order.items.length }} sản phẩm</p>
                    <p class="text-sm font-medium text-gray-800">{{ formatPrice(order.total) }}</p>
                  </div>
                  <router-link 
                    :to="`/order/${order.id}`"
                    class="text-blue-600 hover:text-blue-700 text-sm font-medium"
                  >
                    Xem chi tiết
                  </router-link>
                </div>
              </div>
            </div>
          </div>

          <!-- Addresses Tab -->
          <div v-if="activeTab === 'addresses'" class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <div class="flex items-center justify-between mb-6">
              <h2 class="text-xl font-bold text-gray-800">Sổ địa chỉ</h2>
              <button 
                @click="showAddAddress = true"
                class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors"
              >
                Thêm địa chỉ mới
              </button>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div 
                v-for="address in addresses" 
                :key="address.id"
                class="border border-gray-200 rounded-lg p-4 relative"
              >
                <div class="flex items-start justify-between mb-3">
                  <h4 class="font-medium text-gray-800">{{ address.name }}</h4>
                  <div class="flex space-x-2">
                    <button 
                      @click="editAddress(address)"
                      class="text-blue-600 hover:text-blue-700 text-sm"
                    >
                      Sửa
                    </button>
                    <button 
                      @click="deleteAddress(address.id)"
                      class="text-red-600 hover:text-red-700 text-sm"
                    >
                      Xóa
                    </button>
                  </div>
                </div>
                <p class="text-sm text-gray-600 mb-2">{{ address.phone }}</p>
                <p class="text-sm text-gray-600">{{ address.fullAddress }}</p>
                <span 
                  v-if="address.isDefault"
                  class="inline-block mt-2 px-2 py-1 bg-green-100 text-green-800 text-xs rounded"
                >
                  Địa chỉ mặc định
                </span>
              </div>
            </div>
          </div>

          <!-- Security Tab -->
          <div v-if="activeTab === 'security'" class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h2 class="text-xl font-bold text-gray-800 mb-6">Bảo mật</h2>
            
            <div class="space-y-6">
              <!-- Change Password -->
              <div class="border border-gray-200 rounded-lg p-6">
                <h3 class="text-lg font-semibold text-gray-800 mb-4">Đổi mật khẩu</h3>
                <form @submit.prevent="changePassword" class="space-y-4">
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Mật khẩu hiện tại</label>
                    <input 
                      v-model="passwordForm.currentPassword"
                      type="password" 
                      class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    >
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Mật khẩu mới</label>
                    <input 
                      v-model="passwordForm.newPassword"
                      type="password" 
                      class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    >
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Xác nhận mật khẩu mới</label>
                    <input 
                      v-model="passwordForm.confirmPassword"
                      type="password" 
                      class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    >
                  </div>
                  <button 
                    type="submit"
                    class="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition-colors"
                  >
                    Đổi mật khẩu
                  </button>
                </form>
              </div>

              <!-- Two-Factor Authentication -->
              <div class="border border-gray-200 rounded-lg p-6">
                <div class="flex items-center justify-between">
                  <div>
                    <h3 class="text-lg font-semibold text-gray-800">Xác thực 2 yếu tố</h3>
                    <p class="text-sm text-gray-600 mt-1">Bảo vệ tài khoản bằng mã xác thực</p>
                  </div>
                  <button 
                    @click="toggleTwoFactor"
                    :class="[
                      'px-4 py-2 rounded-lg transition-colors',
                      twoFactorEnabled
                        ? 'bg-red-600 text-white hover:bg-red-700'
                        : 'bg-green-600 text-white hover:bg-green-700'
                    ]"
                  >
                    {{ twoFactorEnabled ? 'Tắt' : 'Bật' }}
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- Reviews Tab -->
          <div v-if="activeTab === 'reviews'" class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h2 class="text-xl font-bold text-gray-800 mb-6">Đánh giá của tôi</h2>
            
            <div v-if="reviews.length === 0" class="text-center py-12">
              <div class="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <svg class="w-8 h-8 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"></path>
                </svg>
              </div>
              <h3 class="text-lg font-semibold text-gray-800 mb-2">Chưa có đánh giá nào</h3>
              <p class="text-gray-600">Bạn chưa đánh giá sản phẩm nào. Hãy mua sắm và đánh giá!</p>
            </div>

            <div v-else class="space-y-4">
              <div 
                v-for="review in reviews" 
                :key="review.id"
                class="border border-gray-200 rounded-lg p-4"
              >
                <div class="flex items-start space-x-4">
                  <img :src="review.bookImage" :alt="review.bookTitle" class="w-16 h-16 object-cover rounded">
                  <div class="flex-1">
                    <h4 class="font-medium text-gray-800">{{ review.bookTitle }}</h4>
                    <div class="flex items-center mt-1">
                      <div class="flex text-yellow-400">
                        <span v-for="star in 5" :key="star" class="text-sm">
                          <span v-html="star <= review.rating ? '&#9733;' : '&#9734;'"></span>
                        </span>
                      </div>
                      <span class="text-sm text-gray-500 ml-2">{{ formatDate(review.date) }}</span>
                    </div>
                    <p class="text-sm text-gray-600 mt-2">{{ review.comment }}</p>
                  </div>
                  <button class="text-blue-600 hover:text-blue-700 text-sm">
                    Chỉnh sửa
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'

export default {
  name: 'Profile',
  setup() {
    const activeTab = ref('profile')
    const isEditing = ref(false)
    const showAddAddress = ref(false)
    const twoFactorEnabled = ref(false)

    // User information
    const userInfo = reactive({
      name: 'Nguyễn Văn A',
      email: 'nguyenvana@email.com',
      phone: '0123456789',
      birthday: '1990-01-01',
      gender: 'male'
    })

    // Password form
    const passwordForm = reactive({
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    })

    // Sample data
    const orders = ref([
      {
        id: 'ORD001',
        date: '2024-01-15',
        status: 'delivered',
        total: 250000,
        items: [
          {
            title: 'Đắc Nhân Tâm',
            image: 'https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=400'
          }
        ]
      },
      {
        id: 'ORD002',
        date: '2024-01-10',
        status: 'processing',
        total: 180000,
        items: [
          {
            title: 'Sapiens: Lược sử loài người',
            image: 'https://images.pexels.com/photos/1029141/pexels-photo-1029141.jpeg?auto=compress&cs=tinysrgb&w=400'
          }
        ]
      }
    ])

    const addresses = ref([
      {
        id: 1,
        name: 'Nhà riêng',
        phone: '0123456789',
        fullAddress: '123 Đường ABC, Quận 1, TP. Hồ Chí Minh',
        isDefault: true
      },
      {
        id: 2,
        name: 'Công ty',
        phone: '0123456789',
        fullAddress: '456 Đường XYZ, Quận 3, TP. Hồ Chí Minh',
        isDefault: false
      }
    ])

    const reviews = ref([
      {
        id: 1,
        bookTitle: 'Đắc Nhân Tâm',
        bookImage: 'https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=400',
        rating: 5,
        comment: 'Cuốn sách rất hay và bổ ích. Tôi đã học được nhiều điều từ cuốn sách này.',
        date: '2024-01-10'
      }
    ])

    // Tab configuration
    const tabs = [
      {
        id: 'profile',
        name: 'Thông tin cá nhân',
        icon: 'UserIcon'
      },
      {
        id: 'orders',
        name: 'Đơn hàng',
        icon: 'ShoppingBagIcon',
        badge: orders.value.length
      },
      {
        id: 'addresses',
        name: 'Sổ địa chỉ',
        icon: 'LocationMarkerIcon'
      },
      {
        id: 'security',
        name: 'Bảo mật',
        icon: 'ShieldCheckIcon'
      },
      {
        id: 'reviews',
        name: 'Đánh giá',
        icon: 'StarIcon'
      }
    ]

    // Methods
    const toggleEdit = () => {
      isEditing.value = !isEditing.value
    }

    const saveProfile = () => {
      // Save profile logic
      console.log('Saving profile:', userInfo)
      isEditing.value = false
    }

    const cancelEdit = () => {
      isEditing.value = false
    }

    const changePassword = () => {
      // Change password logic
      console.log('Changing password:', passwordForm)
    }

    const toggleTwoFactor = () => {
      twoFactorEnabled.value = !twoFactorEnabled.value
    }

    const logout = () => {
      // Logout logic
      console.log('Logging out...')
    }

    const editAddress = (address) => {
      console.log('Editing address:', address)
    }

    const deleteAddress = (id) => {
      addresses.value = addresses.value.filter(addr => addr.id !== id)
    }

    const formatDate = (date) => {
      return new Date(date).toLocaleDateString('vi-VN')
    }

    const formatPrice = (price) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(price)
    }

    const getStatusText = (status) => {
      const statusMap = {
        'pending': 'Chờ xử lý',
        'processing': 'Đang xử lý',
        'shipped': 'Đã gửi hàng',
        'delivered': 'Đã giao hàng',
        'cancelled': 'Đã hủy'
      }
      return statusMap[status] || status
    }

    const getStatusClass = (status) => {
      const classMap = {
        'pending': 'bg-yellow-100 text-yellow-800',
        'processing': 'bg-blue-100 text-blue-800',
        'shipped': 'bg-purple-100 text-purple-800',
        'delivered': 'bg-green-100 text-green-800',
        'cancelled': 'bg-red-100 text-red-800'
      }
      return classMap[status] || 'bg-gray-100 text-gray-800'
    }

    // Icon components
    const UserIcon = {
      template: `<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
      </svg>`
    }

    const ShoppingBagIcon = {
      template: `<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z"></path>
      </svg>`
    }

    const LocationMarkerIcon = {
      template: `<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"></path>
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"></path>
      </svg>`
    }

    const ShieldCheckIcon = {
      template: `<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"></path>
      </svg>`
    }

    const StarIcon = {
      template: `<svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"></path>
      </svg>`
    }

    return {
      activeTab,
      isEditing,
      showAddAddress,
      twoFactorEnabled,
      userInfo,
      passwordForm,
      orders,
      addresses,
      reviews,
      tabs,
      toggleEdit,
      saveProfile,
      cancelEdit,
      changePassword,
      toggleTwoFactor,
      logout,
      editAddress,
      deleteAddress,
      formatDate,
      formatPrice,
      getStatusText,
      getStatusClass,
      UserIcon,
      ShoppingBagIcon,
      LocationMarkerIcon,
      ShieldCheckIcon,
      StarIcon
    }
  }
}
</script> 