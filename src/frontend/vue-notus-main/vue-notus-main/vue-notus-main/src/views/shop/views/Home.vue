<template>
  <div>
    <!-- Hero Section -->
    <section class="relative bg-gradient-to-br from-blue-600 via-purple-600 to-indigo-700 text-white overflow-hidden">
      <!-- Background Pattern -->
      <div class="absolute inset-0 opacity-10">
        <div class="absolute inset-0 bg-pattern"></div>
      </div>
      
      <div class="container mx-auto px-4 py-20 relative z-10">
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
          <div>
            <h1 class="text-4xl md:text-6xl font-bold mb-6 leading-tight">
              Khám phá thế giới 
              <span class="text-yellow-300">tri thức</span>
            </h1>
            <p class="text-xl md:text-2xl mb-8 opacity-90 leading-relaxed">
              Hàng ngàn cuốn sách hay từ các tác giả nổi tiếng đang chờ bạn khám phá. 
              Nâng cao kiến thức, mở rộng tầm nhìn với BBook.
            </p>
            <div class="flex flex-col sm:flex-row gap-4">
              <button class="bg-white text-blue-600 px-8 py-4 rounded-full font-semibold hover:bg-gray-100 transition-all duration-200 shadow-lg hover:shadow-xl transform hover:-translate-y-1">
                🛒 Mua sắm ngay
              </button>
              <button class="border-2 border-white text-white px-8 py-4 rounded-full font-semibold hover:bg-white hover:text-blue-600 transition-all duration-200">
                📚 Xem bộ sưu tập
              </button>
            </div>
            
            <!-- Stats -->
            <div class="flex items-center space-x-8 mt-12">
              <div class="text-center">
                <div class="text-3xl font-bold">50K+</div>
                <div class="text-sm opacity-80">Khách hàng</div>
              </div>
              <div class="text-center">
                <div class="text-3xl font-bold">{{ products.length }}+</div>
                <div class="text-sm opacity-80">Sách hay</div>
              </div>
              <div class="text-center">
                <div class="text-3xl font-bold">99%</div>
                <div class="text-sm opacity-80">Hài lòng</div>
              </div>
            </div>
          </div>
          
          <div class="relative">
            <div class="relative z-10">
              <img src="https://images.unsplash.com/photo-1481627834876-b7833e8f5570?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80" 
                   alt="Books Collection" 
                   class="rounded-2xl shadow-2xl">
            </div>
            <!-- Floating Elements -->
            <div class="absolute -top-4 -right-4 bg-white rounded-full p-4 shadow-lg">
              <div class="text-2xl">📚</div>
            </div>
            <div class="absolute -bottom-4 -left-4 bg-yellow-400 rounded-full p-4 shadow-lg">
              <div class="text-2xl">⭐</div>
            </div>
          </div>
        </div>
      </div>
    </section>


    <!-- Sách mới nhất Section -->
    <section class="py-16">
      <div class="container mx-auto px-4">
        <div class="flex justify-between items-center mb-12">
          <div>
            <h2 class="text-3xl md:text-4xl font-bold text-gray-800 mb-2">Sách mới nhất</h2>
            <p class="text-gray-600">Những cuốn sách vừa được thêm vào thư viện</p>
          </div>
          <router-link 
            to="/products?sort=newest"
            class="bg-gradient-to-r from-blue-600 to-purple-600 text-white px-6 py-3 rounded-full font-semibold hover:from-blue-700 hover:to-purple-700 transition-all duration-200 shadow-lg hover:shadow-xl"
          >
            Xem thêm
          </router-link>
        </div>
        
        <!-- Loading State -->
        <div v-if="loadingNewBooks" class="text-center py-12">
          <div class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
          <p class="mt-4 text-gray-600">Đang tải dữ liệu...</p>
        </div>

        <!-- Error State -->
        <div v-else-if="errorNewBooks" class="text-center py-12">
          <div class="text-red-600 text-lg mb-4">⚠️</div>
          <p class="text-gray-600">{{ errorNewBooks }}</p>
          <button @click="loadNewBooks" class="mt-4 bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700">
            Thử lại
          </button>
        </div>

        <!-- New Books Grid -->
        <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-6">
          <BookCard
            v-for="book in newBooks"
            :key="book.id"
            :book="book"
          />
        </div>
      </div>
    </section>

    <!-- Sản phẩm bán chạy nhất Section -->
    <section class="py-16 bg-gray-50">
      <div class="container mx-auto px-4">
        <div class="flex justify-between items-center mb-12">
          <div>
            <h2 class="text-3xl md:text-4xl font-bold text-gray-800 mb-2">Sản phẩm bán chạy nhất</h2>
            <p class="text-gray-600">Những sản phẩm được yêu thích nhất bao gồm sách lẻ và bộ sách</p>
          </div>
          <router-link 
            to="/products?sort=bestselling"
            class="bg-gradient-to-r from-green-600 to-teal-600 text-white px-6 py-3 rounded-full font-semibold hover:from-green-700 hover:to-teal-700 transition-all duration-200 shadow-lg hover:shadow-xl"
          >
            Xem thêm
          </router-link>
        </div>
        
        <!-- Loading State -->
        <div v-if="loadingBestsellers" class="text-center py-12">
          <div class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-green-600"></div>
          <p class="mt-4 text-gray-600">Đang tải dữ liệu...</p>
        </div>

        <!-- Error State -->
        <div v-else-if="errorBestsellers" class="text-center py-12">
          <div class="text-red-600 text-lg mb-4">⚠️</div>
          <p class="text-gray-600">{{ errorBestsellers }}</p>
          <button @click="loadBestsellers" class="mt-4 bg-green-600 text-white px-6 py-2 rounded-lg hover:bg-green-700">
            Thử lại
          </button>
        </div>

        <!-- Bestsellers Grid -->
        <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-6">
          <BookCard
            v-for="product in bestsellers"
            :key="product.id"
            :book="product"
          />
        </div>
      </div>
    </section>

    <!-- Bộ sách mới nhất Section -->
    <section class="py-16">
      <div class="container mx-auto px-4">
        <div class="flex justify-between items-center mb-12">
          <div>
            <h2 class="text-3xl md:text-4xl font-bold text-gray-800 mb-2">Bộ sách mới nhất</h2>
            <p class="text-gray-600">Những bộ sách vừa được thêm vào thư viện</p>
          </div>
          <router-link 
            to="/products?type=bookset&sort=newest"
            class="bg-gradient-to-r from-purple-600 to-pink-600 text-white px-6 py-3 rounded-full font-semibold hover:from-purple-700 hover:to-pink-700 transition-all duration-200 shadow-lg hover:shadow-xl"
          >
            Xem thêm
          </router-link>
        </div>
        
        <!-- Loading State -->
        <div v-if="loadingNewBookSets" class="text-center py-12">
          <div class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-purple-600"></div>
          <p class="mt-4 text-gray-600">Đang tải dữ liệu...</p>
        </div>

        <!-- Error State -->
        <div v-else-if="errorNewBookSets" class="text-center py-12">
          <div class="text-red-600 text-lg mb-4">⚠️</div>
          <p class="text-gray-600">{{ errorNewBookSets }}</p>
          <button @click="loadNewBookSets" class="mt-4 bg-purple-600 text-white px-6 py-2 rounded-lg hover:bg-purple-700">
            Thử lại
          </button>
        </div>

        <!-- New Book Sets Grid -->
        <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-6">
          <BookCard
            v-for="bookSet in newBookSets"
            :key="bookSet.id"
            :book="bookSet"
          />
        </div>
      </div>
    </section>

    <!-- Features Section -->
    <section class="py-16 bg-gradient-to-br from-gray-50 to-blue-50">
      <div class="container mx-auto px-4">
        <div class="text-center mb-12">
          <h2 class="text-3xl md:text-4xl font-bold text-gray-800 mb-4">Tại sao chọn BBook?</h2>
          <p class="text-lg text-gray-600 max-w-2xl mx-auto">
            Chúng tôi cam kết mang đến trải nghiệm mua sắm sách tốt nhất cho bạn
          </p>
        </div>
        
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
          <div class="text-center group">
            <div class="w-20 h-20 bg-gradient-to-br from-blue-500 to-purple-600 rounded-2xl flex items-center justify-center mx-auto mb-6 group-hover:scale-110 transition-transform duration-300 shadow-lg">
              <svg class="w-10 h-10 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold text-gray-800 mb-3">Giá cả hợp lý</h3>
            <p class="text-gray-600 leading-relaxed">Cam kết giá tốt nhất thị trường với nhiều ưu đãi và khuyến mãi hấp dẫn</p>
          </div>
          
          <div class="text-center group">
            <div class="w-20 h-20 bg-gradient-to-br from-green-500 to-teal-600 rounded-2xl flex items-center justify-center mx-auto mb-6 group-hover:scale-110 transition-transform duration-300 shadow-lg">
              <svg class="w-10 h-10 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold text-gray-800 mb-3">Giao hàng nhanh</h3>
            <p class="text-gray-600 leading-relaxed">Giao hàng toàn quốc trong 1-3 ngày làm việc với dịch vụ đóng gói cẩn thận</p>
          </div>
          
          <div class="text-center group">
            <div class="w-20 h-20 bg-gradient-to-br from-purple-500 to-pink-600 rounded-2xl flex items-center justify-center mx-auto mb-6 group-hover:scale-110 transition-transform duration-300 shadow-lg">
              <svg class="w-10 h-10 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold text-gray-800 mb-3">Chất lượng đảm bảo</h3>
            <p class="text-gray-600 leading-relaxed">Sách chính hãng, chất lượng cao từ các nhà xuất bản uy tín hàng đầu</p>
          </div>
          
          <div class="text-center group">
            <div class="w-20 h-20 bg-gradient-to-br from-orange-500 to-red-600 rounded-2xl flex items-center justify-center mx-auto mb-6 group-hover:scale-110 transition-transform duration-300 shadow-lg">
              <svg class="w-10 h-10 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18.364 5.636l-3.536 3.536m0 5.656l3.536 3.536M9.172 9.172L5.636 5.636m3.536 9.192L5.636 18.364M12 2.25a9.75 9.75 0 109.75 9.75A9.75 9.75 0 0012 2.25z"></path>
              </svg>
            </div>
            <h3 class="text-xl font-bold text-gray-800 mb-3">Hỗ trợ 24/7</h3>
            <p class="text-gray-600 leading-relaxed">Đội ngũ hỗ trợ chuyên nghiệp sẵn sàng phục vụ bạn mọi lúc, mọi nơi</p>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRealDataStore } from '../store/realDataStore'
