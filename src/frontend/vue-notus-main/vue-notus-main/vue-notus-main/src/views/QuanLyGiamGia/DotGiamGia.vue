<template>
  <div class="min-h-screen font-sans relative">
    <div>
      <!-- Header and Actions -->
      <div class="flex justify-between items-center mb-6">
        <h1 class="text-lg font-bold mb-4">Đợt giảm giá</h1>
        <div v-if="!showForm && !selectedDiscount" class="flex space-x-4">
          <select
            v-model="selectedStatus"
            class="w-48 p-2 bg-gray-50 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200"
          >
            <option value="">Tất cả trạng thái</option>
            <option v-for="status in statusOptions" :key="status.value" :value="status.value">
              {{ status.text }}
            </option>
          </select>
          <input
            v-model="searchTerm"
            type="text"
            class="w-48 p-2 bg-gray-50 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200"
            placeholder="Tìm kiếm theo nhiều trường..."
          />
          <input
            v-model="productFilter"
            type="text"
            class="w-48 p-2 bg-gray-50 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200"
            placeholder="Tìm kiếm theo sản phẩm..."
          />
          <label class="flex items-center space-x-2">
            <input
              v-model="showNonExistentProducts"
              type="checkbox"
              class="h-5 w-5 text-blue-500 border-gray-300 rounded focus:ring-blue-500"
            />
            <span class="text-sm text-gray-700">Hiển thị đợt giảm giá có sản phẩm không tồn tại</span>
          </label>
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
          <button
            @click="openCreateForm"
           class="flex items-center gap-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 dark:focus:ring-blue-800 shadow-lg font-medium rounded-lg text-sm px-5 py-2.5 mt-5">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                stroke="currentColor" class="w-4 h-4">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
              </svg> Thêm đợt giảm giá
          </button>
        </div>
      </div>

      <!-- Create/Update Form (Two Columns) -->
      <div v-if="showForm || selectedDiscount" class="mb-6 flex flex-col md:flex-row gap-6">
        <div class="w-full md:w-1/2 p-4">
          <div class="flex justify-between items-center mb-4">
            <h2 class="text-xl font-semibold text-gray-800">{{ isUpdate ? 'Cập nhật đợt giảm giá' : 'Thêm đợt giảm giá' }}</h2>
            <button @click="closeForm" class="text-gray-500 hover:text-gray-700 text-xl transition-all duration-200">×</button>
          </div>

          <form @submit.prevent="handleSubmit" class="space-y-4">
            <div v-if="errorMessage" class="p-2 bg-red-50 border-l-4 border-red-500 text-red-700 rounded-md text-xs">
              {{ errorMessage }}
            </div>

            <div class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Mã</label>
                <input
                  v-model="formData.maDotGiamGia"
                  class="w-full p-2 bg-gray-200 border border-gray-300 rounded-md"
                  placeholder="Nhập mã đợt giảm giá"
                  disabled
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">* Tên Đợt giảm giá</label>
                <input
                  v-model="formData.tenDotGiamGia"
                  class="w-full p-2 bg-white border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200"
                  placeholder="Nhập tên đợt giảm giá"
                  required
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">* Loại giảm giá</label>
                <select
                  v-model="formData.loaiGiamGia"
                  class="w-full p-2 bg-white border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200"
                  required
                >
                  <option value="Phần trăm">Phần trăm</option>
                  <option value="Tiền mặt">Tiền mặt</option>
                </select>
              </div>
              <div v-if="formData.loaiGiamGia === 'Phần trăm'">
                <label class="block text-sm font-medium text-gray-700 mb-1">* Giá trị (%)</label>
                <input
                  v-model.number="formData.soPhanTramGiam"
                  type="number"
                  min="1"
                  max="100"
                  class="w-full p-2 bg-white border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200"
                  required
                />
              </div>
              <div v-if="formData.loaiGiamGia === 'Tiền mặt'">
                <label class="block text-sm font-medium text-gray-700 mb-1">* Số tiền tối đa</label>
                <input
                  v-model.number="formData.giaTriGiam"
                  type="number"
                  min="0"
                  class="w-full p-2 bg-white border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200"
                  required
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">* Ngày bắt đầu</label>
                <input
                  v-model="formData.ngayBatDau"
                  type="date"
                  :class="{ 'border-red-500': isDateInvalid }"
                  class="w-full p-2 bg-white border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200"
                  required
                  @input="validateDates"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">* Ngày kết thúc</label>
                <input
                  v-model="formData.ngayKetThuc"
                  type="date"
                  :class="{ 'border-red-500': isDateInvalid }"
                  class="w-full p-2 bg-white border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200"
                  required
                  @input="validateDates"
                />
              </div>
            </div>

            <div class="flex justify-end mt-4 space-x-2">
              <button
                type="submit"
                class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition-all duration-200"
              >
                {{ isUpdate ? 'CẬP NHẬT' : 'TẠO MỚI' }}
              </button>
              <button
                v-if="!isUpdate"
                type="button"
                @click="selectAllProducts"
                class="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 transition-all duration-200"
              >
                Chọn tất cả
              </button>
              <button
                v-if="!isUpdate"
                type="button"
                @click="deselectAllProducts"
                class="bg-red-500 text-white px-4 py-2 rounded-md hover:bg-red-600 transition-all duration-200"
              >
                Bỏ chọn tất cả
              </button>
            </div>
          </form>
        </div>

        <!-- Product Selection (Right Column) -->
        <div class="w-full md:w-1/2 p-4">
          <div>
            <div class="flex justify-between items-center mb-4">
              <h3 class="text-lg font-semibold text-gray-800">Sản Phẩm</h3>
              <input
                v-model="productSearchTerm"
                type="text"
                class="p-2 bg-white border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200 w-1/2"
                placeholder="Tìm kiếm theo tên sách..."
              />
            </div>
            <table class="min-w-full bg-white border border-gray-200">
              <thead>
                <tr class="bg-gray-100 text-gray-700 text-left">
                  <th class="px-4 py-2 text-sm font-semibold border-b"></th>
                  <th class="px-4 py-2 text-sm font-semibold border-b">STT</th>
                  <th class="px-4 py-2 text-sm font-semibold border-b">Mã sản phẩm</th>
                  <th class="px-4 py-2 text-sm font-semibold border-b">Tên sách</th>
                  <th class="px-4 py-2 text-sm font-semibold border-b">Mô tả</th>
                  <th class="px-4 py-2 text-sm font-semibold border-b">Số lượng tồn</th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="loadingProducts" class="text-center">
                  <td colspan="6" class="px-4 py-3 text-gray-600">
                    <span class="flex items-center justify-center">
                      <svg class="animate-spin h-5 w-5 mr-2 text-blue-500" viewBox="0 0 24 24">
                        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8h8a8 8 0 01-8 8v-8H4z"></path>
                      </svg>
                      Đang tải sản phẩm...
                    </span>
                  </td>
                </tr>
                <tr v-else-if="paginatedProducts.length === 0" class="text-center">
                  <td colspan="6" class="px-4 py-3 text-gray-600">Không có dữ liệu</td>
                </tr>
                <tr
                  v-for="(product, index) in paginatedProducts"
                  :key="product.id"
                  class="border-b border-gray-200 hover:bg-gray-50 transition-all duration-200 cursor-pointer"
                >
                  <td class="px-4 py-2 text-gray-700">
                    <input
                      type="checkbox"
                      v-model="formData.selectedProducts"
                      :value="product.id"
                      class="mr-2"
                      @click.stop="toggleProductSelection(product.id)"
                    />
                  </td>
                  <td class="px-4 py-2 text-gray-700" @click="addProductToSelected(product)">
                    {{ (currentProductPage - 1) * productsPerPage + index + 1 }}
                  </td>
                  <td class="px-4 py-2 text-gray-700" @click="addProductToSelected(product)">
                    {{ product.maSanPham || 'Không có mã' }}
                  </td>
                  <td class="px-4 py-2 text-gray-700" @click="addProductToSelected(product)">
                    {{ product.tenSanPham || 'Không xác định' }}
                  </td>
                  <td class="px-4 py-2 text-gray-700" @click="addProductToSelected(product)">
                    {{ product.moTa || 'Không có mô tả' }}
                  </td>
                  <td class="px-4 py-2 text-gray-700" @click="addProductToSelected(product)">
                    {{ product.soLuongTon || 0 }}
                  </td>
                </tr>
              </tbody>
            </table>

            <div class="mt-4 flex justify-end">
              <div class="flex items-center space-x-2">
                <button
                  @click="prevProductPage"
                  :disabled="currentProductPage === 1"
                  class="bg-blue-500 text-white px-2 py-1 rounded-md hover:bg-blue-700 transition-all duration-200 disabled:bg-gray-300 disabled:text-gray-500"
                >
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path>
                  </svg>
                </button>
                <span class="text-sm text-gray-700">{{ currentProductPage }} / {{ totalProductPages }}</span>
                <button
                  @click="nextProductPage"
                  :disabled="currentProductPage === totalProductPages"
                  class="bg-blue-500 text-white px-2 py-1 rounded-md hover:bg-blue-700 transition-all duration-200 disabled:bg-gray-300 disabled:text-gray-500"
                >
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
                  </svg>
                </button>
              </div>
            </div>

            <!-- Selected Products (Show ChiTietSanPham) -->
            <div v-if="filteredChiTietSanPhams.length > 0" class="mt-6">
              <h3 class="text-lg font-semibold text-gray-800 mb-4">Sản Phẩm Chi Tiết</h3>
              <table class="min-w-full bg-white border border-gray-200">
                <thead>
                  <tr class="bg-gray-100 text-gray-700 text-left">
                    <th class="px-4 py-2 text-sm font-semibold border-b"></th>
                    <th class="px-4 py-2 text-sm font-semibold border-b">
                      STT
                      <span
                        class="ml-2 cursor-pointer"
                        @click="toggleSpeechRecognition"
                        :class="{ 'text-blue-500': isSpeechActive }"
                      >
                        <svg class="w-4 h-4 inline-block" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11a7 7 0 01-7 7m0 0a7 7 0 01-7-7m7 7v4m0 0H8m4 0h4m-4-7a3 3 0 01-3-3V5a3 3 0 116 0v6a3 3 0 01-3 3z"></path>
                        </svg>
                      </span>
                      <span
                        class="ml-2 cursor-pointer"
                        @click="toggleFilter('stt')"
                      >
                        <svg class="w-4 h-4 inline-block" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2a1 1 0 01-.293.707L14 13.414V20a1 1 0 01-1.447.894l-4-2A1 1 0 018 18v-4.586L3.293 6.707A1 1 0 013 6V4z"></path>
                        </svg>
                      </span>
                    </th>
                    <th class="px-4 py-2 text-sm font-semibold border-b">
                      Mã chi tiết
                      <span
                        class="ml-2 cursor-pointer"
                        @click="toggleFilter('maChiTietSanPham')"
                      >
                        <svg class="w-4 h-4 inline-block" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2a1 1 0 01-.293.707L14 13.414V20a1 1 0 01-1.447.894l-4-2A1 1 0 018 18v-4.586L3.293 6.707A1 1 0 013 6V4z"></path>
                        </svg>
                      </span>
                    </th>
                    <th class="px-4 py-2 text-sm font-semibold border-b">
                      Tên chi tiết
                      <span
                        class="ml-2 cursor-pointer"
                        @click="toggleFilter('tenChiTietSanPham')"
                      >
                        <svg class="w-4 h-4 inline-block" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2a1 1 0 01-.293.707L14 13.414V20a1 1 0 01-1.447.894l-4-2A1 1 0 018 18v-4.586L3.293 6.707A1 1 0 013 6V4z"></path>
                        </svg>
                      </span>
                    </th>
                    <th class="px-4 py-2 text-sm font-semibold border-b">
                      Mô tả
                      <span
                        class="ml-2 cursor-pointer"
                        @click="toggleFilter('moTa')"
                      >
                        <svg class="w-4 h-4 inline-block" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2a1 1 0 01-.293.707L14 13.414V20a1 1 0 01-1.447.894l-4-2A1 1 0 018 18v-4.586L3.293 6.707A1 1 0 013 6V4z"></path>
                        </svg>
                      </span>
                    </th>
                    <th class="px-4 py-2 text-sm font-semibold border-b">
                      Nhà xuất bản
                      <span
                        class="ml-2 cursor-pointer"
                        @click="toggleFilter('nhaXuatBan')"
                      >
                        <svg class="w-4 h-4 inline-block" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2a1 1 0 01-.293.707L14 13.414V20a1 1 0 01-1.447.894l-4-2A1 1 0 018 18v-4.586L3.293 6.707A1 1 0 013 6V4z"></path>
                        </svg>
                      </span>
                    </th>
                    <th class="px-4 py-2 text-sm font-semibold border-b">
                      Trọng lượng (g)
                      <span
                        class="ml-2 cursor-pointer"
                        @click="toggleFilter('trongLuong')"
                      >
                        <svg class="w-4 h-4 inline-block" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2a1 1 0 01-.293.707L14 13.414V20a1 1 0 01-1.447.894l-4-2A1 1 0 018 18v-4.586L3.293 6.707A1 1 0 013 6V4z"></path>
                        </svg>
                      </span>
                    </th>
                    <th class="px-4 py-2 text-sm font-semibold border-b">
                      Kích thước (cm)
                      <span
                        class="ml-2 cursor-pointer"
                        @click="toggleFilter('kichThuoc')"
                      >
                        <svg class="w-4 h-4 inline-block" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2a1 1 0 01-.293.707L14 13.414V20a1 1 0 01-1.447.894l-4-2A1 1 0 018 18v-4.586L3.293 6.707A1 1 0 013 6V4z"></path>
                        </svg>
                      </span>
                    </th>
                    <th class="px-4 py-2 text-sm font-semibold border-b">
                      Giá (đ)
                      <span
                        class="ml-2 cursor-pointer"
                        @click="toggleFilter('donGia')"
                      >
                        <svg class="w-4 h-4 inline-block" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2a1 1 0 01-.293.707L14 13.414V20a1 1 0 01-1.447.894l-4-2A1 1 0 018 18v-4.586L3.293 6.707A1 1 0 013 6V4z"></path>
                        </svg>
                      </span>
                    </th>
                    <th class="px-4 py-2 text-sm font-semibold border-b">Thao tác</th>
                  </tr>
                  <tr v-if="activeFilter === 'stt'" class="bg-gray-50">
                    <td colspan="10" class="px-4 py-2">
                      <input
                        v-model="filters.stt"
                        type="number"
                        class="w-full p-2 border border-gray-300 rounded-md text-sm"
                        placeholder="Lọc theo STT..."
                      />
                    </td>
                  </tr>
                  <tr v-if="activeFilter === 'maChiTietSanPham'" class="bg-gray-50">
                    <td colspan="10" class="px-4 py-2">
                      <input
                        v-model="filters.maChiTietSanPham"
                        type="text"
                        class="w-full p-2 border border-gray-300 rounded-md text-sm"
                        placeholder="Lọc theo mã chi tiết..."
                      />
                    </td>
                  </tr>
                  <tr v-if="activeFilter === 'tenChiTietSanPham'" class="bg-gray-50">
                    <td colspan="10" class="px-4 py-2">
                      <input
                        v-model="filters.tenChiTietSanPham"
                        type="text"
                        class="w-full p-2 border border-gray-300 rounded-md text-sm"
                        placeholder="Lọc theo tên chi tiết..."
                      />
                    </td>
                  </tr>
                  <tr v-if="activeFilter === 'moTa'" class="bg-gray-50">
                    <td colspan="10" class="px-4 py-2">
                      <input
                        v-model="filters.moTa"
                        type="text"
                        class="w-full p-2 border border-gray-300 rounded-md text-sm"
                        placeholder="Lọc theo mô tả..."
                      />
                    </td>
                  </tr>
                  <tr v-if="activeFilter === 'nhaXuatBan'" class="bg-gray-50">
                    <td colspan="10" class="px-4 py-2">
                      <select
                        v-model="filters.nhaXuatBan"
                        class="w-full p-2 border border-gray-300 rounded-md text-sm"
                      >
                        <option value="">Tất cả</option>
                        <option v-for="nxb in uniqueNhaXuatBan" :key="nxb" :value="nxb">
                          {{ nxb }}
                        </option>
                      </select>
                    </td>
                  </tr>
                  <tr v-if="activeFilter === 'trongLuong'" class="bg-gray-50">
                    <td colspan="10" class="px-4 py-2">
                      <input
                        v-model="filters.trongLuong"
                        type="number"
                        class="w-full p-2 border border-gray-300 rounded-md text-sm"
                        placeholder="Lọc theo trọng lượng..."
                      />
                    </td>
                  </tr>
                  <tr v-if="activeFilter === 'kichThuoc'" class="bg-gray-50">
                    <td colspan="10" class="px-4 py-2">
                      <input
                        v-model="filters.kichThuoc"
                        type="text"
                        class="w-full p-2 border border-gray-300 rounded-md text-sm"
                        placeholder="Lọc theo kích thước..."
                      />
                    </td>
                  </tr>
                  <tr v-if="activeFilter === 'donGia'" class="bg-gray-50">
                    <td colspan="10" class="px-4 py-2">
                      <input
                        v-model="filters.donGia"
                        type="number"
                        class="w-full p-2 border border-gray-300 rounded-md text-sm"
                        placeholder="Lọc theo giá..."
                      />
                    </td>
                  </tr>
                </thead>
                <tbody>
                  <tr
                    v-for="(chiTiet, index) in filteredChiTietSanPhams"
                    :key="chiTiet.tempId || chiTiet.id"
                    class="border-b border-gray-200 hover:bg-gray-50 transition-all duration-200"
                  >
                    <td class="px-4 py-2 text-gray-700">
                      <input
                        type="checkbox"
                        v-model="selectedChiTietSanPhamIds"
                        :value="chiTiet.tempId || chiTiet.id"
                        class="mr-2"
                        @click.stop="toggleChiTietSelection(chiTiet)"
                      />
                    </td>
                    <td
                      class="px-4 py-2 text-gray-700 cursor-pointer hover:text-blue-500"
                      @click="duplicateChiTietSanPham(chiTiet)"
                    >
                      {{ index + 1 }}
                    </td>
                    <td class="px-4 py-2 text-gray-700" @click="duplicateChiTietSanPham(chiTiet)">
                      {{ chiTiet.maChiTietSanPham || 'Không có mã' }}
                    </td>
                    <td class="px-4 py-2 text-gray-700" @click="duplicateChiTietSanPham(chiTiet)">
                      {{ chiTiet.tenChiTietSanPham || 'Không xác định' }}
                    </td>
                    <td class="px-4 py-2 text-gray-700" @click="duplicateChiTietSanPham(chiTiet)">
                      {{ chiTiet.moTa || 'Không có mô tả' }}
                    </td>
                    <td class="px-4 py-2 text-gray-700" @click="duplicateChiTietSanPham(chiTiet)">
                      {{ chiTiet.idNhaXuatBan?.tenNhaXuatBan || 'Không xác định' }}
                    </td>
                    <td class="px-4 py-2 text-gray-700" @click="duplicateChiTietSanPham(chiTiet)">
                      {{ chiTiet.trongLuong || 'Không xác định' }}
                    </td>
                    <td class="px-4 py-2 text-gray-700" @click="duplicateChiTietSanPham(chiTiet)">
                      {{ chiTiet.kichThuoc || 'Không xác định' }}
                    </td>
                    <td class="px-4 py-2 text-gray-700 typed" @click="duplicateChiTietSanPham(chiTiet)">
                      {{ chiTiet.donGia ? chiTiet.donGia.toLocaleString('vi-VN') + ' đ' : 'Không xác định' }}
                    </td>
                    <td class="px-4 py-2">
                      <button
                        @click="viewChiTietSanPhamDetails(chiTiet)"
                        class="text-blue-500 hover:text-blue-700 transition-all duration-200 mr-2"
                        title="Xem chi tiết"
                      >
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                          <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                          <circle cx="12" cy="12" r="3"/>
                        </svg>
                      </button>
                      <button
                        @click="deleteChiTietSanPham(chiTiet)"
                        class="text-red-500 hover:text-red-700 transition-all duration-200"
                        title="Xóa chi tiết sản phẩm"
                      >
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                          <line x1="18" y1="6" x2="6" y2="18"/>
                          <line x1="6" y1="6" x2="18" y2="18"/>
                        </svg>
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <!-- Modal for ChiTietSanPham Details -->
      <div
        v-if="showChiTietModal"
        class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50"
      >
        <div class="bg-white rounded-lg p-6 w-full max-w-lg">
          <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-semibold text-gray-800">Chi Tiết Sản Phẩm</h3>
            <button @click="closeChiTietModal" class="text-gray-500 hover:text-gray-700 text-xl">×</button>
          </div>
          <div v-if="selectedChiTiet" class="space-y-2">
            <p><strong>Mã chi tiết:</strong> {{ selectedChiTiet.maChiTietSanPham || 'Không có mã' }}</p>
            <p><strong>Tên chi tiết:</strong> {{ selectedChiTiet.tenChiTietSanPham || 'Không xác định' }}</p>
            <p><strong>Mô tả:</strong> {{ selectedChiTiet.moTa || 'Không có mô tả' }}</p>
            <p><strong>Nhà xuất bản:</strong> {{ selectedChiTiet.idNhaXuatBan?.tenNhaXuatBan || 'Không xác định' }}</p>
            <p><strong>Trọng lượng:</strong> {{ selectedChiTiet.trongLuong ? selectedChiTiet.trongLuong + ' g' : 'Không xác định' }}</p>
            <p><strong>Kích thước:</strong> {{ selectedChiTiet.kichThuoc || 'Không xác định' }}</p>
            <p><strong>Giá:</strong> {{ selectedChiTiet.donGia ? selectedChiTiet.donGia.toLocaleString('vi-VN') + ' đ' : 'Không xác định' }}</p>
            <p><strong>Số lượng tồn:</strong> {{ selectedChiTiet.soLuongTon || 0 }}</p>
          </div>
          <div class="mt-4 flex justify-end">
            <button
              @click="closeChiTietModal"
              class="bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition-all duration-200"
            >
              Đóng
            </button>
          </div>
        </div>
      </div>

      <!-- Modal for Confirmation -->
      <div
        v-if="showConfirmModalFlag"
        class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50"
      >
        <div class="bg-white rounded-lg p-6 w-full max-w-md">
          <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-semibold text-gray-800">Xác nhận</h3>
            <button @click="closeConfirmModal" class="text-gray-500 hover:text-gray-700 text-xl">×</button>
          </div>
          <p class="text-gray-700 mb-4">{{ confirmMessage }}</p>
          <div class="flex justify-end space-x-2">
            <button
              @click="closeConfirmModal"
              class="bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition-all duration-200"
            >
              Hủy
            </button>
            <button
              @click="confirmAction"
              class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition-all duration-200"
            >
              Xác nhận
            </button>
          </div>
        </div>
      </div>

      <!-- Discount Table (Show only when not in create form) -->
      <div v-if="!showForm && !selectedDiscount" class="overflow-x-auto">
        <table class="min-w-full bg-white border border-gray-200">
          <thead>
            <tr class="bg-gray-100 text-gray-700 text-left">
              <th class="px-4 py-2 text-sm font-semibold border-b">#</th>
              <th class="px-4 py-2 text-sm font-semibold border-b">Tên Đợt giảm giá</th>
              <th class="px-4 py-2 text-sm font-semibold border-b">Loại giảm giá</th>
              <th class="px-4 py-2 text-sm font-semibold border-b">Giá trị giảm</th>
              <th class="px-4 py-2 text-sm font-semibold border-b">Trạng thái</th>
              <th class="px-4 py-2 text-sm font-semibold border-b">Thời gian bắt đầu</th>
              <th class="px-4 py-2 text-sm font-semibold border-b">Thời gian kết thúc</th>
              <th class="px-4 py-2 text-sm font-semibold border-b">Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="loading" class="text-center">
              <td colspan="8" class="px-4 py-3 text-gray-600">
                <span class="flex items-center justify-center">
                  <svg class="animate-spin h-5 w-5 mr-2 text-blue-500" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8h8a8 8 0 01-8 8v-8H4z"></path>
                  </svg>
                  Đang tải...
                </span>
              </td>
            </tr>
            <tr v-else-if="paginatedDiscounts.length === 0" class="text-center">
              <td colspan="8" class="px-4 py-3 text-gray-600">Không có dữ liệu</td>
            </tr>
            <tr
              v-for="(discount, index) in paginatedDiscounts"
              :key="discount.id"
              class="border-b border-gray-200 hover:bg-gray-50 transition-all duration-200"
            >
              <td class="px-4 py-2 text-gray-700">{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
              <td class="px-4 py-2 text-gray-700 font-medium">{{ discount.tenDotGiamGia }}</td>
              <td class="px-4 py-2 text-gray-700">{{ discount.loaiGiamGia }}</td>
              <td class="px-4 py-2 text-gray-700">
                {{ discount.loaiGiamGia === 'Phần trăm' ? `${discount.soPhanTramGiam}%` : `${discount.giaTriGiam || 0} đ` }}
              </td>
              <td class="px-4 py-2">
                <span
                  :class="{
                    'bg-green-100 text-green-700': discount.trangThai === 'Đang diễn ra',
                    'bg-red-100 text-red-700': discount.trangThai === 'Đã kết thúc',
                    'bg-yellow-100 text-yellow-700': discount.trangThai === 'Chưa bắt đầu',
                  }"
                  class="px-2 py-1 rounded-full text-xs font-medium"
                >
                  {{ discount.trangThai || getDiscountStatus(discount) }}
                </span>
              </td>
              <td class="px-4 py-2 text-gray-700">{{ formatDate(discount.ngayBatDau) }}</td>
              <td class="px-4 py-2 text-gray-700">{{ formatDate(discount.ngayKetThuc) }}</td>
              <td class="px-4 py-2">
                <button
                  @click="openUpdateForm(discount)"
                  class="text-gray-600 hover:text-gray-800 transition-all duration-200 mr-2"
                  title="Xem chi tiết"
                >
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                    <circle cx="12" cy="12" r="3"/>
                  </svg>
                </button>
                <button
                  @click="deleteDiscount(discount.id)"
                  class="text-red-500 hover:text-red-700 transition-all duration-200"
                  title="Xóa đợt giảm giá"
                >
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <line x1="18" y1="6" x2="6" y2="18"/>
                    <line x1="6" y1="6" x2="18" y2="18"/>
                  </svg>
                </button>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- Pagination -->
        <div class="mt-4 flex justify-center">
          <div class="flex items-center space-x-2">
            <button
              @click="prevPage"
              :disabled="currentPage === 1"
              class="bg-blue-500 text-white px-2 py-1 rounded-md hover:bg-blue-700 transition-all duration-200 disabled:bg-gray-300 disabled:text-gray-500"
            >
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path>
              </svg>
            </button>
            <span class="text-sm text-gray-700">Trang {{ currentPage }} / {{ totalPages }}</span>
            <button
              @click="nextPage"
              :disabled="currentPage === totalPages"
              class="bg-blue-500 text-white px-2 py-1 rounded-md hover:bg-blue-700 transition-all duration-200 disabled:bg-gray-300 disabled:text-gray-500"
            >
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
              </svg>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import DotGiamGiaService from '@/service/DotGiamGiaService';
