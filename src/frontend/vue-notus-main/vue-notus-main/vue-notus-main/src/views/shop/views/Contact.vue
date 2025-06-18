<template>
  <div class="min-h-screen bg-gray-50 py-12">
    <div class="container mx-auto px-4">
      <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-8">
        <h1 class="text-2xl font-bold text-gray-800 mb-6">Liên hệ</h1>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
          <!-- Company Info -->
          <div>
            <h2 class="text-lg font-semibold text-gray-700 mb-2">Thông tin liên hệ</h2>
            <ul class="text-gray-600 space-y-2 mb-6">
              <li><strong>Địa chỉ:</strong> 123 Đường ABC, Quận 1, TP. Hồ Chí Minh</li>
              <li><strong>Điện thoại:</strong> 1900-1234</li>
              <li><strong>Email:</strong> support@bbook.com</li>
              <li><strong>Thời gian làm việc:</strong> 8:00 - 22:00 (T2 - CN)</li>
            </ul>
            <div class="w-full h-48 bg-gray-100 rounded-lg flex items-center justify-center text-gray-400">
              Bản đồ sẽ hiển thị ở đây
            </div>
          </div>
          <!-- FAQ Section -->
          <div>
            <h2 class="text-lg font-semibold text-gray-700 mb-4">Câu hỏi thường gặp</h2>
            <div class="space-y-4">
              <div v-for="(faq, idx) in faqs" :key="idx" class="border border-gray-200 rounded-lg">
                <button @click="toggleFaq(idx)" class="w-full px-6 py-4 text-left flex items-center justify-between hover:bg-gray-50 transition-colors">
                  <span class="font-medium text-gray-800">{{ faq.question }}</span>
                  <svg :class="['w-5 h-5 text-gray-500 transition-transform', openFaqs.includes(idx) ? 'rotate-180' : '']" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                  </svg>
                </button>
                <div v-show="openFaqs.includes(idx)" class="px-6 pb-4 text-gray-600">
                  {{ faq.answer }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
export default {
  name: 'Contact',
  setup() {
    const faqs = [
      {
        question: 'Làm thế nào để đặt hàng?',
        answer: 'Bạn có thể đặt hàng trực tuyến thông qua website của chúng tôi. Chọn sản phẩm, thêm vào giỏ hàng và làm theo các bước thanh toán.'
      },
      {
        question: 'Thời gian giao hàng là bao lâu?',
        answer: 'Thời gian giao hàng từ 1-3 ngày làm việc trong nội thành TP.HCM và 3-7 ngày cho các tỉnh thành khác.'
      },
      {
        question: 'Có thể đổi trả sản phẩm không?',
        answer: 'Chúng tôi chấp nhận đổi trả trong vòng 30 ngày kể từ ngày nhận hàng với điều kiện sản phẩm còn nguyên vẹn.'
      },
      {
        question: 'Có những phương thức thanh toán nào?',
        answer: 'Chúng tôi chấp nhận thanh toán bằng tiền mặt khi nhận hàng, chuyển khoản ngân hàng, và các ví điện tử phổ biến.'
      },
      {
        question: 'Làm sao để theo dõi đơn hàng?',
        answer: 'Sau khi đặt hàng, bạn sẽ nhận được email xác nhận với mã đơn hàng. Bạn có thể sử dụng mã này để theo dõi trạng thái đơn hàng.'
      }
    ]
    const openFaqs = ref([])
    const toggleFaq = (idx) => {
      const i = openFaqs.value.indexOf(idx)
      if (i > -1) openFaqs.value.splice(i, 1)
      else openFaqs.value.push(idx)
    }
    return { faqs, openFaqs, toggleFaq }
  }
}
</script> 