<template>
    <div class="w-full font-roboto">
        <transition name="fade">
            <div v-if="showToast"
                class="fixed top-5 right-5 z-50 max-w-sm w-full p-4 rounded-lg shadow-lg text-white flex items-center"
                :class="{
                    'bg-green-500': toastType === 'success',
                    'bg-red-500': toastType === 'error',
                    'bg-yellow-500': toastType === 'warning'
                }">
                <span class="mr-2">
                    <svg v-if="toastType === 'success'" class="w-5 h-5" fill="none" stroke="currentColor"
                        viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                    </svg>
                    <svg v-if="toastType === 'error'" class="w-5 h-5" fill="none" stroke="currentColor"
                        viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12">
                        </path>
                    </svg>
                    <svg v-if="toastType === 'warning'" class="w-5 h-5" fill="none" stroke="currentColor"
                        viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M12 9v2m0 4h.01M12 3a9 9 0 110 18 9 9 0 010-18z"></path>
                    </svg>
                </span>
                {{ toastMessage }}
            </div>
        </transition>

        <div class="min-h-[calc(100vh-56px)] w-full bg-white p-10 rounded flex flex-col">
            <!-- Track step -->
            <div class="flex items-center justify-between mb-8">
                <!-- Nút quay lại bên trái -->
                <button @click="goBack()"
                    class="flex-shrink-0 flex items-center gap-1 text-sm text-gray-600 hover:text-blue-600 font-medium">
                    <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor" stroke-width="2">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7" />
                    </svg>
                    Quay lại
                </button>

                <!-- Step Tracker giữa -->
                <div class="flex-1 flex justify-center">
                    <ol class="flex items-center gap-2 text-sm font-medium text-gray-500 sm:text-base">
                        <!-- Step 1: Chọn loại phiếu -->
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
                        <!-- Step 2: Chọn áp dụng -->
                        <li :class="[
                            'flex items-center',
                            step >= 2 ? 'text-blue-600' : 'text-gray-400',
                            'after:content-[\'\'] after:w-6 after:h-1 after:inline-block after:mx-2 after:border-b',
                            step > 2 ? 'after:border-blue-600' : 'after:border-gray-200'
                        ]">
                            <span class="flex items-center">
                                <svg v-if="step >= 2" class="w-5 h-5 mr-2 text-blue-600" viewBox="0 0 20 20"
                                    fill="currentColor">
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
                        <!-- Step 3: Form nhập thông tin -->
                        <li :class="[
                            'flex items-center',
                            step >= 3 ? 'text-blue-600' : 'text-gray-400'
                        ]">
                            <!-- Step 3: Form nhập thông tin -->
                            <span class="flex items-center">
                                <svg v-if="step >= 3" class="w-5 h-5 mr-2 text-blue-600" viewBox="0 0 20 20"
                                    fill="currentColor">
                                    <path
                                        d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 8.207-4 4a1 1 0 0 1-1.414 0l-2-2a1 1 0 0 1 1.414-1.414L9 10.586l3.293-3.293a1 1 0 0 1 1.414 1.414Z" />
                                </svg>
                                <svg v-else class="w-5 h-5 mr-2 text-gray-400" viewBox="0 0 20 20" fill="currentColor">
                                    <path
                                        d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 8.207-4 4a1 1 0 0 1-1.414 0l-2-2a1 1 0 0 1 1.414-1.414L9 10.586l3.293-3.293a1 1 0 0 1 1.414 1.414Z" />
                                </svg>
                                <!-- Thay đổi text động -->
                                {{ isEdit ? 'Chỉnh sửa phiếu giảm giá' : 'Thêm phiếu giảm giá' }}
                            </span>

                        </li>
                    </ol>
                </div>
            </div>

            <!-- Transition bao quanh các bước -->
            <transition name="fade-slide" mode="out-in">
                <div :key="step">
                    <!-- Bước 1: Chọn loại phiếu -->
                    <div v-if="step === 1"
                        class="flex-1 flex flex-col items-center justify-center gap-12 min-h-[calc(100vh-350px)]">
                        <h2 class="text-2xl md:text-3xl font-bold text-gray-800 tracking-wide">
                            Chọn loại phiếu giảm giá
                        </h2>
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-10 max-w-4xl w-full px-4">
                            <div @click="chonLoai('phanTram')" class="cursor-pointer bg-white border border-gray-300 rounded-2xl shadow-lg p-10 
                    flex flex-col items-center text-center hover:scale-105 transition-all duration-300 
                    hover:bg-gradient-to-tl hover:from-[#003366] hover:to-[#242124] group">
                                <img src="https://cdn-icons-png.flaticon.com/128/4334/4334638.png" alt="Phần trăm"
                                    class="w-24 h-24 mb-5 drop-shadow transition-all duration-300" />
                                <h3
                                    class="text-2xl font-semibold text-gray-800 group-hover:text-white transition-colors duration-300">
                                    Giảm theo phần trăm
                                </h3>
                                <p
                                    class="text-base text-gray-600 mt-3 group-hover:text-white transition-colors duration-300">
                                    Áp dụng mức giảm phần trăm cho đơn hàng.
                                </p>
                            </div>
                            <div @click="chonLoai('giaTri')" class="cursor-pointer bg-white border border-gray-300 rounded-2xl shadow-lg p-10 
                    flex flex-col items-center text-center hover:scale-105 transition-all duration-300 
                    hover:bg-gradient-to-tl hover:from-[#003366] hover:to-[#242124] group">
                                <img src="https://cdn-icons-png.flaticon.com/128/639/639365.png" alt="Giá trị tiền"
                                    class="w-24 h-24 mb-5 drop-shadow transition-all duration-300" />
                                <h3
                                    class="text-2xl font-semibold text-gray-800 group-hover:text-white transition-colors duration-300">
                                    Giảm theo giá trị tiền
                                </h3>
                                <p
                                    class="text-base text-gray-600 mt-3 group-hover:text-white transition-colors duration-300">
                                    Áp dụng mức giảm cố định cho đơn hàng.
                                </p>
                            </div>
                        </div>
                    </div>

                    <!-- Bước 2: Chọn áp dụng -->
                    <div v-else-if="step === 2"
                        class="flex-1 flex flex-col items-center justify-center gap-8 min-h-[calc(100vh-350px)]">
                        <h2 class="text-2xl md:text-3xl font-bold text-gray-800 tracking-wide">
                            Chọn cách áp dụng phiếu giảm giá
                        </h2>
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-10 max-w-4xl w-full px-4">
                            <div @click="chonApDung('congKhai')" class="cursor-pointer bg-white border border-gray-300 rounded-2xl shadow-lg p-10 
                    flex flex-col items-center text-center hover:scale-105 transition-all duration-300 
                    hover:bg-gradient-to-tl hover:from-[#003366] hover:to-[#242124] group">
                                <svg class="w-16 h-16 mb-5 text-blue-600 group-hover:text-white" fill="currentColor"
                                    viewBox="0 0 20 20">
                                    <path
                                        d="M10 2a8 8 0 100 16 8 8 0 000-16zm3.5 8.5h-3v3a.5.5 0 01-1 0v-3h-3a.5.5 0 010-1h3v-3a.5.5 0 011 0v3h3a.5.5 0 010 1z" />
                                </svg>
                                <h3
                                    class="text-2xl font-semibold text-gray-800 group-hover:text-white transition-colors duration-300">
                                    Phiếu công khai
                                </h3>
                                <p
                                    class="text-base text-gray-600 mt-3 group-hover:text-white transition-colors duration-300">
                                    Áp dụng cho mọi khách hàng.
                                </p>
                            </div>
                            <div @click="chonApDung('khachHang')" class="cursor-pointer bg-white border border-gray-300 rounded-2xl shadow-lg p-10 
                    flex flex-col items-center text-center hover:scale-105 transition-all duration-300 
                    hover:bg-gradient-to-tl hover:from-[#003366] hover:to-[#242124] group">
                                <svg class="w-16 h-16 mb-5 text-blue-600 group-hover:text-white" fill="currentColor"
                                    viewBox="0 0 20 20">
                                    <path
                                        d="M10 2a8 8 0 100 16 8 8 0 000-16zm-1 11H7v-1h2v1zm4 0h-2v-1h2v1zm0-3h-6V7h6v3z" />
                                </svg>
                                <h3
                                    class="text-2xl font-semibold text-gray-800 group-hover:text-white transition-colors duration-300">
                                    Áp dụng theo khách hàng
                                </h3>
                                <p
                                    class="text-base text-gray-600 mt-3 group-hover:text-white transition-colors duration-300">
                                    Chọn khách hàng cụ thể nhận ưu đãi.
                                </p>
                            </div>
                        </div>
                    </div>

                    <!-- Bước 3: Form nhập thông tin -->
                    <div v-else-if="step === 3" class="space-y-4">
                        <!-- Tiêu đề -->
                        <div class="relative mb-6">
                            <h1 class="text-2xl font-bold text-center mb-4">
                                {{ newPhieu?.id ? 'Sửa Phiếu Giảm Giá' : 'Thêm Phiếu Giảm Giá' }}
                            </h1>
                        </div>
                        <!-- Container form và bảng khách hàng (chỉ hiển thị bảng nếu loại áp dụng là 'khachHang') -->
                        <div class="flex flex-col lg:flex-row gap-6 items-start">
                            <!-- Form bên trái -->
                            <form @submit.prevent="handleSubmit" class="flex-1 space-y-4">
                                <div class="grid grid-cols-2 gap-4 mb-4">
                                    <div class="col-span-2 sm:col-span-1" v-if="loaiGiamGia === 'phanTram'">
                                        <label for="soPhanTramGiam"
                                            class="block mb-2 text-sm font-medium text-gray-900">
                                            Phần trăm giảm
                                        </label>
                                        <input v-model="newPhieu.soPhanTramGiam" type="number" name="soPhanTramGiam"
                                            id="soPhanTramGiam"
                                            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                                            placeholder="% giảm" required />
                                        <p v-if="errors.soPhanTramGiam" class="text-red-500 text-xs mt-1">
                                            {{ errors.soPhanTramGiam }}
                                        </p>
                                    </div>
                                    <div class="col-span-2 sm:col-span-1" v-if="loaiGiamGia === 'giaTri'">
                                        <label for="giaTriGiam" class="block mb-2 text-sm font-medium text-gray-900">
                                            Giá trị giảm
                                        </label>
                                        <input v-model="newPhieu.giaTriGiam" type="number" name="giaTriGiam"
                                            id="giaTriGiam"
                                            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                                            placeholder="Giá trị giảm" required />
                                        <p v-if="errors.giaTriGiam" class="text-red-500 text-xs mt-1">
                                            {{ errors.giaTriGiam }}
                                        </p>
                                    </div>
                                    <div class="col-span-2 sm:col-span-1">
                                        <label for="giaTriDonHangToiThieu"
                                            class="block mb-2 text-sm font-medium text-gray-900">
                                            Giá trị đơn giảm tối thiểu
                                        </label>
                                        <input v-model="newPhieu.giaTriDonHangToiThieu" type="number"
                                            name="giaTriDonHangToiThieu" id="giaTriDonHangToiThieu"
                                            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                                            placeholder="Giá trị đơn hàng tối thiểu" required />
                                        <p v-if="errors.giaTriDonHangToiThieu" class="text-red-500 text-xs mt-1">
                                            {{ errors.giaTriDonHangToiThieu }}
                                        </p>
                                    </div>
                                    <div class="col-span-2 sm:col-span-1">
                                        <label for="soLuong" class="block mb-2 text-sm font-medium text-gray-900">
                                            Số lượng
                                        </label>
                                        <input v-model="newPhieu.soLuong" type="number" name="soLuong" id="soLuong"
                                            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                                            placeholder="Số lượng" required />
                                        <p v-if="errors.soLuong" class="text-red-500 text-xs mt-1">{{ errors.soLuong }}
                                        </p>
                                    </div>
                                    <!-- Bỏ phần chọn áp dụng trong form vì đã chọn ở bước 2 -->
                                    <div class="col-span-2 sm:col-span-1">
                                        <label for="ngayBatDau" class="block mb-2 text-sm font-medium text-gray-900">
                                            Ngày bắt đầu
                                        </label>
                                        <input v-model="newPhieu.ngayBatDau" type="datetime-local" name="ngayBatDau"
                                            id="ngayBatDau"
                                            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                                            required />
                                        <p v-if="errors.ngayBatDau" class="text-red-500 text-xs mt-1">
                                            {{ errors.ngayBatDau }}
                                        </p>
                                    </div>
                                    <div class="col-span-2 sm:col-span-1">
                                        <label for="ngayKetThuc" class="block mb-2 text-sm font-medium text-gray-900">
                                            Ngày kết thúc
                                        </label>
                                        <input v-model="newPhieu.ngayKetThuc" type="datetime-local" name="ngayKetThuc"
                                            id="ngayKetThuc"
                                            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                                            required />
                                        <p v-if="errors.ngayKetThuc" class="text-red-500 text-xs mt-1">
                                            {{ errors.ngayKetThuc }}
                                        </p>
                                    </div>
                                    <div class="col-span-2">
                                        <label for="moTa" class="block mb-2 text-sm font-medium text-gray-900">
                                            Mô tả
                                        </label>
                                        <textarea v-model="newPhieu.moTa" id="moTa" rows="4"
                                            class="block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500"
                                            placeholder="Nhập mô tả"></textarea>
                                        <p v-if="errors.moTa" class="text-red-500 text-xs mt-1">{{ errors.moTa }}</p>
                                    </div>
                                </div>
                                <button type="submit"
                                    class="flex items-center gap-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg font-medium rounded-lg text-sm px-5 py-2.5 text-center mb-2">
                                    <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20"
                                        xmlns="http://www.w3.org/2000/svg">
                                        <path fill-rule="evenodd"
                                            d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z"
                                            clip-rule="evenodd"></path>
                                    </svg>
                                    {{ newPhieu.id ? "Cập nhật phiếu giảm giá" : "Thêm phiếu giảm giá" }}
                                </button>
                            </form>

                            <!-- Nếu loại áp dụng là khách hàng cụ thể, hiển thị bảng -->
                            <div v-if="loaiApDung === 'khachHang'" class="flex-1 pl-6">
                                <label class="block mb-2 text-sm font-medium text-gray-900">
                                    Chọn khách hàng áp dụng
                                </label>

                                <!-- Ô tìm kiếm -->
                                <div class="mb-4">
                                    <input v-model="searchQuery" type="text" placeholder="Tìm kiếm khách hàng..."
                                        class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500" />
                                </div>

                                <!-- Bảng hiển thị khách hàng -->
                                <div class="max-h overflow-y-auto border rounded-lg p-4">
                                    <table class="min-w-full text-sm text-center">
                                        <thead>
                                            <tr>
                                                <th class="px-4 py-2 text-center">Chọn</th>
                                                <th class="px-4 py-2 text-center">STT</th>
                                                <th class="px-4 py-2 text-center">Mã</th>
                                                <th class="px-4 py-2 text-center">Họ tên</th>
                                                <th class="px-4 py-2 text-center">Email</th>
                                                <th class="px-4 py-2 text-center">SĐT</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr v-for="(kh, index) in paginatedKhachHang" :key="kh.id"
                                                class="hover:bg-gray-50 cursor-pointer"
                                                @click="khachHangDaChon = kh.id">
                                                <td class="px-4 py-2">
                                                    <input type="radio" :value="kh.id" v-model="khachHangDaChon"
                                                        class="form-radio text-blue-600" name="chonKhachHang"
                                                        @click.stop />
                                                </td>
                                                <td class="px-4 py-2">{{ currentPage * itemsPerPage + index + 1 }}</td>
                                                <td class="px-4 py-2">{{ kh.maKhachHang }}</td>
                                                <td class="px-4 py-2">{{ kh.hoTen }}</td>
                                                <td class="px-4 py-2">{{ kh.email }}</td>
                                                <td class="px-4 py-2">{{ kh.soDienThoai }}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Nút phân trang -->
                                <div class="flex justify-center items-center mt-4 space-x-2">
                                    <button @click="prevPage" :disabled="currentPage <= 0"
                                        class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg font-medium rounded-lg text-sm p-2 flex items-center justify-center disabled:opacity-50">
                                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                            stroke-width="2" stroke="currentColor" class="w-4 h-4">
                                            <path stroke-linecap="round" stroke-linejoin="round"
                                                d="M15.75 19.5 8.25 12l7.5-7.5" />
                                        </svg>
                                    </button>
                                    <span class="text-xs font-semibold text-gray-700">
                                        Trang {{ currentPage + 1 }} / {{ totalPages }}
                                    </span>
                                    <button @click="nextPage" :disabled="currentPage >= totalPages - 1"
                                        class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg font-medium rounded-lg text-sm p-2 flex items-center justify-center disabled:opacity-50">
                                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                            stroke-width="2" stroke="currentColor" class="w-4 h-4">
                                            <path stroke-linecap="round" stroke-linejoin="round"
                                                d="m8.25 4.5 7.5 7.5-7.5 7.5" />
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
import phieuGiamGiaService from '../../service/phieuGiamGiaService';
export default {
    name: 'KhachHangTable',
    props: ['id'],
    data() {
        return {
            step: 1,
            loaiGiamGia: '',
            loaiApDung: '', // Sẽ được đặt ở bước 2
            danhSachKhachHang: [], // dữ liệu lấy từ API
            searchQuery: '',
            currentPage: 0, // 0-indexed: trang đầu tiên là 0
            itemsPerPage: 6, // số dòng trên mỗi trang
            khachHangDaChon: null,
            newPhieu: {
                id: null,
                soPhanTramGiam: 0,
                giaTriGiam: 0,
                giaTriDonHangToiThieu: 0,
                soLuong: 0,
                ngayBatDau: '',
                ngayKetThuc: '',
                moTa: '',
            },
            errors: {},
        };
    },
    async mounted() {
        try {
            this.danhSachKhachHang = await KhachHangService.getAll();
        } catch (error) {
            console.error('Lỗi khi lấy dữ liệu khách hàng:', error);
        }
    },

    async addPhieuGiamGia() {
        if (this.validateForm()) {
            try {
                console.log("Dữ liệu gửi lên:", this.newPhieu);
                await phieuGiamGiaService.create(this.newPhieu);
                // Show success message after voucher is added
                this.showToastMessage("Thêm thành công!", "success");

                // Show a success message for email sent (if applicable)
                if (this.newPhieu.loaiApDung === 'Khách hàng cụ thể') {
                    this.showToastMessage("Gửi email thành công!", "success");
                }

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
    showToastMessage(message, type = 'success') {
        this.toastMessage = message;    // Set the message
        this.toastType = type;          // Set the type (success, error, etc.)
        this.showToast = true;          // Trigger the visibility of the toast

        // Hide the toast after 3 seconds
        setTimeout(() => {
            this.showToast = false;       // Hide the toast after the timeout
        }, 3000);
    },

    computed: {
        filteredKhachHang() {
            if (!this.searchQuery) return this.danhSachKhachHang;
            const query = this.searchQuery.toLowerCase();
            return this.danhSachKhachHang.filter(kh => {
                return (
                    (kh.hoTen && kh.hoTen.toLowerCase().includes(query)) ||
                    (kh.email && kh.email.toLowerCase().includes(query)) ||
                    (kh.soDienThoai && kh.soDienThoai.toLowerCase().includes(query)) ||
                    (kh.maKhachHang && kh.maKhachHang.toLowerCase().includes(query))
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
        }
    },
    created() {
        if (this.isEdit) {
            fetch(`http://localhost:8080/api/admin/phieu-giam-gia/${this.id}`)
                .then(res => res.json())
                .then(data => {
                    const formatInputDateTime = (dateString) => {
                        const d = new Date(dateString);
                        return d.toISOString().slice(0, 16);
                    };
                    this.newPhieu = {
                        ...data,
                        ngayBatDau: formatInputDateTime(data.ngayBatDau),
                        ngayKetThuc: formatInputDateTime(data.ngayKetThuc),
                        trangThai: data.trangThai ? 'true' : 'false',
                    };

                    // 👇 Thiết lập các bước dựa theo loại dữ liệu cũ
                    this.loaiGiamGia = data.soPhanTramGiam > 0 ? 'phanTram' : 'giaTri';
                    this.loaiApDung = data.khachHangId ? 'khachHang' : 'congKhai';
                    this.khachHangDaChon = data.khachHangId || null;
                    this.step = 1; // 👈 Bắt đầu từ step 1
                })
                .catch(error => console.error('Lỗi lấy dữ liệu:', error));
        }
    },

    watch: {
        searchQuery() {
            this.currentPage = 0;
        }
    },
    methods: {
        prevPage() {
            if (this.currentPage > 0) {
                this.currentPage--;
            }
        },
        nextPage() {
            if (this.currentPage < this.totalPages - 1) {
                this.currentPage++;
            }
        },
        formatDiaChi(khachHang) {
            if (!khachHang.danhSachDiaChi || khachHang.danhSachDiaChi.length === 0) return '';
            const diaChi = khachHang.danhSachDiaChi[0];
            return `${diaChi.diaChiChiTiet}, ${diaChi.xaPhuong}, ${diaChi.quanHuyen}, ${diaChi.tinhThanh}`;
        },
        goBack() {
            if (this.step > 1) {
                this.step--;
            } else {
                this.$router.push('/admin/phieu-giam-gia');
            }
        },
        // Bước 1: Chọn loại phiếu
        chonLoai(loai) {
            this.loaiGiamGia = loai;
            // Nếu chọn giảm theo phần trăm → reset giá trị tiền
            if (loai === 'phanTram') {
                this.newPhieu.giaTriGiam = 0;
            } else if (loai === 'giaTri') {
                this.newPhieu.soPhanTramGiam = 0;
            }
            this.step = 2;
        },

        // Bước 2: Chọn loại áp dụng
        chonApDung(loai) {
            this.loaiApDung = loai;

            if (loai === 'congKhai') {
                this.khachHangDaChon = null;
                this.newPhieu.khachHangId = null;
            } else if (loai === 'khachHang') {
                // Nếu đang sửa và có dữ liệu cũ thì giữ lại
                this.khachHangDaChon = this.newPhieu.khachHangId || null;
            }

            this.step = 3;
        },


        validateForm() {
            this.errors = {};
            const currentDate = new Date();
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
            } else if (ngayBatDau < currentDate && !this.isEdit) {
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
        handleSubmit() {
            if (this.validateForm()) {
                const apiUrl = this.isEdit
                    ? `http://localhost:8080/api/admin/phieu-giam-gia/${this.id}`
                    : 'http://localhost:8080/api/admin/phieu-giam-gia';

                const method = this.isEdit ? 'PUT' : 'POST';
                const toIsoString = (date) => new Date(date).toISOString().slice(0, 19);

                const requestBody = {
                    ...this.newPhieu,
                    soPhanTramGiam: Number(this.newPhieu.soPhanTramGiam),
                    giaTriGiam: Number(this.newPhieu.giaTriGiam),
                    giaTriDonHangToiThieu: Number(this.newPhieu.giaTriDonHangToiThieu),
                    soLuong: Number(this.newPhieu.soLuong),
                    ngayBatDau: toIsoString(this.newPhieu.ngayBatDau),
                    ngayKetThuc: toIsoString(this.newPhieu.ngayKetThuc),
                    trangThai: this.newPhieu.trangThai === 'true',
                    moTa: this.newPhieu.moTa.trim(),
                    // 👇 chỉ gửi ID nếu là khách hàng cụ thể
                    khachHangId: this.loaiApDung === 'khachHang' ? this.khachHangDaChon : null
                };


                fetch(apiUrl, {
                    method: method,
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    },
                    body: JSON.stringify(requestBody)
                })
                    .then(response => {
                        if (!response.ok) throw new Error(`Server error: ${response.status}`);
                        return response.json();
                    })
                    .then(() => this.$router.push('/admin/phieu-giam-gia'))
                    .catch(error => {
                        console.error('Lỗi API:', error);
                        alert('Có lỗi xảy ra khi gửi dữ liệu lên server: ' + error.message);
                    });
            } else {
                alert('Vui lòng kiểm tra dữ liệu nhập!');
            }
        }
    }
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
</style>