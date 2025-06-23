<template>
  <div class="container mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold mb-8">Thanh toán</h1>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
      <!-- Checkout Form -->
      <div class="space-y-6">
        <!-- Customer Information -->
        <div class="bg-white rounded-lg shadow-md p-6">
          <h2 class="text-xl font-semibold mb-4">Thông tin khách hàng</h2>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Họ và tên *</label>
              <input 
                type="text" 
                v-model="customerInfo.fullName"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                required
              >
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Số điện thoại *</label>
              <input 
                type="tel" 
                v-model="customerInfo.phone"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                required
              >
            </div>
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
              <input 
                type="email" 
                v-model="customerInfo.email"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
              >
            </div>
          </div>
        </div>

        <!-- Shipping Address -->
        <div class="bg-white rounded-lg shadow-md p-6">
          <h2 class="text-xl font-semibold mb-4">Địa chỉ giao hàng</h2>
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Địa chỉ *</label>
              <input 
                type="text" 
                v-model="shippingAddress.address"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                placeholder="Số nhà, tên đường"
                required
              >
            </div>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Thành phố *</label>
                <select 
                  v-model="shippingAddress.city"
                  class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                  required
                >
                  <option value="">Chọn thành phố</option>
                  <option value="ho-chi-minh">TP. Hồ Chí Minh</option>
                  <option value="ha-noi">Hà Nội</option>
                  <option value="da-nang">Đà Nẵng</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Quận/Huyện *</label>
                <input 
                  type="text" 
                  v-model="shippingAddress.district"
                  class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                  required
                >
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Phường/Xã *</label>
                <input 
                  type="text" 
                  v-model="shippingAddress.ward"
                  class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                  required
                >
              </div>
            </div>
          </div>
        </div>

        <!-- Payment Method -->
        <div class="bg-white rounded-lg shadow-md p-6">
          <h2 class="text-xl font-semibold mb-4">Phương thức thanh toán</h2>
          <div class="space-y-3">
            <label class="flex items-center space-x-3 cursor-pointer">
              <input 
                type="radio" 
                v-model="paymentMethod" 
                value="cod"
                class="text-blue-600"
              >
              <span>Thanh toán khi nhận hàng (COD)</span>
            </label>
            <label class="flex items-center space-x-3 cursor-pointer">
              <input 
                type="radio" 
                v-model="paymentMethod" 
                value="bank"
                class="text-blue-600"
              >
              <span>Chuyển khoản ngân hàng</span>
            </label>
            <label class="flex items-center space-x-3 cursor-pointer">
              <input 
                type="radio" 
                v-model="paymentMethod" 
                value="momo"
                class="text-blue-600"
              >
              <span>Ví MoMo</span>
            </label>
          </div>
        </div>
      </div>

      <!-- Order Summary -->
      <div class="space-y-6">
        <div class="bg-white rounded-lg shadow-md p-6">
          <h2 class="text-xl font-semibold mb-4">Đơn hàng của bạn</h2>
          
          <div class="space-y-3 mb-4">
            <div 
              v-for="item in cartItems" 
              :key="item.id"
              class="flex justify-between items-center py-2 border-b border-gray-100"
            >
              <div class="flex items-center space-x-3">
                <img 
                  :src="item.image" 
                  :alt="item.title"
                  class="w-12 h-16 object-cover rounded"
                >
                <div>
                  <p class="font-medium text-sm">{{ item.title }}</p>
                  <p class="text-gray-600 text-xs">{{ item.author }}</p>
                  <p class="text-xs">Số lượng: {{ item.quantity }}</p>
                </div>
              </div>
              <span class="font-semibold">{{ formatPrice(item.price * item.quantity) }}</span>
            </div>
          </div>

          <div class="space-y-2 mb-4">
            <div class="flex justify-between">
              <span>Tạm tính:</span>
              <span>{{ formatPrice(cartTotal) }}</span>
            </div>
            <div class="flex justify-between">
              <span>Phí vận chuyển:</span>
              <span class="text-green-600">Miễn phí</span>
            </div>
            <hr class="my-2">
            <div class="flex justify-between text-lg font-bold">
              <span>Tổng cộng:</span>
              <span class="text-red-600">{{ formatPrice(cartTotal) }}</span>
            </div>
          </div>

          <button 
            @click="placeOrder"
            :disabled="!isFormValid"
            :class="[
              'w-full py-3 rounded-lg font-semibold transition-colors',
              isFormValid 
                ? 'bg-blue-600 text-white hover:bg-blue-700' 
                : 'bg-gray-300 text-gray-500 cursor-not-allowed'
            ]"
          >
            Đặt hàng
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed } from 'vue'
import { useShopStore } from '../store'
import { useRouter } from 'vue-router'

export default {
  name: 'Checkout',
  setup() {
    const store = useShopStore()
    const router = useRouter()

    const customerInfo = ref({
      fullName: '',
      phone: '',
      email: ''
    })

    const shippingAddress = ref({
      address: '',
      city: '',
      district: '',
      ward: ''
    })

    const paymentMethod = ref('cod')

    const cartItems = computed(() => store.cart.value)
    const cartTotal = computed(() => store.cartTotal.value)

    const isFormValid = computed(() => {
      return customerInfo.value.fullName && 
             customerInfo.value.phone && 
             shippingAddress.value.address && 
             shippingAddress.value.city && 
             shippingAddress.value.district && 
             shippingAddress.value.ward &&
             paymentMethod.value
    })

    const formatPrice = (price) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(price)
    }

    const placeOrder = () => {
      if (!isFormValid.value) return

      // Simulate order placement
      alert('Đặt hàng thành công! Cảm ơn bạn đã mua hàng.')
      store.clearCart()
      router.push('/shop')
    }

    return {
      customerInfo,
      shippingAddress,
      paymentMethod,
      cartItems,
      cartTotal,
      isFormValid,
      formatPrice,
      placeOrder
    }
  }
}
</script>