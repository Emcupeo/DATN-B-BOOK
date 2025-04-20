<template>
  <div class="w-full font-roboto">
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
    <!-- Modal chi tiết -->
    <div v-if="showModalDetail" class="fixed inset-0 flex items-center justify-center bg-gray-800 bg-opacity-50 z-50">
      <div class="bg-white p-6 rounded-lg shadow-lg max-w-4xl w-full relative">
        <button @click="showModalDetail = false" class="absolute top-3 right-3 text-gray-600 hover:text-red-500">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" viewBox="0 0 24 24" fill="none" stroke="currentColor"
            stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="18" y1="6" x2="6" y2="18"></line>
            <line x1="6" y1="6" x2="18" y2="18"></line>
          </svg>
        </button>
        <h2 class="text-xl font-bold mb-4 text-center">Chi tiết phiếu giảm giá</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <table class="w-full text-sm">
              <tbody>
                <tr>
                  <td class="p-2 font-semibold w-32">Mã phiếu</td>
                  <td class="p-2">{{ selectedPhieu.maPhieuGiamGia }}</td>
                </tr>
                <tr>
                  <td class="p-2 font-semibold">Tên phiếu</td>
                  <td class="p-2">{{ selectedPhieu.tenPhieuGiamGia }}</td>
                </tr>
                <tr>
                  <td class="p-2 font-semibold align-top">Mô tả</td>
                  <td class="p-2 whitespace-normal break-words" style="max-width: 400px; word-break: break-all;">{{ selectedPhieu.moTa }}</td>
                </tr>
                <tr>
                  <td class="p-2 font-semibold">Loại phiếu</td>
                  <td class="p-2">{{ selectedPhieu.discountDisplay }}</td>
                </tr>
                <tr>
                  <td class="p-2 font-semibold">Giá trị giảm</td>
                  <td class="p-2">{{ selectedPhieu.discountValue }}</td>
                </tr>
                <tr>
                  <td class="p-2 font-semibold">Số lượng</td>
                  <td class="p-2">{{ selectedPhieu.soLuong }}</td>
                </tr>
                <tr>
                  <td class="p-2 font-semibold">Ngày bắt đầu</td>
                  <td class="p-2">{{ formatDate(selectedPhieu.ngayBatDau) }}</td>
                </tr>
                <tr>
                  <td class="p-2 font-semibold">Ngày kết thúc</td>
                  <td class="p-2">{{ formatDate(selectedPhieu.ngayKetThuc) }}</td>
                </tr>
                <tr>
                  <td class="p-2 font-semibold">Tình trạng</td>
                  <td class="p-2">
                    <span class="px-2 py-1 text-xs font-semibold rounded-lg" :class="{
                      'bg-yellow-100 text-yellow-700': selectedPhieu.tinhTrang === 'Chưa bắt đầu',
                      'bg-green-100 text-green-700': selectedPhieu.tinhTrang === 'Đang diễn ra',
                      'bg-red-100 text-red-700': selectedPhieu.tinhTrang === 'Đã kết thúc'
                    }">
                      {{ selectedPhieu.tinhTrang }}
                    </span>
                  </td>
                </tr>
                <tr>
                  <td class="p-2 font-semibold">Loại áp dụng</td>
                  <td class="p-2">
                    {{ selectedPhieu.loaiApDung === 'PUBLIC' ? 'Công khai' : 'Khách hàng cụ thể' }}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div v-if="selectedPhieu.loaiApDung === 'CUSTOMER'">
            <table class="w-full text-sm">
              <tbody>
                <tr>
                  <td class="p-2 font-semibold">Loại áp dụng</td>
                  <td class="p-2">Khách hàng cụ thể</td>
                </tr>
                <tr v-if="selectedPhieu.tenKhachHang">
                  <td class="p-2 font-semibold">Tên khách hàng</td>
                  <td class="p-2">{{ selectedPhieu.tenKhachHang }}</td>
                </tr>
                <tr v-if="selectedPhieu.email">
                  <td class="p-2 font-semibold">Email</td>
                  <td class="p-2">{{ selectedPhieu.email }}</td>
                </tr>
                <tr v-if="selectedPhieu.sdt">
                  <td class="p-2 font-semibold">SĐT</td>
                  <td class="p-2">{{ selectedPhieu.sdt }}</td>
                </tr>
              </tbody>
            </table>
          </div>
          <div v-else>
            <p class="p-4 bg-teal-50 text-teal-700 rounded-md font-medium">Áp dụng công khai cho tất cả khách hàng.</p>
          </div>
        </div>
      </div>
    </div>
    <div class="w-full">
      <div class="bg-white p-4 rounded w-full">
        <h2 class="text-lg font-bold mb-4">Phiếu giảm giá</h2>
        <div class="flex items-center justify-between mb-4">
          <div class="flex flex-wrap items-center justify-between gap-3 mb-4">
            <div class="flex flex-wrap gap-2 items-center mt-5">
              <div class="relative w-[150px]">
                <input type="date" v-model="filterFromDate" id="date-from"
                  class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500"
                  @change="validateDates" />
                <label for="date-from" class="absolute left-2 top-2 text-gray-500 text-xs transition-all duration-300"
                  :class="{ 'top-[-20px] text-blue-600': filterFromDate }">Chọn ngày bắt đầu</label>
              </div>
              <div class="relative w-[150px]">
                <input type="date" v-model="filterToDate" id="date-to"
                  class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500"
                  @change="validateDates" />
                <label for="date-to" class="absolute left-2 top-2 text-gray-500 text-xs transition-all duration-300"
                  :class="{ 'top-[-20px] text-blue-600': filterToDate }">Chọn ngày kết thúc</label>
              </div>
              <select v-model="filterLoaiPhieu" @change="fetchPhieuGiamGia"
                class="block w-[150px] h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500">
                <option value="">Tất cả loại phiếu</option>
                <option value="PERCENT">Giảm theo phần trăm</option>
                <option value="AMOUNT">Giảm theo giá trị tiền</option>
              </select>
              <select v-model="filterLoaiApDung" @change="fetchPhieuGiamGia"
                class="block w-[150px] h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500">
                <option value="">Tất cả cách áp dụng</option>
                <option value="PUBLIC">Công khai</option>
                <option value="CUSTOMER">Khách hàng cụ thể</option>
              </select>
              <select v-model="filterStatus" @change="fetchPhieuGiamGia"
                class="block w-[150px] h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500">
                <option value="">Tất cả tình trạng</option>
                <option value="Chưa bắt đầu">Chưa bắt đầu</option>
                <option value="Đang diễn ra">Đang diễn ra</option>
                <option value="Đã kết thúc">Đã kết thúc</option>
              </select>
              <input v-model="searchQuery" @input="fetchPhieuGiamGia" type="text" placeholder="Tìm kiếm..."
                class="block w-[345px] h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500" />
            </div>
            <button type="button" @click="exportToExcel"
              class="flex items-center gap-2 text-white bg-gradient-to-r from-green-600 via-green-700 to-green-800 hover:from-green-700 hover:via-green-800 hover:to-green-900 focus:ring-4 focus:outline-none focus:ring-green-400 dark:focus:ring-green-900 font-medium rounded-lg text-sm px-5 py-2.5 text-center mt-5">
              <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" viewBox="0,0,256,256">
                <g fill="#ffffff">
                  <g transform="scale(5.12,5.12)">
                    <path
                      d="M16,4c-1.65,0 -3,1.35 -3,3v4h2v-4c0,-0.55 0.45,-1 1,-1h14v8h-3.49023c0.29,0.61 0.46047,1.28 0.48047,2h3.00977v8h-3v2h3v8h-3.00977c-0.02,0.72 -0.19047,1.39 -0.48047,2h3.49023v8h-14c-0.55,0 -1,-0.45 -1,-1v-4h-2v4c0,1.65 1.35,3 3,3h30c1.65,0 3,-1.35 3,-3v-36c0,-1.65 -1.35,-3 -3,-3zM32,6h14c0.55,0 1,0.45 1,1v7h-15zM4.19922,13c-1.75547,0 -3.19922,1.44375 -3.19922,3.19922v17.60156c0,1.75547 1.44375,3.19922 3.19922,3.19922h17.60156c1.75547,0 3.19922,-1.44375 3.19922,-3.19922v-17.60156c0,-1.75547 -1.44375,-3.19922 -3.19922,-3.19922zM4.19922,15h17.60156c0.67453,0 1.19922,0.52468 1.19922,1.19922v17.60156c0,0.67453 -0.52469,1.19922 -1.19922,1.19922h-17.60156c-0.67453,0 -1.19922,-0.52468 -1.19922,-1.19922v-17.60156c0,-0.67453 0.52469,-1.19922 1.19922,-1.19922zM32,16h15v8h-15zM7.96875,19l3.49414,5.97852l-3.83203,6.02148h2.86328l2.52148,-4.7168l2.5332,4.7168h2.82031l-3.76953,-6l3.68555,-6h-2.67578l-2.45508,4.50586l-2.32422,-4.50586zM32,26h15v8h-15zM32,36h15v7c0,0.55 -0.45,1 -1,1h-14z">
                    </path>
                  </g>
                </g>
              </svg>
              Xuất Excel
            </button>
            <button @click="goToAdd" type="button"
              class="flex items-center gap-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 dark:focus:ring-blue-800 shadow-lg font-medium rounded-lg text-sm px-5 py-2.5 mt-5">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                stroke="currentColor" class="w-4 h-4">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
              </svg>
              Thêm phiếu giảm giá
            </button>
          </div>
        </div>
        <div class="relative overflow-x-auto sm:rounded-lg w-full h-[calc(100vh-272px)]">
  <table class="w-full text-sm text-center text-gray-500">
    <thead class="text-xs text-gray-700 uppercase bg-gray-50">
      <tr>
        <th scope="col" class="px-6 py-3">#</th>
        <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('maPhieuGiamGia')">
          Mã phiếu
          <span v-if="sortBy === 'maPhieuGiamGia'" class="ml-1">
            <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortDir === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
            </svg>
          </span>
        </th>
        <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('tenPhieuGiamGia')">
          Tên phiếu
          <span v-if="sortBy === 'tenPhieuGiamGia'" class="ml-1">
            <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortDir === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
            </svg>
          </span>
        </th>
        <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('loaiApDung')">
          Loại áp dụng
          <span v-if="sortBy === 'loaiApDung'" class="ml-1">
            <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortDir === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
            </svg>
          </span>
        </th>
        <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('moTa')">
          Mô tả
          <span v-if="sortBy === 'moTa'" class="ml-1">
            <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortDir === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
            </svg>
          </span>
        </th>
        <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('loaiPhieu')">
          Loại phiếu
          <span v-if="sortBy === 'loaiPhieu'" class="ml-1">
            <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortDir === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
            </svg>
          </span>
        </th>
        <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('soLuong')">
          Số lượng
          <span v-if="sortBy === 'soLuong'" class="ml-1">
            <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortDir === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
            </svg>
          </span>
        </th>
        <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('discountValue')">
          Giá trị giảm
          <span v-if="sortBy === 'discountValue'" class="ml-1">
            <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortDir === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
            </svg>
          </span>
        </th>
        <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('ngayBatDau')">
          Ngày bắt đầu
          <span v-if="sortBy === 'ngayBatDau'" class="ml-1">
            <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortDir === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
            </svg>
          </span>
        </th>
        <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('ngayKetThuc')">
          Ngày kết thúc
          <span v-if="sortBy === 'ngayKetThuc'" class="ml-1">
            <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortDir === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
            </svg>
          </span>
        </th>
        <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('tinhTrang')">
          Tình trạng
          <span v-if="sortBy === 'tinhTrang'" class="ml-1">
            <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortDir === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
            </svg>
          </span>
        </th>
        <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('trangThai')">
          Trạng thái
          <span v-if="sortBy === 'trangThai'" class="ml-1">
            <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortDir === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
            </svg>
          </span>
        </th>
        <th scope="col" class="px-6 py-3">Hành động</th>
      </tr>
    </thead>
    <tbody>
      <tr class="bg-white border-b hover:bg-gray-50" v-if="isLoading">
        <td colspan="13" class="text-center py-4">
          <div class="flex justify-center items-center">
            <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
            <span class="ml-2">Đang tải dữ liệu...</span>
          </div>
        </td>
      </tr>
      <tr class="bg-white border-b hover:bg-gray-50" v-else-if="!phieuGiamGias || phieuGiamGias.length === 0">
        <td colspan="13" class="text-center py-4">Không có dữ liệu phù hợp với bộ lọc</td>
      </tr>
      <template v-else>
        <tr class="bg-white border-b hover:bg-gray-50" v-for="(phieu, index) in phieuGiamGias" :key="phieu.id">
          <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
            {{ calculateSTT(index) }}
          </td>
          <td class="px-6 py-4 max-w-xs" :title="phieu.maPhieuGiamGia">{{ truncateText(phieu.maPhieuGiamGia) }}</td>
          <td class="px-6 py-4 max-w-xs" :title="phieu.tenPhieuGiamGia">{{ truncateText(phieu.tenPhieuGiamGia) }}</td>
          <td class="px-6 py-4 text-center">
            <svg v-if="phieu.loaiApDung === 'PUBLIC'" class="w-6 h-6 inline text-blue-500" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 21a9.004 9.004 0 0 0 8.716-6.747M12 21a9.004 9.004 0 0 1-8.716-6.747M12 21c2.485 0 4.5-4.03 4.5-9S14.485 3 12 3m0 18c-2.485 0-4.5-4.03-4.5-9S9.515 3 12 3m0 0a8.997 8.997 0 0 1 7.843 4.582M12 3a8.997 8.997 0 0 0-7.843 4.582m15.686 0A11.953 11.953 0 0 1 12 10.5c-2.998 0-5.74-1.1-7.843-2.918m15.686 0A8.959 8.959 0 0 1 21 12c0 .778-.099 1.533-.284 2.253m0 0A17.919 17.919 0 0 1 12 16.5c-3.162 0-6.133-.815-8.716-2.247m0 0A9.015 9.015 0 0 1 3 12c0-1.605.42-3.113 1.157-4.418" />
            </svg>
            <svg v-else class="w-6 h-6 inline text-gray-500" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15 19.128a9.38 9.38 0 0 0 2.625.372 9.337 9.337 0 0 0 4.121-.952 4.125 4.125 0 0 0-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 0 1 8.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0 1 11.964-3.07M12 6.375a3.375 3.375 0 1 1-6.75 0 3.375 3.375 0 0 1 6.75 0Zm8.25 2.25a2.625 2.625 0 1 1-5.25 0 2.625 2.625 0 0 1 5.25 0Z" />
            </svg>
          </td>
          <td class="px-6 py-4 max-w-xs" :title="phieu.moTa">{{ truncateText(phieu.moTa) }}</td>
          <td class="px-6 py-4 text-center">
            <svg v-if="phieu.loaiPhieu === 'PERCENT'" class="w-6 h-6 inline text-green-500" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" d="m8.99 14.993 6-6m6 3.001c0 1.268-.63 2.39-1.593 3.069a3.746 3.746 0 0 1-1.043 3.296 3.745 3.745 0 0 1-3.296 1.043 3.745 3.745 0 0 1-3.068 1.593c-1.268 0-2.39-.63-3.068-1.593a3.745 3.745 0 0 1-3.296-1.043 3.746 3.746 0 0 1-1.043-3.297 3.746 3.746 0 0 1-1.593-3.068c0-1.268.63-2.39 1.593-3.068a3.746 3.746 0 0 1 1.043-3.297 3.745 3.745 0 0 1 3.296-1.042 3.745 3.745 0 0 1 3.068-1.594c1.268 0 2.39.63 3.068 1.593a3.745 3.745 0 0 1 3.296 1.043 3.746 3.746 0 0 1 1.043 3.297 3.746 3.746 0 0 1 1.593 3.068ZM9.74 9.743h.008v.007H9.74v-.007Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm4.125 4.5h.008v.008h-.008v-.008Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Z" />
            </svg>
            <svg v-else class="w-6 h-6 inline text-yellow-500" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v12m-3-2.818.879.659c1.171.879 3.07.879 4.242 0 1.172-.879 1.172-2.303 0-3.182C13.536 12.219 12.768 12 12 12c-.725 0-1.45-.22-2.003-.659-1.106-.879-1.106-2.303 0-3.182s2.9-.879 4.006 0l.415.33M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
            </svg>
          </td>
          <td class="px-6 py-4 text-center">{{ phieu.soLuong }}</td>
          <td class="px-6 py-4">
            <span v-if="phieu.loaiPhieu === 'PERCENT'">{{ phieu.soPhanTramGiam }}%</span>
            <span v-else>{{ formatNumber(phieu.giaTriGiam) }}đ</span>
          </td>
          <td class="px-6 py-4">{{ formatDate(phieu.ngayBatDau) }}</td>
          <td class="px-6 py-4">{{ formatDate(phieu.ngayKetThuc) }}</td>
          <td class="px-6 py-4 text-center">
            <svg v-if="phieu.tinhTrang === 'Chưa bắt đầu'" class="w-6 h-6 inline text-gray-500" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
            </svg>
            <svg v-else-if="phieu.tinhTrang === 'Đang diễn ra'" class="w-6 h-6 inline text-green-500" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" d="M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
              <path stroke-linecap="round" stroke-linejoin="round" d="M15.91 11.672a.375.375 0 0 1 0 .656l-5.603 3.113a.375.375 0 0 1-.557-.328V8.887c0-.286.307-.466.557-.327l5.603 3.112Z" />
            </svg>
            <svg v-else class="w-6 h-6 inline text-red-500" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" d="m9.75 9.75 4.5 4.5m0-4.5-4.5 4.5M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
            </svg>
          </td>
          <td class="px-6 py-4 text-center">
            <div class="flex items-center justify-center">
              <label class="flex items-center cursor-pointer select-none text-dark dark:text-white">
                <div class="relative">
                  <input type="checkbox" :checked="phieu.trangThai === true || phieu.trangThai === 1" @change="toggleStatus(phieu)" class="sr-only" />
                  <div :class="['block h-8 rounded-full w-14 transition-colors duration-300 ease-in-out', (phieu.trangThai === true || phieu.trangThai === 1) ? 'bg-blue-100' : 'bg-red-100']"></div>
                  <div :class="{ 'translate-x-full !bg-blue-500': phieu.trangThai === true || phieu.trangThai === 1, 'bg-white': phieu.trangThai !== true && phieu.trangThai !== 1 }" class="absolute flex items-center justify-center w-6 h-6 transition-all duration-300 ease-in-out bg-white rounded-full dot left-1 top-1 shadow-md">
                    <span v-if="phieu.trangThai === true || phieu.trangThai === 1" class="text-white">
                      <svg width="11" height="8" viewBox="0 0 11 8" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M10.0915 0.951972L10.0867 0.946075L10.0813 0.940568C9.90076 0.753564 9.61034 0.753146 9.42927 0.939309L4.16201 6.22962L1.58507 3.63469C1.40401 3.44841 1.11351 3.44879 0.932892 3.63584C0.755703 3.81933 0.755703 4.10875 0.932892 4.29224L0.932878 4.29225L0.934851 4.29424L3.58046 6.95832C3.73676 7.11955 3.94983 7.2 4.1473 7.2C4.36196 7.2 4.55963 7.11773 4.71406 6.9584L10.0468 1.60234C10.2436 1.4199 10.2421 1.1339 10.0915 0.951972ZM4.2327 6.30081L4.2317 6.2998C4.23206 6.30015 4.23237 6.30049 4.23269 6.30082L4.2327 6.30081Z" fill="white" stroke="white" stroke-width="0.4" />
                      </svg>
                    </span>
                    <span v-else class="text-red-500">
                      <svg class="w-4 h-4 stroke-current" fill="none" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                      </svg>
                    </span>
                  </div>
                </div>
              </label>
            </div>
          </td>
          <td class="px-6 py-4 flex justify-center space-x-3">
            <a href="#" @click="goToEdit(phieu.id)" class="text-blue-600 hover:text-blue-800">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M16.862 3.487a2.25 2.25 0 1 1 3.182 3.182L6.75 19.5l-4.5 1.5 1.5-4.5 13.294-13.294z" />
              </svg>
            </a>
            <a href="#" @click="showDetail(phieu)" class="text-gray-600 hover:text-gray-800">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 5c7 0 10 7 10 7s-3 7-10 7-10-7-10-7 3-7 10-7z" />
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 9c1.657 0 3 1.343 3 3s-1.343 3-3 3-3-1.343-3-3 1.343-3 3-3z" />
              </svg>
            </a>
          </td>
        </tr>
      </template>
    </tbody>
  </table>
