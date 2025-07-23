<template>
  <div class="w-full p-4">
    <!-- Thanh tab hóa đơn -->
    <div class="w-full mb-6">
      <div class="bg-white border border-gray-200 rounded-lg shadow-md p-4">
        <h5 class="text-lg font-bold text-gray-900 mb-4">Quản lý bán hàng</h5>
        <div class="flex flex-wrap border-b">
          <button v-for="(order, index) in orders" :key="order.id"
                  :class="['px-4 py-2 text-sm font-medium mr-2 mb-2 rounded-t-lg', { 'bg-blue-100 border border-blue-500 text-blue-700': selectedOrderIndex === index, 'bg-gray-200 text-gray-600 hover:bg-gray-300': selectedOrderIndex !== index }]"
                  @click="selectedOrderIndex = index">
            Hóa đơn #{{ order.maHoaDon || index + 1 }}
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
              <th class="px-4 py-2">Tên sản phẩm</th>
              <th class="px-4 py-2">Chất liệu</th>
              <th class="px-4 py-2">Loại bìa</th>
              <th class="px-4 py-2">Ngôn ngữ</th>
              <th class="px-4 py-2">Tác giả</th>
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
              <td class="px-4 py-2">{{ prod.chiTietSanPham?.tenChiTietSanPham || 'Không xác định' }}</td>
              <td class="px-4 py-2">{{ prod.chiTietSanPham?.idChatLieu?.tenChatLieu || 'N/A' }}</td>
              <td class="px-4 py-2">{{ prod.chiTietSanPham?.idLoaiBia?.tenLoaiBia || 'N/A' }}</td>
              <td class="px-4 py-2">{{ prod.chiTietSanPham?.idNgonNgu?.tenNgonNgu || 'N/A' }}</td>
              <td class="px-4 py-2">{{ prod.chiTietSanPham?.idTacGia?.tenTacGia || 'N/A' }}</td>
              <td class="px-4 py-2">
                <input type="number" v-model.number="prod.soLuong" class="w-16 border rounded px-2 py-1 text-sm" :disabled="selectedOrder.trangThai !== 'Tạo hóa đơn'" @input="updateProductQuantity(prod)" />
              </td>
              <td class="px-4 py-2">{{ prod.chiTietSanPham?.soLuongTon || 0 }}</td>
              <td class="px-4 py-2">{{ formatCurrency(prod.giaSanPham) }}</td>
              <td class="px-4 py-2">{{ formatCurrency(prod.giaSanPham) }}</td>
              <td class="px-4 py-2">{{ formatCurrency(prod.thanhTien || prod.soLuong * prod.giaSanPham) }}</td>
              <td class="px-4 py-2">
                <button v-if="selectedOrder.trangThai === 'Tạo hóa đơn'" @click="removeItem(prod.id)" class="text-red-500 hover:text-red-700">
                  <i class="fas fa-trash-alt"></i>
                </button>
              </td>
            </tr>
            <tr v-if="paginatedProducts.length === 0">
              <td colspan="11" class="px-4 py-2 text-center text-gray-500">
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
            <input type="text" v-model="selectedOrder.diaChiGiaoHang" class="w-full border rounded px-2 py-1 text-sm" />
            <label class="block text-sm mt-2 mb-1">SĐT người nhận</label>
            <input type="text" v-model="selectedOrder.soDienThoaiNguoiNhan" class="w-full border rounded px-2 py-1 text-sm" />
          </div>
        </div>

        <div class="mb-6" v-if="deliveryMethod === 'TaiQuay'">
          <h3 class="text-md font-semibold text-gray-900 mb-2">Hình thức thanh toán</h3>
          <div class="space-y-2">
            <label class="flex items-center space-x-2">
              <input type="radio" value="chuyenKhoan" v-model="paymentMethod" class="text-sm" />
              <span class="text-sm">Chuyển khoản</span>
            </label>
            <label class="flex items-center space-x-2">
              <input type="radio" value="tienMat" v-model="paymentMethod" class="text-sm" />
              <span class="text-sm">Tiền mặt</span>
            </label>
          </div>
          <div class="mt-2" :class="{ 'hidden': paymentMethod !== 'chuyenKhoan' }">
            <img src="https://via.placeholder.com/150" alt="QR thanh toán" class="mx-auto" />
            <p class="text-center text-sm text-gray-500 mt-1">Quét mã để thanh toán</p>
          </div>
          <div v-if="deliveryMethod === 'TaiQuay' && paymentMethod === 'tienMat'" class="mt-4">
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
            <input type="text" placeholder="Nhập voucher..." v-model="voucherCode" class="w-full border border-gray-300 rounded px-3 py-1 text-sm focus:outline-none focus:ring" />
          </div>
        </div>

        <div class="mt-6">
          <button v-if="selectedOrder.trangThai === 'Tạo hóa đơn'" @click="xacNhanHoaDon" class="w-full bg-purple-600 text-white py-2 rounded-lg text-sm font-semibold hover:bg-purple-700 transition" :disabled="isConfirmDisabled">
            Xác nhận đơn hàng
          </button>
        </div>
      </div>
    </div>

    <!-- Modal thêm sản phẩm -->
    <transition name="fade">
      <div v-if="showAddProductModal" class="fixed inset-0 z-10 flex items-center justify-center bg-gray-600 bg-opacity-50">
        <div class="bg-white p-6 z-10 rounded-lg shadow-lg w-3/4 max-h-[80vh] overflow-y-auto">
          <h3 class="text-lg font-semibold mb-4">Thêm sản phẩm</h3>
          <form @submit.prevent="addProduct" class="space-y-4">
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
import { reactive } from "vue";

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
      productSearchQuery: '',
      customerSearchQuery: '',
      searchResults: [],
      allProducts: [],
      customers: [],
      selectedProduct: null,
      selectedProductQuantity: 1,
      newCustomer: {
        hoTen: '',
        email: '',
        soDienThoai: '',
        ngaySinh: '',
        gioiTinh: true,
        trangThai: true
      },
      deliveryMethod: 'TaiQuay',
      paymentMethod: 'tienMat',
      tienKhachDua: 0,
      voucherCode: '',
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
      return this.paginatedProducts.reduce((total, item) => {
        return total + (item.thanhTien || item.soLuong * item.giaSanPham);
      }, 0);
    },
    tienGiamGia() {
      const phanTramGiam = this.selectedOrder.phieuGiamGia?.soPhanTramGiam || 0;
      return (this.tongTienHang * phanTramGiam) / 100;
    },
    thanhTien() {
      const phiShip = this.selectedOrder.phiShip || 0;
      return this.tongTienHang - this.tienGiamGia + phiShip;
    },
    tienTraKhach() {
      return this.paymentMethod === 'tienMat' && this.deliveryMethod === 'TaiQuay' ? this.tienKhachDua - this.thanhTien : 0;
    },
    isConfirmDisabled() {
      if (this.selectedOrder.trangThai !== 'Tạo hóa đơn') return true;
      if (this.paginatedProducts.length === 0) return true;
      if (this.paymentMethod === 'tienMat' && this.deliveryMethod === 'TaiQuay') {
        return this.tienTraKhach < 0 || this.tienKhachDua <= 0;
      }
      return false;
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
      this.tienKhachDua = 0; // Reset tiền khách đưa khi chuyển hóa đơn
    },
    deliveryMethod() {
      this.tienKhachDua = 0; // Reset tiền khách đưa khi thay đổi hình thức nhận hàng
    },
    paymentMethod() {
      this.tienKhachDua = 0; // Reset tiền khách đưa khi thay đổi hình thức thanh toán
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
    // Hàm ánh xạ giá trị deliveryMethod
    mapDeliveryMethod(value) {
      return value === 'TaiQuay' ? 'Tại quầy' : 'Giao hàng';
    },

    formatCurrency(value) {
      return new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(value || 0);
    },
    formatDiaChi(customer) {
      if (!customer.danhSachDiaChi || customer.danhSachDiaChi.length === 0) return '';
      const diaChi = customer.danhSachDiaChi.find(dc => dc.macDinh) || customer.danhSachDiaChi[0];
      return `${diaChi.diaChiChiTiet}, ${diaChi.xaPhuong}, ${diaChi.quanHuyen}, ${diaChi.tinhThanh}`;
    },
    async fetchOrders() {
      try {
        const response = await axios.get('http://localhost:8080/api/hoa-don?trangThai=Tạo hóa đơn');
        this.orders = response.data.map(order => ({
          ...order,
          hoaDonChiTiets: Array.isArray(order.hoaDonChiTiets) ? order.hoaDonChiTiets : [],
          phieuGiamGia: order.phieuGiamGia || {},
          diaChiGiaoHang: order.diaChi || '',
          soDienThoaiNguoiNhan: order.soDienThoaiNguoiNhan || '',
          loaiHoaDon: order.loaiHoaDon === 'Tại quầy' ? 'TaiQuay' : 'GiaoHang', // Ánh xạ ngược
        }));
        if (this.orders.length > 0) {
          this.selectedOrderIndex = 0;
          await this.fetchProductsForOrder();
        }
      } catch (error) {
        console.error('Lỗi khi tải danh sách hóa đơn:', error);
      }
    },
    async fetchProductsForOrder() {
      if (!this.selectedOrder || !this.selectedOrder.id) return;
      try {
        const response = await axios.get(`http://localhost:8080/api/hoa-don/${this.selectedOrder.id}`);
        this.paginatedProducts = response.data.hoaDonChiTiets || [];
      } catch (error) {
        console.error('Lỗi khi tải sản phẩm của hóa đơn:', error);
      }
    },
    async createNewInvoice() {
      try {
        const response = await axios.post('http://localhost:8080/api/hoa-don', {
          loaiHoaDon: this.mapDeliveryMethod(this.deliveryMethod), // Ánh xạ trước khi gửi
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
        this.tienKhachDua = 0;
      } catch (error) {
        console.error('Lỗi khi tạo hóa đơn mới:', error);
        alert('Không thể tạo hóa đơn mới. Vui lòng thử lại!');
      }
    },
    async xacNhanHoaDon() {
      if (!confirm("Bạn có chắc chắn muốn xác nhận hóa đơn?")) return;
      if (this.paginatedProducts.length === 0) {
        alert('Hóa đơn chưa có sản phẩm. Vui lòng thêm sản phẩm trước khi xác nhận!');
        return;
      }
      try {
        const orderId = this.selectedOrder.id;
        const paymentData = {
          tienMat: this.paymentMethod === 'tienMat' ? this.thanhTien : 0,
          chuyenKhoan: this.paymentMethod === 'chuyenKhoan' ? this.thanhTien : 0,
          phuongThucThanhToanId: this.paymentMethod === 'tienMat' ? 1 : 2,
          ghiChu: '',
          loaiHoaDon: this.mapDeliveryMethod(this.deliveryMethod), // Ánh xạ trước khi gửi
          tienKhachDua: this.paymentMethod === 'tienMat' && this.deliveryMethod === 'TaiQuay' ? this.tienKhachDua : 0
        };
        await HoaDonService.updatePayment(orderId, paymentData);
        alert('Xác nhận hóa đơn thành công!');
        await this.fetchOrders();
        this.paginatedProducts = [];
        this.tienKhachDua = 0;
      } catch (error) {
        console.error('Lỗi khi xác nhận hóa đơn:', error);
        alert(error.response?.data?.message || 'Có lỗi xảy ra khi xác nhận hóa đơn. Vui lòng thử lại!');
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
        alert('Không thể tìm kiếm khách hàng. Vui lòng thử lại!');
      }
    },
    async selectCustomer(customer) {
      try {
        const orderId = this.selectedOrder.id;
        const customerData = {
          idKhachHang: customer.id,
          tenNguoiNhan: customer.hoTen,
          soDienThoaiNguoiNhan: customer.soDienThoai,
          diaChiGiaoHang: this.formatDiaChi(customer)
        };
        await HoaDonService.updateCustomerInfo(orderId, customerData);
        this.orders[this.selectedOrderIndex] = {
          ...this.selectedOrder,
          ...customerData
        };
        this.closeSelectCustomerModal();
        alert('Chọn khách hàng thành công!');
      } catch (error) {
        console.error('Lỗi khi chọn khách hàng:', error);
        alert('Có lỗi xảy ra khi chọn khách hàng. Vui lòng thử lại!');
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
          diaChiGiaoHang: ''
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
      if (!confirm('Bạn có chắc chắn muốn thêm sản phẩm này vào hóa đơn?')) return;
      try {
        const orderId = this.selectedOrder.id;
        const productData = {
          chiTietSanPhamId: this.selectedProduct.id,
          soLuong: this.selectedProductQuantity,
          giaSanPham: this.selectedProduct.gia,
        };
        const response = await axios.post(`http://localhost:8080/api/hoa-don/${orderId}/add-product`, productData);
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
    async updateProductQuantity(item) {
      if (this.selectedOrder.trangThai !== 'Tạo hóa đơn') return;
      if (!/^\d+$/.test(item.soLuong) || Number(item.soLuong) < 1) {
        item.soLuong = 1;
      }
      try {
        const orderId = this.selectedOrder.id;
        const updatedData = {
          soLuong: item.soLuong,
          thanhTien: item.soLuong * item.giaSanPham,
        };
        const response = await axios.put(`http://localhost:8080/api/hoa-don/${orderId}/update-product/${item.id}`, updatedData);
        if (response.status === 200) {
          await this.fetchProductsForOrder();
        } else {
          alert('Có lỗi xảy ra khi cập nhật số lượng!');
        }
      } catch (error) {
        console.error('Lỗi khi cập nhật số lượng:', error);
        alert('Có lỗi xảy ra khi cập nhật số lượng. Vui lòng thử lại!');
      }
    },
    async removeItem(itemId) {
      if (!confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')) return;
      try {
        const orderId = this.selectedOrder.id;
        await axios.delete(`http://localhost:8080/api/hoa-don/${orderId}/remove-product/${itemId}`);
        this.paginatedProducts = this.paginatedProducts.filter(item => item.id !== itemId);
        alert('Xóa sản phẩm thành công!');
      } catch (error) {
        console.error('Lỗi khi xóa sản phẩm:', error);
        alert('Có lỗi xảy ra khi xóa sản phẩm!');
      }
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
</style>