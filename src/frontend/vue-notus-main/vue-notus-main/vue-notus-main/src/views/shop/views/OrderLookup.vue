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
            <h1 class="text-xl font-semibold text-gray-900">Tra cứu đơn hàng</h1>
          </div>
        </div>
      </div>
    </div>

    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Search Form -->
      <div class="bg-white rounded-2xl shadow-lg p-8 mb-8">
        <div class="text-center mb-8">
          <div class="w-16 h-16 mx-auto bg-blue-100 rounded-full flex items-center justify-center mb-4">
            <svg class="w-8 h-8 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
            </svg>
          </div>
          <h2 class="text-2xl font-bold text-gray-900 mb-2">Tra cứu đơn hàng</h2>
          <p class="text-gray-600">Nhập mã hóa đơn và số điện thoại người nhận để xem thông tin đơn hàng của bạn</p>
        </div>

        <form @submit.prevent="searchOrder" class="space-y-6">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- Order Code Input -->
            <div>
              <label for="orderCode" class="block text-sm font-medium text-gray-700 mb-2">
                Mã hóa đơn <span class="text-red-500">*</span>
              </label>
              <input
                id="orderCode"
                v-model="searchForm.orderCode"
                type="text"
                placeholder="Nhập mã hóa đơn (VD: HD001)"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
                :class="{ 'border-red-500': errors.orderCode }"
                required
              />
              <p v-if="errors.orderCode" class="mt-1 text-sm text-red-600">{{ errors.orderCode }}</p>
            </div>

            <!-- Phone Number Input -->
            <div>
              <label for="phoneNumber" class="block text-sm font-medium text-gray-700 mb-2">
                Số điện thoại người nhận <span class="text-red-500">*</span>
              </label>
              <input
                id="phoneNumber"
                v-model="searchForm.phoneNumber"
                type="tel"
                placeholder="Nhập số điện thoại người nhận (VD: 0123456789)"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
                :class="{ 'border-red-500': errors.phoneNumber }"
                required
              />
              <p v-if="errors.phoneNumber" class="mt-1 text-sm text-red-600">{{ errors.phoneNumber }}</p>
            </div>
          </div>

          <!-- Search Button -->
          <div class="text-center">
            <button
              type="submit"
              :disabled="loading"
              class="inline-flex items-center justify-center px-8 py-3 border border-transparent text-base font-medium rounded-xl text-white bg-blue-600 hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed transition-colors duration-200 shadow-lg hover:shadow-xl"
            >
              <svg v-if="loading" class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              <svg v-else class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
              </svg>
              {{ loading ? 'Đang tìm kiếm...' : 'Tìm kiếm đơn hàng' }}
            </button>
          </div>
        </form>

        <!-- Error Message -->
        <div v-if="error" class="mt-6 bg-red-50 border border-red-200 rounded-lg p-4">
          <div class="flex">
            <div class="flex-shrink-0">
              <svg class="h-5 w-5 text-red-400" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
              </svg>
            </div>
            <div class="ml-3">
              <h3 class="text-sm font-medium text-red-800">Không tìm thấy đơn hàng</h3>
              <div class="mt-2 text-sm text-red-700">
                <p>{{ error }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Order Results -->
      <div v-if="order" class="bg-white rounded-2xl shadow-lg overflow-hidden">
        <!-- Order Header -->
        <div class="bg-gradient-to-r from-blue-600 to-blue-700 px-6 py-4">
          <div class="flex items-center justify-between">
            <div>
              <h3 class="text-lg font-semibold text-white">Đơn hàng #{{ order.maHoaDon }}</h3>
              <p class="text-blue-100 text-sm">{{ formatDate(order.ngayDatHang) }}</p>
            </div>
            <div class="text-right">
              <div class="text-white text-lg font-semibold">{{ formatPrice(order.tongTien) }}</div>
              <div class="text-blue-100 text-sm">
                <span :class="getStatusColor(order.trangThai)" class="px-2 py-1 rounded-full text-xs font-medium">
                  {{ getStatusText(order.trangThai) }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- Order Details -->
        <div class="p-6">
          <!-- Customer Info -->
          <div class="mb-6">
            <h4 class="text-lg font-semibold text-gray-900 mb-3">Thông tin người nhận</h4>
            <div class="bg-gray-50 rounded-lg p-4">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <span class="text-sm text-gray-500">Họ tên:</span>
                  <p class="font-medium text-gray-900">{{ order.tenNguoiNhan || 'Chưa có thông tin' }}</p>
                </div>
                <div>
                  <span class="text-sm text-gray-500">Số điện thoại:</span>
                  <p class="font-medium text-gray-900">{{ order.soDienThoaiNguoiNhan || 'Chưa có thông tin' }}</p>
                </div>
                <div v-if="order.khachHang?.email" class="md:col-span-2">
                  <span class="text-sm text-gray-500">Email:</span>
                  <p class="font-medium text-gray-900">{{ order.khachHang.email }}</p>
                </div>
              </div>
            </div>
          </div>

          <!-- Shipping Address -->
          <div v-if="order.diaChi" class="mb-6">
            <h4 class="text-lg font-semibold text-gray-900 mb-3">Địa chỉ giao hàng</h4>
            <div class="bg-gray-50 rounded-lg p-4">
              <p class="text-gray-900">{{ order.diaChi }}</p>
            </div>
          </div>

          <!-- Order Items -->
          <div class="mb-6">
            <h4 class="text-lg font-semibold text-gray-900 mb-3">Sản phẩm đã đặt</h4>
            <div class="space-y-4">
              <div
                v-for="item in order.hoaDonChiTiets"
                :key="item.id"
                class="flex items-center space-x-4 p-4 bg-gray-50 rounded-lg"
              >
                <img
                  :src="getProductImage(item.chiTietSanPham)"
                  :alt="item.chiTietSanPham?.sanPham?.tenSanPham || 'Sản phẩm'"
                  class="w-16 h-16 object-cover rounded-lg"
                  @error="handleImageError"
                />
                <div class="flex-1">
                  <h5 class="font-medium text-gray-900">
                    {{ getProductName(item) }}
                  </h5>
                  <p class="text-sm text-gray-600">
                    Số lượng: {{ item.soLuong }} × {{ formatPrice(item.giaSanPham) }}
                  </p>
                </div>
                <div class="text-right">
                  <p class="font-semibold text-gray-900">{{ formatPrice(item.thanhTien) }}</p>
                </div>
              </div>
            </div>
          </div>

          <!-- Order Summary -->
          <div class="border-t pt-6">
            <div class="space-y-2">
              <div class="flex justify-between">
                <span class="text-gray-600">Tạm tính:</span>
                <span class="text-gray-900">{{ formatPrice(getSubtotal()) }}</span>
              </div>
              <div class="flex justify-between">
                <span class="text-gray-600">Phí vận chuyển:</span>
                <span class="text-gray-900">{{ formatPrice(order.phiShip || 0) }}</span>
              </div>
              <div v-if="order.phieuGiamGia && getDiscount() > 0" class="flex justify-between">
                <span class="text-gray-600">Giảm giá:</span>
                <span class="text-green-600">-{{ formatPrice(getDiscount()) }}</span>
              </div>
              <div v-if="order.phieuGiamGia && getDiscount() > 0" class="text-xs text-gray-500 mt-1">
                Phiếu: {{ order.phieuGiamGia.tenPhieuGiamGia || order.phieuGiamGia.tenPhieu }}
                <span v-if="order.phieuGiamGia.soPhanTramGiam"> ({{ order.phieuGiamGia.soPhanTramGiam }}%)</span>
                <span v-else-if="order.phieuGiamGia.giaTriGiam"> ({{ formatPrice(order.phieuGiamGia.giaTriGiam) }})</span>
              </div>
              <div class="flex justify-between text-lg font-semibold border-t pt-2">
                <span class="text-gray-900">Tổng cộng:</span>
                <span class="text-blue-600">{{ formatPrice(order.tongTien) }}</span>
              </div>
            </div>
          </div>

          <!-- Action Buttons -->
          <div class="mt-6 flex flex-col sm:flex-row gap-3">
            <button
              @click="printOrder"
              class="flex-1 inline-flex items-center justify-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-lg text-gray-700 bg-white hover:bg-gray-50 transition-colors"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 17h2a2 2 0 002-2v-4a2 2 0 00-2-2H5a2 2 0 00-2 2v4a2 2 0 002 2h2m2 4h6a2 2 0 002-2v-4a2 2 0 00-2-2H9a2 2 0 00-2 2v4a2 2 0 002 2zm8-12V5a2 2 0 00-2-2H9a2 2 0 00-2 2v4h10z"></path>
              </svg>
              In đơn hàng
            </button>
            <button
              @click="searchAnother"
              class="flex-1 inline-flex items-center justify-center px-4 py-2 border border-transparent text-sm font-medium rounded-lg text-white bg-blue-600 hover:bg-blue-700 transition-colors"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
              </svg>
              Tra cứu đơn khác
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import HoaDonService from '@/service/hoaDonService'

export default {
  name: 'OrderLookup',
  setup() {
    const router = useRouter()
    
    const searchForm = reactive({
      orderCode: '',
      phoneNumber: ''
    })
    
    const errors = reactive({})
    const loading = ref(false)
    const error = ref('')
    const order = ref(null)

    const validateForm = () => {
      errors.orderCode = ''
      errors.phoneNumber = ''
      
      if (!searchForm.orderCode.trim()) {
        errors.orderCode = 'Vui lòng nhập mã hóa đơn'
        return false
      }
      
      if (!searchForm.phoneNumber.trim()) {
        errors.phoneNumber = 'Vui lòng nhập số điện thoại người nhận'
        return false
      }
      
      // Validate phone number format
      const phoneRegex = /^[0-9]{10,11}$/
      if (!phoneRegex.test(searchForm.phoneNumber.replace(/\s/g, ''))) {
        errors.phoneNumber = 'Số điện thoại không hợp lệ (10-11 số)'
        return false
      }
      
      return true
    }

    const searchOrder = async () => {
      if (!validateForm()) return
      
      loading.value = true
      error.value = ''
      order.value = null
      
      try {
        console.log('🔍 Searching order:', searchForm)
        console.log('🔍 Order code:', searchForm.orderCode.trim())
        console.log('🔍 Phone number:', searchForm.phoneNumber.trim())
        
        // Call service to search order by code and phone
        const response = await HoaDonService.lookupOrder(
          searchForm.orderCode.trim(),
          searchForm.phoneNumber.trim()
        )
        
        console.log('✅ Order found:', response.data)
        console.log('🔍 DEBUG: Order details:', response.data.hoaDonChiTiets)
        order.value = response.data
      } catch (err) {
        console.error('❌ Error searching order:', err)
        if (err.response && err.response.status === 404) {
          error.value = 'Không tìm thấy đơn hàng với thông tin đã nhập'
        } else {
          error.value = err.response?.data?.message || err.message || 'Có lỗi xảy ra khi tìm kiếm đơn hàng'
        }
      } finally {
        loading.value = false
      }
    }

    const getProductName = (item) => {
      console.log('🔍 DEBUG: Product item:', item)
      console.log('🔍 DEBUG: chiTietSanPham:', item.chiTietSanPham)
      console.log('🔍 DEBUG: sanPham:', item.chiTietSanPham?.sanPham)
      
      // Thử tenChiTietSanPham trước (từ console log thấy có dữ liệu)
      if (item.chiTietSanPham?.tenChiTietSanPham) {
        return item.chiTietSanPham.tenChiTietSanPham
      }
      
      // Fallback: thử các path khác
      if (item.chiTietSanPham?.sanPham?.tenSanPham) {
        return item.chiTietSanPham.sanPham.tenSanPham
      }
      
      if (item.tenSanPham) {
        return item.tenSanPham
      }
      
      if (item.sanPham?.tenSanPham) {
        return item.sanPham.tenSanPham
      }
      
      return 'Sản phẩm không xác định'
    }

    const getProductImage = (chiTietSanPham) => {
      if (!chiTietSanPham) {
        return 'https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=400'
      }
      
      if (chiTietSanPham.anhSanPhams && chiTietSanPham.anhSanPhams.length > 0) {
        const firstImage = chiTietSanPham.anhSanPhams[0]
        if (firstImage && firstImage.url) {
          return firstImage.url
        }
      }
      
      return 'https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=400'
    }

    const handleImageError = (event) => {
      event.target.src = 'https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=400'
    }

    const formatPrice = (price) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(price || 0)
    }

    const formatDate = (date) => {
      if (!date) return 'Chưa có thông tin'
      return new Date(date).toLocaleDateString('vi-VN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      })
    }

    const getSubtotal = () => {
      if (!order.value || !order.value.hoaDonChiTiets) return 0
      return order.value.hoaDonChiTiets.reduce((total, item) => {
        return total + (item.giaSanPham * item.soLuong)
      }, 0)
    }

    const getDiscount = () => {
      if (!order.value || !order.value.phieuGiamGia) return 0
      
      const voucher = order.value.phieuGiamGia
      const subtotal = getSubtotal()
      
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

    const getStatusText = (status) => {
      const statusMap = {
        'PENDING': 'Chờ xử lý',
        'CONFIRMED': 'Đã xác nhận',
        'PROCESSING': 'Đang xử lý',
        'SHIPPED': 'Đã giao hàng',
        'DELIVERED': 'Đã nhận hàng',
        'CANCELLED': 'Đã hủy',
        'RETURNED': 'Đã trả hàng',
        'Đã thanh toán': 'Đã thanh toán',
        'Chờ thanh toán': 'Chờ thanh toán',
        'Đang giao hàng': 'Đang giao hàng',
        'Đã giao hàng': 'Đã giao hàng',
        'Đã hủy': 'Đã hủy'
      }
      return statusMap[status] || status || 'Chưa xác định'
    }

    const getStatusColor = (status) => {
      const colorMap = {
        'PENDING': 'text-yellow-600 bg-yellow-100',
        'CONFIRMED': 'text-blue-600 bg-blue-100',
        'PROCESSING': 'text-purple-600 bg-purple-100',
        'SHIPPED': 'text-indigo-600 bg-indigo-100',
        'DELIVERED': 'text-green-600 bg-green-100',
        'CANCELLED': 'text-red-600 bg-red-100',
        'RETURNED': 'text-gray-600 bg-gray-100',
        'Đã thanh toán': 'text-green-600 bg-green-100',
        'Chờ thanh toán': 'text-yellow-600 bg-yellow-100',
        'Đang giao hàng': 'text-blue-600 bg-blue-100',
        'Đã giao hàng': 'text-indigo-600 bg-indigo-100',
        'Đã hủy': 'text-red-600 bg-red-100'
      }
      return colorMap[status] || 'text-gray-600 bg-gray-100'
    }

    const printOrder = () => {
      window.print()
    }

    const searchAnother = () => {
      order.value = null
      error.value = ''
      searchForm.orderCode = ''
      searchForm.phoneNumber = ''
    }

    const goBack = () => {
      router.go(-1)
    }

    return {
      searchForm,
      errors,
      loading,
      error,
      order,
      searchOrder,
      getProductName,
      getProductImage,
      handleImageError,
      formatPrice,
      formatDate,
      getSubtotal,
      getDiscount,
      getStatusText,
      getStatusColor,
      printOrder,
      searchAnother,
      goBack
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
