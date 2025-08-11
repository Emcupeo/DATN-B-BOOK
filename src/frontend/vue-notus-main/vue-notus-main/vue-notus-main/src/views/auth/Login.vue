<template>
  <div class="container mx-auto px-4 h-full">
    <div class="flex content-center items-center justify-center h-full">
      <div class="w-full lg:w-4/12 px-4">
        <div
          class="relative flex flex-col min-w-0 break-words w-full mb-6 shadow-lg rounded-lg bg-blueGray-200 border-0 overflow-hidden"
        >
          <div class="rounded-t mb-0 px-6 py-6">
            <div class="text-center mb-3">
              <h6 class="text-blueGray-500 text-sm font-bold">
                Đăng nhập BBook
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
            <form @submit.prevent="handleLogin">
              <div class="relative w-full mb-3">
                <label
                  class="block uppercase text-blueGray-600 text-xs font-bold mb-2"
                  for="tenDangNhap"
                >
                  Tên đăng nhập
                </label>
                <input
                  id="tenDangNhap"
                  v-model="loginForm.tenDangNhap"
                  type="text"
                  class="border-0 px-3 py-3 placeholder-blueGray-300 text-blueGray-600 bg-white rounded text-sm shadow focus:outline-none focus:ring w-full ease-linear transition-all duration-150"
                  placeholder="Nhập tên đăng nhập"
                  required
                  :disabled="loading"
                />
              </div>

              <div class="relative w-full mb-3">
                <label
                  class="block uppercase text-blueGray-600 text-xs font-bold mb-2"
                  for="matKhau"
                >
                  Mật khẩu
                </label>
                <input
                  id="matKhau"
                  v-model="loginForm.matKhau"
                  type="password"
                  class="border-0 px-3 py-3 placeholder-blueGray-300 text-blueGray-600 bg-white rounded text-sm shadow focus:outline-none focus:ring w-full ease-linear transition-all duration-150"
                  placeholder="Nhập mật khẩu"
                  required
                  :disabled="loading"
                />
              </div>
              
              <div>
                <label class="inline-flex items-center cursor-pointer">
                  <input
                    id="rememberMe"
                    v-model="loginForm.rememberMe"
                    type="checkbox"
                    class="form-checkbox border-0 rounded text-blueGray-700 ml-1 w-5 h-5 ease-linear transition-all duration-150"
                    :disabled="loading"
                  />
                  <span class="ml-2 text-sm font-semibold text-blueGray-600">
                    Ghi nhớ đăng nhập
                  </span>
                </label>
              </div>

              <div class="text-center mt-6">
                <button
                  type="submit"
                  class="bg-gradient-to-r from-blue-600 to-blue-700 hover:from-blue-700 hover:to-blue-800 text-white text-sm font-bold uppercase px-6 py-3 rounded-lg shadow-lg hover:shadow-xl outline-none focus:outline-none mr-1 mb-1 w-full ease-linear transition-all duration-150 transform hover:-translate-y-0.5"
                  :disabled="loading"
                  :class="{ 'opacity-50 cursor-not-allowed transform-none': loading }"
                >
                  <span v-if="loading" class="inline-flex items-center">
                    <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                      <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                      <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                    </svg>
                    Đang đăng nhập...
                  </span>
                  <span v-else>Đăng nhập</span>
                </button>
              </div>
            </form>
          </div>
        </div>
        
        <div class="flex flex-wrap mt-6 relative">
          <div class="w-1/2">
            <a href="javascript:void(0)" class="text-blueGray-200">
              <small>Quên mật khẩu?</small>
            </a>
          </div>
          <div class="w-1/2 text-right">
            <router-link to="/auth/register" class="text-blueGray-200">
              <small>Tạo tài khoản mới</small>
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
  name: 'Login',
  setup() {
    const authStore = useAuthStore()
    return { authStore }
  },
  data() {
    return {
      loginForm: {
        tenDangNhap: '',
        matKhau: '',
        rememberMe: false
      },
      loading: false,
      error: null,
      successMessage: null
    }
  },
  computed: {
    isAuthenticated() {
      return this.authStore.isAuthenticated
    }
  },
  mounted() {
    // Nếu đã đăng nhập thì redirect về trang chính
    if (this.isAuthenticated) {
      this.$router.push(this.authStore.defaultRoute)
    }
    
    // Clear error khi vào trang
    this.authStore.clearError()
  },
  methods: {
    async handleLogin() {
      if (this.loading) return
      
      this.loading = true
      this.error = null
      this.successMessage = null
      
      try {
        // Validate input
        if (!this.loginForm.tenDangNhap.trim()) {
          throw new Error('Vui lòng nhập tên đăng nhập')
        }
        
        if (!this.loginForm.matKhau.trim()) {
          throw new Error('Vui lòng nhập mật khẩu')
        }
        
        // Call login API
        const result = await this.authStore.login(
          this.loginForm.tenDangNhap.trim(),
          this.loginForm.matKhau
        )
        
        if (result.success) {
          this.successMessage = 'Đăng nhập thành công!'
          
          // Đảm bảo auth state được cập nhật xong
          await this.$nextTick()
          
          // Delay để hiển thị thông báo success
          setTimeout(async () => {
            // Redirect theo role dựa trên user vừa đăng nhập
            const user = result.user || this.authStore.user
            let defaultRoute = '/shop'
            
            if (user && user.loaiNguoiDung) {
              switch (user.loaiNguoiDung) {
                case 'ADMIN':
                  defaultRoute = '/admin/dashboard'
                  break
                case 'NHAN_VIEN':
                  defaultRoute = '/admin/ban-hang-tai-quay'
                  break
                case 'KHACH_HANG':
                  defaultRoute = '/shop'
                  break
                default:
                  defaultRoute = '/shop'
              }
            }
            
            console.log('Login result user:', result.user)
            console.log('Auth store user:', this.authStore.user)
            console.log('User role:', user?.loaiNguoiDung)
            console.log('Computed defaultRoute:', this.authStore.defaultRoute)
            console.log('Manual defaultRoute:', defaultRoute)
            console.log('Redirecting to:', defaultRoute)
            
            await this.$router.push(defaultRoute)
          }, 1500)
        }
        
      } catch (error) {
        console.error('Login error:', error)
        this.error = error.message || 'Đăng nhập thất bại'
      } finally {
        this.loading = false
      }
    },
    
    clearError() {
      this.error = null
    }
  },
  
  watch: {
    'loginForm.tenDangNhap'() {
      this.clearError()
    },
    'loginForm.matKhau'() {
      this.clearError()
    }
  }
}
</script>
