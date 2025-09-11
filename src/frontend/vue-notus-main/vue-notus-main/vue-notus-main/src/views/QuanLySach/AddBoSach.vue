<template>
  <div class="min-h-screen w-full bg-gray-100 font-roboto">
    <div class="bg-white p-4 shadow rounded w-full">
      <h2 class="text-3xl font-bold mb-8 text-gray-800 text-center">
        {{ isEdit ? 'Sửa Bộ Sách' : 'Thêm Bộ Sách' }}
      </h2>
      <!-- Upload ảnh bộ sách (custom) -->

      <div class="flex flex-row gap-8 items-start">
        <!-- Form nhập thông tin bộ sách -->
        <form @submit.prevent="handleSubmit" class="w-1/2 space-y-6">
          <div class="grid grid-cols-2 gap-6 mb-6">
            <div class="col-span-2 sm:col-span-1">
              <label for="tenBoSach" class="block mb-2 text-sm font-medium text-gray-700">Tên bộ sách <span class="text-red-500">*</span></label>
              <input
                  v-model="form.tenBoSach"
                  id="tenBoSach"
                  type="text"
                  class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-3"
                  placeholder="Nhập tên bộ sách"
                  required
              />
              <p v-if="errors.tenBoSach" class="text-red-500 text-xs mt-1">{{ errors.tenBoSach }}</p>
            </div>
            <div class="col-span-2 sm:col-span-1">
              <label for="giaTien" class="block mb-2 text-sm font-medium text-gray-700">Giá tiền <span class="text-red-500">*</span></label>
              <input
                  v-model.number="form.giaTien"
                  @input="giaTuDong = false"
                  id="giaTien"
                  type="number"
                  min="0"
                  step="1000"
                  :placeholder="isEdit && editBoSachData ? 'Giá hiện có: ' + editBoSachData.giaTien.toLocaleString('vi-VN') + ' VNĐ' : placeholderGiaTien()"
                  class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-3"
                  required
              />
              <p v-if="errors.giaTien" class="text-red-500 text-xs mt-1">{{ errors.giaTien }}</p>
            </div>
            <div class="col-span-2 sm:col-span-1">
              <label for="soLuong" class="block mb-2 text-sm font-medium text-gray-700">Số lượng <span class="text-red-500">*</span></label>
              <input
                  v-model.number="form.soLuong"
                  id="soLuong"
                  type="number"
                  :min="1"
                  :max="minChiTietSoLuongTon"
                  class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-3"
                  required
                  @input="validateBoSachSoLuong"
              />
              <p v-if="errors.soLuong" class="text-red-500 text-xs mt-1">{{ errors.soLuong }}</p>
            </div>
            <div class="col-span-2">
              <label for="moTa" class="block mb-2 text-sm font-medium text-gray-700">Mô tả</label>
              <textarea
                  v-model="form.moTa"
                  id="moTa"
                  rows="6"
                  class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-3"
                  placeholder="Nhập mô tả bộ sách"
              ></textarea>
              <p v-if="errors.moTa" class="text-red-500 text-xs mt-1">{{ errors.moTa }}</p>
            </div>
          </div>

          <!-- Danh sách chi tiết sách đã chọn -->

        </form>

        <!-- Bảng chọn chi tiết sản phẩm -->
        <div class="w-1/2">
          <label class="block mb-3 text-sm font-medium text-gray-700">
            Chọn chi tiết sách áp dụng
          </label>
          <div v-if="isEdit && form.chiTietSanPhams.length > 0" class="mb-3 p-3 bg-blue-50 border border-blue-200 rounded-lg">
            <p class="text-blue-700 text-sm">
              <i class="fas fa-info-circle mr-1"></i>
              Đã chọn sẵn {{ form.chiTietSanPhams.length }} chi tiết sản phẩm từ bộ sách hiện có
            </p>
          </div>
          <div class="mb-4">
            <input
                v-model="searchQuery"
                type="text"
                placeholder="Tìm theo mã hoặc tên sách..."
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-3"
            />
          </div>
          <div class="max-h-[600px] overflow-y-auto border border-gray-300 rounded-lg p-4">
            <table class="w-full text-sm text-center">
              <thead class="text-sm text-black font-semibold bg-white sticky top-0">
              <tr>
                <th class="px-6 py-3">
                  <input
                      type="checkbox"
                      :checked="selectAll"
                      @change="toggleSelectAll"
                      class="w-5 h-5 text-blue-600 bg-white border-gray-300 focus:ring-blue-500"
                  />
                </th>
                <th class="px-6 py-3">Mã</th>
                <th class="px-6 py-3">Tên</th>
                <th class="px-6 py-3">Ngôn ngữ</th>
                <th class="px-6 py-3">Giá tiền</th>
                <th class="px-6 py-3">Số lượng tồn</th>
              </tr>
              </thead>
              <tbody>
              <tr
                  v-for="item in paginatedChiTietSanPhams"
                  :key="item.id"
                  class="hover:bg-gray-100 cursor-pointer text-gray-800"
                  @click="toggleChiTietSelection(item)"
              >
                <td class="px-6 py-3">
                  <input
                      type="checkbox"
                      :checked="isItemSelected(item)"
                      @click.stop
                      @change="toggleChiTietSelection(item)"
                      class="w-5 h-5 text-blue-600 bg-white border-gray-300 focus:ring-blue-500"
                  />
                </td>
                <td class="px-6 py-3">{{ item.maChiTietSanPham }}</td>
                <td class="px-6 py-3">{{ item.tenChiTietSanPham }}</td>
                <td class="px-6 py-3">{{ item.idNgonNgu?.tenNgonNgu || 'N/A' }}</td>
                <td class="px-6 py-3">{{ item.gia ? item.gia.toLocaleString('vi-VN') + ' VNĐ' : 'N/A' }}</td>
                <td class="px-6 py-3">{{ item.soLuongTon }}</td>
              </tr>
              </tbody>
            </table>
          </div>
          <div class="flex justify-center items-center mt-6 space-x-4">
            <button
                @click="prevPage"
                :disabled="currentPage <= 0"
                class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg font-medium rounded-lg text-base p-3 flex items-center justify-center disabled:opacity-50"
            >
              <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke-width="8"
                  stroke="currentColor"
                  class="w-2 h-2"
              >
                <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="M15.75 19.5 8.25 12l7.5-7.5"
                />
              </svg>
            </button>
            <span class="text-base font-semibold text-gray-700">Trang {{ currentPage + 1 }} / {{ totalPages }}</span>
            <button
                @click="nextPage"
                :disabled="currentPage >= totalPages - 1"
                class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg font-medium rounded-lg text-base p-3 flex items-center justify-center disabled:opacity-50"
            >
              <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke-width="8"
                  stroke="currentColor"
                  class="w-2 h-2"
              >
                <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="m8.25 4.5 7.5 7.5-7.5 7.5"
                />
              </svg>
            </button>
          </div>
        </div>
      </div>
      <!-- Upload ảnh bộ sách (di chuyển xuống dưới) -->

      <!-- Danh sách chi tiết sách đã chọn (kéo dài, ở dưới cùng) -->
      <div v-if="form.chiTietSanPhams.length" class="mt-12 w-4/5 mx-auto flex flex-col md:flex-row gap-8 items-start">
        <div class="flex-1">
          <h3 class="text-sm font-medium text-gray-700 mb-2">Danh sách chi tiết sách đã chọn</h3>
          <div class="border border-gray-300 rounded-lg p-4">
            <table class="w-full text-sm text-center">
                <thead class="text-sm text-gray-700 font-semibold bg-white">
                <tr>
                  <th class="px-6 py-3">Mã</th>
                  <th class="px-6 py-3">Tên</th>
                  <th class="px-6 py-3">Ngôn ngữ</th>
                  <th class="px-6 py-3">Giá tiền</th>
                  <th class="px-6 py-3">Số lượng</th>
                  <th class="px-6 py-3">Hành động</th>
                </tr>
                </thead>
                <tbody>
                <tr
                    v-for="(item, index) in form.chiTietSanPhams"
                    :key="item.id"
                    class="hover:bg-gray-100"
                >
                  <td class="px-6 py-3">{{ item.maChiTietSanPham }}</td>
                  <td class="px-6 py-3">{{ item.tenChiTietSanPham }}</td>
                  <td class="px-6 py-3">{{ item.idNgonNgu?.tenNgonNgu || 'N/A' }}</td>
                  <td class="px-6 py-3">{{ item.gia ? item.gia.toLocaleString('vi-VN') + ' VNĐ' : 'N/A' }}</td>
                  <td class="px-6 py-3">
                    <input
                        v-model.number="item.soLuong"
                        type="number"
                        :min="1"
                        :max="Math.min(item.soLuongTon, form.soLuong)"
                        class="w-20 p-2 border border-gray-300 rounded-lg text-center"
                        @input="validateChiTietSoLuong(item, index)"
                    />
                    <p v-if="errors.chiTietSanPhams[index]" class="text-red-500 text-xs mt-1">
                      {{ errors.chiTietSanPhams[index] }}
                    </p>
                  </td>
                  <td class="px-6 py-3">
                    <button
                        type="button"
                        @click="removeChiTietSanPham(index)"
                        class="text-red-600 hover:text-red-800"
                    >
                      <svg
                          class="w-5 h-5"
                          fill="none"
                          stroke="currentColor"
                          viewBox="0 0 24 24"
                      >
                        <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M6 18L18 6M6 6l12 12"
                        />
                      </svg>
                    </button>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
            <p v-if="errors.chiTietSanPhams.length && !form.chiTietSanPhams.length" class="text-red-500 text-sm mt-2">
              Vui lòng chọn ít nhất một chi tiết sách
            </p>
          </div>
        <div class="flex flex-col items-center md:mx-0 md:w-[320px] w-full">
          <h3 class="text-sm font-medium text-gray-700 mb-2">Chọn ảnh</h3>
          <div v-if="isEdit && editBoSachData && editBoSachData.url" class="mb-3 p-3 bg-green-50 border border-green-200 rounded-lg w-full">
            <p class="text-green-700 text-sm mb-2">
              <i class="fas fa-image mr-1"></i>
              Ảnh hiện có:
            </p>
            <img :src="editBoSachData.url" alt="Ảnh hiện có" class="w-full h-32 object-cover rounded-lg">
          </div>
          <div class="image-upload-container w-full">
            <label for="imageInput" class="upload-box">
              <div v-if="imagePreview" class="image-preview">
                <img :src="imagePreview" alt="Preview">
              </div>
              <div v-else id="uploadPlaceholder">
                <i class="fas fa-upload text-3xl text-gray-400 mb-2"></i>
                <p>Click để chọn ảnh hoặc kéo thả</p>
                <p>Hỗ trợ: JPG, PNG, GIF (≤ 5MB)</p>
              </div>
            </label>
            <input type="file" id="imageInput" accept="image/*" @change="onImageChange" ref="imageInput" hidden>
          </div>
        </div>
      </div>
      <!-- Nút hành động dưới cùng bên phải -->
      <div class="flex justify-end space-x-3 mt-10 w-full">
        <button type="button" @click="cancel" class="px-6 py-2 bg-gray-300 text-gray-700 rounded-lg hover:bg-gray-400 focus:outline-none">
          Hủy
        </button>
        <button type="button" @click="handleSubmit" class="px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 focus:outline-none flex items-center gap-2">
          <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z" clip-rule="evenodd"></path>
          </svg>
          {{ isEdit ? 'Cập nhật bộ sách' : 'Thêm bộ sách' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import BoSachService from '@/service/BoSachService';
import { useRouter } from 'vue-router';
import ImageKitService from '@/service/ImageKitService';

export default {
  name: 'AddBoSach',
  setup() {
    const router = useRouter();
    return { router };
  },
  data() {
    return {
      form: {
        tenBoSach: '',
        giaTien: 0,
        moTa: '',
        soLuong: 0,
        chiTietSanPhams: [],
      },
      giaTuDong: true,
      errors: {
        tenBoSach: '',
        giaTien: '',
        moTa: '',
        soLuong: '',
        chiTietSanPhams: [],
      },
      chiTietSanPhams: [],
      selectedChiTietSanPhams: [],
      selectAll: false,
      searchQuery: '',
      currentPage: 0,
      itemsPerPage: 6,
      isEdit: false,
      editBoSachData: null,
      uploading: false,
      imageFile: null,
      imagePreview: '',
    };
  },
  computed: {
    filteredChiTietSanPhams() {
      if (!this.searchQuery) return this.chiTietSanPhams;
      const query = this.searchQuery.toLowerCase().trim();
      return this.chiTietSanPhams.filter(item => {
        return (
            item.maChiTietSanPham.toLowerCase().includes(query) ||
            item.tenChiTietSanPham.toLowerCase().includes(query)
        );
      });
    },
    totalGiaTien() {
      return this.form.chiTietSanPhams.reduce((total, item) => {
        return total + (item.gia || 0) * (item.soLuong || 1);
      }, 0);
    },
    totalPages() {
      return Math.ceil(this.filteredChiTietSanPhams.length / this.itemsPerPage) || 1;
    },
    paginatedChiTietSanPhams() {
      const start = this.currentPage * this.itemsPerPage;
      return this.filteredChiTietSanPhams.slice(start, start + this.itemsPerPage);
    },
    minChiTietSoLuongTon() {
      if (!this.form.chiTietSanPhams.length) return 1;
      return Math.min(...this.form.chiTietSanPhams.map(item => item.soLuongTon || 0));
    },
  },
  async created() {
    await this.loadChiTietSanPhams();
    
    // Check if this is edit mode
    const id = this.$route.params.id;
    if (id) {
      this.isEdit = true;
      await this.loadBoSachForEdit();
    }
  },
  watch: {
    form: {
      handler(newForm) {
        if (this.giaTuDong) {
          newForm.giaTien = this.totalGiaTien;
        }
      },
      deep: true,
    },
    searchQuery() {
      this.currentPage = 0;
    },
    selectedChiTietSanPhams() {
      // Check if all items in current page are selected
      this.selectAll = this.paginatedChiTietSanPhams.length > 0 && 
                      this.paginatedChiTietSanPhams.every(item => this.isItemSelected(item));
    },
  },
  methods: {
    async loadChiTietSanPhams() {
      try {
        this.chiTietSanPhams = await BoSachService.getChiTietSanPhams();
        this.chiTietSanPhams.forEach(item => {
          item.soLuong = 1;
        });
      } catch (error) {
        console.error('Lỗi khi tải danh sách chi tiết sách:', error);
        alert('Không thể tải danh sách chi tiết sách.');
      }
    },
    isItemSelected(item) {
      return this.selectedChiTietSanPhams.some(selected => selected.id === item.id);
    },
    toggleChiTietSelection(item) {
      const index = this.selectedChiTietSanPhams.findIndex(i => i.id === item.id);
      if (index === -1) {
        this.selectedChiTietSanPhams.push(item);
      } else {
        this.selectedChiTietSanPhams.splice(index, 1);
      }
      this.updateSelectedChiTietSanPhams();
    },
    placeholderGiaTien() {
      return this.form.chiTietSanPhams.length
          ? this.form.chiTietSanPhams
          .reduce((total, item) => {
            return total + (item.gia || 0) * (item.soLuong || 1);
          }, 0)
          .toLocaleString('vi-VN') + ' VNĐ'
          : 'Nhập giá tiền';
    },
    toggleSelectAll() {
      this.selectAll = !this.selectAll;
      if (this.selectAll) {
        // Add all items from current page that are not already selected
        this.paginatedChiTietSanPhams.forEach(item => {
          if (!this.isItemSelected(item)) {
            this.selectedChiTietSanPhams.push(item);
          }
        });
      } else {
        // Remove all items from current page
        this.paginatedChiTietSanPhams.forEach(item => {
          const index = this.selectedChiTietSanPhams.findIndex(selected => selected.id === item.id);
          if (index !== -1) {
            this.selectedChiTietSanPhams.splice(index, 1);
          }
        });
      }
      this.updateSelectedChiTietSanPhams();
    },
    updateSelectedChiTietSanPhams() {
      this.form.chiTietSanPhams = this.selectedChiTietSanPhams.map(item => ({
        id: item.id,
        maChiTietSanPham: item.maChiTietSanPham,
        tenChiTietSanPham: item.tenChiTietSanPham,
        idNgonNgu: item.idNgonNgu,
        gia: item.gia,
        soLuongTon: item.soLuongTon,
        soLuong: item.soLuong || 1,
      }));
      this.errors.chiTietSanPhams = new Array(this.form.chiTietSanPhams.length).fill('');
    },
    removeChiTietSanPham(index) {
      const removedItem = this.form.chiTietSanPhams.splice(index, 1)[0];
      this.errors.chiTietSanPhams.splice(index, 1);
      this.selectedChiTietSanPhams = this.selectedChiTietSanPhams.filter(
          item => item.id !== removedItem.id
      );
    },
    validateBoSachSoLuong() {
      // Không cho số lượng bộ sách lớn hơn số lượng tồn nhỏ nhất của các chi tiết
      if (this.form.soLuong > this.minChiTietSoLuongTon) {
        this.errors.soLuong = `Số lượng bộ sách không được vượt quá số lượng tồn nhỏ nhất của các chi tiết (${this.minChiTietSoLuongTon})`;
      } else if (this.form.soLuong < 1) {
        this.errors.soLuong = 'Số lượng bộ sách phải lớn hơn 0';
      } else {
        this.errors.soLuong = '';
      }
      // Kiểm tra lại số lượng từng chi tiết
      this.form.chiTietSanPhams.forEach((item, idx) => {
        this.validateChiTietSoLuong(item, idx);
      });
    },
    validateChiTietSoLuong(item, index) {
      const chiTiet = this.form.chiTietSanPhams.find(selected => selected.id === item.id);
      const chiTietIndex = chiTiet ? this.form.chiTietSanPhams.indexOf(chiTiet) : index;
      if (chiTiet && chiTiet.soLuong < 1) {
        this.errors.chiTietSanPhams[chiTietIndex] = 'Số lượng phải lớn hơn 0';
      } else if (chiTiet && chiTiet.soLuong > chiTiet.soLuongTon) {
        this.errors.chiTietSanPhams[chiTietIndex] = `Số lượng không được vượt quá tồn kho (${chiTiet.soLuongTon})`;
      } else if (chiTiet && chiTiet.soLuong > this.form.soLuong) {
        this.errors.chiTietSanPhams[chiTietIndex] = `Số lượng chi tiết không được vượt quá số lượng bộ sách (${this.form.soLuong})`;
      } else {
        this.errors.chiTietSanPhams[chiTietIndex] = '';
      }
    },
    onImageChange(e) {
      const file = e.target.files[0];
      if (!file) return;
      if (!file.type.startsWith('image/')) {
        alert('Vui lòng chọn file ảnh hợp lệ!');
        return;
      }
      if (file.size > 5 * 1024 * 1024) {
        alert('Ảnh vượt quá dung lượng 5MB!');
        return;
      }
      this.imageFile = file;
      const reader = new FileReader();
      reader.onload = (ev) => {
        this.imagePreview = ev.target.result;
      };
      reader.readAsDataURL(file);
    },
    async handleSubmit() {
      this.errors = {
        tenBoSach: '',
        giaTien: '',
        moTa: '',
        soLuong: '',
        chiTietSanPhams: new Array(this.form.chiTietSanPhams.length).fill(''),
      };
      let hasError = false;
      if (!this.form.tenBoSach.trim()) {
        this.errors.tenBoSach = 'Tên bộ sách không được để trống';
        hasError = true;
      }
      if (this.form.giaTien < 0 || isNaN(this.form.giaTien)) {
        this.errors.giaTien = 'Giá tiền phải là số không âm';
        hasError = true;
      }
      if (this.form.soLuong < 0 || isNaN(this.form.soLuong)) {
        this.errors.soLuong = 'Số lượng phải là số không âm';
        hasError = true;
      }
      if (!this.form.chiTietSanPhams.length) {
        this.errors.chiTietSanPhams = ['Vui lòng chọn ít nhất một chi tiết sách'];
        hasError = true;
      } else {
        this.form.chiTietSanPhams.forEach((item, index) => {
          this.validateChiTietSoLuong(item, index);
          if (this.errors.chiTietSanPhams[index]) hasError = true;
        });
      }
      if (hasError) return;
      // Upload ảnh trước khi gửi payload
      this.uploading = true;
      let imageUrl = '';
      try {
        if (this.imageFile) {
          const result = await ImageKitService.uploadImage(this.imageFile, this.imageFile.name);
          if (result.success) imageUrl = result.url;
          else throw new Error(result.error || 'Lỗi upload ảnh');
        } else if (this.isEdit && this.editBoSachData && this.editBoSachData.url) {
          // Nếu đang edit và không có ảnh mới, sử dụng ảnh hiện có
          imageUrl = this.editBoSachData.url;
        }
      } catch (e) {
        this.uploading = false;
        alert('Lỗi upload ảnh: ' + (e.message || e));
        return;
      }
      this.uploading = false;
      try {
        const payload = {
          tenBoSach: this.form.tenBoSach,
          giaTien: this.form.giaTien,
          moTa: this.form.moTa,
          soLuong: this.form.soLuong,
          url: imageUrl,
          boSachChiTiets: this.form.chiTietSanPhams.map(item => ({
            idChiTietSanPham: parseInt(item.id),
            soLuong: item.soLuong,
          })),
        };
        console.log('Payload gửi đi:', JSON.stringify(payload, null, 2));
        
        if (this.isEdit) {
          await BoSachService.update(this.$route.params.id, payload);
          alert('Cập nhật bộ sách thành công!');
        } else {
          await BoSachService.create(payload);
          alert('Thêm bộ sách thành công!');
        }
        
        await this.router.push('/admin/bo-sach');
      } catch (error) {
        console.error('Lỗi khi thêm bộ sách:', {
          message: error.message,
          response: error.response?.data,
          status: error.response?.status,
        });
        alert(
            error.response?.data?.tenBoSach ||
            'Có lỗi xảy ra khi thêm bộ sách. Vui lòng kiểm tra server logs.'
        );
      }
    },
    cancel() {
      this.router.push('/admin/bo-sach');
    },
    prevPage() {
      if (this.currentPage > 0) this.currentPage--;
    },
    nextPage() {
      if (this.currentPage < this.totalPages - 1) this.currentPage++;
    },
    async loadBoSachForEdit() {
      try {
        const id = this.$route.params.id;
        console.log('[INFO] Loading BoSach for edit with id:', id);
        this.editBoSachData = await BoSachService.getById(id);
        
        // Populate form with existing data
        this.form.tenBoSach = this.editBoSachData.tenBoSach;
        this.form.giaTien = this.editBoSachData.giaTien;
        this.form.moTa = this.editBoSachData.moTa;
        this.form.soLuong = this.editBoSachData.soLuong;
        
        // Set current image as preview if exists
        if (this.editBoSachData.url) {
          this.imagePreview = this.editBoSachData.url;
        }
        
        // Load chi tiết sản phẩm từ API riêng
        console.log('[INFO] Loading chi tiết sản phẩm for BoSach id:', id);
        const chiTietList = await BoSachService.getBoSachChiTietByBoSachId(id);
        console.log('[DEBUG] Loaded chi tiết list:', chiTietList);
        
        if (chiTietList && chiTietList.length > 0) {
          this.form.chiTietSanPhams = chiTietList.map(ctsp => ({
            id: ctsp.idChiTietSanPham.id,
            maChiTietSanPham: ctsp.idChiTietSanPham.maChiTietSanPham,
            tenChiTietSanPham: ctsp.idChiTietSanPham.tenChiTietSanPham,
            gia: ctsp.idChiTietSanPham.gia,
            soLuongTon: ctsp.idChiTietSanPham.soLuongTon,
            soLuong: ctsp.soLuong,
            idNgonNgu: ctsp.idChiTietSanPham.idNgonNgu
          }));
          
          // Clear existing selections first
          this.selectedChiTietSanPhams = [];
          
          // Mark as selected in the main list and update their quantities
          this.chiTietSanPhams.forEach(item => {
            const existingItem = this.form.chiTietSanPhams.find(ctsp => ctsp.id === item.id);
            if (existingItem) {
              // Update the item in the main list with the existing quantity
              item.soLuong = existingItem.soLuong;
              // Create a copy of the item to avoid reference issues
              const selectedItem = {
                ...item,
                soLuong: existingItem.soLuong
              };
              // Add to selected list
              this.selectedChiTietSanPhams.push(selectedItem);
            }
          });
          
          // Update selectAll checkbox state after a short delay to ensure DOM is updated
          this.$nextTick(() => {
            this.selectAll = this.selectedChiTietSanPhams.length === this.paginatedChiTietSanPhams.length;
          });
        }
        
        console.log('[DEBUG] Loaded BoSach data:', this.editBoSachData);
        console.log('[DEBUG] Form data:', this.form);
        console.log('[DEBUG] Selected chi tiết sản phẩm:', this.selectedChiTietSanPhams);
        console.log('[DEBUG] Current giá tiền:', this.form.giaTien);
        console.log('[DEBUG] SelectAll state:', this.selectAll);
        console.log('[DEBUG] Total chi tiết sản phẩm available:', this.chiTietSanPhams.length);
      } catch (error) {
        console.error('[ERROR] Error loading BoSach for edit:', error);
        alert('Không thể tải dữ liệu bộ sách để chỉnh sửa.');
        this.router.push('/admin/bo-sach');
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
.image-upload-container {
  text-align: center;
  border: 2px dashed #ccc;
  border-radius: 12px;
  padding: 20px;
  position: relative;
  width: 100%;
  max-width: 400px;
  margin-top: 10px;
  cursor: pointer;
  transition: border-color 0.3s ease;
}
.image-upload-container:hover {
  border-color: #007bff;
}
.upload-box {
  display: block;
  cursor: pointer;
}
.image-preview {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
}
.image-preview img {
  max-width: 250px;
  max-height: 250px;
  border-radius: 10px;
  object-fit: contain;
}
#uploadPlaceholder {
  color: #888;
}
.hidden {
  display: none;
}
</style>