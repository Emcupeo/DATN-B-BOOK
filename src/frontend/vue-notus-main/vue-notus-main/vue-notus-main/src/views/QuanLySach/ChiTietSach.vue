<template>
  <div class="relative flex flex-col min-w-0 break-words w-full mb-6 shadow-lg rounded bg-white">
    <!-- Header với nút back -->
    <div class="rounded-t mb-0 px-4 py-3 border-0">
      <div class="flex flex-wrap items-center justify-between">
        <div class="relative w-full px-4 max-w-full flex-grow flex-1">
          <button
              class="bg-indigo-500 text-white active:bg-indigo-600 text-xs font-bold uppercase px-3 py-1 rounded outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
              type="button"
              @click="goBack"
          >
            <i class="fas fa-arrow-left mr-2"></i>
            Quay lại
          </button>
          <h3 class="font-semibold text-lg text-blueGray-700 mt-3">
            Chi tiết sách
          </h3>
        </div>
        <div class="relative w-full px-4 max-w-full flex-grow flex-1 text-right">
          <button
              class="bg-green-500 text-white active:bg-green-600 text-xs font-bold uppercase px-3 py-1 rounded outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
              type="button"
              @click="openAddModal"
          >
            <i class="fas fa-plus mr-2"></i>
            Tạo chi tiết sách
          </button>
        </div>
      </div>
    </div>

    <!-- Loading state -->
    <div v-if="loading" class="p-4 text-center">
      <i class="fas fa-spinner fa-spin mr-2"></i>
      Đang tải...
    </div>

    <!-- Error state - Not Found -->
    <div v-else-if="error && error.includes('không tìm thấy')" class="p-8 text-center">
      <div class="mb-4">
        <i class="fas fa-exclamation-circle text-4xl text-yellow-500 mb-4"></i>
        <p class="text-lg text-gray-700 mb-6">
          Chưa có chi tiết sản phẩm cho sách này
        </p>
        <button
            @click="openAddModal"
            class="bg-green-500 text-white px-6 py-3 rounded-lg hover:bg-green-600 transition-colors duration-200"
        >
          <i class="fas fa-plus mr-2"></i>
          Thêm chi tiết sản phẩm
        </button>
      </div>
    </div>

    <!-- Other Error state -->
    <div v-else-if="error" class="p-4 text-red-500">
      <i class="fas fa-exclamation-circle mr-2"></i>
      {{ error }}
    </div>

    <!-- Content -->
    <div v-else class="block w-full overflow-x-auto p-4">
      <table class="items-center w-full bg-transparent border-collapse">
        <thead>
        <tr>
          <th class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap bg-blueGray-50 text-blueGray-500 border-blueGray-100">Mã chi tiết sản phẩm</th>
          <th class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap bg-blueGray-50 text-blueGray-500 border-blueGray-100">Tên sách</th>
          <th class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap bg-blueGray-50 text-blueGray-500 border-blueGray-100">Giá</th>
          <th class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap bg-blueGray-50 text-blueGray-500 border-blueGray-100">Số lượng tồn</th>
          <th class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap bg-blueGray-50 text-blueGray-500 border-blueGray-100">Trọng lượng (gram)</th>
          <th class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap bg-blueGray-50 text-blueGray-500 border-blueGray-100">Kích thước (cm)</th>
          <th class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap bg-blueGray-50 text-blueGray-500 border-blueGray-100">Trạng thái</th>
          <th class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap bg-blueGray-50 text-blueGray-500 border-blueGray-100">Loại bìa</th>
          <th class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap bg-blueGray-50 text-blueGray-500 border-blueGray-100">Tác giả</th>
          <th class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap bg-blueGray-50 text-blueGray-500 border-blueGray-100">Nhà xuất bản</th>
          <th class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap bg-blueGray-50 text-blueGray-500 border-blueGray-100">Chất liệu</th>
          <th class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap bg-blueGray-50 text-blueGray-500 border-blueGray-100">Người dịch</th>
          <th class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap bg-blueGray-50 text-blueGray-500 border-blueGray-100">Thể loại</th>
          <th class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap bg-blueGray-50 text-blueGray-500 border-blueGray-100">Ngôn ngữ</th>
          <th class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap bg-blueGray-50 text-blueGray-500 border-blueGray-100">Hành động</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="chiTiet in chiTietSanPham" :key="chiTiet.id">
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">{{ chiTiet.maChiTietSanPham }}</td>
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">{{ chiTiet.tenChiTietSanPham }}</td>
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">{{ formatCurrency(chiTiet.gia) }}</td>
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">{{ chiTiet.soLuongTon }}</td>
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">{{ chiTiet.trongLuong }}</td>
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">{{ chiTiet.kichThuoc }}</td>
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
            <span
                :class="[
                  'px-2 inline-flex text-xs leading-5 font-semibold rounded-full',
                  chiTiet.trangThai ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800',
                ]"
            >
              {{ chiTiet.trangThai ? "Hoạt động" : "Không hoạt động" }}
            </span>
          </td>
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">{{ chiTiet.idLoaiBia?.tenLoaiBia || 'N/A' }}</td>
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">{{ chiTiet.idTacGia?.tenTacGia || 'N/A' }}</td>
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">{{ chiTiet.idNhaXuatBan?.tenNhaXuatBan || 'N/A' }}</td>
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">{{ chiTiet.idChatLieu?.tenChatLieu || 'N/A' }}</td>
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">{{ chiTiet.idNguoiDich?.tenNguoiDich || 'N/A' }}</td>
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">{{ chiTiet.idTheLoai?.tenTheLoai || 'N/A' }}</td>
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">{{ chiTiet.idNgonNgu?.tenNgonNgu || 'N/A' }}</td>
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
            <button
                @click="openEditModal(chiTiet)"
                class="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600 transition mr-2"
            >
              <i class="fas fa-edit mr-1"></i>
              Sửa
            </button>
            <button
                @click="deleteChiTietSach(chiTiet.id)"
                class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 transition"
            >
              <i class="fas fa-trash mr-1"></i>
              Xóa
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal chỉnh sửa -->
    <div v-if="showEditModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white p-6 rounded-lg w-3/4 shadow-lg">
        <h2 class="text-xl font-bold mb-4">Sửa chi tiết sách</h2>
        <form @submit.prevent="handleUpdate" class="space-y-6">
          <div class="grid grid-cols-3 gap-6">
            <!-- Cột 1 -->
            <div class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700">Mã chi tiết sản phẩm</label>
                <input
                    v-model="editForm.maChiTietSanPham"
                    type="text"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 bg-gray-100"
                    disabled
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Tên chi tiết sản phẩm</label>
                <input
                    v-model="editForm.tenChiTietSanPham"
                    type="text"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                    required
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Giá</label>
                <input
                    v-model.number="editForm.gia"
                    type="number"
                    min="0"
                    step="0.01"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                    required
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Số lượng tồn</label>
                <input
                    v-model.number="editForm.soLuongTon"
                    type="number"
                    min="0"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                    required
                />
              </div>
            </div>

            <!-- Cột 2 -->
            <div class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700">Trọng lượng (gram)</label>
                <input
                    v-model.number="editForm.trongLuong"
                    type="number"
                    min="0"
                    step="0.01"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                    required
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Kích thước (cm)</label>
                <input
                    v-model.number="editForm.kichThuoc"
                    type="number"
                    min="0"
                    step="0.01"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                    required
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Trạng thái</label>
                <select
                    v-model="editForm.trangThai"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                >
                  <option :value="true">Hoạt động</option>
                  <option :value="false">Không hoạt động</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Loại bìa</label>
                <select
                    v-model="editForm.idLoaiBia"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                >
                  <option v-for="item in loaiBiaList" :key="item.id" :value="item.id">{{ item.tenLoaiBia }}</option>
                </select>
              </div>
            </div>

            <!-- Cột 3 -->
            <div class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700">Tác giả</label>
                <select
                    v-model="editForm.idTacGia"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                >
                  <option v-for="item in tacGiaList" :key="item.id" :value="item.id">{{ item.tenTacGia }}</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Nhà xuất bản</label>
                <select
                    v-model="editForm.idNhaXuatBan"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                >
                  <option v-for="item in nhaXuatBanList" :key="item.id" :value="item.id">{{ item.tenNhaXuatBan }}</option>
                </select>
              </div>
              <div>
                <label class="block text- sm font-medium text-gray-700">Chất liệu</label>
                <select
                    v-model="editForm.idChatLieu"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                >
                  <option v-for="item in chatLieuList" :key="item.id" :value="item.id">{{ item.tenChatLieu }}</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Người dịch</label>
                <select
                    v-model="editForm.idNguoiDich"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                >
                  <option v-for="item in nguoiDichList" :key="item.id" :value="item.id">{{ item.tenNguoiDich }}</option>
                </select>
              </div>
            </div>
          </div>

          <!-- Mô tả và ghi chú (toàn chiều rộng) -->
          <div class="grid grid-cols-2 gap-6">
            <div>
              <label class="block text-sm font-medium text-gray-700">Thể loại</label>
              <select
                  v-model="editForm.idTheLoai"
                  class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              >
                <option v-for="item in theLoaiList" :key="item.id" :value="item.id">{{ item.tenTheLoai }}</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Ngôn ngữ</label>
              <select
                  v-model="editForm.idNgonNgu"
                  class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              >
                <option v-for="item in ngonNguList" :key="item.id" :value="item.id">{{ item.tenNgonNgu }}</option>
              </select>
            </div>
          </div>

          <div class="grid grid-cols-2 gap-6">
            <div>
              <label class="block text-sm font-medium text-gray-700">Mô tả</label>
              <textarea
                  v-model="editForm.moTa"
                  rows="3"
                  class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              ></textarea>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Ghi chú</label>
              <textarea
                  v-model="editForm.ghiChu"
                  rows="3"
                  class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              ></textarea>
            </div>
          </div>

          <!-- Buttons -->
          <div class="flex justify-end space-x-3">
            <button
                type="button"
                @click="closeEditModal"
                class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600 transition"
            >
              Hủy
            </button>
            <button
                type="submit"
                class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition"
            >
              Cập nhật
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Modal thêm mới -->
    <div v-if="showAddModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white p-6 rounded-lg w-3/4 shadow-lg">
        <h2 class="text-xl font-bold mb-4">Thêm chi tiết sách mới</h2>
        <form @submit.prevent="handleAdd" class="space-y-6">
          <div class="grid grid-cols-3 gap-6">
            <!-- Cột 1 -->
            <div class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700">Mã chi tiết sản phẩm</label>
                <input
                    v-model="addForm.maChiTietSanPham"
                    type="text"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 bg-gray-100"
                    disabled
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Tên chi tiết sản phẩm</label>
                <input
                    v-model="addForm.tenChiTietSanPham"
                    type="text"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                    required
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Giá</label>
                <input
                    v-model.number="addForm.gia"
                    type="number"
                    min="0"
                    step="0.01"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                    required
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Số lượng tồn</label>
                <input
                    v-model.number="addForm.soLuongTon"
                    type="number"
                    min="0"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                    required
                />
              </div>
            </div>

            <!-- Cột 2 -->
            <div class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700">Trọng lượng (gram)</label>
                <input
                    v-model.number="addForm.trongLuong"
                    type="number"
                    min="0"
                    step="0.01"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                    required
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Kích thước (cm)</label>
                <input
                    v-model.number="addForm.kichThuoc"
                    type="number"
                    min="0"
                    step="0.01"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                    required
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Trạng thái</label>
                <select
                    v-model="addForm.trangThai"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                >
                  <option :value="true">Hoạt động</option>
                  <option :value="false">Không hoạt động</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Loại bìa</label>
                <select
                    v-model="addForm.idLoaiBia"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                >
                  <option v-for="item in loaiBiaList" :key="item.id" :value="item.id">{{ item.tenLoaiBia }}</option>
                </select>
              </div>
            </div>

            <!-- Cột 3 -->
            <div class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700">Tác giả</label>
                <select
                    v-model="addForm.idTacGia"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                >
                  <option v-for="item in tacGiaList" :key="item.id" :value="item.id">{{ item.tenTacGia }}</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Nhà xuất bản</label>
                <select
                    v-model="addForm.idNhaXuatBan"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                >
                  <option v-for="item in nhaXuatBanList" :key="item.id" :value="item.id">{{ item.tenNhaXuatBan }}</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Chất liệu</label>
                <select
                    v-model="addForm.idChatLieu"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                >
                  <option v-for="item in chatLieuList" :key="item.id" :value="item.id">{{ item.tenChatLieu }}</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700">Người dịch</label>
                <select
                    v-model="addForm.idNguoiDich"
                    class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                >
                  <option v-for="item in nguoiDichList" :key="item.id" :value="item.id">{{ item.tenNguoiDich }}</option>
                </select>
              </div>
            </div>
          </div>

          <!-- Mô tả và ghi chú (toàn chiều rộng) -->
          <div class="grid grid-cols-2 gap-6">
            <div>
              <label class="block text-sm font-medium text-gray-700">Thể loại</label>
              <select
                  v-model="addForm.idTheLoai"
                  class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              >
                <option v-for="item in theLoaiList" :key="item.id" :value="item.id">{{ item.tenTheLoai }}</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Ngôn ngữ</label>
              <select
                  v-model="addForm.idNgonNgu"
                  class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              >
                <option v-for="item in ngonNguList" :key="item.id" :value="item.id">{{ item.tenNgonNgu }}</option>
              </select>
            </div>
          </div>

          <div class="grid grid-cols-2 gap-6">
            <div>
              <label class="block text-sm font-medium text-gray-700">Mô tả</label>
              <textarea
                  v-model="addForm.moTa"
                  rows="3"
                  class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              ></textarea>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700">Ghi chú</label>
              <textarea
                  v-model="addForm.ghiChu"
                  rows="3"
                  class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              ></textarea>
            </div>
          </div>

          <!-- Buttons -->
          <div class="flex justify-end space-x-3">
            <button
                type="button"
                @click="closeAddModal"
                class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600 transition"
            >
              Hủy
            </button>
            <button
                type="submit"
                class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition"
            >
              Thêm mới
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import ChiTietSanPhamService from "@/service/ChiTietSanPhamService";
import LoaiBiaService from "@/service/LoaiBiaService";
import TacGiaService from "@/service/TacGiaService";
import NhaXuatBanService from "@/service/NhaXuatBanService";
import ChatLieuService from "@/service/ChatLieuService";
import NguoiDichService from "@/service/NguoiDichService";
import TheLoaiService from "@/service/TheLoaiService";
import NgonNguService from "@/service/NgonNguService";