</div>
        <div class="flex justify-center items-center mt-4 space-x-2">
          <button @click="previousPage" :disabled="currentPage <= 1"
            class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg font-medium rounded-lg text-sm p-2 flex items-center justify-center disabled:opacity-50">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2"
              stroke="currentColor" class="w-4 h-4">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5 8.25 12l7.5-7.5" />
            </svg>
          </button>
          <span class="text-xs font-semibold text-gray-700">Trang {{ currentPage }} / {{ totalPages }}</span>
          <button @click="nextPage" :disabled="currentPage >= totalPages"
            class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg font-medium rounded-lg text-sm p-2 flex items-center justify-center disabled:opacity-50">
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
import * as XLSX from 'xlsx';

export default {
  data() {
    return {
      phieuGiamGias: [],
      currentPage: 1,
      pageSize: 10,
      totalPages: 1,
      totalElements: 0,
      sortBy: 'createdAt',
      sortDir: 'desc',
      isLoading: false,
      showToast: false,
      toastMessage: '',
      toastType: 'success',
      showConfirmModal: false,
      showModalDetail: false,
      selectedPhieu: null,
      filterFromDate: '',
      filterToDate: '',
      filterLoaiPhieu: '',
      filterLoaiApDung: '',
      filterStatus: '',
      searchQuery: '',
    };
  },

  computed: {
    formatNumber() {
      return (number) => {
        return new Intl.NumberFormat('vi-VN').format(number);
      };
    },

    computedFilterLoaiPhieu() {
      return this.filterLoaiPhieu || null;
    },

    computedFilterLoaiApDung() {
      return this.filterLoaiApDung || null;
    },

    computedFilterStatus() {
      return this.filterStatus || null;
    },

    calculateSTT() {
      return (index) => {
        return (this.currentPage - 1) * this.pageSize + index + 1;
      };
    }
  },

  methods: {
    async fetchPhieuGiamGia() {
      try {
        this.isLoading = true;
        const params = {
          page: this.currentPage - 1,
          size: this.pageSize,
          sortBy: this.sortBy,
          sortDir: this.sortDir,
          loaiApDung: this.computedFilterLoaiApDung,
          loaiPhieu: this.computedFilterLoaiPhieu,
          tinhTrang: this.computedFilterStatus,
          searchQuery: this.searchQuery,
          fromDate: this.filterFromDate,
          toDate: this.filterToDate
        };

        console.log('Fetching with params:', params);
        const response = await phieuGiamGiaService.getAll(params);
        console.log('API response:', response.data);

        if (response.data) {
          this.phieuGiamGias = response.data.content;
          this.totalElements = response.data.page.totalElements;
          this.totalPages = response.data.page.totalPages;

          console.log('Pagination info:', {
            totalElements: this.totalElements,
            totalPages: this.totalPages,
            currentPage: this.currentPage,
            itemsPerPage: this.pageSize,
            searchQuery: this.searchQuery
          });
        }
      } catch (error) {
        console.error('Error fetching data:', error);
        this.showNotification('Không thể tải dữ liệu. Vui lòng thử lại sau.', 'error');
      } finally {
        this.isLoading = false;
      }
    },

    validateDates() {
      if (this.filterFromDate && this.filterToDate && this.filterFromDate > this.filterToDate) {
        this.showNotification('Ngày bắt đầu không thể lớn hơn ngày kết thúc', 'error');
        this.filterToDate = '';
      }
      this.currentPage = 1;
      this.fetchPhieuGiamGia();
    },

    previousPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchPhieuGiamGia();
      }
    },

    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchPhieuGiamGia();
      }
    },

    sort(field) {
      if (this.sortBy === field) {
        this.sortDir = this.sortDir === 'asc' ? 'desc' : 'asc';
      } else {
        this.sortBy = field;
        this.sortDir = 'asc';
      }
      console.log('Sorting by:', this.sortBy, 'Direction:', this.sortDir);
      this.currentPage = 1;
      this.fetchPhieuGiamGia();
    },

    showDetail(phieu) {
      this.selectedPhieu = {
        ...phieu,
        discountDisplay:
          phieu.loaiPhieu === 'PERCENT'
            ? 'Giảm theo phần trăm'
            : phieu.loaiPhieu === 'AMOUNT'
              ? 'Giảm theo giá trị tiền'
              : 'Không xác định',
        discountValue:
          phieu.loaiPhieu === 'PERCENT'
            ? `${phieu.soPhanTramGiam}%`
            : phieu.loaiPhieu === 'AMOUNT'
              ? `${this.formatNumber(phieu.giaTriGiam)}đ`
              : 'Không xác định',
      };
      this.showModalDetail = true;
    },

    goToAdd() {
      this.$router.push('/admin/phieu-giam-gia/add');
    },

    goToEdit(id) {
      this.$router.push(`/admin/phieu-giam-gia/edit/${id}`);
    },

    async toggleStatus(phieu) {
      try {
        const currentStatus = phieu.trangThai === true || phieu.trangThai === 1;
        const newStatus = !currentStatus;
        phieu.trangThai = newStatus;

        const updatedPhieu = { ...phieu, trangThai: newStatus };
        await phieuGiamGiaService.update(phieu.id, updatedPhieu);

        this.showToastMessage(
          newStatus ? 'Đã kích hoạt phiếu giảm giá!' : 'Đã ngưng hoạt động phiếu giảm giá!',
          'success'
        );
        this.fetchPhieuGiamGia();
      } catch (error) {
        console.error('Lỗi khi cập nhật trạng thái:', error);
        this.showToastMessage(
          'Lỗi khi cập nhật trạng thái: ' + (error.response?.data?.message || error.message),
          'error'
        );
        phieu.trangThai = !phieu.trangThai;
      }
    },

    showToastMessage(message, type = 'success') {
      this.toastMessage = message;
      this.toastType = type;
      this.showToast = true;
      setTimeout(() => {
        this.showToast = false;
      }, 3000);
    },

    showNotification(message, type) {
      this.showToastMessage(message, type);
    },

    formatDate(dateStr) {
      if (!dateStr) return '';
      const date = new Date(dateStr);
      if (isNaN(date.getTime())) return '';
      const pad = (num) => num.toString().padStart(2, '0');
      return `${pad(date.getDate())}/${pad(date.getMonth() + 1)}/${date.getFullYear()} ${pad(date.getHours())}:${pad(
        date.getMinutes()
      )}`;
    },

    exportToExcel() {
      const data = this.phieuGiamGias.map((item) => ({
        'Mã Phiếu': item.maPhieuGiamGia,
        'Tên Phiếu': item.tenPhieuGiamGia,
        'Mô Tả': item.moTa,
        'Loại Phiếu':
          item.loaiPhieu === 'PERCENT' ? 'Giảm theo phần trăm' : 'Giảm theo giá trị tiền',
        'Giá Trị Giảm':
          item.loaiPhieu === 'PERCENT'
            ? `${item.soPhanTramGiam}%`
            : `${this.formatNumber(item.giaTriGiam)}đ`,
        'Số Lượng': item.soLuong,
        'Ngày Bắt Đầu': this.formatDate(item.ngayBatDau),
        'Ngày Kết Thúc': this.formatDate(item.ngayKetThuc),
        'Tình Trạng': item.tinhTrang,
        'Loại Áp Dụng': item.loaiApDung === 'PUBLIC' ? 'Công khai' : 'Khách hàng cụ thể',
      }));

      const ws = XLSX.utils.json_to_sheet(data);
      const wscols = [
        { wch: 15 },
        { wch: 20 },
        { wch: 40 },
        { wch: 25 },
        { wch: 15 },
        { wch: 10 },
        { wch: 20 },
        { wch: 20 },
        { wch: 15 },
        { wch: 20 },
      ];
      ws['!cols'] = wscols;

      const headerStyle = {
        font: { bold: true, color: { rgb: 'FFFFFF' }, sz: 12 },
        fill: { patternType: 'solid', fgColor: { rgb: '4CAF50' } },
        alignment: { horizontal: 'center', vertical: 'center' },
      };

      ['A1', 'B1', 'C1', 'D1', 'E1', 'F1', 'G1', 'H1', 'I1', 'J1'].forEach((cell) => {
        if (ws[cell]) ws[cell].s = headerStyle;
      });

      const wb = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, 'Phiếu Giảm Giá');

      try {
        XLSX.writeFile(wb, 'Phieu_Giam_Gia.xlsx');
        this.showToastMessage('Xuất Excel thành công!', 'success');
      } catch (error) {
        console.error('Lỗi khi xuất Excel:', error);
        this.showToastMessage('Lỗi khi xuất Excel: ' + error.message, 'error');
      }
    },

    closeConfirmModal() {
      this.showConfirmModal = false;
    },

    confirmAction() {
      if (this.confirmActionCallback) {
        this.confirmActionCallback();
      }
      this.closeConfirmModal();
    },

    async createPhieuGiamGia() {
      const newPhieu = {
        khachHangId: this.selectedKhachHangId,
      };

      try {
        await phieuGiamGiaService.create(newPhieu);
        this.showToastMessage('Tạo phiếu giảm giá thành công!', 'success');
        this.fetchPhieuGiamGia();
      } catch (error) {
        console.error('Lỗi khi tạo phiếu:', error);
        this.showToastMessage('Lỗi khi tạo phiếu: ' + error.message, 'error');
      }
    },

    truncateText(text, length = 10) {
      if (!text) return '';
      return text.length > length ? text.slice(0, length) + '...' : text;
    },

    getSortIcon(field) {
      if (this.sortBy !== field) return 'none';
      return this.sortDir === 'asc' ? 'asc' : 'desc';
    },
  },

  async mounted() {
    console.log('Component mounted, fetching initial data');
    await this.fetchPhieuGiamGia();
    const message = this.$route.query.message;
    const type = this.$route.query.type || 'success';
    if (message) {
      this.showToastMessage(message, type);
      this.$router.replace({ query: {} });
    }
  },

  watch: {
    currentPage(newVal) {
      console.log('currentPage changed:', newVal);
      this.fetchPhieuGiamGia();
    },
    pageSize(newVal) {
      console.log('pageSize changed:', newVal);
      this.currentPage = 1;
      this.fetchPhieuGiamGia();
    },
    filterFromDate(newVal) {
      console.log('filterFromDate changed:', newVal);
      this.currentPage = 1;
      this.validateDates();
    },
    filterToDate(newVal) {
      console.log('filterToDate changed:', newVal);
      this.currentPage = 1;
      this.validateDates();
    },
    filterLoaiPhieu(newVal) {
      console.log('filterLoaiPhieu changed:', newVal);
      this.currentPage = 1;
      this.fetchPhieuGiamGia();
    },
    filterLoaiApDung(newVal) {
      console.log('filterLoaiApDung changed:', newVal);
      this.currentPage = 1;
      this.fetchPhieuGiamGia();
    },
    filterStatus(newVal) {
      console.log('filterStatus changed:', newVal);
      this.currentPage = 1;
      this.fetchPhieuGiamGia();
    },
    searchQuery(newVal) {
      console.log('searchQuery changed:', newVal);
      this.currentPage = 1;
      this.fetchPhieuGiamGia();
    },
  },
};
</script>

<style scoped>
.table-cell {
  white-space: normal;
  word-wrap: break-word;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
}

td {
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

td.description {
  white-space: normal;
  word-break: break-word;
}

/* Loading animation */
.animate-spin {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');

html,
body,
#app {
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

input[type="date"]:focus+label,
input[type="date"]:not(:placeholder-shown)+label {
  top: -20px;
  font-size: 12px;
  color: #3b82f6;
  margin-left: -6px;
}

/* Toggle Switch Styles */
.dot {
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

input:checked+.block {
  background-color: rgba(59, 130, 246, 0.2);
}

input:not(:checked)+.block {
  background-color: rgba(229, 231, 235, 0.8);
}

.small-font-table {
  font-size: 0.75rem;
  /* This is equivalent to text-xs in Tailwind CSS */
}
</style>