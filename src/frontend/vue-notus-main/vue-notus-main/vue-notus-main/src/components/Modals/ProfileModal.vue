<template>
  <div v-if="isOpen" class="fixed inset-0 z-50 overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
    <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
      <!-- Background overlay -->
      <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true" @click="closeModal"></div>

      <!-- This element is to trick the browser into centering the modal contents -->
      <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>

      <!-- Modal panel -->
      <div class="inline-block align-bottom bg-white rounded-lg px-4 pt-5 pb-4 text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full sm:p-6">
        <div>
          <!-- Header -->
          <div class="flex items-center justify-between mb-6">
            <div class="flex items-center">
              <div class="mx-auto flex-shrink-0 flex items-center justify-center h-12 w-12 rounded-full bg-blue-100 sm:mx-0 sm:h-10 sm:w-10">
                <i class="fas fa-user text-blue-600"></i>
              </div>
              <h3 class="ml-3 text-lg leading-6 font-medium text-gray-900" id="modal-title">
                Thông tin cá nhân
              </h3>
            </div>
            <button @click="closeModal" class="text-gray-400 hover:text-gray-500">
              <span class="sr-only">Đóng</span>
              <i class="fas fa-times text-lg"></i>
            </button>
          </div>

          <!-- Loading state -->
          <div v-if="loading" class="flex justify-center items-center py-8">
            <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            <span class="ml-2 text-gray-600">Đang tải thông tin...</span>
          </div>

          <!-- Error state -->
          <div v-else-if="error" class="bg-red-50 border border-red-200 rounded-md p-4 mb-4">
            <div class="flex">
              <i class="fas fa-exclamation-circle text-red-400"></i>
              <div class="ml-3">
                <h3 class="text-sm font-medium text-red-800">Lỗi</h3>
                <p class="mt-1 text-sm text-red-700">{{ error }}</p>
              </div>
            </div>
          </div>

          <!-- Profile content -->
          <div v-else-if="profileData" class="space-y-4">
            <!-- Avatar -->
            <div class="flex justify-center mb-6">
              <div class="relative">
                <div class="w-20 h-20 bg-gradient-to-r from-blue-500 to-purple-500 rounded-full flex items-center justify-center text-white text-2xl font-bold">
                  {{ String(profileData.hoTen || 'U').charAt(0).toUpperCase() }}
                </div>
              </div>
            </div>

            <!-- Profile fields -->
            <div class="grid grid-cols-1 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Họ và tên</label>
                <div class="px-3 py-2 border border-gray-300 rounded-md bg-gray-50 text-gray-900">
                  {{ profileData.hoTen || 'Chưa cập nhật' }}
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Tên đăng nhập</label>
                <div class="px-3 py-2 border border-gray-300 rounded-md bg-gray-50 text-gray-900">
                  {{ profileData.tenDangNhap || 'Chưa cập nhật' }}
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
                <div class="px-3 py-2 border border-gray-300 rounded-md bg-gray-50 text-gray-900">
                  {{ profileData.email || 'Chưa cập nhật' }}
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Số điện thoại</label>
                <div class="px-3 py-2 border border-gray-300 rounded-md bg-gray-50 text-gray-900">
                  {{ profileData.soDienThoai || 'Chưa cập nhật' }}
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Loại tài khoản</label>
                <div class="px-3 py-2 border border-gray-300 rounded-md bg-gray-50 text-gray-900">
                  {{ roleDisplayName }}
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
                <div class="px-3 py-2 border border-gray-300 rounded-md bg-gray-50">
                  <span v-if="profileData.trangThai" class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                    <span class="w-1.5 h-1.5 mr-1.5 bg-green-400 rounded-full"></span>
                    Hoạt động
                  </span>
                  <span v-else class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800">
                    <span class="w-1.5 h-1.5 mr-1.5 bg-red-400 rounded-full"></span>
                    Bị khóa
                  </span>
                </div>
              </div>

              <div v-if="profileData.createdAt">
                <label class="block text-sm font-medium text-gray-700 mb-1">Ngày tạo</label>
                <div class="px-3 py-2 border border-gray-300 rounded-md bg-gray-50 text-gray-900">
                  {{ formatDate(profileData.createdAt) }}
                </div>
              </div>
            </div>
          </div>

          <!-- Footer buttons -->
          <div class="mt-6 flex justify-end space-x-3">
            <button
              @click="closeModal"
              type="button"
              class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              Đóng
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useAuthStore } from '@/store/auth'

export default {
  name: 'ProfileModal',
  props: {
    isOpen: {
      type: Boolean,
      default: false
    }
  },
  emits: ['close'],
  setup() {
    const authStore = useAuthStore()
    return { authStore }
  },
  data() {
    return {
      loading: false,
      error: null,
      profileData: null
    }
  },
  computed: {
    roleDisplayName() {
      const roleMap = {
        'ADMIN': 'Quản trị viên',
        'NHAN_VIEN': 'Nhân viên', 
        'KHACH_HANG': 'Khách hàng'
      }
      return roleMap[this.profileData?.loaiNguoiDung] || 'Người dùng'
    }
  },
  watch: {
    isOpen(newVal) {
      if (newVal) {
        this.loadProfile()
      }
    }
  },
  methods: {
    async loadProfile() {
      this.loading = true
      this.error = null
      
      try {
        // Simulate API call - replace with actual API endpoint
        await new Promise(resolve => setTimeout(resolve, 500))
        
        // Use current user data from auth store
        const currentUser = this.authStore.user || {}
        this.profileData = {
          hoTen: this.authStore.userName || currentUser.hoTen || 'Admin',
          tenDangNhap: currentUser.tenDangNhap || 'admin',
          email: currentUser.email || 'admin@bbook.com',
          soDienThoai: currentUser.soDienThoai || 'Chưa cập nhật',
          loaiNguoiDung: this.authStore.userRole || currentUser.loaiNguoiDung || 'ADMIN',
          trangThai: currentUser.trangThai !== undefined ? currentUser.trangThai : true,
          createdAt: currentUser.createdAt || new Date().toISOString()
        }
      } catch (error) {
        console.error('Error loading profile:', error)
        this.error = 'Không thể tải thông tin cá nhân'
      } finally {
        this.loading = false
      }
    },
    
    closeModal() {
      this.$emit('close')
    },
    
    formatDate(dateString) {
      if (!dateString) return 'Chưa cập nhật'
      try {
        return new Date(dateString).toLocaleDateString('vi-VN', {
          year: 'numeric',
          month: 'long',
          day: 'numeric',
          hour: '2-digit',
          minute: '2-digit'
        })
      } catch (error) {
        return 'Không xác định'
      }
    }
  }
}
</script>

<style scoped>
/* Additional styles if needed */
</style>