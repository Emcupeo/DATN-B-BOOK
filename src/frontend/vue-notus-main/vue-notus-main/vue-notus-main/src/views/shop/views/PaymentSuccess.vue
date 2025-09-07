<template>
  <div class="min-h-screen bg-gray-50 flex items-center justify-center px-4">
    <div class="bg-white rounded-2xl shadow-xl p-8 max-w-lg w-full text-center">
      <div class="mx-auto mb-6 w-16 h-16 rounded-full bg-green-100 flex items-center justify-center">
        <svg class="w-10 h-10 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
      </div>
      <h1 class="text-2xl font-bold text-gray-800 mb-2">Thanh toán thành công</h1>
      <p class="text-gray-600 mb-6">Đơn hàng <span class="font-semibold">{{ orderCode }}</span> đã được thanh toán qua VNPAY.</p>
      <div class="space-x-3">
        <router-link to="/" class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">Về trang chủ</router-link>
        <router-link :to="`/profile`" class="px-6 py-2 bg-gray-100 text-gray-800 rounded-lg hover:bg-gray-200">Xem đơn hàng</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PaymentSuccess',
  computed: {
    orderCode() {
      return this.$route.query.order || ''
    }
  }
}
</script>





