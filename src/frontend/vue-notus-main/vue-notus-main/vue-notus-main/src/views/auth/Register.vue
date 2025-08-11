<template>
  <div class="container mx-auto px-4 h-full">
    <div class="flex content-center items-center justify-center h-full">
      <div class="w-full lg:w-6/12 px-4">
        <div class="relative flex flex-col min-w-0 break-words w-full mb-6 shadow-lg rounded-lg bg-blueGray-200 border-0 overflow-hidden">
          <div class="rounded-t mb-0 px-6 py-6">
            <div class="text-center mb-3">
              <h6 class="text-blueGray-500 text-sm font-bold">
                Đăng ký tài khoản BBook
              </h6>
            </div>
            
            <!-- Error Alert -->
            <div v-if="error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
              <span class="block sm:inline">{{ error }}</span>
            </div>

            <!-- Success Alert -->
            <div v-if="successMessage" class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">
              <span class="block sm:inline">{{ successMessage }}</span>
            </div>
          </div>
          
          <div class="flex-auto px-4 lg:px-10 py-10 pt-0">
            <form @submit.prevent="handleRegister">
              <div class="relative w-full mb-3">
                <label class="block uppercase text-blueGray-600 text-xs font-bold mb-2" for="hoTen">
                  Họ và tên
                </label>
                <input
                  id="hoTen"
                  v-model="registerForm.hoTen"
                  type="text"
                  class="border-0 px-3 py-3 placeholder-blueGray-300 text-blueGray-600 bg-white rounded text-sm shadow focus:outline-none focus:ring w-full ease-linear transition-all duration-150"
                  placeholder="Nhập họ và tên đầy đủ"
                  required
                  :disabled="loading"
                />
              </div>

              <div class="relative w-full mb-3">
                <label class="block uppercase text-blueGray-600 text-xs font-bold mb-2" for="tenDangNhap">
                  Tên đăng nhập
                </label>
                <input
                  id="tenDangNhap"
                  v-model="registerForm.tenDangNhap"
                  type="text"
                  class="border-0 px-3 py-3 placeholder-blueGray-300 text-blueGray-600 bg-white rounded text-sm shadow focus:outline-none focus:ring w-full ease-linear transition-all duration-150"
                  placeholder="Nhập tên đăng nhập"
                  required
                  :disabled="loading"
                />
              </div>

              <div class="relative w-full mb-3">
                <label class="block uppercase text-blueGray-600 text-xs font-bold mb-2" for="email">
                  Email
                </label>
                <input
                  id="email"
                  v-model="registerForm.email"
                  type="email"
                  class="border-0 px-3 py-3 placeholder-blueGray-300 text-blueGray-600 bg-white rounded text-sm shadow focus:outline-none focus:ring w-full ease-linear transition-all duration-150"
                  placeholder="Nhập địa chỉ email"
                  required
                  :disabled="loading"
                />
              </div>

              <div class="relative w-full mb-3">
                <label class="block uppercase text-blueGray-600 text-xs font-bold mb-2" for="soDienThoai">
                  Số điện thoại
                </label>
                <input
                  id="soDienThoai"
                  v-model="registerForm.soDienThoai"
                  type="tel"
                  class="border-0 px-3 py-3 placeholder-blueGray-300 text-blueGray-600 bg-white rounded text-sm shadow focus:outline-none focus:ring w-full ease-linear transition-all duration-150"
                  placeholder="Nhập số điện thoại"
                  :disabled="loading"
                />
              </div>

              <div class="relative w-full mb-3">
                <label class="block uppercase text-blueGray-600 text-xs font-bold mb-2" for="matKhau">
                  Mật khẩu
                </label>
                <input
                  id="matKhau"
                  v-model="registerForm.matKhau"
                  type="password"
                  class="border-0 px-3 py-3 placeholder-blueGray-300 text-blueGray-600 bg-white rounded text-sm shadow focus:outline-none focus:ring w-full ease-linear transition-all duration-150"
                  placeholder="Nhập mật khẩu (tối thiểu 6 ký tự)"
                  required
                  :disabled="loading"
                />
              </div>

              <div class="relative w-full mb-3">
                <label class="block uppercase text-blueGray-600 text-xs font-bold mb-2" for="xacNhanMatKhau">
                  Xác nhận mật khẩu
                </label>
                <input
                  id="xacNhanMatKhau"
                  v-model="registerForm.xacNhanMatKhau"
                  type="password"
                  class="border-0 px-3 py-3 placeholder-blueGray-300 text-blueGray-600 bg-white rounded text-sm shadow focus:outline-none focus:ring w-full ease-linear transition-all duration-150"
                  placeholder="Nhập lại mật khẩu"
                  required
                  :disabled="loading"
                />
              </div>

              <div class="relative w-full mb-3">
                <label class="block uppercase text-blueGray-600 text-xs font-bold mb-2" for="loaiNguoiDung">
                  Loại tài khoản
                </label>
                <select
                  id="loaiNguoiDung"
                  v-model="registerForm.loaiNguoiDung"
                  class="border-0 px-3 py-3 placeholder-blueGray-300 text-blueGray-600 bg-white rounded text-sm shadow focus:outline-none focus:ring w-full ease-linear transition-all duration-150"
                  required
                  :disabled="true"
                >
                  <option value="KHACH_HANG">Khách hàng</option>
                </select>
                <small class="text-blueGray-400">
                  * Tài khoản nhân viên sẽ được tạo bởi admin
                </small>
              </div>

              <div>
                <label class="inline-flex items-center cursor-pointer">
                  <input
                    id="agreeTerms"
                    v-model="registerForm.agreeTerms"
                    type="checkbox"
                    class="form-checkbox border-0 rounded text-blueGray-700 ml-1 w-5 h-5 ease-linear transition-all duration-150"
                    required
                    :disabled="loading"
                  />
                  <span class="ml-2 text-sm font-semibold text-blueGray-600">
                    Tôi đồng ý với 
                    <a href="#" class="text-lightBlue-500">
                      điều khoản sử dụng
                    </a>
                  </span>
                </label>
              </div>

              <div class="text-center mt-6">
                <button
                  type="submit"
                  class="bg-gradient-to-r from-green-600 to-green-700 hover:from-green-700 hover:to-green-800 text-white text-sm font-bold uppercase px-6 py-3 rounded-lg shadow-lg hover:shadow-xl outline-none focus:outline-none mr-1 mb-1 w-full ease-linear transition-all duration-150 transform hover:-translate-y-0.5"
                  :disabled="loading || !isFormValid"
                  :class="{ 'opacity-50 cursor-not-allowed transform-none': loading || !isFormValid }"
                >
                  <span v-if="loading" class="inline-flex items-center">
                    <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                      <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                      <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                    </svg>
                    Đang đăng ký...
                  </span>
                  <span v-else>Đăng ký</span>
                </button>
              </div>
            </form>
          </div>
        </div>
        
        <div class="flex flex-wrap mt-6 relative">
          <div class="w-1/2">
            <router-link to="/auth/login" class="text-blueGray-200">
              <small>Đã có tài khoản? Đăng nhập</small>
            </router-link>
          </div>
          <div class="w-1/2 text-right">
            <router-link to="/shop" class="text-blueGray-200">
              <small>Quay về trang chủ</small>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useAuthStore } from '@/store/auth'

