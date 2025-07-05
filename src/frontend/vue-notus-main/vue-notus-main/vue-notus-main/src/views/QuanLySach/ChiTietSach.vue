<template>
  <div class="min-h-screen w-full bg-gray-100 font-roboto">
    <div class="bg-white p-4 shadow rounded w-full">
      <!-- Thanh tiêu đề và công cụ -->
      <div class="flex items-center justify-between mb-4">
        <h2 class="text-lg font-bold">Chi tiết sách</h2>
        <div class="flex items-center justify-between gap-3">
          <button
              @click="goBack"
              type="button"
              class="flex items-center gap-2 text-white bg-gradient-to-r from-indigo-500 via-indigo-600 to-indigo-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-indigo-300 shadow-lg shadow-indigo-500/50 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2"
          >
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4">
              <path stroke-linecap="round" stroke-linejoin="round" d="M10.5 19.5 3 12m0 0 7.5-7.5M3 12h18" />
            </svg>
            Quay lại
          </button>
          <button
              @click="showAllChiTiet"
              type="button"
              class="flex items-center gap-2 text-white bg-gradient-to-r from-purple-500 via-purple-600 to-purple-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-purple-300 shadow-lg shadow-purple-500/50 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2"
          >
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4">
              <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 12h16.5m-16.5 3.75h16.5M3.75 9.75h16.5" />
            </svg>
            Hiển thị tất cả
          </button>
          <button
              @click="openAddModal"
              type="button"
              class="flex items-center gap-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2"
          >
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
            </svg>
            Tạo chi tiết sách
          </button>
        </div>
      </div>

      <!-- Bộ lọc -->
      <div v-if="!loading && !error" class="mb-4 grid grid-cols-5 gap-4">
        <div class="col-span-1">
          <label class="block text-sm font-medium text-gray-700">Tìm kiếm</label>
          <input
              v-model="filters.searchQuery"
              type="text"
              class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              placeholder="Tìm theo mã, tên, số lượng, trọng lượng, kích thước"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">Giá tối thiểu</label>
          <input
              v-model.number="filters.minPrice"
              type="number"
              min="0"
              class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              placeholder="0"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">Giá tối đa</label>
          <input
              v-model.number="filters.maxPrice"
              type="number"
              min="0"
              class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              placeholder="∞"
          />
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

      <!-- Loading state -->
      <div v-if="loading" class="p-4 text-center">
        <svg class="animate-spin inline-block w-5 h-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 12a8 8 0 1116 0 8 8 0 01-16 0z" />
        </svg>
        Đang tải...
      </div>

      <!-- Error state - Not Found -->
      <div v-else-if="error && error.includes('không tìm thấy')" class="p-8 text-center">
        <div class="mb-4">
          <svg class="w-10 h-10 text-yellow-500 mb-4 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01M12 3a9 9 0 110 18 9 9 0 010-18z" />
          </svg>
          <p class="text-lg text-gray-700 mb-6">
            Chưa có chi tiết sản phẩm cho sách này
          </p>
          <button
              @click="openAddModal"
              class="flex items-center gap-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-5 py-2.5 text-center mx-auto"
          >
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
            </svg>
            Thêm chi tiết sản phẩm
          </button>
        </div>
      </div>

      <!-- Other Error state -->
      <div v-else-if="error" class="p-4 text-red-500 text-center">
        <svg class="w-5 h-5 inline mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01M12 3a9 9 0 110 18 9 9 0 010-18z" />
        </svg>
        {{ error }}
      </div>

      <!-- Nội dung bảng -->
      <div v-else class="relative overflow-x-auto sm:rounded-lg w-full h-[calc(130vh-250px)]">
        <table class="w-full text-sm text-center text-gray-500 sticky-table">
          <thead class="text-xs text-gray-700 uppercase bg-gray-50">
          <tr>
            <th scope="col" class="px-6 py-3 sticky-col-1">STT</th>
            <th scope="col" class="px-6 py-3">Ảnh</th>
            <th scope="col" class="px-6 py-3">Mã chi tiết sản phẩm</th>
            <th scope="col" class="px-6 py-3">Tên sách</th>
            <th scope="col" class="px-6 py-3">Giá</th>
            <th scope="col" class="px-6 py-3">Số lượng tồn</th>
            <th scope="col" class="px-6 py-3">Trọng lượng (gram)</th>
            <th scope="col" class="px-6 py-3">Kích thước (cm)</th>
            <th scope="col" class="px-6 py-3">Trạng thái</th>
            <th scope="col" class="px-6 py-3">Loại bìa</th>
            <th scope="col" class="px-6 py-3">Tác giả</th>
            <th scope="col" class="px-6 py-3">Nhà xuất bản</th>
            <th scope="col" class="px-6 py-3">Chất liệu</th>
            <th scope="col" class="px-6 py-3">Người dịch</th>
            <th scope="col" class="px-6 py-3">Thể loại</th>
            <th scope="col" class="px-6 py-3">Ngôn ngữ</th>
            <th scope="col" class="px-6 py-3 sticky-col-action">Hành động</th>
          </tr>
          </thead>
          <tbody>
          <tr class="bg-white border-b hover:bg-gray-50" v-for="(chiTiet, index) in paginatedChiTietSanPham" :key="chiTiet.id">
            <td class="px-6 py-4 sticky-col-1">{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
            <td class="px-6 py-4">
              <div class="relative w-20 h-20 mx-auto">
                <div v-if="chiTiet.images && chiTiet.images.length > 0" class="image-container">
                  <img
                      v-for="(image, imgIndex) in chiTiet.images"
                      :key="image.id"
                      :src="image.url"
                      class="w-full h-full object-cover absolute top-0 left-0"
                      :class="{'image-active': imgIndex === currentImageIndex[chiTiet.id], 'image-inactive': imgIndex !== currentImageIndex[chiTiet.id]}"
                      alt="Chi tiết sản phẩm"
                  />
                </div>
                <span v-else class="text-gray-500">Không có ảnh</span>
              </div>
            </td>
            <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">{{ chiTiet.maChiTietSanPham }}</td>
            <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">{{ chiTiet.tenChiTietSanPham }}</td>
            <td class="px-6 py-4">{{ formatCurrency(chiTiet.gia) }}</td>
            <td class="px-6 py-4">{{ chiTiet.soLuongTon }}</td>
            <td class="px-6 py-4">{{ chiTiet.trongLuong }}</td>
            <td class="px-6 py-4">{{ chiTiet.kichThuoc }}</td>
            <td class="px-6 py-4 text-center">
              <span :class="chiTiet.trangThai
                ? 'bg-green-100 text-green-700 px-2 py-1 text-xs text-center font-semibold rounded-lg'
                : 'bg-red-100 text-red-700 px-2 py-1 text-xs text-center font-semibold rounded-lg'">
                {{ chiTiet.trangThai ? 'Hoạt động' : 'Không hoạt động' }}
              </span>
            </td>
            <td class="px-6 py-4">{{ chiTiet.idLoaiBia?.tenLoaiBia || 'N/A' }}</td>
            <td class="px-6 py-4">{{ chiTiet.idTacGia?.tenTacGia || 'N/A' }}</td>
            <td class="px-6 py-4">{{ chiTiet.idNhaXuatBan?.tenNhaXuatBan || 'N/A' }}</td>
            <td class="px-6 py-4">{{ chiTiet.idChatLieu?.tenChatLieu || 'N/A' }}</td>
            <td class="px-6 py-4">{{ chiTiet.idNguoiDich?.tenNguoiDich || 'N/A' }}</td>
            <td class="px-6 py-4">{{ chiTiet.idTheLoai?.tenTheLoai || 'N/A' }}</td>
            <td class="px-6 py-4">{{ chiTiet.idNgonNgu?.tenNgonNgu || 'N/A' }}</td>
            <td class="px-6 py-4 align-middle sticky-col-action">
              <div class="flex items-center justify-center space-x-3 h-full">
                <a href="#" @click="openEditModal(chiTiet)" class="text-blue-600 hover:text-blue-800">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M16.862 3.487a2.25 2.25 0 1 1 3.182 3.182L6.75 19.5l-4.5 1.5 1.5-4.5 13.294-13.294z" />
                  </svg>
                </a>
                <a href="#" @click="deleteChiTietSach(chiTiet.id)" class="text-red-600 hover:text-red-800">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
                  </svg>
                </a>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Phân trang -->
      <div v-if="!loading && !error" class="flex justify-center items-center mt-4 space-x-2">
        <button
            @click="prevPage"
            :disabled="currentPage === 1"
            class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm p-2 text-center flex items-center justify-center"
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-4 h-4">
            <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5 8.25 12l7.5-7.5" />
          </svg>
        </button>
        <span class="text-xs font-semibold text-gray-700">
          Trang {{ currentPage }} / {{ totalPages }}
        </span>
        <button
            @click="nextPage"
            :disabled="currentPage === totalPages"
            class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm p-2 text-center flex items-center justify-center"
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-4 h-4">
            <path stroke-linecap="round" stroke-linejoin="round" d="m8.25 4.5 7.5 7.5-7.5 7.5" />
          </svg>
        </button>
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
                  <label class="block text-sm font-medium text-gray-700">Chất liệu</label>
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

            <!-- Thêm ảnh -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Ảnh sản phẩm</label>
              <div class="flex flex-wrap gap-4">
                <div v-for="(image, index) in addForm.images" :key="index" class="relative">
                  <img :src="image.url" class="w-32 h-32 object-cover rounded-md shadow-sm" alt="Preview" />
                  <button
                      @click="removeImage(index)"
                      type="button"
                      class="absolute top-0 right-0 bg-red-500 text-white rounded-full w-6 h-6 flex items-center justify-center"
                  >
                    &times;
                  </button>
                </div>
                <div>
                  <label class="w-32 h-32 flex items-center justify-center border-2 border-dashed border-gray-300 rounded-md cursor-pointer hover:border-indigo-500">
                    <input
                        type="file"
                        accept="image/*"
                        class="hidden"
                        @change="handleImageUpload"
                    />
                    <svg xmlns="http://www.w3.org/2000/svg" class="w-8 h-8 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                    </svg>
                  </label>
                </div>
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
import AnhSanPhamService from "@/service/AnhSanPhamService";
import { reactive } from "vue";

export default {
  name: "ChiTietSach",

  data() {
    return {
      chiTietSanPham: [], // Đảm bảo khởi tạo là mảng rỗng
      loading: true,
      error: null,
      showEditModal: false,
      showAddModal: false,
      currentPage: 1,
      itemsPerPage: 6,
      currentImageIndex: reactive({}),
      imageIntervals: {},
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
        images: [], // Thêm trường images để lưu ảnh tạm thời
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

  computed: {
    filteredChiTietSanPham() {
      return this.chiTietSanPham.filter(item => {
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
    paginatedChiTietSanPham() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredChiTietSanPham.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.filteredChiTietSanPham.length / this.itemsPerPage);
    }
  },

  watch: {
    filters: {
      handler() {
        this.currentPage = 1;
      },
      deep: true
    },
    chiTietSanPham: {
      handler() {
        console.log("[DEBUG] chiTietSanPham updated, resetting image intervals");
        this.clearImageIntervals();
        if (document.visibilityState === 'visible') {
          this.setupImageIntervals();
        }
      },
      deep: true
    }
  },

  created() {
    console.log("[INFO] Initializing ChiTietSach component");
    this.loadInitialData();
    this.loadChiTietSanPham();
    // Lắng nghe sự kiện visibilitychange
    document.addEventListener('visibilitychange', this.handleVisibilityChange);
  },

  beforeUnmount() {
    console.log("[INFO] Component ChiTietSach is being destroyed, clearing image intervals and removing event listener");
    this.clearImageIntervals();
    document.removeEventListener('visibilitychange', this.handleVisibilityChange);
  },

  methods: {
    handleVisibilityChange() {
      if (document.visibilityState === 'hidden') {
        console.log("[INFO] Tab is hidden, clearing image intervals");
        this.clearImageIntervals();
      } else if (document.visibilityState === 'visible') {
        console.log("[INFO] Tab is visible, setting up image intervals");
        this.setupImageIntervals();
      }
    },

    async loadChiTietSanPham() {
      console.log("[INFO] Loading chiTietSanPham for idSanPham:", this.$route.params.id);
      try {
        this.loading = true;
        this.error = null;
        const idSanPham = this.$route.params.id;
        const response = await ChiTietSanPhamService.getBySanPhamId(idSanPham);
        // Đảm bảo chiTietSanPham là mảng
        this.chiTietSanPham = Array.isArray(response) ? response : [];
        if (this.chiTietSanPham.length === 0) {
          console.log("[DEBUG] No chiTietSanPham found for idSanPham:", idSanPham);
          this.error = "Không tìm thấy chi tiết sản phẩm cho sách này";
          return;
        }
        console.log("[DEBUG] Loaded chiTietSanPham:", this.chiTietSanPham.length);
        for (let chiTiet of this.chiTietSanPham) {
          if (chiTiet.id) { // Kiểm tra id hợp lệ
            chiTiet.images = await AnhSanPhamService.getImagesByChiTietSanPhamId(chiTiet.id) || [];
            console.log("[DEBUG] Loaded images for chiTietSanPhamId:", chiTiet.id, chiTiet.images.length);
            this.currentImageIndex[chiTiet.id] = 0;
            // Preload ảnh để tránh hiện tượng trắng
            chiTiet.images.forEach(image => {
              const img = new Image();
              img.src = image.url;
            });
          } else {
            console.warn("[WARN] Chi tiết sản phẩm không có ID:", chiTiet);
            chiTiet.images = [];
          }
        }
      } catch (error) {
        console.error("[ERROR] Error loading chi tiet san pham:", error);
        this.error = error.response?.data?.message || "Không tìm thấy chi tiết sản phẩm cho sách này";
      } finally {
        this.loading = false;
        console.log("[INFO] Finished loading chiTietSanPham");
      }
    },

    async loadInitialData() {
      console.log("[INFO] Loading initial data");
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
        this.error = "Có lỗi xảy ra khi tải dữ liệu danh sách";
      }
    },

    setupImageIntervals() {
      console.log("[INFO] Setting up image intervals");
      this.chiTietSanPham.forEach(chiTiet => {
        if (chiTiet.id && chiTiet.images && chiTiet.images.length > 1) {
          console.log("[DEBUG] Setting up interval for chiTietSanPhamId:", chiTiet.id, "with", chiTiet.images.length, "images");
          this.imageIntervals[chiTiet.id] = setInterval(() => {
            const currentIndex = this.currentImageIndex[chiTiet.id] || 0;
            const nextIndex = (currentIndex + 1) % chiTiet.images.length;
            console.log("[DEBUG] Changing image for chiTietSanPhamId:", chiTiet.id, "from index:", currentIndex, "to:", nextIndex);
            this.currentImageIndex[chiTiet.id] = nextIndex;
          }, 3000);
        } else {
          console.log("[DEBUG] No interval set for chiTietSanPhamId:", chiTiet.id, "images count:", chiTiet.images?.length || 0);
        }
      });
    },
    showAllChiTiet() {
      console.log("[INFO] Navigating to /admin/sach/all-sach");
      this.$router.push("/admin/sach/all-sach");
    },
    clearImageIntervals() {
      console.log("[INFO] Clearing image intervals");
      Object.keys(this.imageIntervals).forEach(id => {
        console.log("[DEBUG] Clearing interval for chiTietSanPhamId:", id);
        clearInterval(this.imageIntervals[id]);
      });
      this.imageIntervals = {};
    },

    formatCurrency(value) {
      return new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND",
      }).format(value);
    },

    goBack() {
      console.log("[INFO] Navigating back to /admin/sach");
      this.$router.push("/admin/sach");
    },

    openAddModal() {
      console.log("[INFO] Opening add modal");
      this.addForm.idSanPham = this.$route.params.id;
      this.addForm.images = []; // Reset images
      this.showAddModal = true;
    },

    closeAddModal() {
      console.log("[INFO] Closing add modal");
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
        images: [],
      };
    },

    async handleAdd() {
      console.log("[INFO] Handling add chiTietSanPham");
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
        console.log("[DEBUG] Adding new chiTietSanPham:", newData);

        const response = await ChiTietSanPhamService.create(newData);
        const chiTietSanPhamId = response.id;

        // Upload ảnh nếu có
        if (this.addForm.images.length > 0) {
          for (const image of this.addForm.images) {
            const formData = new FormData();
            formData.append('file', image.file);
            formData.append('chiTietSanPhamId', chiTietSanPhamId);
            await AnhSanPhamService.uploadImage(formData);
          }
        }

        alert("Thêm chi tiết sách thành công!");
        this.closeAddModal();
        await this.loadChiTietSanPham();
      } catch (error) {
        console.error("[ERROR] Error adding chi tiet san pham:", error);
        alert("Có lỗi xảy ra khi thêm chi tiết sách");
      }
    },

    handleImageUpload(event) {
      console.log("[INFO] Handling image upload");
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.addForm.images.push({
            url: e.target.result,
            file: file
          });
          console.log("[DEBUG] Added image to addForm.images:", this.addForm.images.length);
        };
        reader.readAsDataURL(file);
      }
    },

    removeImage(index) {
      console.log("[INFO] Removing image at index:", index);
      this.addForm.images.splice(index, 1);
      console.log("[DEBUG] Updated addForm.images:", this.addForm.images.length);
    },

    openEditModal(chiTiet) {
      console.log("[INFO] Opening edit modal for chiTietSanPhamId:", chiTiet.id);
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
      console.log("[INFO] Closing edit modal");
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
      console.log("[INFO] Handling update chiTietSanPhamId:", this.editForm.id);
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
        console.log("[DEBUG] Updating chiTietSanPham:", updatedData);

        await ChiTietSanPhamService.update(this.editForm.id, updatedData);
        alert("Cập nhật chi tiết sách thành công!");
        this.closeEditModal();
        await this.loadChiTietSanPham();
      } catch (error) {
        console.error("[ERROR] Error updating chi tiet san pham:", error);
        alert("Có lỗi xảy ra khi cập nhật chi tiết sách");
      }
    },

    async deleteChiTietSach(id) {
      console.log("[INFO] Confirming deletion for chiTietSanPhamId:", id);
      if (confirm("Bạn có chắc chắn muốn xóa chi tiết sách này?")) {
        try {
          await ChiTietSanPhamService.delete(id);
          console.log("[INFO] Deleted chiTietSanPhamId:", id);
          alert("Xóa chi tiết sách thành công!");
          await this.loadChiTietSanPham();
        } catch (error) {
          console.error("[ERROR] Error deleting chi tiet san pham:", error);
          alert("Có lỗi xảy ra khi xóa chi tiết sách");
        }
      }
    },

    prevPage() {
      if (this.currentPage > 1) {
        console.log("[INFO] Navigating to previous page:", this.currentPage - 1);
        this.currentPage--;
      }
    },

    nextPage() {
      if (this.currentPage < this.totalPages) {
        console.log("[INFO] Navigating to next page:", this.currentPage + 1);
        this.currentPage++;
      }
    },
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');
.font-roboto {
  font-family: 'Roboto', sans-serif;
}
th {
  min-width: 150px;
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

/* Sticky table styles */
.sticky-table {
  position: relative;
}

.sticky-col-1 {
  position: sticky;
  left: 0;
  background-color: white;
  z-index: 10;
  border-right: 2px solid #e5e7eb;
}

.sticky-col-action {
  position: sticky;
  right: 0;
  background-color: white;
  z-index: 10;
  border-left: 2px solid #e5e7eb;
}

/* Header sticky columns */
thead .sticky-col-1 {
  background-color: #f9fafb;
  z-index: 20;
}

thead .sticky-col-action {
  background-color: #f9fafb;
  z-index: 20;
}

/* Hover effect for sticky columns */
tbody tr:hover .sticky-col-1,
tbody tr:hover .sticky-col-action {
  background-color: #f3f4f6;
}

/* Ensure proper layering */
.sticky-col-1 {
  box-shadow: 2px 0 4px rgba(0, 0, 0, 0.1);
}

.sticky-col-action {
  box-shadow: -2px 0 4px rgba(0, 0, 0, 0.1);
}
</style>