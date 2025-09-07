<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header with Progress Steps -->
    <div class="bg-white shadow-sm border-b">
      <div class="container mx-auto px-4 py-4">
        <div class="flex items-center justify-between">
          <h1 class="text-xl font-semibold text-gray-900">Thanh toán bị hủy</h1>
          <div class="flex items-center space-x-2 text-sm">
            <!-- Step 1: Giỏ hàng -->
            <div class="flex items-center">
              <div class="w-6 h-6 rounded-full flex items-center justify-center text-xs font-medium bg-green-500 text-white">
                <i class="fas fa-check text-white text-xs"></i>
              </div>
              <span class="ml-2 font-medium text-green-600">Giỏ hàng</span>
            </div>
            
            <i class="fas fa-chevron-right text-gray-400 text-xs"></i>
            
            <!-- Step 2: Thanh toán -->
            <div class="flex items-center">
              <div class="w-6 h-6 rounded-full flex items-center justify-center text-xs font-medium bg-red-500 text-white">
                <i class="fas fa-times text-white text-xs"></i>
              </div>
              <span class="ml-2 font-medium text-red-600">Thanh toán</span>
            </div>
            
            <i class="fas fa-chevron-right text-gray-400 text-xs"></i>
            
            <!-- Step 3: Hoàn thành -->
            <div class="flex items-center">
              <div class="w-6 h-6 rounded-full flex items-center justify-center text-xs font-medium bg-gray-300 text-gray-600">
                <span class="text-white">3</span>
              </div>
              <span class="ml-2 font-medium text-gray-500">Hoàn thành</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="container mx-auto px-4 py-6">
      <div class="max-w-4xl mx-auto">

      <!-- Cancel Content -->
      <div class="bg-white rounded-3xl shadow-2xl p-8 border border-gray-100">
        <div class="text-center">
          <!-- Cancel Animation -->
          <div class="w-24 h-24 mx-auto mb-6 bg-gradient-to-r from-orange-400 to-red-500 rounded-full flex items-center justify-center shadow-lg">
            <svg class="w-12 h-12 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M6 18L18 6M6 6l12 12"></path>
            </svg>
          </div>
          
          <h1 class="text-4xl font-bold text-gray-900 mb-4">❌ Thanh toán bị hủy</h1>
          <p class="text-xl text-gray-600 mb-6">Bạn đã hủy quá trình thanh toán</p>
          
          <!-- Cancel Details -->
          <div class="bg-gradient-to-r from-orange-50 to-red-50 rounded-2xl p-6 mb-8">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div class="text-center">
                <div class="text-sm text-gray-500 mb-2">Mã đơn hàng</div>
                <div class="text-lg font-semibold text-gray-900">{{ orderCode || 'HD001' }}</div>
              </div>
              <div class="text-center">
                <div class="text-sm text-gray-500 mb-2">Lý do hủy</div>
                <div class="text-lg font-semibold text-gray-900">{{ cancelReason || 'Người dùng hủy thanh toán' }}</div>
              </div>
            </div>
          </div>


          <!-- Action Buttons -->
          <div class="flex flex-col sm:flex-row gap-4 justify-center">
            <button 
              @click="retryPayment"
              class="inline-flex items-center justify-center px-8 py-3 border border-transparent text-base font-medium rounded-xl text-white bg-blue-600 hover:bg-blue-700 transition-colors duration-200 shadow-lg hover:shadow-xl"
            >
              <i class="fas fa-credit-card mr-2"></i>
              Thanh toán lại
            </button>
            
            <router-link 
              to="/profile?tab=orders" 
              class="inline-flex items-center justify-center px-8 py-3 border border-gray-300 text-base font-medium rounded-xl text-gray-700 bg-white hover:bg-gray-50 transition-colors duration-200 shadow-lg hover:shadow-xl"
            >
              <i class="fas fa-list mr-2"></i>
              Xem đơn hàng
            </router-link>
            
            <router-link 
              to="/" 
              class="inline-flex items-center justify-center px-8 py-3 border border-gray-300 text-base font-medium rounded-xl text-gray-700 bg-white hover:bg-gray-50 transition-colors duration-200 shadow-lg hover:shadow-xl"
            >
              <i class="fas fa-home mr-2"></i>
              Về trang chủ
            </router-link>
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
import { useRealDataStore } from '../store/realDataStore'

export default {
  name: 'PaymentCancel',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const store = useRealDataStore()
    const orderCode = ref('')
    const cancelReason = ref('')

    onMounted(() => {
      // Lấy thông tin từ route params hoặc localStorage
      orderCode.value = route.query.orderCode || localStorage.getItem('lastOrderCode') || 'HD001'
      cancelReason.value = route.query.cancelReason || localStorage.getItem('lastCancelReason') || 'Người dùng hủy thanh toán'
      
      // KHÔNG xóa buyNowCart và buyNowCartBackup để có thể thanh toán lại
      // Chỉ clear các thông tin không cần thiết
      console.log('🔄 PaymentCancel mounted - preserving cart for retry')
      console.log('🛒 Current buyNowCart:', store.buyNowCart.value)
      console.log('💾 buyNowCartBackup exists:', !!localStorage.getItem('buyNowCartBackup'))
      
      // Clear localStorage sau khi lấy thông tin
      localStorage.removeItem('lastOrderCode')
      localStorage.removeItem('lastCancelReason')
    })

    // Function để thanh toán lại
    const retryPayment = () => {
      console.log('🔄 Retry payment clicked')
      console.log('🛒 Current buyNowCart:', store.buyNowCart.value)
      console.log('💾 buyNowCartBackup exists:', !!localStorage.getItem('buyNowCartBackup'))
      
      // Kiểm tra buyNowCartBackup trước (ưu tiên cao nhất)
      const buyNowBackup = localStorage.getItem('buyNowCartBackup')
      if (buyNowBackup) {
        try {
          const backupData = JSON.parse(buyNowBackup)
          if (backupData && backupData.length > 0) {
            // Khôi phục buyNowCart từ backup
            store.buyNowCart.value.splice(0, store.buyNowCart.value.length, ...backupData)
            console.log('🔄 Restored buyNowCart from backup:', backupData)
            console.log('🛒 Redirecting to checkout with buynow mode (from backup)')
            router.push('/checkout?mode=buynow')
            return
          }
        } catch (error) {
          console.error('❌ Error parsing buyNowCartBackup:', error)
        }
      }
      
      // Kiểm tra buyNowCart hiện tại
      if (store.buyNowCart.value.length > 0) {
        console.log('🛒 Redirecting to checkout with buynow mode (current)')
        router.push('/checkout?mode=buynow')
        return
      }
      
      // Kiểm tra giỏ hàng thường
      if (store.cart.value.length > 0) {
        console.log('🛒 Redirecting to checkout with normal cart')
        router.push('/checkout')
        return
      }
      
      // Không có giỏ hàng nào
      console.log('🛒 No cart items - redirecting to products')
      alert('Không có sản phẩm nào trong giỏ hàng. Vui lòng thêm sản phẩm trước khi thanh toán.')
      router.push('/products')
    }

    return {
      orderCode,
      cancelReason,
      retryPayment
    }
  }
}
</script>

<style scoped>
/* Custom styles if needed */
</style>