<template>
  <div v-if="boSach" class="container mx-auto px-4 py-8">
    <div class="flex flex-col lg:flex-row gap-12">
      <!-- Ảnh lớn + thumbnails ngang, chiếm 2/5 -->
      <div class="w-full lg:w-2/5 space-y-4 flex-shrink-0">
        <div class="flex flex-col items-center">
          <div class="bg-gray-100 rounded-lg overflow-hidden flex items-center justify-center" style="width:342px; height:342px; display:flex; align-items:center; justify-content:center;">
            <img :src="mainImage" :alt="boSach.title" class="object-contain transition-all duration-200 mx-auto my-auto" style="max-width:342px; max-height:342px; width:auto; height:auto; display:block; margin:auto;" />
          </div>
          <div class="flex flex-row items-center space-x-2 mt-2 justify-center w-full">
            <button @click="prevThumbs" :disabled="!canPrev" class="px-2 py-1 text-lg text-gray-500 hover:text-blue-600 disabled:opacity-30">&#8592;</button>
            <div class="flex flex-row space-x-4">
              <img
                v-for="(img, idx) in pagedThumbs"
                :key="img + idx"
                :src="img"
                class="w-20 h-28 object-cover rounded border cursor-pointer hover:border-blue-500"
                :class="{ 'border-blue-600': mainImage === img }"
                @click="mainImage = img"
              >
            </div>
            <button @click="nextThumbs" :disabled="!canNext" class="px-2 py-1 text-lg text-gray-500 hover:text-blue-600 disabled:opacity-30">&#8594;</button>
          </div>
        </div>
      </div>
      <!-- Thông tin bộ sách, chiếm 3/5 -->
      <div class="w-full lg:w-3/5 space-y-6">
        <div>
          <span class="text-sm text-blue-600 font-medium">Bộ sách</span>
          <h1 class="text-3xl font-bold text-gray-900 mt-2">{{ boSach.tenBoSach }}</h1>
        </div>
        <!-- Giá và ưu đãi -->
        <div class="space-y-2">
          <div class="flex items-center space-x-4">
            <span class="text-3xl font-bold text-red-600">{{ formatPrice(discountInfo ? discountInfo.giaSauGiam : (boSach.giaTien || 0)) }}</span>
            <span v-if="discountInfo && discountInfo.giaBanDau && discountInfo.giaBanDau > discountInfo.giaSauGiam" class="text-lg text-gray-500 line-through">{{ formatPrice(discountInfo.giaBanDau) }}</span>
            <span v-if="discountInfo && discountInfo.giaBanDau && discountInfo.giaBanDau > discountInfo.giaSauGiam" class="bg-red-100 text-red-800 px-2 py-1 rounded-full text-sm font-semibold">
              -{{ Math.round(((discountInfo.giaBanDau - discountInfo.giaSauGiam) / discountInfo.giaBanDau) * 100) }}%
            </span>
            <span class="bg-green-100 text-green-800 px-2 py-1 rounded-full text-sm font-semibold" v-if="boSach.soLuong > 0">Còn hàng</span>
            <span class="bg-red-100 text-red-800 px-2 py-1 rounded-full text-sm font-semibold" v-else>Hết hàng</span>
          </div>
        </div>
        <!-- Thông tin chi tiết -->
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-x-8 gap-y-2 text-sm">
          <div><span class="font-medium">Tác giả:</span> {{ displayAuthors }}</div>
          <div><span class="font-medium">Người dịch:</span> {{ displayTranslators }}</div>
          <div><span class="font-medium">Nhà xuất bản:</span> {{ displayPublishers }}</div>
          <div><span class="font-medium">Ngôn ngữ:</span> {{ displayLanguages }}</div>
          <div><span class="font-medium">Loại bìa:</span> {{ displayCovers }}</div>
          <div><span class="font-medium">Chất liệu:</span> {{ displayMaterials }}</div>
          <div><span class="font-medium">Trọng lượng:</span> {{ totalWeight }} gr</div>
          <div><span class="font-medium">Số lượng tồn:</span> {{ boSach.soLuong }}</div>
        </div>
        <div class="space-y-4">
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
              :disabled="boSach.soLuong === 0"
              :class="[
                'flex-1 px-6 py-3 rounded-lg font-semibold transition-colors flex items-center justify-center',
                boSach.soLuong > 0 
                  ? 'bg-gradient-to-r from-blue-600 to-purple-600 text-white hover:from-blue-700 hover:to-purple-700' 
                  : 'bg-gradient-to-r from-gray-400 to-gray-500 text-white cursor-not-allowed'
              ]"
            >
              <svg v-if="boSach.soLuong === 0" class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4m0 0L7 13m0 0l-2.5 5M7 13l2.5 5m6-5v6a2 2 0 01-2 2H9a2 2 0 01-2-2v-6m8 0V9a2 2 0 00-2-2H9a2 2 0 00-2 2v4.01"></path>
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path>
              </svg>
              {{ boSach.soLuong > 0 ? 'Thêm vào giỏ hàng' : 'Hết hàng' }}
            </button>
            <button class="px-6 py-3 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors">♡ Yêu thích</button>
          </div>
        </div>
      </div>
    </div>
    <!-- Mô tả bộ sách -->
    <div class="mt-10 bg-white rounded-lg shadow p-6">
      <h3 class="text-lg font-semibold mb-2">Mô tả bộ sách</h3>
      <p class="text-gray-700 leading-relaxed" v-if="boSach.moTa && boSach.moTa.trim() !== ''">{{ boSach.moTa }}</p>
      <p class="text-gray-500 italic" v-else>Chưa có mô tả cho bộ sách này.</p>
      <div class="mb-2 text-gray-800">
        Bộ sách {{ boSach.tenBoSach || boSach.title }} ({{ chiTietList.length }} cuốn) bao gồm:
        <ul class="list-disc ml-6 mt-1">
          <li v-for="ct in chiTietList" :key="ct.idChiTietSanPham?.id || ct.idChiTietSanPham?.maChiTietSanPham || ct.idChiTietSanPham?.tenChiTietSanPham">
            {{ ct.idChiTietSanPham?.tenChiTietSanPham || 'Chưa rõ tên' }}
            <span v-if="ct.idChiTietSanPham?.isbn" class="text-xs text-gray-500 ml-2">(ISBN: {{ formatIsbnShort(ct.idChiTietSanPham.isbn) }})</span>
          </li>
        </ul>
        <div v-if="chiTietList.length > 2" class="text-xs text-gray-500 ml-6 mt-1">... ({{ chiTietList.length - 2 }} ISBN khác)</div>
      </div>

    </div>
  </div>
  <div v-else class="container mx-auto px-4 py-8 text-center">
    <p class="text-gray-600">Không tìm thấy bộ sách</p>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import BoSachService from '@/service/BoSachService'
