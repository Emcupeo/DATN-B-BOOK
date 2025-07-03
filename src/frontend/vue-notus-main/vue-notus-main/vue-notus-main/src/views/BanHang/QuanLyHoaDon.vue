<template>
  <div class="p-6 bg-gray-100 min-h-screen">
    <!-- Quản lý đơn hàng -->
    <div ref="invoice" class="bg-white p-4 rounded-lg shadow-md">
      <h2 class="text-lg font-semibold">Quản lý hóa đơn / <span class="text-gray-400">{{ order.maHoaDon || "Không có" }}</span></h2>

      <!-- Lịch sử đơn hàng dạng timeline -->
      <div v-if="order.lichSuHoaDons.length" class="bg-white p-6 rounded-lg shadow-md">
        <div class="flex items-center overflow-x-auto space-x-4 py-4">
          <div v-for="(status, index) in sortedOrderHistory" :key="status.id" class="flex items-center">
            <div class="flex flex-col items-center">
              <div class="icon-wrapper" :class="getStatusClass(status.trangThaiMoi)">
                <!-- Icon cho từng trạng thái -->
                <i v-if="status.trangThaiMoi === 'Tạo hóa đơn'" class="fas fa-file-alt"></i>
                <i v-if="status.trangThaiMoi === 'Chờ xác nhận'" class="fas fa-clock"></i>
                <i v-if="status.trangThaiMoi === 'Chờ giao hàng'" class="fas fa-truck"></i>
                <i v-if="status.trangThaiMoi === 'Đang vận chuyển'" class="fas fa-shipping-fast"></i>
                <i v-if="status.trangThaiMoi === 'Đã giao hàng'" class="fas fa-check-circle"></i>
                <i v-if="status.trangThaiMoi === 'Hoàn thành'" class="fas fa-check-double"></i>
              </div>
              <p class="mt-2 font-semibold text-center">{{ status.trangThaiMoi || "Không xác định" }}</p>
              <p class="text-sm text-gray-500">{{ formatDate(status.createdAt) }}</p>
            </div>
            <div v-if="index < sortedOrderHistory.length - 1" class="progress-line"></div>
          </div>
        </div>

        <!-- Nút quay về trạng thái trước với confirm -->
        <div class="mt-4 flex justify-end">
          <button v-if="order.trangThai !== 'Tạo hóa đơn' && order.trangThai !== 'Chờ xác nhận'"
                  @click="quayVeTrangThai"
                  class="bg-orange-500 text-white px-4 py-2 rounded">
            Quay về trạng thái trước
          </button>
        </div>
      </div>

      <!-- Nút điều khiển trạng thái đơn hàng -->
      <div class="bg-white p-6 rounded-lg shadow-md mt-4 flex justify-between items-center">
        <div class="flex gap-2">
          <button v-if="order.trangThai === 'Chờ xác nhận'" @click="xacNhanHoaDon" class="px-4 py-2 border border-green-500 text-green-500 rounded-lg">Xác nhận hóa đơn</button>
          <button v-if="order.trangThai === 'Chờ xác nhận' || order.trangThai === 'Chờ giao hàng' || order.trangThai === 'Đang vận chuyển' || order.trangThai === 'Đã giao hàng'"
                  @click="huyDon"
                  class="px-4 py-2 border border-red-500 text-red-500 rounded-lg">
            Hủy đơn
          </button>
          <button v-if="order.trangThai === 'Chờ giao hàng'" @click="xacNhanGiaoHang" class="px-4 py-2 border border-blue-500 text-blue-500 rounded-lg">Xác nhận giao hàng</button>
          <button v-if="order.trangThai === 'Đang vận chuyển'" @click="xacNhanLayHang" class="px-4 py-2 border border-yellow-500 text-yellow-500 rounded-lg">Xác nhận lấy hàng</button>
          <button v-if="order.trangThai === 'Đã giao hàng'" @click="openPaymentModal" class="px-4 py-2 border border-green-500 text-green-500 rounded-lg">Xác nhận thanh toán</button>
          <template v-if="order.trangThai === 'Hoàn thành'">
            <button @click="printInvoice" class="px-4 py-2 border border-orange-400 text-orange-400 rounded-lg">In hóa đơn</button>
            <button @click="openHistoryModal" class="px-4 py-2 border border-orange-400 text-orange-400 rounded-lg">Chi tiết</button>
          </template>
        </div>
        <div v-if="order.trangThai !== 'Hoàn thành'">
          <button @click="openHistoryModal" class="px-4 py-2 border border-orange-400 text-orange-400 rounded-lg">Chi tiết</button>
        </div>
      </div>

      <!-- Modal hiển thị chi tiết lịch sử hóa đơn -->
      <transition name="fade">
        <div v-if="showHistoryModal" class="fixed inset-0 z-50 flex items-center justify-center bg-gray-600 bg-opacity-50">
          <div class="bg-white p-6 rounded-lg shadow-lg w-[600px] z-50">
            <h3 class="text-lg font-semibold mb-4">Chi tiết lịch sử hóa đơn</h3>
            <div v-if="sortedOrderHistory.length" class="max-h-96 overflow-y-auto">
              <table class="w-full text-sm text-left text-gray-500">
                <thead class="text-xs text-gray-700 uppercase bg-gray-50 sticky top-0">
                <tr>
                  <th class="px-4 py-3">Thời gian</th>
                  <th class="px-4 py-3">Trạng thái cũ</th>
                  <th class="px-4 py-3 status-column">Trạng thái mới</th>
                  <th class="px-4 py-3">Ghi chú</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(history, index) in sortedOrderHistory" :key="index" class="bg-white border-b hover:bg-gray-50">
                  <td class="px-4 py-3">{{ formatDate(history.createdAt) }}</td>
                  <td class="px-4 py-3">{{ history.trangThaiCu || "Không có" }}</td>
                  <td class="px-4 py-3">
                    <span :class="getStatusClass(history.trangThaiMoi)">{{ history.trangThaiMoi || "Không xác định" }}</span>
                  </td>
                  <td class="px-4 py-3">{{ history.ghiChu || "Không có ghi chú" }}</td>
                </tr>
                </tbody>
              </table>
            </div>
            <p v-else class="text-center text-gray-500 py-4">Không có dữ liệu lịch sử</p>
            <div class="mt-4 flex justify-end gap-2">
              <button @click="closeHistoryModal" class="bg-red-500 text-white px-4 py-2 rounded text-sm hover:bg-red-600">
                Đóng
              </button>
            </div>
          </div>
        </div>
      </transition>

      <!-- Thông tin đơn hàng -->
      <div class="mt-4">
        <div class="flex justify-between items-center">
          <h3 class="text-md font-semibold">Thông tin đơn hàng - Đơn tại quầy</h3>
          <div class="flex gap-2">
            <button v-if="order.trangThai === 'Chờ giao hàng'" @click="showCustomerForm = true" class="bg-blue-500 text-white px-3 py-1 rounded">
              Cập nhật thông tin khách hàng
            </button>
          </div>
        </div>
        <div v-if="showCustomerForm" class="bg-white p-6 rounded-lg shadow-md mt-4">
          <form @submit.prevent="saveCustomerInfo" class="space-y-4">
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium mb-1">Tên khách hàng</label>
                <input v-model="customerFormData.tenNguoiNhan" type="text" class="w-full border rounded px-3 py-2" required>
              </div>
              <div>
                <label class="block text-sm font-medium mb-1">Số điện thoại</label>
                <input v-model="customerFormData.soDienThoaiNguoiNhan" type="text" class="w-full border rounded px-3 py-2" pattern="[0-9]{10}" title="Số điện thoại phải có 10 chữ số" required>
              </div>
              <div>
                <label class="block text-sm font-medium mb-1">Tỉnh/Thành phố</label>
                <select v-model="selectedAddressProvince" @change="handleAddressProvinceChange" class="w-full border rounded px-3 py-2" required>
                  <option value="">Chọn Tỉnh/Thành phố</option>
                  <option v-for="province in provinces" :key="province.code" :value="province">
                    {{ province.name }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium mb-1">Quận/Huyện</label>
                <select v-model="selectedAddressDistrict" @change="handleAddressDistrictChange" class="w-full border rounded px-3 py-2" required :disabled="!selectedAddressProvince">
                  <option value="">Chọn Quận/Huyện</option>
                  <option v-for="district in districts" :key="district.code" :value="district">
                    {{ district.name }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium mb-1">Xã/Phường/Thị trấn</label>
                <select v-model="selectedAddressWard" @change="handleWardChange" class="w-full border rounded px-3 py-2" required :disabled="!selectedAddressDistrict">
                  <option value="">Chọn Xã/Phường/Thị trấn</option>
                  <option v-for="ward in wards" :key="ward.code" :value="ward">
                    {{ ward.name }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium mb-1">Địa chỉ chi tiết</label>
                <input v-model="customerFormData.diaChiChiTiet" type="text" class="w-full border rounded px-3 py-2" required placeholder="Số nhà, tên đường...">
              </div>
            </div>
            <div class="flex justify-end gap-2">
              <button type="button" @click="showCustomerForm = false" class="px-4 py-2 border rounded">Hủy</button>
              <button type="submit" class="px-4 py-2 bg-blue-500 text-white rounded">Lưu</button>
            </div>
          </form>
        </div>
        <div v-else class="bg-white p-6 rounded-lg shadow-md flex justify-between items-center mt-4">
          <div class="text-sm">
            <p class="mb-3"><strong>Mã:</strong> {{ order.maHoaDon || "Không có" }}</p>
            <p><strong>Số điện thoại:</strong> {{ order.soDienThoaiNguoiNhan || "Không có" }}</p>
          </div>
          <div class="text-sm">
            <p class="mb-3"><strong>Tên khách hàng:</strong> {{ order.tenNguoiNhan || "Khách lẻ" }}</p>
            <p><strong>Địa chỉ:</strong> {{ formatDiaChi(order) }}</p>
          </div>
          <div class="text-sm">
            <p class="mb-3"><strong>Loại:</strong> <span :class="getInvoiceTypeClass(order.loaiHoaDon)">{{ order.loaiHoaDon || "Không xác định" }}</span></p>
            <p><strong>Trạng thái:</strong> <span class="px-2 py-1 rounded" :class="getStatusClass(order.trangThai)">{{ order.trangThai || "Không xác định" }}</span></p>
          </div>
        </div>
      </div>

      <!-- Lịch sử thanh toán -->
      <div class="mt-4">
        <h3 class="text-md font-semibold">Lịch sử thanh toán</h3>
        <div class="bg-white p-6 rounded-lg shadow-md">
          <template v-if="order.trangThai === 'Hoàn thành' && order.hinhThucThanhToan && order.hinhThucThanhToan.phuongThucThanhToan">
            <table class="w-full text-sm text-left text-gray-500">
              <thead class="text-xs text-gray-700 uppercase bg-gray-50">
              <tr>
                <th class="px-4 py-3">Số tiền</th>
                <th class="px-4 py-3">Thời gian</th>
                <th class="px-4 py-3">Phương thức thanh toán</th>
                <th class="px-4 py-3">Trạng thái</th>
                <th class="px-4 py-3">Nhân viên xác nhận</th>
              </tr>
              </thead>
              <tbody>
              <tr class="bg-white border-b hover:bg-gray-50">
                <td class="px-4 py-3">{{ formatCurrency(order.tongTien) }}</td>
                <td class="px-4 py-3">{{ formatDate(order.updatedAt || order.lichSuHoaDons[0]?.updatedAt) }}</td>
                <td class="px-4 py-3">
                    <span :class="getPaymentClass(order.hinhThucThanhToan.phuongThucThanhToan.kieuThanhToan)">
                      {{ order.hinhThucThanhToan.phuongThucThanhToan.kieuThanhToan || 'Không xác định' }}
                    </span>
                </td>
                <td class="px-4 py-3">
                    <span :class="getStatusClass(order.trangThai)">
                      {{ order.trangThai }}
                    </span>
                </td>
                <td class="px-4 py-3">{{ order.nhanVien?.maNhanVien || 'Không có' }}</td>
              </tr>
              </tbody>
            </table>
          </template>
          <template v-if="order.trangThai === 'Đã giao hàng'">
            <div class="flex justify-end mb-4">
              <button @click="openPaymentModal" class="px-4 py-2 border border-green-500 text-green-500 rounded-lg">
                Xác nhận thanh toán
              </button>
            </div>
            <p class="text-center text-gray-500 py-4">Chưa có thông tin thanh toán. Vui lòng xác nhận để hoàn tất.</p>
          </template>
          <p v-else class="text-center text-gray-500 py-4">Không có dữ liệu</p>
        </div>
      </div>

      <!-- Danh sách sản phẩm -->
      <div class="mt-4">
        <div class="flex justify-between items-center">
          <h3 class="text-md font-semibold">Danh sách sản phẩm</h3>
          <div class="flex gap-2">
            <button v-if="order.trangThai === 'Chờ giao hàng'" @click="openAddProductModal" class="bg-blue-500 text-white px-3 py-1 rounded">
              Thêm sản phẩm
            </button>
          </div>
        </div>
        <div class="bg-white p-6 rounded-lg shadow-md mt-4">
          <div v-if="order.hoaDonChiTiets.length">
            <div v-for="item in order.hoaDonChiTiets" :key="item.id" class="grid grid-cols-6 gap-4 items-center border-b py-4">
              <div class="flex justify-center">
                <img :src="item.chiTietSanPham?.anhSanPhams?.[0]?.url || 'default-image.jpg'" alt="Sản phẩm" class="w-16 h-16 object-cover rounded-md">
              </div>
              <div>
                <p class="font-medium">{{ item.chiTietSanPham?.tenChiTietSanPham || 'Không xác định' }}</p>
                <p class="text-gray-500">Mã: {{ item.maHoaDonChiTiet }}</p>
                <p class="text-red-500">{{ formatCurrency(item.giaSanPham) }}</p>
                <p class="text-gray-500">Tác giả: {{ item.chiTietSanPham?.idTacGia?.tenTacGia || 'Không có' }}</p>
              </div>
              <div class="flex justify-center">
                <p>x{{ item.soLuong }}</p>
              </div>
              <div class="flex justify-center">
                <div class="flex items-center border rounded-lg">
                  <button @click="decreaseQuantity(item)" class="px-3 py-1 border-r" :disabled="order.trangThai !== 'Chờ giao hàng'">
                    -
                  </button>
                  <input type="text" v-model.number="item.soLuong" class="w-12 text-center border-none outline-none" :disabled="order.trangThai !== 'Chờ giao hàng'" @input="validateQuantity(item)">
                  <button @click="increaseQuantity(item)" class="px-3 py-1 border-l" :disabled="order.trangThai !== 'Chờ giao hàng'">
                    +
                  </button>
                </div>
              </div>
              <p class="text-red-500 font-semibold text-center">
                {{ formatCurrency(item.thanhTien || (item.soLuong * item.giaSanPham)) }}
              </p>
              <div class="flex justify-center">
                <button v-if="order.trangThai === 'Chờ giao hàng'" @click="removeItem(item.id)" class="text-gray-500 hover:text-red-500">
                  <i class="fas fa-trash text-xl"></i>
                </button>
              </div>
            </div>
          </div>
          <p v-else class="text-gray-500 text-center">Không có sản phẩm nào trong đơn hàng.</p>
        </div>
      </div>

      <!-- Modal thêm sản phẩm -->
      <transition name="fade">
        <div v-if="showAddProductModal" class="fixed inset-0 z-10 flex items-center justify-center bg-gray-600 bg-opacity-50">
          <div class="bg-white p-6 rounded-lg shadow-lg w-3/4 max-h-[80vh] overflow-y-auto">
            <h3 class="text-lg font-semibold mb-4">Thêm sản phẩm</h3>
            <form @submit.prevent="addProduct" class="space-y-4">
              <!-- Thanh tìm kiếm và nút hiển thị tất cả -->
              <div class="flex justify-between items-center mb-4">
                <div class="w-1/2">
                  <label class="block text-sm font-medium mb-1">Tìm kiếm</label>
                  <input v-model="productSearchQuery" @input="searchProducts" type="text" class="w-full border rounded px-3 py-2" placeholder="Nhập mã hoặc tên sách...">
                </div>
                <button @click="showAllProducts" type="button" class="text-white bg-gradient-to-r from-purple-500 via-purple-600 to-purple-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-purple-300 shadow-lg shadow-purple-500/50 font-medium rounded-lg text-sm px-5 py-2.5">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 inline mr-2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 12h16.5m-16.5 3.75h16.5M3.75 9.75h16.5" />
                  </svg>
                  Hiển thị tất cả
                </button>
              </div>

              <!-- Bộ lọc -->
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

              <!-- Danh sách sản phẩm -->
              <div v-if="filteredProducts.length" class="max-h-64 overflow-y-auto">
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
                      <button @click="selectProduct(product)" type="button" class="text-blue-500">Chọn</button>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
              <p v-else-if="productSearchQuery || allProducts.length" class="text-gray-500">Không tìm thấy sản phẩm phù hợp.</p>
              <p v-else class="text-gray-500">Vui lòng tìm kiếm hoặc nhấn "Hiển thị tất cả" để xem danh sách sản phẩm.</p>

              <!-- Sản phẩm đã chọn -->
              <div v-if="selectedProduct" class="mt-4">
                <p class="font-medium">Sản phẩm đã chọn: {{ selectedProduct.tenChiTietSanPham }}</p>
                <div class="flex items-center mt-2">
                  <label class="block text-sm font-medium mr-2">Số lượng</label>
                  <input v-model.number="selectedProductQuantity" type="number" min="1" :max="selectedProduct.soLuongTon" class="w-20 border rounded px-3 py-2" required>
                </div>
              </div>

              <!-- Nút điều khiển -->
              <div class="flex justify-end gap-2 mt-4">
                <button type="button" @click="closeAddProductModal" class="bg-red-500 text-white px-4 py-2 rounded">Hủy</button>
                <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded" :disabled="!selectedProduct">Thêm</button>
              </div>
            </form>
          </div>
        </div>
      </transition>

      <!-- Phiếu giảm giá -->
      <div class="mt-4">
        <h3 class="text-md font-semibold">Phiếu giảm giá</h3>
        <div class="bg-white p-6 rounded-lg shadow-md flex justify-between items-center">
          <div class="text-sm">
            <p class="mb-3">Phiếu giảm giá: <strong>{{ order.phieuGiamGia?.maPhieuGiamGia || "Không có" }}</strong></p>
            <p class="mb-3">Giảm giá từ cửa hàng: <strong>0%</strong></p>
          </div>
          <div class="text-sm">
            <p class="mb-3">Tổng tiền hàng: <strong>{{ formatCurrency(tongTienHang) }}</strong></p>
            <p class="mb-3">Giảm giá: <strong>{{ formatCurrency(tienGiamGia) }}</strong></p>
            <p class="mb-3">Phí vận chuyển: <strong>{{ formatCurrency(order.phiShip || 0) }}</strong></p>
            <hr>
            <p class="mb-3">Tổng tiền: <strong class="text-red-500">{{ formatCurrency(thanhTien) }}</strong></p>
          </div>
        </div>
      </div>

      <!-- Modal thanh toán khi xác nhận -->
      <transition name="fade">
        <div v-if="showPaymentModal" class="fixed inset-0 z-50 flex items-center justify-center bg-gray-600 bg-opacity-50">
          <div class="bg-white p-6 rounded-lg shadow-lg w-[400px] z-50">
            <h3 class="text-lg font-semibold mb-4">Xác nhận thanh toán</h3>
            <form @submit.prevent="submitPayment" class="space-y-4">
              <div class="mb-4">
                <label class="block text-sm font-medium mb-1">Phương thức thanh toán</label>
                <div class="flex items-center mb-2">
                  <input v-model="paymentMethod" type="radio" id="tienMat" value="1" class="mr-2" required>
                  <label for="tienMat" class="flex items-center">
                    <i class="fas fa-money-bill-wave mr-2"></i> Tiền mặt
                  </label>
                </div>
                <div class="flex items-center">
                  <input v-model="paymentMethod" type="radio" id="chuyenKhoan" value="2" class="mr-2" required>
                  <label for="chuyenKhoan" class="flex items-center">
                    <i class="fas fa-university mr-2"></i> Chuyển khoản
                  </label>
                </div>
              </div>
              <div class="mb-4">
                <label class="block text-sm font-medium mb-1">Tổng tiền</label>
                <input v-model="totalAmount" type="text" class="w-full border rounded px-3 py-2" readonly>
              </div>
              <div class="mb-4">
                <label class="block text-sm font-medium mb-1">Ghi chú</label>
                <textarea v-model="paymentNote" class="w-full border rounded px-3 py-2" placeholder="Nhập ghi chú (nếu có)"></textarea>
              </div>
              <div class="flex justify-end gap-2">
                <button type="button" @click="showPaymentModal = false" class="bg-red-500 text-white px-4 py-2 rounded">
                  Hủy
                </button>
                <button type="submit" class="bg-green-500 text-white px-4 py-2 rounded">
                  Xác nhận
                </button>
              </div>
            </form>
          </div>
        </div>
      </transition>
    </div>
  </div>
</template>

<script>
import HoaDonService from "@/service/hoaDonService";
import AddressService from "@/service/AddressService";
import ChiTietSanPhamService from "@/service/ChiTietSanPhamService";
import LoaiBiaService from "@/service/LoaiBiaService";
import TacGiaService from "@/service/TacGiaService";
import NhaXuatBanService from "@/service/NhaXuatBanService";
import ChatLieuService from "@/service/ChatLieuService";
import NguoiDichService from "@/service/NguoiDichService";
import TheLoaiService from "@/service/TheLoaiService";
import NgonNguService from "@/service/NgonNguService";
import AnhSanPhamService from "@/service/AnhSanPhamService";
import { reactive } from "vue";

export default {
  computed: {
    tongTienHang() {
      return this.order.hoaDonChiTiets.reduce((total, item) => {
        return total + (item.thanhTien || item.soLuong * item.giaSanPham);
      }, 0);
    },

    sortedOrderHistory() {
      return [...this.order.lichSuHoaDons]
          .filter(history => history.createdAt)
          .sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt));
    },

    tienGiamGia() {
      const phanTramGiam = this.order.phieuGiamGia?.soPhanTramGiam || 0;
      return (this.tongTienHang * phanTramGiam) / 100;
    },

    thanhTien() {
      const phiShip = this.order.phiShip || 0;
      return this.tongTienHang - this.tienGiamGia + phiShip;
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
                item.trongLuong === numericQuery ||
                item.kichThuoc === numericQuery
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
  },

  data() {
    return {
      order: {
        phieuGiamGia: {},
        nhanVien: {},
        hinhThucThanhToan: {},
        lichSuHoaDons: [],
        hoaDonChiTiets: [],
        diaChi: {},
      },
      loading: true,
      showHistoryModal: false,
      showCustomerForm: false,
      showAddProductModal: false,
      showPaymentModal: false,
      customerFormData: {
        tenNguoiNhan: '',
        soDienThoaiNguoiNhan: '',
        diaChiChiTiet: '',
        tinhThanh: '',
        quanHuyen: '',
        xaPhuong: '',
      },
      provinces: [],
      districts: [],
      wards: [],
      selectedAddressProvince: null,
      selectedAddressDistrict: null,
      selectedAddressWard: null,
      productSearchQuery: '',
      searchResults: [],
      allProducts: [],
      selectedProduct: null,
      selectedProductQuantity: 1,
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
      paymentMethod: "1",
      totalAmount: "",
      paymentNote: "",
    };
  },

  watch: {
    filters: {
      handler() {
        this.productSearchQuery = '';
        this.searchResults = [];
      },
      deep: true
    },
    allProducts: {
      handler() {
        this.clearImageIntervals();
        if (document.visibilityState === 'visible') {
          this.setupImageIntervals();
        }
      },
      deep: true
    },
    searchResults: {
      handler() {
        this.clearImageIntervals();
        if (document.visibilityState === 'visible') {
          this.setupImageIntervals();
        }
      },
      deep: true
    }
  },

  methods: {
    async fetchOrder() {
      try {
        const orderId = this.$route.params.id;
        if (!orderId) {
          console.error("Thiếu ID hóa đơn");
          alert("Không tìm thấy ID hóa đơn!");
          this.loading = false;
          return;
        }
        console.log("Đang tải hóa đơn với ID:", orderId);
        const response = await HoaDonService.getListHoaDonById(orderId);
        if (response.data) {
          console.log("Dữ liệu hóa đơn:", response.data);
          this.order = {
            ...response.data,
            lichSuHoaDons: Array.isArray(response.data.lichSuHoaDons) ? response.data.lichSuHoaDons : [],
            hoaDonChiTiets: Array.isArray(response.data.hoaDonChiTiets) ? response.data.hoaDonChiTiets : [],
            phieuGiamGia: response.data.phieuGiamGia || {},
            nhanVien: response.data.nhanVien || {},
            hinhThucThanhToan: response.data.hinhThucThanhToan || {},
            diaChi: response.data.diaChi || {},
          };
          this.customerFormData = {
            tenNguoiNhan: this.order.tenNguoiNhan || '',
            soDienThoaiNguoiNhan: this.order.soDienThoaiNguoiNhan || '',
            diaChiChiTiet: this.order.diaChi?.diaChiChiTiet || '',
            tinhThanh: this.order.diaChi?.tinhThanh || '',
            quanHuyen: this.order.diaChi?.quanHuyen || '',
            xaPhuong: this.order.diaChi?.xaPhuong || '',
          };
          if (this.order.tongTien === null || this.order.tongTien === undefined) {
            this.order.tongTien = this.thanhTien;
          }
          // Đảm bảo hinhThucThanhToan được cập nhật từ API
          if (!this.order.hinhThucThanhToan.phuongThucThanhToan) {
            await this.loadPaymentMethod();
          }
        }
      } catch (error) {
        console.error("Lỗi khi tải hóa đơn:", error);
        alert("Có lỗi xảy ra khi tải hóa đơn!");
      } finally {
        this.loading = false;
      }
    },

    async loadProvinces() {
      try {
        this.provinces = await AddressService.getProvinces();
      } catch (error) {
        console.error("Lỗi khi tải danh sách tỉnh/thành phố:", error);
        alert("Không thể tải danh sách tỉnh/thành phố!");
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
        console.log("[DEBUG] Loaded initial data:", {
          loaiBiaList: this.loaiBiaList.length,
          tacGiaList: this.tacGiaList.length,
          nhaXuatBanList: this.nhaXuatBanList.length,
          chatLieuList: this.chatLieuList.length,
          nguoiDichList: this.nguoiDichList.length,
          theLoaiList: this.theLoaiList.length,
          ngonNguList: this.ngonNguList.length
        });
      } catch (error) {
        console.error("[ERROR] Error loading initial data:", error);
        alert("Có lỗi xảy ra khi tải dữ liệu danh sách");
      }
    },

    async handleAddressProvinceChange() {
      this.districts = [];
      this.wards = [];
      this.selectedAddressDistrict = null;
      this.selectedAddressWard = null;
      this.customerFormData.tinhThanh = this.selectedAddressProvince ? this.selectedAddressProvince.name : '';

      if (this.selectedAddressProvince) {
        try {
          this.districts = await AddressService.getDistrictsByProvinceCode(this.selectedAddressProvince.code);
        } catch (error) {
          console.error("Lỗi khi tải danh sách quận/huyện:", error);
          alert("Không thể tải danh sách quận/huyện!");
        }
      }
    },

    async handleAddressDistrictChange() {
      this.wards = [];
      this.selectedAddressWard = null;
      this.customerFormData.quanHuyen = this.selectedAddressDistrict ? this.selectedAddressDistrict.name : '';

      if (this.selectedAddressDistrict) {
        try {
          this.wards = await AddressService.getWardsByDistrictCode(this.selectedAddressDistrict.code);
        } catch (error) {
          console.error("Lỗi khi tải danh sách xã/phường:", error);
          alert("Không thể tải danh sách xã/phường!");
        }
      }
    },

    handleWardChange() {
      this.customerFormData.xaPhuong = this.selectedAddressWard ? this.selectedAddressWard.name : '';
    },

    async saveCustomerInfo() {
      try {
        const orderId = this.$route.params.id;
        if (!orderId) {
          console.error("Thiếu ID hóa đơn để cập nhật thông tin khách hàng");
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }

        const updatedData = {
          tenNguoiNhan: this.customerFormData.tenNguoiNhan,
          soDienThoaiNguoiNhan: this.customerFormData.soDienThoaiNguoiNhan,
          diaChi: {
            diaChiChiTiet: this.customerFormData.diaChiChiTiet,
            tinhThanh: this.customerFormData.tinhThanh,
            quanHuyen: this.customerFormData.quanHuyen,
            xaPhuong: this.customerFormData.xaPhuong,
          },
        };

        const response = await HoaDonService.updateCustomerInfo(orderId, updatedData);
        if (response.status === 200) {
          alert("Cập nhật thông tin khách hàng thành công!");
          this.showCustomerForm = false;
          await this.fetchOrder();
        } else {
          console.error("Cập nhật thông tin khách hàng thất bại:", response.status);
          alert("Có lỗi xảy ra khi cập nhật thông tin khách hàng!");
        }
      } catch (error) {
        console.error("Lỗi khi cập nhật thông tin khách hàng:", error);
        alert("Có lỗi xảy ra khi cập nhật thông tin khách hàng. Vui lòng thử lại!");
      }
    },

    formatDiaChi(order) {
      if (!order.diaChi) return "Không có";
      const { diaChiChiTiet, xaPhuong, quanHuyen, tinhThanh } = order.diaChi;
      return `${diaChiChiTiet || ''}, ${xaPhuong || ''}, ${quanHuyen || ''}, ${tinhThanh || ''}`;
    },

    getStatusClass(status) {
      return {
        "Tạo hóa đơn": "text-gray-500 bg-gray-200 px-2 py-1 rounded",
        "Chờ xác nhận": "text-yellow-500 bg-yellow-200 px-2 py-1 rounded",
        "Chờ giao hàng": "text-yellow-500 bg-yellow-200 px-2 py-1 rounded",
        "Đang vận chuyển": "text-blue-500 bg-blue-200 px-2 py-1 rounded",
        "Đã giao hàng": "text-green-500 bg-green-200 px-2 py-1 rounded",
        "Hoàn thành": "text-green-500 bg-green-200 px-2 py-1 rounded",
      }[status] || "text-gray-500 bg-gray-200 px-2 py-1 rounded";
    },

    formatDate(dateString) {
      if (!dateString) return "Không có dữ liệu";
      return new Date(dateString).toLocaleString("vi-VN");
    },

    formatCurrency(value) {
      return new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND",
      }).format(value || 0);
    },

    getInvoiceTypeClass(type) {
      const typeClasses = {
        "Tại quầy": "text-purple-600 bg-purple-100 px-2 py-1 rounded",
        "Trực tuyến": "text-indigo-600 bg-indigo-100 px-2 py-1 rounded",
      };
      return typeClasses[type] || "text-gray-600 bg-gray-100 px-2 py-1 rounded";
    },

    getPaymentClass(pttt) {
      switch (pttt) {
        case "Tiền mặt":
          return "bg-green-200 text-green-800 px-2 py-1 rounded";
        case "Chuyển khoản":
          return "bg-blue-200 text-blue-800 px-2 py-1 rounded";
        case "Tiền mặt và Chuyển khoản":
          return "bg-orange-200 text-orange-800 px-2 py-1 rounded";
        default:
          return "bg-gray-200 text-gray-800 px-2 py-1 rounded";
      }
    },

    increaseQuantity(item) {
      if (this.order.trangThai !== 'Chờ giao hàng') return;
      item.soLuong = Number(item.soLuong) + 1;
      item.thanhTien = item.soLuong * item.giaSanPham;
      this.updateProductQuantity(item);
    },

    decreaseQuantity(item) {
      if (this.order.trangThai !== 'Chờ giao hàng') return;
      if (item.soLuong > 1) {
        item.soLuong = Number(item.soLuong) - 1;
        item.thanhTien = item.soLuong * item.giaSanPham;
        this.updateProductQuantity(item);
      }
    },

    validateQuantity(item) {
      if (this.order.trangThai !== 'Chờ giao hàng') return;
      if (!/^\d+$/.test(item.soLuong) || Number(item.soLuong) < 1) {
        item.soLuong = 1;
      }
      item.thanhTien = item.soLuong * item.giaSanPham;
      this.updateProductQuantity(item);
    },

    async updateProductQuantity(item) {
      try {
        const orderId = this.$route.params.id;
        const updatedData = {
          soLuong: item.soLuong,
          thanhTien: item.thanhTien,
        };
        const response = await HoaDonService.updateProductQuantity(orderId, item.id, updatedData);
        if (response.status === 200) {
          console.log("Cập nhật số lượng sản phẩm thành công:", item);
          await this.fetchOrder(); // Làm mới dữ liệu để đồng bộ
        } else {
          console.error("Cập nhật số lượng thất bại:", response.status);
          alert("Có lỗi xảy ra khi cập nhật số lượng!");
        }
      } catch (error) {
        console.error("Lỗi khi cập nhật số lượng:", error);
        alert("Có lỗi xảy ra khi cập nhật số lượng. Vui lòng thử lại!");
      }
    },

    openHistoryModal() {
      this.showHistoryModal = true;
    },

    closeHistoryModal() {
      this.showHistoryModal = false;
    },

    openAddProductModal() {
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
      this.loadInitialData();
    },

    closeAddProductModal() {
      this.showAddProductModal = false;
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
      this.clearImageIntervals();
    },

    async searchProducts() {
      if (this.productSearchQuery.trim()) {
        try {
          console.log("Đang tìm kiếm sản phẩm với từ khóa:", this.productSearchQuery);
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
          console.log("Kết quả tìm kiếm:", this.searchResults);
        } catch (error) {
          console.error("Lỗi khi tìm kiếm sản phẩm:", error);
          alert("Có lỗi xảy ra khi tìm kiếm sản phẩm!");
        }
      } else {
        this.searchResults = [];
        this.filters.searchQuery = '';
      }
    },

    async showAllProducts() {
      try {
        console.log("Đang tải toàn bộ sản phẩm chi tiết");
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
        console.log("Đã tải toàn bộ sản phẩm:", this.allProducts.length);
      } catch (error) {
        console.error("Lỗi khi tải toàn bộ sản phẩm:", error);
        alert("Có lỗi xảy ra khi tải danh sách sản phẩm!");
      }
    },

    setupImageIntervals() {
      const products = this.productSearchQuery ? this.searchResults : this.allProducts;
      products.forEach(product => {
        if (product.id && product.anhSanPhams && product.anhSanPhams.length > 1) {
          console.log("[DEBUG] Setting up interval for chiTietSanPhamId:", product.id, "with", product.anhSanPhams.length, "images");
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
        alert("Vui lòng chọn một sản phẩm trước khi thêm!");
        return;
      }
      if (this.selectedProductQuantity > this.selectedProduct.soLuongTon) {
        alert(`Số lượng vượt quá số lượng tồn kho (${this.selectedProduct.soLuongTon})!`);
        return;
      }
      if (!confirm("Bạn có chắc chắn muốn thêm sản phẩm này vào hóa đơn?")) return;
      try {
        const orderId = this.$route.params.id;
        const productData = {
          chiTietSanPhamId: this.selectedProduct.id,
          soLuong: this.selectedProductQuantity,
          giaSanPham: this.selectedProduct.gia,
        };
        console.log("Đang thêm sản phẩm vào hóa đơn:", productData);
        const response = await HoaDonService.addProductToOrder(orderId, productData);
        if (response.status === 200) {
          alert("Thêm sản phẩm thành công!");
          this.closeAddProductModal();
          await this.fetchOrder();
        } else {
          console.error("Thêm sản phẩm thất bại:", response.status);
          alert("Có lỗi xảy ra khi thêm sản phẩm!");
        }
      } catch (error) {
        console.error("Lỗi khi thêm sản phẩm:", error.response?.data || error.message);
        alert(error.response?.data || "Có lỗi xảy ra khi thêm sản phẩm!");
      }
    },

    async xacNhanHoaDon() {
      try {
        const orderId = this.$route.params.id;
        if (!orderId) {
          console.error("Thiếu ID hóa đơn để xác nhận");
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }
        const response = await HoaDonService.updateTrangThaiHoaDon(orderId, "Chờ giao hàng");
        if (response.status === 200) {
          console.log("Cập nhật trạng thái thành công:", response.data);
          alert("Xác nhận hóa đơn thành công!");
          await this.fetchOrder();
        } else {
          console.error("Cập nhật trạng thái thất bại:", response.status);
          alert("Có lỗi xảy ra khi xác nhận hóa đơn!");
        }
      } catch (error) {
        console.error("Lỗi khi xác nhận hóa đơn:", error);
        alert("Có lỗi xảy ra khi xác nhận hóa đơn. Vui lòng thử lại!");
      }
    },

    async xacNhanGiaoHang() {
      try {
        const orderId = this.$route.params.id;
        if (!orderId) {
          console.error("Thiếu ID hóa đơn để xác nhận giao hàng");
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }
        const response = await HoaDonService.updateTrangThaiHoaDon(orderId, "Đang vận chuyển");
        if (response.status === 200) {
          console.log("Cập nhật trạng thái giao hàng thành công:", response.data);
          alert("Xác nhận giao hàng thành công!");
          await this.fetchOrder();
        } else {
          console.error("Cập nhật trạng thái thất bại:", response.status);
          alert("Có lỗi xảy ra khi xác nhận giao hàng!");
        }
      } catch (error) {
        console.error("Lỗi khi xác nhận giao hàng:", error);
        alert("Có lỗi xảy ra khi xác nhận giao hàng. Vui lòng thử lại!");
      }
    },

    async xacNhanLayHang() {
      try {
        const orderId = this.$route.params.id;
        if (!orderId) {
          console.error("Thiếu ID hóa đơn để xác nhận lấy hàng");
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }
        const response = await HoaDonService.updateTrangThaiHoaDon(orderId, "Đã giao hàng");
        if (response.status === 200) {
          console.log("Cập nhật trạng thái lấy hàng thành công:", response.data);
          alert("Xác nhận lấy hàng thành công!");
          await this.fetchOrder();
        } else {
          console.error("Cập nhật trạng thái thất bại:", response.status);
          alert("Có lỗi xảy ra khi xác nhận lấy hàng!");
        }
      } catch (error) {
        console.error("Lỗi khi xác nhận lấy hàng:", error);
        alert("Có lỗi xảy ra khi xác nhận lấy hàng. Vui lòng thử lại!");
      }
    },

    openPaymentModal() {
      if (!confirm("Bạn có chắc chắn muốn xác nhận thanh toán?")) return;
      this.totalAmount = this.formatCurrency(this.thanhTien);
      this.showPaymentModal = true;
    },

    async submitPayment() {
      try {
        const orderId = this.$route.params.id;
        if (!orderId) {
          console.error("Thiếu ID hóa đơn để xác nhận thanh toán");
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }
        const paymentData = {
          phuongThucThanhToanId: this.paymentMethod,
          tienMat: this.paymentMethod === "1" ? this.thanhTien : 0,
          chuyenKhoan: this.paymentMethod === "2" ? this.thanhTien : 0,
          ghiChu: this.paymentNote,
        };
        console.log("Gửi dữ liệu thanh toán:", paymentData);
        const response = await HoaDonService.updatePayment(orderId, paymentData);
        if (response.status === 200) {
          console.log("Xác nhận thanh toán thành công:", response.data);
          await HoaDonService.updateTrangThaiHoaDon(orderId, "Hoàn thành");
          alert("Xác nhận thanh toán thành công!");
          this.showPaymentModal = false;
          await this.fetchOrder();
        } else {
          console.error("Xác nhận thanh toán thất bại:", response.status);
          alert("Có lỗi xảy ra khi xác nhận thanh toán!");
        }
      } catch (error) {
        console.error("Lỗi khi xác nhận thanh toán:", error);
        alert("Có lỗi xảy ra khi xác nhận thanh toán. Vui lòng thử lại!");
      }
    },

    async loadPaymentMethod() {
      try {
        const orderId = this.$route.params.id;
        if (!orderId) return;
        const response = await HoaDonService.getPaymentMethod(orderId); // Giả định API này
        if (response.status === 200) {
          this.order.hinhThucThanhToan = response.data;
        }
      } catch (error) {
        console.error("Lỗi khi tải phương thức thanh toán:", error);
      }
    },

    async huyDon() {
      if (!confirm("Bạn có chắc chắn muốn hủy đơn? Hành động này không thể hoàn tác!")) return;
      try {
        const orderId = this.$route.params.id;
        if (!orderId) {
          console.error("Thiếu ID hóa đơn để hủy");
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }
        const response = await HoaDonService.deleteHoaDon(orderId);
        if (response.status === 200) {
          console.log("Hủy đơn thành công:", response.data);
          alert("Hủy đơn thành công!");
          await this.fetchOrder();
        } else {
          console.error("Hủy đơn thất bại:", response.status);
          alert("Có lỗi xảy ra khi hủy đơn!");
        }
      } catch (error) {
        console.error("Lỗi khi hủy đơn:", error);
        alert("Có lỗi xảy ra khi hủy đơn. Vui lòng thử lại!");
      }
    },

    async quayVeTrangThai() {
      if (!confirm("Bạn có chắc chắn muốn quay về trạng thái trước? Hành động này không thể hoàn tác!")) return;
      const orderId = this.$route.params.id;
      if (!orderId) {
        console.error("Thiếu ID hóa đơn để quay về trạng thái trước");
        alert("Không tìm thấy ID hóa đơn!");
        return;
      }
      const currentIndex = this.sortedOrderHistory.length - 1;
      const previousStatus = this.sortedOrderHistory[currentIndex - 1];
      if (previousStatus) {
        try {
          const response = await HoaDonService.updateTrangThaiHoaDon(orderId, previousStatus.trangThaiMoi);
          if (response.status === 200) {
            console.log("Cập nhật trạng thái thành công:", response.data);
            alert("Quay về trạng thái trước thành công!");
            await this.fetchOrder();
          } else {
            console.error("Cập nhật trạng thái thất bại:", response.status);
            alert("Có lỗi xảy ra khi quay về trạng thái trước!");
          }
        } catch (error) {
          console.error("Lỗi khi quay về trạng thái trước:", error);
          alert("Có lỗi xảy ra khi quay về trạng thái trước. Vui lòng thử lại!");
        }
      } else {
        alert("Không có trạng thái trước để quay về!");
      }
    },

    async removeItem(itemId) {
      if (!confirm("Bạn có chắc chắn muốn xóa sản phẩm này? Hành động này không thể hoàn tác!")) return;
      try {
        const orderId = this.$route.params.id;
        console.log("Đang xóa sản phẩm với ID:", itemId);
        await HoaDonService.removeProductFromOrder(orderId, itemId);
        this.order.hoaDonChiTiets = this.order.hoaDonChiTiets.filter((item) => item.id !== itemId);
        alert("Xóa sản phẩm thành công!");
      } catch (error) {
        console.error("Lỗi khi xóa sản phẩm:", error);
        alert("Có lỗi xảy ra khi xóa sản phẩm!");
      }
    },

    async printInvoice() {
      try {
        const orderId = this.$route.params.id;
        if (!orderId) {
          console.error("Thiếu ID hóa đơn để in");
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }
        console.log("Đang yêu cầu in hóa đơn với ID:", orderId);
        const response = await HoaDonService.printInvoice(orderId);
        if (response.status === 200) {
          console.log("Nhận được file PDF từ API");
          const blob = new Blob([response.data], { type: "application/pdf" });
          const url = window.URL.createObjectURL(blob);
          const pdfWindow = window.open(url);
          if (pdfWindow) {
            pdfWindow.onload = () => {
              pdfWindow.focus();
              pdfWindow.print();
            };
          } else {
            console.error("Trình duyệt đã chặn popup. Vui lòng cho phép popup để in!");
            alert("Vui lòng cho phép popup để in hóa đơn!");
          }
          window.URL.revokeObjectURL(url);
        } else {
          console.error("API trả về mã lỗi:", response.status);
          alert("Không thể tải hóa đơn từ server!");
        }
      } catch (error) {
        console.error("Lỗi khi in hóa đơn:", error);
        alert("Có lỗi xảy ra khi in hóa đơn. Vui lòng thử lại!");
      }
    },
  },

  async mounted() {
    await this.fetchOrder();
    await this.loadProvinces();
  },

  beforeUnmount() {
    this.clearImageIntervals();
  },
};
</script>

<style scoped>
.bg-green-500 { background-color: #22c55e; }
.bg-yellow-500 { background-color: #facc15; }
.bg-yellow-400 { background-color: #fbbf24; }
.bg-gray-500 { background-color: #6b7280; }

.icon-wrapper {
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  font-size: 20px;
  color: white;
}

.progress-line {
  width: 80px;
  height: 8px;
  background-color: #d1d5db;
  border-radius: 5px;
  margin: 0 10px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.status-column {
  width: 200px;
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
</style>