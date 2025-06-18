<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Breadcrumb -->
    <div class="bg-white border-b border-gray-200">
      <div class="container mx-auto px-4 py-4">
        <nav class="flex" aria-label="Breadcrumb">
          <ol class="inline-flex items-center space-x-1 md:space-x-3">
            <li class="inline-flex items-center">
              <router-link to="/shop" class="inline-flex items-center text-sm font-medium text-gray-700 hover:text-blue-600">
                <svg class="w-4 h-4 mr-2" fill="currentColor" viewBox="0 0 20 20">
                  <path d="M10.707 2.293a1 1 0 00-1.414 0l-7 7a1 1 0 001.414 1.414L4 10.414V17a1 1 0 001 1h2a1 1 0 001-1v-2a1 1 0 011-1h2a1 1 0 011 1v2a1 1 0 001 1h2a1 1 0 001-1v-6.586l.293.293a1 1 0 001.414-1.414l-7-7z"></path>
                </svg>
                Trang chủ
              </router-link>
            </li>
            <li>
              <div class="flex items-center">
                <svg class="w-6 h-6 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd"></path>
                </svg>
                <router-link to="/shop/profile" class="ml-1 text-sm font-medium text-gray-700 hover:text-blue-600 md:ml-2">Tài khoản</router-link>
              </div>
            </li>
            <li>
              <div class="flex items-center">
                <svg class="w-6 h-6 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd"></path>
                </svg>
                <span class="ml-1 text-sm font-medium text-gray-500 md:ml-2">Đơn hàng #{{ orderId }}</span>
              </div>
            </li>
          </ol>
        </nav>
      </div>
    </div>

    <div class="container mx-auto px-4 py-8">
      <!-- Order Header -->
      <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6 mb-8">
        <div class="flex items-center justify-between mb-4">
          <div>
            <h1 class="text-2xl font-bold text-gray-800">Đơn hàng #{{ orderId }}</h1>
            <p class="text-gray-600">Đặt hàng ngày {{ formatDate(order.orderDate) }}</p>
          </div>
          <div class="text-right">
            <span 
              :class="[
                'px-4 py-2 rounded-full text-sm font-medium',
                getStatusClass(order.status)
              ]"
            >
              {{ getStatusText(order.status) }}
            </span>
          </div>
        </div>

        <!-- Order Progress -->
        <div class="mb-6">
          <div class="flex items-center justify-between">
            <div 
              v-for="(step, index) in orderSteps" 
              :key="step.id"
              class="flex items-center"
            >
              <div 
                :class="[
                  'w-8 h-8 rounded-full flex items-center justify-center text-sm font-medium',
                  step.completed 
                    ? 'bg-green-500 text-white' 
                    : step.active 
                      ? 'bg-blue-500 text-white' 
                      : 'bg-gray-200 text-gray-500'
                ]"
              >
                <span v-if="step.completed">✓</span>
                <span v-else>{{ index + 1 }}</span>
              </div>
              <div class="ml-3">
                <p class="text-sm font-medium text-gray-800">{{ step.name }}</p>
                <p class="text-xs text-gray-500">{{ step.description }}</p>
              </div>
              <div 
                v-if="index < orderSteps.length - 1"
                :class="[
                  'w-16 h-0.5 mx-4',
                  step.completed ? 'bg-green-500' : 'bg-gray-200'
                ]"
              ></div>
            </div>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <!-- Order Details -->
        <div class="lg:col-span-2 space-y-6">
          <!-- Products -->
          <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h2 class="text-lg font-bold text-gray-800 mb-4">Sản phẩm đã đặt</h2>
            <div class="space-y-4">
              <div 
                v-for="item in order.items" 
                :key="item.id"
                class="flex items-center space-x-4 p-4 border border-gray-100 rounded-lg"
              >
                <img 
                  :src="item.image" 
                  :alt="item.title"
                  class="w-16 h-16 object-cover rounded"
                >
                <div class="flex-1">
                  <h3 class="font-medium text-gray-800">{{ item.title }}</h3>
                  <p class="text-sm text-gray-600">{{ item.author }}</p>
                  <p class="text-sm text-gray-500">Số lượng: {{ item.quantity }}</p>
                </div>
                <div class="text-right">
                  <p class="font-medium text-gray-800">{{ formatPrice(item.price) }}</p>
                  <p class="text-sm text-gray-500 line-through">{{ formatPrice(item.originalPrice) }}</p>
                </div>
              </div>
            </div>

            <!-- Order Summary -->
            <div class="mt-6 pt-6 border-t border-gray-200">
              <div class="space-y-2">
                <div class="flex justify-between text-sm">
                  <span class="text-gray-600">Tạm tính:</span>
                  <span class="text-gray-800">{{ formatPrice(order.subtotal) }}</span>
                </div>
                <div class="flex justify-between text-sm">
                  <span class="text-gray-600">Phí vận chuyển:</span>
                  <span class="text-gray-800">{{ formatPrice(order.shippingFee) }}</span>
                </div>
                <div class="flex justify-between text-sm">
                  <span class="text-gray-600">Giảm giá:</span>
                  <span class="text-red-600">-{{ formatPrice(order.discount) }}</span>
                </div>
                <div class="flex justify-between text-lg font-bold pt-2 border-t border-gray-200">
                  <span>Tổng cộng:</span>
                  <span class="text-blue-600">{{ formatPrice(order.total) }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- Order Timeline -->
          <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h2 class="text-lg font-bold text-gray-800 mb-4">Lịch sử đơn hàng</h2>
            <div class="space-y-4">
              <div 
                v-for="(timeline, index) in order.timeline" 
                :key="index"
                class="flex items-start space-x-4"
              >
                <div 
                  :class="[
                    'w-3 h-3 rounded-full mt-2',
                    index === 0 ? 'bg-blue-500' : 'bg-gray-300'
                  ]"
                ></div>
                <div class="flex-1">
                  <p class="font-medium text-gray-800">{{ timeline.title }}</p>
                  <p class="text-sm text-gray-600">{{ timeline.description }}</p>
                  <p class="text-xs text-gray-500 mt-1">{{ formatDateTime(timeline.timestamp) }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Sidebar -->
        <div class="space-y-6">
          <!-- Shipping Information -->
          <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h3 class="text-lg font-bold text-gray-800 mb-4">Thông tin giao hàng</h3>
            <div class="space-y-3">
              <div>
                <p class="text-sm font-medium text-gray-700">Người nhận:</p>
                <p class="text-gray-800">{{ order.shipping.name }}</p>
              </div>
              <div>
                <p class="text-sm font-medium text-gray-700">Số điện thoại:</p>
                <p class="text-gray-800">{{ order.shipping.phone }}</p>
              </div>
              <div>
                <p class="text-sm font-medium text-gray-700">Địa chỉ:</p>
                <p class="text-gray-800">{{ order.shipping.address }}</p>
              </div>
              <div>
                <p class="text-sm font-medium text-gray-700">Ghi chú:</p>
                <p class="text-gray-800">{{ order.shipping.note || 'Không có' }}</p>
              </div>
            </div>
          </div>

          <!-- Payment Information -->
          <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h3 class="text-lg font-bold text-gray-800 mb-4">Thông tin thanh toán</h3>
            <div class="space-y-3">
              <div>
                <p class="text-sm font-medium text-gray-700">Phương thức:</p>
                <p class="text-gray-800">{{ order.payment.method }}</p>
              </div>
              <div>
                <p class="text-sm font-medium text-gray-700">Trạng thái:</p>
                <span 
                  :class="[
                    'px-2 py-1 rounded-full text-xs font-medium',
                    order.payment.status === 'paid' ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'
                  ]"
                >
                  {{ order.payment.status === 'paid' ? 'Đã thanh toán' : 'Chưa thanh toán' }}
                </span>
              </div>
              <div v-if="order.payment.transactionId">
                <p class="text-sm font-medium text-gray-700">Mã giao dịch:</p>
                <p class="text-gray-800 font-mono text-sm">{{ order.payment.transactionId }}</p>
              </div>
            </div>
          </div>

          <!-- Actions -->
          <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h3 class="text-lg font-bold text-gray-800 mb-4">Thao tác</h3>
            <div class="space-y-3">
              <button 
                v-if="order.status === 'delivered'"
                @click="downloadInvoice"
                class="w-full bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors"
              >
                Tải hóa đơn
              </button>
              <button 
                v-if="order.status === 'delivered'"
                @click="writeReview"
                class="w-full bg-green-600 text-white py-2 px-4 rounded-lg hover:bg-green-700 transition-colors"
              >
                Viết đánh giá
              </button>
              <button 
                v-if="order.status === 'pending'"
                @click="cancelOrder"
                class="w-full bg-red-600 text-white py-2 px-4 rounded-lg hover:bg-red-700 transition-colors"
              >
                Hủy đơn hàng
              </button>
              <button 
                @click="contactSupport"
                class="w-full bg-gray-100 text-gray-700 py-2 px-4 rounded-lg hover:bg-gray-200 transition-colors"
              >
                Liên hệ hỗ trợ
              </button>
            </div>
          </div>

          <!-- Tracking Information -->
          <div v-if="order.tracking" class="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
            <h3 class="text-lg font-bold text-gray-800 mb-4">Theo dõi vận chuyển</h3>
            <div class="space-y-3">
              <div>
                <p class="text-sm font-medium text-gray-700">Đơn vị vận chuyển:</p>
                <p class="text-gray-800">{{ order.tracking.carrier }}</p>
              </div>
              <div>
                <p class="text-sm font-medium text-gray-700">Mã vận đơn:</p>
                <p class="text-gray-800 font-mono text-sm">{{ order.tracking.trackingNumber }}</p>
              </div>
              <div>
                <p class="text-sm font-medium text-gray-700">Trạng thái:</p>
                <p class="text-gray-800">{{ order.tracking.status }}</p>
              </div>
              <a 
                :href="order.tracking.url" 
                target="_blank"
                class="inline-block bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors text-sm"
              >
                Theo dõi chi tiết
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'

