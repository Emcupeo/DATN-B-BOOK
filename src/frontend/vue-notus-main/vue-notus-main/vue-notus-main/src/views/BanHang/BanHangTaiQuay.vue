<template>
  <div class="w-full p-4">
    <!-- Thanh tab hóa đơn -->
    <div class="w-full mb-6">
      <div class="bg-white border border-gray-200 rounded-lg shadow-md p-4">
        <h5 class="text-lg font-bold text-gray-900 mb-4">Quản lý bán hàng</h5>
        <div class="flex flex-wrap border-b">
          <button v-for="(order, index) in orders" :key="order.id"
                  :class="['px-4 py-2 text-sm font-medium mr-2 mb-2 rounded-t-lg flex items-center space-x-2', { 'bg-blue-100 border border-blue-500 text-blue-700': selectedOrderIndex === index, 'bg-gray-200 text-gray-600 hover:bg-gray-300': selectedOrderIndex !== index }]"
                  @click="selectedOrderIndex = index">
            <span>Hóa đơn #{{ order.maHoaDon || index + 1 }}</span>
          </button>
          <button @click="createNewInvoice" class="ml-auto bg-green-600 text-white text-sm px-4 py-2 rounded-lg hover:bg-green-700 transition">
            Tạo hóa đơn mới
          </button>
        </div>
      </div>
    </div>

    <!-- Nội dung chính -->
    <div class="flex flex-col lg:flex-row gap-6">
      <!-- Phần sản phẩm -->
      <div class="w-full lg:w-2/3 bg-white shadow-md rounded-lg p-4 border">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-semibold text-gray-900">Danh sách sản phẩm</h3>
          <div class="flex space-x-2">
            <button class="border border-gray-300 text-sm px-3 py-1 rounded-lg bg-white hover:bg-gray-100">
              Quét mã QR
            </button>
            <button v-if="selectedOrder && selectedOrder.trangThai === 'Tạo hóa đơn'" @click="openAddProductModal" class="bg-blue-500 text-white text-sm px-3 py-1 rounded-lg hover:bg-blue-600">
              Thêm sản phẩm
            </button>
          </div>
        </div>

        <!-- Bảng sản phẩm -->
        <div class="overflow-x-auto">
          <table class="w-full text-sm text-left text-gray-500">
            <thead class="text-xs text-gray-700 uppercase bg-gray-50">
            <tr>
              <th class="px-4 py-2">Loại</th>
              <th class="px-4 py-2">Tên sản phẩm</th>
              <th class="px-4 py-2">Số lượng</th>
              <th class="px-4 py-2">Kho</th>
              <th class="px-4 py-2">Giá đã giảm</th>
              <th class="px-4 py-2">Giá chưa giảm</th>
              <th class="px-4 py-2">Tổng tiền</th>
              <th class="px-4 py-2">Hành động</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="prod in paginatedProducts" :key="prod.id" class="bg-white border-b">
              <td class="px-4 py-2">
                <span class="px-2 py-1 text-xs rounded-full" :class="prod.boSach ? 'bg-blue-100 text-blue-800' : 'bg-green-100 text-green-800'">
                  {{ prod.boSach ? 'Bộ sách' : 'Sách lẻ' }}
                </span>
              </td>
              <td class="px-4 py-2 font-medium">
                {{ prod.boSach?.tenBoSach || prod.chiTietSanPham?.tenChiTietSanPham || 'Không xác định' }}
              </td>
              <td class="px-4 py-2">
                <input
                    type="number"
                    v-model="prod.soLuong"
                    min="1"
                    :max="prod.boSach?.soLuong || prod.chiTietSanPham?.soLuongTon || 0"
                    @change="updateProductQuantity(prod)"
                    class="w-16 px-2 py-1 text-sm border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </td>
              <td class="px-4 py-2">{{ prod.boSach?.soLuong || prod.chiTietSanPham?.soLuongTon || 0 }}</td>
              <!-- Giá đã giảm -->
              <td class="px-4 py-2">
                <span v-if="hasDiscount(prod)" class="text-red-500 font-semibold">{{ formatCurrency(prod.giaSanPham) }}</span>
                <span v-else class="text-gray-900 font-semibold">{{ formatCurrency(prod.giaSanPham) }}</span>
              </td>
              <!-- Giá chưa giảm -->
              <td class="px-4 py-2">
                <span v-if="hasDiscount(prod)" class="text-gray-500 line-through">{{ formatCurrency(getOriginalPrice(prod)) }}</span>
                <span v-else class="text-gray-500">{{ formatCurrency(prod.giaSanPham) }}</span>
              </td>
              <td class="px-4 py-2 font-semibold">{{ formatCurrency(prod.thanhTien || prod.soLuong * prod.giaSanPham) }}</td>
              <td class="px-4 py-2">
                <div class="flex space-x-2">
                  <!-- Nút Chi tiết cho bộ sách -->
                  <button
                      v-if="prod.boSach"
                      @click="openBookSetDetailModal(prod.boSach)"
                      class="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded text-xs font-medium transition-colors duration-200"
                      title="Xem chi tiết bộ sách"
                  >
                    <i class="fas fa-info-circle mr-1"></i>
                    Chi tiết
                  </button>

                  <!-- Nút Xóa sản phẩm -->
                  <button
                      v-if="selectedOrder && (selectedOrder.trangThai === 'Tạo hóa đơn' || selectedOrder.trangThai === 'Chờ xác nhận')"
                      @click="removeItem(prod.id)"
                      class="bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded text-xs font-medium transition-colors duration-200"
                      title="Xóa sản phẩm khỏi hóa đơn"
                  >
                    <i class="fas fa-trash-alt mr-1"></i>
                    Xóa
                  </button>

                  <!-- Debug info -->
                  <span v-if="selectedOrder" class="text-xs text-gray-400 ml-2">
                    ({{ selectedOrder.trangThai }})
                  </span>
                </div>
              </td>
            </tr>

            <!-- Chi tiết bộ sách (nếu đang expand) -->
            <tr v-for="prod in expandedBookSetDetails" :key="`detail-${prod.id}`" class="bg-gray-50">
              <td colspan="8" class="px-4 py-2">
                <div class="ml-4 space-y-1">
                  <div v-for="bookDetail in bookSetDetails[prod.boSach.id]" :key="bookDetail.id" class="text-sm text-gray-600">
                    {{ bookDetail.tenChiTietSanPham }} | {{ bookDetail.idTacGia?.tenTacGia || 'Không có tác giả' }} | Kho: {{ bookDetail.soLuongTon }} | Giá: {{ formatCurrency(bookDetail.gia) }}
                  </div>
                </div>
              </td>
            </tr>

            <tr v-if="paginatedProducts.length === 0">
              <td colspan="8" class="px-4 py-2 text-center text-gray-500">
                Chưa có sản phẩm
              </td>
            </tr>
            </tbody>
          </table>
        </div>

        <!-- Phân trang -->
        <div class="mt-4 flex justify-between items-center">
          <button class="bg-gray-200 text-gray-700 text-sm px-3 py-1 rounded-lg hover:bg-gray-300">
            Prev
          </button>
          <span class="text-sm">Trang 1 / 1</span>
          <button class="bg-gray-200 text-gray-700 text-sm px-3 py-1 rounded-lg hover:bg-gray-300">
            Next
          </button>
        </div>
      </div>

      <!-- Phần thông tin khách hàng & thanh toán -->
      <div class="w-full lg:w-1/3 bg-white shadow-md rounded-lg p-4 border">
        <div class="mb-6">
          <h2 class="text-lg font-semibold text-gray-900 mb-4">Thông tin khách hàng</h2>
          <div class="text-sm text-gray-600 mb-4">
            <p>{{ selectedOrder?.tenNguoiNhan || 'Khách lẻ' }}</p>
          </div>
          <div class="flex space-x-2">
            <button @click="openSelectCustomerModal" class="bg-blue-500 text-white text-sm px-3 py-1 rounded-lg hover:bg-blue-600">
              Chọn khách hàng
            </button>
            <button @click="openAddCustomerModal" class="bg-green-500 text-white text-sm px-3 py-1 rounded-lg hover:bg-green-600">
              Thêm mới khách hàng
            </button>
            <button v-if="selectedOrder?.idKhachHang" @click="removeCustomer" class="bg-red-500 text-white text-sm px-3 py-1 rounded-lg hover:bg-red-600">
              Bỏ chọn khách hàng
            </button>
          </div>
        </div>

        <div class="mb-6">
          <h3 class="text-md font-semibold text-gray-900 mb-2">Hình thức nhận hàng</h3>
          <div class="space-y-2">
            <label class="flex items-center space-x-2">
              <input type="radio" value="TaiQuay" v-model="deliveryMethod" class="text-sm" />
              <span class="text-sm">Tại quầy</span>
            </label>
            <label class="flex items-center space-x-2">
              <input type="radio" value="GiaoHang" v-model="deliveryMethod" class="text-sm" />
              <span class="text-sm">Giao hàng</span>
            </label>
          </div>
          <div class="mt-2 p-2 border rounded" :class="{ 'hidden': deliveryMethod !== 'GiaoHang' }">
            <label class="block text-sm mb-1">Địa chỉ giao hàng</label>
            <label class="flex items-center gap-2 mb-2 text-sm">
              <input type="checkbox" v-model="useDefaultAddress" /> Sử dụng địa chỉ mặc định của khách hàng (nếu có)
            </label>
            <input type="text" v-model="selectedOrder.diaChi" class="w-full border rounded px-2 py-1 text-sm" />
            <label class="block text-sm mt-2 mb-1">SĐT người nhận</label>
            <input type="text" v-model="selectedOrder.soDienThoaiNguoiNhan" class="w-full border rounded px-2 py-1 text-sm" />
          </div>
        </div>

        <div class="mb-6">
          <h3 class="text-md font-semibold text-gray-900 mb-2">Hình thức thanh toán</h3>
          <div class="space-y-2">
            <!-- Tại quầy: Chỉ Tiền mặt -->
            <template v-if="deliveryMethod === 'TaiQuay'">
              <label class="flex items-center space-x-2 p-2 rounded-lg border-2 transition-all border-green-500 bg-green-50">
                <input type="radio" value="4" v-model="paymentMethod" class="text-sm" checked />
                <i class="fas fa-money-bill-wave text-green-600"></i>
                <span class="text-sm font-medium">Tiền mặt</span>
                <span class="px-2 py-1 text-xs bg-green-100 text-green-800 rounded-full">TIỀN MẶT</span>
              </label>
            </template>

            <!-- Giao hàng: COD và Tiền mặt -->
            <template v-if="deliveryMethod === 'GiaoHang'">
              <label class="flex items-center space-x-2 p-2 rounded-lg border-2 transition-all"
                     :class="paymentMethod === 'cod' ? 'border-orange-500 bg-orange-50' : 'border-gray-200 hover:border-orange-300'">
                <input type="radio" value="cod" v-model="paymentMethod" class="text-sm" />
                <i class="fas fa-truck text-orange-600"></i>
                <span class="text-sm font-medium">Thanh toán khi nhận hàng (COD)</span>
                <span class="px-2 py-1 text-xs bg-orange-100 text-orange-800 rounded-full">COD</span>
              </label>
              <label class="flex items-center space-x-2 p-2 rounded-lg border-2 transition-all"
                     :class="paymentMethod === '4' ? 'border-green-500 bg-green-50' : 'border-gray-200 hover:border-green-300'">
                <input type="radio" value="4" v-model="paymentMethod" class="text-sm" />
                <i class="fas fa-money-bill-wave text-green-600"></i>
                <span class="text-sm font-medium">Tiền mặt</span>
                <span class="px-2 py-1 text-xs bg-green-100 text-green-800 rounded-full">TIỀN MẶT</span>
              </label>
            </template>
          </div>

          <!-- QR Code cho chuyển khoản -->
          <div class="mt-2" :class="{ 'hidden': paymentMethod !== '1' }">
            <div class="w-32 h-32 mx-auto bg-gray-200 border-2 border-dashed border-gray-400 flex items-center justify-center">
              <div class="text-center text-gray-500">
                <svg class="w-8 h-8 mx-auto mb-2" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M4 4a2 2 0 00-2 2v8a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2H4zm2 6a2 2 0 114 0 2 2 0 01-4 0zm6 0a2 2 0 114 0 2 2 0 01-4 0z" clip-rule="evenodd"></path>
                </svg>
                <span class="text-xs">QR Code</span>
              </div>
            </div>
            <p class="text-center text-sm text-gray-500 mt-1">Quét mã để thanh toán</p>
          </div>

          <!-- Tiền khách đưa hiển thị khi thanh toán tiền mặt (tại quầy hoặc giao hàng) -->
          <div v-if="paymentMethod === '4'" class="mt-4">
            <div class="flex justify-between mb-2">
              <label class="block text-sm font-medium">Tiền khách đưa:</label>
              <input type="number" v-model.number="tienKhachDua" class="w-1/2 border rounded px-2 py-1 text-sm" placeholder="Nhập số tiền" min="0" />
            </div>
            <div class="flex justify-between mb-2">
              <span class="text-gray-600 text-sm">Tiền trả khách:</span>
              <span class="font-medium text-sm" :class="{ 'text-red-500': tienTraKhach < 0 }">{{ formatCurrency(tienTraKhach) }}</span>
            </div>
          </div>
        </div>

        <div class="border-t pt-4">
          <!-- Hiển thị phương thức thanh toán nếu đã có -->
          <div v-if="selectedOrder?.phuongThucThanhToanId" class="flex justify-between items-center mb-3 p-2 rounded-lg"
               :class="selectedOrder.phuongThucThanhToanId === 1 ? 'bg-blue-50 border border-blue-200' :
                       selectedOrder.phuongThucThanhToanId === 4 ? 'bg-green-50 border border-green-200' : 
                       'bg-orange-50 border border-orange-200'">
            <span class="text-gray-600 text-sm font-medium">Phương thức thanh toán:</span>
            <div class="flex items-center space-x-2">
              <i :class="selectedOrder.phuongThucThanhToanId === 1 ? 'fas fa-credit-card text-blue-600' :
                        selectedOrder.phuongThucThanhToanId === 4 ? 'fas fa-money-bill-wave text-green-600' : 
                        'fas fa-truck text-orange-600'"></i>
              <span :class="['text-sm font-semibold',
                selectedOrder.phuongThucThanhToanId === 1 ? 'text-blue-700' :
                selectedOrder.phuongThucThanhToanId === 4 ? 'text-green-700' : 
                'text-orange-700']">
                {{ selectedOrder.phuongThucThanhToanId === 1 ? 'Chuyển khoản' :
                  selectedOrder.phuongThucThanhToanId === 4 ? 'Tiền mặt' :
                      'COD' }}
              </span>
              <span :class="['px-2 py-1 text-xs rounded-full font-bold',
                selectedOrder.phuongThucThanhToanId === 1 ? 'bg-blue-100 text-blue-800' :
                selectedOrder.phuongThucThanhToanId === 4 ? 'bg-green-100 text-green-800' : 
                'bg-orange-100 text-orange-800']">
                {{ selectedOrder.phuongThucThanhToanId === 1 ? 'CHUYỂN KHOẢN' :
                  selectedOrder.phuongThucThanhToanId === 4 ? 'TIỀN MẶT' :
                      'COD' }}
              </span>
            </div>
          </div>

          <div class="flex justify-between mb-2">
            <span class="text-gray-600 text-sm">Tạm tính:</span>
            <span class="font-medium text-sm">{{ formatCurrency(tongTienHang) }}</span>
          </div>
          <div class="flex justify-between mb-2">
            <span class="text-gray-600 text-sm">Phí vận chuyển:</span>
            <span class="font-medium text-sm">{{ formatCurrency(selectedOrder?.phiShip || 0) }}</span>
          </div>
          <div class="flex justify-between mb-2">
            <span class="text-gray-600 text-sm">Giảm giá:</span>
            <span class="font-medium text-sm">{{ formatCurrency(tienGiamGia) }}</span>
          </div>
          <div class="flex justify-between text-lg font-bold mt-2">
            <span>Tổng thanh toán:</span>
            <span>{{ formatCurrency(thanhTien) }}</span>
          </div>
          <div class="mt-4">
            <select v-model="selectedVoucher" @change="onVoucherChange" class="w-full border border-gray-300 rounded px-3 py-1 text-sm focus:outline-none focus:ring">
              <option value="">Chọn voucher...</option>
              <!-- Voucher cá nhân của khách hàng -->
              <optgroup v-if="personalVouchers.length > 0" label="Voucher cá nhân">
                <option v-for="voucher in personalVouchers" :key="'personal-' + voucher.id" :value="voucher">
                  🎁 {{ voucher.tenPhieuGiamGia }} - {{ getVoucherDisplayValue(voucher) }}
                </option>
              </optgroup>
              <!-- Voucher công khai -->
              <optgroup v-if="availableVouchers.length > 0" label="Voucher công khai">
                <option v-for="voucher in availableVouchers" :key="'public-' + voucher.id" :value="voucher">
                  {{ voucher.tenPhieuGiamGia }} - {{ getVoucherDisplayValue(voucher) }}
                </option>
              </optgroup>
            </select>
          </div>
        </div>

        <div class="mt-6 space-y-2">
          <button v-if="selectedOrder.trangThai === 'Tạo hóa đơn'" @click="xacNhanHoaDon" class="w-full bg-purple-600 text-white py-2 rounded-lg text-sm font-semibold hover:bg-purple-700 transition" :disabled="isConfirmDisabled">
            Xác nhận đơn hàng
          </button>

          <button v-if="selectedOrder.trangThai === 'Chờ xác nhận'" @click="openPaymentModal" class="w-full bg-green-600 text-white py-2 rounded-lg text-sm font-semibold hover:bg-green-700 transition">
            Xác nhận thanh toán
          </button>

          <button v-if="selectedOrder.trangThai === 'Tạo hóa đơn' || selectedOrder.trangThai === 'Chờ xác nhận'" @click="huyHoaDon" class="w-full bg-red-600 text-white py-2 rounded-lg text-sm font-semibold hover:bg-red-700 transition">
            Hủy hóa đơn
          </button>
        </div>
      </div>
    </div>

    <!-- Modal thêm sản phẩm -->
    <transition name="fade">
      <div v-if="showAddProductModal" class="fixed inset-0 z-10 flex items-center justify-center bg-gray-600 bg-opacity-50">
        <div class="bg-white p-6 z-10 rounded-lg shadow-lg w-3/4 max-h-[80vh] overflow-y-auto">
          <h3 class="text-lg font-semibold mb-4">Chọn chi tiết sản phẩm</h3>
          <form @submit.prevent="addProduct" class="space-y-4">
            <div class="flex justify-between items-center mb-4">
              <div class="w-1/2">
                <label class="block text-sm font-medium mb-1">Tìm kiếm</label>
                <input v-model="productSearchQuery" @input="searchProducts" type="text" class="w-full border rounded px-3 py-2" placeholder="Nhập mã hoặc tên sách...">
              </div>
              <div class="flex space-x-2">
                <button @click="showAllProducts" type="button" class="text-white bg-gradient-to-r from-purple-500 via-purple-600 to-purple-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-purple-300 shadow-lg shadow-purple-500/50 font-medium rounded-lg text-sm px-5 py-2.5">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 inline mr-2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 12h16.5m-16.5 3.75h16.5M3.75 9.75h16.5" />
                  </svg>
                  Hiển thị sản phẩm
                </button>
                <button @click="showAllBookSets" type="button" class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-5 py-2.5">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 inline mr-2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 6.042A8.967 8.967 0 006 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 016 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 016-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0018 18a8.967 8.967 0 00-6 2.292m0-14.25v14.25" />
                  </svg>
                  Hiển thị bộ sách
                </button>
              </div>
            </div>

            <div class="grid grid-cols-5 gap-4 mb-4">
              <div>
                <label class="block text-sm font-medium text-gray-700">Giá tối thiểu</label>
                <input v-model.number="filters.minPrice" type="number" min="0" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" placeholder="0" />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Giá tối đa</label>
                <input v-model.number="filters.maxPrice" type="number" min="0" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" placeholder="∞" />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Loại bìa</label>
                <select v-model="filters.idLoaiBia" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                  <option :value="null">Tất cả</option>
                  <option v-for="item in loaiBiaList" :key="item.id" :value="item.id">{{ item.tenLoaiBia }}</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Tác giả</label>
                <select v-model="filters.idTacGia" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                  <option :value="null">Tất cả</option>
                  <option v-for="item in tacGiaList" :key="item.id" :value="item.id">{{ item.tenTacGia }}</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Nhà xuất bản</label>
                <select v-model="filters.idNhaXuatBan" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                  <option :value="null">Tất cả</option>
                  <option v-for="item in nhaXuatBanList" :key="item.id" :value="item.id">{{ item.tenNhaXuatBan }}</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Chất liệu</label>
                <select v-model="filters.idChatLieu" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                  <option :value="null">Tất cả</option>
                  <option v-for="item in chatLieuList" :key="item.id" :value="item.id">{{ item.tenChatLieu }}</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Người dịch</label>
                <select v-model="filters.idNguoiDich" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                  <option :value="null">Tất cả</option>
                  <option v-for="item in nguoiDichList" :key="item.id" :value="item.id">{{ item.tenNguoiDich }}</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Thể loại</label>
                <select v-model="filters.idTheLoai" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                  <option :value="null">Tất cả</option>
                  <option v-for="item in theLoaiList" :key="item.id" :value="item.id">{{ item.tenTheLoai }}</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Ngôn ngữ</label>
                <select v-model="filters.idNgonNgu" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
                  <option :value="null">Tất cả</option>
                  <option v-for="item in ngonNguList" :key="item.id" :value="item.id">{{ item.tenNgonNgu }}</option>
                </select>
              </div>
            </div>

            <!-- Bảng hiển thị sản phẩm -->
            <div v-if="filteredProducts.length && !isShowingBookSets" class="max-h-64 overflow-y-auto">
              <table class="w-full text-sm text-left text-gray-500">
                <thead class="text-xs text-gray-700 uppercase bg-gray-50">
                <tr>
                  <th class="px-4 py-3">Ảnh</th>
                  <th class="px-4 py-3">Tên</th>
                  <th class="px-4 py-3">Giá</th>
                  <th class="px-4 py-3">Mã</th>
                  <th class="px-4 py-3">Tác giả</th>
                  <th class="px-4 py-3">Số lượng tồn</th>
                  <th class="px-4 py-3">Chọn</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="product in filteredProducts" :key="product.id" class="bg-white border-b hover:bg-gray-50">
                  <td class="px-4 py-3">
                    <div class="relative w-12 h-12">
                      <div v-if="product.anhSanPhams && product.anhSanPhams.length > 0" class="image-container">
                        <img v-for="(image, imgIndex) in product.anhSanPhams" :key="image.id" :src="image.url" class="w-full h-full object-cover absolute top-0 left-0" :class="{'image-active': imgIndex === currentImageIndex[product.id], 'image-inactive': imgIndex !== currentImageIndex[product.id]}" alt="Sản phẩm" />
                      </div>
                      <span v-else class="text-gray-500">Không có ảnh</span>
                    </div>
                  </td>
                  <td class="px-4 py-3 font-medium">{{ product.tenChiTietSanPham }}</td>
                  <td class="px-4 py-3 text-red-500">{{ formatCurrency(product.gia) }}</td>
                  <td class="px-4 py-3 text-gray-500">{{ product.maChiTietSanPham }}</td>
                  <td class="px-4 py-3 text-gray-500">{{ product.idTacGia?.tenTacGia || 'Không có' }}</td>
                  <td class="px-4 py-3 text-gray-500">{{ product.soLuongTon }}</td>
                  <td class="px-4 py-3">
                    <button @click="openConfirmationModal(product, 'product')" type="button" class="text-blue-500">Chọn</button>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>

            <!-- Bảng hiển thị bộ sách -->
            <div v-if="filteredBookSets.length && isShowingBookSets" class="max-h-64 overflow-y-auto">
              <table class="w-full text-sm text-left text-gray-500">
                <thead class="text-xs text-gray-700 uppercase bg-gray-50">
                <tr>
                  <th class="px-4 py-3">Ảnh</th>
                  <th class="px-4 py-3">Tên bộ sách</th>
                  <th class="px-4 py-3">Giá</th>
                  <th class="px-4 py-3">Mã bộ sách</th>
                  <th class="px-4 py-3">Số lượng tồn</th>
                  <th class="px-4 py-3">Chọn</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="bookSet in filteredBookSets" :key="bookSet.id" class="bg-white border-b hover:bg-gray-50">
                  <td class="px-4 py-3">
                    <div class="relative w-12 h-12">
                      <img v-if="bookSet.url" :src="bookSet.url" class="w-full h-full object-cover rounded" alt="Bộ sách" />
                      <div v-else class="w-full h-full bg-gray-200 rounded flex items-center justify-center">
                        <svg class="w-6 h-6 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                          <path fill-rule="evenodd" d="M4 3a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V5a2 2 0 00-2-2H4zm12 12H4l4-8 3 6 2-4 3 6z" clip-rule="evenodd"></path>
                        </svg>
                      </div>
                    </div>
                  </td>
                  <td class="px-4 py-3 font-medium">{{ bookSet.tenBoSach }}</td>
                  <td class="px-4 py-3 text-red-500">{{ formatCurrency(bookSet.giaTien) }}</td>
                  <td class="px-4 py-3 text-gray-500">{{ bookSet.maBoSach }}</td>
                  <td class="px-4 py-3 text-gray-500">{{ bookSet.soLuong }}</td>
                  <td class="px-4 py-3">
                    <button @click="openConfirmationModal(bookSet, 'bookset')" type="button" class="text-blue-500">Chọn</button>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
            <p v-else-if="(productSearchQuery || allProducts.length) && !isShowingBookSets" class="text-gray-500">Không tìm thấy sản phẩm phù hợp.</p>
            <p v-else-if="(productSearchQuery || allBookSets.length) && isShowingBookSets" class="text-gray-500">Không tìm thấy bộ sách phù hợp.</p>
            <p v-else class="text-gray-500">Vui lòng tìm kiếm hoặc nhấn "Hiển thị sản phẩm"/"Hiển thị bộ sách" để xem danh sách.</p>

            <div v-if="selectedProduct" class="mt-4">
              <p class="font-medium">Sản phẩm đã chọn: {{ selectedProduct.tenChiTietSanPham }}</p>
              <div class="flex items-center mt-2">
                <label class="block text-sm font-medium mr-2">Số lượng</label>
                <input v-model.number="selectedProductQuantity" type="number" min="1" :max="selectedProduct.soLuongTon" class="w-20 border rounded px-3 py-2" required>
              </div>
            </div>

            <div class="flex justify-end gap-2 mt-4">
              <button type="button" @click="closeAddProductModal" class="bg-red-500 text-white px-4 py-2 rounded">Hủy</button>
              <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded" :disabled="!selectedProduct">Thêm</button>
            </div>
          </form>
        </div>
      </div>
    </transition>

    <!-- Modal chọn khách hàng -->
    <transition name="fade">
      <div v-if="showSelectCustomerModal" class="fixed inset-0 z-50 flex items-center justify-center bg-gray-600 bg-opacity-50">
        <div class="bg-white p-6 rounded-lg shadow-lg w-3/4 max-h-[80vh] overflow-y-auto">
          <h3 class="text-lg font-semibold mb-4">Chọn khách hàng</h3>
          <div class="mb-4">
            <input v-model="customerSearchQuery" @input="searchCustomers" type="text" placeholder="Tìm kiếm khách hàng..." class="w-full border rounded px-3 py-2">
          </div>
          <div class="max-h-64 overflow-y-auto">
            <table class="w-full text-sm text-left text-gray-500">
              <thead class="text-xs text-gray-700 uppercase bg-gray-50">
              <tr>
                <th class="px-4 py-3">Mã KH</th>
                <th class="px-4 py-3">Họ tên</th>
                <th class="px-4 py-3">Email</th>
                <th class="px-4 py-3">Số điện thoại</th>
                <th class="px-4 py-3">Địa chỉ</th>
                <th class="px-4 py-3">Chọn</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="customer in filteredCustomers" :key="customer.id" class="bg-white border-b hover:bg-gray-50">
                <td class="px-4 py-3">{{ customer.maKhachHang }}</td>
                <td class="px-4 py-3">{{ customer.hoTen }}</td>
                <td class="px-4 py-3">{{ customer.email }}</td>
                <td class="px-4 py-3">{{ customer.soDienThoai }}</td>
                <td class="px-4 py-3">{{ formatDiaChi(customer) }}</td>
                <td class="px-4 py-3">
                  <button @click="selectCustomer(customer)" class="text-blue-500">Chọn</button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
          <div class="flex justify-end gap-2 mt-4">
            <button @click="closeSelectCustomerModal" class="bg-red-500 text-white px-4 py-2 rounded">Hủy</button>
          </div>
        </div>
      </div>
    </transition>

    <!-- Modal thêm khách hàng -->
    <transition name="fade">
      <div v-if="showAddCustomerModal" class="fixed inset-0 z-50 flex items-center justify-center bg-gray-600 bg-opacity-50">
        <div class="bg-white p-6 rounded-lg shadow-lg w-1/2 max-h-[80vh] overflow-y-auto">
          <h2 class="text-xl font-bold mb-4">Thêm khách hàng</h2>
          <form @submit.prevent="saveCustomer" class="space-y-4">
            <div>
              <label class="block text-sm font-medium mb-1">Họ tên</label>
              <input v-model="newCustomer.hoTen" type="text" class="w-full border rounded px-3 py-2" required>
            </div>
            <div>
              <label class="block text-sm font-medium mb-1">Email</label>
              <input v-model="newCustomer.email" type="email" class="w-full border rounded px-3 py-2" required>
            </div>
            <div>
              <label class="block text-sm font-medium mb-1">Số điện thoại</label>
              <input v-model="newCustomer.soDienThoai" type="text" class="w-full border rounded px-3 py-2" pattern="[0-9]{10}" title="Số điện thoại phải có 10 chữ số" required>
            </div>
            <div>
              <label class="block text-sm font-medium mb-1">Ngày sinh</label>
              <input v-model="newCustomer.ngaySinh" type="date" class="w-full border rounded px-3 py-2" required>
            </div>
            <div>
              <label class="block text-sm font-medium mb-1">Giới tính</label>
              <select v-model="newCustomer.gioiTinh" class="w-full border rounded px-3 py-2">
                <option :value="true">Nam</option>
                <option :value="false">Nữ</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium mb-1">Trạng thái</label>
              <select v-model="newCustomer.trangThai" class="w-full border rounded px-3 py-2">
                <option :value="true">Hoạt động</option>
                <option :value="false">Không hoạt động</option>
              </select>
            </div>
            <div class="flex justify-end gap-2 mt-4">
              <button type="button" @click="closeAddCustomerModal" class="bg-red-500 text-white px-4 py-2 rounded">Hủy</button>
              <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded">Thêm</button>
            </div>
          </form>
        </div>
      </div>
    </transition>

    <!-- Modal xác nhận chọn sản phẩm/bộ sách -->
    <transition name="fade">
      <div v-if="showConfirmationModal" class="fixed inset-0 z-20 flex items-center justify-center bg-gray-600 bg-opacity-50">
        <div class="bg-white p-6 z-20 rounded-lg shadow-lg w-96">
          <h3 class="text-lg font-semibold mb-4">Xác nhận thêm vào hóa đơn</h3>
          <div class="space-y-4">
            <div class="flex items-center space-x-4">
              <div class="w-16 h-16 bg-gray-200 rounded-lg flex items-center justify-center">
                <img v-if="confirmationItem && confirmationItem.anhSanPhams && confirmationItem.anhSanPhams.length > 0"
                     :src="confirmationItem.anhSanPhams[0].url"
                     class="w-full h-full object-cover rounded-lg"
                     alt="Sản phẩm">
                <svg v-else class="w-8 h-8 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M4 3a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V5a2 2 0 00-2-2H4zm12 12H4l4-8 3 6 2-4 3 6z" clip-rule="evenodd"></path>
                </svg>
              </div>
              <div class="flex-1">
                <h4 class="font-medium text-gray-900">{{ confirmationItem?.tenChiTietSanPham || confirmationItem?.tenBoSach }}</h4>
                <p class="text-sm text-gray-500">{{ confirmationItem?.maChiTietSanPham || confirmationItem?.maBoSach }}</p>
                <p class="text-lg font-semibold text-red-500">{{ formatCurrency(confirmationItem?.gia || confirmationItem?.giaTien) }}</p>
              </div>
            </div>
            <div>
              <label class="block text-sm font-medium mb-1">Số lượng</label>
              <input v-model.number="confirmationQuantity"
                     type="number"
                     min="1"
                     :max="confirmationItem?.soLuongTon || confirmationItem?.soLuong"
                     class="w-full border rounded px-3 py-2"
                     required>
              <p class="text-xs text-gray-500 mt-1">
                Tồn kho: {{ confirmationItem?.soLuongTon || confirmationItem?.soLuong }} sản phẩm
              </p>
            </div>
            <div class="flex justify-end gap-2 mt-4">
              <button @click="closeConfirmationModal" type="button" class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600">
                Hủy
              </button>
              <button @click="confirmAddToOrder" type="button" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                Xác nhận
              </button>
            </div>
          </div>
        </div>
      </div>
    </transition>

    <!-- Modal chi tiết bộ sách -->
    <transition name="fade">
      <div v-if="showBookSetDetailModal" class="fixed inset-0 z-30 flex items-center justify-center bg-gray-600 bg-opacity-50">
        <div class="bg-white p-6 z-30 rounded-lg shadow-lg w-2/3 max-h-[80vh] overflow-y-auto">
          <h3 class="text-lg font-semibold mb-4">Chi tiết bộ sách: {{ selectedBookSet?.tenBoSach }}</h3>
          <div class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700">Mã bộ sách</label>
                <p class="text-sm text-gray-900">{{ selectedBookSet?.maBoSach }}</p>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Giá bộ sách</label>
                <p class="text-sm text-gray-900 font-semibold text-red-500">{{ formatCurrency(selectedBookSet?.giaTien) }}</p>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Số lượng tồn</label>
                <p class="text-sm text-gray-900">{{ selectedBookSet?.soLuong }}</p>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Mô tả</label>
                <p class="text-sm text-gray-900">{{ selectedBookSet?.moTa || 'Không có mô tả' }}</p>
              </div>
            </div>

            <div>
              <h4 class="text-md font-medium mb-2">Danh sách sách trong bộ:</h4>
              <div class="overflow-x-auto">
                <table class="w-full text-sm text-left text-gray-500">
                  <thead class="text-xs text-gray-700 uppercase bg-gray-50">
                  <tr>
                    <th class="px-4 py-3">Tên sách</th>
                    <th class="px-4 py-3">Tác giả</th>
                    <th class="px-4 py-3">Số lượng trong bộ</th>
                    <th class="px-4 py-3">Kho</th>
                    <th class="px-4 py-3">Giá</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="bookDetail in bookSetDetails[selectedBookSet?.id] || []" :key="bookDetail.id?.idBoSach + '-' + bookDetail.id?.idChiTietSanPham" class="bg-white border-b hover:bg-gray-50">
                    <td class="px-4 py-3 font-medium">{{ bookDetail.idChiTietSanPham?.tenChiTietSanPham || 'Không xác định' }}</td>
                    <td class="px-4 py-3">{{ bookDetail.idChiTietSanPham?.idTacGia?.tenTacGia || 'Không có tác giả' }}</td>
                    <td class="px-4 py-3">{{ bookDetail.soLuong || 1 }}</td>
                    <td class="px-4 py-3">{{ bookDetail.idChiTietSanPham?.soLuongTon || 0 }}</td>
                    <td class="px-4 py-3 text-red-500">{{ formatCurrency(bookDetail.idChiTietSanPham?.gia || 0) }}</td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>

            <div class="flex justify-end gap-2 mt-4">
              <button @click="closeBookSetDetailModal" type="button" class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600">
                Đóng
              </button>
            </div>
          </div>
        </div>
      </div>
    </transition>

    <!-- Modal thanh toán -->
    <transition name="fade">
      <div v-if="showPaymentModal" class="fixed inset-0 z-10 flex items-center justify-center bg-gray-600 bg-opacity-50">
        <div class="bg-white rounded-lg shadow-xl max-w-md w-full mx-4">
          <div class="p-6">
            <h3 class="text-lg font-semibold text-gray-900 mb-4">Xác nhận thanh toán</h3>

            <!-- Tổng tiền cần thanh toán -->
            <div class="mb-4">
              <p class="text-sm text-gray-600 mb-2">Tổng tiền cần thanh toán:</p>
              <p class="text-xl font-bold text-red-600">{{ formatCurrency(thanhTien) }}</p>
            </div>

            <!-- Hình thức thanh toán (chỉ hiển thị) -->
            <div class="mb-4">
              <p class="text-sm text-gray-600 mb-2">Hình thức thanh toán:</p>
              <div class="flex items-center space-x-2">
                <i :class="paymentMethod === 'cod' ? 'fas fa-truck text-orange-600' :
                          paymentMethod === '4' ? 'fas fa-money-bill-wave text-green-600' : 
                          'fas fa-credit-card text-blue-600'"></i>
                <span class="text-sm font-medium text-gray-900">
                  {{ paymentMethod === 'cod' ? 'Thanh toán khi nhận hàng (COD)' :
                    paymentMethod === '4' ? 'Tiền mặt' : 'Chuyển khoản' }}
                </span>
                <span :class="['px-2 py-1 text-xs rounded-full font-semibold',
                  paymentMethod === 'cod' ? 'bg-orange-100 text-orange-800' :
                  paymentMethod === '4' ? 'bg-green-100 text-green-800' : 
                  'bg-blue-100 text-blue-800']">
                  {{ paymentMethod === 'cod' ? 'COD' :
                    paymentMethod === '4' ? 'TIỀN MẶT' : 'CHUYỂN KHOẢN' }}
                </span>
              </div>
            </div>

            <!-- Tiền khách đưa (chỉ hiển thị khi thanh toán tiền mặt) -->
            <div v-if="paymentMethod === '4'" class="mb-4">
              <p class="text-sm text-gray-600 mb-2">Tiền khách đưa:</p>
              <p class="text-sm font-medium text-gray-900">{{ formatCurrency(tienKhachDua) }}</p>
            </div>

            <!-- Tiền trả lại khách (chỉ hiển thị khi thanh toán tiền mặt) -->
            <div v-if="paymentMethod === '4'" class="mb-4">
              <p class="text-sm text-gray-600 mb-2">Tiền trả lại khách:</p>
              <p class="text-sm font-medium" :class="tienTraKhach >= 0 ? 'text-green-600' : 'text-red-600'">
                {{ formatCurrency(tienTraKhach) }}
              </p>
            </div>

            <!-- Ghi chú (chỉ hiển thị nếu có) -->
            <div v-if="paymentNote" class="mb-4">
              <p class="text-sm text-gray-600 mb-2">Ghi chú:</p>
              <p class="text-sm font-medium text-gray-900">{{ paymentNote }}</p>
            </div>

            <!-- Thông báo lỗi nếu tiền không đủ -->
            <div v-if="paymentMethod === '4' && tienTraKhach < 0" class="mb-4 p-3 bg-red-50 border border-red-200 rounded">
              <p class="text-sm text-red-600">⚠️ Số tiền khách đưa không đủ để thanh toán!</p>
            </div>

            <div class="flex justify-end gap-2">
              <button
                  @click="showPaymentModal = false"
                  type="button"
                  class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600 transition"
              >
                Hủy
              </button>
              <button
                  @click="submitPayment"
                  type="button"
                  class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition"
                  :disabled="paymentMethod === '4' && (tienKhachDua <= 0 || tienTraKhach < 0)"
              >
                Xác nhận thanh toán
              </button>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import axios from 'axios';
