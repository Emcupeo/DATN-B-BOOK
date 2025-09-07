import api from './api'

const AuthService = {
  // Đăng nhập
  async login(tenDangNhap, matKhau) {
    try {
      const response = await api.post('/auth/login', {
        tenDangNhap,
        matKhau
      })
      
      if (response.data.success) {
        // Lưu thông tin user và token vào localStorage
        localStorage.setItem('token', response.data.token)
        localStorage.setItem('user', JSON.stringify(response.data.userInfo))
        
        // Set token cho api instance
        api.defaults.headers.common['Authorization'] = `Bearer ${response.data.token}`
      }
      
      return response.data
    } catch (error) {
      console.error('Login error:', error)
      if (error.response && error.response.data) {
        throw new Error(error.response.data.message || 'Đăng nhập thất bại')
      }
      throw new Error('Lỗi kết nối server')
    }
  },

  // Đăng ký
  async register(userData) {
    try {
      const response = await api.post('/auth/nguoi-dung', {
        tenDangNhap: userData.tenDangNhap,
        matKhau: userData.matKhau,
        email: userData.email,
        hoTen: userData.hoTen,
        soDienThoai: userData.soDienThoai,
        loaiNguoiDung: userData.loaiNguoiDung || 'KHACH_HANG'
      })
      
      return response.data
    } catch (error) {
      console.error('Register error:', error)
      if (error.response && error.response.data) {
        throw new Error(error.response.data.error || 'Đăng ký thất bại')
      }
      throw new Error('Lỗi kết nối server')
    }
  },

  // Đăng xuất
  logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    delete api.defaults.headers.common['Authorization']
  },

  // Kiểm tra trạng thái đăng nhập
  isAuthenticated() {
    const token = localStorage.getItem('token')
    const user = localStorage.getItem('user')
    return !!(token && user)
  },

  // Lấy thông tin user hiện tại
  getCurrentUser() {
    const user = localStorage.getItem('user')
    return user ? JSON.parse(user) : null
  },

  // Lấy token hiện tại
  getToken() {
    return localStorage.getItem('token')
  },

  // Khởi tạo token khi app start
  initializeAuth() {
    const token = this.getToken()
    if (token) {
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`
    }
  },

  // Kiểm tra role của user
  hasRole(role) {
    const user = this.getCurrentUser()
    return user && user.loaiNguoiDung === role
  },

  // Kiểm tra có phải admin không
  isAdmin() {
    return this.hasRole('ADMIN')
  },

  // Kiểm tra có phải nhân viên không
  isNhanVien() {
    return this.hasRole('NHAN_VIEN')
  },

  // Kiểm tra có phải khách hàng không
  isKhachHang() {
    return this.hasRole('KHACH_HANG')
  },

  // Lấy route mặc định theo role
  getDefaultRoute() {
    const user = this.getCurrentUser()
    if (!user) return '/'
    
    switch (user.loaiNguoiDung) {
      case 'ADMIN':
        return '/admin/dashboard'
      case 'NHAN_VIEN':
        return '/admin/ban-hang-tai-quay'
      case 'KHACH_HANG':
        return '/'
      default:
        return '/'
    }
  },

  // Kiểm tra quyền truy cập route
  canAccessRoute(route) {
    const user = this.getCurrentUser()
    if (!user) {
      // Chỉ cho phép truy cập shop (base '/') và auth routes khi chưa đăng nhập
      return !route.startsWith('/admin') || route.startsWith('/auth') || route === '/'
    }

    // Admin có thể truy cập tất cả
    if (user.loaiNguoiDung === 'ADMIN') {
      return true
    }

    // Nhân viên chỉ truy cập được admin routes
    if (user.loaiNguoiDung === 'NHAN_VIEN') {
      return route.startsWith('/admin') || !route.startsWith('/admin')
    }

    // Khách hàng chỉ truy cập được shop
    if (user.loaiNguoiDung === 'KHACH_HANG') {
      return !route.startsWith('/admin') || route === '/'
    }

    return false
  },

  // Lấy thông tin profile
  async getProfile() {
    try {
      const response = await api.get('/profile')
      return response.data
    } catch (error) {
      console.error('Get profile error:', error)
      if (error.response && error.response.data) {
        throw new Error(error.response.data.message || 'Lấy thông tin profile thất bại')
      }
      throw new Error('Lỗi kết nối server')
    }
  },

  // Đổi mật khẩu
  async changePassword(currentPassword, newPassword) {
    try {
      const currentUser = this.getCurrentUser()
      if (!currentUser) {
        throw new Error('Người dùng chưa đăng nhập')
      }

      const response = await api.post('/profile/change-password', {
        tenDangNhap: currentUser.tenDangNhap,
        currentPassword,
        newPassword
      })
      return response.data
    } catch (error) {
      console.error('Change password error:', error)
      if (error.response && error.response.data) {
        throw new Error(error.response.data.message || 'Đổi mật khẩu thất bại')
      }
      throw new Error('Lỗi kết nối server')
    }
  }
}

export default AuthService