export default {
  name: 'OrderDetail',
  setup() {
    const route = useRoute()
    const orderId = route.params.id

    // Sample order data
    const order = ref({
      id: orderId,
      orderDate: '2024-01-15T10:30:00',
      status: 'delivered',
      subtotal: 250000,
      shippingFee: 30000,
      discount: 25000,
      total: 255000,
      items: [
        {
          id: 1,
          title: 'Đắc Nhân Tâm',
          author: 'Dale Carnegie',
          image: 'https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=400',
          price: 89000,
          originalPrice: 120000,
          quantity: 2
        },
        {
          id: 2,
          title: 'Sapiens: Lược sử loài người',
          author: 'Yuval Noah Harari',
          image: 'https://images.pexels.com/photos/1029141/pexels-photo-1029141.jpeg?auto=compress&cs=tinysrgb&w=400',
          price: 159000,
          originalPrice: 200000,
          quantity: 1
        }
      ],
      shipping: {
        name: 'Nguyễn Văn A',
        phone: '0123456789',
        address: '123 Đường ABC, Quận 1, TP. Hồ Chí Minh',
        note: 'Giao hàng giờ hành chính'
      },
      payment: {
        method: 'Thanh toán khi nhận hàng',
        status: 'paid',
        transactionId: 'TXN123456789'
      },
      tracking: {
        carrier: 'Giao hàng nhanh',
        trackingNumber: 'GHN123456789',
        status: 'Đã giao hàng',
        url: '#'
      },
      timeline: [
        {
          title: 'Đơn hàng đã được đặt',
          description: 'Đơn hàng của bạn đã được đặt thành công',
          timestamp: '2024-01-15T10:30:00'
        },
        {
          title: 'Đơn hàng đã được xác nhận',
          description: 'Chúng tôi đã xác nhận đơn hàng của bạn',
          timestamp: '2024-01-15T11:00:00'
        },
        {
          title: 'Đơn hàng đang được xử lý',
          description: 'Đơn hàng đang được chuẩn bị để giao',
          timestamp: '2024-01-15T14:00:00'
        },
        {
          title: 'Đơn hàng đã được gửi',
          description: 'Đơn hàng đã được gửi cho đơn vị vận chuyển',
          timestamp: '2024-01-16T09:00:00'
        },
        {
          title: 'Đơn hàng đã được giao',
          description: 'Đơn hàng đã được giao thành công',
          timestamp: '2024-01-17T15:30:00'
        }
      ]
    })

    // Order progress steps
    const orderSteps = computed(() => {
      const steps = [
        { id: 'ordered', name: 'Đã đặt hàng', description: 'Đơn hàng đã được đặt', completed: true, active: false },
        { id: 'confirmed', name: 'Đã xác nhận', description: 'Đơn hàng đã được xác nhận', completed: true, active: false },
        { id: 'processing', name: 'Đang xử lý', description: 'Đang chuẩn bị hàng', completed: true, active: false },
        { id: 'shipped', name: 'Đã gửi hàng', description: 'Đã giao cho vận chuyển', completed: true, active: false },
        { id: 'delivered', name: 'Đã giao hàng', description: 'Giao hàng thành công', completed: true, active: false }
      ]

      // Update based on current status
      const statusIndex = {
        'pending': 0,
        'confirmed': 1,
        'processing': 2,
        'shipped': 3,
        'delivered': 4
      }

      const currentIndex = statusIndex[order.value.status] || 0

      steps.forEach((step, index) => {
        step.completed = index <= currentIndex
        step.active = index === currentIndex
      })

      return steps
    })

    // Methods
    const formatDate = (dateString) => {
      return new Date(dateString).toLocaleDateString('vi-VN')
    }

    const formatDateTime = (dateString) => {
      return new Date(dateString).toLocaleString('vi-VN')
    }

    const formatPrice = (price) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(price)
    }

    const getStatusText = (status) => {
      const statusMap = {
        'pending': 'Chờ xử lý',
        'confirmed': 'Đã xác nhận',
        'processing': 'Đang xử lý',
        'shipped': 'Đã gửi hàng',
        'delivered': 'Đã giao hàng',
        'cancelled': 'Đã hủy'
      }
      return statusMap[status] || status
    }

    const getStatusClass = (status) => {
      const classMap = {
        'pending': 'bg-yellow-100 text-yellow-800',
        'confirmed': 'bg-blue-100 text-blue-800',
        'processing': 'bg-purple-100 text-purple-800',
        'shipped': 'bg-indigo-100 text-indigo-800',
        'delivered': 'bg-green-100 text-green-800',
        'cancelled': 'bg-red-100 text-red-800'
      }
      return classMap[status] || 'bg-gray-100 text-gray-800'
    }

    const downloadInvoice = () => {
      console.log('Downloading invoice for order:', orderId)
    }

    const writeReview = () => {
      console.log('Writing review for order:', orderId)
    }

    const cancelOrder = () => {
      console.log('Cancelling order:', orderId)
    }

    const contactSupport = () => {
      console.log('Contacting support for order:', orderId)
    }

    onMounted(() => {
      console.log('Loading order details for:', orderId)
    })

    return {
      orderId,
      order,
      orderSteps,
      formatDate,
      formatDateTime,
      formatPrice,
      getStatusText,
      getStatusClass,
      downloadInvoice,
      writeReview,
      cancelOrder,
      contactSupport
    }
  }
}
</script> 