import DotGiamGiaService from '@/service/DotGiamGiaService'
import { useRoute } from 'vue-router'
import { useRealDataStore } from '../store/realDataStore'

export default {
  name: 'BoSachDetail',
  setup() {
    const store = useRealDataStore()
    const route = useRoute()
    const boSach = ref(null)
    const chiTietList = ref([])
    const quantity = ref(1)
    const mainImage = ref('')
    const allImages = ref([])
    const thumbPage = ref(0)
    const THUMBS_PER_PAGE = 4
    const discountInfo = ref(null)

    // Thông tin chi tiết tổng hợp
    const displayAuthors = computed(() => {
      const set = new Set()
      chiTietList.value.forEach(ct => {
        if (ct.idChiTietSanPham?.idTacGia?.tenTacGia) set.add(ct.idChiTietSanPham.idTacGia.tenTacGia)
      })
      return Array.from(set).join(', ') || boSach.value?.author || 'N/A'
    })
    const displayTranslators = computed(() => {
      const set = new Set()
      chiTietList.value.forEach(ct => {
        if (ct.idChiTietSanPham?.idNguoiDich?.tenNguoiDich) set.add(ct.idChiTietSanPham.idNguoiDich.tenNguoiDich)
      })
      return Array.from(set).join(', ') || 'N/A'
    })
    const displayPublishers = computed(() => {
      const set = new Set()
      chiTietList.value.forEach(ct => {
        if (ct.idChiTietSanPham?.idNhaXuatBan?.tenNhaXuatBan) set.add(ct.idChiTietSanPham.idNhaXuatBan.tenNhaXuatBan)
      })
      return Array.from(set).join(', ') || 'N/A'
    })
    const displayLanguages = computed(() => {
      const set = new Set()
      chiTietList.value.forEach(ct => {
        if (ct.idChiTietSanPham?.idNgonNgu?.tenNgonNgu) set.add(ct.idChiTietSanPham.idNgonNgu.tenNgonNgu)
      })
      return Array.from(set).join(', ') || 'N/A'
    })
    const displayCovers = computed(() => {
      const set = new Set()
      chiTietList.value.forEach(ct => {
        if (ct.idChiTietSanPham?.idLoaiBia?.tenLoaiBia) set.add(ct.idChiTietSanPham.idLoaiBia.tenLoaiBia)
      })
      return Array.from(set).join(', ') || 'N/A'
    })
    const displayMaterials = computed(() => {
      const set = new Set()
      chiTietList.value.forEach(ct => {
        if (ct.idChiTietSanPham?.idChatLieu?.tenChatLieu) set.add(ct.idChiTietSanPham.idChatLieu.tenChatLieu)
      })
      return Array.from(set).join(', ') || 'N/A'
    })
    const totalWeight = computed(() => {
      let sum = 0
      chiTietList.value.forEach(ct => {
        if (ct.idChiTietSanPham?.trongLuong) sum += Number(ct.idChiTietSanPham.trongLuong)
      })
      return sum
    })

    const formatPrice = (price) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(price)
    }

    const increaseQuantity = () => {
      if (boSach.value?.soLuong === 0) {
        alert('Bộ sách này đã hết hàng!')
        return
      }
      if (quantity.value < (boSach.value?.soLuong || 1)) {
        quantity.value++
      } else {
        alert('Chỉ có thể mua tối đa ' + (boSach.value?.soLuong || 1) + ' bộ sách này!')
      }
    }
    const decreaseQuantity = () => { if (quantity.value > 1) quantity.value-- }

    const addToCart = () => {
      if (boSach.value?.soLuong === 0) {
        alert('Bộ sách này đã hết hàng!')
        return
      }
      
      if (quantity.value > boSach.value?.soLuong) {
        alert(`Chỉ còn ${boSach.value?.soLuong} bộ sách trong kho!`)
        return
      }
      
      // Tạo object bộ sách để thêm vào giỏ hàng
      const boSachForCart = {
        id: boSach.value.id,
        title: boSach.value.tenBoSach,
        author: displayAuthors.value,
        price: boSach.value.giaTien,
        image: boSach.value.url,
        description: boSach.value.moTa,
        category: 'Bộ sách',
        inStock: boSach.value.soLuong > 0,
        soLuongTon: boSach.value.soLuong
      }
      
      // Thêm vào giỏ hàng
      for (let i = 0; i < quantity.value; i++) {
        const success = store.addToCart(boSachForCart)
        if (!success) {
          alert('Không thể thêm bộ sách: vượt quá số lượng tồn kho!')
          return
        }
      }
      
      alert('Đã thêm vào giỏ hàng thành công!')
      quantity.value = 1
    }

    const pagedThumbs = computed(() => {
      const start = thumbPage.value * THUMBS_PER_PAGE
      return allImages.value.slice(start, start + THUMBS_PER_PAGE)
    })
    const canPrev = computed(() => thumbPage.value > 0)
    const canNext = computed(() => (thumbPage.value + 1) * THUMBS_PER_PAGE < allImages.value.length)
    const prevThumbs = () => { if (canPrev.value) thumbPage.value-- }
    const nextThumbs = () => { if (canNext.value) thumbPage.value++ }

    const formatIsbnShort = (isbn) => {
      if (!isbn) return '';
      return isbn.length > 20 ? isbn.slice(0, 20) + '...' : isbn;
    };

    onMounted(async () => {
      let id = route.params.id
      id = String(Number(id))
      const allBoSach = await BoSachService.getAll()
      boSach.value = allBoSach.find(bs => String(bs.id) === id)
      if (boSach.value) {
        chiTietList.value = await BoSachService.getBoSachChiTietByBoSachId(boSach.value.id)
        
        // Lấy thông tin giảm giá cho bộ sách
        try {
          discountInfo.value = await DotGiamGiaService.getActiveBoSachDetail(boSach.value.id)
        } catch (error) {
          console.log('No active discount for this book set:', error.message)
          discountInfo.value = null
        }
        
        // Ảnh bộ sách + tất cả ảnh chi tiết sách
        const images = [boSach.value.url]
        chiTietList.value.forEach(ct => {
          const arr = ct.idChiTietSanPham?.anhSanPhams || [];
          arr.forEach(a => {
            if (a.url && typeof a.url === 'string' && a.url.trim() !== '' && a.url !== boSach.value.url) {
              images.push(a.url)
            }
          })
        })
        allImages.value = images.filter(Boolean)
        mainImage.value = allImages.value[0] || ''
      }
    })

    return {
      boSach,
      chiTietList,
      quantity,
      mainImage,
      allImages,
      pagedThumbs,
      canPrev,
      canNext,
      prevThumbs,
      nextThumbs,
      formatPrice,
      increaseQuantity,
      decreaseQuantity,
      addToCart,
      displayAuthors,
      displayTranslators,
      displayPublishers,
      displayLanguages,
      displayCovers,
      displayMaterials,
      totalWeight,
      formatIsbnShort,
      discountInfo
    }
  }
}
</script> 