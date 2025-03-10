<template>
  <div class="relative">
    <div class="relative">
      <!-- Form nhập phiếu giảm giá -->
      <div v-if="showModal" class="fixed inset-0 flex items-center justify-center  backdrop-blur-sm z-50">
        <!-- new form -->
        <div class="relative w-full max-w-md max-h-full rounded-lg shadow-[0_10px_30px_rgba(0,0,0,0.4)]">
          <!-- Modal content -->
          <div class="relative bg-white rounded-lg shadow-sm dark:bg-gray-700">
            <!-- Modal header -->
            <div
              class="flex items-center justify-between p-4 md:p-5 border-b rounded-t dark:border-gray-600 border-gray-200">
              <h3 class="text-lg font-semibold text-gray-900 dark:text-white">
                {{ newPhieu.id ? "Sửa phiếu giảm giá" : "Thêm phiếu giảm giá" }}
              </h3>
              <button @click="closeModal" type="button"
                class="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm w-8 h-8 ms-auto inline-flex justify-center items-center dark:hover:bg-gray-600 dark:hover:text-white"
                data-modal-toggle="crud-modal">
                <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none"
                  viewBox="0 0 14 14">
                  <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6" />
                </svg>
                <span class="sr-only">Đóng</span>
              </button>
            </div>
            <!-- Modal body -->
            <form class="p-4 md:p-5" @submit.prevent="newPhieu.id ? updatePhieuGiamGia() : addPhieuGiamGia()">
              <div class="grid gap-4 mb-4 grid-cols-2">
                <div class="col-span-2 sm:col-span-1">
                  <label for="price" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Phần trăm
                    giảm</label>
                  <input v-model="newPhieu.soPhanTramGiam" type="number" name="price" id="price"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                    placeholder="% giảm" required="">
                </div>

                <div class="col-span-2 sm:col-span-1">
                  <label for="price" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Giá trị
                    giảm</label>
                  <input v-model="newPhieu.giaTriGiam" type="number" name="price" id="price"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                    placeholder="Giá trị giảm" required="">
                </div>


                <div class="col-span-2 sm:col-span-1">
                  <label for="price" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Giá trị đơn
                    giảm tối thiểu</label>
                  <input v-model="newPhieu.giaTriDonHangToiThieu" type="number" name="price" id="price"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                    placeholder="Giá trị đơn hàng tối thiểu" required="">
                </div>

                <div class="col-span-2 sm:col-span-1">
                  <label for="price" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Số
                    lượng</label>
                  <input v-model="newPhieu.soLuong" type="number" name="price" id="price"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                    placeholder="Số lượng" required="">
                </div>

                <div class="col-span-2 sm:col-span-1">
                  <label for="price" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Ngày bắt
                    đầu</label>
                  <input v-model="newPhieu.ngayBatDau" type="date" name="price" id="price"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                    required="">
                </div>

                <div class="col-span-2 sm:col-span-1">
                  <label for="price" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Ngày kết
                    thúc</label>
                  <input v-model="newPhieu.ngayKetThuc" type="date" name="price" id="price"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                    required="">
                </div>

                <div class="col-span-2 sm:col-span-1">
                  <label for="category" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Trạng
                    thái</label>
                  <select v-model="newPhieu.trangThai" id="category"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-500 focus:border-primary-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500">
                    <option selected="">Chọn trạng thái</option>
                    <option value=true>Hoạt động</option>
                    <option value=false>Hết hạn</option>
                  </select>
                </div>

                <div class="col-span-2">
                  <label for="description" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Mô
                    tả</label>
                  <textarea v-model="newPhieu.moTa" id="description" rows="4"
                    class="block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder="Nhập mô tả"></textarea>
                </div>

              </div>
              <button type="submit"
                class="text-white inline-flex items-center bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                <svg class="me-1 -ms-1 w-5 h-5" fill="currentColor" viewBox="0 0 20 20"
                  xmlns="http://www.w3.org/2000/svg">
                  <path fill-rule="evenodd"
                    d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z"
                    clip-rule="evenodd"></path>
                </svg>
                {{ newPhieu.id ? "Cập nhật phiếu giảm giá" : "Thêm phiếu giảm giá" }}
              </button>
            </form>
          </div>
        </div>
      </div>

      <!-- Bảng hiển thị phiếu giảm giá -->

      <div class="bg-white p-4 shadow rounded">
        <div class="flex items-center justify-between mb-4">
          <h2 class="text-lg font-bold mb-4">Danh sách phiếu giảm giá</h2>
          <button @click="showModal = true" type="button"
            class="text-white bg-gradient-to-br from-green-400 to-blue-600 hover:bg-gradient-to-bl focus:ring-4 focus:outline-none focus:ring-green-200 dark:focus:ring-green-800 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2">Thêm
            phiếu giảm giá</button>
        </div>

        <div class="flex items-center">
          <input type="text" placeholder="Tìm kiếm phiếu giảm giá..."
            class="block w-full p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500 mb-3" />
        </div>

        <div class="relative overflow-x-auto sm:rounded-lg">
          <table class="w-full text-sm text-left text-gray-500">
            <thead class="text-xs text-gray-700 uppercase bg-gray-50">
              <tr>
                <th scope="col" class="px-6 py-3">Mã phiếu</th>
                <th scope="col" class="px-6 py-3">Số phần trăm giảm</th>
                <th scope="col" class="px-6 py-3">Giá trị giảm</th>
                <th scope="col" class="px-6 py-3">Giá trị đơn hàng tối thiểu</th>
                <th scope="col" class="px-6 py-3">Mô tả</th>
                <th scope="col" class="px-6 py-3">Số lượng</th>
                <th scope="col" class="px-6 py-3">Trạng thái</th>
                <th scope="col" class="px-6 py-3">Ngày bắt đầu</th>
                <th scope="col" class="px-6 py-3">Ngày kết thúc</th>
                <th scope="col" class="px-6 py-3">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr class="bg-white border-b hover:bg-gray-50" v-for="phieu in phieuGiamGias" :key="phieu.id">
                <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">{{ phieu.maPhieuGiamGia }}</td>
                <td class="px-6 py-4">{{ phieu.soPhanTramGiam }}%</td>
                <td class="px-6 py-4">{{ phieu.giaTriGiam }}đ</td>
                <td class="px-6 py-4">{{ phieu.giaTriDonHangToiThieu }}đ</td>
                <td class="px-6 py-4">{{ phieu.moTa }}</td>
                <td class="px-6 py-4">{{ phieu.soLuong }}</td>
                <td class="px-6 py-4">{{ phieu.trangThai ? 'Hoạt động' : 'Hết hạn' }}</td>
                <td class="px-6 py-4">{{ phieu.ngayBatDau }}</td>
                <td class="px-6 py-4">{{ phieu.ngayKetThuc }}</td>
                <td class="px-6 py-4">
                  <a href="#" class="font-medium text-blue-600 hover:underline" @click="editPhieu(phieu)">Edit</a>
                  <a href="#" class="font-medium text-red-600 hover:underline ml-3"
                    @click="xoaPhieu(phieu.id)">Remove</a>
                </td>
              </tr>
              <tr class="bg-white border-b hover:bg-gray-50" v-if="phieuGiamGias.length == 0">
                <td class="px-6 py-4" colspan="5" align="center">Không có dữ liệu</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import phieuGiamGiaService from '../../service/phieuGiamGiaService';

