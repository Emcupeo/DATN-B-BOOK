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
              <div class="mx-auto flex-shrink-0 flex items-center justify-center h-12 w-12 rounded-full bg-green-100 sm:mx-0 sm:h-10 sm:w-10">
                <i class="fas fa-lock text-green-600"></i>
              </div>
              <h3 class="ml-3 text-lg leading-6 font-medium text-gray-900" id="modal-title">
                Đổi mật khẩu
              </h3>
            </div>
            <button @click="closeModal" class="text-gray-400 hover:text-gray-500">
              <span class="sr-only">Đóng</span>
              <i class="fas fa-times text-lg"></i>
            </button>
          </div>

          <!-- Error Alert -->
          <div v-if="error" class="bg-red-50 border border-red-200 rounded-md p-4 mb-4">
            <div class="flex">
              <i class="fas fa-exclamation-circle text-red-400"></i>
              <div class="ml-3">
                <h3 class="text-sm font-medium text-red-800">Lỗi</h3>
                <p class="mt-1 text-sm text-red-700">{{ error }}</p>
              </div>
            </div>
          </div>

          <!-- Success Alert -->
          <div v-if="successMessage" class="bg-green-50 border border-green-200 rounded-md p-4 mb-4">
            <div class="flex">
              <i class="fas fa-check-circle text-green-400"></i>
              <div class="ml-3">
                <h3 class="text-sm font-medium text-green-800">Thành công</h3>
                <p class="mt-1 text-sm text-green-700">{{ successMessage }}</p>
              </div>
            </div>
          </div>

          <!-- Change password form -->
          <form @submit.prevent="handleChangePassword" v-if="!successMessage">
            <div class="space-y-4">
              <!-- Current password -->
              <div>
                <label for="currentPassword" class="block text-sm font-medium text-gray-700 mb-1">
                  Mật khẩu hiện tại <span class="text-red-500">*</span>
                </label>
                <div class="relative">
                  <input
                    id="currentPassword"
                    v-model="form.currentPassword"
                    :type="showCurrentPassword ? 'text' : 'password'"
                    class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    placeholder="Nhập mật khẩu hiện tại"
                    required
                    :disabled="loading"
                  />
                  <button
                    type="button"
                    @click="showCurrentPassword = !showCurrentPassword"
                    class="absolute inset-y-0 right-0 pr-3 flex items-center"
                    :disabled="loading"
                  >
                    <i :class="showCurrentPassword ? 'fas fa-eye-slash' : 'fas fa-eye'" class="h-4 w-4 text-gray-400"></i>
                  </button>
                </div>
              </div>

              <!-- New password -->
              <div>
                <label for="newPassword" class="block text-sm font-medium text-gray-700 mb-1">
                  Mật khẩu mới <span class="text-red-500">*</span>
                </label>
                <div class="relative">
                  <input
                    id="newPassword"
                    v-model="form.newPassword"
                    :type="showNewPassword ? 'text' : 'password'"
                    class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    :class="{ 'border-red-300': passwordErrors.length > 0 }"
                    placeholder="Nhập mật khẩu mới (tối thiểu 6 ký tự)"
                    required
                    :disabled="loading"
                    @input="validatePassword"
                  />
                  <button
                    type="button"
                    @click="showNewPassword = !showNewPassword"
                    class="absolute inset-y-0 right-0 pr-3 flex items-center"
                    :disabled="loading"
                  >
                    <i :class="showNewPassword ? 'fas fa-eye-slash' : 'fas fa-eye'" class="h-4 w-4 text-gray-400"></i>
                  </button>
                </div>
                <!-- Password strength indicator -->
                <div v-if="form.newPassword" class="mt-2">
                  <div class="flex items-center space-x-2">
                    <div class="flex-1 bg-gray-200 rounded-full h-2">
                      <div 
                        class="h-2 rounded-full transition-all duration-300"
                        :class="passwordStrengthColor"
                        :style="{ width: passwordStrengthPercent + '%' }"
                      ></div>
                    </div>
                    <span class="text-sm font-medium" :class="passwordStrengthTextColor">
                      {{ passwordStrengthText }}
                    </span>
                  </div>
                  <!-- Password requirements -->
                  <ul v-if="passwordErrors.length > 0" class="mt-2 text-sm text-red-600 space-y-1">
                    <li v-for="error in passwordErrors" :key="error" class="flex items-center">
                      <i class="fas fa-times text-red-500 mr-2"></i>
                      {{ error }}
                    </li>
                  </ul>
                </div>
              </div>

              <!-- Confirm new password -->
              <div>
                <label for="confirmPassword" class="block text-sm font-medium text-gray-700 mb-1">
                  Xác nhận mật khẩu mới <span class="text-red-500">*</span>
                </label>
                <div class="relative">
                  <input
                    id="confirmPassword"
                    v-model="form.confirmPassword"
                    :type="showConfirmPassword ? 'text' : 'password'"
                    class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    :class="{ 'border-red-300': !passwordsMatch && form.confirmPassword }"
                    placeholder="Nhập lại mật khẩu mới"
                    required
                    :disabled="loading"
                  />
                  <button
                    type="button"
                    @click="showConfirmPassword = !showConfirmPassword"
                    class="absolute inset-y-0 right-0 pr-3 flex items-center"
                    :disabled="loading"
                  >
                    <i :class="showConfirmPassword ? 'fas fa-eye-slash' : 'fas fa-eye'" class="h-4 w-4 text-gray-400"></i>
                  </button>
                </div>
                <p v-if="!passwordsMatch && form.confirmPassword" class="mt-1 text-sm text-red-600">
                  Mật khẩu xác nhận không khớp
                </p>
              </div>
            </div>

            <!-- Footer buttons -->
            <div class="mt-6 flex justify-end space-x-3">
              <button
                @click="closeModal"
                type="button"
                class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
                :disabled="loading"
              >
                Hủy
              </button>
              <button
                type="submit"
                class="bg-blue-600 py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
                :disabled="loading || !isFormValid"
              >
                <span v-if="loading" class="inline-flex items-center">
                  <svg class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                  Đang xử lý...
                </span>
                <span v-else>Đổi mật khẩu</span>
              </button>
            </div>
          </form>

          <!-- Success state -->
          <div v-else class="mt-6 flex justify-end">
            <button
              @click="closeModal"
              type="button"
              class="bg-blue-600 py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
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
import AuthService from '@/service/AuthService'

