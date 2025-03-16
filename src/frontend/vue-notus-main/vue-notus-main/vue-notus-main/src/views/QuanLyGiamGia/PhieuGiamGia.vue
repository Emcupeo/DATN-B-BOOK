<template>
  <div class="min-h-screen w-full bg-gray-100 font-roboto">
    <!-- Toast Notification -->
    <transition name="fade">
      <div v-if="showToast"
        class="fixed top-5 right-5 z-50 max-w-sm w-full p-4 rounded-lg shadow-lg text-white flex items-center" :class="{
          'bg-green-500': toastType === 'success',
          'bg-red-500': toastType === 'error',
          'bg-yellow-500': toastType === 'warning'
        }">
        <span class="mr-2">
          <svg v-if="toastType === 'success'" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
          </svg>
          <svg v-if="toastType === 'error'" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
          <svg v-if="toastType === 'warning'" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M12 9v2m0 4h.01M12 3a9 9 0 110 18 9 9 0 010-18z"></path>
          </svg>
        </span>
        {{ toastMessage }}
      </div>
    </transition>

    <!-- Modal xác nhận xóa -->
    <div v-if="showConfirmModal" class="fixed inset-0 flex items-center justify-center bg-gray-800 bg-opacity-50 z-50">
      <div class="relative p-4 w-full max-w-md max-h-full">
        <div class="relative bg-white rounded-lg shadow-sm dark:bg-gray-700">
          <button @click="closeConfirmModal" type="button"
            class="absolute top-3 end-2.5 text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm w-8 h-8 ms-auto inline-flex justify-center items-center dark:hover:bg-gray-600 dark:hover:text-white">
            <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
              <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6" />
            </svg>
            <span class="sr-only">Close modal</span>
          </button>
          <div class="p-4 md:p-5 text-center">
            <svg class="mx-auto mb-4 text-gray-400 w-12 h-12 dark:text-gray-200" aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
              <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M10 11V6m0 8h.01M19 10a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
            </svg>
            <h3 class="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">Bạn có chắc muốn xóa phiếu giảm giá
              này không?</h3>
            <button @click="confirmAction" type="button"
              class="text-white bg-red-600 hover:bg-red-800 focus:ring-4 focus:outline-none focus:ring-red-300 dark:focus:ring-red-800 font-medium rounded-lg text-sm inline-flex items-center px-5 py-2.5 text-center">
              Có, tôi chắc chắn
            </button>
            <button @click="closeConfirmModal" type="button"
              class="py-2.5 px-5 ms-3 text-sm font-medium text-gray-900 focus:outline-none bg-white rounded-lg border border-gray-200 hover:bg-gray-100 hover:text-blue-700 focus:z-10 focus:ring-4 focus:ring-gray-100 dark:focus:ring-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:border-gray-600 dark:hover:text-white dark:hover:bg-gray-700">
              Không, hủy bỏ
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="w-full">
      <!-- Modal chi tiết -->
      <div v-if="showModalDetail" class="fixed inset-0 flex items-center justify-center bg-gray-800 bg-opacity-50 z-50">
        <div class="bg-white p-6 rounded-lg shadow-lg max-w-lg w-full relative">
          <button @click="showModalDetail = false" class="absolute top-3 right-3 text-gray-600 hover:text-red-500">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" viewBox="0 0 24 24" fill="none"
              stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <line x1="18" y1="6" x2="6" y2="18"></line>
              <line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>
          </button>
          <h2 class="text-xl font-bold mb-4 text-center">Chi tiết phiếu giảm giá</h2>
          <table class="w-full">
            <tbody>
              <tr>
                <td class="p-2 font-semibold">ID</td>
                <td class="p-2">{{ selectedPhieu?.id }}</td>
              </tr>
              <tr>
                <td class="p-2 font-semibold">Mô tả</td>
                <td class="p-2">{{ selectedPhieu?.moTa }}</td>
              </tr>
              <tr>
                <td class="p-2 font-semibold">Phần trăm giảm</td>
                <td class="p-2">{{ selectedPhieu?.soPhanTramGiam }}%</td>
              </tr>
              <tr>
                <td class="p-2 font-semibold">Giá trị giảm</td>
                <td class="p-2">{{ selectedPhieu?.giaTriGiam }}</td>
              </tr>
              <tr>
                <td class="p-2 font-semibold">Số lượng</td>
                <td class="p-2">{{ selectedPhieu?.soLuong }}</td>
              </tr>
              <tr>
                <td class="p-2 font-semibold">Ngày bắt đầu</td>
                <td class="p-2">{{ selectedPhieu?.ngayBatDau }}</td>
              </tr>
              <tr>
                <td class="p-2 font-semibold">Ngày kết thúc</td>
                <td class="p-2">{{ selectedPhieu?.ngayKetThuc }}</td>
              </tr>
              <tr>
                <td class="p-2 font-semibold">Trạng thái</td>
                <td class="p-2">
                  <span
                    :class="selectedPhieu?.trangThai ? 'text-green-600 font-semibold' : 'text-red-600 font-semibold'">
                    {{ selectedPhieu?.trangThai ? "Hoạt động" : "Ngừng" }}
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Form nhập phiếu giảm giá -->
      <div v-if="showModal" class="fixed inset-0 flex items-center justify-center backdrop-blur-sm z-50">
        <div class="relative w-full max-w-md max-h-full rounded-lg shadow-[0_10px_30px_rgba(0,0,0,0.4)]">
          <div class="relative bg-white rounded-lg shadow-sm dark:bg-gray-700">
            <div
              class="flex items-center justify-between p-4 md:p-5 border-b rounded-t dark:border-gray-600 border-gray-200">
              <h3 class="text-lg font-semibold text-gray-900 dark:text-white">
                {{ newPhieu.id ? "Sửa phiếu giảm giá" : "Thêm phiếu giảm giá" }}
              </h3>
              <button @click="closeModal" type="button"
                class="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm w-8 h-8 ms-auto inline-flex justify-center items-center dark:hover:bg-gray-600 dark:hover:text-white">
                <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none"
                  viewBox="0 0 14 14">
                  <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6" />
                </svg>
                <span class="sr-only">Đóng</span>
              </button>
            </div>
            <form class="p-4 md:p-5" @submit.prevent="newPhieu.id ? updatePhieuGiamGia() : addPhieuGiamGia()">
              <div class="grid gap-4 mb-4 grid-cols-2">
                <div class="col-span-2 sm:col-span-1">
                  <label for="soPhanTramGiam" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Phần
                    trăm giảm</label>
                  <input v-model="newPhieu.soPhanTramGiam" type="number" name="soPhanTramGiam" id="soPhanTramGiam"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                    placeholder="% giảm" required>
                  <p v-if="errors.soPhanTramGiam" class="text-red-500 text-xs mt-1">{{ errors.soPhanTramGiam }}</p>
                </div>
                <div class="col-span-2 sm:col-span-1">
                  <label for="giaTriGiam" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Giá trị
                    giảm</label>
                  <input v-model="newPhieu.giaTriGiam" type="number" name="giaTriGiam" id="giaTriGiam"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                    placeholder="Giá trị giảm" required>
                  <p v-if="errors.giaTriGiam" class="text-red-500 text-xs mt-1">{{ errors.giaTriGiam }}</p>
                </div>
                <div class="col-span-2 sm:col-span-1">
                  <label for="giaTriDonHangToiThieu"
                    class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Giá trị đơn giảm tối
                    thiểu</label>
                  <input v-model="newPhieu.giaTriDonHangToiThieu" type="number" name="giaTriDonHangToiThieu"
                    id="giaTriDonHangToiThieu"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                    placeholder="Giá trị đơn hàng tối thiểu" required>
                  <p v-if="errors.giaTriDonHangToiThieu" class="text-red-500 text-xs mt-1">{{
                    errors.giaTriDonHangToiThieu }}</p>
                </div>
                <div class="col-span-2 sm:col-span-1">
                  <label for="soLuong" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Số
                    lượng</label>
                  <input v-model="newPhieu.soLuong" type="number" name="soLuong" id="soLuong"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                    placeholder="Số lượng" required>
                  <p v-if="errors.soLuong" class="text-red-500 text-xs mt-1">{{ errors.soLuong }}</p>
                </div>
                <div class="col-span-2 sm:col-span-1">
                  <label for="ngayBatDau" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Ngày bắt
                    đầu</label>
                  <input v-model="newPhieu.ngayBatDau" type="date" name="ngayBatDau" id="ngayBatDau"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                    required>
                  <p v-if="errors.ngayBatDau" class="text-red-500 text-xs mt-1">{{ errors.ngayBatDau }}</p>
                </div>
                <div class="col-span-2 sm:col-span-1">
                  <label for="ngayKetThuc" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Ngày kết
                    thúc</label>
                  <input v-model="newPhieu.ngayKetThuc" type="date" name="ngayKetThuc" id="ngayKetThuc"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                    required>
                  <p v-if="errors.ngayKetThuc" class="text-red-500 text-xs mt-1">{{ errors.ngayKetThuc }}</p>
                </div>
                <div class="col-span-2 sm:col-span-1">
                  <label for="trangThai" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Trạng
                    thái</label>
                  <select v-model="newPhieu.trangThai" id="trangThai"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-500 focus:border-primary-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500">
                    <option value="" disabled>Chọn trạng thái</option>
                    <option value="true">Hoạt động</option>
                    <option value="false">Hết hạn</option>
                  </select>
                  <p v-if="errors.trangThai" class="text-red-500 text-xs mt-1">{{ errors.trangThai }}</p>
                </div>
                <div class="col-span-2">
                  <label for="moTa" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Mô tả</label>
                  <textarea v-model="newPhieu.moTa" id="moTa" rows="4"
                    class="block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder="Nhập mô tả"></textarea>
                  <p v-if="errors.moTa" class="text-red-500 text-xs mt-1">{{ errors.moTa }}</p>
                </div>
              </div>
              <button type="submit"
                class="flex items-center gap-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 dark:focus:ring-blue-800 shadow-lg shadow-blue-500/50 dark:shadow-lg dark:shadow-blue-800/80 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2">
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
      <div class="bg-white p-4 shadow rounded w-full">
        <div class="flex items-center justify-between mb-4">
          <h2 class="text-lg font-bold mb-4">Phiếu giảm giá</h2>
          <div class="flex items-center justify-between gap-3">
            <select v-model="filterStatus" @change="applyFilters"
              class="block w-[150px] h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500 mb-2">
              <option value="">Tất cả trạng thái</option>
              <option value="true">Hoạt động</option>
              <option value="false">Hết hạn</option>
            </select>
            <input v-model="searchQuery" type="text" placeholder="Tìm kiếm phiếu giảm giá theo tên, mã, số lượng..."
              class="block w-[300px] h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500 mb-2" />
            <button @click="showModal = true" type="button"
              class="flex items-center gap-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 dark:focus:ring-blue-800 shadow-lg shadow-blue-500/50 dark:shadow-lg dark:shadow-blue-800/80 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                stroke="currentColor" class="w-4 h-4">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
              </svg>
              Thêm phiếu giảm giá
            </button>
          </div>
        </div>

        <div class="relative overflow-x-auto sm:rounded-lg w-full h-[calc(100vh-200px)]">
          <table class="w-full text-sm text-center text-gray-500">
            <thead class="text-xs text-gray-700 uppercase bg-gray-50">
              <tr>
                <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('id')">
                  #
                  <span v-if="sortKey === 'id'" class="ml-1">
                    <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24"
                      xmlns="http://www.w3.org/2000/svg">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        :d="sortDirection === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                    </svg>
                  </span>
                </th>
                <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('maPhieuGiamGia')">
                  Mã phiếu
                  <span v-if="sortKey === 'maPhieuGiamGia'" class="ml-1">
                    <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24"
                      xmlns="http://www.w3.org/2000/svg">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        :d="sortDirection === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                    </svg>
                  </span>
                </th>
                <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('moTa')">
                  Mô tả
                  <span v-if="sortKey === 'moTa'" class="ml-1">
                    <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24"
                      xmlns="http://www.w3.org/2000/svg">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        :d="sortDirection === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                    </svg>
                  </span>
                </th>
                <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('soLuong')">
                  Số lượng
                  <span v-if="sortKey === 'soLuong'" class="ml-1">
                    <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24"
                      xmlns="http://www.w3.org/2000/svg">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        :d="sortDirection === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                    </svg>
                  </span>
                </th>
                <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('trangThai')">
                  Trạng thái
                  <span v-if="sortKey === 'trangThai'" class="ml-1">
                    <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24"
                      xmlns="http://www.w3.org/2000/svg">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        :d="sortDirection === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                    </svg>
                  </span>
                </th>
                <th scope="col" class="px-6 py-3">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr class="bg-white border-b hover:bg-gray-50" v-for="(phieu, index) in filteredPhieuGiamGias"
                :key="phieu.id">
                <td class="px-6 py-4">{{ (currentPage * itemsPerPage) + index + 1 }}</td>
                <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">{{ phieu.maPhieuGiamGia }}</td>
                <td class="px-6 py-4">{{ phieu.moTa }}</td>
                <td class="px-6 py-4">{{ phieu.soLuong }}</td>
                <td class="px-6 py-4 text-center">
                  <span :class="phieu.trangThai
                    ? 'bg-green-100 text-green-700 px-2 py-1 text-xs text-center font-semibold rounded-lg'
                    : 'bg-red-100 text-red-700 px-2 py-1 text-xs text-center font-semibold rounded-lg'">
                    {{ phieu.trangThai ? 'Hoạt động' : 'Hết hạn' }}
                  </span>
                </td>
                <td class="px-6 py-4 flex justify-center space-x-3">
                  <a href="#" @click="editPhieu(phieu)" class="text-blue-600 hover:text-blue-800">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                      stroke="currentColor" class="w-5 h-5">
                      <path stroke-linecap="round" stroke-linejoin="round"
                        d="M16.862 3.487a2.25 2.25 0 1 1 3.182 3.182L6.75 19.5l-4.5 1.5 1.5-4.5 13.294-13.294z" />
                    </svg>
                  </a>
                  <a href="#" @click="xoaPhieu(phieu.id)" class="text-red-600 hover:text-red-800">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                      stroke="currentColor" class="w-5 h-5">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
                    </svg>
                  </a>
                  <a href="#" @click="showDetail(phieu)" class="text-gray-600 hover:text-gray-800">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                      stroke="currentColor" class="w-5 h-5">
                      <path stroke-linecap="round" stroke-linejoin="round"
                        d="M12 5c7 0 10 7 10 7s-3 7-10 7-10-7-10-7 3-7 10-7z" />
                      <path stroke-linecap="round" stroke-linejoin="round"
                        d="M12 9c1.657 0 3 1.343 3 3s-1.343 3-3 3-3-1.343-3-3 1.343-3 3-3z" />
                    </svg>
                  </a>
                </td>
              </tr>
              <tr class="bg-white border-b hover:bg-gray-50" v-if="filteredPhieuGiamGias.length == 0">
                <td class="px-6 py-4" colspan="6" align="center">Không có dữ liệu</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="flex justify-center items-center mt-4 space-x-2">
          <button @click="prevPage" :disabled="currentPage === 0"
            class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 dark:focus:ring-blue-800 shadow-lg shadow-blue-500/50 dark:shadow-lg dark:shadow-blue-800/80 font-medium rounded-lg text-sm p-2 text-center flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2"
              stroke="currentColor" class="w-4 h-4">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5 8.25 12l7.5-7.5" />
            </svg>
          </button>
          <span class="text-xs font-semibold text-gray-700">
            Trang {{ currentPage + 1 }} / {{ totalPages }}
          </span>
          <button @click="nextPage" :disabled="currentPage >= totalPages - 1"
            class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 dark:focus:ring-blue-800 shadow-lg shadow-blue-500/50 dark:shadow-lg dark:shadow-blue-800/80 font-medium rounded-lg text-sm p-2 text-center flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2"
              stroke="currentColor" class="w-4 h-4">
              <path stroke-linecap="round" stroke-linejoin="round" d="m8.25 4.5 7.5 7.5-7.5 7.5" />
            </svg>
          </button>
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
      currentPage: 0,
      totalPages: 0,
      itemsPerPage: 14,
      searchQuery: "",
      filterStatus: "", // Bộ lọc trạng thái
      showModal: false,
      showModalDetail: false,
      selectedPhieu: null,
      newPhieu: {
        soPhanTramGiam: 0,
        giaTriGiam: 0,
        giaTriDonHangToiThieu: 0,
        moTa: "",
        soLuong: 0,
        trangThai: "",
        ngayBatDau: "",
        ngayKetThuc: "",
        deleted: 0,
      },
      sortKey: '',
      sortDirection: 'asc',
      errors: {},
      showToast: false,
      toastMessage: '',
      toastType: 'success',
      showConfirmModal: false,
      confirmActionCallback: null,
    };
  },

  computed: {
    filteredPhieuGiamGias() {
      let result = [...this.phieuGiamGias];

      // Lọc theo trạng thái
      if (this.filterStatus !== "") {
        result = result.filter(phieu => phieu.trangThai.toString() === this.filterStatus);
      }

      // Lọc theo tìm kiếm
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase();
        result = result.filter(phieu => {
          return Object.values(phieu).some(value => {
            if (value !== null && value !== undefined) {
              return value.toString().toLowerCase().includes(query);
            }
            return false;
          });
        });
      }

      // Sắp xếp nếu có sortKey
      if (this.sortKey) {
        result.sort((a, b) => {
          let valueA = a[this.sortKey];
          let valueB = b[this.sortKey];
          if (valueA == null) valueA = '';
          if (valueB == null) valueB = '';
          if (this.sortKey === 'soLuong') {
            valueA = Number(valueA) || 0;
            valueB = Number(valueB) || 0;
          }
          if (valueA < valueB) return this.sortDirection === 'asc' ? -1 : 1;
          if (valueA > valueB) return this.sortDirection === 'asc' ? 1 : -1;
          return 0;
        });
      }

      return result;
    },
  },

  methods: {
    showToastMessage(message, type = 'success') {
      this.toastMessage = message;
      this.toastType = type;
      this.showToast = true;
      setTimeout(() => {
        this.showToast = false;
      }, 3000);
    },

    applyFilters() {
      // Không cần làm gì thêm vì computed property sẽ tự động cập nhật
    },

    validateForm() {
      this.errors = {};
      const currentDate = new Date('2025-03-16');
      const ngayBatDau = new Date(this.newPhieu.ngayBatDau);
      const ngayKetThuc = new Date(this.newPhieu.ngayKetThuc);

      if (this.newPhieu.soPhanTramGiam < 0 || this.newPhieu.soPhanTramGiam > 100) {
        this.errors.soPhanTramGiam = 'Phần trăm giảm phải từ 0 đến 100.';
      }
      if (this.newPhieu.giaTriGiam < 0) {
        this.errors.giaTriGiam = 'Giá trị giảm không được âm.';
      }
      if (this.newPhieu.giaTriDonHangToiThieu < 0) {
        this.errors.giaTriDonHangToiThieu = 'Giá trị đơn hàng tối thiểu không được âm.';
      }
      if (this.newPhieu.soLuong <= 0 || !Number.isInteger(Number(this.newPhieu.soLuong))) {
        this.errors.soLuong = 'Số lượng phải là số nguyên dương.';
      }
      if (!this.newPhieu.ngayBatDau) {
        this.errors.ngayBatDau = 'Ngày bắt đầu không được để trống.';
      } else if (ngayBatDau < currentDate && !this.newPhieu.id) {
        this.errors.ngayBatDau = 'Ngày bắt đầu không được nhỏ hơn ngày hiện tại.';
      }
      if (!this.newPhieu.ngayKetThuc) {
        this.errors.ngayKetThuc = 'Ngày kết thúc không được để trống.';
      } else if (ngayKetThuc < ngayBatDau) {
        this.errors.ngayKetThuc = 'Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu.';
      }
      if (!this.newPhieu.moTa.trim()) {
        this.errors.moTa = 'Mô tả không được để trống.';
      }
      if (this.newPhieu.trangThai === '') {
        this.errors.trangThai = 'Vui lòng chọn trạng thái.';
      }

      return Object.keys(this.errors).length === 0;
    },

    sort(key) {
      if (this.sortKey === key) {
        this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
      } else {
        this.sortKey = key;
        this.sortDirection = 'asc';
      }
    },

    prevPage() {
      if (this.currentPage > 0) {
        this.fetchPhieuGiamGia(this.currentPage - 1);
      }
    },

    nextPage() {
      if (this.currentPage < this.totalPages - 1) {
        this.fetchPhieuGiamGia(this.currentPage + 1);
      }
    },

    showDetail(phieu) {
      this.selectedPhieu = phieu;
      this.showModalDetail = true;
    },

    resetForm() {
      this.newPhieu = {
        id: null,
        soPhanTramGiam: 0,
        giaTriGiam: 0,
        giaTriDonHangToiThieu: 0,
        moTa: "",
        soLuong: 0,
        trangThai: "",
        ngayBatDau: "",
        ngayKetThuc: "",
      };
      this.errors = {};
    },

    fetchPhieuGiamGia(page = 0) {
      if (page < 0 || (this.totalPages && page >= this.totalPages)) return;
      phieuGiamGiaService.getAll(page, this.itemsPerPage).then((res) => {
        console.log("API Response:", res.data);
        if (res.data && Array.isArray(res.data.content)) {
          this.phieuGiamGias = res.data.content;
          this.totalPages = res.data.totalPages;
          this.currentPage = res.data.number;
        } else {
          console.error("Dữ liệu không hợp lệ:", res.data);
          this.phieuGiamGias = [];
        }
      }).catch((error) => {
        console.error("Lỗi gọi API:", error);
        this.showToastMessage("Lỗi khi tải dữ liệu: " + (error.response?.data || error.message), "error");
      });
    },

    async fetchData() {
      try {
        const response = await phieuGiamGiaService.getAll(this.currentPage, this.itemsPerPage);
        this.phieuGiamGias = Array.isArray(response.data.content) ? response.data.content : [];
        this.totalPages = response.data.totalPages;
        this.currentPage = response.data.number;
      } catch (error) {
        console.error("Lỗi khi tải dữ liệu:", error);
        this.showToastMessage("Lỗi khi tải dữ liệu: " + (error.response?.data || error.message), "error");
      }
    },

    showConfirmModalWithAction(callback) {
      this.showConfirmModal = true;
      this.confirmActionCallback = callback;
    },

    closeConfirmModal() {
      this.showConfirmModal = false;
      this.confirmActionCallback = null;
    },

    confirmAction() {
      if (this.confirmActionCallback) {
        this.confirmActionCallback();
      }
      this.closeConfirmModal();
    },

    async deletePhieu(id) {
      this.showConfirmModalWithAction(async () => {
        try {
          await phieuGiamGiaService.delete(id);
          this.showToastMessage("Xóa thành công!", "success");
          this.fetchData();
        } catch (error) {
          console.error("Lỗi khi xóa:", error);
          this.showToastMessage("Lỗi khi xóa: " + (error.response?.data || error.message), "error");
        }
      });
    },

    editPhieu(phieu) {
      this.newPhieu = { ...phieu };
      this.showModal = true;
    },

    async updatePhieuGiamGia() {
      if (this.validateForm()) {
        try {
          await phieuGiamGiaService.update(this.newPhieu.id, this.newPhieu);
          this.showToastMessage("Cập nhật thành công!", "success");
          this.showModal = false;
          this.resetForm();
          this.fetchData();
        } catch (error) {
          console.error("Lỗi khi cập nhật:", error.response?.data || error.message);
          this.showToastMessage("Lỗi khi cập nhật: " + (error.response?.data || error.message), "error");
        }
      } else {
        this.showToastMessage("Vui lòng kiểm tra lại dữ liệu nhập!", "warning");
        console.log("Validation failed:", this.errors);
      }
    },

    closeModal() {
      this.showModal = false;
      this.resetForm();
    },

    async addPhieuGiamGia() {
      if (this.validateForm()) {
        try {
          console.log("Dữ liệu gửi lên:", this.newPhieu);
          await phieuGiamGiaService.create(this.newPhieu);
          this.showToastMessage("Thêm thành công!", "success");
          this.resetForm();
          this.showModal = false;
          this.fetchData();
        } catch (error) {
          console.error("Lỗi khi thêm:", error.response?.data || error.message);
          this.showToastMessage("Lỗi khi thêm: " + (error.response?.data || error.message), "error");
        }
      } else {
        this.showToastMessage("Vui lòng kiểm tra lại dữ liệu nhập!", "warning");
        console.log("Validation failed:", this.errors);
      }
    },

    async xoaPhieu(id) {
      this.showConfirmModalWithAction(async () => {
        try {
          await phieuGiamGiaService.delete(id);
          this.showToastMessage("Xóa thành công!", "success");
          this.fetchData();
        } catch (error) {
          console.error("Lỗi khi xóa:", error);
          this.showToastMessage("Lỗi khi xóa: " + (error.response?.data || error.message), "error");
        }
      });
    },
  },

  mounted() {
    this.fetchData();
  },
};
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');
html, body, #app {
  height: 100%;
  width: 100%;
  margin: 0;
  padding: 0;
}

.h-screen {
  height: 100vh;
}

.w-screen {
  width: 100vw;
}
.font-roboto {
  font-family: 'Roboto', sans-serif;
}

.modal {
  z-index: 50;
  position: fixed;
  inset: 0;
}

table {
  position: relative;
  z-index: 1;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.bg-green-500 {
  background-color: #10b981;
}

.bg-red-500 {
  background-color: #ef4444;
}

.bg-yellow-500 {
  background-color: #f59e0b;
}
</style>