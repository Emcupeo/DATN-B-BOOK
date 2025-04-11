<template>
  <div class="w-full font-roboto">
    <!-- Toast Notification -->
    <transition name="fade">
      <div v-if="showToast" class="fixed top-5 right-5 z-50 max-w-sm w-full p-4 rounded-lg shadow-lg text-white flex items-center" :class="{
        'bg-green-500': toastType === 'success',
        'bg-red-500': toastType === 'error',
        'bg-yellow-500': toastType === 'warning'
      }">
        <span class="mr-2">
          <svg v-if="toastType === 'success'" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
          </svg>
          <svg v-if="toastType === 'error'" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
          <svg v-if="toastType === 'warning'" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01M12 3a9 9 0 110 18 9 9 0 010-18z"></path>
          </svg>
        </span>
        {{ toastMessage }}
      </div>
    </transition>
    <!-- Modal xác nhận xóa -->
    <div v-if="showConfirmModal" class="fixed inset-0 flex items-center justify-center bg-gray-800 bg-opacity-50 z-50">
      <div class="relative p-4 w-full max-w-md max-h-full">
        <div class="relative bg-white rounded-lg shadow-sm dark:bg-gray-700">
          <button @click="closeConfirmModal" type="button" class="absolute top-3 end-2.5 text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm w-8 h-8 ms-auto inline-flex justify-center items-center dark:hover:bg-gray-600 dark:hover:text-white">
            <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
              <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6" />
            </svg>
            <span class="sr-only">Close modal</span>
          </button>
          <div class="p-4 md:p-5 text-center">
            <svg class="mx-auto mb-4 text-gray-400 w-12 h-12 dark:text-gray-200" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
              <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 11V6m0 8h.01M19 10a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
            </svg>
            <h3 class="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">Bạn có chắc muốn xóa phiếu giảm giá này không?</h3>
            <button @click="confirmAction" type="button" class="text-white bg-red-600 hover:bg-red-800 focus:ring-4 focus:outline-none focus:ring-red-300 dark:focus:ring-red-800 font-medium rounded-lg text-sm inline-flex items-center px-5 py-2.5 text-center">
              Có, tôi chắc chắn
            </button>
            <button @click="closeConfirmModal" type="button" class="py-2.5 px-5 ms-3 text-sm font-medium text-gray-900 focus:outline-none bg-white rounded-lg border border-gray-200 hover:bg-gray-100 hover:text-blue-700 focus:z-10 focus:ring-4 focus:ring-gray-100 dark:focus:ring-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:border-gray-600 dark:hover:text-white dark:hover:bg-gray-700">
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
          <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
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
                  <td class="p-2 font-semibold">Mã phiếu</td>
                  <td class="p-2">{{ selectedPhieu?.maPhieuGiamGia }}</td>
                </tr>
                <tr>
                  <td class="p-2 font-semibold">Tên phiếu</td>
                  <td class="p-2">{{ selectedPhieu?.tenPhieuGiamGia }}</td>
                </tr>
                <tr>
                  <td class="p-2 font-semibold">Mô tả</td>
                  <td class="p-2">{{ selectedPhieu?.moTa }}</td>
                </tr>
                <tr>
                  <td class="p-2 font-semibold">Loại phiếu</td>
                  <td class="p-2">
                    <span :class="{
                      'bg-blue-100 text-blue-700 px-2 py-1 text-xs font-semibold rounded-lg': selectedPhieu.discountDisplay === 'Giảm theo phần trăm',
                      'bg-purple-100 text-purple-700 px-2 py-1 text-xs font-semibold rounded-lg': selectedPhieu.discountDisplay === 'Giảm theo giá trị tiền',
                      'bg-gray-100 text-gray-700 px-2 py-1 text-xs font-semibold rounded-lg': selectedPhieu.discountDisplay === 'Không xác định'
                    }">
                      {{ selectedPhieu.discountDisplay }}
                    </span>
                  </td>
                </tr>
                <tr v-if="selectedPhieu.discountValue && selectedPhieu.discountValue !== 'Không xác định'">
                  <td class="p-2 font-semibold">Giá trị giảm</td>
                  <td class="p-2">{{ selectedPhieu.discountValue }}</td>
                </tr>
                <tr>
                  <td class="p-2 font-semibold">Số lượng</td>
                  <td class="p-2">{{ selectedPhieu?.soLuong }}</td>
                </tr>
                <tr>
                  <td class="p-2 font-semibold">Ngày bắt đầu</td>
                  <td class="p-2">{{ formatDate(selectedPhieu?.ngayBatDau) }}</td>
                </tr>
                <tr>
                  <td class="p-2 font-semibold">Ngày kết thúc</td>
                  <td class="p-2">{{ formatDate(selectedPhieu?.ngayKetThuc) }}</td>
                </tr>
                <tr>
                  <td class="p-2 font-semibold">Tình trạng</td>
                  <td class="p-2">
                    <span :class="{
                      'bg-green-100 text-green-700 px-2 py-1 text-xs font-semibold rounded-lg': getTrangThaiHienTai(selectedPhieu) === 'Hoạt động',
                      'bg-yellow-100 text-yellow-700 px-2 py-1 text-xs font-semibold rounded-lg': getTrangThaiHienTai(selectedPhieu) === 'Đang chờ',
                      'bg-red-100 text-red-700 px-2 py-1 text-xs font-semibold rounded-lg': getTrangThaiHienTai(selectedPhieu) === 'Hết hạn',
                      'bg-gray-100 text-gray-700 px-2 py-1 text-xs font-semibold rounded-lg': getTrangThaiHienTai(selectedPhieu) === 'Không xác định'
                    }">
                      {{ getTrangThaiHienTai(selectedPhieu) }}
                    </span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div v-if="selectedPhieu.loaiApDung === 'Khách hàng cụ thể'">
            <table class="w-full text-sm">
              <tbody>
                <tr>
                  <td class="p-2 font-semibold">Loại áp dụng</td>
                  <td class="bg-orange-100 text-orange-700 px-2 py-1 text-xs font-semibold rounded-lg">Khách hàng cụ thể</td>
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
                <input type="date" v-model="filterFromDate" id="date-from" class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500" />
                <label for="date-from" class="absolute left-2 top-2 text-gray-500 text-xs transition-all duration-300">Chọn ngày bắt đầu</label>
              </div>
              <div class="relative w-[150px]">
                <input type="date" v-model="filterToDate" id="date-to" class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500" />
                <label for="date-to" class="absolute left-2 top-2 text-gray-500 text-xs transition-all duration-300">Chọn ngày kết thúc</label>
              </div>
              <select v-model="filterLoaiPhieu" class="block w-[150px] h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500">
                <option value="">Tất cả loại phiếu</option>
                <option value="Giảm theo phần trăm">Giảm theo phần trăm</option>
                <option value="Giảm theo giá trị tiền">Giảm theo giá trị tiền</option>
              </select>
              <select v-model="filterLoaiApDung" class="block w-[150px] h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500">
                <option value="">Tất cả cách áp dụng</option>
                <option value="Công khai">Công khai</option>
                <option value="Khách hàng cụ thể">Khách hàng cụ thể</option>
              </select>
              <select v-model="filterStatus" class="block w-[150px] h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500">
                <option value="">Tất cả tình trạng</option>
                <option value="Hoạt động">Hoạt động</option>
                <option value="Đang chờ">Đang chờ</option>
                <option value="Hết hạn">Hết hạn</option>
              </select>
              <input v-model="searchQuery" type="text" placeholder="Tìm kiếm..." class="block w-[345px] h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500" />
            </div>
            <button type="button" @click="exportToExcel" class="flex items-center gap-2 text-white bg-gradient-to-r from-green-600 via-green-700 to-green-800 hover:from-green-700 hover:via-green-800 hover:to-green-900 focus:ring-4 focus:outline-none focus:ring-green-400 dark:focus:ring-green-900 font-medium rounded-lg text-sm px-5 py-2.5 text-center mt-5">
              <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" viewBox="0,0,256,256">
                <g fill="#ffffff">
                  <g transform="scale(5.12,5.12)">
                    <path d="M16,4c-1.65,0 -3,1.35 -3,3v4h2v-4c0,-0.55 0.45,-1 1,-1h14v8h-3.49023c0.29,0.61 0.46047,1.28 0.48047,2h3.00977v8h-3v2h3v8h-3.00977c-0.02,0.72 -0.19047,1.39 -0.48047,2h3.49023v8h-14c-0.55,0 -1,-0.45 -1,-1v-4h-2v4c0,1.65 1.35,3 3,3h30c1.65,0 3,-1.35 3,-3v-36c0,-1.65 -1.35,-3 -3,-3zM32,6h14c0.55,0 1,0.45 1,1v7h-15zM4.19922,13c-1.75547,0 -3.19922,1.44375 -3.19922,3.19922v17.60156c0,1.75547 1.44375,3.19922 3.19922,3.19922h17.60156c1.75547,0 3.19922,-1.44375 3.19922,-3.19922v-17.60156c0,-1.75547 -1.44375,-3.19922 -3.19922,-3.19922zM4.19922,15h17.60156c0.67453,0 1.19922,0.52468 1.19922,1.19922v17.60156c0,0.67453 -0.52469,1.19922 -1.19922,1.19922h-17.60156c-0.67453,0 -1.19922,-0.52468 -1.19922,-1.19922v-17.60156c0,-0.67453 0.52469,-1.19922 1.19922,-1.19922zM32,16h15v8h-15zM7.96875,19l3.49414,5.97852l-3.83203,6.02148h2.86328l2.52148,-4.7168l2.5332,4.7168h2.82031l-3.76953,-6l3.68555,-6h-2.67578l-2.45508,4.50586l-2.32422,-4.50586zM32,26h15v8h-15zM32,36h15v7c0,0.55 -0.45,1 -1,1h-14z"></path>
                  </g>
                </g>
              </svg>
              Xuất Excel
            </button>
            <button @click="goToAdd" type="button" class="flex items-center gap-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 dark:focus:ring-blue-800 shadow-lg font-medium rounded-lg text-sm px-5 py-2.5 mt-5">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4">
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
                <th scope="col" class="px-6 py-3 cursor-pointer">
                  #
                </th>
                <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('maPhieuGiamGia')">
                  Mã phiếu
                  <span v-if="sortKey === 'maPhieuGiamGia'" class="ml-1">
                    <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortDirection === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                    </svg>
                  </span>
                </th>
                <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('tenPhieuGiamGia')">
                  Tên phiếu
                  <span v-if="sortKey === 'tenPhieuGiamGia'" class="ml-1">
                    <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortDirection === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                    </svg>
                  </span>
                </th>
                <!-- Loại áp dụng: Bỏ sort -->
                <th scope="col" class="px-6 py-3">
                  Loại áp dụng
                </th>
                <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('moTa')">
                  Mô tả
                  <span v-if="sortKey === 'moTa'" class="ml-1">
                    <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortDirection === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                    </svg>
                  </span>
                </th>
                <!-- Loại phiếu: Bỏ sort -->
                <th scope="col" class="px-6 py-3">
                  Loại phiếu
                </th>
                <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('giaTriGiam')">
                  Giá trị giảm
                  <span v-if="sortKey === 'giaTriGiam'" class="ml-1">
                    <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortDirection === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                    </svg>
                  </span>
                </th>
                <th scope="col" class="px-6 py-3 cursor-pointer" @click="sort('soLuong')">
                  Số lượng
                  <span v-if="sortKey === 'soLuong'" class="ml-1">
                    <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortDirection === 'asc' ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                    </svg>
                  </span>
                </th>
                <!-- Tình trạng: Bỏ sort -->
                <th scope="col" class="px-6 py-3">
                  Tình trạng
                </th>
                <th scope="col" class="px-6 py-3">Trạng thái</th>
                <th scope="col" class="px-6 py-3">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr class="bg-white border-b hover:bg-gray-50" v-for="(phieu, index) in filteredPhieuGiamGias" :key="phieu.id">
                <td class="px-6 py-4">{{ (currentPage * itemsPerPage) + index + 1 }}</td>
                <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">{{ phieu.maPhieuGiamGia }}</td>
                <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">{{ phieu.tenPhieuGiamGia }}</td>
                <td class="px-6 py-4 text-center">
                  <span :class="{
                    'bg-orange-100 text-orange-700 px-2 py-1 text-xs font-semibold rounded-lg': phieu.loaiApDung === 'Khách hàng cụ thể',
                    'bg-blue-100 text-blue-700 px-2 py-1 text-xs font-semibold rounded-lg': phieu.loaiApDung === 'Công khai'
                  }">
                    {{ phieu.loaiApDung }}
                  </span>
                </td>
                <td class="px-6 py-4">{{ phieu.moTa }}</td>
                <td class="px-6 py-4 text-center">
                  <span :class="{
                    'bg-blue-100 text-blue-700': phieu.discountDisplay === 'Giảm theo phần trăm',
                    'bg-purple-100 text-purple-700': phieu.discountDisplay === 'Giảm theo giá trị tiền',
                    'bg-gray-100 text-gray-700': phieu.discountDisplay === 'Không xác định'
                  }" class="px-2 py-1 text-xs font-semibold rounded-lg">
                    {{ phieu.discountDisplay }}
                  </span>
                </td>
                <td class="px-6 py-4 text-center text-sm font-medium text-gray-900">{{ phieu.discountValue }}</td>
                <td class="px-6 py-4">{{ phieu.soLuong }}</td>
                <td class="px-6 py-4 text-center">
                  <span :class="{
                    'bg-green-100 text-green-700 px-2 py-1 text-xs font-semibold rounded-lg': phieu.trangThaiPhieu === 'Hoạt động',
                    'bg-yellow-100 text-yellow-700 px-2 py-1 text-xs font-semibold rounded-lg': phieu.trangThaiPhieu === 'Đang chờ',
                    'bg-red-100 text-red-700 px-2 py-1 text-xs font-semibold rounded-lg': phieu.trangThaiPhieu === 'Hết hạn',
                    'bg-gray-100 text-gray-700 px-2 py-1 text-xs font-semibold rounded-lg': phieu.trangThaiPhieu === 'Không xác định'
                  }">
                    {{ phieu.trangThaiPhieu }}
                  </span>
                </td>
                <td class="px-6 py-4 text-center">
                  <div class="flex items-center justify-center">
                    <label class="flex items-center cursor-pointer select-none text-dark dark:text-white">
                      <div class="relative">
                        <input type="checkbox" :checked="phieu.trangThai === true || phieu.trangThai === 1" @change="toggleStatus(phieu)" class="sr-only" />
                        <div :class="['block h-8 rounded-full w-14 transition-colors duration-300 ease-in-out', (phieu.trangThai === true || phieu.trangThai === 1) ? 'bg-blue-50' : 'bg-red-100']"></div>
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
              <tr class="bg-white border-b hover:bg-gray-50" v-if="filteredPhieuGiamGias.length == 0">
                <td class="px-6 py-4" colspan="6" align="center">Không có dữ liệu</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="flex justify-center items-center mt-4 space-x-2">
          <button @click="prevPage" :disabled="currentPage <= 0" class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 dark:focus:ring-blue-800 shadow-lg shadow-blue-500/50 dark:shadow-lg dark:shadow-blue-800/80 font-medium rounded-lg text-sm p-2 text-center flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-4 h-4">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5 8.25 12l7.5-7.5" />
            </svg>
          </button>
          <span class="text-xs font-semibold text-gray-700">
            Trang {{ currentPage + 1 }} / {{ totalPages }}
          </span>
          <button @click="nextPage" :disabled="currentPage >= totalPages - 1" class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 dark:focus:ring-blue-800 shadow-lg shadow-blue-500/50 dark:shadow-lg dark:shadow-blue-800/80 font-medium rounded-lg text-sm p-2 text-center flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-4 h-4">
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
      currentPage: 0,
      totalPages: 1,
      itemsPerPage: 12,
      searchQuery: "",
      filterStatus: "",
      filterLoaiPhieu: "",
      filterLoaiApDung: "",
      filterFromDate: "",
      filterToDate: "",
      showModalDetail: false,
      selectedPhieu: null,
      sortKey: 'id', // Mặc định sắp xếp theo id
      sortDirection: 'asc', // Mặc định tăng dần
      showToast: false,
      toastMessage: '',
      toastType: 'success',
      showConfirmModal: false,
      confirmActionCallback: null,
    };
  },

  computed: {
    phieuGiamGiasWithStatus() {
      return this.phieuGiamGias.map(phieu => {
        const now = new Date();
        const start = new Date(phieu.ngayBatDau);
        const end = new Date(phieu.ngayKetThuc);

        if (!phieu.ngayBatDau || !phieu.ngayKetThuc) {
          phieu.trangThaiPhieu = 'Không xác định';
        } else if (now < start) {
          phieu.trangThaiPhieu = 'Đang chờ';
        } else if (now >= start && now <= end) {
          phieu.trangThaiPhieu = 'Hoạt động';
        } else {
          phieu.trangThaiPhieu = 'Hết hạn';
        }

        if (phieu.soPhanTramGiam > 0) {
          phieu.discountDisplay = 'Giảm theo phần trăm';
          phieu.discountValue = `${phieu.soPhanTramGiam}%`;
        } else if (phieu.giaTriGiam > 0) {
          phieu.discountDisplay = 'Giảm theo giá trị tiền';
          phieu.discountValue = new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
          }).format(phieu.giaTriGiam);
        } else {
          phieu.discountDisplay = 'Không xác định';
          phieu.discountValue = 'Không xác định';
        }

        phieu.loaiApDung = phieu.khachHangId ? 'Khách hàng cụ thể' : 'Công khai';
        return phieu;
      });
    },

    filteredPhieuGiamGias() {
      let result = [...this.phieuGiamGiasWithStatus];

      if (this.filterStatus) {
        result = result.filter(p => p.trangThaiPhieu === this.filterStatus);
      }
      if (this.filterLoaiPhieu) {
        result = result.filter(p => p.discountDisplay === this.filterLoaiPhieu);
      }
      if (this.filterLoaiApDung) {
        result = result.filter(p => p.loaiApDung === this.filterLoaiApDung);
      }
      if (this.filterFromDate) {
        const from = new Date(this.filterFromDate);
        result = result.filter(p => new Date(p.ngayBatDau) >= from);
      }
      if (this.filterToDate) {
        const to = new Date(this.filterToDate);
        result = result.filter(p => new Date(p.ngayKetThuc) <= to);
      }
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase();
        result = result.filter(p => 
          Object.values(p).some(v => (v ?? '').toString().toLowerCase().includes(query))
        );
      }

      return result;
    },
  },

  methods: {
    async fetchData(page = 0) {
      try {
        const res = await phieuGiamGiaService.getAll(page, this.itemsPerPage, this.sortKey, this.sortDirection);
        const data = res.data;

        this.phieuGiamGias = data.content || [];
        this.totalPages = data.page.totalPages || 1;
        this.currentPage = data.page.number || page;

        console.log('API Response:', data);
        console.log('Current Page:', this.currentPage);
        console.log('Total Pages:', this.totalPages);
        console.log('Items:', this.phieuGiamGias);
      } catch (error) {
        console.error("Lỗi khi tải dữ liệu:", error);
        this.showToastMessage("Lỗi khi tải dữ liệu: " + (error.response?.data || error.message), "error");
      }
    },

    sort(key) {
      if (this.sortKey === key) {
        this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
      } else {
        this.sortKey = key;
        this.sortDirection = 'asc';
      }
      this.currentPage = 0;
      this.fetchData(0);
    },

    nextPage() {
      if (this.currentPage < this.totalPages - 1) {
        this.currentPage++;
        this.fetchData(this.currentPage);
      }
    },

    prevPage() {
      if (this.currentPage > 0) {
        this.currentPage--;
        this.fetchData(this.currentPage);
      }
    },

    showDetail(phieu) {
      this.selectedPhieu = { ...phieu };
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
          newStatus ? "Đã kích hoạt phiếu giảm giá!" : "Đã ngưng hoạt động phiếu giảm giá!",
          "success"
        );
        this.fetchData(this.currentPage);
      } catch (error) {
        console.error("Lỗi khi cập nhật trạng thái:", error);
        this.showToastMessage("Lỗi khi cập nhật trạng thái: " + (error.response?.data || error.message), "error");
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

    getTrangThaiHienTai(phieu) {
      if (!phieu?.ngayBatDau || !phieu?.ngayKetThuc) return "Không xác định";
      const now = new Date();
      const ngayBatDau = new Date(phieu.ngayBatDau);
      const ngayKetThuc = new Date(phieu.ngayKetThuc);

      if (now >= ngayBatDau && now <= ngayKetThuc) return "Hoạt động";
      else if (now < ngayBatDau) return "Đang chờ";
      else return "Hết hạn";
    },

    formatDate(dateStr) {
      if (!dateStr) return "";
      const date = new Date(dateStr);
      const pad = (num) => num.toString().padStart(2, '0');
      return `${pad(date.getDate())}/${pad(date.getMonth() + 1)}/${date.getFullYear()} ${pad(date.getHours())}:${pad(date.getMinutes())}`;
    },

    exportToExcel() {
      const data = this.filteredPhieuGiamGias.map(item => ({
        'Mã Phiếu': item.maPhieuGiamGia,
        'Tên Phiếu': item.tenPhieuGiamGia,
        'Mô Tả': item.moTa,
        'Loại Phiếu': item.discountDisplay,
        'Giá Trị Giảm': item.discountValue,
        'Số Lượng': item.soLuong,
        'Ngày Bắt Đầu': this.formatDate(item.ngayBatDau),
        'Ngày Kết Thúc': this.formatDate(item.ngayKetThuc),
        'Tình Trạng': item.trangThaiPhieu,
        'Loại Áp Dụng': item.loaiApDung,
      }));

      const ws = XLSX.utils.json_to_sheet(data);
      const wscols = [
        { wch: 15 }, { wch: 20 }, { wch: 40 }, { wch: 25 }, { wch: 15 },
        { wch: 10 }, { wch: 20 }, { wch: 20 }, { wch: 15 }, { wch: 20 }
      ];
      ws['!cols'] = wscols;

      const headerStyle = {
        font: { bold: true, color: { rgb: 'FFFFFF' }, sz: 12 },
        fill: { patternType: "solid", fgColor: { rgb: "4CAF50" } },
        alignment: { horizontal: "center", vertical: "center" }
      };

      ['A1', 'B1', 'C1', 'D1', 'E1', 'F1', 'G1', 'H1', 'I1', 'J1'].forEach(cell => {
        if (ws[cell]) ws[cell].s = headerStyle;
      });

      const wb = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, 'Phiếu Giảm Giá');
      XLSX.writeFile(wb, 'Phieu_Giam_Gia.xlsx');
    },

    // Thêm phương thức cho modal xác nhận (nếu cần xóa)
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
  },

  mounted() {
    this.fetchData(0);
  },

  watch: {
    filterStatus() { this.fetchData(0); this.currentPage = 0; },
    filterLoaiPhieu() { this.fetchData(0); this.currentPage = 0; },
    filterLoaiApDung() { this.fetchData(0); this.currentPage = 0; },
    filterFromDate() { this.fetchData(0); this.currentPage = 0; },
    filterToDate() { this.fetchData(0); this.currentPage = 0; },
    searchQuery() { this.fetchData(0); this.currentPage = 0; },
  },
};
</script>

<style>
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

input:checked + .block {
  background-color: rgba(59, 130, 246, 0.2);
}

input:not(:checked) + .block {
  background-color: rgba(229, 231, 235, 0.8);
}
</style>