export default {
  name: 'ChangePasswordModal',
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
      successMessage: null,
      form: {
        currentPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      showCurrentPassword: false,
      showNewPassword: false,
      showConfirmPassword: false,
      passwordErrors: []
    }
  },
  computed: {
    passwordsMatch() {
      return this.form.newPassword === this.form.confirmPassword
    },
    
    isFormValid() {
      return this.form.currentPassword.trim() && 
             this.form.newPassword.length >= 6 && 
             this.passwordsMatch && 
             this.passwordErrors.length === 0
    },
    
    passwordStrength() {
      const password = this.form.newPassword
      let score = 0
      
      if (password.length >= 6) score += 1
      if (password.length >= 8) score += 1
      if (/[a-z]/.test(password)) score += 1
      if (/[A-Z]/.test(password)) score += 1
      if (/[0-9]/.test(password)) score += 1
      if (/[^A-Za-z0-9]/.test(password)) score += 1
      
      return score
    },
    
    passwordStrengthPercent() {
      return (this.passwordStrength / 6) * 100
    },
    
    passwordStrengthColor() {
      if (this.passwordStrength <= 2) return 'bg-red-500'
      if (this.passwordStrength <= 4) return 'bg-yellow-500'
      return 'bg-green-500'
    },
    
    passwordStrengthTextColor() {
      if (this.passwordStrength <= 2) return 'text-red-600'
      if (this.passwordStrength <= 4) return 'text-yellow-600'
      return 'text-green-600'
    },
    
    passwordStrengthText() {
      if (this.passwordStrength <= 2) return 'Yếu'
      if (this.passwordStrength <= 4) return 'Trung bình'
      return 'Mạnh'
    }
  },
  watch: {
    isOpen(newVal) {
      if (newVal) {
        this.resetForm()
      }
    }
  },
  methods: {
    validatePassword() {
      const password = this.form.newPassword
      this.passwordErrors = []
      
      if (password.length < 6) {
        this.passwordErrors.push('Mật khẩu phải có ít nhất 6 ký tự')
      }
      
      if (password === this.form.currentPassword) {
        this.passwordErrors.push('Mật khẩu mới phải khác mật khẩu hiện tại')
      }
    },
    
    async handleChangePassword() {
      if (this.loading || !this.isFormValid) return
      
      this.loading = true
      this.error = null
      this.successMessage = null
      
      try {
        // Make actual API call to change password
        const response = await AuthService.changePassword(
          this.form.currentPassword,
          this.form.newPassword
        )
        
        if (response.success) {
          this.successMessage = 'Mật khẩu đã được thay đổi thành công!'
          
          // Reset form after success and close modal
          setTimeout(() => {
            this.resetForm()
            this.closeModal()
          }, 2000)
        } else {
          throw new Error(response.message || 'Đổi mật khẩu thất bại')
        }
        
      } catch (error) {
        console.error('Change password error:', error)
        this.error = error.message || 'Đổi mật khẩu thất bại. Vui lòng thử lại.'
      } finally {
        this.loading = false
      }
    },
    
    resetForm() {
      this.form = {
        currentPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
      this.showCurrentPassword = false
      this.showNewPassword = false
      this.showConfirmPassword = false
      this.passwordErrors = []
      this.error = null
      this.successMessage = null
    },
    
    closeModal() {
      this.resetForm()
      this.$emit('close')
    }
  }
}
</script>

<style scoped>
/* Additional styles if needed */
</style>