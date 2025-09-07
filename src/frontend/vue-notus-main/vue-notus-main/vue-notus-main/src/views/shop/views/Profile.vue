<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Loading State -->
    <div v-if="loading" class="min-h-screen flex items-center justify-center">
      <div class="text-center">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
        <p class="text-gray-600">Đang tải...</p>
      </div>
    </div>

    <!-- Not Authenticated State -->
    <div v-else-if="!isAuthenticated" class="min-h-screen flex items-center justify-center">
      <div class="text-center">
        <div class="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
          <svg class="w-8 h-8 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
          </svg>
        </div>
        <h3 class="text-lg font-semibold text-gray-800 mb-2">Vui lòng đăng nhập</h3>
        <p class="text-gray-600 mb-4">Bạn cần đăng nhập để xem thông tin tài khoản</p>
        <router-link 
          to="/auth/login"
          class="inline-flex items-center px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
        >
          Đăng nhập
        </router-link>
      </div>
    </div>

    <!-- Main Content -->
    <div v-else>
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
                  <span class="ml-1 text-sm font-medium text-gray-500 md:ml-2">Tài khoản</span>
                </div>
              </li>
            </ol>
          </nav>
        </div>
      </div>


      <!-- Main Content Area -->
      <div class="container mx-auto px-4 py-8">
        <div class="grid grid-cols-1 lg:grid-cols-4 gap-8">
          <!-- Sidebar -->
          <div class="lg:col-span-1">
            <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6 sticky top-24">
              <!-- User Avatar -->
              <div class="text-center mb-6">
                <div class="w-20 h-20 bg-gradient-to-r from-blue-500 to-purple-500 rounded-full flex items-center justify-center mx-auto mb-4">
                  <span class="text-2xl font-bold text-white">{{ userInfo.name ? userInfo.name.charAt(0).toUpperCase() : 'U' }}</span>
                </div>
                <h2 class="text-xl font-semibold text-gray-900">{{ userInfo.name || 'Người dùng' }}</h2>
                <p class="text-gray-600">{{ userInfo.email }}</p>
              </div>

              <!-- Navigation Tabs -->
              <div class="mt-6 pt-6 border-t border-gray-200">
                <nav class="space-y-2">
                  <button 
                    v-for="tab in tabs" 
                    :key="tab.id"
                    @click="activeTab = tab.id"
                    :class="[
                      'w-full flex items-center px-4 py-3 text-left rounded-lg transition-colors',
                      activeTab === tab.id 
                        ? 'bg-blue-50 text-blue-700 border border-blue-200' 
                        : 'text-gray-700 hover:bg-gray-50'
                    ]"
                  >
                    <component :is="tab.icon" class="w-5 h-5 mr-3" />
                    {{ tab.name }}
                  </button>
                </nav>
              </div>
            </div>
          </div>

          <!-- Main Content -->
          <div class="lg:col-span-3">
            <!-- Profile Tab -->
            <div v-if="activeTab === 'profile'" class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
              <div class="flex items-center justify-between mb-6">
                <h3 class="text-xl font-semibold text-gray-900">Thông tin cá nhân</h3>
                <button 
                  @click="toggleEdit"
                  class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
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
                      class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-50"
                    >
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Email</label>
                    <input 
                      v-model="userInfo.email"
                      :disabled="!isEditing"
                      type="email" 
                      class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-50"
                    >
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Số điện thoại</label>
                    <input 
                      v-model="userInfo.phone"
                      :disabled="!isEditing"
                      type="tel" 
                      class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-50"
                    >
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Ngày sinh</label>
                    <input 
                      v-model="userInfo.birthday"
                      :disabled="!isEditing"
                      type="date" 
                      class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-50"
                    >
                  </div>
                </div>

                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Giới tính</label>
                  <select 
                    v-model="userInfo.gender"
                    :disabled="!isEditing"
                    class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-50"
                  >
                    <option value="">Chọn giới tính</option>
                    <option value="1">Nam</option>
                    <option value="0">Nữ</option>
                    <option value="2">Khác</option>
                  </select>
                </div>

                <div v-if="isEditing" class="flex space-x-3">
                  <button 
                    type="submit"
                    class="px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
                  >
                    Lưu thay đổi
                  </button>
                  <button 
                    type="button"
                    @click="cancelEdit"
                    class="px-6 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
                  >
                    Hủy
                  </button>
                </div>
              </form>
            </div>

            <!-- Orders Tab -->
            <div v-if="activeTab === 'orders'" class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
              <div class="flex items-center justify-between mb-6">
                <h3 class="text-xl font-semibold text-gray-900">Lịch sử đơn hàng</h3>
                <button 
                  @click="loadOrders"
                  :disabled="loading"
                  class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  <i class="fas fa-sync-alt mr-2"></i>
                  {{ loading ? 'Đang tải...' : 'Làm mới' }}
                </button>
              </div>
              
              <!-- Error Message -->
              <div v-if="error" class="mb-4 p-4 bg-red-50 border border-red-200 rounded-lg">
                <div class="flex items-center">
                  <i class="fas fa-exclamation-triangle text-red-500 mr-2"></i>
                  <span class="text-red-700">{{ error }}</span>
                </div>
              </div>

              <div v-if="orders.length === 0 && !loading" class="text-center py-12">
                <div class="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
                  <ShoppingBagIcon class="w-8 h-8 text-gray-400" />
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

              <!-- Loading State -->
              <div v-if="loading" class="text-center py-12">
                <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
                  <i class="fas fa-spinner fa-spin text-blue-600 text-xl"></i>
                </div>
                <p class="text-gray-600">Đang tải lịch sử đơn hàng...</p>
              </div>

              <div v-else class="space-y-4">
                <div 
                  v-for="order in orders" 
                  :key="order.id"
                  class="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow"
                >
                  <div class="flex items-center justify-between mb-3">
                    <div>
                      <h4 class="font-medium text-gray-900">Đơn hàng #{{ order.maHoaDon }}</h4>
                      <p class="text-sm text-gray-600">{{ formatDate(order.ngayDatHang) }}</p>
                      <p class="text-xs text-gray-500">ID: {{ order.id }}</p>
                    </div>
                    <span :class="getStatusClass(order.trangThai)" class="px-3 py-1 rounded-full text-sm font-medium">
                      {{ getStatusText(order.trangThai) }}
                    </span>
                  </div>
                  
                  <div class="flex items-center space-x-4">
                    <div v-if="(order.chiTietHoaDonList || order.hoaDonChiTiets) && (order.chiTietHoaDonList || order.hoaDonChiTiets).length > 0" class="w-16 h-16 bg-gray-100 rounded flex items-center justify-center">
                      <span class="text-2xl">📚</span>
                    </div>
                    <div class="flex-1">
                      <p class="text-sm text-gray-600">
                        {{ (order.chiTietHoaDonList || order.hoaDonChiTiets) ? (order.chiTietHoaDonList || order.hoaDonChiTiets).length : 0 }} sản phẩm
                      </p>
                      <p class="font-medium text-gray-900">{{ formatPrice(order.tongTien) }}</p>
                      <!-- Hiển thị chi tiết sản phẩm nếu có -->
                      <div v-if="(order.chiTietHoaDonList || order.hoaDonChiTiets) && (order.chiTietHoaDonList || order.hoaDonChiTiets).length > 0" class="mt-2">
                        <div v-for="(item, index) in (order.chiTietHoaDonList || order.hoaDonChiTiets)" :key="index" class="text-xs text-gray-500">
                          • {{ item.chiTietSanPham?.tenChiTietSanPham || item.chiTietSanPham?.tenSanPham || 'Sản phẩm' }} (x{{ item.soLuong }}) - {{ formatPrice(item.giaSanPham) }}
                        </div>
                      </div>
                    </div>
                    <div class="text-right">
                      <p class="text-sm text-gray-600">Tổng tiền</p>
                      <p class="font-semibold text-gray-900">{{ formatPrice(order.tongTien) }}</p>
                      <button 
                        @click="viewOrderDetails(order)"
                        class="mt-2 px-3 py-1 bg-blue-500 text-white text-sm rounded hover:bg-blue-600 transition-colors"
                      >
                        Xem chi tiết
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- Addresses Tab -->
            <div v-if="activeTab === 'addresses'" class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
              <div class="flex items-center justify-between mb-6">
                <h3 class="text-xl font-semibold text-gray-900">Sổ địa chỉ</h3>
                <button 
                  @click="showAddAddress = true"
                  class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
                >
                  Thêm địa chỉ mới
                </button>
              </div>

              <!-- Empty state when no addresses -->
              <div v-if="addresses.length === 0" class="text-center py-12">
                <div class="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
                  <svg class="w-8 h-8 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"></path>
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"></path>
                  </svg>
                </div>
                <h3 class="text-lg font-medium text-gray-900 mb-2">Chưa có địa chỉ nào</h3>
                <p class="text-gray-600 mb-6">Bạn chưa có địa chỉ nào được lưu. Hãy thêm địa chỉ đầu tiên của bạn.</p>
                <button 
                  @click="showAddAddress = true"
                  class="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
                >
                  Thêm địa chỉ đầu tiên
                </button>
              </div>

              <!-- Address list when addresses exist -->
              <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div 
                  v-for="address in addresses" 
                  :key="address.id"
                  class="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow"
                >
                  <div class="flex items-start justify-between mb-3">
                    <h4 class="font-medium text-gray-900">{{ address.fullName || address.name }}</h4>
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
                  <p class="text-sm text-gray-600 mb-2">
                    <i class="fas fa-phone text-xs mr-1"></i>
                    {{ address.phone }}
                  </p>
                  <p class="text-sm text-gray-700">
                    <i class="fas fa-map-marker-alt text-xs mr-1"></i>
                    {{ getFullAddressDisplay(address) }}
                  </p>
                  <div v-if="address.isDefault" class="mt-2">
                    <span class="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                      Địa chỉ mặc định
                    </span>
                  </div>
                </div>
              </div>
            </div>

            <!-- Security Tab -->
            <div v-if="activeTab === 'security'" class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
              <h3 class="text-xl font-semibold text-gray-900 mb-6">Bảo mật</h3>
              
              <div class="space-y-6">
                <!-- Change Password -->
                <div class="border border-gray-200 rounded-lg p-6">
                  <h4 class="text-lg font-medium text-gray-900 mb-4">Đổi mật khẩu</h4>
                  <form @submit.prevent="changePassword" class="space-y-4">
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-2">Mật khẩu hiện tại</label>
                      <input 
                        v-model="passwordForm.currentPassword"
                        type="password" 
                        class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                      >
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-2">Mật khẩu mới</label>
                      <input 
                        v-model="passwordForm.newPassword"
                        type="password" 
                        class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                      >
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-2">Xác nhận mật khẩu mới</label>
                      <input 
                        v-model="passwordForm.confirmPassword"
                        type="password" 
                        class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                      >
                    </div>
                    <button 
                      type="submit"
                      class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
                    >
                      Đổi mật khẩu
                    </button>
                  </form>
                </div>

              </div>
            </div>

            <!-- Reviews Tab -->
            <div v-if="activeTab === 'reviews'" class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
              <h3 class="text-xl font-semibold text-gray-900 mb-6">Đánh giá của tôi</h3>
              
              <div v-if="reviews.length === 0" class="text-center py-12">
                <div class="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
                  <StarIcon class="w-8 h-8 text-gray-400" />
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
                    <div class="w-12 h-12 bg-gray-100 rounded flex items-center justify-center">
                      <span class="text-lg">📚</span>
                    </div>
                    <div class="flex-1">
                      <h4 class="font-medium text-gray-900">{{ review.productName }}</h4>
                      <div class="flex items-center mt-1">
                        <div class="flex text-yellow-400">
                          <span v-for="i in 5" :key="i" class="text-sm" v-html="i <= review.rating ? '★' : '☆'">
                          </span>
                        </div>
                        <span class="ml-2 text-sm text-gray-600">{{ formatDate(review.date) }}</span>
                      </div>
                      <p class="text-gray-700 mt-2">{{ review.comment }}</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Add Address Modal -->
    <div v-if="showAddAddress" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-xl shadow-xl p-6 max-w-2xl w-full mx-4 max-h-[90vh] overflow-y-auto">
        <div class="flex items-center justify-between mb-6">
          <h2 class="text-xl font-semibold text-gray-900">Thêm địa chỉ mới</h2>
          <button @click="showAddAddress = false" class="text-gray-400 hover:text-gray-600">
            <i class="fas fa-times text-xl"></i>
          </button>
        </div>
        
        <form @submit.prevent="saveAddress" class="space-y-4">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Họ và tên *</label>
              <input 
                v-model="newAddress.fullName" 
                type="text" 
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                required
              >
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Số điện thoại *</label>
              <input 
                v-model="newAddress.phone" 
                type="tel" 
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                :class="{ 'border-red-500': newAddress.phone && !validatePhoneNumber(newAddress.phone) }"
                placeholder="Nhập số điện thoại (VD: 0123456789)"
                required
                @blur="validatePhoneField"
              >
              <div v-if="newAddress.phone && !validatePhoneNumber(newAddress.phone)" class="text-red-500 text-xs mt-1">
                <i class="fas fa-exclamation-triangle mr-1"></i>
                Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại Việt Nam (10-11 số)
              </div>
            </div>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Địa chỉ chi tiết *</label>
            <input 
              v-model="newAddress.address" 
              type="text" 
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Số nhà, tên đường..."
              required
            >
          </div>
          
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Tỉnh/Thành phố *</label>
              <select 
                v-model="newAddress.city" 
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                :disabled="loadingProvinces"
                required
              >
                <option value="">Chọn tỉnh/thành phố</option>
                <option v-for="province in provinces" :key="province.code" :value="province.code">
                  {{ province.name }}
                </option>
              </select>
              <div v-if="loadingProvinces" class="text-sm text-gray-500 mt-1">Đang tải...</div>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Quận/Huyện *</label>
              <select 
                v-model="newAddress.district" 
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                :disabled="loadingDistricts || !newAddress.city"
                required
              >
                <option value="">Chọn quận/huyện</option>
                <option v-for="district in districts" :key="district.code" :value="district.code">
                  {{ district.name }}
                </option>
              </select>
              <div v-if="loadingDistricts" class="text-sm text-gray-500 mt-1">Đang tải...</div>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Xã/Phường *</label>
              <select 
                v-model="newAddress.ward" 
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                :disabled="loadingWards || !newAddress.district"
                required
              >
                <option value="">Chọn xã/phường</option>
                <option v-for="ward in wards" :key="ward.code" :value="ward.code">
                  {{ ward.name }}
                </option>
              </select>
              <div v-if="loadingWards" class="text-sm text-gray-500 mt-1">Đang tải...</div>
            </div>
          </div>
          
          <div class="flex items-center">
            <input 
              v-model="newAddress.isDefault" 
              type="checkbox" 
              id="defaultAddress"
              class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
            >
            <label for="defaultAddress" class="ml-2 block text-sm text-gray-700">
              Đặt làm địa chỉ mặc định
            </label>
          </div>
          
          <div class="flex space-x-3 pt-4">
            <button 
              type="submit" 
              class="flex-1 bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              Lưu địa chỉ
            </button>
            <button 
              type="button" 
              @click="showAddAddress = false" 
              class="flex-1 border border-gray-300 text-gray-700 py-2 rounded-lg hover:bg-gray-50"
            >
              Hủy
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Edit Address Modal -->
    <div v-if="showEditAddress" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-xl shadow-xl p-6 max-w-2xl w-full mx-4 max-h-[90vh] overflow-y-auto">
        <div class="flex items-center justify-between mb-6">
          <h2 class="text-xl font-semibold text-gray-900">Sửa địa chỉ</h2>
          <button @click="showEditAddress = false; editingAddress = null" class="text-gray-400 hover:text-gray-600">
            <i class="fas fa-times text-xl"></i>
          </button>
        </div>
        
        <form @submit.prevent="updateAddress" class="space-y-4">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Họ và tên *</label>
              <input 
                v-model="newAddress.fullName" 
                type="text" 
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                required
              >
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Số điện thoại *</label>
              <input 
                v-model="newAddress.phone" 
                type="tel" 
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                :class="{ 'border-red-500': newAddress.phone && !validatePhoneNumber(newAddress.phone) }"
                placeholder="Nhập số điện thoại (VD: 0123456789)"
                required
                @blur="validatePhoneField"
              >
              <div v-if="newAddress.phone && !validatePhoneNumber(newAddress.phone)" class="text-red-500 text-xs mt-1">
                <i class="fas fa-exclamation-triangle mr-1"></i>
                Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại Việt Nam (10-11 số)
              </div>
            </div>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Địa chỉ chi tiết *</label>
            <input 
              v-model="newAddress.address" 
              type="text" 
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Số nhà, tên đường..."
              required
            >
          </div>
          
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Tỉnh/Thành phố *</label>
              <select 
                v-model="newAddress.city" 
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                :disabled="loadingProvinces"
                required
              >
                <option value="">Chọn tỉnh/thành phố</option>
                <option v-for="province in provinces" :key="province.code" :value="province.code">
                  {{ province.name }}
                </option>
              </select>
              <div v-if="loadingProvinces" class="text-sm text-gray-500 mt-1">Đang tải...</div>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Quận/Huyện *</label>
              <select 
                v-model="newAddress.district" 
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                :disabled="loadingDistricts || !newAddress.city"
                required
              >
                <option value="">Chọn quận/huyện</option>
                <option v-for="district in districts" :key="district.code" :value="district.code">
                  {{ district.name }}
                </option>
              </select>
              <div v-if="loadingDistricts" class="text-sm text-gray-500 mt-1">Đang tải...</div>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Xã/Phường *</label>
              <select 
                v-model="newAddress.ward" 
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                :disabled="loadingWards || !newAddress.district"
                required
              >
                <option value="">Chọn xã/phường</option>
                <option v-for="ward in wards" :key="ward.code" :value="ward.code">
                  {{ ward.name }}
                </option>
              </select>
              <div v-if="loadingWards" class="text-sm text-gray-500 mt-1">Đang tải...</div>
            </div>
          </div>
          
          <div class="flex items-center">
            <input 
              v-model="newAddress.isDefault" 
              type="checkbox" 
              id="editDefaultAddress"
              class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
            >
            <label for="editDefaultAddress" class="ml-2 block text-sm text-gray-700">
              Đặt làm địa chỉ mặc định
            </label>
          </div>
          
          <div class="flex space-x-3 pt-4">
            <button 
              type="submit" 
              class="flex-1 bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              Cập nhật địa chỉ
            </button>
            <button 
              type="button" 
              @click="showEditAddress = false; editingAddress = null" 
              class="flex-1 border border-gray-300 text-gray-700 py-2 rounded-lg hover:bg-gray-50"
            >
              Hủy
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import hoaDonService from '@/service/hoaDonService'
import AddressService from '@/service/AddressService'

