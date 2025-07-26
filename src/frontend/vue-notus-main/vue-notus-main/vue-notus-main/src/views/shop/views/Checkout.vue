<template>
  <div class="container mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold mb-8">Thanh toán</h1>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
      <!-- Checkout Form -->
      <div class="space-y-6">
        <div class="mb-4">
          <label class="block text-gray-700 text-sm font-bold mb-2">
            Ghi chú đơn hàng
          </label>
          <textarea
            v-model="ghiChu"
            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            rows="3"
            placeholder="Nhập ghi chú cho đơn hàng (tùy chọn)"
          ></textarea>
        </div>

        <div class="mb-4">
          <label class="block text-gray-700 text-sm font-bold mb-2">
            Thông tin khách hàng
          </label>
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
                  :disabled="loadingProvinces"
                >
                  <option value="">Chọn thành phố</option>
                  <option v-for="p in provinces" :key="p.code" :value="p.code">{{ p.name }}</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Quận/Huyện *</label>
                <select 
                  v-model="shippingAddress.district"
                  class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                  required
                  :disabled="!shippingAddress.city || loadingDistricts"
                >
                  <option value="">Chọn quận/huyện</option>
                  <option v-for="d in districts" :key="d.code" :value="d.code">{{ d.name }}</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Phường/Xã *</label>
                <select 
                  v-model="shippingAddress.ward"
                  class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                  required
                  :disabled="!shippingAddress.district || loadingWards"
                >
                  <option value="">Chọn phường/xã</option>
                  <option v-for="w in wards" :key="w.code" :value="w.code">{{ w.name }}</option>
                </select>
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
          </div>
        </div>
      </div>

      <!-- Order Summary -->
      <div class="space-y-6">
        <div class="bg-white rounded-lg shadow-md p-6">
          <h2 class="text-xl font-semibold mb-4">Đơn hàng của bạn</h2>

          <!-- Mã giảm giá -->
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">Mã giảm giá</label>
            <div class="flex flex-row items-stretch space-x-2">
              <input
                v-model="discountCode"
                type="text"
                placeholder="Nhập mã giảm giá (SALE10, SALE20)"
                class="flex-1 px-3 py-2 border border-gray-300 rounded-l-lg focus:outline-none focus:border-blue-500 h-10"
              >
              <button
                @click="applyDiscount"
                class="px-4 h-10 bg-blue-600 text-white rounded-r-lg hover:bg-blue-700 font-medium whitespace-nowrap"
                type="button"
              >
                Áp dụng
              </button>
            </div>
            <div v-if="discountError" class="text-red-500 text-xs mt-1">{{ discountError }}</div>
            <div v-if="discountInfo" class="bg-green-50 border border-green-300 rounded-lg p-4 mt-2 mb-2">
              <div class="flex items-center mb-2">
                <svg class="w-5 h-5 text-green-600 mr-2" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2l4 -4"/></svg>
                <span class="font-semibold text-green-700">Mã giảm giá hợp lệ!</span>
              </div>
              <div class="text-sm text-gray-700 mb-1">
                <span class="font-medium">Mã:</span> <span class="font-mono">{{ discountInfo.maPhieuGiamGia }}</span>
              </div>
              <div class="text-sm text-gray-700 mb-1">
                <span class="font-medium">Tên chương trình:</span> {{ discountInfo.tenPhieuGiamGia }}
              </div>
              <div class="text-sm text-gray-700 mb-1">
                <span class="font-medium">Loại giảm giá:</span>
                <span v-if="discountInfo.loaiPhieu === 'PERCENT'">Giảm {{ discountInfo.soPhanTramGiam }}%</span>
                <span v-else>Giảm {{ formatPrice(discountInfo.giaTriGiam) }}</span>
              </div>
              <div class="text-sm text-gray-700 mb-1">
                <span class="font-medium">Số tiền được giảm:</span> <span class="text-green-600 font-semibold">-{{ formatPrice(discountAmount) }}</span>
              </div>
              <div class="text-sm text-gray-700 mb-1" v-if="discountInfo.giaTriDonHangToiThieu">
                <span class="font-medium">Đơn hàng tối thiểu:</span> {{ formatPrice(discountInfo.giaTriDonHangToiThieu) }}
              </div>
              <div class="text-sm text-gray-700 mb-1" v-if="discountInfo.soLuong !== null">
                <span class="font-medium">Số lượt còn lại:</span> {{ discountInfo.soLuong }}
              </div>
              <div class="text-sm text-gray-700 mb-1" v-if="discountInfo.ngayBatDau">
                <span class="font-medium">Hiệu lực từ:</span> {{ new Date(discountInfo.ngayBatDau).toLocaleDateString('vi-VN') }}
              </div>
              <div class="text-sm text-gray-700 mb-1" v-if="discountInfo.ngayKetThuc">
                <span class="font-medium">Đến hết:</span> {{ new Date(discountInfo.ngayKetThuc).toLocaleDateString('vi-VN') }}
              </div>
              <div class="text-sm text-gray-700 mb-1" v-if="discountInfo.moTa">
                <span class="font-medium">Mô tả:</span> {{ discountInfo.moTa }}
              </div>
            </div>
          </div>
          
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
            <div v-if="discountInfo" class="flex justify-between">
              <span>Giảm giá ({{ discountInfo.loaiPhieu === 'PERCENT' ? discountInfo.soPhanTramGiam + '%' : formatPrice(discountInfo.giaTriGiam) }}):</span>
              <span class="text-green-600">-{{ formatPrice(discountAmount) }}</span>
            </div>
            <div class="flex justify-between">
              <span>Phí vận chuyển:</span>
              <span class="text-blue-600">
                <span v-if="shippingFeeLoading">Đang tính...</span>
                <span v-else-if="shippingFeeError">{{ shippingFeeError }}</span>
                <span v-else-if="shippingFee > 0">{{ formatPrice(shippingFee) }}</span>
                <span v-else>Chưa chọn</span>
              </span>
            </div>
            <hr class="my-2">
            <div class="flex justify-between text-lg font-bold">
              <span>Tổng cộng:</span>
              <span class="text-red-600">{{ formatPrice(finalTotal) }}</span>
            </div>
          </div>

          <button 
            @click="onPlaceOrder"
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
    <!-- Modal QR chuyển khoản ngân hàng -->
    <div v-if="showBankQR" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-lg p-8 max-w-md w-full relative">
        <h2 class="text-xl font-bold mb-4 text-center">Chuyển khoản ngân hàng</h2>
        <div class="flex flex-col items-center mb-4">
          <img :src="bankQRUrl" alt="QR chuyển khoản" class="w-64 h-64 object-contain mb-2" />
          <div class="text-sm text-gray-700 mb-1">Số tài khoản: <span class="font-semibold">{{ bankAccount }}</span></div>
          <div class="text-sm text-gray-700 mb-1">Ngân hàng: <span class="font-semibold">{{ bankName }}</span></div>
          <div class="text-sm text-gray-700 mb-1">Số tiền: <span class="font-semibold text-blue-600">{{ formatPrice(transferAmount) }}</span></div>
          <div class="text-sm text-gray-700 mb-1">Nội dung chuyển khoản: <span class="font-semibold text-green-600">{{ transferContent }}</span></div>
        </div>
        <button @click="confirmBankTransfer" class="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 transition-colors">Tôi đã chuyển tiền</button>
        <button @click="showBankQR = false" class="absolute top-2 right-2 text-gray-400 hover:text-red-500 text-xl">×</button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, watch } from 'vue'
