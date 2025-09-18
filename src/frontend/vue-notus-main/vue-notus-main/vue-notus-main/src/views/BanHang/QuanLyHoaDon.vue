<template>
  <div class="p-6 bg-gray-100 min-h-screen">
    <!-- Quản lý đơn hàng -->
    <div ref="invoice" class="bg-white p-4 rounded-lg shadow-md">
      <div class="flex items-center justify-between">
        <h2 class="text-lg font-semibold">Quản lý hóa đơn / <span class="text-gray-400">{{ order.maHoaDon || "Không có" }}</span></h2>
        <!-- Badge phương thức thanh toán -->
        <div v-if="order.phuongThucThanhToan" class="flex items-center space-x-2">
          <i :class="isVNPayOrder ? 'fas fa-credit-card text-blue-600' :
                    isCOrder ? 'fas fa-truck text-orange-600' : 
                    'fas fa-money-bill-wave text-green-600'"></i>
          <span :class="['px-3 py-1 text-sm rounded-full font-semibold',
            isVNPayOrder ? 'bg-blue-100 text-blue-800' :
            isCOrder ? 'bg-orange-100 text-orange-800' : 
            'bg-green-100 text-green-800']">
            <i :class="isVNPayOrder ? 'fas fa-credit-card mr-1' :
                      isCOrder ? 'fas fa-truck mr-1' : 
                      'fas fa-money-bill-wave mr-1'"></i>
            {{ isVNPayOrder ? 'Chuyển khoản' :
               isCOrder ? 'COD' : 
               isTienMatOrder ? 'Tiền mặt' : 
               'Không xác định' }}
          </span>
        </div>
      </div>

      <!-- Lịch sử đơn hàng dạng timeline -->
      <div v-if="order.lichSuHoaDons.length" class="bg-white p-6 rounded-lg shadow-md">
        <!-- Hiển thị loại hóa đơn -->
        <div class="mb-4 p-3 rounded-lg" 
             :class="isVNPayOrder ? 'bg-blue-50 border border-blue-200' : 
                     isCOrder ? 'bg-orange-50 border border-orange-200' : 
                     'bg-green-50 border border-green-200'">
          <div class="flex items-center space-x-2">
            <i :class="isVNPayOrder ? 'fas fa-credit-card text-blue-600' : 
                      isCOrder ? 'fas fa-truck text-orange-600' : 
                      'fas fa-money-bill-wave text-green-600'"></i>
            <span class="font-semibold" :class="isVNPayOrder ? 'text-blue-700' : 
                                               isCOrder ? 'text-orange-700' : 
                                               'text-green-700'">
              {{ isCOrder ? 'Hóa đơn online (COD)' :
                 isVNPayOrder ? 
                   (isOnlineOrder ? 'Hóa đơn online (Chuyển khoản VNPay)' : 'Hóa đơn tại quầy (Chuyển khoản)') :
                 isTienMatOrder ? 'Hóa đơn tại quầy (Tiền mặt)' : 
                 'Hóa đơn không xác định' }}
            </span>
          </div>
        </div>
        
        <div class="flex items-center overflow-x-auto space-x-4 py-4">
          <div v-for="(status, index) in sortedOrderHistory" :key="status.id" class="flex items-center">
            <div class="flex flex-col items-center">
              <div class="icon-wrapper" :class="getStatusClass(status.trangThaiMoi)">
                <i v-if="status.trangThaiMoi === 'Tạo hóa đơn'" class="fas fa-file-alt"></i>
                <i v-if="status.trangThaiMoi === 'Chờ xác nhận'" class="fas fa-clock"></i>
                <i v-if="status.trangThaiMoi === 'Thanh toán thành công'" class="fas fa-credit-card"></i>
                <i v-if="status.trangThaiMoi === 'Chờ giao hàng'" class="fas fa-truck"></i>
                <i v-if="status.trangThaiMoi === 'Đang vận chuyển'" class="fas fa-shipping-fast"></i>
                <i v-if="status.trangThaiMoi === 'Đã giao hàng'" class="fas fa-check-circle"></i>
                <i v-if="status.trangThaiMoi === 'Đã thanh toán'" class="fas fa-money-bill-wave"></i>
                <i v-if="status.trangThaiMoi === 'Hoàn thành'" class="fas fa-check-double"></i>
              </div>
              <p class="mt-2 font-semibold text-center">{{ status.trangThaiMoi || "Không xác định" }}</p>
              <p class="text-sm text-gray-500">{{ formatDate(status.createdAt) }}</p>
            </div>
            <div v-if="index < sortedOrderHistory.length - 1" class="progress-line"></div>
          </div>
        </div>

        <!-- Nút quay về trạng thái trước -->
        <div class="mt-4 flex justify-end">
          <button v-if="order.trangThai === 'Chờ giao hàng'" @click="quayVeTrangThai" class="bg-orange-500 text-white px-4 py-2 rounded">
            Quay về trạng thái trước
          </button>
        </div>
      </div>

      <!-- Nút điều khiển trạng thái đơn hàng -->
      <div class="bg-white p-6 rounded-lg shadow-md mt-4 flex justify-between items-center">
        <div class="flex gap-2">
          <!-- COD: Chờ xác nhận → Chờ giao hàng -->
          <button v-if="isCOrder && order.trangThai === 'Chờ xác nhận'" @click="xacNhanHoaDon" class="px-4 py-2 border border-green-500 text-green-500 rounded-lg">
            <i class="fas fa-check mr-1"></i>Xác nhận hóa đơn
          </button>

          <!-- Tại quầy (Tiền mặt): Chờ xác nhận → Chờ giao hàng -->
          <button v-if="isTienMatOrder && order.trangThai === 'Chờ xác nhận'" @click="xacNhanHoaDon" class="px-4 py-2 border border-green-500 text-green-500 rounded-lg">
            <i class="fas fa-check mr-1"></i>Xác nhận hóa đơn
          </button>

          <!-- Chuyển khoản: Thanh toán thành công → Chờ giao hàng -->
          <button v-if="isVNPayOrder && order.trangThai === 'Thanh toán thành công'" @click="xacNhanGiaoHangVNPay" class="px-4 py-2 border border-blue-500 text-blue-500 rounded-lg">
            <i class="fas fa-truck mr-1"></i>Xác nhận giao hàng
          </button>

          <!-- COD: Chờ xác nhận → Chờ giao hàng -->

          <!-- COD & Chuyển khoản: Chờ giao hàng → Đang vận chuyển -->
          <button v-if="order.trangThai === 'Chờ giao hàng'" @click="xacNhanGiaoHang" class="px-4 py-2 border border-blue-500 text-blue-500 rounded-lg">
            <i class="fas fa-truck mr-1"></i>Xác nhận giao hàng
          </button>

          <!-- COD & Chuyển khoản: Đang vận chuyển → Đã giao hàng -->
          <button v-if="order.trangThai === 'Đang vận chuyển'" @click="xacNhanLayHang" class="px-4 py-2 border border-yellow-500 text-yellow-500 rounded-lg">
            <i class="fas fa-shipping-fast mr-1"></i>Xác nhận lấy hàng
          </button>

          <!-- COD: Đã giao hàng → Đã thanh toán -->
          <button
              v-if="isCOrder && order.trangThai === 'Đã giao hàng'"
              @click="openPaymentModal"
              class="px-4 py-2 border border-green-500 text-green-500 rounded-lg"
          >
            <i class="fas fa-money-bill-wave mr-1"></i>Xác nhận thanh toán COD
          </button>

          <!-- COD: Đã thanh toán → Hoàn thành -->
          <button v-if="isCOrder && order.trangThai === 'Đã thanh toán'" @click="hoanThanhDon" class="px-4 py-2 border border-green-500 text-green-500 rounded-lg">
            <i class="fas fa-check-double mr-1"></i>Hoàn thành đơn hàng
          </button>

          <!-- Chuyển khoản: Đã giao hàng → Hoàn thành -->
          <button v-if="isVNPayOrder && order.trangThai === 'Đã giao hàng'" @click="hoanThanhDon" class="px-4 py-2 border border-green-500 text-green-500 rounded-lg">
            <i class="fas fa-check-double mr-1"></i>Hoàn thành đơn hàng
          </button>

          <!-- Hủy đơn (chỉ cho Chờ xác nhận) -->
          <button v-if="order.trangThai === 'Chờ xác nhận'" @click="huyDon" class="px-4 py-2 border border-red-500 text-red-500 rounded-lg">
            <i class="fas fa-times mr-1"></i>Hủy đơn
          </button>

          <!-- In hóa đơn và chi tiết cho trạng thái Hoàn thành -->
          <template v-if="order.trangThai === 'Hoàn thành'">
            <button @click="printInvoice" class="px-4 py-2 border border-orange-400 text-orange-400 rounded-lg">
              <i class="fas fa-print mr-1"></i>In hóa đơn
            </button>
            <button @click="openHistoryModal" class="px-4 py-2 border border-orange-400 text-orange-400 rounded-lg">
              <i class="fas fa-info-circle mr-1"></i>Chi tiết
            </button>
          </template>
        </div>
        <div v-if="order.trangThai !== 'Hoàn thành'">
          <button @click="openHistoryModal" class="px-4 py-2 border border-orange-400 text-orange-400 rounded-lg">
            <i class="fas fa-info-circle mr-1"></i>Chi tiết
          </button>
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
            <button v-if="order.trangThai === 'Chờ giao hàng'" @click="openCustomerForm" class="bg-blue-500 text-white px-3 py-1 rounded">
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
            <!-- Phương thức thanh toán -->
            <div v-if="order.phuongThucThanhToan" class="mt-3 p-2 rounded-lg" 
                 :class="isVNPayOrder ? 'bg-blue-50 border border-blue-200' :
                         isCOrder ? 'bg-orange-50 border border-orange-200' : 
                         'bg-green-50 border border-green-200'">
              <p class="mb-2"><strong>Phương thức thanh toán:</strong></p>
              <div class="flex items-center space-x-2">
                <i :class="isVNPayOrder ? 'fas fa-credit-card text-blue-600' :
                          isCOrder ? 'fas fa-truck text-orange-600' : 
                          'fas fa-money-bill-wave text-green-600'"></i>
                <span :class="['text-sm font-semibold',
                  isVNPayOrder ? 'text-blue-700' :
                  isCOrder ? 'text-orange-700' : 
                  'text-green-700']">
                  {{ isVNPayOrder ? 'Chuyển khoản' :
                     isCOrder ? 'COD' : 
                     isTienMatOrder ? 'Tiền mặt' : 
                     'Không xác định' }}
                </span>
                <span :class="['px-2 py-1 text-xs rounded-full font-bold',
                  isVNPayOrder ? 'bg-blue-100 text-blue-800' :
                  isCOrder ? 'bg-orange-100 text-orange-800' : 
                  'bg-green-100 text-green-800']">
                  {{ isVNPayOrder ? 'CHUYỂN KHOẢN' :
                     isCOrder ? 'COD' : 
                     isTienMatOrder ? 'TIỀN MẶT' : 
                     'KHÔNG XÁC ĐỊNH' }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Lịch sử thanh toán -->
      <div class="mt-4">
        <h3 class="text-md font-semibold">Lịch sử thanh toán</h3>
        <div class="bg-white p-6 rounded-lg shadow-md">
          <template v-if="order.trangThai === 'Thanh toán thành công' || order.trangThai === 'Hoàn thành' || order.trangThai === 'Đã thanh toán'">
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
                <td class="px-4 py-3">{{ formatDate(order.updatedAt || order.lichSuHoaDons.find(h => h.trangThaiMoi === 'Thanh toán thành công')?.createdAt) }}</td>
                <td class="px-4 py-3">
                  <div class="flex items-center space-x-2">
                    <i :class="isVNPayOrder ? 'fas fa-credit-card text-blue-600' :
                              isCOrder ? 'fas fa-truck text-orange-600' : 
                              'fas fa-money-bill-wave text-green-600'"></i>
                    <span :class="['px-2 py-1 text-xs rounded-full font-semibold',
                      isVNPayOrder ? 'bg-blue-100 text-blue-800' :
                      isCOrder ? 'bg-orange-100 text-orange-800' : 
                      'bg-green-100 text-green-800']">
                      {{ isVNPayOrder ? 'Chuyển khoản' :
                         isCOrder ? 'COD' : 
                         isTienMatOrder ? 'Tiền mặt' : 
                         'Không xác định' }}
                    </span>
                  </div>
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
          <template v-else-if="order.trangThai === 'Đã giao hàng' && order.hinhThucThanhToan?.id === 4">
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
                <img :src="getProductImage(item)" alt="Sản phẩm" class="w-16 h-16 object-cover rounded-md">
              </div>
              <div>
                <p class="font-medium">{{ getProductName(item) }}</p>
                <p class="text-gray-500">Mã: {{ getProductCode(item) }}</p>
                <p class="text-red-500">{{ formatCurrency(item.giaSanPham) }}</p>
                <p class="text-gray-500">Tác giả: {{ getProductAuthor(item) }}</p>
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
              <div class="flex justify-center space-x-2">
                <button v-if="item.boSach" @click="openBookSetDetailModal(item.boSach)" class="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded text-xs font-medium transition-colors duration-200" title="Xem chi tiết bộ sách">
                  <i class="fas fa-info-circle mr-1"></i>
                  Chi tiết
                </button>
                <button v-if="order.trangThai === 'Chờ giao hàng'" @click="removeItem(item.id)" class="bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded text-xs font-medium transition-colors duration-200" title="Xóa sản phẩm khỏi hóa đơn">
                  <i class="fas fa-trash-alt mr-1"></i>
                  Xóa
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
              <div class="flex justify-between items-center mb-4">
                <div class="w-1/2">
                  <label class="block text-sm font-medium mb-1">Tìm kiếm</label>
                  <input v-model="productSearchQuery" @input="searchProducts" type="text" class="w-full border rounded px-3 py-2" placeholder="Nhập mã hoặc tên sách...">
                </div>
                <div class="flex gap-2">
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
              <div v-if="!isShowingBookSets && filteredProducts.length" class="max-h-64 overflow-y-auto">
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
                      <button @click="selectProduct(product, 'product')" type="button" class="text-blue-500">Chọn</button>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
              <div v-if="isShowingBookSets && allBookSets.length" class="max-h-64 overflow-y-auto">
                <table class="w-full text-sm text-left text-gray-500">
                  <thead class="text-xs text-gray-700 uppercase bg-gray-50">
                  <tr>
                    <th class="px-4 py-3">Ảnh</th>
                    <th class="px-4 py-3">Tên bộ sách</th>
                    <th class="px-4 py-3">Giá</th>
                    <th class="px-4 py-3">Mã</th>
                    <th class="px-4 py-3">Số lượng tồn</th>
                    <th class="px-4 py-3">Chọn</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="bookSet in allBookSets" :key="bookSet.id" class="bg-white border-b hover:bg-gray-50">
                    <td class="px-4 py-3">
                      <img v-if="bookSet.url" :src="bookSet.url" class="w-12 h-12 object-cover rounded" />
                      <span v-else class="text-gray-500">Không có ảnh</span>
                    </td>
                    <td class="px-4 py-3 font-medium">{{ bookSet.tenBoSach }}</td>
                    <td class="px-4 py-3 text-red-500">{{ formatCurrency(bookSet.giaTien) }}</td>
                    <td class="px-4 py-3 text-gray-500">{{ bookSet.maBoSach }}</td>
                    <td class="px-4 py-3 text-gray-500">{{ bookSet.soLuong }}</td>
                    <td class="px-4 py-3">
                      <button @click="selectProduct(bookSet, 'bookset')" type="button" class="text-blue-500">Chọn</button>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
              <p v-else-if="productSearchQuery || allProducts.length || allBookSets.length" class="text-gray-500">Không tìm thấy sản phẩm phù hợp.</p>
              <p v-else class="text-gray-500">Vui lòng tìm kiếm hoặc nhấn "Hiển thị sản phẩm/Bộ sách" để xem danh sách.</p>
              <div v-if="selectedProduct" class="mt-4">
                <p class="font-medium">
                  {{ selectedItemType === 'bookset' ? 'Bộ sách đã chọn: ' + (selectedProduct.tenBoSach || '') : 'Sản phẩm đã chọn: ' + (selectedProduct.tenChiTietSanPham || '') }}
                </p>
                <div class="flex items-center mt-2">
                  <label class="block text-sm font-medium mr-2">Số lượng</label>
                  <input v-model.number="selectedProductQuantity" type="number" min="1" :max="selectedItemType === 'bookset' ? selectedProduct.soLuong : selectedProduct.soLuongTon" class="w-20 border rounded px-3 py-2" required>
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

      <!-- Phiếu giảm giá -->
      <div class="mt-4">
        <h3 class="text-md font-semibold">Phiếu giảm giá</h3>
        <div class="bg-white p-6 rounded-lg shadow-md flex justify-between items-center">
          <div class="text-sm">
            <p class="mb-3">Phiếu giảm giá: <strong>{{ order.phieuGiamGia?.maPhieuGiamGia || "Không có" }}</strong></p>
            <p class="mb-3">Phần trăm giảm giá: <strong>{{ order.phieuGiamGia?.soPhanTramGiam || 0 }}%</strong></p>
            <p class="mb-3">Số tiền giảm giá: <strong>{{ formatCurrency(tienGiamGia) }}</strong></p>
          </div>
          <div class="text-sm">
            <p class="mb-3">Tổng tiền hàng: <strong>{{ formatCurrency(tongTienHang) }}</strong></p>
            <p class="mb-3">Phí vận chuyển: <strong>{{ formatCurrency(order.phiShip || 0) }}</strong></p>
            <hr>
            <p class="mb-3">Tổng tiền: <strong class="text-red-500">{{ formatCurrency(thanhTien) }}</strong></p>
          </div>
        </div>
      </div>

      <!-- Modal thanh toán khi xác nhận -->
      <transition name="fade">
        <div v-if="showPaymentModal" class="fixed inset-0 z-50 flex items-center justify-center bg-gray-600 bg-opacity-50">
          <div class="bg-white p-6 rounded-lg shadow-lg w-[500px] z-50">
            <h3 class="text-lg font-semibold mb-4 flex items-center">
              <i class="fas fa-credit-card text-blue-600 mr-2"></i>
              Xác nhận thanh toán
            </h3>
            <form @submit.prevent="submitPayment" class="space-y-4">
              <div class="mb-4">
                <label class="block text-sm font-medium mb-1">Tổng tiền</label>
                <input :value="formatCurrency(thanhTien)" type="text" class="w-full border rounded px-3 py-2 bg-gray-100 font-semibold text-lg text-red-600" readonly>
              </div>
              <div class="mb-4">
                <label class="block text-sm font-medium mb-1">Hình thức thanh toán</label>
                <div class="space-y-3">
                  <label class="flex items-center space-x-3 p-3 rounded-lg border-2 transition-all" 
                         :class="orderPaymentMethod === '1' ? 'border-blue-500 bg-blue-50' : 'border-gray-200 hover:border-blue-300'">
                    <input type="radio" value="1" v-model="orderPaymentMethod" class="text-sm" />
                    <i class="fas fa-credit-card text-blue-600"></i>
                    <span class="text-sm font-medium">Chuyển khoản</span>
                  </label>
                  <label class="flex items-center space-x-3 p-3 rounded-lg border-2 transition-all" 
                         :class="orderPaymentMethod === '4' ? 'border-green-500 bg-green-50' : 'border-gray-200 hover:border-green-300'">
                    <input type="radio" value="4" v-model="orderPaymentMethod" class="text-sm" />
                    <i class="fas fa-money-bill-wave text-green-600"></i>
                    <span class="text-sm font-medium">Tiền mặt</span>
                  </label>
                </div>
              </div>
              <div class="mb-4">
                <label class="block text-sm font-medium mb-1">Ghi chú</label>
                <textarea v-model="paymentNote" class="w-full border rounded px-3 py-2" placeholder="Nhập ghi chú (nếu có)"></textarea>
              </div>
              <div class="flex justify-end gap-2">
                <button type="button" @click="showPaymentModal = false" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition">
                  <i class="fas fa-times mr-1"></i>
                  Hủy
                </button>
                <button type="submit" class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition">
                  <i class="fas fa-check mr-1"></i>
                  Xác nhận
                </button>
              </div>
            </form>
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
    </div>
  </div>
