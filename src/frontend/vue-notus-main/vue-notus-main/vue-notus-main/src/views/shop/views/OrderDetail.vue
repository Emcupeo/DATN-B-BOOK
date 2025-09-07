<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <div class="bg-white shadow-sm border-b">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex items-center justify-between h-16">
              <div class="flex items-center">
            <button @click="goBack" class="mr-4 p-2 rounded-full hover:bg-gray-100 transition-colors">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path>
                </svg>
            </button>
            <h1 class="text-xl font-semibold text-gray-900">Chi tiết đơn hàng</h1>
          </div>
          <div class="flex items-center space-x-4">
            <span class="text-sm text-gray-500">Đơn hàng #{{ order?.maHoaDon || '...' }}</span>
          </div>
        </div>
              </div>
              </div>

    <!-- Loading State -->
    <div v-if="loading" class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="flex items-center justify-center h-64">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            </div>
          </div>

    <!-- Error State -->
    <div v-else-if="error" class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="bg-red-50 border border-red-200 rounded-lg p-6 text-center">
        <div class="text-red-600 text-lg font-medium mb-2">Không thể tải đơn hàng</div>
        <p class="text-red-500 mb-4">{{ error }}</p>
        <button @click="loadOrder" class="bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700 transition-colors">
          Thử lại
        </button>
        </div>
      </div>

    <!-- Order Content -->
    <div v-else-if="order" class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <!-- Main Content -->
        <div class="lg:col-span-2 space-y-6">
          <!-- Order Status -->
          <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <div class="flex items-center justify-between mb-4">
              <h2 class="text-lg font-bold text-gray-800">Trạng thái đơn hàng</h2>
              <span :class="getStatusClass(order.trangThai)" class="px-4 py-2 rounded-full text-sm font-medium">
                {{ getStatusText(order.trangThai) }}
              </span>
            </div>
            <div class="text-sm text-gray-600">
              <p><span class="font-medium">Mã đơn hàng:</span> {{ order.maHoaDon }}</p>
              <p><span class="font-medium">Ngày đặt:</span> {{ formatDate(order.ngayDatHang) }}</p>
              <p><span class="font-medium">Tổng tiền:</span> {{ formatPrice(order.tongTien) }}</p>
            </div>
          </div>

          <!-- Products -->
          <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h2 class="text-lg font-bold text-gray-800 mb-4">Sản phẩm đã đặt</h2>
            <div class="space-y-4">
              <div 
                v-for="item in order.hoaDonChiTiets" 
                :key="item.id"
                class="flex items-center space-x-4 p-4 border border-gray-100 rounded-lg hover:bg-gray-50 transition-colors"
              >
                <!-- Product Image -->
                <div class="w-20 h-24 bg-gray-100 rounded-lg overflow-hidden flex-shrink-0">
                  <img 
                    :src="getProductImage(item.chiTietSanPham)" 
                    :alt="item.chiTietSanPham?.tenChiTietSanPham || 'Sản phẩm'"
                    class="w-full h-full object-cover"
                    @error="handleImageError"
                  >
            </div>

                <!-- Product Info -->
                <div class="flex-1 min-w-0">
                  <h3 class="font-medium text-gray-800 truncate">
                    {{ item.chiTietSanPham?.tenChiTietSanPham || 'Sản phẩm không xác định' }}
                  </h3>
                  <div class="text-sm text-gray-600 space-y-1">
                    <p v-if="item.chiTietSanPham?.idTacGia?.tenTacGia">
                      <span class="font-medium">Tác giả:</span> {{ item.chiTietSanPham.idTacGia.tenTacGia }}
                    </p>
                    <p v-if="item.chiTietSanPham?.idNhaXuatBan?.tenNhaXuatBan">
                      <span class="font-medium">NXB:</span> {{ item.chiTietSanPham.idNhaXuatBan.tenNhaXuatBan }}
                    </p>
                    <p v-if="item.chiTietSanPham?.isbn">
                      <span class="font-medium">ISBN:</span> {{ item.chiTietSanPham.isbn }}
                    </p>
                </div>
                  <div class="mt-2">
                    <span class="text-sm text-gray-500">Số lượng: {{ item.soLuong }}</span>
                </div>
                </div>
                
                <!-- Price -->
                <div class="text-right flex-shrink-0">
                  <p class="font-medium text-gray-800">{{ formatPrice(item.giaSanPham) }}</p>
                  <p class="text-sm text-gray-500">x {{ item.soLuong }}</p>
                  <p class="font-semibold text-blue-600">{{ formatPrice(item.thanhTien || (parseFloat(item.giaSanPham || 0) * parseInt(item.soLuong || 0))) }}</p>
                </div>
              </div>
            </div>
          </div>

          <!-- Order Timeline -->
          <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h2 class="text-lg font-bold text-gray-800 mb-4">Lịch sử đơn hàng</h2>
            <div class="space-y-4">
              <div class="flex items-start space-x-3">
                <div class="w-2 h-2 bg-blue-600 rounded-full mt-2"></div>
                <div>
                  <p class="font-medium text-gray-800">Đơn hàng đã được tạo</p>
                  <p class="text-sm text-gray-500">{{ formatDate(order.ngayDatHang) }}</p>
                </div>
              </div>
              <div class="flex items-start space-x-3">
                <div class="w-2 h-2 bg-gray-300 rounded-full mt-2"></div>
                <div>
                  <p class="font-medium text-gray-600">Đang xử lý</p>
                  <p class="text-sm text-gray-500">Đơn hàng đang được xử lý</p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Sidebar -->
        <div class="space-y-6">
          <!-- Order Summary -->
          <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h3 class="text-lg font-bold text-gray-800 mb-4">Tóm tắt đơn hàng</h3>
            <div class="space-y-3">
              <div class="flex justify-between">
                <span class="text-gray-600">Tạm tính:</span>
                <span>{{ formatPrice(calculateSubtotal()) }}</span>
              </div>
              <div class="flex justify-between">
                <span class="text-gray-600">Phí vận chuyển:</span>
                <span>{{ formatPrice(order.phiShip || 0) }}</span>
              </div>
              <div class="flex justify-between">
                <span class="text-gray-600">Giảm giá:</span>
                <span>{{ formatPrice(calculateDiscount()) }}</span>
              </div>
              <div v-if="order.phieuGiamGia" class="text-xs text-gray-500">
                Phiếu: {{ order.phieuGiamGia.tenPhieuGiamGia || order.phieuGiamGia.tenPhieu }}
                <span v-if="order.phieuGiamGia.soPhanTramGiam"> ({{ order.phieuGiamGia.soPhanTramGiam }}%)</span>
              </div>
              <div class="border-t pt-3">
                <div class="flex justify-between font-bold text-lg">
                  <span>Tổng cộng:</span>
                  <span class="text-blue-600">{{ formatPrice(calculateTotal()) }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- Customer Info -->
          <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h3 class="text-lg font-bold text-gray-800 mb-4">Thông tin khách hàng</h3>
            <div class="space-y-2 text-sm">
              <p>
                <span class="font-medium">Tên người nhận:</span> {{ order.tenNguoiNhan || 'Chưa cập nhật' }}
              </p>
              <p>
                <span class="font-medium">SĐT người nhận:</span> {{ order.soDienThoaiNguoiNhan || 'Chưa cập nhật' }}
              </p>
              <p>
                <span class="font-medium">Địa chỉ giao hàng:</span> {{ order.diaChi || 'Chưa cập nhật' }}
              </p>
              <div v-if="order.khachHang" class="mt-3 pt-3 border-t border-gray-200">
                <p class="text-xs text-gray-500 mb-1">Thông tin tài khoản:</p>
                <p class="text-xs">
                  <span class="font-medium">Tên tài khoản:</span> {{ order.khachHang.hoTen || order.khachHang.tenKhachHang || 'Chưa cập nhật' }}
                </p>
                <p class="text-xs">
                  <span class="font-medium">Email:</span> {{ order.khachHang.email || 'Chưa cập nhật' }}
                </p>
              </div>
            </div>
          </div>

          <!-- Actions -->
          <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h3 class="text-lg font-bold text-gray-800 mb-4">Thao tác</h3>
            <div class="space-y-3">
              <button 
                @click="printOrder"
                class="w-full bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors"
              >
                In đơn hàng
              </button>
              <button 
                @click="contactSupport"
                class="w-full bg-gray-100 text-gray-700 py-2 px-4 rounded-lg hover:bg-gray-200 transition-colors"
              >
                Liên hệ hỗ trợ
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

export default {
  name: 'OrderDetail',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const orderId = route.params.id

    const order = ref(null)
    const loading = ref(true)
    const error = ref(null)

    const loadOrder = async () => {
      try {
        loading.value = true
        error.value = null
        
        console.log(`🔄 Loading order details for ID: ${orderId}`)
        
        const response = await fetch(`http://localhost:8080/api/hoa-don/${orderId}`)
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}: ${response.statusText}`)
        }
        
        const orderData = await response.json()
        console.log('📦 Order data loaded:', orderData)
        
        order.value = orderData
      } catch (err) {
        console.error('❌ Error loading order:', err)
        error.value = err.message || 'Có lỗi xảy ra khi tải đơn hàng'
      } finally {
        loading.value = false
      }
    }

    const getProductImage = (chiTietSanPham) => {
      if (!chiTietSanPham) {
        return 'https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=400'
      }
      
      // Lấy ảnh từ anhSanPhams
      if (chiTietSanPham.anhSanPhams && chiTietSanPham.anhSanPhams.length > 0) {
        const firstImage = chiTietSanPham.anhSanPhams[0]
        if (firstImage && firstImage.url) {
          return firstImage.url
        }
      }
      
      // Fallback image
      return 'https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=400'
    }

    const handleImageError = (event) => {
      event.target.src = 'https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=400'
    }

    const formatDate = (date) => {
      if (!date) return 'Chưa có ngày'
      try {
        const dateObj = new Date(date)
        if (isNaN(dateObj.getTime())) return 'Ngày không hợp lệ'
        return dateObj.toLocaleDateString('vi-VN', {
          year: 'numeric',
          month: 'long',
          day: 'numeric',
          hour: '2-digit',
          minute: '2-digit'
        })
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
        'Chờ xác nhận': 'Chờ xác nhận',
        'Đã xác nhận': 'Đã xác nhận',
        'Đang giao': 'Đang giao',
        'Đã giao': 'Đã giao',
        'Đã hủy': 'Đã hủy',
        'Hoàn trả': 'Hoàn trả'
      }
      return statusMap[status] || status || 'Không xác định'
    }

    const getStatusClass = (status) => {
      const classMap = {
        'Chờ xác nhận': 'bg-yellow-100 text-yellow-800',
        'Đã xác nhận': 'bg-blue-100 text-blue-800',
        'Đang giao': 'bg-purple-100 text-purple-800',
        'Đã giao': 'bg-green-100 text-green-800',
        'Đã hủy': 'bg-red-100 text-red-800',
        'Hoàn trả': 'bg-gray-100 text-gray-800'
      }
      return classMap[status] || 'bg-gray-100 text-gray-800'
    }

    const goBack = () => {
      router.go(-1)
    }

    const printOrder = () => {
      window.print()
    }

    const contactSupport = () => {
      alert('Liên hệ hỗ trợ: Hotline 1900-1234 hoặc Email: support@bbook.com')
    }

    const calculateSubtotal = () => {
      if (!order.value || !order.value.hoaDonChiTiets) return 0
      return order.value.hoaDonChiTiets.reduce((total, item) => {
        const itemTotal = parseFloat(item.thanhTien || 0) || (parseFloat(item.giaSanPham || 0) * parseInt(item.soLuong || 0))
        return total + itemTotal
      }, 0)
    }

    const calculateDiscount = () => {
      if (!order.value || !order.value.phieuGiamGia) return 0
      
      const voucher = order.value.phieuGiamGia
      const subtotal = calculateSubtotal()
      
      // Kiểm tra đơn tối thiểu
      if (voucher.giaTriDonHangToiThieu && subtotal < voucher.giaTriDonHangToiThieu) {
        return 0
      }
      
      // Tính giảm giá theo phần trăm
      if (voucher.soPhanTramGiam && voucher.soPhanTramGiam > 0) {
        return (subtotal * voucher.soPhanTramGiam) / 100
      }
      
      // Tính giảm giá theo số tiền cố định
      if (voucher.giaTriGiam && voucher.giaTriGiam > 0) {
        return voucher.giaTriGiam
      }
      
      return 0
    }

    const calculateTotal = () => {
      if (!order.value) return 0
      const subtotal = calculateSubtotal()
      const shipping = parseFloat(order.value.phiShip || 0)
      const discount = calculateDiscount()
      return subtotal + shipping - discount
    }

    onMounted(() => {
      loadOrder()
    })

    return {
      order,
      loading,
      error,
      loadOrder,
      getProductImage,
      handleImageError,
      formatDate,
      formatPrice,
      getStatusText,
      getStatusClass,
      goBack,
      printOrder,
      contactSupport,
      calculateSubtotal,
      calculateDiscount,
      calculateTotal
    }
  }
}
</script> 

<style scoped>
@media print {
  .no-print {
    display: none !important;
  }
}
</style>