import { useRealDataStore } from '../store/realDataStore'
import { useRouter } from 'vue-router'
import phieuGiamGiaService from '@/service/phieuGiamGiaService'
import hoaDonService from '@/service/hoaDonService'

export default {
  name: 'Checkout',
  setup() {
    const store = useRealDataStore()
    const router = useRouter()

    const customerInfo = ref({
      fullName: '',
      phone: '',
      email: ''
    })

    const ghiChu = ref('')

    const shippingAddress = ref({
      address: '',
      city: '', // province code
      district: '', // district code
      ward: '' // ward code
    })

    // Địa chỉ động
    const provinces = ref([])
    const districts = ref([])
    const wards = ref([])
    const loadingProvinces = ref(false)
    const loadingDistricts = ref(false)
    const loadingWards = ref(false)

    // Fetch provinces
    const fetchProvinces = async () => {
      loadingProvinces.value = true
      const res = await fetch('https://provinces.open-api.vn/api/p/')
      provinces.value = await res.json()
      loadingProvinces.value = false
    }
    // Fetch districts
    const fetchDistricts = async (provinceCode) => {
      if (!provinceCode) { districts.value = []; wards.value = []; return }
      loadingDistricts.value = true
      const res = await fetch(`https://provinces.open-api.vn/api/p/${provinceCode}?depth=2`)
      const data = await res.json()
      districts.value = data.districts || []
      loadingDistricts.value = false
      wards.value = []
    }
    // Fetch wards
    const fetchWards = async (districtCode) => {
      if (!districtCode) { wards.value = []; return }
      loadingWards.value = true
      const res = await fetch(`https://provinces.open-api.vn/api/d/${districtCode}?depth=2`)
      const data = await res.json()
      wards.value = data.wards || []
      loadingWards.value = false
    }

    onMounted(() => {
      fetchProvinces()
    })
    watch(() => shippingAddress.value.city, (val) => {
      shippingAddress.value.district = ''
      shippingAddress.value.ward = ''
      fetchDistricts(val)
    })
    watch(() => shippingAddress.value.district, (val) => {
      shippingAddress.value.ward = ''
      fetchWards(val)
    })

    const paymentMethod = ref('cod')
    const cartItems = computed(() => store.cart.value)
    const cartTotal = computed(() => store.cartTotal.value)

    // Shipping fee mockup logic
    const shippingFee = computed(() => {
      const cityCode = shippingAddress.value.city
      if (!cityCode) return 0
      const city = provinces.value.find(p => p.code == cityCode)
      if (!city) return 0
      if (city.name.includes('Hồ Chí Minh') || city.name.includes('Hà Nội')) return 20000
      if (city.name.includes('Đà Nẵng')) return 30000
      return 40000
    })
    const shippingFeeLoading = ref(false)
    const shippingFeeError = ref('')
    const shippingServices = ref([])
    const selectedServiceIndex = ref(0)

    // Discount logic
    const discountCode = ref('')
    const discountPercent = ref(0)
    const discountError = ref('')
    const discountInfo = ref(null)
    const discountAmount = computed(() => {
      if (discountInfo.value) {
        if (discountInfo.value.loaiPhieu === 'PERCENT') {
          return Math.round(cartTotal.value * discountInfo.value.soPhanTramGiam / 100)
        } else if (discountInfo.value.loaiPhieu === 'AMOUNT') {
          return Math.min(discountInfo.value.giaTriGiam, cartTotal.value)
        }
      }
      return 0
    })
    const finalTotal = computed(() => cartTotal.value - discountAmount.value + shippingFee.value)

    async function applyDiscount() {
      const code = discountCode.value.trim().toUpperCase()
      discountError.value = ''
      discountInfo.value = null
      if (!code) {
        discountError.value = ''
        return
      }
      try {
        const res = await phieuGiamGiaService.getAll({ searchQuery: code, trangThai: true })
        const list = res.data.content || res.data
        if (!list.length) {
          discountError.value = 'Mã giảm giá không hợp lệ hoặc đã hết hạn!'
          return
        }
        const voucher = list[0]
        // Kiểm tra ngày hiệu lực
        const now = new Date()
        if (voucher.ngayBatDau && new Date(voucher.ngayBatDau) > now) {
          discountError.value = 'Mã giảm giá chưa bắt đầu!'
          return
        }
        if (voucher.ngayKetThuc && new Date(voucher.ngayKetThuc) < now) {
          discountError.value = 'Mã giảm giá đã hết hạn!'
          return
        }
        if (voucher.soLuong !== null && voucher.soLuong <= 0) {
          discountError.value = 'Mã giảm giá đã hết lượt sử dụng!'
          return
        }
        if (voucher.giaTriDonHangToiThieu && cartTotal.value < voucher.giaTriDonHangToiThieu) {
          discountError.value = `Đơn hàng phải từ ${formatPrice(voucher.giaTriDonHangToiThieu)} mới được áp dụng mã này!`
          return
        }
        discountInfo.value = voucher
        discountError.value = ''
      } catch (e) {
        discountError.value = 'Có lỗi khi kiểm tra mã giảm giá!'
      }
    }

    const isFormValid = computed(() => {
      return customerInfo.value.fullName.trim() !== '' &&
             customerInfo.value.phone.trim() !== '' &&
             /^[0-9]{10,11}$/.test(customerInfo.value.phone.replace(/\s/g, '')) &&
             customerInfo.value.email.trim() !== '' &&
             shippingAddress.value.address.trim() !== '' &&
             shippingAddress.value.city !== '' &&
             shippingAddress.value.district !== '' &&
             shippingAddress.value.ward !== ''
    })

    const formatPrice = (price) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(price)
    }

    const placeOrder = async () => {
      if (!isFormValid.value) return
      try {
        // 1. Tạo hóa đơn mới (hoặc sử dụng hóa đơn đã tạo cho chuyển khoản)
        let hoaDonId
        if (createdHoaDonId.value) {
          // Đã tạo hóa đơn trước đó (cho chuyển khoản)
          hoaDonId = createdHoaDonId.value
        } else {
          // Tạo hóa đơn mới (cho COD)
          const res = await hoaDonService.createHoaDon({ loaiHoaDon: 'Online' })
          const hoaDon = res.data
          hoaDonId = hoaDon.id
          orderCode.value = hoaDon.maHoaDon // Lưu mã đơn hàng để dùng cho nội dung chuyển khoản
        }
        // 2. Thêm sản phẩm vào hóa đơn
        let hasValidItems = false
        for (const item of cartItems.value) {
          let chiTietSanPhamId = item.id // Fallback
          
          if (item.category === 'Bộ sách') {
            // Xử lý riêng cho bộ sách - lấy chiTietSanPhamId từ BoSachChiTiet
            try {
              const BoSachService = (await import('@/service/BoSachService')).default
              const chiTietList = await BoSachService.getBoSachChiTietByBoSachId(item.id)
              if (chiTietList && chiTietList.length > 0) {
                // Lấy chi tiết sản phẩm đầu tiên có tồn kho > 0
                const availableItem = chiTietList.find(chiTiet => 
                  chiTiet.idChiTietSanPham && chiTiet.idChiTietSanPham.soLuongTon > 0
                )
                if (availableItem && availableItem.idChiTietSanPham) {
                  chiTietSanPhamId = availableItem.idChiTietSanPham.id
                  hasValidItems = true
                } else {
                  // Nếu không có chi tiết nào có tồn kho, bỏ qua item này
                  console.warn('⚠️ Bỏ qua item:', item.title, '- không có tồn kho')
                  alert(`Sản phẩm "${item.title}" đã hết hàng và sẽ được bỏ qua khỏi đơn hàng.`)
                  continue
                }
              }
            } catch (error) {
              console.error('Lỗi khi lấy chi tiết bộ sách:', error)
              continue
            }
          } else {
            // Xử lý cho sản phẩm thường
            console.log('DEBUG: Processing regular product:', item.title, 'productDetailItems:', item.productDetailItems)
            if (item.productDetailItems && item.productDetailItems.length > 0) {
              const availableItem = item.productDetailItems.find(detail => detail.soLuongTon > 0)
              if (availableItem) {
                chiTietSanPhamId = availableItem.id
                hasValidItems = true
                console.log('DEBUG: Found available item with ID:', availableItem.id, 'soLuongTon:', availableItem.soLuongTon)
              } else {
                // Nếu không có chi tiết nào có tồn kho, bỏ qua item này
                console.warn('⚠️ Bỏ qua item:', item.title, '- không có tồn kho')
                alert(`Sản phẩm "${item.title}" đã hết hàng và sẽ được bỏ qua khỏi đơn hàng.`)
                continue
              }
            } else {
              // Nếu không có productDetailItems, cần kiểm tra tồn kho trước
              console.log('DEBUG: No productDetailItems, checking stock for item.id:', item.id)
              try {
                // Gọi API để kiểm tra tồn kho
                const ChiTietSanPhamService = (await import('@/service/ChiTietSanPhamService')).default
                const chiTietSanPham = await ChiTietSanPhamService.getChiTietSanPhamById(item.id)
                if (chiTietSanPham && chiTietSanPham.soLuongTon > 0) {
                  chiTietSanPhamId = item.id
                  hasValidItems = true
                  console.log('DEBUG: Item has stock, soLuongTon:', chiTietSanPham.soLuongTon)
                } else {
                  console.warn('⚠️ Bỏ qua item:', item.title, '- không có tồn kho (soLuongTon:', chiTietSanPham?.soLuongTon || 0, ')')
                  alert(`Sản phẩm "${item.title}" đã hết hàng và sẽ được bỏ qua khỏi đơn hàng.`)
                  continue
                }
              } catch (error) {
                console.error('Lỗi khi kiểm tra tồn kho:', error)
                continue
              }
            }
          }
          
          const productPayload = {
            chiTietSanPhamId: chiTietSanPhamId,
            soLuong: item.quantity,
            giaSanPham: item.price
          }
          console.log('addProductToOrder payload:', productPayload)
          console.log('item object:', item)
          try {
            await hoaDonService.addProductToOrder(hoaDonId, productPayload)
            console.log('✅ addProductToOrder thành công cho item:', item.title)
          } catch (error) {
            console.error('❌ addProductToOrder lỗi cho item:', item.title)
            console.error('Payload:', productPayload)
            console.error('Error:', error.response?.data || error.message)
            console.error('Error Response:', error.response)
            console.error('Error Status:', error.response?.status)
            
            // Nếu có lỗi, hủy hóa đơn và dừng toàn bộ quá trình
            try {
              await hoaDonService.huyDon(hoaDonId)
              console.log('✅ Đã hủy hóa đơn do lỗi thêm sản phẩm')
            } catch (cancelError) {
              console.error('❌ Lỗi khi hủy hóa đơn:', cancelError)
            }
            
            // Reset các biến
            orderCode.value = ''
            createdHoaDonId.value = null
            
            throw new Error(`Không thể thêm sản phẩm "${item.title}" vào đơn hàng. Lỗi: ${error.response?.data?.message || error.message}`)
          }
        }
        
        // Kiểm tra nếu không có items nào hợp lệ
        if (!hasValidItems) {
          alert('Tất cả sản phẩm trong giỏ hàng đều đã hết hàng. Không thể đặt đơn hàng.')
          return
        }
        // 3. Cập nhật thông tin khách nhận
        await hoaDonService.updateCustomerInfo(hoaDonId, {
          tenNguoiNhan: customerInfo.value.fullName,
          soDienThoaiNguoiNhan: customerInfo.value.phone,
          diaChiGiaoHang: `${shippingAddress.value.address}, ${getWardName()}, ${getDistrictName()}, ${getProvinceName()}`
        })
        // 3.5. Cập nhật phí ship và tổng tiền
        await hoaDonService.updateHoaDon(hoaDonId, {
          phiShip: shippingFee.value,
          tongTien: finalTotal.value
        })
        // 4. Cập nhật thanh toán
        const paymentPayload = {
          tienMat: paymentMethod.value === 'cod' ? finalTotal.value : 0,
          chuyenKhoan: paymentMethod.value === 'bank' ? finalTotal.value : 0,
          phuongThucThanhToanId: paymentMethod.value === 'cod' ? 1 : 2, // 1: COD, 2: Bank
          ghiChu: ghiChu.value || '',
          loaiHoaDon: 'Online',
          tienKhachDua: paymentMethod.value === 'cod' ? finalTotal.value : 0,
          phieuGiamGiaId: discountInfo.value ? discountInfo.value.id : null
        }
        console.log('updatePayment payload:', paymentPayload)
        await hoaDonService.updatePayment(hoaDonId, paymentPayload)
        
        // 5. Cập nhật ngày đặt hàng
        const updateData = {
          ngayDatHang: new Date().toISOString()
        }
        await hoaDonService.updateHoaDon(hoaDonId, updateData)
        // 5. Clear cart, thông báo, chuyển hướng
        alert('Đặt hàng thành công! Mã đơn: ' + orderCode.value)
        store.clearCart()
        router.push('/')
      } catch (e) {
        alert('Có lỗi khi đặt hàng: ' + (e.response?.data?.message || e.message))
      }
    }

    const showBankQR = ref(false)
    const orderCode = ref('') // Lưu mã đơn hàng
    const createdHoaDonId = ref(null) // Lưu ID hóa đơn đã tạo (cho chuyển khoản)
    const bankQRUrl = computed(() =>
      `https://img.vietqr.io/image/VCB-1041261134-compact2.png?amount=${transferAmount.value}&addInfo=${encodeURIComponent(transferContent.value)}`
    )
    const bankAccount = ref('1041261134')
    const bankName = ref('LE AN BINH - PGD Việt Hưng')
    const transferContent = computed(() => {
      if (orderCode.value) {
        return `BBOOK-${orderCode.value}`
      }
      return `BBOOK-${customerInfo.value.phone || 'SODT'}-${Math.floor(Math.random()*10000)}`
    })
    const transferAmount = computed(() => finalTotal.value)
    const transferConfirmed = ref(false)

    const onPlaceOrder = async () => {
      if (!isFormValid.value) return
      if (paymentMethod.value === 'bank') {
        // Tạo hóa đơn trước để có mã đơn hàng
        try {
          const res = await hoaDonService.createHoaDon({ loaiHoaDon: 'Online' })
          const hoaDon = res.data
          orderCode.value = hoaDon.maHoaDon // Lưu mã đơn hàng để dùng cho nội dung chuyển khoản
          createdHoaDonId.value = hoaDon.id // Lưu ID hóa đơn để dùng sau
          showBankQR.value = true
          transferConfirmed.value = false
        } catch (error) {
          // Reset các biến nếu có lỗi
          orderCode.value = ''
          createdHoaDonId.value = null
          alert('Có lỗi khi tạo hóa đơn: ' + (error.response?.data?.message || error.message))
        }
        return
      }
      // COD: đặt hàng luôn
      placeOrder()
    }
    const confirmBankTransfer = () => {
      transferConfirmed.value = true
      showBankQR.value = false
      placeOrder()
    }

    // Hàm lấy tên địa chỉ từ mã
    function getProvinceName() {
      const p = provinces.value.find(p => p.code == shippingAddress.value.city)
      return p ? p.name : ''
    }
    function getDistrictName() {
      const d = districts.value.find(d => d.code == shippingAddress.value.district)
      return d ? d.name : ''
    }
    function getWardName() {
      const w = wards.value.find(w => w.code == shippingAddress.value.ward)
      return w ? w.name : ''
    }

    return {
      customerInfo,
      ghiChu,
      shippingAddress,
      paymentMethod,
      cartItems,
      cartTotal,
      isFormValid,
      formatPrice,
      placeOrder,
      // Discount
      discountCode,
      discountPercent,
      discountError,
      discountAmount,
      finalTotal,
      applyDiscount,
      discountInfo,
      // Địa chỉ động
      provinces,
      districts,
      wards,
      loadingProvinces,
      loadingDistricts,
      loadingWards,
      shippingFee,
      shippingFeeLoading,
      shippingFeeError,
      shippingServices,
      selectedServiceIndex,
      showBankQR,
      bankQRUrl,
      bankAccount,
      bankName,
      transferContent,
      transferAmount,
      transferConfirmed,
      orderCode,
      createdHoaDonId,
      onPlaceOrder,
      confirmBankTransfer,
    }
  }
}
</script>