export default {
  name: 'Register',
  setup() {
    const authStore = useAuthStore()
    return { authStore }
  },
  data() {
    return {
      registerForm: {
        hoTen: '',
        tenDangNhap: '',
        email: '',
        soDienThoai: '',
        matKhau: '',
        xacNhanMatKhau: '',
        loaiNguoiDung: 'KHACH_HANG',
        agreeTerms: false
      },
      loading: false,
      error: null,
      successMessage: null
    }
  },
  computed: {
    isFormValid() {
      return (
        this.registerForm.hoTen.trim() &&
        this.registerForm.tenDangNhap.trim() &&
        this.registerForm.email.trim() &&
        this.registerForm.matKhau.length >= 6 &&
        this.registerForm.matKhau === this.registerForm.xacNhanMatKhau &&
        this.registerForm.agreeTerms
      )
    },
    passwordMatch() {
      return this.registerForm.matKhau === this.registerForm.xacNhanMatKhau
    }
  },
  mounted() {
    // Nếu đã đăng nhập thì redirect về trang chính
    if (this.authStore.isAuthenticated) {
      this.$router.push(this.authStore.defaultRoute)
    }
  },
  methods: {
    async handleRegister() {
      if (this.loading || !this.isFormValid) return
      
      this.loading = true
      this.error = null
      this.successMessage = null
      
      try {
        // Validate form
        if (!this.passwordMatch) {
          throw new Error('Mật khẩu xác nhận không khớp')
        }
        
        if (this.registerForm.matKhau.length < 6) {
          throw new Error('Mật khẩu phải có ít nhất 6 ký tự')
        }
        
        // Validate email format
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
        if (!emailRegex.test(this.registerForm.email)) {
          throw new Error('Email không hợp lệ')
        }
        
        // Validate phone number (optional)
        if (this.registerForm.soDienThoai && !/^[0-9+\-\s()]+$/.test(this.registerForm.soDienThoai)) {
          throw new Error('Số điện thoại không hợp lệ')
        }
        
        // Call register API
        const userData = {
          hoTen: this.registerForm.hoTen.trim(),
          tenDangNhap: this.registerForm.tenDangNhap.trim(),
          email: this.registerForm.email.trim(),
          soDienThoai: this.registerForm.soDienThoai.trim() || null,
          matKhau: this.registerForm.matKhau,
          loaiNguoiDung: this.registerForm.loaiNguoiDung
        }
        
        const result = await this.authStore.register(userData)
        
        if (result.success) {
          this.successMessage = 'Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.'
          
          // Reset form
          this.resetForm()
          
          // Delay và redirect về login
          setTimeout(() => {
            this.$router.push('/auth/login')
          }, 2000)
        }
        
      } catch (error) {
        console.error('Register error:', error)
        this.error = error.message || 'Đăng ký thất bại'
      } finally {
        this.loading = false
      }
    },
    
    resetForm() {
      this.registerForm = {
        hoTen: '',
        tenDangNhap: '',
        email: '',
        soDienThoai: '',
        matKhau: '',
        xacNhanMatKhau: '',
        loaiNguoiDung: 'KHACH_HANG',
        agreeTerms: false
      }
    },
    
    clearError() {
      this.error = null
    }
  },
  
  watch: {
    'registerForm.tenDangNhap'() {
      this.clearError()
    },
    'registerForm.email'() {
      this.clearError()
    },
    'registerForm.matKhau'() {
      this.clearError()
    },
    'registerForm.xacNhanMatKhau'() {
      this.clearError()
    }
  }
}
</script>