export default {
  data() {
    return {
      phieuGiamGias: [],
      showModal: false,
      newPhieu: {
        soPhanTramGiam: 0,
        giaTriGiam: 0,
        giaTriDonHangToiThieu: 0,
        moTa: "",
        soLuong: 0,
        trangThai: "true",
        ngayBatDau: "",
        ngayKetThuc: "",
        deleted: 0,
      },


    };
  },
  methods: {
    resetForm() {
      this.newPhieu = {
        id: null,  // Đảm bảo ID được reset khi thêm mới
        soPhanTramGiam: 0,
        giaTriGiam: 0,
        giaTriDonHangToiThieu: 0,
        moTa: "",
        soLuong: 0,
        trangThai: "true",
        ngayBatDau: "",
        ngayKetThuc: "",
      };
    },
    async fetchData() {
      try {
        const response = await phieuGiamGiaService.getAll();
        this.phieuGiamGias = response.data;
      } catch (error) {
        console.error("Lỗi khi tải dữ liệu:", error);
      }
    },
    async deletePhieu(id) {
      if (confirm("Bạn có chắc muốn xóa?")) {
        try {
          await phieuGiamGiaService.delete(id);
          this.fetchData();
        } catch (error) {
          console.error("Lỗi khi xóa:", error);
        }
      }
    },
    editPhieu(phieu) {
      this.newPhieu = { ...phieu };
      this.showModal = true;
    },

    async updatePhieuGiamGia() {
      try {
        await phieuGiamGiaService.update(this.newPhieu.id, this.newPhieu);
        alert("Cập nhật thành công!");

        // Đóng modal
        this.showModal = false;

        // Reset form
        this.newPhieu = {
          id: null,

          soPhanTramGiam: "",
          giaTriGiam: "",
          giaTriDonHangToiThieu: "",
          soLuong: "",
          ngayBatDau: "",
          ngayKetThuc: "",
          trangThai: null,
          moTa: ""
        };

        // Reload danh sách
        this.fetchData();
      } catch (error) {
        console.error("Lỗi khi cập nhật:", error.response?.data || error.message);
        alert("Lỗi khi cập nhật: " + (error.response?.data || error.message));
      }
    },

    closeModal() {
      this.showModal = false;
      this.resetForm(); // Reset form khi đóng modal
    },

    async addPhieuGiamGia() {
      try {
        console.log("Dữ liệu gửi lên:", this.newPhieu); // Kiểm tra dữ liệu trước khi gửi

        await phieuGiamGiaService.create(this.newPhieu);
        alert("Thêm thành công!");

        // Reset form
        this.resetForm();

        // Đóng modal
        this.showModal = false;

        // Reload danh sách
        this.fetchData();
      } catch (error) {
        console.error("Lỗi khi thêm:", error.response?.data || error.message);
        alert("Lỗi khi thêm: " + (error.response?.data || error.message));
      }
    },

    async xoaPhieu(id) {
      const confirmDelete = confirm("Bạn có chắc muốn xóa?");
      if (confirmDelete) {
        try {
          await phieuGiamGiaService.delete(id);
          this.fetchData(); // CẬP NHẬT DANH SÁCH LUÔN
        } catch (error) {
          console.error("Lỗi khi xóa:", error);
        }
      }
    },



    watch: {
      showModal(newVal) {
        if (newVal) {
          document.body.classList.add("overflow-hidden");
        } else {
          document.body.classList.remove("overflow-hidden");
        }
      }
    }


  },
  mounted() {
    this.fetchData();
  },
};
</script>

<style>
.modal {
  z-index: 50;
  /* Đảm bảo modal nằm trên */
  position: fixed;
  inset: 0;
}

table {
  position: relative;
  z-index: 1;
  /* Đảm bảo bảng không đè lên modal */
}
</style>