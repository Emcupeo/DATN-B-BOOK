<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Breadcrumb -->
    <div class="bg-white border-b border-gray-200">
      <div class="container mx-auto px-4 py-4">
        <nav class="flex" aria-label="Breadcrumb">
          <ol class="inline-flex items-center space-x-1 md:space-x-3">
            <li class="inline-flex items-center">
              <router-link to="/" class="inline-flex items-center text-sm font-medium text-gray-700 hover:text-blue-600">
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
                <span class="ml-1 text-sm font-medium text-gray-500 md:ml-2">Sản phẩm</span>
              </div>
            </li>
          </ol>
        </nav>
      </div>
    </div>

    <!-- Categories Bar -->
    <div class="bg-white border-b border-gray-200 sticky top-0 z-40">
      <div class="container mx-auto px-4 py-4">
        <div class="flex items-center justify-between mb-4">
          <h2 class="text-lg font-bold text-gray-800">Danh mục sản phẩm</h2>
          <div class="flex items-center space-x-2">
            <span class="text-sm text-gray-500">{{ filteredBooks.length }} sản phẩm</span>
            <button 
              @click="clearAllFilters"
              class="text-sm text-blue-600 hover:text-blue-700 font-medium"
            >
              Xóa bộ lọc
            </button>
          </div>
        </div>
        
        <!-- Categories Grid -->
        <div class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-3">
          <button
            v-for="category in categoryData"
            :key="category.name"
            @click="toggleCategory(category.name)"
            :class="[
              'flex flex-col items-center p-4 rounded-xl border-2 transition-all duration-200 hover:shadow-md',
              isCategorySelected(category.name)
                ? 'border-blue-500 bg-blue-50 text-blue-700'
                : 'border-gray-200 bg-white text-gray-700 hover:border-gray-300'
            ]"
          >
            <div class="w-10 h-10 bg-gradient-to-r from-blue-100 to-blue-200 rounded-lg flex items-center justify-center mb-2">
              <span class="text-lg">{{ category.icon }}</span>
            </div>
            <span class="text-xs font-medium text-center">{{ category.name }}</span>
            <span class="text-xs text-gray-500 mt-1">{{ category.count }}</span>
          </button>
        </div>

        <!-- Active Filters -->
        <div v-if="hasActiveFilters" class="mt-4 pt-4 border-t border-gray-100">
          <div class="flex items-center flex-wrap gap-2">
            <span class="text-sm text-gray-600">Bộ lọc đang áp dụng:</span>
            <span 
              v-for="category in filters.categories" 
              :key="category"
              class="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-blue-100 text-blue-800"
            >
              {{ category }}
              <button 
                @click="removeCategory(category)"
                class="ml-1 hover:text-blue-600"
              >
                ×
              </button>
            </span>
            <span 
              v-for="author in filters.authors" 
              :key="author"
              class="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800"
            >
              {{ author }}
              <button 
                @click="removeAuthor(author)"
                class="ml-1 hover:text-green-600"
              >
                ×
              </button>
            </span>
            <span 
              v-for="publisher in filters.publishers" 
              :key="publisher"
              class="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-purple-100 text-purple-800"
            >
              {{ publisher }}
              <button 
                @click="removePublisher(publisher)"
                class="ml-1 hover:text-purple-600"
              >
                ×
              </button>
            </span>
          </div>
        </div>
      </div>
    </div>

    <div class="container mx-auto px-4 py-8">
      <div class="flex flex-col lg:flex-row gap-8">
        <!-- Sidebar Filters -->
        <div class="lg:w-1/4">
          <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6 sticky top-24">
            <div class="flex items-center justify-between mb-6">
              <h2 class="text-lg font-bold text-gray-800">Bộ lọc chi tiết</h2>
              <button 
                @click="clearAllFilters"
                class="text-sm text-blue-600 hover:text-blue-700 font-medium"
              >
                Xóa tất cả
              </button>
            </div>

            <!-- Mobile Filter Toggle -->
            <div class="lg:hidden mb-4">
              <button 
                @click="showMobileFilters = !showMobileFilters"
                class="w-full bg-gray-100 text-gray-700 px-4 py-2 rounded-lg flex items-center justify-between"
              >
                <span>Hiển thị bộ lọc</span>
                <svg 
                  :class="['w-5 h-5 transition-transform', showMobileFilters ? 'rotate-180' : '']"
                  fill="none" 
                  stroke="currentColor" 
                  viewBox="0 0 24 24"
                >
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                </svg>
              </button>
            </div>

            <!-- Filters Content -->
            <div :class="['lg:block', showMobileFilters ? 'block' : 'hidden']">
              <!-- Search -->
              <div class="mb-6">
                <label class="block text-sm font-medium text-gray-700 mb-2">Tìm kiếm</label>
                <div class="relative">
                  <input 
                    v-model="filters.search"
                    type="text" 
                    placeholder="Tên sách, tác giả..."
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  >
                  <svg class="absolute right-3 top-2.5 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
                  </svg>
                </div>
              </div>

              <!-- Categories -->
              <div class="mb-6">
                <h3 class="text-sm font-semibold text-gray-800 mb-3">Danh mục</h3>
                <div class="space-y-2">
                  <label 
                    v-for="category in categories" 
                    :key="category"
                    class="flex items-center cursor-pointer"
                  >
                    <input 
                      type="checkbox" 
                      :value="category"
                      v-model="filters.categories"
                      class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500"
                    >
                    <span class="ml-2 text-sm text-gray-700">{{ category }}</span>
                    <span class="ml-auto text-xs text-gray-500">({{ getCategoryCount(category) }})</span>
                  </label>
                </div>
              </div>

              <!-- Price Range -->
              <div class="mb-6">
                <h3 class="text-sm font-semibold text-gray-800 mb-3">Khoảng giá</h3>
                <div class="space-y-3">
                  <div class="flex items-center justify-between">
                    <span class="text-sm text-gray-600">{{ formatPrice(filters.priceRange[0]) }}</span>
                    <span class="text-sm text-gray-600">{{ formatPrice(filters.priceRange[1]) }}</span>
                  </div>
                  <div class="relative">
                    <input 
                      v-model.number="filters.priceRange[0]"
                      type="range" 
                      :min="priceRange.min" 
                      :max="priceRange.max"
                      class="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer slider"
                    >
                    <input 
                      v-model.number="filters.priceRange[1]"
                      type="range" 
                      :min="priceRange.min" 
                      :max="priceRange.max"
                      class="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer slider"
                    >
                  </div>
                  <div class="flex space-x-2">
                    <input 
                      v-model.number="filters.priceRange[0]"
                      type="number" 
                      :min="priceRange.min" 
                      :max="priceRange.max"
                      class="w-1/2 px-2 py-1 text-sm border border-gray-300 rounded"
                      placeholder="Từ"
                    >
                    <input 
                      v-model.number="filters.priceRange[1]"
                      type="number" 
                      :min="priceRange.min" 
                      :max="priceRange.max"
                      class="w-1/2 px-2 py-1 text-sm border border-gray-300 rounded"
                      placeholder="Đến"
                    >
                  </div>
                </div>
              </div>

              <!-- Authors -->
              <div class="mb-6">
                <h3 class="text-sm font-semibold text-gray-800 mb-3">Tác giả</h3>
                <div class="space-y-2 max-h-32 overflow-y-auto">
                  <label 
                    v-for="author in authors" 
                    :key="author"
                    class="flex items-center cursor-pointer"
                  >
                    <input 
                      type="checkbox" 
                      :value="author"
                      v-model="filters.authors"
                      class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500"
                    >
                    <span class="ml-2 text-sm text-gray-700">{{ author }}</span>
                  </label>
                </div>
              </div>

              <!-- Publishers -->
              <div class="mb-6">
                <h3 class="text-sm font-semibold text-gray-800 mb-3">Nhà xuất bản</h3>
                <div class="space-y-2 max-h-32 overflow-y-auto">
                  <label 
                    v-for="publisher in publishers" 
                    :key="publisher"
                    class="flex items-center cursor-pointer"
                  >
                    <input 
                      type="checkbox" 
                      :value="publisher"
                      v-model="filters.publishers"
                      class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500"
                    >
                    <span class="ml-2 text-sm text-gray-700">{{ publisher }}</span>
                  </label>
                </div>
              </div>

              <!-- Rating -->
              <div class="mb-6">
                <h3 class="text-sm font-semibold text-gray-800 mb-3">Đánh giá</h3>
                <div class="space-y-2">
                  <label 
                    v-for="rating in [5, 4, 3, 2, 1]" 
                    :key="rating"
                    class="flex items-center cursor-pointer"
                  >
                    <input 
                      type="checkbox" 
                      :value="rating"
                      v-model="filters.ratings"
                      class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500"
                    >
                    <div class="flex items-center ml-2">
                      <div class="flex text-yellow-400">
                        <span v-for="star in 5" :key="star" class="text-sm" v-html="star <= rating ? '&#9733;' : '&#9734;'"></span>
                      </div>
                      <span class="ml-1 text-sm text-gray-700">& trở lên</span>
                    </div>
                  </label>
                </div>
              </div>

              <!-- Availability -->
              <div class="mb-6">
                <h3 class="text-sm font-semibold text-gray-800 mb-3">Tình trạng</h3>
                <div class="space-y-2">
                  <label class="flex items-center cursor-pointer">
                    <input 
                      type="checkbox" 
                      v-model="filters.inStock"
                      class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500"
                    >
                    <span class="ml-2 text-sm text-gray-700">Còn hàng</span>
                  </label>
                  <label class="flex items-center cursor-pointer">
                    <input 
                      type="checkbox" 
                      v-model="filters.onSale"
                      class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500"
                    >
                    <span class="ml-2 text-sm text-gray-700">Đang giảm giá</span>
                  </label>
                </div>
              </div>

              <!-- Language -->
              <div class="mb-6">
                <h3 class="text-sm font-semibold text-gray-800 mb-3">Ngôn ngữ</h3>
                <div class="space-y-2">
                  <label 
                    v-for="language in languages" 
                    :key="language"
                    class="flex items-center cursor-pointer"
                  >
                    <input 
                      type="checkbox" 
                      :value="language"
                      v-model="filters.languages"
                      class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500"
                    >
                    <span class="ml-2 text-sm text-gray-700">{{ language }}</span>
                  </label>
                </div>
              </div>

              <!-- Format -->
              <div class="mb-6">
                <h3 class="text-sm font-semibold text-gray-800 mb-3">Định dạng</h3>
                <div class="space-y-2">
                  <label 
                    v-for="format in formats" 
                    :key="format"
                    class="flex items-center cursor-pointer"
                  >
                    <input 
                      type="checkbox" 
                      :value="format"
                      v-model="filters.formats"
                      class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500"
                    >
                    <span class="ml-2 text-sm text-gray-700">{{ format }}</span>
                  </label>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Main Content -->
        <div class="lg:w-3/4">
          <!-- Header -->
          <div class="bg-white rounded-xl shadow-sm border border-gray-200 p-6 mb-6">
            <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
              <div>
                <h1 class="text-2xl font-bold text-gray-800 mb-2">Tất cả sản phẩm</h1>
                <p class="text-gray-600">{{ filteredBooks.length }} sản phẩm được tìm thấy</p>
              </div>
              
              <div class="flex items-center space-x-4">
                <!-- Sort -->
                <div class="flex items-center space-x-2">
                  <label class="text-sm font-medium text-gray-700">Sắp xếp:</label>
                  <select 
                    v-model="sortBy"
                    class="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent text-sm"
                  >
                    <option value="relevance">Liên quan nhất</option>
                    <option value="price-low">Giá tăng dần</option>
                    <option value="price-high">Giá giảm dần</option>
                    <option value="rating">Đánh giá cao nhất</option>
                    <option value="newest">Mới nhất</option>
                    <option value="popular">Phổ biến nhất</option>
                  </select>
                </div>

                <!-- View Toggle -->
                <div class="flex border border-gray-300 rounded-lg">
                  <button 
                    @click="viewMode = 'grid'"
                    :class="[
                      'p-2 transition-colors',
                      viewMode === 'grid' ? 'bg-blue-600 text-white' : 'bg-white text-gray-600 hover:bg-gray-50'
                    ]"
                  >
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z"></path>
                    </svg>
                  </button>
                  <button 
                    @click="viewMode = 'list'"
                    :class="[
                      'p-2 transition-colors',
                      viewMode === 'list' ? 'bg-blue-600 text-white' : 'bg-white text-gray-600 hover:bg-gray-50'
                    ]"
                  >
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>
                    </svg>
                  </button>
                </div>
              </div>
            </div>

            <!-- Active Filters -->
            <div v-if="hasActiveFilters" class="mt-4 pt-4 border-t border-gray-200">
              <div class="flex items-center flex-wrap gap-2">
                <span class="text-sm font-medium text-gray-700">Bộ lọc đang áp dụng:</span>
                <span 
                  v-for="filter in activeFilters" 
                  :key="filter.key"
                  class="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-blue-100 text-blue-800"
                >
                  {{ filter.label }}
                  <button 
                    @click="removeFilter(filter.key)"
                    class="ml-1 hover:text-blue-600"
                  >
                    <svg class="w-3 h-3" fill="currentColor" viewBox="0 0 20 20">
                      <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd"></path>
                    </svg>
                  </button>
                </span>
              </div>
            </div>
          </div>

          <!-- Products Grid/List -->
          <div v-if="filteredBooks.length > 0">
            <div 
              :class="[
                viewMode === 'grid' 
                  ? 'grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6'
                  : 'space-y-4'
              ]"
            >
              <BookCard
                v-for="book in paginatedBooks"
                :key="book.id"
                :book="book"
                :view-mode="viewMode"
              />
            </div>

            <!-- Pagination -->
            <div class="mt-8 flex justify-center">
              <nav class="flex items-center space-x-2">
                <button 
                  @click="currentPage--"
                  :disabled="currentPage === 1"
                  class="px-3 py-2 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Trước
                </button>
                
                <button 
                  v-for="page in visiblePages" 
                  :key="page"
                  @click="currentPage = page"
                  :class="[
                    'px-3 py-2 text-sm font-medium rounded-lg',
                    currentPage === page 
                      ? 'bg-blue-600 text-white' 
                      : 'text-gray-500 bg-white border border-gray-300 hover:bg-gray-50'
                  ]"
                >
                  {{ page }}
                </button>
                
                <button 
                  @click="currentPage++"
                  :disabled="currentPage === totalPages"
                  class="px-3 py-2 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Sau
                </button>
              </nav>
            </div>
          </div>

          <!-- No Results -->
          <div v-else class="text-center py-12">
            <div class="w-24 h-24 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg class="w-12 h-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.172 16.172a4 4 0 015.656 0M9 12h6m-6-4h6m2 5.291A7.962 7.962 0 0112 15c-2.34 0-4.47-.881-6.08-2.33"></path>
              </svg>
            </div>
            <h3 class="text-lg font-medium text-gray-900 mb-2">Không tìm thấy sản phẩm</h3>
            <p class="text-gray-500 mb-4">Thử điều chỉnh bộ lọc hoặc tìm kiếm với từ khóa khác</p>
            <button 
              @click="clearAllFilters"
              class="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition-colors"
            >
              Xóa tất cả bộ lọc
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, computed, watch } from 'vue'
import { useRealDataStore } from '../store/realDataStore'
import BookCard from '../components/BookCard.vue'