import * as XLSX from 'xlsx';

export default {
  data() {
    return {
      searchTerm: '',
      selectedStatus: '',
      productFilter: '',
      showNonExistentProducts: false,
      currentPage: 1,
      itemsPerPage: 5,
      discountList: [],
      statusOptions: [
        { value: 'Chưa bắt đầu', text: 'Chưa bắt đầu' },
        { value: 'Đang diễn ra', text: 'Đang diễn ra' },
        { value: 'Đã kết thúc', text: 'Đã kết thúc' },
      ],
      showForm: false,
      isUpdate: false,
      selectedDiscount: null,
      formData: {
        id: null,
        maDotGiamGia: '',
        tenDotGiamGia: '',
        loaiGiamGia: 'Phần trăm',
        soPhanTramGiam: 0,
        giaTriGiam: 0,
        ngayBatDau: '',
        ngayKetThuc: '',
        trangThai: 'Chưa bắt đầu',
        selectedProducts: [],
      },
      errorMessage: '',
      loading: false,
      loadingProducts: false,
      isDateInvalid: false,
      products: [],
      selectedChiTietSanPhams: [],
      selectedChiTietSanPhamIds: [],
      productSearchTerm: '',
      currentProductPage: 1,
      productsPerPage: 5,
      showChiTietModal: false,
      selectedChiTiet: null,
      filters: {
        maChiTietSanPham: '',
        tenChiTietSanPham: '',
        moTa: '',
        nhaXuatBan: '',
        trongLuong: '',
        kichThuoc: '',
        donGia: '',
        stt: '',
      },
      activeFilter: '',
      isSpeechActive: false,
      speechRecognition: null,
      showConfirmModalFlag: false,
      confirmMessage: '',
      confirmActionType: '',
      confirmDiscountId: null,
    };
  },
  computed: {
    filteredDiscounts() {
      return this.discountList.filter((discount) => {
        const matchesStatus = this.selectedStatus === '' || discount.trangThai === this.selectedStatus;

        const searchLower = this.searchTerm.toLowerCase();
        const matchesSearch =
          this.searchTerm === '' ||
          discount.tenDotGiamGia.toLowerCase().includes(searchLower) ||
          discount.maDotGiamGia.toLowerCase().includes(searchLower) ||
          discount.loaiGiamGia.toLowerCase().includes(searchLower) ||
          (discount.soPhanTramGiam && discount.soPhanTramGiam.toString().includes(searchLower)) ||
          (discount.giaTriGiam && discount.giaTriGiam.toString().includes(searchLower)) ||
          (discount.ngayBatDau && this.formatDate(discount.ngayBatDau).toLowerCase().includes(searchLower)) ||
          (discount.ngayKetThuc && this.formatDate(discount.ngayKetThuc).toLowerCase().includes(searchLower));

        const matchesProduct =
          this.productFilter === '' ||
          (discount.dotGiamGiaChiTiets &&
            discount.dotGiamGiaChiTiets.some((chiTiet) => {
              const product = this.products.find(
                (p) => p.id === chiTiet.idChiTietSanPham?.idSanPham?.id
              );
              return (
                product &&
                product.tenSanPham.toLowerCase().includes(this.productFilter.toLowerCase())
              );
            }));

        const matchesNonExistentProducts =
          !this.showNonExistentProducts ||
          (discount.dotGiamGiaChiTiets &&
            discount.dotGiamGiaChiTiets.some((chiTiet) => {
              const product = this.products.find(
                (p) => p.id === chiTiet.idChiTietSanPham?.idSanPham?.id
              );
              return !product;
            }));

        return matchesSearch && matchesStatus && matchesProduct && matchesNonExistentProducts;
      });
    },
    totalPages() {
      return Math.ceil(this.filteredDiscounts.length / this.itemsPerPage);
    },
    paginatedDiscounts() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      return this.filteredDiscounts.slice(start, start + this.itemsPerPage);
    },
    filteredProducts() {
      return this.products.filter((product) => {
        return product.tenSanPham.toLowerCase().includes(this.productSearchTerm.toLowerCase());
      });
    },
    totalProductPages() {
      return Math.ceil(this.filteredProducts.length / this.productsPerPage) || 1;
    },
    paginatedProducts() {
      const start = (this.currentProductPage - 1) * this.productsPerPage;
      return this.filteredProducts.slice(start, start + this.productsPerPage);
    },
    filteredChiTietSanPhams() {
      return this.selectedChiTietSanPhams.filter((chiTiet, index) => {
        const matchesFilters =
          (!this.filters.stt || (index + 1).toString() === this.filters.stt.toString()) &&
          (!this.filters.maChiTietSanPham || chiTiet.maChiTietSanPham?.toLowerCase().includes(this.filters.maChiTietSanPham.toLowerCase())) &&
          (!this.filters.tenChiTietSanPham || chiTiet.tenChiTietSanPham?.toLowerCase().includes(this.filters.tenChiTietSanPham.toLowerCase())) &&
          (!this.filters.moTa || chiTiet.moTa?.toLowerCase().includes(this.filters.moTa.toLowerCase())) &&
          (!this.filters.nhaXuatBan || chiTiet.idNhaXuatBan?.tenNhaXuatBan === this.filters.nhaXuatBan) &&
          (!this.filters.trongLuong || chiTiet.trongLuong == this.filters.trongLuong) &&
          (!this.filters.kichThuoc || chiTiet.kichThuoc?.toLowerCase().includes(this.filters.kichThuoc.toLowerCase())) &&
          (!this.filters.donGia || chiTiet.donGia == this.filters.donGia);

        return matchesFilters;
      });
    },
    uniqueNhaXuatBan() {
      const nhaXuatBans = this.selectedChiTietSanPhams.map(chiTiet => chiTiet.idNhaXuatBan?.tenNhaXuatBan).filter(Boolean);
      return [...new Set(nhaXuatBans)];
    },
  },
  methods: {
    formatDate(date) {
      return date ? new Date(date).toLocaleDateString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric' }) : '';
    },
    getDiscountStatus(discount) {
      const today = new Date();
      const startDate = new Date(discount.ngayBatDau);
      const endDate = new Date(discount.ngayKetThuc);

      if (today < startDate) return 'Chưa bắt đầu';
      if (today >= startDate && today <= endDate) return 'Đang diễn ra';
      return 'Đã kết thúc';
    },
    async fetchDiscounts() {
      this.loading = true;
      try {
        const discounts = await DotGiamGiaService.getListDiscounts();
        this.discountList = discounts
          .map((discount) => ({
            ...discount,
            maDotGiamGia: discount.maDotGiamGia || '',
            TDG: discount.tenDotGiamGia || '',
            loaiGiamGia: discount.loaiGiamGia || 'Phần trăm',
            soPhanTramGiam: discount.soPhanTramGiam || 0,
            giaTriGiam: discount.giaTriGiam || 0,
            ngayBatDau: discount.ngayBatDau,
            ngayKetThuc: discount.ngayKetThuc,
            trangThai: this.getDiscountStatus(discount),
            dotGiamGiaChiTiets: discount.dotGiamGiaChiTiets || [],
          }))
          .sort((a, b) => b.id - a.id);
      } catch (error) {
        console.error('Lỗi khi lấy dữ liệu:', error);
        this.errorMessage = error.message;
      } finally {
        this.loading = false;
      }
    },
    async fetchProducts() {
      this.loadingProducts = true;
      try {
        const products = await DotGiamGiaService.getAllSanPham();
        this.products = await Promise.all(
          products.map(async (product) => {
            let chiTietSanPhams = product.chiTietSanPhams;
            if (!chiTietSanPhams || chiTietSanPhams.length === 0) {
              chiTietSanPhams = await DotGiamGiaService.getChiTietSanPhamBySanPhamId(product.id);
            }
            const totalSoLuongTon = chiTietSanPhams
              ? chiTietSanPhams.reduce((sum, chiTiet) => {
                  const soLuongTon = chiTiet.soLuongTon || chiTiet.so_luong_ton || 0;
                  return sum + soLuongTon;
                }, 0)
              : 0;
            return {
              ...product,
              tenSanPham: product.tenSanPham || 'Không xác định',
              maSanPham: product.maSanPham || 'Không có mã',
              moTa: product.moTa || 'Không có mô tả',
              soLuongTon: totalSoLuongTon,
            };
          })
        );
      } catch (error) {
        console.error('Lỗi khi lấy sản phẩm:', error);
        this.errorMessage = error.message || 'Không thể lấy danh sách sản phẩm!';
      } finally {
        this.loadingProducts = false;
      }
    },
    async fetchChiTietSanPham(idSanPham) {
      try {
        const details = await DotGiamGiaService.getChiTietSanPhamBySanPhamId(idSanPham);
        return details.map((detail) => ({
          ...detail,
          maChiTietSanPham: detail.maChiTietSanPham || 'Không có mã',
          tenChiTietSanPham: detail.tenChiTietSanPham || 'Không xác định',
          moTa: detail.moTa || 'Không có mô tả',
          soLuongTon: detail.soLuongTon || detail.so_luong_ton || 0,
          donGia: detail.donGia || detail.don_gia || detail.gia || detail.price || 0,
          trongLuong: detail.trongLuong || detail.trong_luong || 0,
          kichThuoc: detail.kichThuoc || detail.kich_thuoc || '',
          idNhaXuatBan: detail.idNhaXuatBan || { tenNhaXuatBan: 'Không xác định' },
          idSanPham: idSanPham,
        }));
      } catch (error) {
        console.error('Lỗi khi lấy chi tiết sản phẩm:', error);
        this.errorMessage = error.message || 'Không thể lấy chi tiết sản phẩm!';
        return [];
      }
    },
    async toggleProductSelection(productId) {
      const index = this.formData.selectedProducts.indexOf(productId);
      if (index === -1) {
        this.formData.selectedProducts.push(productId);
        const chiTietSanPhams = await this.fetchChiTietSanPham(productId);
        this.selectedChiTietSanPhams = [...this.selectedChiTietSanPhams, ...chiTietSanPhams];
      } else {
        this.formData.selectedProducts.splice(index, 1);
        this.selectedChiTietSanPhams = this.selectedChiTietSanPhams.filter(
          (chiTiet) => chiTiet.idSanPham !== productId
        );
        this.selectedChiTietSanPhamIds = this.selectedChiTietSanPhamIds.filter(
          (id) => !this.selectedChiTietSanPhams.some((chiTiet) => (chiTiet.tempId || chiTiet.id) === id)
        );
      }
    },
    toggleChiTietSelection(chiTiet) {
      const chiTietId = chiTiet.tempId || chiTiet.id;
      const index = this.selectedChiTietSanPhamIds.indexOf(chiTietId);
      if (index === -1) {
        this.selectedChiTietSanPhamIds.push(chiTietId);
      } else {
        this.selectedChiTietSanPhamIds.splice(index, 1);
      }
    },
    async addProductToSelected(product) {
      if (!this.formData.selectedProducts.includes(product.id)) {
        this.formData.selectedProducts.push(product.id);
      }
      const chiTietSanPhams = await this.fetchChiTietSanPham(product.id);
      this.selectedChiTietSanPhams.unshift(...chiTietSanPhams);
    },
    duplicateChiTietSanPham(chiTiet) {
      const newChiTiet = {
        ...chiTiet,
        tempId: Date.now() + Math.random().toString(36).substr(2, 9),
      };
      this.selectedChiTietSanPhams.unshift(newChiTiet);
    },
    deleteChiTietSanPham(chiTiet) {
      const chiTietId = chiTiet.tempId || chiTiet.id;
      this.selectedChiTietSanPhams = this.selectedChiTietSanPhams.filter(
        (item) => (item.tempId || item.id) !== chiTietId
      );
      this.selectedChiTietSanPhamIds = this.selectedChiTietSanPhamIds.filter(
        (id) => id !== chiTietId
      );
      const productId = chiTiet.idSanPham;
      const hasOtherChiTiet = this.selectedChiTietSanPhams.some(
        (item) => item.idSanPham === productId
      );
      if (!hasOtherChiTiet) {
        this.formData.selectedProducts = this.formData.selectedProducts.filter(
          (id) => id !== productId
        );
      }
    },
    viewChiTietSanPhamDetails(chiTiet) {
      this.selectedChiTiet = chiTiet;
      this.showChiTietModal = true;
    },
    closeChiTietModal() {
      this.showChiTietModal = false;
      this.selectedChiTiet = null;
    },
    toggleFilter(column) {
      this.activeFilter = this.activeFilter === column ? '' : column;
    },
    toggleSpeechRecognition() {
      if (this.isSpeechActive) {
        this.stopSpeechRecognition();
      } else {
        this.startSpeechRecognition();
      }
    },
    startSpeechRecognition() {
      if (!('webkitSpeechRecognition' in window)) {
        alert('Trình duyệt của bạn không hỗ trợ nhận diện giọng nói. Vui lòng sử dụng Chrome hoặc trình duyệt hỗ trợ SpeechRecognition.');
        return;
      }

      this.isSpeechActive = true;
      this.filters.stt = '';

      const SpeechRecognition = window.webkitSpeechRecognition || window.SpeechRecognition;
      this.speechRecognition = new SpeechRecognition();
      this.speechRecognition.lang = 'vi-VN';
      this.speechRecognition.continuous = false;
      this.speechRecognition.interimResults = false;

      this.speechRecognition.onresult = (event) => {
        const transcript = event.results[0][0].transcript.trim();
        const number = this.convertSpeechToNumber(transcript);
        if (number) {
          this.filters.stt = number.toString();
          this.$forceUpdate();
        } else {
          this.errorMessage = 'Không nhận diện được số STT. Vui lòng thử lại!';
        }
        this.stopSpeechRecognition();
      };

      this.speechRecognition.onerror = (event) => {
        console.error('Lỗi nhận diện giọng nói:', event.error);
        this.errorMessage = 'Có lỗi xảy ra khi nhận diện giọng nói. Vui lòng thử lại!';
        this.stopSpeechRecognition();
      };

      this.speechRecognition.onend = () => {
        this.isSpeechActive = false;
      };

      this.speechRecognition.start();
    },
    stopSpeechRecognition() {
      if (this.speechRecognition) {
        this.speechRecognition.stop();
        this.isSpeechActive = false;
      }
    },
    convertSpeechToNumber(transcript) {
      const numberWords = {
        'một': 1,
        'hai': 2,
        'ba': 3,
        'bốn': 4,
        'năm': 5,
        'sáu': 6,
        'bảy': 7,
        'tám': 8,
        'chín': 9,
        'mười': 10,
      };

      const normalized = transcript.toLowerCase().trim();
      if (/^\d+$/.test(normalized)) {
        return parseInt(normalized);
      }
      return numberWords[normalized] || null;
    },
    openCreateForm() {
      this.isUpdate = false;
      this.selectedDiscount = null;
      this.errorMessage = '';
      this.formData = {
        id: null,
        maDotGiamGia: '',
        tenDotGiamGia: '',
        loaiGiamGia: 'Phần trăm',
        soPhanTramGiam: 0,
        giaTriGiam: 0,
        ngayBatDau: '',
        ngayKetThuc: '',
        trangThai: 'Chưa bắt đầu',
        selectedProducts: [],
      };
      this.selectedChiTietSanPhams = [];
      this.selectedChiTietSanPhamIds = [];
      this.showForm = true;
      this.currentProductPage = 1;
      this.filters = {
        maChiTietSanPham: '',
        tenChiTietSanPham: '',
        moTa: '',
        nhaXuatBan: '',
        trongLuong: '',
        kichThuoc: '',
        donGia: '',
        stt: '',
      };
      this.activeFilter = '';
      this.isSpeechActive = false;
      this.fetchProducts();
    },
    openUpdateForm(discount) {
      this.isUpdate = true;
      this.selectedDiscount = discount;
      this.errorMessage = '';
      this.formData = {
        id: discount.id,
        maDotGiamGia: discount.maDotGiamGia || '',
        tenDotGiamGia: discount.tenDotGiamGia || '',
        loaiGiamGia: discount.loaiGiamGia || 'Phần trăm',
        soPhanTramGiam: discount.soPhanTramGiam || 0,
        giaTriGiam: discount.giaTriGiam || 0,
        ngayBatDau: discount.ngayBatDau ? new Date(discount.ngayBatDau).toISOString().split('T')[0] : '',
        ngayKetThuc: discount.ngayKetThuc ? new Date(discount.ngayKetThuc).toISOString().split('T')[0] : '',
        trangThai: discount.trangThai || 'Chưa bắt đầu',
        selectedProducts: discount.dotGiamGiaChiTiets
          ? [...new Set(discount.dotGiamGiaChiTiets.map(item => item.idChiTietSanPham?.idSanPham?.id).filter(id => id))]
          : [],
      };
      this.selectedChiTietSanPhamIds = [];
      this.updateSelectedChiTietSanPhams();
      this.showForm = false;
      this.currentProductPage = 1;
      this.filters = {
        maChiTietSanPham: '',
        tenChiTietSanPham: '',
        moTa: '',
        nhaXuatBan: '',
        trongLuong: '',
        kichThuoc: '',
        donGia: '',
        stt: '',
      };
      this.activeFilter = '';
      this.isSpeechActive = false;
      this.fetchProducts();
    },
    async updateSelectedChiTietSanPhams() {
      this.selectedChiTietSanPhams = [];
      for (const productId of this.formData.selectedProducts) {
        const chiTietSanPhams = await this.fetchChiTietSanPham(productId);
        this.selectedChiTietSanPhams = [...this.selectedChiTietSanPhams, ...chiTietSanPhams];
      }
    },
    closeForm() {
      this.showForm = false;
      this.selectedDiscount = null;
      this.errorMessage = '';
      this.isDateInvalid = false;
      this.selectedChiTietSanPhams = [];
      this.selectedChiTietSanPhamIds = [];
      this.formData.selectedProducts = [];
      this.products = [];
      this.currentProductPage = 1;
      this.filters = {
        maChiTietSanPham: '',
        tenChiTietSanPham: '',
        moTa: '',
        nhaXuatBan: '',
        trongLuong: '',
        kichThuoc: '',
        donGia: '',
        stt: '',
      };
      this.activeFilter = '';
      this.isSpeechActive = false;
    },
    validateDates() {
      if (!this.formData.ngayBatDau || !this.formData.ngayKetThuc) {
        this.isDateInvalid = false;
        this.errorMessage = '';
        return;
      }
      const startDate = new Date(this.formData.ngayBatDau);
      const endDate = new Date(this.formData.ngayKetThuc);
      this.isDateInvalid = startDate >= endDate;
      if (this.isDateInvalid) {
        this.errorMessage = 'Ngày bắt đầu phải nhỏ hơn ngày kết thúc!';
      } else {
        this.errorMessage = '';
      }
    },
    validateForm() {
      if (!this.formData.tenDotGiamGia || !this.formData.ngayBatDau || !this.formData.ngayKetThuc) {
        this.errorMessage = 'Vui lòng điền đầy đủ các trường bắt buộc!';
        return false;
      }
      if (this.isDateInvalid) {
        this.errorMessage = 'Ngày bắt đầu phải nhỏ hơn ngày kết thúc!';
        return false;
      }
      if (this.formData.loaiGiamGia === 'Phần trăm' && (this.formData.soPhanTramGiam <= 0 || this.formData.soPhanTramGiam > 100)) {
        this.errorMessage = 'Giá trị giảm phải từ 1 đến 100!';
        return false;
      }
      if (this.formData.loaiGiamGia === 'Tiền mặt' && this.formData.giaTriGiam <= 0) {
        this.errorMessage = 'Số tiền tối đa phải lớn hơn 0!';
        return false;
      }
      if (!this.isUpdate && this.selectedChiTietSanPhams.length === 0) {
        this.errorMessage = 'Vui lòng chọn ít nhất một chi tiết sản phẩm!';
        return false;
      }
      return true;
    },
    async handleSubmit() {
      this.showConfirmModal('submit');
    },
    async deleteDiscount(discountId) {
      this.showConfirmModal('delete', discountId);
    },
    showConfirmModal(actionType, discountId = null) {
      this.confirmActionType = actionType;
      this.confirmDiscountId = discountId;
      if (actionType === 'submit') {
        if (!this.validateForm()) return;
        this.confirmMessage = this.isUpdate
          ? 'Bạn có chắc chắn muốn cập nhật đợt giảm giá này không?'
          : 'Bạn có chắc chắn muốn tạo mới đợt giảm giá này không?';
      } else if (actionType === 'delete') {
        this.confirmMessage = 'Bạn có chắc chắn muốn xóa đợt giảm giá này không?';
      }
      this.showConfirmModalFlag = true;
    },
    closeConfirmModal() {
      this.showConfirmModalFlag = false;
      this.confirmMessage = '';
      this.confirmActionType = '';
      this.confirmDiscountId = null;
    },
    async confirmAction() {
      if (this.confirmActionType === 'submit') {
        const chiTietSanPhamIds = this.selectedChiTietSanPhams
          .filter(chiTiet => !chiTiet.tempId)
          .map(chiTiet => chiTiet.id);

        const payload = {
          tenDotGiamGia: this.formData.tenDotGiamGia,
          loaiGiamGia: this.formData.loaiGiamGia,
          soPhanTramGiam: this.formData.loaiGiamGia === 'Phần trăm' ? this.formData.soPhanTramGiam : null,
          giaTriGiam: this.formData.loaiGiamGia === 'Tiền mặt' ? this.formData.giaTriGiam : null,
          ngayBatDau: new Date(this.formData.ngayBatDau).toISOString(),
          ngayKetThuc: new Date(this.formData.ngayKetThuc).toISOString(),
          chiTietSanPhamIds: chiTietSanPhamIds,
        };

        try {
          if (this.isUpdate) {
            const response = await DotGiamGiaService.updateDiscount(this.formData.id, payload);
            const index = this.discountList.findIndex((d) => d.id === this.formData.id);
            if (index !== -1) {
              this.discountList.splice(index, 1, {
                ...response,
                maDotGiamGia: response.maDotGiamGia || '',
                loaiGiamGia: response.loaiGiamGia || 'Phần trăm',
                soPhanTramGiam: response.soPhanTramGiam || 0,
                giaTriGiam: response.giaTriGiam || 0,
                ngayBatDau: response.ngayBatDau,
                ngayKetThuc: response.ngayKetThuc,
                trangThai: this.getDiscountStatus(response),
                dotGiamGiaChiTiets: response.dotGiamGiaChiTiets || [],
              });
            }
          } else {
            const response = await DotGiamGiaService.createDiscount(payload);
            this.discountList.unshift({
              ...response,
              maDotGiamGia: response.maDotGiamGia || '',
              loaiGiamGia: response.loaiGiamGia || 'Phần trăm',
              soPhanTramGiam: response.soPhanTramGiam || 0,
              giaTriGiam: response.giaTriGiam || 0,
              ngayBatDau: response.ngayBatDau,
              ngayKetThuc: response.ngayKetThuc,
              trangThai: this.getDiscountStatus(response),
              dotGiamGiaChiTiets: response.dotGiamGiaChiTiets || [],
            });
            this.currentPage = 1;
          }
          this.closeForm();
        } catch (error) {
          console.error('Lỗi khi lưu:', error);
          this.errorMessage = error.response?.data?.message || error.message || 'Có lỗi xảy ra khi lưu dữ liệu!';
        }
      } else if (this.confirmActionType === 'delete') {
        try {
          await DotGiamGiaService.deleteDiscount(this.confirmDiscountId);
          const index = this.discountList.findIndex((d) => d.id === this.confirmDiscountId);
          if (index !== -1) {
            this.discountList.splice(index, 1);
          }
          if (this.paginatedDiscounts.length === 0 && this.currentPage > 1) {
            this.currentPage--;
          }
        } catch (error) {
          console.error('Lỗi khi xóa:', error);
          this.errorMessage = error.message || 'Có lỗi xảy ra khi xóa đợt giảm giá!';
        }
      }
      this.closeConfirmModal();
    },
    exportToExcel() {
      const data = this.filteredDiscounts.map((discount, index) => ({
        STT: (this.currentPage - 1) * this.itemsPerPage + index + 1,
        'Tên Đợt giảm giá': discount.tenDotGiamGia,
        'Loại giảm giá': discount.loaiGiamGia,
        'Giá trị giảm': discount.loaiGiamGia === 'Phần trăm' ? `${discount.soPhanTramGiam}%` : `${discount.giaTriGiam} đ`,
        'Trạng thái': discount.trangThai || this.getDiscountStatus(discount),
        'Thời gian bắt đầu': this.formatDate(discount.ngayBatDau),
        'Thời gian kết thúc': this.formatDate(discount.ngayKetThuc),
      }));

      const worksheet = XLSX.utils.json_to_sheet(data);
      const workbook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(workbook, worksheet, 'Discounts');
      XLSX.writeFile(workbook, 'DotGiamGia.xlsx');
    },
    selectAllProducts() {
      this.formData.selectedProducts = this.products.map(product => product.id);
      this.updateSelectedChiTietSanPhams();
    },
    deselectAllProducts() {
      this.formData.selectedProducts = [];
      this.selectedChiTietSanPhams = [];
      this.selectedChiTietSanPhamIds = [];
    },
    prevPage() {
      if (this.currentPage > 1) this.currentPage--;
    },
    nextPage() {
      if (this.currentPage < this.totalPages) this.currentPage++;
    },
    prevProductPage() {
      if (this.currentProductPage > 1) this.currentProductPage--;
    },
    nextProductPage() {
      if (this.currentProductPage < this.totalProductPages) this.currentProductPage++;
    },
  },
  mounted() {
    this.fetchDiscounts();
    this.fetchProducts();
  },
};
</script>

<style scoped>
.font-sans {
  font-family: 'Inter', sans-serif;
}

input:disabled,
select:disabled {
  background-color: #e5e7eb;
  cursor: not-allowed;
  opacity: 0.7;
}

input,
select {
  padding: 0.5rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  background-color: #f9fafb;
  transition: all 0.2s ease-in-out;
}

input:focus,
select:focus {
  outline: none;
  ring: 2px solid #3b82f6;
}

.border-red-500 {
  border-color: #ef4444;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  padding: 0.5rem;
  border-bottom: 1px solid #e5e5e5;
}

th {
  background-color: #f3f4f6;
  font-weight: 600;
  font-size: 0.875rem;
}

tr:hover {
  background-color: #f9fafb;
}

tr:nth-child(even) {
  background-color: #f9fafb;
}

@media (max-width: 768px) {
  .md\:flex-row {
    flex-direction: column;
  }

  .w-48 {
    width: 100%;
  }
}
</style>