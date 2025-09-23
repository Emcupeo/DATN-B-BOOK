<template>
  <div v-if="book" class="container mx-auto px-4 py-8">
    <div class="flex flex-col lg:flex-row gap-12">
      <!-- Ảnh lớn + thumbnails ngang, giống BoSachDetail -->
      <div class="w-full lg:w-2/5 space-y-4 flex-shrink-0">
        <div class="flex flex-col items-center">
          <div class="bg-gray-100 rounded-lg overflow-hidden flex items-center justify-center" style="width:342px; height:342px; display:flex; align-items:center; justify-content:center;">
            <img :src="mainImage" :alt="book.title" class="object-contain transition-all duration-200 mx-auto my-auto" style="max-width:342px; max-height:342px; width:auto; height:auto; display:block; margin:auto;" />
          </div>
          <div class="flex flex-row space-x-4 mt-2 justify-center w-full">
            <img
              v-for="(img, idx) in allImages"
              :key="img + idx"
              :src="img"
              class="w-20 h-28 object-cover rounded border cursor-pointer hover:border-blue-500"
              :class="{ 'border-blue-600': mainImage === img }"
              @click="setMainImage(img)"
            >
          </div>
        </div>
      </div>
      <!-- Thông tin sách -->
      <div class="w-full lg:w-3/5 space-y-6">
        <div v-if="book.tenBoSach || book.tenBoSach || book.boSach">
          <span class="font-semibold text-blue-600">Thuộc bộ sách: </span>
          <span class="font-semibold text-gray-800">
            {{ book.tenBoSach || (typeof book.boSach === 'string' ? book.boSach : (book.boSach?.tenBoSach || book.boSach?.tenBoSach )) }}
          </span>
        </div>
        <div>
          <span class="text-sm text-blue-600 font-medium">{{ book.category }}</span>
          <h1 class="text-3xl font-bold text-gray-900 mt-2">{{ book.tenBoSach }}</h1>
          <p class="text-lg text-gray-600 mt-1">Tác giả: {{ book.author }}</p>
        </div>
        <!-- Giá theo chi tiết được chọn -->
        <div class="space-y-2">
          <div class="flex items-center space-x-4">
            <span class="text-3xl font-bold text-red-600">{{ formatPrice(selectedPrice) }}</span>
            <span v-if="selectedOriginalPrice && selectedOriginalPrice > selectedPrice" class="text-lg text-gray-500 line-through">{{ formatPrice(selectedOriginalPrice) }}</span>
            <span v-if="selectedOriginalPrice && selectedOriginalPrice > selectedPrice" class="bg-red-100 text-red-800 px-2 py-1 rounded-full text-sm font-semibold">
              -{{ formatDiscountAmount(selectedOriginalPrice - selectedPrice) }}
            </span>
            <span class="bg-green-100 text-green-800 px-2 py-1 rounded-full text-sm font-semibold" v-if="totalInStock > 0">Còn hàng</span>
            <span class="bg-red-100 text-red-800 px-2 py-1 rounded-full text-sm font-semibold" v-else>Hết hàng</span>
          </div>
        </div>
        <!-- Thông tin chi tiết -->
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-x-8 gap-y-2 text-sm">
          <div><span class="font-medium">Tác giả:</span> {{ book.author }}</div>
          <div v-if="book.translator"><span class="font-medium">Người dịch:</span> {{ book.translator }}</div>
          <div v-if="book.publisher"><span class="font-medium">Nhà xuất bản:</span> {{ book.publisher }}</div>
          <div v-if="book.language"><span class="font-medium">Ngôn ngữ:</span> {{ book.language }}</div>
          <div v-if="book.cover"><span class="font-medium">Loại bìa:</span> {{ book.cover }}</div>
          <div v-if="book.material"><span class="font-medium">Chất liệu:</span> {{ book.material }}</div>
          <div v-if="book.weight"><span class="font-medium">Trọng lượng:</span> {{ book.weight }} gr</div>
          <div><span class="font-medium">Số lượng tồn:</span> {{ totalInStock }}</div>
        </div>
        <!-- Chọn chi tiết + Số lượng và nút mua -->
        <div class="space-y-4">
          <div v-if="book.productDetailItems && book.productDetailItems.length" class="flex items-center space-x-2">
            <label class="text-sm font-medium text-gray-700">Chọn phiên bản:</label>
            <select v-model="selectedCtspId" @change="onSelectCtsp" class="border rounded px-2 py-1 text-sm">
              <option v-for="ct in book.productDetailItems" :key="ct.id" :value="ct.id">
                {{ ct.isbn || ('CTSP #' + ct.id) }} - {{ formatPrice(ct.gia) }}
              </option>
            </select>
          </div>
          <div class="flex items-center space-x-4">
            <label class="text-sm font-medium text-gray-700">Số lượng:</label>
            <div class="flex items-center border border-gray-300 rounded-lg">
              <button @click="decreaseQuantity" class="px-3 py-2 text-gray-600 hover:bg-gray-100" :disabled="quantity <= 1">-</button>
              <span class="px-4 py-2 border-x border-gray-300">{{ quantity }}</span>
              <button @click="increaseQuantity" class="px-3 py-2 text-gray-600 hover:bg-gray-100">+</button>
            </div>
          </div>
          <div class="flex space-x-4">
            <button 
              @click="addToCart" 
              :disabled="totalInStock === 0"
              :class="[
                'flex-1 px-6 py-3 rounded-lg font-semibold transition-colors flex items-center justify-center',
                totalInStock > 0 
                  ? 'bg-gradient-to-r from-blue-600 to-purple-600 text-white hover:from-blue-700 hover:to-purple-700' 
                  : 'bg-gradient-to-r from-gray-400 to-gray-500 text-white cursor-not-allowed'
              ]"
            >
              <svg v-if="totalInStock === 0" class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-2.5 5M7 13l2.5 5m6-5v6a2 2 0 01-2 2H9a2 2 0 01-2-2v-6m8 0V9a2 2 0 00-2-2H9a2 2 0 00-2 2v4.01"></path>
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path>
              </svg>
              {{ totalInStock > 0 ? 'Thêm vào giỏ hàng' : 'Hết hàng' }}
            </button>
            <button class="px-6 py-3 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors">♡ Yêu thích</button>
          </div>
        </div>
        <!-- Mô tả sách ở dưới cùng -->
      </div>
    </div>
    <div class="mt-10 bg-white rounded-lg shadow p-6">
      <h3 class="text-lg font-semibold mb-2">Mô tả sách</h3>
      <div v-if="book.productDetailItems && book.productDetailItems.length">
        <span class="font-medium">ISBN:</span>
        <span v-for="(ct, idx) in book.productDetailItems.slice(0,2)" :key="ct.id || idx" class="text-xs text-gray-500 ml-2">{{ formatIsbnShort(ct.isbn) }}</span>
        <span v-if="book.productDetailItems.length > 2" class="text-xs text-gray-500 ml-2">... ({{ book.productDetailItems.length - 2 }} ISBN khác)</span>
      </div>
      <p class="text-gray-700 leading-relaxed" v-if="book.moTa && book.moTa.trim() !== ''">{{ book.moTa }}</p>
      <p class="text-gray-700 leading-relaxed" v-else-if="book.description && book.description.trim() !== ''">{{ book.description }}</p>
      <p class="text-gray-500 italic" v-else>Chưa có mô tả cho sách này.</p>
    </div>
  </div>
  <div v-else class="container mx-auto px-4 py-8 text-center">
    <p class="text-gray-600">Không tìm thấy sách</p>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRealDataStore } from '../store/realDataStore'
