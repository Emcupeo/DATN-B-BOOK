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
          </div>
        </div>

        <!-- Bảng chi tiết sản phẩm -->
        <div class="mt-6" v-if="formData.idChatLieuList.length > 0 && formData.idLoaiBiaList.length > 0">
          <h4 class="text-md font-semibold text-gray-700 mb-4">Danh sách chi tiết sản phẩm</h4>
          <div class="grid grid-cols-1 gap-6">
            <div v-for="idChatLieu in formData.idChatLieuList" :key="idChatLieu" class="mb-6">
              <div class="flex justify-between items-center mb-2">
                <h5 class="font-medium text-gray-700">{{ getChatLieuName(idChatLieu) }}</h5>
                <button
                    type="button"
                    @click="openBulkEditModal(idChatLieu)"
                    class="text-white bg-gradient-to-r from-yellow-500 via-yellow-600 to-yellow-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-yellow-300 shadow-lg shadow-yellow-500/50 font-medium rounded-lg text-sm px-3 py-1 text-center"
                >
                  Sửa chung
                </button>
              </div>
              <div class="relative overflow-x-auto sm:rounded-lg">
                <table class="w-full text-sm text-center text-gray-500">
                  <thead class="text-xs text-gray-700 uppercase bg-gray-50">
                  <tr>
                    <th class="px-6 py-3">
                      <input
                          type="checkbox"
                          :checked="isAllSelected(idChatLieu)"
                          @change="toggleSelectAll(idChatLieu, $event.target.checked)"
                      />
                    </th>
                    <th class="px-6 py-3">STT</th>
                    <th class="px-6 py-3">Tên chi tiết sản phẩm</th>
                    <th class="px-6 py-3">Số lượng</th>
                    <th class="px-6 py-3">Giá bán</th>
                    <th class="px-6 py-3">Trọng lượng (gram)</th>
                    <th class="px-6 py-3">Kích thước (cm)</th>
                    <th class="px-6 py-3">Hành động</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="(chiTiet, index) in getChiTietByChatLieu(idChatLieu)" :key="index" class="bg-white border-b hover:bg-gray-50">
                    <td class="px-6 py-4">
                      <input type="checkbox" v-model="chiTiet.selected" />
                    </td>
                    <td class="px-6 py-4">{{ index + 1 }}</td>
                    <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">{{ chiTiet.tenChiTietSanPham }}</td>
                    <td class="px-6 py-4">
                      <input
                          v-model.number="chiTiet.soLuongTon"
                          type="number"
                          min="1"
                          class="block w-20 h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
                      />
                    </td>
                    <td class="px-6 py-4">
                      <input
                          v-model.number="chiTiet.gia"
                          type="number"
                          step="0.01"
                          class="block w-32 h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
                      />
                    </td>
                    <td class="px-6 py-4">
                      <input
                          v-model.number="chiTiet.trongLuong"
                          type="number"
                          step="0.01"
                          class="block w-32 h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
                      />
                    </td>
                    <td class="px-6 py-4">
                      <input
                          v-model.number="chiTiet.kichThuoc"
                          type="number"
                          step="0.01"
                          class="block w-32 h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
                      />
                    </td>
                    <td class="px-6 py-4 flex justify-center items-center" style="margin-top: 11px">
                      <a href="#" @click.prevent="confirmRemoveChiTiet(idChatLieu, chiTiet.idLoaiBia)" class="text-red-600 hover:text-red-800">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                          <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
                        </svg>
                      </a>
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
          <h3 class="text-lg font-semibold mb-4">Sửa chung cho {{ getChatLieuName(currentChatLieuId) }}</h3>
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
      modalType: '',
      tempLoaiBiaList: [],
      tempChatLieuList: [],
      showAddModal: false,
      addModalType: '',
      newItemName: '',
      showBulkEditModal: false,
      currentChatLieuId: null,
      bulkEditData: {
        soLuongTon: null,
        gia: null,
        trongLuong: null,
        kichThuoc: null,
      },
    };
  },
  async created() {
    await this.loadInitialData();
  },
  watch: {
    'formData.idLoaiBiaList': 'generateChiTietSanPham',
    'formData.idChatLieuList': 'generateChiTietSanPham',
  },
  methods: {
    async loadInitialData() {
      try {
        this.loading = true;
        this.loaiBiaList = await LoaiBiaService.getAll();
        this.tacGiaList = await TacGiaService.getAll();
        this.nhaXuatBanList = await NhaXuatBanService.getAll();
        this.chatLieuList = await ChatLieuService.getAll();
        this.nguoiDichList = await NguoiDichService.getAll();
        this.theLoaiList = await TheLoaiService.getAll();
        this.ngonNguList = await NgonNguService.getAll();
        this.sanPhamList = await api.get('/san-pham').then(res => res.data);

        if (this.tacGiaList.length > 0) this.formData.idTacGia = this.tacGiaList[0].id;
        if (this.nhaXuatBanList.length > 0) this.formData.idNhaXuatBan = this.nhaXuatBanList[0].id;
        if (this.nguoiDichList.length > 0) this.formData.idNguoiDich = this.nguoiDichList[0].id;
        if (this.theLoaiList.length > 0) this.formData.idTheLoai = this.theLoaiList[0].id;
        if (this.ngonNguList.length > 0) this.formData.idNgonNgu = this.ngonNguList[0].id;
      } catch (error) {
        console.error("Error loading initial data:", error);
        this.error = "Có lỗi xảy ra khi tải dữ liệu";
      } finally {
        this.loading = false;
      }
    },
    selectSanPham(tenSanPham) {
      this.formData.tenSanPham = tenSanPham;
      this.showSanPhamList = false;
    },
    delayHideSanPhamList() {
      setTimeout(() => {
        this.showSanPhamList = false;
      }, 200);
    },
    generateChiTietSanPham() {
      this.chiTietSanPhamList = [];
      const { idLoaiBiaList, idChatLieuList, tenSanPham } = this.formData;

      if (idLoaiBiaList.length > 0 && idChatLieuList.length > 0) {
        idChatLieuList.forEach(idChatLieu => {
          idLoaiBiaList.forEach(idLoaiBia => {
            this.chiTietSanPhamList.push({
              tenChiTietSanPham: `${tenSanPham} (${this.getLoaiBiaName(idLoaiBia)} - ${this.getChatLieuName(idChatLieu)})`,
              idLoaiBia,
              idChatLieu,
              soLuongTon: 1,
              gia: 50000,
              trongLuong: 200,
              kichThuoc: 20,
              selected: false,
            });
          });
        });
      }
    },
    getChiTietByChatLieu(idChatLieu) {
      return this.chiTietSanPhamList.filter(chiTiet => chiTiet.idChatLieu === idChatLieu);
    },
    confirmRemoveChiTiet(idChatLieu, idLoaiBia) {
      if (confirm('Bạn có chắc chắn muốn xóa chi tiết sản phẩm này?')) {
        this.removeChiTiet(idChatLieu, idLoaiBia);
      }
    },
    removeChiTiet(idChatLieu, idLoaiBia) {
      this.chiTietSanPhamList = this.chiTietSanPhamList.filter(
          chiTiet => !(chiTiet.idChatLieu === idChatLieu && chiTiet.idLoaiBia === idLoaiBia)
      );
    },
    isAllSelected(idChatLieu) {
      const chiTietList = this.getChiTietByChatLieu(idChatLieu);
      return chiTietList.length > 0 && chiTietList.every(chiTiet => chiTiet.selected);
    },
    toggleSelectAll(idChatLieu, checked) {
      this.chiTietSanPhamList.forEach(chiTiet => {
        if (chiTiet.idChatLieu === idChatLieu) {
          chiTiet.selected = checked;
        }
      });
    },
    openBulkEditModal(idChatLieu) {
      const chiTietList = this.getChiTietByChatLieu(idChatLieu);
      const hasSelected = chiTietList.some(chiTiet => chiTiet.selected);
      if (!hasSelected) {
        alert("Vui lòng chọn ít nhất một sản phẩm trước!");
        return;
      }
      this.currentChatLieuId = idChatLieu;
      this.bulkEditData = { soLuongTon: null, gia: null, trongLuong: null, kichThuoc: null };
      this.showBulkEditModal = true;
    },
    closeBulkEditModal() {
      this.showBulkEditModal = false;
      this.currentChatLieuId = null;
    },
    saveBulkEdit() {
      this.chiTietSanPhamList.forEach(chiTiet => {
        if (chiTiet.idChatLieu === this.currentChatLieuId && chiTiet.selected) {
          if (this.bulkEditData.soLuongTon !== null) chiTiet.soLuongTon = this.bulkEditData.soLuongTon;
          if (this.bulkEditData.gia !== null) chiTiet.gia = this.bulkEditData.gia;
          if (this.bulkEditData.trongLuong !== null) chiTiet.trongLuong = this.bulkEditData.trongLuong;
          if (this.bulkEditData.kichThuoc !== null) chiTiet.kichThuoc = this.bulkEditData.kichThuoc;
        }
      });
      this.closeBulkEditModal();
    },
    getLoaiBiaName(id) {
      const loaiBia = this.loaiBiaList.find(item => item.id === id);
      return loaiBia ? loaiBia.tenLoaiBia : 'N/A';
    },
    getChatLieuName(id) {
      const chatLieu = this.chatLieuList.find(item => item.id === id);
      return chatLieu ? chatLieu.tenChatLieu : 'N/A';
    },
    formatCurrency(value) {
      return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
    },
    openModal(type) {
      this.modalType = type;
      this.showModal = true;
      this.newItemName = '';
      if (type === 'loaiBia') {
        this.tempLoaiBiaList = [...this.formData.idLoaiBiaList];
      } else {
        this.tempChatLieuList = [...this.formData.idChatLieuList];
      }
    },
    closeModal() {
      this.showModal = false;
      this.modalType = '';
      this.newItemName = '';
    },
    saveModalSelection() {
      if (this.modalType === 'loaiBia') {
        this.formData.idLoaiBiaList = [...this.tempLoaiBiaList];
      } else {
        this.formData.idChatLieuList = [...this.tempChatLieuList];
      }
      this.closeModal();
    },
    openAddModal(type) {
      this.addModalType = type;
      this.showAddModal = true;
      this.newItemName = '';
    },
    closeAddModal() {
      this.showAddModal = false;
      this.addModalType = '';
      this.newItemName = '';
    },
    async addNewItem() {
      if (!this.newItemName) {
        alert("Vui lòng nhập tên!");
        return;
      }
      try {
        let newItem;
        if (this.modalType === 'loaiBia') {
          newItem = await LoaiBiaService.create({ tenLoaiBia: this.newItemName });
          this.loaiBiaList.push(newItem);
          this.tempLoaiBiaList.push(newItem.id);
        } else if (this.modalType === 'chatLieu') {
          newItem = await ChatLieuService.create({ tenChatLieu: this.newItemName });
          this.chatLieuList.push(newItem);
          this.tempChatLieuList.push(newItem.id);
        }
        this.newItemName = '';
        alert(`Đã thêm ${this.modalType === 'loaiBia' ? 'loại bìa' : 'chất liệu'} thành công!`);
      } catch (error) {
        console.error("Error adding new item:", error);
        alert("Có lỗi xảy ra khi thêm!");
      }
    },
    async saveNewItem() {
      if (!this.newItemName) {
        alert("Vui lòng nhập tên!");
        return;
      }
      try {
        let newItem;
        switch (this.addModalType) {
          case 'tacGia':
            newItem = await TacGiaService.create({ tenTacGia: this.newItemName });
            this.tacGiaList.push(newItem);
            this.formData.idTacGia = newItem.id;
            break;
          case 'nhaXuatBan':
            newItem = await NhaXuatBanService.create({ tenNhaXuatBan: this.newItemName });
            this.nhaXuatBanList.push(newItem);
            this.formData.idNhaXuatBan = newItem.id;
            break;
          case 'nguoiDich':
            newItem = await NguoiDichService.create({ tenNguoiDich: this.newItemName });
            this.nguoiDichList.push(newItem);
            this.formData.idNguoiDich = newItem.id;
            break;
          case 'theLoai':
            newItem = await TheLoaiService.create({ tenTheLoai: this.newItemName });
            this.theLoaiList.push(newItem);
            this.formData.idTheLoai = newItem.id;
            break;
          case 'ngonNgu':
            newItem = await NgonNguService.create({ tenNgonNgu: this.newItemName });
            this.ngonNguList.push(newItem);
            this.formData.idNgonNgu = newItem.id;
            break;
        }
        this.closeAddModal();
        alert(`Đã thêm ${this.addModalType} thành công!`);
      } catch (error) {
        console.error("Error saving new item:", error);
        alert("Có lỗi xảy ra khi thêm!");
      }
    },
    async saveSanPhamAndChiTiet() {
      try {
        const payload = {
          tenSanPham: this.formData.tenSanPham,
          moTaSanPham: this.formData.moTaSanPham,
          idTacGia: this.formData.idTacGia || null,
          idNhaXuatBan: this.formData.idNhaXuatBan || null,
          idNguoiDich: this.formData.idNguoiDich || null,
          idTheLoai: this.formData.idTheLoai || null,
          idNgonNgu: this.formData.idNgonNgu || null,
          moTaChiTiet: this.formData.moTaSanPham,
          chiTietSanPhamList: this.chiTietSanPhamList.map(ctsp => ({
            tenChiTietSanPham: ctsp.tenChiTietSanPham,
            idLoaiBia: ctsp.idLoaiBia,
            idChatLieu: ctsp.idChatLieu,
            gia: ctsp.gia,
            soLuongTon: ctsp.soLuongTon,
            trongLuong: ctsp.trongLuong,
            kichThuoc: ctsp.kichThuoc,
          })),
        };
        await api.post('/san-pham/create-with-details', payload);
        alert("Thêm sách và chi tiết thành công!");
        this.goBack();
      } catch (error) {
        console.error("Error saving:", error);
        alert("Có lỗi xảy ra khi lưu");
      }
    },
    goBack() {
      this.$router.push("/admin/sach");
    },
  },
};
</script>
<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');
.font-roboto {
  font-family: 'Roboto', sans-serif;
}
</style>