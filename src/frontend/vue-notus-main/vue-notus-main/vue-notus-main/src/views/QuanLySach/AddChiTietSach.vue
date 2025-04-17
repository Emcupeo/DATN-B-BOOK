<template>
  <div class="min-h-screen w-full bg-gray-100 font-roboto">
    <div class="bg-white p-4 shadow rounded w-full">
      <!-- Thanh tiêu đề -->
      <div class="mb-4">
        <h2 class="text-lg font-bold">Thêm sách và chi tiết</h2>
      </div>

      <!-- Form -->
      <form @submit.prevent="saveSanPhamAndChiTiet">
        <div class="grid grid-cols-2 gap-4">
          <!-- Tên sách -->
          <div class="relative">
            <label class="block text-sm font-medium text-gray-700">Tên sách</label>
            <input
                v-model="formData.tenSanPham"
                type="text"
                class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500 mt-1"
                required
                @focus="showSanPhamList = true"
                @blur="delayHideSanPhamList"
                @input="showSanPhamList = false"
            />
            <div
                v-if="showSanPhamList && sanPhamList.length > 0"
                class="absolute z-10 mt-1 w-full bg-white border rounded shadow-lg max-h-60 overflow-y-auto"
            >
              <div
                  v-for="sanPham in sanPhamList"
                  :key="sanPham.id"
                  class="px-3 py-2 hover:bg-gray-100 cursor-pointer"
                  @mousedown="selectSanPham(sanPham.tenSanPham)"
              >
                {{ sanPham.tenSanPham }}
              </div>
            </div>
          </div>

          <!-- Tác giả -->
          <div>
            <label class="block text-sm font-medium text-gray-700">Tác giả</label>
            <div class="flex items-center mt-1">
              <select
                  v-model="formData.idTacGia"
                  class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
              >
                <option v-for="item in tacGiaList" :key="item.id" :value="item.id">
                  {{ item.tenTacGia }}
                </option>
              </select>
              <button
                  type="button"
                  @click="openAddModal('tacGia')"
                  class="ml-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-3 py-2"
              >
                +
              </button>
            </div>
          </div>

          <!-- Nhà xuất bản -->
          <div>
            <label class="block text-sm font-medium text-gray-700">Nhà xuất bản</label>
            <div class="flex items-center mt-1">
              <select
                  v-model="formData.idNhaXuatBan"
                  class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
              >
                <option v-for="item in nhaXuatBanList" :key="item.id" :value="item.id">
                  {{ item.tenNhaXuatBan }}
                </option>
              </select>
              <button
                  type="button"
                  @click="openAddModal('nhaXuatBan')"
                  class="ml-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-3 py-2"
              >
                +
              </button>
            </div>
          </div>

          <!-- Người dịch -->
          <div>
            <label class="block text-sm font-medium text-gray-700">Người dịch</label>
            <div class="flex items-center mt-1">
              <select
                  v-model="formData.idNguoiDich"
                  class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
              >
                <option v-for="item in nguoiDichList" :key="item.id" :value="item.id">
                  {{ item.tenNguoiDich }}
                </option>
              </select>
              <button
                  type="button"
                  @click="openAddModal('nguoiDich')"
                  class="ml-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-3 py-2"
              >
                +
              </button>
            </div>
          </div>

          <!-- Thể loại -->
          <div>
            <label class="block text-sm font-medium text-gray-700">Thể loại</label>
            <div class="flex items-center mt-1">
              <select
                  v-model="formData.idTheLoai"
                  class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
              >
                <option v-for="item in theLoaiList" :key="item.id" :value="item.id">
                  {{ item.tenTheLoai }}
                </option>
              </select>
              <button
                  type="button"
                  @click="openAddModal('theLoai')"
                  class="ml-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-3 py-2"
              >
                +
              </button>
            </div>
          </div>

          <!-- Ngôn ngữ -->
          <div>
            <label class="block text-sm font-medium text-gray-700">Ngôn ngữ</label>
            <div class="flex items-center mt-1">
              <select
                  v-model="formData.idNgonNgu"
                  class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
              >
                <option v-for="item in ngonNguList" :key="item.id" :value="item.id">
                  {{ item.tenNgonNgu }}
                </option>
              </select>
              <button
                  type="button"
                  @click="openAddModal('ngonNgu')"
                  class="ml-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-3 py-2"
              >
                +
              </button>
            </div>
          </div>
        </div>

        <!-- Mô tả sách -->
        <div class="mt-6">
          <label class="block text-sm font-medium text-gray-700">Mô tả sách</label>
          <textarea
              v-model="formData.moTaSanPham"
              class="block w-full p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500 mt-1"
              rows="5"
          ></textarea>
        </div>

        <!-- Thuộc tính chi tiết -->
        <div class="mt-6 border-t pt-4">
          <h4 class="text-md font-semibold text-gray-700 mb-4">Thuộc tính chi tiết</h4>
          <div class="grid grid-cols-2 gap-4">
            <!-- Loại bìa -->
            <div>
              <label class="block text-sm font-medium text-gray-700">Loại bìa</label>
              <button
                  type="button"
                  @click="openModal('loaiBia')"
                  class="mt-1 w-full text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
              >
                <span class="mr-2">+</span> Chọn loại bìa
              </button>
              <div class="mt-2" v-if="formData.idLoaiBiaList.length > 0">
                <span v-for="id in formData.idLoaiBiaList" :key="id" class="inline-block bg-gray-200 rounded px-2 py-1 mr-2 mb-2">
                  {{ getLoaiBiaName(id) }}
                </span>
              </div>
            </div>

            <!-- Chất liệu -->
            <div>
              <label class="block text-sm font-medium text-gray-700">Chất liệu</label>
              <button
                  type="button"
                  @click="openModal('chatLieu')"
                  class="mt-1 w-full text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
              >
                <span class="mr-2">+</span> Chọn chất liệu
              </button>
              <div class="mt-2" v-if="formData.idChatLieuList.length > 0">
                <span v-for="id in formData.idChatLieuList" :key="id" class="inline-block bg-gray-200 rounded px-2 py-1 mr-2 mb-2">
                  {{ getChatLieuName(id) }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- Bảng chi tiết sản phẩm (dựa trên Loại bìa) -->
        <div class="mt-6" v-if="formData.idChatLieuList.length > 0 && formData.idLoaiBiaList.length > 0">
          <h4 class="text-md font-semibold text-gray-700 mb-4">Danh sách chi tiết sản phẩm</h4>
          <div class="grid grid-cols-1 gap-6">
            <div v-for="idLoaiBia in formData.idLoaiBiaList" :key="idLoaiBia" class="mb-6">
              <div class="flex justify-between items-center mb-2">
                <h5 class="font-medium text-gray-700">{{ getLoaiBiaName(idLoaiBia) }}</h5>
                <button
                    type="button"
                    @click="openBulkEditModal(idLoaiBia)"
                    class="text-white bg-gradient-to-r from-yellow-500 via-yellow-600 to-yellow-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-yellow-300 shadow-lg shadow-yellow-500/50 font-medium rounded-lg text-sm px-3 py-1 text-center"
                >
                  Sửa chung
                </button>
              </div>
              <div class="relative overflow-x-auto sm:rounded-lg">
                <table class="w-full text-sm text-center text-gray-500">
                  <thead class="text-xs text-gray-700 uppercase bg-gray-50">
                  <tr>
                    <th class="px-2 py-3 w-12">
                      <input
                          type="checkbox"
                          :checked="isAllSelected(idLoaiBia)"
                          @change="toggleSelectAll(idLoaiBia, $event.target.checked)"
                      />
                    </th>
                    <th class="px-2 py-3 w-12">STT</th>
                    <th class="px-2 py-3 w-40">Tên chi tiết sản phẩm</th>
                    <th class="px-2 py-3 w-20">Số lượng</th>
                    <th class="px-2 py-3 w-24">Giá bán</th>
                    <th class="px-2 py-3 w-24">Trọng lượng (gram)</th>
                    <th class="px-2 py-3 w-24">Kích thước (cm)</th>
                    <th class="px-2 py-3 w-20">Hành động</th>
                    <th class="px-2 py-3 w-96">Ảnh</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="(chiTiet, index) in getChiTietByLoaiBia(idLoaiBia)" :key="index" class="bg-white border-b hover:bg-gray-50">
                    <td class="px-2 py-4">
                      <input type="checkbox" v-model="chiTiet.selected" />
                    </td>
                    <td class="px-2 py-4">{{ index + 1 }}</td>
                    <td class="px-2 py-4 font-medium text-gray-900 whitespace-nowrap">{{ chiTiet.tenChiTietSanPham }}</td>
                    <td class="px-2 py-4">
                      <input
                          v-model.number="chiTiet.soLuongTon"
                          type="number"
                          min="1"
                          class="block w-16 h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
                      />
                    </td>
                    <td class="px-2 py-4">
                      <input
                          v-model.number="chiTiet.gia"
                          type="number"
                          step="0.01"
                          class="block w-20 h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
                      />
                    </td>
                    <td class="px-2 py-4">
                      <input
                          v-model.number="chiTiet.trongLuong"
                          type="number"
                          step="0.01"
                          class="block w-20 h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
                      />
                    </td>
                    <td class="px-2 py-4">
                      <input
                          v-model.number="chiTiet.kichThuoc"
                          type="number"
                          step="0.01"
                          class="block w-20 h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
                      />
                    </td>
                    <td class="px-2 py-4">
                      <div class="flex justify-center items-center h-full">
                        <a href="#" @click.prevent="confirmRemoveChiTiet(idLoaiBia, chiTiet.idChatLieu)" class="text-red-600 hover:text-red-800">
                          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
                          </svg>
                        </a>
                      </div>
                    </td>
                    <td
                        v-if="index === 0"
                        :rowspan="getChiTietByLoaiBia(idLoaiBia).length"
                        class="px-2 py-4 align-middle"
                    >
                      <div class="flex items-center space-x-2">
                        <button
                            type="button"
                            @click="openImageModal(idLoaiBia)"
                            class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-3 py-2"
                        >
                          Thêm ảnh
                        </button>
                        <div class="flex space-x-2 overflow-x-auto max-w-[300px]">
                          <div v-for="imageId in getImageIdsForLoaiBia(idLoaiBia)" :key="imageId" class="relative">
                            <img
                                :src="getImageUrl(imageId)"
                                class="w-24 h-24 object-cover rounded"
                                alt="Preview"
                            />
                            <button
                                @click="removeImage(idLoaiBia, imageId)"
                                class="absolute top-0 right-0 bg-red-500 text-white rounded-full w-5 h-5 flex items-center justify-center"
                            >
                              x
                            </button>
                          </div>
                        </div>
                      </div>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>

        <!-- Nút hành động -->
        <div class="flex justify-end space-x-3 mt-6">
          <button
              type="button"
              @click="goBack"
              class="text-white bg-gradient-to-r from-gray-500 via-gray-600 to-gray-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-gray-300 shadow-lg shadow-gray-500/50 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
          >
            Hủy
          </button>
          <button
              type="submit"
              class="flex items-center gap-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
          >
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
            </svg>
            Lưu
          </button>
        </div>
      </form>

      <!-- Modal chọn Loại bìa/Chất liệu -->
      <div v-if="showModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white p-6 rounded shadow-lg w-1/3">
          <h3 class="text-lg font-semibold mb-4">{{ modalType === 'loaiBia' ? 'Chọn loại bìa' : 'Chọn chất liệu' }}</h3>
          <div class="mb-4">
            <input
                v-model="newItemName"
                type="text"
                class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
                :placeholder="modalType === 'loaiBia' ? 'Thêm loại bìa mới' : 'Thêm chất liệu mới'"
            />
            <button
                type="button"
                @click="addNewItem"
                class="mt-2 w-full text-white bg-gradient-to-r from-green-500 via-green-600 to-green-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-green-300 shadow-lg shadow-green-500/50 font-medium rounded-lg text-sm px-3 py-2 text-center"
            >
              Thêm nhanh
            </button>
          </div>
          <div class="max-h-64 overflow-y-auto">
            <div v-if="modalType === 'loaiBia'">
              <label v-for="item in loaiBiaList" :key="item.id" class="block mb-2">
                <input type="checkbox" :value="item.id" v-model="tempLoaiBiaList" class="mr-2" />
                {{ item.tenLoaiBia }}
              </label>
            </div>
            <div v-else>
              <label v-for="item in chatLieuList" :key="item.id" class="block mb-2">
                <input type="checkbox" :value="item.id" v-model="tempChatLieuList" class="mr-2" />
                {{ item.tenChatLieu }}
              </label>
            </div>
          </div>
          <div class="flex justify-end space-x-3 mt-4">
            <button
                @click="closeModal"
                class="text-white bg-gradient-to-r from-gray-500 via-gray-600 to-gray-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-gray-300 shadow-lg shadow-gray-500/50 font-medium rounded-lg text-sm px-4 py-2 text-center"
            >
              Hủy
            </button>
            <button
                @click="saveModalSelection"
                class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-4 py-2 text-center"
            >
              Xác nhận
            </button>
          </div>
        </div>
      </div>

      <!-- Modal thêm nhanh thuộc tính -->
      <div v-if="showAddModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white p-6 rounded-lg w-1/2 shadow-lg">
          <h2 class="text-xl font-bold mb-4">Thêm {{ addModalType }}</h2>
          <form @submit.prevent="saveNewItem">
            <div class="mb-4">
              <label class="block text-sm font-medium text-gray-700 mb-1">Tên {{ addModalType }}</label>
              <input
                  v-model="newItemName"
                  type="text"
                  class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
                  :placeholder="`Nhập tên ${addModalType}`"
                  required
              />
            </div>
            <div class="flex justify-end space-x-3">
              <button
                  type="button"
                  @click="closeAddModal"
                  class="text-white bg-gradient-to-r from-gray-500 via-gray-600 to-gray-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-gray-300 shadow-lg shadow-gray-500/50 font-medium rounded-lg text-sm px-4 py-2 text-center"
              >
                Hủy
              </button>
              <button
                  type="submit"
                  class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-4 py-2 text-center"
              >
                Thêm
              </button>
            </div>
          </form>
        </div>
      </div>

      <!-- Modal sửa chung -->
      <div v-if="showBulkEditModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white p-6 rounded shadow-lg w-1/3">
          <h3 class="text-lg font-semibold mb-4">Sửa chung cho {{ getLoaiBiaName(currentLoaiBiaId) }}</h3>
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700">Số lượng</label>
            <input
                v-model.number="bulkEditData.soLuongTon"
                type="number"
                min="1"
                class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500 mt-1"
            />
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700">Giá bán (VNĐ)</label>
            <input
                v-model.number="bulkEditData.gia"
                type="number"
                step="0.01"
                class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500 mt-1"
            />
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700">Trọng lượng (gram)</label>
            <input
                v-model.number="bulkEditData.trongLuong"
                type="number"
                step="0.01"
                class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500 mt-1"
            />
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700">Kích thước (cm)</label>
            <input
                v-model.number="bulkEditData.kichThuoc"
                type="number"
                step="0.01"
                class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500 mt-1"
            />
          </div>
          <div class="flex justify-end space-x-3">
            <button
                @click="closeBulkEditModal"
                class="text-white bg-gradient-to-r from-gray-500 via-gray-600 to-gray-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-gray-300 shadow-lg shadow-gray-500/50 font-medium rounded-lg text-sm px-4 py-2 text-center"
            >
              Hủy
            </button>
            <button
                @click="saveBulkEdit"
                class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-4 py-2 text-center"
            >
              Lưu
            </button>
          </div>
        </div>
      </div>

      <!-- Modal quản lý ảnh -->
      <div v-if="showImageModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white p-6 rounded shadow-lg w-1/2">
          <h3 class="text-lg font-semibold mb-4">Quản lý ảnh cho {{ getLoaiBiaName(currentImageModal.loaiBiaId) }}</h3>
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">Tải ảnh mới</label>
            <input
                type="file"
                accept="image/*"
                multiple
                @change="handleImageUpload($event)"
                class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
            />
          </div>
          <div class="grid grid-cols-3 gap-4 max-h-64 overflow-y-auto">
            <div v-for="(image, index) in imageModalImages" :key="index" class="relative">
              <img :src="image.url" class="w-32 h-32 object-cover rounded" />
              <input
                  type="checkbox"
                  :value="image.id"
                  v-model="selectedImageIds"
                  class="absolute top-2 left-2"
                  :disabled="selectedImageIds.length >= 3 && !selectedImageIds.includes(image.id)"
              />
              <button
                  v-if="image.isNew"
                  @click="removeNewImage(index)"
                  class="absolute top-0 right-0 bg-red-500 text-white rounded-full w-10 h-10 flex items-center justify-center"
              >
                x
              </button>
            </div>
          </div>
          <div class="flex justify-end space-x-3 mt-4">
            <button
                @click="closeImageModal"
                class="text-white bg-gradient-to-r from-gray-500 via-gray-600 to-gray-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-gray-300 shadow-lg shadow-gray-500/50 font-medium rounded-lg text-sm px-4 py-2 text-center"
            >
              Hủy
            </button>
            <button
                @click="saveImageSelection"
                class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-4 py-2 text-center"
            >
              Lưu
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import LoaiBiaService from "@/service/LoaiBiaService";
import TacGiaService from "@/service/TacGiaService";
import NhaXuatBanService from "@/service/NhaXuatBanService";
import ChatLieuService from "@/service/ChatLieuService";
import NguoiDichService from "@/service/NguoiDichService";
import TheLoaiService from "@/service/TheLoaiService";
import NgonNguService from "@/service/NgonNguService";
import api from "@/service/api";

export default {
  name: "AddSanPhamAndChiTiet",
  data() {
    return {
      formData: {
        tenSanPham: "",
        moTaSanPham: "",
        idLoaiBiaList: [],
        idTacGia: "",
        idNhaXuatBan: "",
        idChatLieuList: [],
        idNguoiDich: "",
        idTheLoai: "",
        idNgonNgu: "",
      },
      loaiBiaList: [],
      tacGiaList: [],
      nhaXuatBanList: [],
      chatLieuList: [],
      nguoiDichList: [],
      theLoaiList: [],
      ngonNguList: [],
      chiTietSanPhamList: [],
      sanPhamList: [],
      showSanPhamList: false,
      loading: false,
      error: null,
      showModal: false,
      modalType: "",
      tempLoaiBiaList: [],
      tempChatLieuList: [],
      showAddModal: false,
      addModalType: "",
      newItemName: "",
      showBulkEditModal: false,
      currentLoaiBiaId: null,
      bulkEditData: {
        soLuongTon: null,
        gia: null,
        trongLuong: null,
        kichThuoc: null,
      },
      showImageModal: false,
      currentImageModal: {
        loaiBiaId: null,
      },
      imageModalImages: [],
      selectedImageIds: [],
      newImages: [],
      imageCache: {}, // Lưu trữ URL ảnh theo imageId
      loaiBiaImageIds: {}, // Lưu trữ imageIds cho mỗi idLoaiBia
    };
  },
  async created() {
    console.log("[DEBUG] Initializing AddSanPhamAndChiTiet component");
    await this.loadInitialData();
  },
  watch: {
    "formData.idLoaiBiaList": {
      handler() {
        console.log("[DEBUG] idLoaiBiaList changed:", this.formData.idLoaiBiaList);
        this.generateChiTietSanPham();
      },
      deep: true,
    },
    "formData.idChatLieuList": {
      handler() {
        console.log("[DEBUG] idChatLieuList changed:", this.formData.idChatLieuList);
        this.generateChiTietSanPham();
      },
      deep: true,
    },
  },
  methods: {
    async loadInitialData() {
      console.log("[INFO] Loading initial data");
      try {
        this.loading = true;
        this.loaiBiaList = await LoaiBiaService.getAll();
        this.tacGiaList = await TacGiaService.getAll();
        this.nhaXuatBanList = await NhaXuatBanService.getAll();
        this.chatLieuList = await ChatLieuService.getAll();
        this.nguoiDichList = await NguoiDichService.getAll();
        this.theLoaiList = await TheLoaiService.getAll();
        this.ngonNguList = await NgonNguService.getAll();
        this.sanPhamList = await api.get("/san-pham").then((res) => res.data);

        console.log("[DEBUG] Loaded data:", {
          loaiBiaList: this.loaiBiaList.length,
          tacGiaList: this.tacGiaList.length,
          nhaXuatBanList: this.nhaXuatBanList.length,
          chatLieuList: this.chatLieuList.length,
          nguoiDichList: this.nguoiDichList.length,
          theLoaiList: this.theLoaiList.length,
          ngonNguList: this.ngonNguList.length,
          sanPhamList: this.sanPhamList.length,
        });

        if (this.tacGiaList.length > 0)
          this.formData.idTacGia = this.tacGiaList[0].id;
        if (this.nhaXuatBanList.length > 0)
          this.formData.idNhaXuatBan = this.nhaXuatBanList[0].id;
        if (this.nguoiDichList.length > 0)
          this.formData.idNguoiDich = this.nguoiDichList[0].id;
        if (this.theLoaiList.length > 0)
          this.formData.idTheLoai = this.theLoaiList[0].id;
        if (this.ngonNguList.length > 0)
          this.formData.idNgonNgu = this.ngonNguList[0].id;
      } catch (error) {
        console.error("[ERROR] Error loading initial data:", error);
        this.error = "Có lỗi xảy ra khi tải dữ liệu";
      } finally {
        this.loading = false;
        console.log("[INFO] Finished loading initial data");
      }
    },
    selectSanPham(tenSanPham) {
      console.log("[DEBUG] Selected sanPham:", tenSanPham);
      this.formData.tenSanPham = tenSanPham;
      this.showSanPhamList = false;
    },
    delayHideSanPhamList() {
      console.log("[DEBUG] Delaying hide of sanPhamList");
      setTimeout(() => {
        this.showSanPhamList = false;
      }, 200);
    },
    generateChiTietSanPham() {
      console.log("[INFO] Generating chiTietSanPhamList");
      this.chiTietSanPhamList = [];
      const { idLoaiBiaList, idChatLieuList, tenSanPham } = this.formData;

      if (idLoaiBiaList.length > 0 && idChatLieuList.length > 0) {
        idLoaiBiaList.forEach((idLoaiBia) => {
          idChatLieuList.forEach((idChatLieu) => {
            this.chiTietSanPhamList.push({
              tenChiTietSanPham: `${tenSanPham} (${this.getLoaiBiaName(
                  idLoaiBia
              )} - ${this.getChatLieuName(idChatLieu)})`,
              idLoaiBia,
              idChatLieu,
              soLuongTon: 1,
              gia: 50000,
              trongLuong: 200,
              kichThuoc: 20,
              selected: false,
              imageIds: this.loaiBiaImageIds[idLoaiBia] || [], // Gán imageIds từ loaiBiaImageIds
            });
          });
        });
        console.log(
            "[DEBUG] Generated chiTietSanPhamList:",
            this.chiTietSanPhamList
        );
      } else {
        console.log("[DEBUG] No chiTietSanPham generated due to empty lists");
      }
    },
    getChiTietByLoaiBia(idLoaiBia) {
      const chiTietList = this.chiTietSanPhamList.filter(
          (chiTiet) => chiTiet.idLoaiBia === idLoaiBia
      );
      console.log(
          "[DEBUG] Retrieved chiTiet for idLoaiBia:",
          idLoaiBia,
          chiTietList
      );
      return chiTietList;
    },
    getImageIdsForLoaiBia(idLoaiBia) {
      const imageIds = this.loaiBiaImageIds[idLoaiBia] || [];
      console.log("[DEBUG] Retrieved imageIds for idLoaiBia:", idLoaiBia, imageIds);
      return imageIds;
    },
    confirmRemoveChiTiet(idLoaiBia, idChatLieu) {
      console.log(
          "[DEBUG] Confirming removal of chiTiet:",
          idLoaiBia,
          idChatLieu
      );
      if (confirm("Bạn có chắc chắn muốn xóa chi tiết sản phẩm này?")) {
        this.removeChiTiet(idLoaiBia, idChatLieu);
      }
    },
    removeChiTiet(idLoaiBia, idChatLieu) {
      console.log("[INFO] Removing chiTiet:", idLoaiBia, idChatLieu);
      this.chiTietSanPhamList = this.chiTietSanPhamList.filter(
          (chiTiet) =>
              !(chiTiet.idLoaiBia === idLoaiBia && chiTiet.idChatLieu === idChatLieu)
      );
      console.log(
          "[DEBUG] Updated chiTietSanPhamList after removal:",
          this.chiTietSanPhamList
      );
    },
    isAllSelected(idLoaiBia) {
      const chiTietList = this.getChiTietByLoaiBia(idLoaiBia);
      const allSelected =
          chiTietList.length > 0 && chiTietList.every((chiTiet) => chiTiet.selected);
      console.log("[DEBUG] Checked if all selected for idLoaiBia:", idLoaiBia, allSelected);
      return allSelected;
    },
    toggleSelectAll(idLoaiBia, checked) {
      console.log("[DEBUG] Toggling select all for idLoaiBia:", idLoaiBia, checked);
      this.chiTietSanPhamList.forEach((chiTiet) => {
        if (chiTiet.idLoaiBia === idLoaiBia) {
          chiTiet.selected = checked;
        }
      });
    },
    openBulkEditModal(idLoaiBia) {
      console.log("[INFO] Opening bulk edit modal for idLoaiBia:", idLoaiBia);
      const chiTietList = this.getChiTietByLoaiBia(idLoaiBia);
      const hasSelected = chiTietList.some((chiTiet) => chiTiet.selected);
      if (!hasSelected) {
        console.log("[DEBUG] No items selected for bulk edit");
        alert("Vui lòng chọn ít nhất một sản phẩm trước!");
        return;
      }
      this.currentLoaiBiaId = idLoaiBia;
      this.bulkEditData = {
        soLuongTon: null,
        gia: null,
        trongLuong: null,
        kichThuoc: null,
      };
      this.showBulkEditModal = true;
      console.log("[DEBUG] Bulk edit modal opened");
    },
    closeBulkEditModal() {
      console.log("[INFO] Closing bulk edit modal");
      this.showBulkEditModal = false;
      this.currentLoaiBiaId = null;
    },
    saveBulkEdit() {
      console.log("[INFO] Saving bulk edit for idLoaiBia:", this.currentLoaiBiaId);
      this.chiTietSanPhamList.forEach((chiTiet) => {
        if (chiTiet.idLoaiBia === this.currentLoaiBiaId && chiTiet.selected) {
          if (this.bulkEditData.soLuongTon !== null)
            chiTiet.soLuongTon = this.bulkEditData.soLuongTon;
          if (this.bulkEditData.gia !== null) chiTiet.gia = this.bulkEditData.gia;
          if (this.bulkEditData.trongLuong !== null)
            chiTiet.trongLuong = this.bulkEditData.trongLuong;
          if (this.bulkEditData.kichThuoc !== null)
            chiTiet.kichThuoc = this.bulkEditData.kichThuoc;
        }
      });
      console.log(
          "[DEBUG] Updated chiTietSanPhamList after bulk edit:",
          this.chiTietSanPhamList
      );
      this.closeBulkEditModal();
    },
    getLoaiBiaName(id) {
      const loaiBia = this.loaiBiaList.find((item) => item.id === id);
      const name = loaiBia ? loaiBia.tenLoaiBia : "N/A";
      console.log("[DEBUG] Retrieved loaiBia name for id:", id, name);
      return name;
    },
    getChatLieuName(id) {
      const chatLieu = this.chatLieuList.find((item) => item.id === id);
      const name = chatLieu ? chatLieu.tenChatLieu : "N/A";
      console.log("[DEBUG] Retrieved chatLieu name for id:", id, name);
      return name;
    },
    openModal(type) {
      console.log("[INFO] Opening modal for type:", type);
      this.modalType = type;
      this.showModal = true;
      this.newItemName = "";
      if (type === "loaiBia") {
        this.tempLoaiBiaList = [...this.formData.idLoaiBiaList];
      } else {
        this.tempChatLieuList = [...this.formData.idChatLieuList];
      }
    },
    closeModal() {
      console.log("[INFO] Closing modal");
      this.showModal = false;
      this.modalType = "";
      this.newItemName = "";
    },
    saveModalSelection() {
      console.log("[INFO] Saving modal selection for type:", this.modalType);
      if (this.modalType === "loaiBia") {
        this.formData.idLoaiBiaList = [...this.tempLoaiBiaList];
      } else {
        this.formData.idChatLieuList = [...this.tempChatLieuList];
      }
      console.log("[DEBUG] Updated formData:", this.formData);
      this.closeModal();
    },
    openAddModal(type) {
      console.log("[INFO] Opening add modal for type:", type);
      this.addModalType = type;
      this.showAddModal = true;
      this.newItemName = "";
    },
    closeAddModal() {
      console.log("[INFO] Closing add modal");
      this.showAddModal = false;
      this.addModalType = "";
      this.newItemName = "";
    },
    async addNewItem() {
      console.log("[INFO] Adding new item for modalType:", this.modalType);
      if (!this.newItemName) {
        console.log("[DEBUG] No item name provided");
        alert("Vui lòng nhập tên!");
        return;
      }
      try {
        let newItem;
        if (this.modalType === "loaiBia") {
          newItem = await LoaiBiaService.create({ tenLoaiBia: this.newItemName });
          this.loaiBiaList.push(newItem);
          this.tempLoaiBiaList.push(newItem.id);
        } else if (this.modalType === "chatLieu") {
          newItem = await ChatLieuService.create({ tenChatLieu: this.newItemName });
          this.chatLieuList.push(newItem);
          this.tempChatLieuList.push(newItem.id);
        }
        console.log("[DEBUG] Added new item:", newItem);
        this.newItemName = "";
        alert(
            `Đã thêm ${
                this.modalType === "loaiBia" ? "loại bìa" : "chất liệu"
            } thành công!`
        );
      } catch (error) {
        console.error("[ERROR] Error adding new item:", error);
        alert("Có lỗi xảy ra khi thêm!");
      }
    },
    async saveNewItem() {
      console.log("[INFO] Saving new item for addModalType:", this.addModalType);
      if (!this.newItemName) {
        console.log("[DEBUG] No item name provided");
        alert("Vui lòng nhập tên!");
        return;
      }
      try {
        let newItem;
        switch (this.addModalType) {
          case "tacGia":
            newItem = await TacGiaService.create({ tenTacGia: this.newItemName });
            this.tacGiaList.push(newItem);
            this.formData.idTacGia = newItem.id;
            break;
          case "nhaXuatBan":
            newItem = await NhaXuatBanService.create({
              tenNhaXuatBan: this.newItemName,
            });
            this.nhaXuatBanList.push(newItem);
            this.formData.idNhaXuatBan = newItem.id;
            break;
          case "nguoiDich":
            newItem = await NguoiDichService.create({
              tenNguoiDich: this.newItemName,
            });
            this.nguoiDichList.push(newItem);
            this.formData.idNguoiDich = newItem.id;
            break;
          case "theLoai":
            newItem = await TheLoaiService.create({ tenTheLoai: this.newItemName });
            this.theLoaiList.push(newItem);
            this.formData.idTheLoai = newItem.id;
            break;
          case "ngonNgu":
            newItem = await NgonNguService.create({ tenNgonNgu: this.newItemName });
            this.ngonNguList.push(newItem);
            this.formData.idNgonNgu = newItem.id;
            break;
        }
        console.log("[DEBUG] Saved new item:", newItem);
        this.closeAddModal();
        alert(`Đã thêm ${this.addModalType} thành công!`);
      } catch (error) {
        console.error("[ERROR] Error saving new item:", error);
        alert("Có lỗi xảy ra khi thêm!");
      }
    },
    async openImageModal(idLoaiBia) {
      console.log("[INFO] Opening image modal for idLoaiBia:", idLoaiBia);
      this.currentImageModal = { loaiBiaId: idLoaiBia };
      this.imageModalImages = [];
      this.selectedImageIds = [];
      this.newImages = [];

      try {
        console.log("[DEBUG] Fetching images for idLoaiBia:", idLoaiBia);
        const response = await api.get(`/anh-san-pham/by-loai-bia/${idLoaiBia}`);
        this.imageModalImages = response.data.map((img) => {
          this.imageCache[img.id] = img.url;
          return {
            id: img.id,
            url: img.url,
            isNew: false,
          };
        });
        console.log(
            "[DEBUG] Loaded images for modal:",
            this.imageModalImages.length
        );

        this.selectedImageIds = [...(this.loaiBiaImageIds[idLoaiBia] || [])];
        console.log(
            "[DEBUG] Selected imageIds for idLoaiBia:",
            idLoaiBia,
            this.selectedImageIds
        );

        this.showImageModal = true;
      } catch (error) {
        console.error("[ERROR] Error loading images:", error);
        alert("Có lỗi xảy ra khi tải danh sách ảnh!");
      }
    },
    closeImageModal() {
      console.log("[INFO] Closing image modal");
      this.showImageModal = false;
      this.currentImageModal = { loaiBiaId: null };
      this.imageModalImages = [];
      this.selectedImageIds = [];
      this.newImages = [];
    },
    async handleImageUpload(event) {
      console.log("[INFO] Handling image upload");
      const files = Array.from(event.target.files);
      const maxImages = 3;
      const currentSelected = this.selectedImageIds.length;
      const availableSlots = maxImages - currentSelected;

      if (files.length > availableSlots) {
        console.log(
            "[DEBUG] Too many images selected:",
            files.length,
            "Available slots:",
            availableSlots
        );
        alert(`Chỉ được phép chọn tối đa ${maxImages} ảnh!`);
        files.splice(availableSlots);
      }

      for (const file of files) {
        console.log("[DEBUG] Processing file:", file.name);
        if (!file.type.startsWith("image/")) {
          console.log("[DEBUG] Invalid file type:", file.name);
          alert(`File "${file.name}" không phải là ảnh!`);
          continue;
        }
        if (file.size > 5 * 1024 * 1024) {
          console.log("[DEBUG] File too large:", file.name);
          alert(`File "${file.name}" vượt quá kích thước tối đa 5MB!`);
          continue;
        }

        try {
          const formData = new FormData();
          formData.append("file", file);
          formData.append("idLoaiBia", this.currentImageModal.loaiBiaId);

          console.log("[DEBUG] Uploading image:", file.name);
          const response = await api.post("/anh-san-pham/upload", formData, {
            headers: { "Content-Type": "multipart/form-data" },
          });

          const newImage = {
            id: response.data.id,
            url: response.data.url,
            isNew: true,
          };

          if (
              this.imageModalImages.some(
                  (img) => img.url === newImage.url && !img.isNew
              )
          ) {
            console.log("[DEBUG] Image already exists:", file.name);
            alert(`Ảnh "${file.name}" đã tồn tại trong danh sách!`);
            continue;
          }

          console.log("[DEBUG] Added new image to modal:", newImage);
          this.imageModalImages.push(newImage);
          this.newImages.push(newImage);
          this.selectedImageIds.push(newImage.id);
          this.imageCache[newImage.id] = newImage.url;
        } catch (error) {
          console.error("[ERROR] Error uploading image:", file.name, error);
          alert(
              `Có lỗi xảy ra khi tải ảnh "${file.name}": ${
                  error.response?.data?.message || error.message
              }`
          );
        }
      }
      event.target.value = "";
      console.log("[INFO] Finished handling image upload");
    },
    removeNewImage(index) {
      const image = this.imageModalImages[index];
      console.log("[INFO] Removing new image from modal:", image);
      if (image.isNew) {
        this.imageModalImages.splice(index, 1);
        this.newImages = this.newImages.filter((img) => img.id !== image.id);
        this.selectedImageIds = this.selectedImageIds.filter(
            (id) => id !== image.id
        );
        delete this.imageCache[image.id];
        console.log("[DEBUG] Updated image lists after removal");
      }
    },
    removeImage(idLoaiBia, imageId) {
      console.log("[INFO] Removing image from idLoaiBia:", idLoaiBia, imageId);
      this.loaiBiaImageIds[idLoaiBia] = (
          this.loaiBiaImageIds[idLoaiBia] || []
      ).filter((id) => id !== imageId);
      this.chiTietSanPhamList.forEach((chiTiet) => {
        if (chiTiet.idLoaiBia === idLoaiBia) {
          chiTiet.imageIds = this.loaiBiaImageIds[idLoaiBia];
        }
      });
      console.log(
          "[DEBUG] Updated loaiBiaImageIds and chiTietSanPhamList:",
          this.loaiBiaImageIds,
          this.chiTietSanPhamList
      );
    },
    saveImageSelection() {
      console.log("[INFO] Saving image selection for idLoaiBia:", this.currentImageModal.loaiBiaId);
      if (this.selectedImageIds.length > 3) {
        console.log("[DEBUG] Too many images selected:", this.selectedImageIds.length);
        alert("Chỉ được phép chọn tối đa 3 ảnh!");
        return;
      }

      this.loaiBiaImageIds[this.currentImageModal.loaiBiaId] = [
        ...this.selectedImageIds,
      ];
      this.chiTietSanPhamList.forEach((chiTiet) => {
        if (chiTiet.idLoaiBia === this.currentImageModal.loaiBiaId) {
          chiTiet.imageIds = [...this.selectedImageIds];
        }
      });
      console.log(
          "[DEBUG] Updated loaiBiaImageIds and chiTietSanPhamList with imageIds:",
          this.loaiBiaImageIds,
          this.chiTietSanPhamList
      );

      this.closeImageModal();
    },
    getImageUrl(imageId) {
      const url =
          this.imageCache[imageId] || "https://via.placeholder.com/50";
      console.log("[DEBUG] Retrieved image URL for imageId:", imageId, url);
      return url;
    },
    async saveSanPhamAndChiTiet() {
      console.log("[INFO] Saving sanPham and chiTiet");
      try {
        if (!this.formData.tenSanPham) {
          console.log("[DEBUG] Missing tenSanPham");
          alert("Vui lòng nhập tên sản phẩm!");
          return;
        }
        if (this.chiTietSanPhamList.length === 0) {
          console.log("[DEBUG] No chiTietSanPham provided");
          alert("Vui lòng chọn ít nhất một loại bìa và chất liệu!");
          return;
        }

        const formDataToSend = new FormData();
        formDataToSend.append("tenSanPham", this.formData.tenSanPham);
        formDataToSend.append("moTaSanPham", this.formData.moTaSanPham || "");
        formDataToSend.append("idTacGia", this.formData.idTacGia || "");
        formDataToSend.append(
            "idNhaXuatBan",
            this.formData.idNhaXuatBan || ""
        );
        formDataToSend.append("idNguoiDich", this.formData.idNguoiDich || "");
        formDataToSend.append("idTheLoai", this.formData.idTheLoai || "");
        formDataToSend.append("idNgonNgu", this.formData.idNgonNgu || "");
        formDataToSend.append("moTaChiTiet", this.formData.moTaSanPham || "");

        const chiTietSanPhamList = this.chiTietSanPhamList.map((ctsp) => ({
          tenChiTietSanPham: ctsp.tenChiTietSanPham,
          idLoaiBia: ctsp.idLoaiBia,
          idChatLieu: ctsp.idChatLieu,
          gia: ctsp.gia,
          soLuongTon: ctsp.soLuongTon,
          trongLuong: ctsp.trongLuong,
          kichThuoc: ctsp.kichThuoc,
          imageIds: ctsp.imageIds || [],
        }));
        formDataToSend.append(
            "chiTietSanPhamList",
            JSON.stringify(chiTietSanPhamList)
        );

        console.log("[DEBUG] Form data to send:", {
          tenSanPham: this.formData.tenSanPham,
          chiTietSanPhamList,
        });

        this.loading = true;
        const response = await api.post(
            "/san-pham/create-with-details",
            formDataToSend,
            {
              headers: { "Content-Type": "multipart/form-data" },
              timeout: 60000,
            }
        );

        console.log("[INFO] Successfully saved sanPham:", response.data);
        alert("Thêm sách và chi tiết thành công!");
        this.goBack();
      } catch (error) {
        console.error("[ERROR] Error saving sanPham:", error);
        alert(
            "Có lỗi xảy ra khi lưu: " +
            (error.response?.data?.message || error.message)
        );
      } finally {
        this.loading = false;
        console.log("[INFO] Finished saving sanPham");
      }
    },
    goBack() {
      console.log("[INFO] Navigating back to /admin/sach");
      this.$router.push("/admin/sach");
    },
  },
};
</script>
<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap");

.font-roboto {
  font-family: "Roboto", sans-serif;
}

table {
  table-layout: fixed;
}

th,
td {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>