export default {
  name: 'Products',
  components: {
    BookCard
  },
  setup() {
    const store = useRealDataStore()
    
    // Reactive data
    const viewMode = ref('grid')
    const sortBy = ref('relevance')
    const currentPage = ref(1)
    const itemsPerPage = 12
    const showMobileFilters = ref(false)

    // Filters
    const filters = reactive({
      search: '',
      categories: [],
      priceRange: [0, 1000000],
      authors: [],
      publishers: [],
      ratings: [],
      inStock: false,
      onSale: false,
      languages: [],
      formats: []
    })

    // Sample data for filters
    const authors = ['Dale Carnegie', 'Yuval Noah Harari', 'James Clear', 'Adam Khoo', 'Paulo Coelho', 'Napoleon Hill', 'David J. Lieberman', 'Robert T. Kiyosaki', 'Stephen R. Covey', 'Jo Gang-soo', 'Charles Duhigg']
    const publishers = ['NXB Trẻ', 'NXB Kim Đồng', 'NXB Văn Học', 'NXB Tổng Hợp', 'NXB Giáo Dục']
    const languages = ['Tiếng Việt', 'Tiếng Anh', 'Tiếng Pháp', 'Tiếng Đức']
    const formats = ['Bìa mềm', 'Bìa cứng', 'E-book', 'Audiobook']

    // Computed properties
    const books = computed(() => store.products.value)
    const categories = computed(() => store.categories.value)

    const priceRange = computed(() => {
      const prices = books.value.map(book => book.price)
      return {
        min: Math.min(...prices),
        max: Math.max(...prices)
      }
    })

    // Initialize price range when component mounts
    const initializePriceRange = () => {
      const range = priceRange.value
      filters.priceRange = [range.min, range.max]
    }

    // Watch for books to be loaded and initialize price range
    watch(books, () => {
      if (books.value.length > 0) {
        initializePriceRange()
      }
    }, { immediate: true })

    const filteredBooks = computed(() => {
      let result = [...books.value]

      // Search filter
      if (filters.search) {
        const searchTerm = filters.search.toLowerCase()
        result = result.filter(book => 
          book.title.toLowerCase().includes(searchTerm) ||
          book.author.toLowerCase().includes(searchTerm) ||
          (book.description && book.description.toLowerCase().includes(searchTerm))
        )
      }

      // Category filter
      if (filters.categories.length > 0) {
        result = result.filter(book => filters.categories.includes(book.category))
      }

      // Price filter
      result = result.filter(book =>
        book.price >= filters.priceRange[0] && book.price <= filters.priceRange[1]
      )

      // Author filter
      if (filters.authors.length > 0) {
        result = result.filter(book => filters.authors.includes(book.author))
      }

      // Publisher filter
      if (filters.publishers.length > 0) {
        result = result.filter(book => filters.publishers.includes(book.publisher || 'NXB Trẻ'))
      }

      // Rating filter
      if (filters.ratings.length > 0) {
        const minRating = Math.min(...filters.ratings)
        result = result.filter(book => book.rating >= minRating)
      }

      // Stock filter
      if (filters.inStock) {
        result = result.filter(book => book.inStock)
      }

      // Sale filter
      if (filters.onSale) {
        result = result.filter(book => book.price < book.originalPrice)
      }

      // Language filter
      if (filters.languages.length > 0) {
        result = result.filter(book => filters.languages.includes(book.language || 'Tiếng Việt'))
      }

      // Format filter
      if (filters.formats.length > 0) {
        result = result.filter(book => filters.formats.includes(book.format || 'Bìa mềm'))
      }

      return result
    })

    // Category data for the categories bar
    const categoryData = computed(() => {
      const categoryCounts = {}
      const categoryIcons = {
        'Văn học': '📚',
        'Kinh doanh': '💼',
        'Phát triển bản thân': '🚀',
        'Kỹ năng sống': '💡',
        'Giáo dục': '🎓',
        'Lịch sử': '📖',
        'Khoa học': '🔬',
        'Thiếu nhi': '🧸',
        'Ngoại ngữ': '🌍',
        'Công nghệ': '💻',
        'Y học': '🏥',
        'Nghệ thuật': '🎨'
      }

      // Count books by category
      books.value.forEach(book => {
        if (categoryCounts[book.category]) {
          categoryCounts[book.category]++
        } else {
          categoryCounts[book.category] = 1
        }
      })

      // Convert to array with icons
      return Object.entries(categoryCounts).map(([name, count]) => ({
        name,
        count,
        icon: categoryIcons[name] || '📖'
      })).sort((a, b) => b.count - a.count)
    })

    const sortedBooks = computed(() => {
      const books = [...filteredBooks.value]
      
      switch (sortBy.value) {
        case 'price-low':
          return books.sort((a, b) => a.price - b.price)
        case 'price-high':
          return books.sort((a, b) => b.price - a.price)
        case 'rating':
          return books.sort((a, b) => b.rating - a.rating)
        case 'newest':
          return books.sort((a, b) => new Date(b.publishedDate || Date.now()) - new Date(a.publishedDate || Date.now()))
        case 'popular':
          return books.sort((a, b) => (b.sales || 0) - (a.sales || 0))
        default:
          return books
      }
    })

    const totalPages = computed(() => Math.ceil(sortedBooks.value.length / itemsPerPage))
    
    const paginatedBooks = computed(() => {
      const start = (currentPage.value - 1) * itemsPerPage
      const end = start + itemsPerPage
      return sortedBooks.value.slice(start, end)
    })

    const visiblePages = computed(() => {
      const pages = []
      const total = totalPages.value
      const current = currentPage.value
      
      if (total <= 7) {
        for (let i = 1; i <= total; i++) {
          pages.push(i)
        }
      } else {
        if (current <= 4) {
          for (let i = 1; i <= 5; i++) {
            pages.push(i)
          }
          pages.push('...')
          pages.push(total)
        } else if (current >= total - 3) {
          pages.push(1)
          pages.push('...')
          for (let i = total - 4; i <= total; i++) {
            pages.push(i)
          }
        } else {
          pages.push(1)
          pages.push('...')
          for (let i = current - 1; i <= current + 1; i++) {
            pages.push(i)
          }
          pages.push('...')
          pages.push(total)
        }
      }
      
      return pages
    })

    const hasActiveFilters = computed(() => {
      return filters.search || 
             filters.categories.length > 0 || 
             filters.authors.length > 0 || 
             filters.publishers.length > 0 || 
             filters.ratings.length > 0 || 
             filters.inStock || 
             filters.onSale || 
             filters.languages.length > 0 || 
             filters.formats.length > 0 ||
             filters.priceRange[0] !== priceRange.value.min ||
             filters.priceRange[1] !== priceRange.value.max
    })

    const activeFilters = computed(() => {
      const active = []
      
      if (filters.search) {
        active.push({ key: 'search', label: `Tìm kiếm: "${filters.search}"` })
      }
      
      if (filters.categories.length > 0) {
        active.push({ key: 'categories', label: `Danh mục: ${filters.categories.join(', ')}` })
      }
      
      if (filters.authors.length > 0) {
        active.push({ key: 'authors', label: `Tác giả: ${filters.authors.join(', ')}` })
      }
      
      if (filters.publishers.length > 0) {
        active.push({ key: 'publishers', label: `NXB: ${filters.publishers.join(', ')}` })
      }
      
      if (filters.ratings.length > 0) {
        active.push({ key: 'ratings', label: `Đánh giá: ${Math.min(...filters.ratings)}+ sao` })
      }
      
      if (filters.inStock) {
        active.push({ key: 'inStock', label: 'Còn hàng' })
      }
      
      if (filters.onSale) {
        active.push({ key: 'onSale', label: 'Đang giảm giá' })
      }
      
      if (filters.languages.length > 0) {
        active.push({ key: 'languages', label: `Ngôn ngữ: ${filters.languages.join(', ')}` })
      }
      
      if (filters.formats.length > 0) {
        active.push({ key: 'formats', label: `Định dạng: ${filters.formats.join(', ')}` })
      }
      
      return active
    })

    // Methods
    const formatPrice = (price) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(price)
    }

    const getCategoryCount = (category) => {
      if (category === 'Tất cả') {
        return books.value.length
      }
      return books.value.filter(book => book.category === category).length
    }

    // Category bar methods
    const toggleCategory = (categoryName) => {
      const index = filters.categories.indexOf(categoryName)
      if (index > -1) {
        filters.categories.splice(index, 1)
      } else {
        filters.categories.push(categoryName)
      }
      currentPage.value = 1
    }

    const isCategorySelected = (categoryName) => {
      return filters.categories.includes(categoryName)
    }

    const removeCategory = (categoryName) => {
      const index = filters.categories.indexOf(categoryName)
      if (index > -1) {
        filters.categories.splice(index, 1)
        currentPage.value = 1
      }
    }

    const removeAuthor = (authorName) => {
      const index = filters.authors.indexOf(authorName)
      if (index > -1) {
        filters.authors.splice(index, 1)
        currentPage.value = 1
      }
    }

    const removePublisher = (publisherName) => {
      const index = filters.publishers.indexOf(publisherName)
      if (index > -1) {
        filters.publishers.splice(index, 1)
        currentPage.value = 1
      }
    }

    const clearAllFilters = () => {
      filters.search = ''
      filters.categories = []
      filters.priceRange = [priceRange.value.min, priceRange.value.max]
      filters.authors = []
      filters.publishers = []
      filters.ratings = []
      filters.inStock = false
      filters.onSale = false
      filters.languages = []
      filters.formats = []
      currentPage.value = 1
      
      // Clear localStorage
      localStorage.removeItem('shop-filters')
      localStorage.removeItem('shop-sort')
    }

    const removeFilter = (key) => {
      switch (key) {
        case 'search':
          filters.search = ''
          break
        case 'categories':
          filters.categories = []
          break
        case 'authors':
          filters.authors = []
          break
        case 'publishers':
          filters.publishers = []
          break
        case 'ratings':
          filters.ratings = []
          break
        case 'inStock':
          filters.inStock = false
          break
        case 'onSale':
          filters.onSale = false
          break
        case 'languages':
          filters.languages = []
          break
        case 'formats':
          filters.formats = []
          break
      }
      currentPage.value = 1
    }

    // Watch for filter changes to reset pagination
    watch([filters, sortBy], () => {
      currentPage.value = 1
      // Save filters to localStorage
      localStorage.setItem('shop-filters', JSON.stringify(filters))
      localStorage.setItem('shop-sort', sortBy.value)
    })

    // Load filters from localStorage on mount
    const loadSavedFilters = () => {
      const savedFilters = localStorage.getItem('shop-filters')
      const savedSort = localStorage.getItem('shop-sort')
      
      if (savedFilters) {
        const parsed = JSON.parse(savedFilters)
        Object.assign(filters, parsed)
      }
      
      if (savedSort) {
        sortBy.value = savedSort
      }
    }

    // Load saved filters and apply category from query when component mounts
    loadSavedFilters()

    // Apply category from query string if provided (e.g., /products?category=Văn học)
    try {
      const params = new URLSearchParams(window.location.search)
      const categoryFromQuery = params.get('category')
      if (categoryFromQuery) {
        filters.categories = [categoryFromQuery]
      }
    } catch (e) {
      // Fallback: ignore invalid query strings
      // eslint-disable-next-line no-unused-vars
      const _ignored = e
    }

    return {
      // Data
      viewMode,
      sortBy,
      currentPage,
      filters,
      showMobileFilters,
      authors,
      publishers,
      languages,
      formats,
      
      // Computed
      books,
      categories,
      priceRange,
      filteredBooks,
      sortedBooks,
      totalPages,
      paginatedBooks,
      visiblePages,
      hasActiveFilters,
      activeFilters,
      categoryData,
      
      // Methods
      formatPrice,
      getCategoryCount,
      clearAllFilters,
      removeFilter,
      
      // Category bar methods
      toggleCategory,
      isCategorySelected,
      removeCategory,
      removeAuthor,
      removePublisher
    }
  }
}
</script>

<style scoped>
.slider {
  -webkit-appearance: none;
  appearance: none;
}

.slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: #3b82f6;
  cursor: pointer;
}

.slider::-moz-range-thumb {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: #3b82f6;
  cursor: pointer;
  border: none;
}
</style>