</template>

<script>
import HoaDonService from "@/service/hoaDonService";
import AddressService from "@/service/AddressService";
import ChiTietSanPhamService from "@/service/ChiTietSanPhamService";
import AnhSanPhamService from "@/service/AnhSanPhamService";
import LoaiBiaService from "@/service/LoaiBiaService";
import TacGiaService from "@/service/TacGiaService";
import NhaXuatBanService from "@/service/NhaXuatBanService";
import ChatLieuService from "@/service/ChatLieuService";
import NguoiDichService from "@/service/NguoiDichService";
import TheLoaiService from "@/service/TheLoaiService";
import NgonNguService from "@/service/NgonNguService";
import { reactive } from "vue";

export default {
  computed: {
    isVNPayOrder() {
      return this.order.phuongThucThanhToan === 'VNPAY' || this.order.phuongThucThanhToanId === 1;
    },
    isCOrder() {
      return this.order.phuongThucThanhToan === 'COD' || this.order.phuongThucThanhToanId === 2;
    },
    isTienMatOrder() {
      return this.order.phuongThucThanhToan === 'TIEN_MAT' || this.order.phuongThucThanhToanId === 4;
    },
    isOnlineOrder() {
      return this.order.loaiHoaDon === 'Online' || this.order.loaiHoaDon === 'Giao hàng';
    },
    isTaiQuayOrder() {
      return this.order.loaiHoaDon === 'Tại quầy';
    },
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
      const tongTienHang = this.tongTienHang;
      const phanTramGiam = this.order.phieuGiamGia?.soPhanTramGiam || 0;
      const tienGiamGia = (tongTienHang * phanTramGiam) / 100;
      return tongTienHang - tienGiamGia + phiShip;
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
      showBookSetDetailModal: false,
      selectedBookSet: null,
      bookSetDetails: {},
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
      // Bổ sung dữ liệu cho bộ sách
      isShowingBookSets: false,
      allBookSets: [],
      selectedItemType: 'product',
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
      paymentNote: "",
      orderPaymentMethod: '4',
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
    openCustomerForm() {
      this.showCustomerForm = true;
      this.loadProvinces();
    },
    openAddProductModal() {
      this.showAddProductModal = true;
      this.productSearchQuery = '';
      this.searchResults = [];
      this.allProducts = [];
      this.allBookSets = [];
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
      this.isShowingBookSets = false;
      this.loadInitialData();
    },
    async fetchOrder() {
      try {
        this.loading = true;
        const orderId = this.$route.params.id;
        if (!orderId) {
          console.error("Thiếu ID hóa đơn");
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }
        const response = await HoaDonService.getListHoaDonById(orderId);
        if (response.data) {
          this.order = {
            ...response.data,
            lichSuHoaDons: Array.isArray(response.data.lichSuHoaDons) ? response.data.lichSuHoaDons : [],
            hoaDonChiTiets: Array.isArray(response.data.hoaDonChiTiets) ? response.data.hoaDonChiTiets : [],
            phieuGiamGia: response.data.phieuGiamGia || null,
            nhanVien: response.data.nhanVien || null,
            hinhThucThanhToan: response.data.hinhThucThanhToan || null,
            diaChi: response.data.diaChi || null,
          };
          this.customerFormData = {
            tenNguoiNhan: this.order.tenNguoiNhan || '',
            soDienThoaiNguoiNhan: this.order.soDienThoaiNguoiNhan || '',
            diaChiChiTiet: (this.order.diaChi && typeof this.order.diaChi === 'object') ? this.order.diaChi.diaChiChiTiet || '' : '',
            tinhThanh: (this.order.diaChi && typeof this.order.diaChi === 'object') ? this.order.diaChi.tinhThanh || '' : '',
            quanHuyen: (this.order.diaChi && typeof this.order.diaChi === 'object') ? this.order.diaChi.quanHuyen || '' : '',
            xaPhuong: (this.order.diaChi && typeof this.order.diaChi === 'object') ? this.order.diaChi.xaPhuong || '' : '',
          };
          if (this.order.tongTien === null || this.order.tongTien === undefined) {
            this.order.tongTien = this.thanhTien;
          }
        }
      } catch (error) {
        console.error("Lỗi khi tải hóa đơn:", error);
        alert("Có lỗi xảy ra khi tải hóa đơn: " + (error.response?.data?.message || "Vui lòng thử lại!"));
      } finally {
        this.loading = false;
      }
    },
    async xacNhanHoaDon() {
      try {
        const orderId = this.$route.params.id;
        if (!orderId) {
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }

        let newStatus;
        if (this.isVNPayOrder) {
          // Chuyển khoản: Chờ xác nhận → Thanh toán thành công
          newStatus = "Thanh toán thành công";
        } else if (this.isCOrder) {
          // COD: Chờ xác nhận → Chờ giao hàng
          newStatus = "Chờ giao hàng";
        } else {
          // Tiền mặt tại quầy: Chờ xác nhận → Chờ giao hàng
          newStatus = "Chờ giao hàng";
        }

        // Cập nhật trạng thái
        const response = await HoaDonService.updateTrangThaiHoaDon(orderId, newStatus);
        
        // Chỉ cập nhật phương thức thanh toán cho chuyển khoản
        if (this.isVNPayOrder) {
          await HoaDonService.updatePayment(orderId, {
            tienMat: 0,
            chuyenKhoan: this.order.tongTien,
            phuongThucThanhToanId: 1,
            ghiChu: this.order.ghiChu || '',
            loaiHoaDon: 'Online',
            tienKhachDua: 0,
            phieuGiamGiaId: this.order.phieuGiamGia?.id || null,
            phuongThucThanhToan: 'VNPAY'
          });
        }
        if (response.status === 200) {
          if (this.isVNPayOrder) {
            alert("Xác nhận thanh toán chuyển khoản thành công!");
          } else {
            alert("Xác nhận hóa đơn COD thành công!");
          }
          await this.fetchOrder();
        } else {
          alert("Có lỗi xảy ra khi xác nhận hóa đơn!");
        }
      } catch (error) {
        console.error("Lỗi khi xác nhận hóa đơn:", error);
        alert("Có lỗi xảy ra khi xác nhận hóa đơn: " + (error.response?.data?.message || "Vui lòng thử lại!"));
      }
    },
    async xacNhanGiaoHangVNPay() {
      try {
        const orderId = this.$route.params.id;
        if (!orderId) {
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }

        // Chuyển khoản: Thanh toán thành công → Chờ giao hàng
        const newStatus = "Chờ giao hàng";

        const response = await HoaDonService.updateTrangThaiHoaDon(orderId, newStatus);
        if (response.status === 200) {
          alert("Xác nhận giao hàng thành công!");
          await this.fetchOrder();
        } else {
          alert("Có lỗi xảy ra khi xác nhận giao hàng!");
        }
      } catch (error) {
        console.error("Lỗi khi xác nhận giao hàng:", error);
        alert("Có lỗi xảy ra khi xác nhận giao hàng: " + (error.response?.data?.message || "Vui lòng thử lại!"));
      }
    },
    async xacNhanHoaDonTuCheckout(phuongThucThanhToanId) {
      try {
        const orderId = this.$route.params.id;
        if (!orderId) {
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }

        // Lưu phương thức thanh toán vào hóa đơn
        if (phuongThucThanhToanId) {
          const paymentData = {
            phuongThucThanhToanId: phuongThucThanhToanId,
            tienMat: phuongThucThanhToanId === 4 ? this.thanhTien : 0,
            chuyenKhoan: phuongThucThanhToanId === 1 ? this.thanhTien : 0,
            tienKhachDua: phuongThucThanhToanId === 4 ? this.thanhTien : 0,
            ghiChu: "Thanh toán từ checkout",
            phieuGiamGiaId: this.order?.phieuGiamGia?.id || null,
            loaiHoaDon: this.order?.loaiHoaDon || 'Giao hàng',
            tongTien: this.thanhTien,
            phuongThucThanhToan: phuongThucThanhToanId === 1 ? 'VNPAY' : 'COD',
          };
          await HoaDonService.updatePayment(orderId, paymentData);
        }

        let newStatus;
        if (phuongThucThanhToanId === 1) {
          // Chuyển khoản: Chờ xác nhận → Thanh toán thành công
          newStatus = "Thanh toán thành công";
        } else {
          // COD: Chờ xác nhận → Chờ giao hàng
          newStatus = "Chờ giao hàng";
        }

        const response = await HoaDonService.updateTrangThaiHoaDon(orderId, newStatus);
        if (response.status === 200) {
          if (phuongThucThanhToanId === 1) {
            alert("Xác nhận thanh toán chuyển khoản thành công!");
          } else {
            alert("Xác nhận hóa đơn COD thành công!");
          }
          await this.fetchOrder();
        } else {
          alert("Có lỗi xảy ra khi xác nhận hóa đơn!");
        }
      } catch (error) {
        console.error("Lỗi khi xác nhận hóa đơn từ checkout:", error);
        alert("Có lỗi xảy ra khi xác nhận hóa đơn: " + (error.response?.data?.message || "Vui lòng thử lại!"));
      }
    },
    async xacNhanGiaoHang() {
      try {
        const orderId = this.$route.params.id;
        if (!orderId) {
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }
        
        // Chuyển từ "Chờ giao hàng" → "Đang vận chuyển"
        const response = await HoaDonService.updateTrangThaiHoaDon(orderId, "Đang vận chuyển");
        if (response.status === 200) {
          alert("Xác nhận giao hàng thành công!");
          await this.fetchOrder();
        } else {
          alert("Có lỗi xảy ra khi xác nhận giao hàng!");
        }
      } catch (error) {
        console.error("Lỗi khi xác nhận giao hàng:", error);
        alert("Có lỗi xảy ra khi xác nhận giao hàng: " + (error.response?.data?.message || "Vui lòng thử lại!"));
      }
    },
    async xacNhanLayHang() {
      try {
        const orderId = this.$route.params.id;
        if (!orderId) {
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }
        const response = await HoaDonService.updateTrangThaiHoaDon(orderId, "Đã giao hàng");
        if (response.status === 200) {
          alert("Xác nhận lấy hàng thành công!");
          await this.fetchOrder();
        } else {
          alert("Có lỗi xảy ra khi xác nhận lấy hàng!");
        }
      } catch (error) {
        console.error("Lỗi khi xác nhận lấy hàng:", error);
        alert("Có lỗi xảy ra khi xác nhận lấy hàng: " + (error.response?.data?.message || "Vui lòng thử lại!"));
      }
    },
    async hoanThanhDon() {
      try {
        const orderId = this.$route.params.id;
        if (!orderId) {
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }
        
        // Kiểm tra trạng thái hiện tại
        if (this.order.trangThai === 'Hoàn thành') {
          alert("Đơn hàng đã ở trạng thái hoàn thành!");
          return;
        }
        
        // Kiểm tra xem đã có trạng thái "Hoàn thành" trong lịch sử chưa
        const hasCompletedStatus = this.order.lichSuHoaDons && this.order.lichSuHoaDons.some(history => 
          history.trangThaiMoi === 'Hoàn thành'
        );
        
        if (hasCompletedStatus) {
          alert("Đơn hàng đã được hoàn thành trước đó!");
          return;
        }
        
        // Kiểm tra thêm: nếu trạng thái hiện tại không phù hợp để hoàn thành
        const validStatuses = ['Đã giao hàng', 'Đã thanh toán'];
        if (!validStatuses.includes(this.order.trangThai)) {
          alert("Đơn hàng chưa ở trạng thái phù hợp để hoàn thành!");
          return;
        }
        
        const response = await HoaDonService.updateTrangThaiHoaDon(orderId, "Hoàn thành");
        if (response.status === 200) {
          alert("Hoàn thành đơn hàng thành công!");
          await this.fetchOrder();
        } else {
          alert("Có lỗi xảy ra khi hoàn thành đơn hàng!");
        }
      } catch (error) {
        console.error("Lỗi khi hoàn thành đơn hàng:", error);
        alert("Có lỗi xảy ra khi hoàn thành đơn hàng: " + (error.response?.data?.message || "Vui lòng thử lại!"));
      }
    },
    async submitPayment() {
      try {
        const orderId = this.$route.params.id;
        if (!orderId) {
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }
        // Chọn hình thức thanh toán: chuyển khoản (1) hoặc tiền mặt (4)
        const isChuyenKhoan = String(this.orderPaymentMethod) === '1';
        const phieuGiamGiaId = this.order?.phieuGiamGia?.id || null;
        const tongTien = this.thanhTien;

        // Đồng bộ tổng tiền vào hóa đơn (đề phòng backend yêu cầu)
        await HoaDonService.updateHoaDon(orderId, {
          tongTien: tongTien,
          phiShip: this.order?.phiShip || 0,
        });

        const paymentData = {
          phuongThucThanhToanId: isChuyenKhoan ? 1 : 4,
          tienMat: isChuyenKhoan ? 0 : tongTien,
          chuyenKhoan: isChuyenKhoan ? tongTien : 0,
          tienKhachDua: isChuyenKhoan ? 0 : tongTien,
          ghiChu: this.paymentNote,
          phieuGiamGiaId: phieuGiamGiaId,
          loaiHoaDon: this.order?.loaiHoaDon || 'Tại quầy',
          tongTien: tongTien,
          phuongThucThanhToan: isChuyenKhoan ? 'VNPAY' : 'TIEN_MAT',
        };

        const response = await HoaDonService.updatePayment(orderId, paymentData);
        if (response.status === 200) {
          alert("Xác nhận thanh toán thành công!");
          this.showPaymentModal = false;
          // Chỉ chuyển trạng thái cho COD: Đã giao hàng → Đã thanh toán
          if (this.isCOrder && this.order.trangThai === 'Đã giao hàng') {
            await HoaDonService.updateTrangThaiHoaDon(orderId, "Đã thanh toán");
          }
          await this.fetchOrder();
        } else {
          alert("Có lỗi xảy ra khi xác nhận thanh toán!");
        }
      } catch (error) {
        console.error("Lỗi khi xác nhận thanh toán:", error);
        alert("Có lỗi xảy ra khi xác nhận thanh toán: " + (error.response?.data?.message || "Vui lòng thử lại!"));
      }
    },
    async huyDon() {
      try {
        const orderId = this.$route.params.id;
        if (!orderId) {
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }
        const response = await HoaDonService.updateTrangThaiHoaDon(orderId, "Hủy");
        if (response.status === 200) {
          alert("Hủy đơn hàng thành công!");
          await this.fetchOrder();
        } else {
          alert("Có lỗi xảy ra khi hủy đơn hàng!");
        }
      } catch (error) {
        console.error("Lỗi khi hủy đơn hàng:", error);
        alert("Có lỗi xảy ra khi hủy đơn hàng: " + (error.response?.data?.message || "Vui lòng thử lại!"));
      }
    },
    async quayVeTrangThai() {
      try {
        const orderId = this.$route.params.id;
        if (!orderId) {
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }
        const previousStatus = this.sortedOrderHistory[this.sortedOrderHistory.length - 2]?.trangThaiMoi || "Chờ xác nhận";
        const response = await HoaDonService.updateTrangThaiHoaDon(orderId, previousStatus);
        if (response.status === 200) {
          alert("Quay về trạng thái trước thành công!");
          await this.fetchOrder();
        } else {
          alert("Có lỗi xảy ra khi quay về trạng thái trước!");
        }
      } catch (error) {
        console.error("Lỗi khi quay về trạng thái trước:", error);
        alert("Có lỗi xảy ra khi quay về trạng thái trước: " + (error.response?.data?.message || "Vui lòng thử lại!"));
      }
    },
    async printInvoice() {
      try {
        const invoiceElement = this.$refs.invoice;
        if (!invoiceElement) {
          alert("Không tìm thấy nội dung hóa đơn để in!");
          return;
        }
        const printWindow = window.open('', '_blank');
        printWindow.document.write(`
          <html>
            <head>
              <title>Hóa đơn ${this.order.maHoaDon}</title>
              <style>
                body { font-family: Arial, sans-serif; padding: 20px; }
                table { width: 100%; border-collapse: collapse; }
                th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                th { background-color: #f2f2f2; }
                h2, h3 { color: #333; }
                .text-red { color: red; }
              </style>
            </head>
            <body>
              ${invoiceElement.innerHTML}
            </body>
          </html>
        `);
        printWindow.document.close();
        printWindow.print();
      } catch (error) {
        console.error("Lỗi khi in hóa đơn:", error);
        alert("Có lỗi xảy ra khi in hóa đơn: " + (error.message || "Vui lòng thử lại!"));
      }
    },
    getProductName(item) {
      if (item.boSach) {
        return item.boSach.tenBoSach || 'Bộ sách không xác định';
      } else if (item.chiTietSanPham) {
        return item.chiTietSanPham.tenChiTietSanPham || 'Sản phẩm không xác định';
      }
      return 'Sản phẩm không xác định';
    },
    getProductCode(item) {
      if (item.boSach) {
        return item.boSach.maBoSach || 'Mã không xác định';
      } else if (item.chiTietSanPham) {
        return item.chiTietSanPham.maChiTietSanPham || 'Mã không xác định';
      }
      return 'Mã không xác định';
    },
    getProductAuthor(item) {
      if (item.boSach) {
        return 'Bộ sách';
      } else if (item.chiTietSanPham?.idTacGia?.tenTacGia) {
        return item.chiTietSanPham.idTacGia.tenTacGia;
      }
      return 'Không có';
    },
    getProductImage(item) {
      if (item.boSach && item.boSach.url) {
        return item.boSach.url;
      }
      if (item.chiTietSanPham?.anhSanPhams?.[0]?.url) {
        return item.chiTietSanPham.anhSanPhams[0].url;
      }
      return 'default-image.jpg';
    },
    async loadProvinces() {
      try {
        this.provinces = await AddressService.getProvinces();
      } catch (error) {
        console.error("Lỗi khi tải danh sách tỉnh/thành phố:", error);
        alert("Không thể tải danh sách tỉnh/thành phố: " + (error.response?.data?.message || "Vui lòng thử lại!"));
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
        console.error("Lỗi khi tải dữ liệu danh sách:", error);
        alert("Có lỗi xảy ra khi tải dữ liệu danh sách: " + (error.response?.data?.message || "Vui lòng thử lại!"));
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
          alert("Không thể tải danh sách quận/huyện: " + (error.response?.data?.message || "Vui lòng thử lại!"));
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
          alert("Không thể tải danh sách xã/phường: " + (error.response?.data?.message || "Vui lòng thử lại!"));
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
          alert("Không tìm thấy ID hóa đơn!");
          return;
        }
        const fullAddress = `${this.customerFormData.diaChiChiTiet || ''}, ${this.customerFormData.xaPhuong || ''}, ${this.customerFormData.quanHuyen || ''}, ${this.customerFormData.tinhThanh || ''}`.replace(/^,\s*|,\s*$/g, '');
        const updatedData = {
          tenNguoiNhan: this.customerFormData.tenNguoiNhan,
          soDienThoaiNguoiNhan: this.customerFormData.soDienThoaiNguoiNhan,
          diaChi: fullAddress,
        };
        const response = await HoaDonService.updateCustomerInfo(orderId, updatedData);
        if (response.status === 200) {
          alert("Cập nhật thông tin khách hàng thành công!");
          this.showCustomerForm = false;
          await this.fetchOrder();
        } else {
          alert("Có lỗi xảy ra khi cập nhật thông tin khách hàng!");
        }
      } catch (error) {
        console.error("Lỗi khi cập nhật thông tin khách hàng:", error);
        alert("Có lỗi xảy ra khi cập nhật thông tin khách hàng: " + (error.response?.data?.message || "Vui lòng thử lại!"));
      }
    },
    formatDiaChi(order) {
      if (!order.diaChi) return "Chưa cập nhật";
      if (typeof order.diaChi === 'string') {
        const cleanAddress = order.diaChi.replace(/^,\s*|,\s*$/g, '').trim();
        return cleanAddress || "Chưa cập nhật";
      }
      const { diaChiChiTiet, xaPhuong, quanHuyen, tinhThanh } = order.diaChi;
      const fullAddress = `${diaChiChiTiet || ''}, ${xaPhuong || ''}, ${quanHuyen || ''}, ${tinhThanh || ''}`.replace(/^,\s*|,\s*$/g, '').trim();
      return fullAddress || "Chưa cập nhật";
    },
    getStatusClass(status) {
      return {
        "Tạo hóa đơn": "text-gray-500 bg-gray-200 px-2 py-1 rounded",
        "Chờ xác nhận": "text-yellow-500 bg-yellow-200 px-2 py-1 rounded",
        "Chờ giao hàng": "text-yellow-500 bg-yellow-200 px-2 py-1 rounded",
        "Đang vận chuyển": "text-blue-500 bg-blue-200 px-2 py-1 rounded",
        "Đã giao hàng": "text-green-500 bg-green-200 px-2 py-1 rounded",
        "Thanh toán thành công": "text-green-500 bg-green-200 px-2 py-1 rounded",
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
        case "VNPAY":
          return "bg-purple-200 text-purple-800 px-2 py-1 rounded";
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
          await this.fetchOrder();
        } else {
          alert("Có lỗi xảy ra khi cập nhật số lượng!");
        }
      } catch (error) {
        console.error("Lỗi khi cập nhật số lượng:", error);
        alert("Có lỗi xảy ra khi cập nhật số lượng: " + (error.response?.data?.message || "Vui lòng thử lại!"));
      }
    },
    openHistoryModal() {
      this.showHistoryModal = true;
    },
    closeHistoryModal() {
      this.showHistoryModal = false;
    },
    closeAddProductModal() {
      this.showAddProductModal = false;
      this.productSearchQuery = '';
      this.searchResults = [];
      this.allProducts = [];
      this.allBookSets = [];
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
    },
    async searchProducts() {
      try {
        this.filters.searchQuery = this.productSearchQuery;
        // Đồng bộ với POS: dùng search theo chuỗi, sau đó nạp ảnh
        const results = await ChiTietSanPhamService.search(this.productSearchQuery);
        this.searchResults = results || [];
        for (let product of this.searchResults) {
          if (product.id) {
            product.anhSanPhams = await AnhSanPhamService.getImagesByChiTietSanPhamId(product.id) || [];
            this.currentImageIndex[product.id] = 0;
          } else {
            product.anhSanPhams = [];
          }
        }
      } catch (error) {
        console.error("Lỗi khi tìm kiếm sản phẩm:", error);
        alert("Có lỗi xảy ra khi tìm kiếm sản phẩm: " + (error.response?.data?.message || "Vui lòng thử lại!"));
      }
    },
    async showAllProducts() {
      try {
        this.isShowingBookSets = false;
        const products = await ChiTietSanPhamService.getAll();
        this.allProducts = products || [];
        for (let product of this.allProducts) {
          if (product.id) {
            product.anhSanPhams = await AnhSanPhamService.getImagesByChiTietSanPhamId(product.id) || [];
            this.currentImageIndex[product.id] = 0;
          } else {
            product.anhSanPhams = [];
          }
        }
      } catch (error) {
        console.error("Lỗi khi tải danh sách sản phẩm:", error);
        alert("Có lỗi xảy ra khi tải danh sách sản phẩm: " + (error.response?.data?.message || "Vui lòng thử lại!"));
      }
    },
    selectProduct(product, type = 'product') {
      this.selectedProduct = product;
      this.selectedItemType = type;
      this.selectedProductQuantity = 1;
    },
    async addProduct() {
      try {
        if (!this.selectedProduct || this.selectedProductQuantity <= 0) {
          alert("Số lượng không hợp lệ hoặc chưa chọn sản phẩm!");
          return;
        }
        const orderId = this.$route.params.id;
        let response;
        if (this.selectedItemType === 'bookset') {
          const productData = {
            boSachId: this.selectedProduct.id,
            soLuong: this.selectedProductQuantity,
            giaSanPham: this.selectedProduct.giaTien,
          };
          response = await HoaDonService.addBoSachToOrder(orderId, productData);
        } else {
          if (this.selectedProductQuantity > (this.selectedProduct.soLuongTon || 0)) {
            alert("Số lượng vượt quá tồn kho!");
            return;
          }
          const productData = {
            chiTietSanPhamId: this.selectedProduct.id,
            soLuong: this.selectedProductQuantity,
            giaSanPham: this.selectedProduct.gia,
          };
          response = await HoaDonService.addProductToOrder(orderId, productData);
        }
        if (response.status === 200) {
          alert("Thêm sản phẩm thành công!");
          this.closeAddProductModal();
          await this.fetchOrder();
        } else {
          alert("Có lỗi xảy ra khi thêm sản phẩm!");
        }
      } catch (error) {
        console.error("Lỗi khi thêm sản phẩm:", error);
        alert("Có lỗi xảy ra khi thêm sản phẩm: " + (error.response?.data?.message || "Vui lòng thử lại!"));
      }
    },
    async removeItem(itemId) {
      try {
        const orderId = this.$route.params.id;
        const response = await HoaDonService.removeProductFromOrder(orderId, itemId);
        if (response.status === 200) {
          alert("Xóa sản phẩm thành công!");
          await this.fetchOrder();
        } else {
          alert("Có lỗi xảy ra khi xóa sản phẩm!");
        }
      } catch (error) {
        console.error("Lỗi khi xóa sản phẩm:", error);
        alert("Có lỗi xảy ra khi xóa sản phẩm: " + (error.response?.data?.message || "Vui lòng thử lại!"));
      }
    },
    openBookSetDetailModal(bookSet) {
      this.selectedBookSet = bookSet;
      this.fetchBookSetDetails(bookSet.id);
      this.showBookSetDetailModal = true;
    },
    async fetchBookSetDetails(bookSetId) {
      try {
        const response = await ChiTietSanPhamService.getBookSetDetails(bookSetId);
        this.$set(this.bookSetDetails, bookSetId, response.data || []);
      } catch (error) {
        console.error("Lỗi khi tải chi tiết bộ sách:", error);
        alert("Có lỗi xảy ra khi tải chi tiết bộ sách: " + (error.response?.data?.message || "Vui lòng thử lại!"));
      }
    },
    closeBookSetDetailModal() {
      this.showBookSetDetailModal = false;
      this.selectedBookSet = null;
    },
    openPaymentModal() {
      this.paymentNote = '';
      this.showPaymentModal = true;
    },
    async showAllBookSets() {
      try {
        this.isShowingBookSets = true;
        const BoSachService = (await import('@/service/BoSachService')).default;
        const res = await BoSachService.getAll();
        this.allBookSets = res || [];
      } catch (error) {
        console.error('Lỗi khi tải danh sách bộ sách:', error);
        alert('Có lỗi xảy ra khi tải danh sách bộ sách!');
      }
    },
    setupImageIntervals() {
      const products = this.productSearchQuery ? this.searchResults : this.allProducts;
      products.forEach(product => {
        if (product.anhSanPhams && product.anhSanPhams.length > 1) {
          this.imageIntervals[product.id] = setInterval(() => {
            this.currentImageIndex[product.id] = (this.currentImageIndex[product.id] + 1) % product.anhSanPhams.length;
          }, 3000);
        }
      });
    },
    clearImageIntervals() {
      Object.values(this.imageIntervals).forEach(interval => clearInterval(interval));
      this.imageIntervals = {};
    }
  },
  mounted() {
    this.fetchOrder();
    this.loadProvinces();
    document.addEventListener('visibilitychange', () => {
      if (document.visibilityState === 'visible') {
        this.setupImageIntervals();
      } else {
        this.clearImageIntervals();
      }
    });
  },
  beforeUnmount() {
    this.clearImageIntervals();
    document.removeEventListener('visibilitychange', () => {});
  }
};
</script>

<style scoped>
/* General container */
.p-6 {
  padding: 1.5rem;
}
.bg-gray-100 {
  background-color: #f3f4f6;
}
.min-h-screen {
  min-height: 100vh;
}
.bg-white {
  background-color: #ffffff;
}
.rounded-lg {
  border-radius: 0.5rem;
}
.shadow-md {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}
.p-4 {
  padding: 1rem;
}

/* Timeline */
.progress-line {
  width: 100px;
  height: 2px;
  background: #d1d5db;
}
.icon-wrapper {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 20px;
}

/* Image container */
.image-container {
  position: relative;
  width: 100%;
  height: 100%;
}
.image-active {
  opacity: 1;
}
.image-inactive {
  opacity: 0;
}

/* Fade transition */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>