<template>
  <div class="w-full font-roboto">
    <!-- Loading Overlay -->
    <div v-if="isLoading" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div class="bg-white p-6 rounded-lg shadow-xl flex flex-col items-center overflow-hidden">
        <div class="loading-spinner mb-4"></div>
        <p class="text-gray-700 font-medium">Đang xử lý yêu cầu...</p>
        <p class="text-gray-500 text-sm mt-2">Vui lòng đợi trong giây lát</p>
      </div>
    </div>

    <!-- Toast Notification -->
    <div v-if="showToast" class="fixed top-4 right-4 z-50">
      <div :class="[
        'p-4 rounded-lg shadow-lg text-white flex items-center gap-2',
        toastType === 'success' ? 'bg-green-500' : toastType === 'error' ? 'bg-red-500' : 'bg-yellow-500'
      ]">
        <span>{{ toastMessage }}</span>
        <button @click="showToast = false" class="ml-2 text-white hover:text-gray-200">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>
    </div>

    <div class="min-h-[calc(100vh-56px)] w-full bg-white p-10 rounded flex flex-col">
      <!-- Track step -->
      <div class="flex items-center justify-between mb-8">
        <button @click="goBack()"
          class="flex-shrink-0 flex items-center gap-1 text-sm text-gray-600 hover:text-blue-600 font-medium">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"
            stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7" />
          </svg>
          Quay lại
        </button>

        <div class="flex-1 flex justify-center">
          <ol class="flex items-center gap-2 text-sm font-medium text-gray-500 sm:text-base">
            <li :class="[
              'flex items-center',
              step >= 1 ? 'text-blue-600' : 'text-gray-400',
              'after:content-[\'\'] after:w-6 after:h-1 after:inline-block after:mx-2 after:border-b',
              step > 1 ? 'after:border-blue-600' : 'after:border-gray-200'
            ]">
              <span class="flex items-center">
                <svg class="w-5 h-5 mr-2 text-blue-600" viewBox="0 0 20 20" fill="currentColor">
                  <path
                    d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 8.207-4 4a1 1 0 0 1-1.414 0l-2-2a1 1 0 0 1 1.414-1.414L9 10.586l3.293-3.293a1 1 0 0 1 1.414 1.414Z" />
                </svg>
                Chọn loại phiếu
              </span>
            </li>
            <li :class="[
              'flex items-center',
              step >= 2 ? 'text-blue-600' : 'text-gray-400',
              'after:content-[\'\'] after:w-6 after:h-1 after:inline-block after:mx-2 after:border-b',
              step > 2 ? 'after:border-blue-600' : 'after:border-gray-200'
            ]">
              <span class="flex items-center">
                <svg v-if="step >= 2" class="w-5 h-5 mr-2 text-blue-600" viewBox="0 0 20 20" fill="currentColor">
                  <path
                    d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 8.207-4 4a1 1 0 0 1-1.414 0l-2-2a1 1 0 0 1 1.414-1.414L9 10.586l3.293-3.293a1 1 0 0 1 1.414 1.414Z" />
                </svg>
                <svg v-else class="w-5 h-5 mr-2 text-gray-400" viewBox="0 0 20 20" fill="currentColor">
                  <path
                    d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 8.207-4 4a1 1 0 0 1-1.414 0l-2-2a1 1 0 0 1 1.414-1.414L9 10.586l3.293-3.293a1 1 0 0 1 1.414 1.414Z" />
                </svg>
                Chọn cách áp dụng
              </span>
            </li>
            <li :class="['flex items-center', step >= 3 ? 'text-blue-600' : 'text-gray-400']">
              <span class="flex items-center">
                <svg v-if="step >= 3" class="w-5 h-5 mr-2 text-blue-600" viewBox="0 0 20 20" fill="currentColor">
                  <path
                    d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 8.207-4 4a1 1 0 0 1-1.414 0l-2-2a1 1 0 0 1 1.414-1.414L9 10.586l3.293-3.293a1 1 0 0 1 1.414 1.414Z" />
                </svg>
                <svg v-else class="w-5 h-5 mr-2 text-gray-400" viewBox="0 0 20 20" fill="currentColor">
                  <path
                    d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 8.207-4 4a1 1 0 0 1-1.414 0l-2-2a1 1 0 0 1 1.414-1.414L9 10.586l3.293-3.293a1 1 0 0 1 1.414 1.414Z" />
                </svg>
                {{ isEdit ? 'Chỉnh sửa phiếu giảm giá' : 'Thêm phiếu giảm giá' }}
              </span>
            </li>
          </ol>
        </div>
      </div>

      <!-- Transition bao quanh các bước -->
      <transition name="fade-slide" mode="out-in">
        <div :key="step">
          <!-- Step 1 -->
          <div v-if="step === 1"
            class="flex-1 flex flex-col items-center justify-center gap-12 min-h-[calc(100vh-350px)]">
            <h2 class="text-2xl md:text-3xl font-bold text-gray-800 tracking-wide">Chọn loại phiếu giảm giá</h2>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-10 max-w-4xl w-full px-4">
              <div @click="chonLoai('phanTram')"
                class="cursor-pointer bg-white border border-gray-300 rounded-2xl shadow-lg p-10 flex flex-col items-center text-center hover:scale-105 transition-all duration-300 hover:bg-gradient-to-tl hover:from-[#003366] hover:to-[#242124] group">
                <img src="https://cdn-icons-png.flaticon.com/128/4334/4334638.png" alt="Phần trăm"
                  class="w-24 h-24 mb-5 drop-shadow transition-all duration-300" />
                <h3 class="text-2xl font-semibold text-gray-800 group-hover:text-white transition-colors duration-300">
                  Giảm theo phần trăm</h3>
                <p class="text-base text-gray-600 mt-3 group-hover:text-white transition-colors duration-300">
                  Áp dụng mức giảm phần trăm cho đơn hàng.
                </p>
              </div>
              <div @click="chonLoai('giaTri')"
                class="cursor-pointer bg-white border border-gray-300 rounded-2xl shadow-lg p-10 flex flex-col items-center text-center hover:scale-105 transition-all duration-300 hover:bg-gradient-to-tl hover:from-[#003366] hover:to-[#242124] group">
                <img src="https://cdn-icons-png.flaticon.com/128/639/639365.png" alt="Giá trị tiền"
                  class="w-24 h-24 mb-5 drop-shadow transition-all duration-300" />
                <h3 class="text-2xl font-semibold text-gray-800 group-hover:text-white transition-colors duration-300">
                  Giảm theo giá trị tiền</h3>
                <p class="text-base text-gray-600 mt-3 group-hover:text-white transition-colors duration-300">
                  Áp dụng mức giảm cố định cho đơn hàng.
                </p>
              </div>
            </div>
          </div>

          <!-- Step 2 -->
          <div v-else-if="step === 2"
            class="flex-1 flex flex-col items-center justify-center gap-8 min-h-[calc(100vh-350px)]">
            <h2 class="text-2xl md:text-3xl font-bold text-gray-800 tracking-wide">Chọn cách áp dụng phiếu giảm giá</h2>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-10 max-w-4xl w-full px-4">
              <div @click="chonApDung('congKhai')"
                class="cursor-pointer bg-white border border-gray-300 rounded-2xl shadow-lg p-10 flex flex-col items-center text-center hover:scale-105 transition-all duration-300 hover:bg-gradient-to-tl hover:from-[#003366] hover:to-[#242124] group">
                <svg class="w-16 h-16 mb-5 text-blue-600 group-hover:text-white" fill="currentColor"
                  viewBox="0 0 20 20">
                  <path
                    d="M10 2a8 8 0 100 16 8 8 0 000-16zm3.5 8.5h-3v3a.5.5 0 01-1 0v-3h-3a.5.5 0 010-1h3v-3a.5.5 0 011 0v3h3a.5.5 0 010 1z" />
                </svg>
                <h3 class="text-2xl font-semibold text-gray-800 group-hover:text-white transition-colors duration-300">
                  Phiếu công khai</h3>
                <p class="text-base text-gray-600 mt-3 group-hover:text-white transition-colors duration-300">
                  Áp dụng cho mọi khách hàng.
                </p>
              </div>
              <div @click="chonApDung('khachHang')"
                class="cursor-pointer bg-white border border-gray-300 rounded-2xl shadow-lg p-10 flex flex-col items-center text-center hover:scale-105 transition-all duration-300 hover:bg-gradient-to-tl hover:from-[#003366] hover:to-[#242124] group">
                <svg class="w-16 h-16 mb-5 text-blue-600 group-hover:text-white" fill="currentColor"
                  viewBox="0 0 20 20">
                  <path d="M10 2a8 8 0 100 16 8 8 0 000-16zm-1 11H7v-1h2v1zm4 0h-2v-1h2v1zm0-3h-6V7h6v3z" />
                </svg>
                <h3 class="text-2xl font-semibold text-gray-800 group-hover:text-white transition-colors duration-300">
                  Áp dụng theo khách hàng</h3>
                <p class="text-base text-gray-600 mt-3 group-hover:text-white transition-colors duration-300">
                  Chọn khách hàng cụ thể nhận ưu đãi.
                </p>
              </div>
            </div>
          </div>

          <!-- Step 3 -->
          <div v-else-if="step === 3" class="space-y-4">
            <div class="relative mb-6">
              <h1 class="text-2xl font-bold text-center mb-4">
                {{ newPhieu?.id ? 'Sửa Phiếu Giảm Giá' : 'Thêm Phiếu Giảm Giá' }}
              </h1>
            </div>
            <div class="flex flex-col lg:flex-row gap-6 items-start">
              <form @submit.prevent="handleSubmit" class="flex-1 space-y-4">
                <div class="grid grid-cols-2 gap-4 mb-4">
                  <div class="col-span-2 sm:col-span-1">
                    <label for="maPhieuGiamGia" class="block mb-2 text-sm font-medium text-gray-900">Mã phiếu</label>
                    <input v-model="newPhieu.maPhieuGiamGia" type="text" name="maPhieuGiamGia" id="maPhieuGiamGia"
                      class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                      placeholder="Mã phiếu" />
                  </div>
                  <div class="col-span-2 sm:col-span-1">
                    <label for="tenPhieuGiamGia" class="block mb-2 text-sm font-medium text-gray-900">Tên phiếu</label>
                    <input v-model="newPhieu.tenPhieuGiamGia" type="text" name="tenPhieuGiamGia" id="tenPhieuGiamGia"
                      class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                      placeholder="Tên phiếu" />
                    <p v-if="errors.tenPhieuGiamGia" class="text-red-500 text-xs mt-1">{{ errors.tenPhieuGiamGia }}</p>
                  </div>
                  <div class="col-span-2 sm:col-span-1" v-if="loaiGiamGia === 'phanTram'">
                    <label for="soPhanTramGiam" class="block mb-2 text-sm font-medium text-gray-900">Phần trăm
                      giảm</label>
                    <input v-model="newPhieu.soPhanTramGiam" type="number" name="soPhanTramGiam" id="soPhanTramGiam"
                      class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                      placeholder="% giảm" required />
                    <p v-if="errors.soPhanTramGiam" class="text-red-500 text-xs mt-1">{{ errors.soPhanTramGiam }}</p>
                  </div>
                  <div class="col-span-2 sm:col-span-1" v-if="loaiGiamGia === 'giaTri'">
                    <label for="giaTriGiam" class="block mb-2 text-sm font-medium text-gray-900">Giá trị giảm</label>
                    <input v-model="newPhieu.giaTriGiam" type="number" name="giaTriGiam" id="giaTriGiam"
                      class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                      placeholder="Giá trị giảm" required />
                    <p v-if="errors.giaTriGiam" class="text-red-500 text-xs mt-1">{{ errors.giaTriGiam }}</p>
                  </div>
                  <div class="col-span-2 sm:col-span-1">
                    <label for="giaTriDonHangToiThieu" class="block mb-2 text-sm font-medium text-gray-900">Giá trị đơn
                      giảm tối thiểu</label>
                    <input v-model="newPhieu.giaTriDonHangToiThieu" type="number" name="giaTriDonHangToiThieu"
                      id="giaTriDonHangToiThieu"
                      class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                      placeholder="Giá trị đơn hàng tối thiểu" required />
                    <p v-if="errors.giaTriDonHangToiThieu" class="text-red-500 text-xs mt-1">{{
                      errors.giaTriDonHangToiThieu }}</p>
                  </div>
                  <div class="col-span-2 sm:col-span-1">
                    <label for="soLuong" class="block mb-2 text-sm font-medium text-gray-900">Số lượng</label>
                    <input v-model="newPhieu.soLuong" type="number" name="soLuong" id="soLuong"
                      class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                      placeholder="Số lượng" required />
                    <p v-if="errors.soLuong" class="text-red-500 text-xs mt-1">{{ errors.soLuong }}</p>
                  </div>
                  <div class="col-span-2 sm:col-span-1">
                    <label for="ngayBatDau" class="block mb-2 text-sm font-medium text-gray-900">Ngày bắt đầu</label>
                    <input v-model="newPhieu.ngayBatDau" type="datetime-local" name="ngayBatDau" id="ngayBatDau"
                      class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                      required />
                    <p v-if="errors.ngayBatDau" class="text-red-500 text-xs mt-1">{{ errors.ngayBatDau }}</p>
                  </div>
                  <div class="col-span-2 sm:col-span-1">
                    <label for="ngayKetThuc" class="block mb-2 text-sm font-medium text-gray-900">Ngày kết thúc</label>
                    <input v-model="newPhieu.ngayKetThuc" type="datetime-local" name="ngayKetThuc" id="ngayKetThuc"
                      class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                      required />
                    <p v-if="errors.ngayKetThuc" class="text-red-500 text-xs mt-1">{{ errors.ngayKetThuc }}</p>
                  </div>
                  <div class="col-span-2">
                    <label for="moTa" class="block mb-2 text-sm font-medium text-gray-900">Mô tả</label>
                    <textarea v-model="newPhieu.moTa" id="moTa" rows="4"
                      class="block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500"
                      placeholder="Nhập mô tả"></textarea>
                    <p v-if="errors.moTa" class="text-red-500 text-xs mt-1">{{ errors.moTa }}</p>
                  </div>
                  
                </div>
                <button type="button" @click="showConfirmModal = true"
                  class="flex items-center gap-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg font-medium rounded-lg text-sm px-5 py-2.5 text-center mb-2">
                  <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                    <path fill-rule="evenodd"
                      d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z"
                      clip-rule="evenodd"></path>
                  </svg>
                  {{ newPhieu.id ? "Cập nhật phiếu giảm giá" : "Thêm phiếu giảm giá" }}
                </button>
              </form>

              <!-- Modal Xác Nhận -->
              <div v-if="showConfirmModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
                <div class="bg-white rounded-lg p-6 max-w-md w-full mx-4">
                  <h3 class="text-lg font-semibold mb-4">Xác nhận {{ newPhieu.id ? "cập nhật" : "thêm" }} phiếu giảm giá
                  </h3>
                  <p class="text-gray-600 mb-6">Bạn có chắc chắn muốn {{ newPhieu.id ? "cập nhật" : "thêm" }} phiếu giảm
                    giá này không?</p>
                  <div class="flex justify-end gap-3">
                    <button @click="showConfirmModal = false"
                      class="px-4 py-2 text-gray-600 hover:text-gray-800 font-medium">Hủy</button>
                    <button @click="confirmSubmit"
                      class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-medium">Xác
                      nhận</button>
                  </div>
                </div>
              </div>

              <!-- Bảng khách hàng -->
              <div v-if="loaiApDung === 'khachHang'" class="flex-1 pl-6">
                <label class="block mb-2 text-sm font-medium text-gray-900">Chọn khách hàng áp dụng</label>
                <div class="mb-4">
                  <input v-model="searchQuery" type="text" placeholder="Tìm kiếm khách hàng..."
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5" />
                </div>
                <div class="max-h-[400px] overflow-y-auto border rounded-lg p-4">
                  <table class="min-w-full text-sm text-center">
                    <thead>
                      <tr>
                        <th class="px-4 py-2 text-center">Chọn</th>
                        <th class="px-4 py-2 text-center">STT</th>
                        <th class="px-4 py-2 text-center">Mã</th>
                        <th class="px-4 py-2 text-center">Họ tên</th>
                        <th class="px-4 py-2 text-center">GT</th>
                        <th class="px-4 py-2 text-center">Email</th>
                        <th class="px-4 py-2 text-center">SĐT</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="(kh, index) in paginatedKhachHang" :key="kh.id" class="hover:bg-gray-50 cursor-pointer"
                        @click="khachHangDaChon = kh.id">
                        <td class="px-4 py-2">
                          <input type="radio" :value="kh.id" v-model="khachHangDaChon" class="form-radio text-blue-600"
                            name="chonKhachHang" @click.stop />
                        </td>
                        <td class="px-4 py-2">{{ currentPage * itemsPerPage + index + 1 }}</td>
                        <td class="px-4 py-2">{{ kh.maKhachHang }}</td>
                        <td class="px-4 py-2">{{ kh.hoTen }}</td>
                        <td class="px-4 py-2">{{ kh.gioiTinh ? 'Nam' : 'Nữ' }}</td>
                        <td class="px-4 py-2">{{ kh.email }}</td>
                        <td class="px-4 py-2">{{ kh.soDienThoai }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
                <div class="flex justify-center items-center mt-4 space-x-2">
                  <button @click="prevPage" :disabled="currentPage <= 0"
                    class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg font-medium rounded-lg text-sm p-2 flex items-center justify-center disabled:opacity-50">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2"
                      stroke="currentColor" class="w-4 h-4">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5 8.25 12l7.5-7.5" />
                    </svg>
                  </button>
                  <span class="text-xs font-semibold text-gray-700">Trang {{ currentPage + 1 }} / {{ totalPages
                    }}</span>
                  <button @click="nextPage" :disabled="currentPage >= totalPages - 1"
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
        </div>
      </transition>
    </div>
  </div>
</template>
<script>
import KhachHangService from '../../service/KhachHangService';


export default {
  name: 'PhieuGiamGiaForm',
  props: ['id'],
  data() {
    return {
      step: 1,
      loaiGiamGia: '',
      loaiApDung: '',
      danhSachKhachHang: [],
      searchQuery: '',
      currentPage: 0,
      itemsPerPage: 6,
      khachHangDaChon: null,
      showConfirmModal: false,
      newPhieu: {
        id: null,
        maPhieuGiamGia: '',
        tenPhieuGiamGia: '',
        soPhanTramGiam: 0,
        giaTriGiam: 0,
        giaTriDonHangToiThieu: 0,
        soLuong: 0,
        ngayBatDau: '',
        ngayKetThuc: '',
        moTa: '',
        trangThai: true,
        khachHangId: null,
      },
      errors: {},
      isLoading: false,
      showToast: false,
      toastMessage: '',
      toastType: 'success',
    };
  },
  computed: {
    filteredKhachHang() {
      if (!this.searchQuery) return this.danhSachKhachHang;
      const query = this.searchQuery.toLowerCase();
      return this.danhSachKhachHang.filter(kh => {
        const gender = typeof kh.gioiTinh === 'string' ? kh.gioiTinh.toLowerCase() : (kh.gioiTinh ? 'nam' : 'nữ');
        return (
          (kh.hoTen?.toLowerCase().includes(query)) ||
          (kh.email?.toLowerCase().includes(query)) ||
          (kh.soDienThoai?.toLowerCase().includes(query)) ||
          (kh.maKhachHang?.toLowerCase().includes(query)) ||
          gender.includes(query)
        );
      });
    },
    totalPages() {
      return Math.ceil(this.filteredKhachHang.length / this.itemsPerPage) || 1;
    },
    paginatedKhachHang() {
      const start = this.currentPage * this.itemsPerPage;
      return this.filteredKhachHang.slice(start, start + this.itemsPerPage);
    },
    isEdit() {
      return !!this.id;
    },
  },
  async mounted() {
    try {
      this.danhSachKhachHang = await KhachHangService.getAll();
    } catch (error) {
      console.error('Lỗi khi lấy dữ liệu khách hàng:', error);
      this.showNotification('Không thể tải danh sách khách hàng.', 'error');
    }
  },
  created() {
    if (this.isEdit) {
      this.fetchPhieuGiamGia();
    }
  },
  watch: {
    searchQuery() {
      this.currentPage = 0;
    },
  },
  methods: {
    async fetchPhieuGiamGia() {
      try {
        const response = await fetch(`http://localhost:8080/api/admin/phieu-giam-gia/${this.id}`);
        if (!response.ok) throw new Error('Không thể tải dữ liệu phiếu giảm giá');
        const data = await response.json();
        const formatInputDateTime = (dateString) => new Date(dateString).toISOString().slice(0, 16);
        this.newPhieu = {
          ...data,
          ngayBatDau: formatInputDateTime(data.ngayBatDau),
          ngayKetThuc: formatInputDateTime(data.ngayKetThuc),
          trangThai: data.trangThai ? 'true' : 'false',
          khachHangId: data.khachHangId || null,
        };
        this.loaiGiamGia = data.soPhanTramGiam > 0 ? 'phanTram' : 'giaTri';
        this.loaiApDung = data.khachHangId ? 'khachHang' : 'congKhai';
        this.khachHangDaChon = data.khachHangId || null;
        this.step = 1;
      } catch (error) {
        console.error('Lỗi lấy dữ liệu:', error);
        this.showNotification('Không thể tải dữ liệu phiếu giảm giá.', 'error');
      }
    },
    prevPage() {
      if (this.currentPage > 0) this.currentPage--;
    },
    nextPage() {
      if (this.currentPage < this.totalPages - 1) this.currentPage++;
    },
    goBack() {
      if (this.step > 1) {
        this.step--;
      } else {
        this.$router.push('/admin/phieu-giam-gia');
      }
    },
    chonLoai(loai) {
      this.loaiGiamGia = loai;
      if (loai === 'phanTram') {
        this.newPhieu.giaTriGiam = 0;
      } else if (loai === 'giaTri') {
        this.newPhieu.soPhanTramGiam = 0;
      }
      this.step = 2;
    },
    chonApDung(loai) {
      this.loaiApDung = loai;
      if (loai === 'congKhai') {
        this.khachHangDaChon = null;
        this.newPhieu.khachHangId = null;
      } else if (loai === 'khachHang') {
        this.khachHangDaChon = this.newPhieu.khachHangId || null;
      }
      this.step = 3;
    },
    validateForm() {
      this.errors = {};
      const ngayBatDau = new Date(this.newPhieu.ngayBatDau);
      const ngayKetThuc = new Date(this.newPhieu.ngayKetThuc);

      if (!this.newPhieu.tenPhieuGiamGia?.trim()) {
        this.errors.tenPhieuGiamGia = 'Tên phiếu không được để trống.';
      }
      if (this.loaiGiamGia === 'phanTram') {
        const soPhanTram = Number(this.newPhieu.soPhanTramGiam);
        if (isNaN(soPhanTram) || soPhanTram <= 0 || soPhanTram > 100) {
          this.errors.soPhanTramGiam = 'Phần trăm giảm phải từ 1 đến 100.';
        }
      }
      if (this.loaiGiamGia === 'giaTri') {
        const giaTri = Number(this.newPhieu.giaTriGiam);
        if (isNaN(giaTri) || giaTri <= 0) {
          this.errors.giaTriGiam = 'Giá trị giảm phải là số dương.';
        }
        const giaTriDonHang = Number(this.newPhieu.giaTriDonHangToiThieu);
        if (!isNaN(giaTriDonHang) && giaTri > giaTriDonHang) {
          this.errors.giaTriGiam = 'Giá trị giảm không được lớn hơn giá trị đơn hàng tối thiểu.';
        }
      }
      const giaTriDonHang = Number(this.newPhieu.giaTriDonHangToiThieu);
      if (isNaN(giaTriDonHang) || giaTriDonHang <= 0) {
        this.errors.giaTriDonHangToiThieu = 'Giá trị đơn hàng tối thiểu phải là số dương.';
      }
      const soLuong = Number(this.newPhieu.soLuong);
      if (isNaN(soLuong) || soLuong <= 0 || !Number.isInteger(soLuong)) {
        this.errors.soLuong = 'Số lượng phải là số nguyên dương.';
      }
      if (!this.newPhieu.ngayBatDau) {
        this.errors.ngayBatDau = 'Ngày bắt đầu không được để trống.';
      }
      if (!this.newPhieu.ngayKetThuc) {
        this.errors.ngayKetThuc = 'Ngày kết thúc không được để trống.';
      } else if (ngayKetThuc < ngayBatDau) {
        this.errors.ngayKetThuc = 'Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu.';
      }
      if (!this.newPhieu.moTa?.trim()) {
        this.errors.moTa = 'Mô tả không được để trống.';
      }
      if (this.loaiApDung === 'khachHang' && !this.khachHangDaChon) {
        this.errors.khachHangId = 'Vui lòng chọn khách hàng áp dụng.';
      }

      return Object.keys(this.errors).length === 0;
    },
    async handleSubmit() {
    if (!this.validateForm()) {
        this.showNotification('Vui lòng kiểm tra lại thông tin.', 'warning');
        return;
    }

    this.isLoading = true;
    const apiUrl = this.isEdit
        ? `http://localhost:8080/api/admin/phieu-giam-gia/${this.id}`
        : 'http://localhost:8080/api/admin/phieu-giam-gia';
    const method = this.isEdit ? 'PUT' : 'POST';

    // Hàm định dạng thời gian giữ nguyên múi giờ địa phương
    const formatLocalDateTime = (date) => {
        const d = new Date(date);
        const pad = (n) => n.toString().padStart(2, '0');
        return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
    };

    const requestBody = {
        ...this.newPhieu,
        tenPhieuGiamGia: this.newPhieu.tenPhieuGiamGia.trim(),
        maPhieuGiamGia: this.newPhieu.maPhieuGiamGia.trim(),
        soPhanTramGiam: Number(this.newPhieu.soPhanTramGiam) || 0,
        giaTriGiam: Number(this.newPhieu.giaTriGiam) || 0,
        giaTriDonHangToiThieu: Number(this.newPhieu.giaTriDonHangToiThieu) || 0,
        soLuong: Number(this.newPhieu.soLuong) || 0,
        ngayBatDau: formatLocalDateTime(this.newPhieu.ngayBatDau),
        ngayKetThuc: formatLocalDateTime(this.newPhieu.ngayKetThuc),
        trangThai: this.isEdit ? this.newPhieu.trangThai === 'true' : true,
        moTa: this.newPhieu.moTa.trim(),
        khachHangId: this.loaiApDung === 'khachHang' ? this.khachHangDaChon : null,
        loaiPhieu: this.loaiGiamGia === 'phanTram' ? 'PERCENT' : 'AMOUNT',
        loaiApDung: this.loaiApDung === 'congKhai' ? 'PUBLIC' : 'CUSTOMER',
    };

    try {
        const response = await fetch(apiUrl, {
            method,
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
            },
            body: JSON.stringify(requestBody),
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Lỗi từ server');
        }

        await response.json();
        this.$router.push({
            path: '/admin/phieu-giam-gia',
            query: {
                message: this.isEdit ? 'Cập nhật phiếu giảm giá thành công!' : 'Thêm phiếu giảm giá thành công!',
                type: 'success',
            },
        });
    } catch (error) {
        console.error('Lỗi API:', error);
        this.showNotification(`Lỗi: ${error.message}`, 'error');
    } finally {
        this.isLoading = false;
    }
},
    confirmSubmit() {
      this.showConfirmModal = false;
      this.handleSubmit();
    },
    showNotification(message, type = 'success') {
      this.toastMessage = message;
      this.toastType = type;
      this.showToast = true;
      setTimeout(() => (this.showToast = false), 3000);
    },
    resetForm() {
      this.newPhieu = {
        id: null,
        maPhieuGiamGia: '',
        tenPhieuGiamGia: '',
        soPhanTramGiam: 0,
        giaTriGiam: 0,
        giaTriDonHangToiThieu: 0,
        soLuong: 0,
        ngayBatDau: '',
        ngayKetThuc: '',
        moTa: '',
        trangThai: true,
        khachHangId: null,
      };
      this.loaiGiamGia = '';
      this.loaiApDung = '';
      this.khachHangDaChon = null;
      this.step = 1;
      this.errors = {};
    },
  },
};
</script>
<style scoped>
/* Hiệu ứng trượt ngang cho các bước trong form */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.5s ease;
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateX(20px);
}

.fade-slide-enter-to {
  opacity: 1;
  transform: translateX(0);
}

.fade-slide-leave-from {
  opacity: 1;
  transform: translateX(0);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}

/* Loading animation */
.loading-spinner {
  width: 50px;
  height: 50px;
  border: 5px solid #f3f3f3;
  border-top: 5px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}
</style>