import BookCard from '../components/BookCard.vue'
import ThongKeService from '../../../service/ThongKeService.js'

export default {
  name: 'Home',
  components: {
    BookCard
  },
  setup() {
    const store = useRealDataStore()

    const products = computed(() => store.products.value)
    const loading = computed(() => store.loading.value)
    const error = computed(() => store.error.value)

    // Data cho các section mới
    const newBooks = ref([])
    const bestsellers = ref([])
    const newBookSets = ref([])
    
    // Loading states cho các section mới
    const loadingNewBooks = ref(false)
    const loadingBestsellers = ref(false)
    const loadingNewBookSets = ref(false)
    
    // Error states cho các section mới
    const errorNewBooks = ref(null)
    const errorBestsellers = ref(null)
    const errorNewBookSets = ref(null)


    // Load sách mới nhất
    const loadNewBooks = async () => {
      loadingNewBooks.value = true
      errorNewBooks.value = null
      try {
        const data = await ThongKeService.getSachMoiNhat(5)
        newBooks.value = data
      } catch (error) {
        console.error('Error loading new books:', error)
        errorNewBooks.value = 'Không thể tải sách mới nhất'
      } finally {
        loadingNewBooks.value = false
      }
    }

    // Load sản phẩm bán chạy (cả sách lẻ và bộ sách)
    const loadBestsellers = async () => {
      loadingBestsellers.value = true
      errorBestsellers.value = null
      try {
        // Load cả sách lẻ và bộ sách bán chạy
        const [sachBanChay, boSachBanChay] = await Promise.all([
          ThongKeService.getSachBanChay(3), // 3 sách lẻ
          ThongKeService.getBoSachBanChay(2) // 2 bộ sách
        ])
        
        // Gộp lại và sắp xếp theo thứ tự
        const allBestsellers = [...sachBanChay, ...boSachBanChay]
        bestsellers.value = allBestsellers
      } catch (error) {
        console.error('Error loading bestsellers:', error)
        errorBestsellers.value = 'Không thể tải sản phẩm bán chạy'
      } finally {
        loadingBestsellers.value = false
      }
    }

    // Load bộ sách mới nhất
    const loadNewBookSets = async () => {
      loadingNewBookSets.value = true
      errorNewBookSets.value = null
      try {
        const data = await ThongKeService.getBoSachMoiNhat(5)
        newBookSets.value = data
      } catch (error) {
        console.error('Error loading new book sets:', error)
        errorNewBookSets.value = 'Không thể tải bộ sách mới nhất'
      } finally {
        loadingNewBookSets.value = false
      }
    }


    const loadData = async () => {
      await store.loadProducts()
    }

    onMounted(async () => {
      await loadData()
      
      // Load các section mới
      await Promise.all([
        loadNewBooks(),
        loadBestsellers(),
        loadNewBookSets()
      ])
    })

    return {
      products,
      loading,
      error,
      newBooks,
      bestsellers,
      newBookSets,
      loadingNewBooks,
      loadingBestsellers,
      loadingNewBookSets,
      errorNewBooks,
      errorBestsellers,
      errorNewBookSets,
      loadData,
      loadNewBooks,
      loadBestsellers,
      loadNewBookSets
    }
  }
}
</script>

<style scoped>
.bg-pattern {
  background-image: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.1'%3E%3Ccircle cx='30' cy='30' r='2'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
}
</style>