export default {
  name: 'Profile',
  setup() {
    const router = useRouter()
    const route = useRoute()
    const authStore = useAuthStore()
    
    // Set active tab from query parameter
    const activeTab = ref(route.query.tab || 'profile')
    const isEditing = ref(false)
    const showAddAddress = ref(false)
    const showEditAddress = ref(false)
    const editingAddress = ref(null)
    const loading = ref(false)
    const error = ref('')

    // User information - lấy từ auth store
    const userInfo = reactive({
      name: '',
      email: '',
      phone: '',
      birthday: '',
      gender: '',
      id: null
    })

    // Computed để kiểm tra authentication
    const isAuthenticated = computed(() => {
      return authStore.isAuthenticated && authStore.user
    })

    // Password form
    const passwordForm = reactive({
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    })

    // New address form
    const newAddress = reactive({
      fullName: '',
      phone: '',
      address: '',
      city: '',
      district: '',
      ward: '',
      isDefault: false
    })

    // Address API data
    const provinces = ref([])
    const districts = ref([])
    const wards = ref([])
    const loadingProvinces = ref(false)
    const loadingDistricts = ref(false)
    const loadingWards = ref(false)

    // Sample data
    const orders = ref([])
    const addresses = ref([])
    const reviews = ref([])

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

    // Load user data from customer API instead of auth store
    const loadUserData = async () => {
      console.log('👤 loadUserData called, authStore.user:', authStore.user)
      console.log('👤 authStore.user.khachHangId:', authStore.user?.khachHangId)
      
      if (authStore.user && authStore.user.khachHangId) {
        try {
          // Lấy dữ liệu từ API khách hàng để đảm bảo đồng bộ với admin
          const response = await fetch(`http://localhost:8080/api/khach-hang/${authStore.user.khachHangId}`, {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
          })
          
          if (response.ok) {
            const customerData = await response.json()
            userInfo.name = customerData.hoTen || ''
            userInfo.email = customerData.email || ''
            userInfo.phone = customerData.soDienThoai || ''
            userInfo.birthday = customerData.ngaySinh || ''
            userInfo.gender = customerData.gioiTinh !== null ? customerData.gioiTinh.toString() : ''
            userInfo.id = customerData.id
            console.log('👤 Set userInfo.id from API:', customerData.id)
          } else {
            // Fallback to auth store if API fails
            userInfo.name = authStore.user.hoTen || authStore.user.name || ''
            userInfo.email = authStore.user.email || ''
            userInfo.phone = authStore.user.soDienThoai || authStore.user.phone || ''
            userInfo.birthday = authStore.user.ngaySinh || authStore.user.birthday || ''
            userInfo.gender = authStore.user.gioiTinh || authStore.user.gender || ''
            userInfo.id = authStore.user.khachHangId || authStore.user.id
            console.log('👤 Set userInfo.id from fallback:', userInfo.id)
          }
        } catch (error) {
          console.error('❌ Error loading customer data:', error)
          // Fallback to auth store if API fails
          userInfo.name = authStore.user.hoTen || authStore.user.name || ''
          userInfo.email = authStore.user.email || ''
          userInfo.phone = authStore.user.soDienThoai || authStore.user.phone || ''
          userInfo.birthday = authStore.user.ngaySinh || authStore.user.birthday || ''
          userInfo.gender = authStore.user.gioiTinh || authStore.user.gender || ''
          userInfo.id = authStore.user.khachHangId || authStore.user.id
          console.log('👤 Set userInfo.id from catch fallback:', userInfo.id)
        }
      } else if (authStore.user) {
        // Fallback to auth store if no khachHangId
        console.log('👤 Auth store user (fallback):', authStore.user)
        userInfo.name = authStore.user.hoTen || authStore.user.name || ''
        userInfo.email = authStore.user.email || ''
        userInfo.phone = authStore.user.soDienThoai || authStore.user.phone || ''
        userInfo.birthday = authStore.user.ngaySinh || authStore.user.birthday || ''
        userInfo.gender = authStore.user.gioiTinh || authStore.user.gender || ''
        userInfo.id = authStore.user.khachHangId || authStore.user.id
        console.log('👤 Set userInfo.id to (fallback):', userInfo.id)
        console.log('👤 Available IDs (fallback):', {
          khachHangId: authStore.user.khachHangId,
          id: authStore.user.id,
          finalId: userInfo.id
        })
      } else {
        console.log('👤 No authStore.user found')
      }
      
      console.log('👤 Final userInfo after loadUserData:', userInfo)
    }

    // Load orders from backend
    const viewOrderDetails = (order) => {
      console.log('🔍 Navigating to order details:', order)
      router.push(`/order/${order.id}`)
    }

    const updateOrderDataFromAPI = async (order) => {
      try {
        console.log(`🔄 Updating order data from API for order ${order.id}...`)
        
        // Lấy dữ liệu đầy đủ từ API
        const response = await fetch(`http://localhost:8080/api/hoa-don/${order.id}`)
        if (response.ok) {
          const fullOrderData = await response.json()
          console.log(`🔄 Full order data from API:`, fullOrderData)
          
          // Cập nhật dữ liệu trong order object
          order.ngayDatHang = fullOrderData.ngayDatHang
          order.tongTien = fullOrderData.tongTien
          order.hoaDonChiTiets = fullOrderData.hoaDonChiTiets
          order.trangThai = fullOrderData.trangThai
          
          console.log(`✅ Updated order data:`, {
            id: order.id,
            maHoaDon: order.maHoaDon,
            ngayDatHang: order.ngayDatHang,
            tongTien: order.tongTien,
            hoaDonChiTietsLength: order.hoaDonChiTiets ? order.hoaDonChiTiets.length : 0,
            trangThai: order.trangThai
          })
        } else {
          console.error(`❌ Failed to update order ${order.id}:`, response.status)
        }
      } catch (error) {
        console.error(`❌ Error updating order ${order.id}:`, error)
      }
    }


    const loadOrders = async () => {
      if (!isAuthenticated.value) return
      
      try {
        loading.value = true
        console.log('🛒 Loading orders for user:', userInfo)
        console.log('🛒 User ID:', userInfo?.id)
        
        const response = await hoaDonService.getListHoaDon()
        console.log('🛒 Raw orders response:', response)
        console.log('🛒 Response structure:', {
          data: response.data,
          dataType: typeof response.data,
          isArray: Array.isArray(response.data),
          hasContent: response.data?.content,
          contentLength: response.data?.content?.length
        })
        
        // Handle different response structures
        let ordersData = []
        if (response.data) {
          if (Array.isArray(response.data)) {
            // Direct array response
            ordersData = response.data
            console.log('🛒 Using direct array response, length:', ordersData.length)
          } else if (response.data.content && Array.isArray(response.data.content)) {
            // Paginated response with content
            ordersData = response.data.content
            console.log('🛒 Using paginated response, content length:', ordersData.length)
          } else if (response.data.data && Array.isArray(response.data.data)) {
            // Nested data response
            ordersData = response.data.data
            console.log('🛒 Using nested data response, length:', ordersData.length)
          }
        }
        
        if (ordersData.length > 0) {
          console.log('🛒 All orders from API:', ordersData)
          console.log('🛒 First order structure:', ordersData[0])
          console.log('🛒 Sample order khachHang:', ordersData[0]?.khachHang)
          
           // SECURITY: Get current customer ID and validate
           const customerId = userInfo?.id
           const authUserId = authStore.user?.id
           const authKhachHangId = authStore.user?.khachHangId
           
           console.log('🛒 Security validation - Customer ID:', customerId)
           console.log('🛒 Auth user ID:', authUserId)
           console.log('🛒 Auth khachHangId:', authKhachHangId)
           
           // SECURITY CHECK: If no valid customer ID, show empty list
           if (!customerId || customerId <= 0) {
             console.log('🚨 SECURITY: No valid customer ID found - showing empty orders list')
             orders.value = []
             return
           }
          
           // SECURITY: Only show orders that belong to the current customer
           const customerOrders = ordersData.filter(order => {
             // Get customer ID from order (try multiple possible fields)
             const orderCustomerId = order.khachHang?.id || order.khachHangId || order.customerId || order.idKhachHang
             
             // Only match if we have a valid customer ID and it matches the current user
             const hasValidCustomerId = customerId && customerId > 0
             const matches = hasValidCustomerId && (
               orderCustomerId === customerId ||
               order.khachHangId === customerId
             )
             
             console.log('🛒 Security check for order:', {
               orderId: order.id,
               orderCustomerId: orderCustomerId,
               orderKhachHangId: order.khachHangId,
               currentCustomerId: customerId,
               hasValidCustomerId: hasValidCustomerId,
               matches: matches,
               securityNote: matches ? '✅ Authorized' : '❌ Blocked for security'
             })
             
             return matches
           })
          
          console.log('🛒 Filtered customer orders:', customerOrders)
          
           // Only show orders that belong to the current customer - NEVER show all orders as fallback
           if (customerOrders.length === 0) {
             console.log('🛒 No orders found for this customer - showing empty list for security')
             orders.value = []
           } else {
             console.log('🛒 Showing customer-specific orders only:', customerOrders.length)
             orders.value = customerOrders.map(order => ({
               id: order.id,
               maHoaDon: order.maHoaDon,
               date: order.ngayDatHang || order.ngayTao,
               status: order.trangThai,
               total: order.tongTien,
               items: order.chiTietHoaDonList || []
             }))
           }
          
          console.log('🛒 Final orders array:', orders.value)
          
          // Debug từng đơn hàng và cập nhật dữ liệu
          for (let index = 0; index < orders.value.length; index++) {
            const order = orders.value[index]
            console.log(`📦 Order ${index + 1}:`, {
              id: order.id,
              maHoaDon: order.maHoaDon,
              ngayDatHang: order.ngayDatHang,
              tongTien: order.tongTien,
              chiTietHoaDonList: order.chiTietHoaDonList,
              hoaDonChiTiets: order.hoaDonChiTiets,
              chiTietHoaDonListLength: order.chiTietHoaDonList ? order.chiTietHoaDonList.length : 0,
              hoaDonChiTietsLength: order.hoaDonChiTiets ? order.hoaDonChiTiets.length : 0,
              allFields: Object.keys(order)
            })
            
            // Gọi API debug để kiểm tra dữ liệu thực tế và cập nhật giao diện
            await updateOrderDataFromAPI(order)
          }
        } else {
          console.log('🛒 No orders data found in response')
          console.log('🛒 Available data:', {
            responseData: response.data,
            ordersDataLength: ordersData.length,
            userInfo: userInfo
          })
        }
      } catch (error) {
        console.error('❌ Error loading orders:', error)
        error.value = 'Không thể tải lịch sử đơn hàng'
      } finally {
        loading.value = false
      }
    }

    // Load addresses from backend
    const loadAddresses = async () => {
      if (!isAuthenticated.value) return
      
      try {
        const token = localStorage.getItem('token')
        addresses.value = await AddressService.loadCustomerAddresses(userInfo.id, token)
        console.log('Loaded addresses:', addresses.value) // Debug log
      } catch (error) {
        console.error('Error loading addresses:', error)
        // Fallback to empty array
        addresses.value = []
      }
    }

    // Load provinces
    const loadProvinces = async () => {
      try {
        loadingProvinces.value = true
        const data = await AddressService.getProvinces()
        provinces.value = data
      } catch (error) {
        console.error('Error loading provinces:', error)
        // Fallback data
        provinces.value = [
          { code: '79', name: 'TP. Hồ Chí Minh' },
          { code: '01', name: 'Hà Nội' },
          { code: '48', name: 'Đà Nẵng' }
        ]
      } finally {
        loadingProvinces.value = false
      }
    }

    // Load districts
    const loadDistricts = async (provinceCode) => {
      if (!provinceCode) {
        districts.value = []
        wards.value = []
        return
      }
      
      try {
        loadingDistricts.value = true
        const data = await AddressService.getDistricts(provinceCode)
        districts.value = data
        wards.value = []
        newAddress.district = ''
        newAddress.ward = ''
      } catch (error) {
        console.error('Error loading districts:', error)
        districts.value = []
      } finally {
        loadingDistricts.value = false
      }
    }

    // Load wards
    const loadWards = async (districtCode) => {
      if (!districtCode) {
        wards.value = []
        return
      }
      
      try {
        loadingWards.value = true
        const data = await AddressService.getWards(districtCode)
        wards.value = data
        newAddress.ward = ''
      } catch (error) {
        console.error('Error loading wards:', error)
        wards.value = []
      } finally {
        loadingWards.value = false
      }
    }

    // Methods
    const toggleEdit = () => {
      isEditing.value = !isEditing.value
    }

    const saveProfile = async () => {
      try {
        loading.value = true
        
        // Convert gender to number format for API
        const genderValue = userInfo.gender ? parseInt(userInfo.gender) : null
        
        // Update customer data via API
        const updateData = {
          hoTen: userInfo.name,
          email: userInfo.email,
          soDienThoai: userInfo.phone,
          ngaySinh: userInfo.birthday,
          gioiTinh: genderValue,
          trangThai: 1 // Keep status as active
        }
        
        console.log('Updating customer profile:', updateData)
        
        // Call API to update customer profile
        const response = await fetch(`http://localhost:8080/api/khach-hang/${userInfo.id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          body: JSON.stringify(updateData)
        })
        
        if (response.ok) {
          const updatedCustomer = await response.json()
          console.log('Profile updated successfully:', updatedCustomer)
          
          // Update auth store with new data
          authStore.updateUser({
            hoTen: updatedCustomer.hoTen,
            email: updatedCustomer.email,
            soDienThoai: updatedCustomer.soDienThoai,
            ngaySinh: updatedCustomer.ngaySinh,
            gioiTinh: updatedCustomer.gioiTinh
          })
          
          isEditing.value = false
          alert('Cập nhật thông tin thành công!')
        } else {
          const errorData = await response.json()
          throw new Error(errorData.message || 'Cập nhật thông tin thất bại')
        }
      } catch (error) {
        console.error('Error updating profile:', error)
        alert('Có lỗi khi cập nhật thông tin: ' + error.message)
      } finally {
        loading.value = false
      }
    }

    const cancelEdit = () => {
      // Reset to original values
      loadUserData()
      isEditing.value = false
    }

    const changePassword = async () => {
      // Validation
      if (!passwordForm.currentPassword || !passwordForm.newPassword || !passwordForm.confirmPassword) {
        alert('Vui lòng điền đầy đủ thông tin')
        return
      }
      
      if (passwordForm.newPassword !== passwordForm.confirmPassword) {
        alert('Mật khẩu mới và xác nhận mật khẩu không khớp')
        return
      }
      
      if (passwordForm.newPassword.length < 6) {
        alert('Mật khẩu mới phải có ít nhất 6 ký tự')
        return
      }
      
      try {
        loading.value = true
        
        // Call API to change password
        const response = await fetch('http://localhost:8080/api/profile/change-password', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          body: JSON.stringify({
            tenDangNhap: authStore.user?.tenDangNhap || authStore.user?.userName,
            currentPassword: passwordForm.currentPassword,
            newPassword: passwordForm.newPassword
          })
        })
        
        const result = await response.json()
        
        if (response.ok && result.success) {
          // Reset form
          passwordForm.currentPassword = ''
          passwordForm.newPassword = ''
          passwordForm.confirmPassword = ''
          
          alert('Đổi mật khẩu thành công!')
        } else {
          throw new Error(result.message || 'Có lỗi khi đổi mật khẩu')
        }
      } catch (error) {
        console.error('Error changing password:', error)
        alert('Lỗi: ' + error.message)
      } finally {
        loading.value = false
      }
    }


    const logout = async () => {
      try {
        await authStore.logout()
        router.push('/')
      } catch (error) {
        console.error('Error logging out:', error)
      }
    }

    const editAddress = (address) => {
      console.log('Editing address:', address)
      editingAddress.value = { ...address }
      
      // Set form data - cần tìm code từ tên
      newAddress.fullName = address.fullName || address.name
      newAddress.phone = address.phone
      newAddress.address = address.address || address.diaChiChiTiet
      newAddress.isDefault = address.isDefault
      
      // Load provinces if not loaded
      if (provinces.value.length === 0) {
        loadProvinces()
      }
      
      // Tìm code từ tên tỉnh/thành phố
      const province = provinces.value.find(p => p.name === address.city || p.name === address.tinhThanh)
      if (province) {
        newAddress.city = province.code
        loadDistricts(province.code)
        
        // Tìm code từ tên quận/huyện
        const district = districts.value.find(d => d.name === address.district || d.name === address.quanHuyen)
        if (district) {
          newAddress.district = district.code
          loadWards(district.code)
          
          // Tìm code từ tên xã/phường
          const ward = wards.value.find(w => w.name === address.ward || w.name === address.xaPhuong)
          if (ward) {
            newAddress.ward = ward.code
          }
        }
      }
      
      showEditAddress.value = true
    }

    const deleteAddress = async (id) => {
      if (!confirm('Bạn có chắc muốn xóa địa chỉ này?')) return
      
      try {
        loading.value = true
        // Call API to delete address
        const response = await fetch(`http://localhost:8080/api/dia-chi/${id}`, {
          method: 'DELETE',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        })
        
        if (response.ok) {
          addresses.value = addresses.value.filter(addr => addr.id !== id)
          alert('Xóa địa chỉ thành công!')
        } else {
          throw new Error('Xóa địa chỉ thất bại')
        }
      } catch (error) {
        console.error('Error deleting address:', error)
        alert('Có lỗi khi xóa địa chỉ')
      } finally {
        loading.value = false
      }
    }

    const updateAddress = async () => {
      try {
        // Get province, district, ward names from codes
        const province = provinces.value.find(p => p.code === newAddress.city)
        const district = districts.value.find(d => d.code === newAddress.district)
        const ward = wards.value.find(w => w.code === newAddress.ward)
        
        const addressData = {
          diaChiChiTiet: newAddress.address,
          tinhThanh: province ? province.name : newAddress.city,
          quanHuyen: district ? district.name : newAddress.district,
          xaPhuong: ward ? ward.name : newAddress.ward,
          macDinh: newAddress.isDefault,
          tenNguoiNhan: newAddress.fullName,
          soDienThoaiNguoiNhan: newAddress.phone
        }
        
        // Call API to update address
        const response = await fetch(`http://localhost:8080/api/dia-chi/${editingAddress.value.id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          body: JSON.stringify(addressData)
        })
        
        if (response.ok) {
          const updatedAddr = await response.json()
          
          // Update local addresses array
          const index = addresses.value.findIndex(addr => addr.id === editingAddress.value.id)
          if (index !== -1) {
            addresses.value[index] = {
              id: updatedAddr.id,
              name: updatedAddr.tenNguoiNhan && updatedAddr.tenNguoiNhan.trim() ? updatedAddr.tenNguoiNhan : userInfo.name,
              phone: updatedAddr.soDienThoaiNguoiNhan && updatedAddr.soDienThoaiNguoiNhan.trim() ? updatedAddr.soDienThoaiNguoiNhan : userInfo.phone,
              fullAddress: `${newAddress.address}, ${ward ? ward.name : newAddress.ward}, ${district ? district.name : newAddress.district}, ${province ? province.name : newAddress.city}`,
              isDefault: newAddress.isDefault,
              diaChiChiTiet: newAddress.address,
              tinhThanh: province ? province.name : newAddress.city,
              quanHuyen: district ? district.name : newAddress.district,
              xaPhuong: ward ? ward.name : newAddress.ward
            }
          }
          
          // Reset form and close modal
          resetAddressForm()
          showEditAddress.value = false
          editingAddress.value = null
          
          alert('Cập nhật địa chỉ thành công!')
        } else {
          throw new Error('Cập nhật địa chỉ thất bại')
        }
      } catch (error) {
        console.error('Error updating address:', error)
        alert('Cập nhật địa chỉ thất bại: ' + error.message)
      }
    }

    // Sử dụng AddressService
    const getFullAddressDisplay = AddressService.getFullAddressDisplay
    const validatePhoneNumber = AddressService.validatePhoneNumber

    const validatePhoneField = () => {
      // Function để validate khi blur khỏi field
      if (newAddress.value.phone && !validatePhoneNumber(newAddress.value.phone)) {
        console.log('Invalid phone number:', newAddress.value.phone)
      }
    }

    const saveAddress = async () => {
      // Validate số điện thoại
      if (!validatePhoneNumber(newAddress.phone)) {
        alert('Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại Việt Nam (10-11 số)')
        return
      }

      try {
        loading.value = true
        
        const token = localStorage.getItem('token')
        const addressData = AddressService.transformAddressForAPI(newAddress, provinces.value, districts.value, wards.value)
        
        const result = await AddressService.saveAddress(addressData, userInfo.id, token, false)
        const address = AddressService.transformAddressFromAPI(result)
        
        if (address.isDefault) {
          addresses.value.forEach(addr => addr.isDefault = false)
        }
        addresses.value.push(address)
        
        // Reset form
        resetAddressForm()
        showAddAddress.value = false
        alert('Thêm địa chỉ thành công!')
      } catch (error) {
        console.error('Error saving address:', error)
        alert('Có lỗi khi thêm địa chỉ')
      } finally {
        loading.value = false
      }
    }

    // Watchers for address selection
    watch(() => newAddress.city, (newCity) => {
      if (newCity) {
        loadDistricts(newCity)
      }
    })

    watch(() => newAddress.district, (newDistrict) => {
      if (newDistrict) {
        loadWards(newDistrict)
      }
    })

    // Watch for route changes to update active tab
    watch(() => route.query.tab, (newTab) => {
      if (newTab) {
        activeTab.value = newTab
      }
    })

    // Load data on component mount
    onMounted(async () => {
      if (isAuthenticated.value) {
        await loadUserData()
        loadOrders()
        loadAddresses()
        loadProvinces()
      } else {
        // Redirect to login if not authenticated
        router.push('/auth/login')
      }
    })

    const resetAddressForm = () => {
      newAddress.fullName = ''
      newAddress.phone = ''
      newAddress.address = ''
      newAddress.city = ''
      newAddress.district = ''
      newAddress.ward = ''
      newAddress.isDefault = false
      districts.value = []
      wards.value = []
    }

    const formatDate = (date) => {
      if (!date) return 'Chưa có ngày'
      try {
        const dateObj = new Date(date)
        if (isNaN(dateObj.getTime())) return 'Ngày không hợp lệ'
        return dateObj.toLocaleDateString('vi-VN')
      } catch (error) {
        console.error('Error formatting date:', date, error)
        return 'Ngày không hợp lệ'
      }
    }

    const formatPrice = (price) => {
      if (!price || isNaN(price) || price === null || price === undefined) {
        return '0 ₫'
      }
      try {
        const numPrice = parseFloat(price)
        if (isNaN(numPrice)) return '0 ₫'
        return new Intl.NumberFormat('vi-VN', {
          style: 'currency',
          currency: 'VND'
        }).format(numPrice)
      } catch (error) {
        console.error('Error formatting price:', price, error)
        return '0 ₫'
      }
    }

    const getStatusText = (status) => {
      const statusMap = {
        'pending': 'Chờ xử lý',
        'processing': 'Đang xử lý',
        'shipped': 'Đã gửi hàng',
        'delivered': 'Đã giao hàng',
        'cancelled': 'Đã hủy',
        // Trạng thái từ backend (tiếng Việt)
        'Tạo hóa đơn': 'Tạo hóa đơn',
        'Chờ xác nhận': 'Chờ xác nhận',
        'Đã thanh toán': 'Đã thanh toán',
        'Chờ giao hàng': 'Chờ giao hàng',
        'Đang vận chuyển': 'Đang vận chuyển',
        'Đã giao hàng': 'Đã giao hàng',
        'Thanh toán': 'Thanh toán',
        'Hoàn thành': 'Hoàn thành',
        'Đã hủy': 'Đã hủy'
      }
      return statusMap[status] || status
    }

    const getStatusClass = (status) => {
      const classMap = {
        'pending': 'bg-yellow-100 text-yellow-800',
        'processing': 'bg-blue-100 text-blue-800',
        'shipped': 'bg-purple-100 text-purple-800',
        'delivered': 'bg-green-100 text-green-800',
        'cancelled': 'bg-red-100 text-red-800',
        // Trạng thái từ backend (tiếng Việt)
        'Tạo hóa đơn': 'bg-gray-100 text-gray-800',
        'Chờ xác nhận': 'bg-yellow-100 text-yellow-800',
        'Đã thanh toán': 'bg-green-100 text-green-800',
        'Chờ giao hàng': 'bg-blue-100 text-blue-800',
        'Đang vận chuyển': 'bg-purple-100 text-purple-800',
        'Đã giao hàng': 'bg-indigo-100 text-indigo-800',
        'Thanh toán': 'bg-orange-100 text-orange-800',
        'Hoàn thành': 'bg-green-100 text-green-800',
        'Đã hủy': 'bg-red-100 text-red-800'
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
      showEditAddress,
      editingAddress,
      loading,
      error,
      userInfo,
      passwordForm,
      newAddress,
      orders,
      addresses,
      reviews,
      tabs,
      isAuthenticated,
      // Address API data
      provinces,
      districts,
      wards,
      loadingProvinces,
      loadingDistricts,
      loadingWards,
      // Methods
      loadUserData,
      loadOrders,
      loadAddresses,
      loadProvinces,
      loadDistricts,
      loadWards,
      toggleEdit,
      saveProfile,
      cancelEdit,
      changePassword,
      logout,
      editAddress,
      updateAddress,
      deleteAddress,
      saveAddress,
      resetAddressForm,
      getFullAddressDisplay,
      validatePhoneNumber,
      validatePhoneField,
      formatDate,
      formatPrice,
      getStatusText,
      getStatusClass,
      viewOrderDetails,
      updateOrderDataFromAPI,
      UserIcon,
      ShoppingBagIcon,
      LocationMarkerIcon,
      ShieldCheckIcon,
      StarIcon
    }
  }
}
</script>
