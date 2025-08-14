import { reactive, computed } from 'vue'
import AuthService from '@/service/AuthService'

// Global reactive state
const state = reactive({
  user: null,
  token: null,
  isAuthenticated: false,
  loading: false,
  error: null
})

// Auth store functions
export const useAuthStore = () => {
  // Computed properties
  const isAdmin = computed(() => state.user?.loaiNguoiDung === 'ADMIN')
  const isNhanVien = computed(() => state.user?.loaiNguoiDung === 'NHAN_VIEN')
  const isKhachHang = computed(() => state.user?.loaiNguoiDung === 'KHACH_HANG')
  const userName = computed(() => String(state.user?.hoTen || state.user?.tenDangNhap || ''))
  const userRole = computed(() => String(state.user?.loaiNguoiDung || ''))
  const defaultRoute = computed(() => {
    if (!state.user) return '/shop'
    
    switch (state.user.loaiNguoiDung) {
      case 'ADMIN':
        return '/admin/dashboard'
      case 'NHAN_VIEN':
        return '/admin/ban-hang-tai-quay'
      case 'KHACH_HANG':
        return '/shop'
      default:
        return '/shop'
    }
  })

  // Actions
  const initializeAuth = () => {
    const token = localStorage.getItem('token')
    const user = localStorage.getItem('user')
    
    if (token && user) {
      try {
        state.token = token
        state.user = JSON.parse(user)
        state.isAuthenticated = true
      } catch (error) {
        console.error('Error parsing user data:', error)
        clearAuth()
      }
    }
  }

  const login = async (tenDangNhap, matKhau) => {
    state.loading = true
    state.error = null
    
    try {
      const response = await AuthService.login(tenDangNhap, matKhau)
      
      if (response.success) {
        state.user = response.userInfo
        state.token = response.token
        state.isAuthenticated = true
        
        localStorage.setItem('token', response.token)
        localStorage.setItem('user', JSON.stringify(response.userInfo))
        
        return { success: true, user: response.userInfo }
      } else {
        throw new Error(response.message)
      }
    } catch (error) {
      state.error = error.message
      clearAuth()
      throw error
    } finally {
      state.loading = false
    }
  }

  const register = async (userData) => {
    state.loading = true
    state.error = null
    
    try {
      const response = await AuthService.register(userData)
      return { success: true, data: response }
    } catch (error) {
      state.error = error.message
      throw error
    } finally {
      state.loading = false
    }
  }

  const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    clearAuth()
  }

  const clearAuth = () => {
    state.user = null
    state.token = null
    state.isAuthenticated = false
    state.error = null
  }

  const canAccessRoute = (route) => {
    if (!state.isAuthenticated) return false
    
    const role = state.user?.loaiNguoiDung
    
    // Admin và nhân viên có thể truy cập admin routes
    if (route.startsWith('/admin')) {
      return role === 'ADMIN' || role === 'NHAN_VIEN'
    }
    
    // Tất cả user có thể truy cập shop routes
    if (route.startsWith('/shop')) {
      return true
    }
    
    // Public routes
    const publicRoutes = ['/auth/login', '/auth/register', '/']
    return publicRoutes.includes(route) || publicRoutes.some(r => route.startsWith(r))
  }

  const clearError = () => {
    state.error = null
  }

  return {
    // State
    get isAuthenticated() { return state.isAuthenticated },
    get loading() { return state.loading },
    get error() { return state.error },
    get user() { return state.user },
    get token() { return state.token },
    
    // Computed - unwrap values
    get isAdmin() { return isAdmin.value },
    get isNhanVien() { return isNhanVien.value },
    get isKhachHang() { return isKhachHang.value },
    get userName() { return userName.value },
    get userRole() { return userRole.value },
    get defaultRoute() { return defaultRoute.value },
    
    // Actions
    initializeAuth,
    login,
    register,
    logout,
    clearAuth,
    canAccessRoute,
    clearError
  }
}