import LoaiBiaService from "@/service/LoaiBiaService";
import TacGiaService from "@/service/TacGiaService";
import NhaXuatBanService from "@/service/NhaXuatBanService";
import ChatLieuService from "@/service/ChatLieuService";
import NguoiDichService from "@/service/NguoiDichService";
import TheLoaiService from "@/service/TheLoaiService";
import NgonNguService from "@/service/NgonNguService";
import ChiTietSanPhamService from "@/service/ChiTietSanPhamService";
import AnhSanPhamService from "@/service/AnhSanPhamService";
import KhachHangService from '@/service/KhachHangService';
import HoaDonService from '@/service/hoaDonService';
import DotGiamGiaService from '@/service/DotGiamGiaService';
import PhieuGiamGiaService from '@/service/phieuGiamGiaService';
import { reactive } from "vue";
import { API_URL } from '@/config';

export default {
  name: "BanHangTaiQuay",
  data() {
    return {
      orders: [],
      selectedOrderIndex: 0,
      paginatedProducts: [],
      showAddProductModal: false,
      showSelectCustomerModal: false,
      showAddCustomerModal: false,
      showConfirmationModal: false,
      showBookSetDetailModal: false,
      showPaymentModal: false,
      selectedBookSet: null,
      bookSetDetails: {},
      expandedBookSets: {},
      productSearchQuery: '',
      customerSearchQuery: '',
      searchResults: [],
      allProducts: [],
      allBookSets: [],
      isShowingBookSets: false,
      customers: [],
      selectedProduct: null,
      selectedProductQuantity: 1,
      confirmationItem: null,
      confirmationQuantity: 1,
      confirmationType: 'product', // 'product' or 'bookset'
      newCustomer: {
        hoTen: '',
        email: '',
        soDienThoai: '',
        ngaySinh: '',
        gioiTinh: true,
        trangThai: true
      },
      deliveryMethod: 'TaiQuay',
      paymentMethod: '4', // 4: tiền mặt, 1: chuyển khoản
      tienKhachDua: 0,
      paymentNote: '',
      voucherCode: '',
      selectedVoucher: null,
      availableVouchers: [],
      personalVouchers: [],
      productDiscounts: {},
      bookSetDiscounts: {},
      filters: {
        searchQuery: '',
        minPrice: null,
        maxPrice: null,
        idLoaiBia: null,
        idTacGia: null,
        idNhaXuatBan: null,
        idChatLieu: null,
        idNguoiDich: null,
        idTheLoai: null,
        idNgonNgu: null,
      },
      loaiBiaList: [],
      tacGiaList: [],
      nhaXuatBanList: [],
      chatLieuList: [],
      nguoiDichList: [],
      theLoaiList: [],
      ngonNguList: [],
      currentImageIndex: reactive({}),
      imageIntervals: {},
    };
  },
  computed: {
    selectedOrder() {
      return this.orders[this.selectedOrderIndex] || {};
    },
    tongTienHang() {
      const total = this.paginatedProducts.reduce((total, item) => {
        return total + (item.thanhTien || item.soLuong * item.giaSanPham);
      }, 0);
      this.$nextTick(() => {
        this.loadAvailableVouchers();
        if (this.selectedOrder && (this.selectedOrder.idKhachHang || this.selectedOrder.khachHang)) {
          this.loadPersonalVouchers();
        }
      });
      return total;
    },
    tienGiamGia() {
      if (!this.selectedVoucher) return 0;

      // Nếu có giảm giá theo phần trăm
      if (this.selectedVoucher.soPhanTramGiam > 0) {
        return (this.tongTienHang * this.selectedVoucher.soPhanTramGiam) / 100;
      }

      // Nếu có giảm giá theo số tiền cố định
      if (this.selectedVoucher.giaTriGiam > 0) {
        return this.selectedVoucher.giaTriGiam;
      }

      return 0;
    },
    thanhTien() {
      const phiShip = this.selectedOrder.phiShip || 0;
      return this.tongTienHang - this.tienGiamGia + phiShip;
    },
    tienTraKhach() {
      return this.paymentMethod === '4' ? this.tienKhachDua - this.thanhTien : 0;
    },
    isConfirmDisabled() {
      if (this.selectedOrder.trangThai !== 'Tạo hóa đơn') return true;
      if (this.paginatedProducts.length === 0) return true;
      if (this.paymentMethod === '4' && this.tienTraKhach < 0) return true;
      if (this.paymentMethod === '4' && this.tienKhachDua <= 0) return true;
      return false;
    },
    filteredBookSets() {
      return this.allBookSets.filter(bookSet => {
        const query = this.productSearchQuery.toLowerCase().trim();
        const numericQuery = parseFloat(query);
        const matchesSearchQuery = !query || (
            bookSet.maBoSach.toLowerCase().includes(query) ||
            bookSet.tenBoSach.toLowerCase().includes(query) ||
            (isFinite(numericQuery) && (
                bookSet.soLuong === numericQuery ||
                bookSet.giaTien === numericQuery
            ))
        );
        const matchesPriceRange = (
            (this.filters.minPrice === null || bookSet.giaTien >= this.filters.minPrice) &&
            (this.filters.maxPrice === null || bookSet.giaTien <= this.filters.maxPrice)
        );
        return matchesSearchQuery && matchesPriceRange;
      });
    },
    expandedBookSetDetails() {
      return this.paginatedProducts.filter(prod =>
          prod.boSach && this.expandedBookSets[prod.boSach.id]
      );
    },
    filteredProducts() {
      const products = this.productSearchQuery ? this.searchResults : this.allProducts;
      return products.filter(item => {
        const query = this.filters.searchQuery.toLowerCase().trim();
        const numericQuery = parseFloat(query);
        const matchesSearchQuery = !query || (
            item.maChiTietSanPham.toLowerCase().includes(query) ||
            item.tenChiTietSanPham.toLowerCase().includes(query) ||
            (isFinite(numericQuery) && (
                item.soLuongTon === numericQuery ||
                item.gia === numericQuery
            ))
        );
        return (
            matchesSearchQuery &&
            (this.filters.minPrice === null || item.gia >= this.filters.minPrice) &&
            (this.filters.maxPrice === null || item.gia <= this.filters.maxPrice) &&
            (this.filters.idLoaiBia === null || (item.idLoaiBia && item.idLoaiBia.id === this.filters.idLoaiBia)) &&
            (this.filters.idTacGia === null || (item.idTacGia && item.idTacGia.id === this.filters.idTacGia)) &&
            (this.filters.idNhaXuatBan === null || (item.idNhaXuatBan && item.idNhaXuatBan.id === this.filters.idNhaXuatBan)) &&
            (this.filters.idChatLieu === null || (item.idChatLieu && item.idChatLieu.id === this.filters.idChatLieu)) &&
            (this.filters.idNguoiDich === null || (item.idNguoiDich && item.idNguoiDich.id === this.filters.idNguoiDich)) &&
            (this.filters.idTheLoai === null || (item.idTheLoai && item.idTheLoai.id === this.filters.idTheLoai)) &&
            (this.filters.idNgonNgu === null || (item.idNgonNgu && item.idNgonNgu.id === this.filters.idNgonNgu))
        );
      });
    },
    filteredCustomers() {
      return this.customers.filter(customer => {
        const query = this.customerSearchQuery.toLowerCase().trim();
        return !query || (
            customer.maKhachHang?.toLowerCase().includes(query) ||
            customer.hoTen?.toLowerCase().includes(query) ||
            customer.email?.toLowerCase().includes(query) ||
            customer.soDienThoai?.toLowerCase().includes(query)
        );
      });
    }
  },
  watch: {
    selectedOrderIndex() {
      this.fetchProductsForOrder();
      this.tienKhachDua = 0;
    },
    deliveryMethod(newValue, oldValue) {
      console.log(`DEBUG: deliveryMethod changed from "${oldValue}" to "${newValue}"`);
      console.log(`DEBUG: loaiHoaDon: ${this.mapDeliveryMethod(newValue)}`);
      this.tienKhachDua = 0;
      if (this.deliveryMethod === 'TaiQuay') {
        console.log('DEBUG: Selected Tại quầy, setting paymentMethod to Tiền mặt (4)');
        this.paymentMethod = '4';
      } else if (this.deliveryMethod === 'GiaoHang') {
        console.log('DEBUG: Selected Giao hàng, setting paymentMethod to COD');
        this.paymentMethod = 'cod';
      }
    },
    paymentMethod() {
      this.tienKhachDua = 0;
    },
    filters: {
      handler() {
        this.productSearchQuery = '';
        this.searchResults = [];
      },
      deep: true,
    },
    allProducts: {
      handler() {
        this.clearImageIntervals();
        if (document.visibilityState === 'visible') {
          this.setupImageIntervals();
        }
      },
      deep: true,
    },
    searchResults: {
      handler() {
        this.clearImageIntervals();
        if (document.visibilityState === 'visible') {
          this.setupImageIntervals();
        }
      },
      deep: true,
    },
  },
  async created() {
    await this.fetchOrders();
  },
  beforeUnmount() {
    this.clearImageIntervals();
  },
  methods: {
    mapDeliveryMethod(value) {
      return value === 'TaiQuay' ? 'Tại quầy' : 'Giao hàng';
    },
    formatCurrency(value) {
      return new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(value || 0);
    },
    getVoucherDisplayValue(voucher) {
      if (!voucher) return '';

      // Nếu có giảm giá theo phần trăm
      if (voucher.soPhanTramGiam > 0) {
        return `${voucher.soPhanTramGiam}%`;
      }

      // Nếu có giảm giá theo số tiền cố định
      if (voucher.giaTriGiam > 0) {
        return this.formatCurrency(voucher.giaTriGiam);
      }

      return '';
    },
    formatDiaChi(customer) {
      if (!customer.danhSachDiaChi || customer.danhSachDiaChi.length === 0) return '';
      const diaChi = customer.danhSachDiaChi.find(dc => dc.macDinh) || customer.danhSachDiaChi[0];
      return `${diaChi.diaChiChiTiet}, ${diaChi.xaPhuong}, ${diaChi.quanHuyen}, ${diaChi.tinhThanh}`;
    },
    async fetchOrders() {
      try {
        const response = await axios.get(`${API_URL}/hoa-don?trangThai=Tạo hóa đơn`);
        const data = Array.isArray(response.data) ? response.data : [];
        this.orders = data.map(order => ({
          ...order,
          hoaDonChiTiets: Array.isArray(order.hoaDonChiTiets) ? order.hoaDonChiTiets : [],
          phieuGiamGia: order.phieuGiamGia || {},
          diaChi: order.diaChi || '',
          soDienThoaiNguoiNhan: order.soDienThoaiNguoiNhan || '',
          loaiHoaDon: order.loaiHoaDon === 'Tại quầy' ? 'TaiQuay' : 'GiaoHang',
        }));
        if (this.orders.length > 0) {
          this.selectedOrderIndex = 0;
          await this.fetchProductsForOrder();
        }
      } catch (error) {
        console.error('Lỗi khi tải danh sách hóa đơn:', error);
        alert('Không thể tải danh sách hóa đơn!');
      }
    },
    async fetchProductsForOrder() {
      if (!this.selectedOrder || !this.selectedOrder.id) return;
      try {
        const response = await axios.get(`${API_URL}/hoa-don/${this.selectedOrder.id}`);
        this.paginatedProducts = response.data.hoaDonChiTiets || [];
        this.paginatedProducts.forEach(prod => {
          if (!prod.oldQuantity) {
            prod.oldQuantity = prod.soLuong;
          }
        });
        await this.loadProductDiscounts();
      } catch (error) {
        console.error('Lỗi khi tải sản phẩm của hóa đơn:', error);
        alert('Không thể tải sản phẩm của hóa đơn!');
      }
    },
    async createNewInvoice() {
      try {
        const response = await axios.post(`${API_URL}/hoa-don`, {
          loaiHoaDon: this.mapDeliveryMethod(this.deliveryMethod),
        });
        this.orders.push({
          ...response.data,
          hoaDonChiTiets: [],
          phieuGiamGia: {},
          diaChiGiaoHang: '',
          soDienThoaiNguoiNhan: '',
        });
        this.selectedOrderIndex = this.orders.length - 1;
        this.paginatedProducts = [];
        this.resetPaymentForm();
      } catch (error) {
        console.error('Lỗi khi tạo hóa đơn mới:', error);
        alert('Không thể tạo hóa đơn mới!');
      }
    },
    async xacNhanHoaDon() {
      try {
        const orderId = this.selectedOrder?.id;
        if (!orderId) {
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }
        if (this.deliveryMethod === 'GiaoHang') {
          const hasCustomer = !!(this.selectedOrder?.idKhachHang || this.selectedOrder?.khachHang || this.selectedOrder?.tenNguoiNhan);
          if (!hasCustomer) {
            alert('Giao hàng chỉ áp dụng khi đã chọn khách hàng. Vui lòng chọn khách hàng trước.');
            return;
          }
        }
        if (this.paginatedProducts.length === 0) {
          alert("Hóa đơn phải có ít nhất một sản phẩm!");
          return;
        }

        // Cập nhật thông tin hóa đơn
        await HoaDonService.updateHoaDon(orderId, {
          phiShip: this.selectedOrder.phiShip || 0,
          tongTien: this.thanhTien,
          loaiHoaDon: this.mapDeliveryMethod(this.deliveryMethod),
          phieuGiamGiaId: this.selectedVoucher?.id || null,
        });

        // Ghi nhận thông tin thanh toán
        let paymentPayload = {
          tienMat: 0,
          chuyenKhoan: 0,
          ghiChu: this.paymentNote || '',
          loaiHoaDon: this.mapDeliveryMethod(this.deliveryMethod),
          tienKhachDua: 0,
          phieuGiamGiaId: this.selectedVoucher?.id || null,
        };

        if (this.deliveryMethod === 'TaiQuay') {
          // Đối với hóa đơn tại quầy, gán đầy đủ thông tin thanh toán
          paymentPayload.phuongThucThanhToanId = this.paymentMethod === '1' ? 1 : 4;
          paymentPayload.phuongThucThanhToan = this.paymentMethod === '1' ? 'Tra_Truoc' : 'TIEN_MAT';
          paymentPayload.tienMat = this.paymentMethod === '4' ? this.thanhTien : 0;
          paymentPayload.chuyenKhoan = this.paymentMethod === '1' ? this.thanhTien : 0;
          paymentPayload.tienKhachDua = this.paymentMethod === '4' ? this.tienKhachDua : 0;
        } else if (this.deliveryMethod === 'GiaoHang') {
          // Đối với hóa đơn giao hàng, không gán phuongThucThanhToanId cho COD
          paymentPayload.phuongThucThanhToan = this.paymentMethod === 'cod' ? 'COD' : this.paymentMethod === '1' ? 'Tra_Truoc' : 'TIEN_MAT';
          paymentPayload.tienMat = this.paymentMethod === 'cod' || this.paymentMethod === '4' ? this.thanhTien : 0;
          paymentPayload.chuyenKhoan = this.paymentMethod === '1' ? this.thanhTien : 0;
          paymentPayload.tienKhachDua = this.paymentMethod === '4' ? this.tienKhachDua : this.thanhTien;
          if (this.paymentMethod !== 'cod') {
            paymentPayload.phuongThucThanhToanId = this.paymentMethod === '1' ? 1 : 4;
          }
        }

        console.log("Dữ liệu thanh toán gửi đi:", paymentPayload);

        // Ghi nhận thanh toán
        const payRes = await HoaDonService.updatePayment(orderId, paymentPayload);
        if (payRes.status !== 200) {
          alert('Có lỗi khi ghi nhận thanh toán, vui lòng thử lại!');
          return;
        }

        // Đặt trạng thái
        const newStatus = this.deliveryMethod === 'TaiQuay' ? 'Hoàn thành' : 'Chờ xác nhận';
        const response = await HoaDonService.updateTrangThaiHoaDon(orderId, newStatus);
        if (response.status === 200) {
          alert("Xác nhận hóa đơn thành công!");
          await this.fetchOrders();
          window.location.reload();
        } else {
          alert("Có lỗi xảy ra khi xác nhận hóa đơn!");
        }
      } catch (error) {
        console.error("Lỗi khi xác nhận hóa đơn:", error);
        alert("Có lỗi xảy ra khi xác nhận hóa đơn!");
      }
    },
    async openPaymentModal() {
      if (this.deliveryMethod === 'GiaoHang') {
        const hasCustomer = !!(this.selectedOrder?.idKhachHang || this.selectedOrder?.khachHang || this.selectedOrder?.tenNguoiNhan);
        if (!hasCustomer) {
          alert('Giao hàng chỉ áp dụng khi đã chọn khách hàng. Vui lòng chọn khách hàng trước.');
          return;
        }
      }
      if (this.paymentMethod === '4' && (this.tienKhachDua <= 0 || this.tienTraKhach < 0)) {
        alert("Vui lòng nhập số tiền khách đưa hợp lệ!");
        return;
      }
      this.showPaymentModal = true;
    },
    async submitPayment() {
      try {
        const orderId = this.selectedOrder?.id;
        if (!orderId) {
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }
        let paymentPayload = {
          tienMat: 0,
          chuyenKhoan: 0,
          phuongThucThanhToanId: 4,
          ghiChu: this.paymentNote || '',
          loaiHoaDon: this.mapDeliveryMethod(this.deliveryMethod),
          tienKhachDua: 0,
          phieuGiamGiaId: this.selectedVoucher?.id || null,
          phuongThucThanhToan: 'COD',
        };
        if (this.paymentMethod === 'cod') {
          paymentPayload.phuongThucThanhToanId = 4;
          paymentPayload.phuongThucThanhToan = 'COD';
          paymentPayload.tienMat = this.thanhTien;
          paymentPayload.chuyenKhoan = 0;
          paymentPayload.tienKhachDua = this.thanhTien;
        } else if (this.paymentMethod === '1') {
          paymentPayload.phuongThucThanhToanId = 1;
          paymentPayload.phuongThucThanhToan = 'Tra_Truoc';
          paymentPayload.tienMat = 0;
          paymentPayload.chuyenKhoan = this.thanhTien;
          paymentPayload.tienKhachDua = 0;
        } else if (this.paymentMethod === '4') {
          paymentPayload.phuongThucThanhToanId = 4;
          paymentPayload.phuongThucThanhToan = 'COD';
          paymentPayload.tienMat = this.thanhTien;
          paymentPayload.chuyenKhoan = 0;
          paymentPayload.tienKhachDua = this.thanhTien;
        }
        if (this.thanhTien <= 0) {
          alert("Tổng tiền hóa đơn không hợp lệ!");
          return;
        }
        if ( paymentPayload.phuongThucThanhToanId === 4 && (this.tienKhachDua <= 0 || this.tienTraKhach < 0)) {
          alert("Số tiền khách đưa không đủ để thanh toán!");
          return;
        }
        await HoaDonService.updateHoaDon(orderId, {
          phiShip: this.selectedOrder.phiShip || 0,
          tongTien: this.thanhTien
        });
        const response = await HoaDonService.updatePayment(orderId, paymentPayload);
        if (response.status === 200) {
          let newStatus = paymentPayload.phuongThucThanhToan === 'Tra_Truoc' ? 'Thanh toán thành công' : 'Thanh toán';
          await HoaDonService.updateTrangThaiHoaDon(orderId, newStatus);
          await this.deductStockAfterPayment();
          alert("Xác nhận thanh toán thành công!");
          this.showPaymentModal = false;
          await this.fetchOrders();
        } else {
          alert("Có lỗi xảy ra khi xác nhận thanh toán!");
        }
      } catch (error) {
        alert(error.response?.data?.message || "Có lỗi xảy ra khi xác nhận thanh toán!");
      }
    },
    async openAddProductModal() {
      this.showAddProductModal = true;
      this.productSearchQuery = '';
      this.searchResults = [];
      this.allProducts = [];
      this.selectedProduct = null;
      this.selectedProductQuantity = 1;
      this.filters = {
        searchQuery: '',
        minPrice: null,
        maxPrice: null,
        idLoaiBia: null,
        idTacGia: null,
        idNhaXuatBan: null,
        idChatLieu: null,
        idNguoiDich: null,
        idTheLoai: null,
        idNgonNgu: null,
      };
      await this.loadInitialData();
    },
    closeAddProductModal() {
      this.showAddProductModal = false;
      this.productSearchQuery = '';
      this.searchResults = [];
      this.allProducts = [];
      this.selectedProduct = null;
      this.selectedProductQuantity = 1;
      this.clearImageIntervals();
    },
    async openSelectCustomerModal() {
      this.showSelectCustomerModal = true;
      this.customerSearchQuery = '';
      this.customers = [];
      await this.loadCustomers();
    },
    closeSelectCustomerModal() {
      this.showSelectCustomerModal = false;
      this.customerSearchQuery = '';
      this.customers = [];
    },
    openAddCustomerModal() {
      this.showAddCustomerModal = true;
      this.newCustomer = {
        hoTen: '',
        email: '',
        soDienThoai: '',
        ngaySinh: '',
        gioiTinh: true,
        trangThai: true
      };
    },
    closeAddCustomerModal() {
      this.showAddCustomerModal = false;
      this.newCustomer = {
        hoTen: '',
        email: '',
        soDienThoai: '',
        ngaySinh: '',
        gioiTinh: true,
        trangThai: true
      };
    },
    async loadCustomers() {
      try {
        this.customers = await KhachHangService.getAll();
      } catch (error) {
        console.error('Lỗi khi tải danh sách khách hàng:', error);
        alert('Không thể tải danh sách khách hàng!');
      }
    },
    async searchCustomers() {
      try {
        if (this.customerSearchQuery.trim()) {
          this.customers = await KhachHangService.search(this.customerSearchQuery);
        } else {
          await this.loadCustomers();
        }
      } catch (error) {
        console.error('Lỗi khi tìm kiếm khách hàng:', error);
        alert('Không thể tìm kiếm khách hàng!');
      }
    },
    async selectCustomer(customer) {
      try {
        const orderId = this.selectedOrder.id;
        const customerData = {
          idKhachHang: customer.id,
          tenNguoiNhan: customer.hoTen,
          soDienThoaiNguoiNhan: customer.soDienThoai,
          diaChi: this.formatDiaChi(customer)
        };
        await HoaDonService.updateCustomerInfo(orderId, customerData);
        this.orders[this.selectedOrderIndex] = {
          ...this.selectedOrder,
          ...customerData
        };
        this.$nextTick(() => {
          this.loadPersonalVouchers();
        });
        this.closeSelectCustomerModal();
        alert('Chọn khách hàng thành công!');
      } catch (error) {
        console.error('Lỗi khi chọn khách hàng:', error);
        alert('Có lỗi xảy ra khi chọn khách hàng!');
      }
    },
    async removeCustomer() {
      if (!confirm('Bạn có chắc chắn muốn bỏ chọn khách hàng này?')) {
        return;
      }

      try {
        const orderId = this.selectedOrder.id;
        const customerData = {
          idKhachHang: null,
          tenNguoiNhan: null,
          soDienThoaiNguoiNhan: null,
          diaChi: null
        };
        await HoaDonService.updateCustomerInfo(orderId, customerData);
        this.orders[this.selectedOrderIndex] = {
          ...this.selectedOrder,
          ...customerData
        };
        this.$nextTick(() => {
          this.personalVouchers = []; // Xóa voucher cá nhân
        });
        alert('Bỏ chọn khách hàng thành công!');
      } catch (error) {
        console.error('Lỗi khi bỏ chọn khách hàng:', error);
        alert('Có lỗi xảy ra khi bỏ chọn khách hàng!');
      }
    },
    async saveCustomer() {
      try {
        const response = await KhachHangService.create(this.newCustomer);
        const orderId = this.selectedOrder.id;
        const customerData = {
          idKhachHang: response.id,
          tenNguoiNhan: response.hoTen,
          soDienThoaiNguoiNhan: response.soDienThoai,
          diaChi: ''
        };
        await HoaDonService.updateCustomerInfo(orderId, customerData);
        this.orders[this.selectedOrderIndex] = {
          ...this.selectedOrder,
          ...customerData
        };
        this.closeAddCustomerModal();
        alert('Thêm khách hàng thành công!');
      } catch (error) {
        console.error('Lỗi khi thêm khách hàng:', error);
        alert(error.response?.data?.message || 'Có lỗi xảy ra khi thêm khách hàng!');
      }
    },
    async loadInitialData() {
      try {
        this.loaiBiaList = await LoaiBiaService.getAll();
        this.tacGiaList = await TacGiaService.getAll();
        this.nhaXuatBanList = await NhaXuatBanService.getAll();
        this.chatLieuList = await ChatLieuService.getAll();
        this.nguoiDichList = await NguoiDichService.getAll();
        this.theLoaiList = await TheLoaiService.getAll();
        this.ngonNguList = await NgonNguService.getAll();
      } catch (error) {
        console.error('Lỗi khi tải dữ liệu danh sách:', error);
        alert('Có lỗi xảy ra khi tải dữ liệu danh sách!');
      }
    },
    async searchProducts() {
      if (this.productSearchQuery.trim()) {
        try {
          this.filters.searchQuery = this.productSearchQuery;
          this.searchResults = await ChiTietSanPhamService.search(this.productSearchQuery);
          for (let product of this.searchResults) {
            if (product.id) {
              product.anhSanPhams = await AnhSanPhamService.getImagesByChiTietSanPhamId(product.id) || [];
              this.currentImageIndex[product.id] = 0;
              product.anhSanPhams.forEach(image => {
                const img = new Image();
                img.src = image.url;
              });
            } else {
              product.anhSanPhams = [];
            }
          }
        } catch (error) {
          console.error('Lỗi khi tìm kiếm sản phẩm:', error);
          alert('Có lỗi xảy ra khi tìm kiếm sản phẩm!');
        }
      } else {
        this.searchResults = [];
        this.filters.searchQuery = '';
      }
    },
    async showAllProducts() {
      try {
        this.isShowingBookSets = false;
        this.allProducts = await ChiTietSanPhamService.getAll();
        for (let product of this.allProducts) {
          if (product.id) {
            product.anhSanPhams = await AnhSanPhamService.getImagesByChiTietSanPhamId(product.id) || [];
            this.currentImageIndex[product.id] = 0;
            product.anhSanPhams.forEach(image => {
              const img = new Image();
              img.src = image.url;
            });
          } else {
            product.anhSanPhams = [];
          }
        }
        this.searchResults = [];
        this.productSearchQuery = '';
      } catch (error) {
        console.error('Lỗi khi tải toàn bộ sản phẩm:', error);
        alert('Có lỗi xảy ra khi tải danh sách sản phẩm!');
      }
    },
    setupImageIntervals() {
      const products = this.productSearchQuery ? this.searchResults : this.allProducts;
      products.forEach(product => {
        if (product.id && product.anhSanPhams && product.anhSanPhams.length > 1) {
          this.imageIntervals[product.id] = setInterval(() => {
            const currentIndex = this.currentImageIndex[product.id] || 0;
            const nextIndex = (currentIndex + 1) % product.anhSanPhams.length;
            this.currentImageIndex[product.id] = nextIndex;
          }, 3000);
        }
      });
    },
    clearImageIntervals() {
      Object.keys(this.imageIntervals).forEach(id => {
        clearInterval(this.imageIntervals[id]);
      });
      this.imageIntervals = {};
    },
    selectProduct(product) {
      this.selectedProduct = product;
      this.selectedProductQuantity = 1;
    },
    async addProduct() {
      if (!this.selectedProduct) {
        alert('Vui lòng chọn một sản phẩm trước khi thêm!');
        return;
      }
      if (this.selectedProductQuantity > this.selectedProduct.soLuongTon) {
        alert(`Số lượng vượt quá số lượng tồn kho (${this.selectedProduct.soLuongTon})!`);
        return;
      }
      try {
        const orderId = this.selectedOrder.id;
        const productData = {
          chiTietSanPhamId: this.selectedProduct.id,
          soLuong: this.selectedProductQuantity,
          giaSanPham: this.selectedProduct.gia,
        };
        const response = await axios.post(`${API_URL}/hoa-don/${orderId}/add-product`, productData);
        if (response.status === 200) {
          alert('Thêm sản phẩm thành công!');
          this.closeAddProductModal();
          await this.fetchProductsForOrder();
        } else {
          alert('Có lỗi xảy ra khi thêm sản phẩm!');
        }
      } catch (error) {
        console.error('Lỗi khi thêm sản phẩm:', error);
        alert(error.response?.data || 'Có lỗi xảy ra khi thêm sản phẩm!');
      }
    },
    async removeItem(itemId) {
      if (!confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')) return;
      try {
        const orderId = this.selectedOrder.id;
        await axios.delete(`${API_URL}/hoa-don/${orderId}/remove-product/${itemId}`);
        alert('Xóa sản phẩm thành công!');
        await this.fetchProductsForOrder();
      } catch (error) {
        console.error('Lỗi khi xóa sản phẩm:', error);
        alert('Có lỗi xảy ra khi xóa sản phẩm!');
      }
    },
    async openConfirmationModal(item, type) {
      this.confirmationItem = item;
      this.confirmationType = type;
      this.confirmationQuantity = 1;
      this.showConfirmationModal = true;
    },
    closeConfirmationModal() {
      this.showConfirmationModal = false;
      this.confirmationItem = null;
      this.confirmationQuantity = 1;
      this.confirmationType = 'product';
    },
    async confirmAddToOrder() {
      if (!this.confirmationItem || !this.confirmationQuantity) {
        alert('Vui lòng nhập số lượng hợp lệ!');
        return;
      }
      if (this.confirmationQuantity <= 0) {
        alert('Số lượng phải lớn hơn 0!');
        return;
      }
      if (!this.selectedOrder || !this.selectedOrder.id) {
        alert('Vui lòng chọn hóa đơn trước khi thêm sản phẩm!');
        return;
      }
      const maxQuantity = this.confirmationItem?.soLuongTon || this.confirmationItem?.soLuong;
      if (this.confirmationQuantity > maxQuantity) {
        alert(`Số lượng không được vượt quá tồn kho (${maxQuantity})!`);
        return;
      }
      try {
        const orderId = this.selectedOrder.id;
        let productData;
        if (this.confirmationType === 'product') {
          productData = {
            chiTietSanPhamId: this.confirmationItem.id,
            soLuong: this.confirmationQuantity,
            giaSanPham: this.confirmationItem.gia,
          };
          const response = await axios.post(`${API_URL}/hoa-don/${orderId}/add-product`, productData);
          if (response.status === 200) {
            alert('Thêm sản phẩm thành công!');
            this.closeConfirmationModal();
            await this.fetchProductsForOrder();
          }
        } else if (this.confirmationType === 'bookset') {
          productData = {
            boSachId: this.confirmationItem.id,
            soLuong: this.confirmationQuantity,
            giaSanPham: this.confirmationItem.giaTien,
          };
          const response = await axios.post(`${API_URL}/hoa-don/${orderId}/add-bosach`, productData);
          if (response.status === 200) {
            alert('Thêm bộ sách thành công!');
            this.closeConfirmationModal();
            await this.fetchProductsForOrder();
          }
        }
      } catch (error) {
        console.error('Lỗi khi thêm vào hóa đơn:', error);
        alert('Có lỗi xảy ra khi thêm vào hóa đơn!');
      }
    },
    async deductStockAfterPayment() {
      console.log('Số lượng đã được trừ khi thay đổi trong POS, không cần trừ lại khi thanh toán');
    },
    async huyHoaDon() {
      if (!confirm("Bạn có chắc chắn muốn hủy hóa đơn này?")) return;
      try {
        const orderId = this.selectedOrder?.id;
        if (!orderId) {
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }
        const response = await HoaDonService.updateTrangThaiHoaDon(orderId, "Đã hủy");
        if (response.status === 200) {
          alert("Hủy hóa đơn thành công!");
          await this.fetchOrders();
        } else {
          alert("Có lỗi xảy ra khi hủy hóa đơn!");
        }
      } catch (error) {
        console.error("Lỗi khi hủy hóa đơn:", error);
        alert("Có lỗi xảy ra khi hủy hóa đơn!");
      }
    },
    async openBookSetDetailModal(bookSet) {
      this.selectedBookSet = bookSet;
      this.showBookSetDetailModal = true;
      try {
        const BoSachChiTietService = (await import('@/service/BoSachChiTietService')).default;
        const details = await BoSachChiTietService.getDetailedByBoSachId(bookSet.id);
        this.bookSetDetails[bookSet.id] = details;
      } catch (error) {
        console.error('Lỗi khi tải chi tiết bộ sách:', error);
        this.bookSetDetails[bookSet.id] = [];
      }
    },
    closeBookSetDetailModal() {
      this.showBookSetDetailModal = false;
      this.selectedBookSet = null;
    },
    async showAllBookSets() {
      try {
        this.isShowingBookSets = true;
        const BoSachService = (await import('@/service/BoSachService')).default;
        this.allBookSets = await BoSachService.getAll();
        this.searchResults = [];
        this.productSearchQuery = '';
      } catch (error) {
        console.error('Lỗi khi tải danh sách bộ sách:', error);
        alert('Có lỗi xảy ra khi tải danh sách bộ sách!');
      }
    },
    resetPaymentForm() {
      this.paymentMethod = '4';
      this.tienKhachDua = 0;
      this.paymentNote = '';
      this.voucherCode = '';
      this.selectedVoucher = null;
    },
    async loadAvailableVouchers() {
      try {
        this.availableVouchers = await DotGiamGiaService.getAvailableVouchersForPos(this.tongTienHang);
      } catch (error) {
        console.error('Lỗi khi load voucher:', error);
        this.availableVouchers = [];
      }
    },
    async loadPersonalVouchers() {
      let customerId = null;
      if (this.selectedOrder?.idKhachHang?.id) {
        customerId = this.selectedOrder.idKhachHang.id;
      } else if (this.selectedOrder?.khachHang?.id) {
        customerId = this.selectedOrder.khachHang.id;
      } else if (this.selectedOrder?.idKhachHang) {
        customerId = this.selectedOrder.idKhachHang;
      }
      if (customerId) {
        try {
          this.personalVouchers = await DotGiamGiaService.getPersonalVouchersForCustomer(customerId, this.tongTienHang);
        } catch (error) {
          console.error('Lỗi khi load voucher cá nhân:', error);
          this.personalVouchers = [];
        }
      } else {
        this.personalVouchers = [];
      }
    },
    async updateProductQuantity(prod) {
      if (prod.soLuong < 1) {
        prod.soLuong = 1;
      }
      const maxQuantity = prod.boSach?.soLuong || prod.chiTietSanPham?.soLuongTon || 0;
      if (prod.soLuong > maxQuantity) {
        prod.soLuong = maxQuantity;
        alert(`Số lượng không được vượt quá ${maxQuantity}`);
      }
      const giaSanPham = this.hasDiscount(prod) ? this.getDiscountedPrice(prod) : prod.giaSanPham;
      prod.thanhTien = prod.soLuong * giaSanPham;
      try {
        const orderId = this.selectedOrder?.id;
        if (orderId && prod.chiTietSanPham) {
          await axios.put(`${API_URL}/hoa-don/${orderId}/update-product-quantity`, {
            chiTietSanPhamId: prod.chiTietSanPham.id,
            soLuong: prod.soLuong
          });
          const oldQuantity = prod.oldQuantity || 0;
          const quantityDiff = prod.soLuong - oldQuantity;
          if (quantityDiff !== 0) {
            const newStock = prod.chiTietSanPham.soLuongTon - quantityDiff;
            await axios.put(`${API_URL}/chi-tiet-san-pham/${prod.chiTietSanPham.id}/update-stock`, {
              soLuongTon: newStock
            });
            prod.chiTietSanPham.soLuongTon = newStock;
          }
        } else if (orderId && prod.boSach) {
          await axios.put(`${API_URL}/hoa-don/${orderId}/update-bosach-quantity`, {
            boSachId: prod.boSach.id,
            soLuong: prod.soLuong
          });
          const oldQuantity = prod.oldQuantity || 0;
          const quantityDiff = prod.soLuong - oldQuantity;
          if (quantityDiff !== 0) {
            const newStock = prod.boSach.soLuong - quantityDiff;
            await axios.put(`${API_URL}/bo-sach/${prod.boSach.id}/update-stock`, {
              soLuong: newStock
            });
            prod.boSach.soLuong = newStock;
            const boSachChiTietResponse = await axios.get(`${API_URL}/bo-sach/${prod.boSach.id}/chi-tiet`);
            const boSachChiTiets = boSachChiTietResponse.data;
            for (const chiTiet of boSachChiTiets) {
              if (chiTiet.idChiTietSanPham) {
                const newChiTietStock = chiTiet.idChiTietSanPham.soLuongTon - (chiTiet.soLuong * quantityDiff);
                await axios.put(`${API_URL}/chi-tiet-san-pham/${chiTiet.idChiTietSanPham.id}/update-stock`, {
                  soLuongTon: newChiTietStock
                });
              }
            }
          }
        }
      } catch (error) {
        console.error('Lỗi khi cập nhật số lượng:', error);
        alert('Có lỗi xảy ra khi cập nhật số lượng!');
      }
      prod.oldQuantity = prod.soLuong;
      this.paginatedProducts = [...this.paginatedProducts];
      this.$nextTick(() => {
        this.loadAvailableVouchers();
        this.loadPersonalVouchers();
      });
    },
    onVoucherChange() {
      if (this.selectedVoucher) {
        this.voucherCode = this.selectedVoucher.maPhieuGiamGia;
      } else {
        this.voucherCode = '';
      }
    },
    async deductVoucherAfterPayment() {
      if (this.selectedVoucher) {
        try {
          await PhieuGiamGiaService.deductVoucherQuantity(this.selectedVoucher.id);
        } catch (error) {
          console.error('Lỗi khi trừ số lượng voucher:', error);
        }
      }
    },
    async loadProductDiscounts() {
      try {
        for (const product of this.paginatedProducts) {
          if (product.chiTietSanPham) {
            const discountInfo = await DotGiamGiaService.getActiveDetail(product.chiTietSanPham.id);
            if (discountInfo && discountInfo.giaBanDau) {
              this.productDiscounts[product.chiTietSanPham.id] = discountInfo;
            }
          } else if (product.boSach) {
            const discountInfo = await DotGiamGiaService.getActiveBoSachDetail(product.boSach.id);
            if (discountInfo && discountInfo.giaBanDau) {
              this.bookSetDiscounts[product.boSach.id] = discountInfo;
            }
          }
        }
      } catch (error) {
        console.error('Lỗi khi tải thông tin giảm giá:', error);
      }
    },
    getOriginalPrice(product) {
      if (product.chiTietSanPham) {
        const discountInfo = this.productDiscounts[product.chiTietSanPham.id];
        return discountInfo ? discountInfo.giaBanDau : product.giaSanPham;
      } else if (product.boSach) {
        const discountInfo = this.bookSetDiscounts[product.boSach.id];
        return discountInfo ? discountInfo.giaBanDau : product.giaSanPham;
      }
      return product.giaSanPham;
    },
    getDiscountedPrice(product) {
      if (product.chiTietSanPham) {
        const discountInfo = this.productDiscounts[product.chiTietSanPham.id];
        return discountInfo ? discountInfo.giaSauGiam : product.giaSanPham;
      } else if (product.boSach) {
        const discountInfo = this.bookSetDiscounts[product.boSach.id];
        return discountInfo ? discountInfo.giaSauGiam : product.giaSanPham;
      }
      return product.giaSanPham;
    },
    hasDiscount(product) {
      if (product.chiTietSanPham) {
        return this.productDiscounts[product.chiTietSanPham.id] != null;
      } else if (product.boSach) {
        return this.bookSetDiscounts[product.boSach.id] != null;
      }
      return false;
    },
  },
};
</script>

<style scoped>
.hidden {
  display: none;
}
.tab-button {
  transition: all 0.3s ease;
}
.tab-button:hover {
  background-color: #f3f4f6;
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}
.image-container {
  position: relative;
  width: 100%;
  height: 100%;
}
.image-active {
  opacity: 1;
  transition: opacity 0.5s ease-in-out;
  z-index: 1;
}
.image-inactive {
  opacity: 0;
  transition: opacity 0.5s ease-in-out;
  z-index: 0;
}
.line-through {
  text-decoration: line-through;
}
</style>
