<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header with Progress Steps -->
    <div class="bg-white shadow-sm border-b">
      <div class="container mx-auto px-4 py-4">
        <div class="flex items-center justify-between">
          <h1 class="text-xl font-semibold text-gray-900">Đặt hàng thành công</h1>
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
              <div class="w-6 h-6 rounded-full flex items-center justify-center text-xs font-medium bg-green-500 text-white">
                <i class="fas fa-check text-white text-xs"></i>
              </div>
              <span class="ml-2 font-medium text-green-600">Thanh toán</span>
            </div>
            
            <i class="fas fa-chevron-right text-gray-400 text-xs"></i>
            
            <!-- Step 3: Hoàn thành -->
            <div class="flex items-center">
              <div class="w-6 h-6 rounded-full flex items-center justify-center text-xs font-medium bg-green-500 text-white">
                <i class="fas fa-check text-white text-xs"></i>
              </div>
              <span class="ml-2 font-medium text-green-600">Hoàn thành</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="container mx-auto px-4 py-6">
      <div class="max-w-4xl mx-auto">

      <!-- Success Content -->
      <div class="bg-white rounded-3xl shadow-2xl p-8 border border-gray-100">
        <div class="text-center">
          <!-- Success Animation -->
          <div class="w-24 h-24 mx-auto mb-6 bg-gradient-to-r from-green-400 to-green-600 rounded-full flex items-center justify-center shadow-lg animate-pulse">
            <svg class="w-12 h-12 text-white animate-bounce" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7"></path>
            </svg>
          </div>
          
          <h1 class="text-4xl font-bold text-gray-900 mb-4">🎉 Đặt hàng thành công!</h1>
          <p class="text-xl text-gray-600 mb-6">Cảm ơn bạn đã tin tưởng và mua sách tại BBook</p>
          
          <!-- Order Details -->
          <div class="bg-gradient-to-r from-blue-50 to-green-50 rounded-2xl p-6 mb-8">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div class="text-center">
                <div class="text-sm text-gray-500 mb-2">Mã đơn hàng</div>
                <div class="text-lg font-semibold text-gray-900">{{ orderCode || 'HD001' }}</div>
              </div>
              <div class="text-center">
                <div class="text-sm text-gray-500 mb-2">Phương thức thanh toán</div>
                <div class="text-lg font-semibold text-gray-900">{{ paymentMethod || 'COD' }}</div>
              </div>
              <div class="text-center">
                <div class="text-sm text-gray-500 mb-2">Thời gian giao hàng</div>
                <div class="text-lg font-semibold text-gray-900">{{ deliveryTime || '2-5 ngày làm việc' }}</div>
              </div>
            </div>
          </div>

          <!-- Additional Info -->
          <div class="bg-blue-50 rounded-xl p-6 mb-8">
            <h3 class="text-lg font-semibold text-gray-900 mb-3">Thông tin bổ sung</h3>
            <div class="text-sm text-gray-600 space-y-2">
              <p>• Bạn có thể theo dõi trạng thái đơn hàng trong mục "Đơn hàng của tôi"</p>
              <p>• Nếu có thắc mắc, vui lòng liên hệ hotline: 1900-1234</p>
            </div>
          </div>

          <!-- Action Buttons -->
          <div class="flex flex-col sm:flex-row gap-4 justify-center">
            <router-link 
              to="/" 
              class="inline-flex items-center justify-center px-8 py-3 border border-transparent text-base font-medium rounded-xl text-white bg-blue-600 hover:bg-blue-700 transition-colors duration-200 shadow-lg hover:shadow-xl"
            >
              <i class="fas fa-home mr-2"></i>
              Về trang chủ
            </router-link>
            
            <router-link 
              to="/profile?tab=orders" 
              class="inline-flex items-center justify-center px-8 py-3 border border-gray-300 text-base font-medium rounded-xl text-gray-700 bg-white hover:bg-gray-50 transition-colors duration-200 shadow-lg hover:shadow-xl"
            >
              <i class="fas fa-list mr-2"></i>
              Xem đơn hàng
            </router-link>
            
            <router-link 
              to="/products" 
              class="inline-flex items-center justify-center px-8 py-3 border border-gray-300 text-base font-medium rounded-xl text-gray-700 bg-white hover:bg-gray-50 transition-colors duration-200 shadow-lg hover:shadow-xl"
            >
              <i class="fas fa-shopping-bag mr-2"></i>
              Tiếp tục mua sắm
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
import { useRoute } from 'vue-router'
import { useRealDataStore } from '../store/realDataStore'

export default {
  name: 'OrderSuccess',
  setup() {
    const route = useRoute()
    const store = useRealDataStore()
    const orderCode = ref('')
    const paymentMethod = ref('')
    const deliveryTime = ref('')

    onMounted(() => {
      // Lấy thông tin từ route params hoặc localStorage
      orderCode.value = route.query.orderCode || localStorage.getItem('lastOrderCode') || 'HD001'
      paymentMethod.value = route.query.paymentMethod || localStorage.getItem('lastPaymentMethod') || 'COD'
      deliveryTime.value = route.query.deliveryTime || localStorage.getItem('lastDeliveryTime') || '2-5 ngày làm việc'
      
      // Clear buyNowCart sau khi đặt hàng thành công (chỉ nếu có)
      if (store.buyNowCart.value.length > 0) {
        store.clearBuyNowCart()
        localStorage.removeItem('buyNowCartBackup')
        console.log('✅ Đã clear buyNowCart sau khi đặt hàng thành công')
      }
      
      // Clear localStorage sau khi lấy thông tin
      localStorage.removeItem('lastOrderCode')
      localStorage.removeItem('lastPaymentMethod')
      localStorage.removeItem('lastDeliveryTime')
    })

    return {
      orderCode,
      paymentMethod,
      deliveryTime
    }
  }
}
</script>

<style scoped>
.animate-pulse {
  animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}

.animate-bounce {
  animation: bounce 1s infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: .5;
  }
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(-25%);
    animation-timing-function: cubic-bezier(0.8, 0, 1, 1);
  }
  50% {
    transform: translateY(0);
    animation-timing-function: cubic-bezier(0, 0, 0.2, 1);
  }
}
</style>