<template>
  <footer class="bg-gray-900 border-t border-gray-800 mt-16 text-white">
    <div class="container mx-auto px-4 py-12">
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
        <!-- Company Info -->
        <div class="lg:col-span-1">
          <div class="flex items-center space-x-3 mb-6">
            <img src="@/assets/img/bbook-logo.png" alt="BBook Logo" class="w-12 h-12 object-contain rounded-xl" />
            <div>
              <span class="text-2xl font-bold">BBook</span>
              <p class="text-xs text-gray-400 -mt-1">Khám phá tri thức</p>
            </div>
          </div>
          <p class="text-gray-400 mb-6 leading-relaxed">
            BBook - Nơi kết nối tri thức, mang đến những cuốn sách hay nhất từ các tác giả nổi tiếng. 
            Chúng tôi cam kết mang đến trải nghiệm đọc sách tuyệt vời nhất.
          </p>
          
          <!-- Social Links -->
          <div class="flex space-x-4">
            <a href="#" class="w-10 h-10 bg-blue-600 rounded-full flex items-center justify-center hover:bg-blue-700 transition-colors">
              <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                <path d="M24 4.557c-.883.392-1.832.656-2.828.775 1.017-.609 1.798-1.574 2.165-2.724-.951.564-2.005.974-3.127 1.195-.897-.957-2.178-1.555-3.594-1.555-3.179 0-5.515 2.966-4.797 6.045-4.091-.205-7.719-2.165-10.148-5.144-1.29 2.213-.669 5.108 1.523 6.574-.806-.026-1.566-.247-2.229-.616-.054 2.281 1.581 4.415 3.949 4.89-.693.188-1.452.232-2.224.084.626 1.956 2.444 3.379 4.6 3.419-2.07 1.623-4.678 2.348-7.29 2.04 2.179 1.397 4.768 2.212 7.548 2.212 9.142 0 14.307-7.721 13.995-14.646.962-.695 1.797-1.562 2.457-2.549z"/>
              </svg>
            </a>
            <a href="#" class="w-10 h-10 bg-blue-800 rounded-full flex items-center justify-center hover:bg-blue-900 transition-colors">
              <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/>
              </svg>
            </a>
            <a href="#" class="w-10 h-10 bg-pink-600 rounded-full flex items-center justify-center hover:bg-pink-700 transition-colors">
              <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                <path d="M12 0C5.373 0 0 5.373 0 12s5.373 12 12 12 12-5.373 12-12S18.627 0 12 0zm0 19c-3.859 0-7-3.141-7-7s3.141-7 7-7 7 3.141 7 7-3.141 7-7 7z"/>
              </svg>
            </a>
            <a href="#" class="w-10 h-10 bg-gray-700 rounded-full flex items-center justify-center hover:bg-gray-600 transition-colors">
              <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"/>
              </svg>
            </a>
          </div>
        </div>

        <!-- Quick Links -->
        <div>
          <h3 class="text-lg font-bold mb-6">Liên kết nhanh</h3>
          <ul class="space-y-3">
            <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Trang chủ</a></li>
            <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Sách mới</a></li>
            <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Bán chạy</a></li>
            <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Khuyến mãi</a></li>
            <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Tin tức</a></li>
            <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Liên hệ</a></li>
          </ul>
        </div>

        <!-- Categories -->
        <div>
          <h3 class="text-lg font-bold mb-6">Danh mục sách</h3>
          <ul class="space-y-3">
            <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Sách Văn học</a></li>
            <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Sách Kinh tế</a></li>
            <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Sách Khoa học</a></li>
            <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Sách Thiếu nhi</a></li>
            <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Sách Ngoại ngữ</a></li>
            <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Sách Kỹ năng</a></li>
          </ul>
        </div>

        <!-- Contact Info -->
        <div>
          <h3 class="text-lg font-bold mb-6">Thông tin liên hệ</h3>
          <div class="space-y-4">
            <div class="flex items-start space-x-3">
              <svg class="w-5 h-5 text-blue-400 mt-1 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"></path>
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"></path>
              </svg>
              <div>
                <p class="text-gray-400">123 Đường ABC, Quận 1</p>
                <p class="text-gray-400">TP. Hồ Chí Minh, Việt Nam</p>
              </div>
            </div>
            
            <div class="flex items-center space-x-3">
              <svg class="w-5 h-5 text-blue-400 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"></path>
              </svg>
              <span class="text-gray-400">1900-1234</span>
            </div>
            
            <div class="flex items-center space-x-3">
              <svg class="w-5 h-5 text-blue-400 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 4.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"></path>
              </svg>
              <span class="text-gray-400">support@bbook.com</span>
            </div>
            
            <div class="flex items-center space-x-3">
              <svg class="w-5 h-5 text-blue-400 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
              <span class="text-gray-400">8:00 - 22:00 (T2 - CN)</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Bottom Footer -->
    <div class="border-t border-gray-800">
      <div class="container mx-auto px-4 py-8">
        <div class="flex flex-col md:flex-row justify-between items-center">
          <div class="text-gray-400 text-sm mb-4 md:mb-0">
            © {{ currentYear }} BBook. Tất cả quyền được bảo lưu.
          </div>
          
          <div class="flex items-center space-x-6 text-sm">
            <a href="#" class="text-gray-400 hover:text-white transition-colors">Chính sách bảo mật</a>
            <a href="#" class="text-gray-400 hover:text-white transition-colors">Điều khoản sử dụng</a>
            <a href="#" class="text-gray-400 hover:text-white transition-colors">Chính sách đổi trả</a>
            <a href="#" class="text-gray-400 hover:text-white transition-colors">Hướng dẫn mua hàng</a>
          </div>
        </div>
      </div>
    </div>

    <!-- Payment Methods -->
    <div class="border-t border-gray-800 bg-gray-950">
      <div class="container mx-auto px-4 py-4">
        <div class="flex flex-col md:flex-row justify-between items-center">
          <div class="text-gray-400 text-sm mb-4 md:mb-0">
            Phương thức thanh toán:
          </div>
          <div class="flex items-center space-x-4">
            <div class="w-12 h-8 bg-white rounded flex items-center justify-center">
              <span class="text-xs font-bold text-gray-800">VISA</span>
            </div>
            <div class="w-12 h-8 bg-white rounded flex items-center justify-center">
              <span class="text-xs font-bold text-gray-800">MC</span>
            </div>
            <div class="w-12 h-8 bg-white rounded flex items-center justify-center">
              <span class="text-xs font-bold text-gray-800">JCB</span>
            </div>
            <div class="w-12 h-8 bg-white rounded flex items-center justify-center">
              <span class="text-xs font-bold text-gray-800">COD</span>
            </div>
            <div class="w-12 h-8 bg-white rounded flex items-center justify-center">
              <span class="text-xs font-bold text-gray-800">ZALO</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'Footer',
  computed: {
    currentYear() {
      return new Date().getFullYear()
    }
  }
}
</script>