import { useRoute } from 'vue-router'
import DotGiamGiaService from '@/service/DotGiamGiaService'

export default {
  name: 'BookDetail',
  setup() {
    const store = useRealDataStore()
    const route = useRoute()
    const quantity = ref(1)
    const selectedCtspId = ref(null)
    const selectedPrice = ref(0)
    const selectedOriginalPrice = ref(0)
    const mainImage = ref('')
    const allImages = ref([])

    const book = computed(() => store.products.value.find(p => String(p.id) === String(Number(route.params.id))))

    // Tổng số lượng tồn
    const totalInStock = computed(() => {
      if (!book.value || !book.value.productDetailItems) return 0
      return book.value.productDetailItems.reduce((sum, item) => sum + (item.soLuongTon || 0), 0)
    })

    // Lấy tất cả ảnh con từ các chi tiết sản phẩm
    const collectAllImages = () => {
      const images = []
      if (book.value && book.value.productDetailItems) {
        book.value.productDetailItems.forEach(item => {
          if (item.anhSanPhams && Array.isArray(item.anhSanPhams)) {
            item.anhSanPhams.forEach(a => {
              if (a.url && typeof a.url === 'string' && a.url.trim() !== '' && !images.includes(a.url)) {
                images.push(a.url)
              }
            })
          }
        })
      }
      // Đảm bảo ảnh chính luôn là đầu tiên
      if (book.value && book.value.image && !images.includes(book.value.image)) {
        images.unshift(book.value.image)
      }
      return images
    }

    const setMainImage = (img) => {
      mainImage.value = img
    }

    const formatPrice = (price) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(price)
    }

    const formatDiscountAmount = (amount) => {
      if (amount >= 1000000) {
        return `${Math.round(amount / 1000000)}M`
      } else if (amount >= 1000) {
        return `${Math.round(amount / 1000)}k`
      } else {
        return `${Math.round(amount)}`
      }
    }

    const formatIsbnShort = (isbn) => {
      if (!isbn) return '';
      return isbn.length > 20 ? isbn.slice(0, 20) + '...' : isbn;
    };

    const increaseQuantity = () => {
      if (quantity.value < totalInStock.value) {
        quantity.value++
      } else {
        alert('Chỉ có thể mua tối đa ' + totalInStock.value + ' cuốn sách này!')
      }
    }

    const decreaseQuantity = () => {
      if (quantity.value > 1) {
        quantity.value--
      }
    }

    const addToCart = () => {
      if (totalInStock.value === 0) {
        alert('Sản phẩm này đã hết hàng!')
        return
      }
      
      if (quantity.value > totalInStock.value) {
        alert(`Chỉ còn ${totalInStock.value} sản phẩm trong kho!`)
        return
      }
      
      for (let i = 0; i < quantity.value; i++) {
        const success = store.addToCart(book.value)
        if (!success) {
          alert('Không thể thêm sản phẩm: vượt quá số lượng tồn kho!')
          return
        }
      }
      
      alert('Đã thêm vào giỏ hàng thành công!')
      quantity.value = 1
    }

    onMounted(async () => {
      if (!book.value) {
        await store.loadProducts()
      }
      // Lấy danh sách ảnh con
      allImages.value = collectAllImages()
      mainImage.value = allImages.value[0] || ''
      if (book.value && book.value.productDetailItems && book.value.productDetailItems.length) {
        selectedCtspId.value = book.value.productDetailItems[0].id
        await onSelectCtsp()
      }
    })

    const onSelectCtsp = async () => {
      const ct = (book.value?.productDetailItems || []).find(x => x.id === selectedCtspId.value)
      if (!ct) return
      try {
        const detail = await DotGiamGiaService.getActiveDetail(ct.id)
        if (detail && detail.giaBanDau && detail.giaSauGiam) {
          selectedOriginalPrice.value = Number(detail.giaBanDau)
          selectedPrice.value = Number(detail.giaSauGiam)
        } else {
          selectedOriginalPrice.value = Number(ct.gia)
          selectedPrice.value = Number(ct.gia)
        }
      } catch (e) {
        selectedOriginalPrice.value = Number(ct.gia)
        selectedPrice.value = Number(ct.gia)
      }
    }

    return {
      book,
      quantity,
      formatPrice,
      formatDiscountAmount,
      increaseQuantity,
      decreaseQuantity,
      addToCart,
      mainImage,
      allImages,
      setMainImage,
      totalInStock,
      formatIsbnShort,
      selectedCtspId,
      onSelectCtsp,
      selectedPrice,
      selectedOriginalPrice
    }
  }
}
</script>