export default {
  name: "ChiTietSach",

  data() {
    return {
      chiTietSanPham: [],
      loading: true,
      error: null,
      showEditModal: false,
      showAddModal: false,
      editForm: {
        id: null,
        maChiTietSanPham: '',
        tenChiTietSanPham: '',
        gia: 0,
        soLuongTon: 0,
        trongLuong: 0,
        kichThuoc: 0,
        trangThai: true,
        moTa: '',
        ghiChu: '',
        idSanPham: null,
        idLoaiBia: null,
        idTacGia: null,
        idNhaXuatBan: null,
        idChatLieu: null,
        idNguoiDich: null,
        idTheLoai: null,
        idNgonNgu: null,
      },
      addForm: {
        maChiTietSanPham: '',
        tenChiTietSanPham: '',
        gia: 0,
        soLuongTon: 0,
        trongLuong: 0,
        kichThuoc: 0,
        trangThai: true,
        moTa: '',
        ghiChu: '',
        idSanPham: null,
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
    };
  },

  async created() {
    await this.loadChiTietSanPham();
    await this.loadInitialData();
  },

  methods: {
    async loadChiTietSanPham() {
      try {
        this.loading = true;
        this.error = null;
        const idSanPham = this.$route.params.id;
        this.chiTietSanPham = await ChiTietSanPhamService.getBySanPhamId(idSanPham);
        if (!this.chiTietSanPham || this.chiTietSanPham.length === 0) {
          throw new Error("Không tìm thấy chi tiết sản phẩm cho sách này");
        }
      } catch (error) {
        console.error("Error loading chi tiet san pham:", error);
        this.error = error.response?.data?.message || "Không tìm thấy chi tiết sản phẩm cho sách này";
      } finally {
        this.loading = false;
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
        console.error("Error loading initial data:", error);
        this.error = "Có lỗi xảy ra khi tải dữ liệu danh sách";
      }
    },

    formatCurrency(value) {
      return new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND",
      }).format(value);
    },

    goBack() {
      this.$router.push("/admin/sach");
    },

    openAddModal() {
      this.addForm.idSanPham = this.$route.params.id; // Gán idSanPham từ route params
      this.showAddModal = true;
    },

    closeAddModal() {
      this.showAddModal = false;
      this.addForm = {
        maChiTietSanPham: '',
        tenChiTietSanPham: '',
        gia: 0,
        soLuongTon: 0,
        trongLuong: 0,
        kichThuoc: 0,
        trangThai: true,
        moTa: '',
        ghiChu: '',
        idSanPham: null,
        idLoaiBia: null,
        idTacGia: null,
        idNhaXuatBan: null,
        idChatLieu: null,
        idNguoiDich: null,
        idTheLoai: null,
        idNgonNgu: null,
      };
    },

    async handleAdd() {
      try {
        const newData = {
          maChiTietSanPham: this.addForm.maChiTietSanPham,
          tenChiTietSanPham: this.addForm.tenChiTietSanPham,
          gia: this.addForm.gia,
          soLuongTon: this.addForm.soLuongTon,
          trongLuong: this.addForm.trongLuong,
          kichThuoc: this.addForm.kichThuoc,
          trangThai: this.addForm.trangThai,
          moTa: this.addForm.moTa,
          ghiChu: this.addForm.ghiChu,
          idSanPham: { id: this.addForm.idSanPham },
          idLoaiBia: this.addForm.idLoaiBia ? { id: this.addForm.idLoaiBia } : null,
          idTacGia: this.addForm.idTacGia ? { id: this.addForm.idTacGia } : null,
          idNhaXuatBan: this.addForm.idNhaXuatBan ? { id: this.addForm.idNhaXuatBan } : null,
          idChatLieu: this.addForm.idChatLieu ? { id: this.addForm.idChatLieu } : null,
          idNguoiDich: this.addForm.idNguoiDich ? { id: this.addForm.idNguoiDich } : null,
          idTheLoai: this.addForm.idTheLoai ? { id: this.addForm.idTheLoai } : null,
          idNgonNgu: this.addForm.idNgonNgu ? { id: this.addForm.idNgonNgu } : null,
        };

        await ChiTietSanPhamService.create(newData);
        alert("Thêm chi tiết sách thành công!");
        this.closeAddModal();
        await this.loadChiTietSanPham();
      } catch (error) {
        console.error("Error adding chi tiet san pham:", error);
        alert("Có lỗi xảy ra khi thêm chi tiết sách");
      }
    },

    openEditModal(chiTiet) {
      this.editForm = {
        id: chiTiet.id,
        maChiTietSanPham: chiTiet.maChiTietSanPham,
        tenChiTietSanPham: chiTiet.tenChiTietSanPham,
        gia: Number(chiTiet.gia),
        soLuongTon: chiTiet.soLuongTon,
        trongLuong: Number(chiTiet.trongLuong),
        kichThuoc: Number(chiTiet.kichThuoc),
        trangThai: chiTiet.trangThai,
        moTa: chiTiet.moTa || '',
        ghiChu: chiTiet.ghiChu || '',
        idSanPham: chiTiet.idSanPham ? chiTiet.idSanPham.id : null,
        idLoaiBia: chiTiet.idLoaiBia ? chiTiet.idLoaiBia.id : null,
        idTacGia: chiTiet.idTacGia ? chiTiet.idTacGia.id : null,
        idNhaXuatBan: chiTiet.idNhaXuatBan ? chiTiet.idNhaXuatBan.id : null,
        idChatLieu: chiTiet.idChatLieu ? chiTiet.idChatLieu.id : null,
        idNguoiDich: chiTiet.idNguoiDich ? chiTiet.idNguoiDich.id : null,
        idTheLoai: chiTiet.idTheLoai ? chiTiet.idTheLoai.id : null,
        idNgonNgu: chiTiet.idNgonNgu ? chiTiet.idNgonNgu.id : null,
      };
      this.showEditModal = true;
    },

    closeEditModal() {
      this.showEditModal = false;
      this.editForm = {
        id: null,
        maChiTietSanPham: '',
        tenChiTietSanPham: '',
        gia: 0,
        soLuongTon: 0,
        trongLuong: 0,
        kichThuoc: 0,
        trangThai: true,
        moTa: '',
        ghiChu: '',
        idSanPham: null,
        idLoaiBia: null,
        idTacGia: null,
        idNhaXuatBan: null,
        idChatLieu: null,
        idNguoiDich: null,
        idTheLoai: null,
        idNgonNgu: null,
      };
    },

    async handleUpdate() {
      try {
        const updatedData = {
          id: this.editForm.id,
          maChiTietSanPham: this.editForm.maChiTietSanPham,
          tenChiTietSanPham: this.editForm.tenChiTietSanPham,
          gia: this.editForm.gia,
          soLuongTon: this.editForm.soLuongTon,
          trongLuong: this.editForm.trongLuong,
          kichThuoc: this.editForm.kichThuoc,
          trangThai: this.editForm.trangThai,
          moTa: this.editForm.moTa,
          ghiChu: this.editForm.ghiChu,
          idSanPham: { id: this.editForm.idSanPham },
          idLoaiBia: this.editForm.idLoaiBia ? { id: this.editForm.idLoaiBia } : null,
          idTacGia: this.editForm.idTacGia ? { id: this.editForm.idTacGia } : null,
          idNhaXuatBan: this.editForm.idNhaXuatBan ? { id: this.editForm.idNhaXuatBan } : null,
          idChatLieu: this.editForm.idChatLieu ? { id: this.editForm.idChatLieu } : null,
          idNguoiDich: this.editForm.idNguoiDich ? { id: this.editForm.idNguoiDich } : null,
          idTheLoai: this.editForm.idTheLoai ? { id: this.editForm.idTheLoai } : null,
          idNgonNgu: this.editForm.idNgonNgu ? { id: this.editForm.idNgonNgu } : null,
        };

        await ChiTietSanPhamService.update(this.editForm.id, updatedData);
        alert("Cập nhật chi tiết sách thành công!");
        this.closeEditModal();
        await this.loadChiTietSanPham();
      } catch (error) {
        console.error("Error updating chi tiet san pham:", error);
        alert("Có lỗi xảy ra khi cập nhật chi tiết sách");
      }
    },

    async deleteChiTietSach(id) {
      if (confirm("Bạn có chắc chắn muốn xóa chi tiết sách này?")) {
        try {
          await ChiTietSanPhamService.delete(id);
          alert("Xóa chi tiết sách thành công!");
          await this.loadChiTietSanPham();
        } catch (error) {
          console.error("Error deleting chi tiet san pham:", error);
          alert("Có lỗi xảy ra khi xóa chi tiết sách");
        }
      }
    },
  },
};
</script>