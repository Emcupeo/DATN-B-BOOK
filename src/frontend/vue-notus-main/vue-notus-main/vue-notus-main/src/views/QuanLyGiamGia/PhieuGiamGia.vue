<template>
    <div class="relative">
      <div class="flex flex-col md:flex-row gap-4">
        <!-- Form nhập phiếu giảm giá -->
        <div class="relative md:w-[500px]">
          <div class="w-full md:w-[300px] shadow-lg rounded p-4 bg-white">
            <h2 class="text-xl font-semibold mb-4">{{ selectedCoupon ? "Chỉnh sửa phiếu giảm giá" : "Thêm phiếu giảm giá" }}</h2>
            <form @submit.prevent="handleSubmit">
              <div class="mb-4">
                <label class="block text-gray-700 mb-1">Tên phiếu</label>
                <input type="text" v-model="form.name" placeholder="Nhập tên phiếu"
                  class="block w-full p-2 border rounded-lg" required />
              </div>
              <div class="mb-4">
                <label class="block text-gray-700 mb-1">Mã phiếu</label>
                <input type="text" v-model="form.code" placeholder="Nhập mã phiếu"
                  class="block w-full p-2 border rounded-lg" required />
              </div>
              <div class="mb-4">
                <label class="block text-gray-700 mb-1">Số lượng</label>
                <input type="number" v-model.number="form.quantity" placeholder="Nhập số lượng"
                  class="block w-full p-2 border rounded-lg" required />
              </div>
              <div class="mb-4">
                <label class="block text-gray-700 mb-1">Phần trăm giảm tối đa</label>
                <input type="number" v-model.number="form.discount" placeholder="Nhập % giảm"
                  class="block w-full p-2 border rounded-lg" required />
              </div>
              <div class="mb-4">
                <label class="block text-gray-700 mb-1">Giá tối thiểu</label>
                <input type="number" v-model.number="form.minPrice" placeholder="Nhập giá tối thiểu"
                  class="block w-full p-2 border rounded-lg" required />
              </div>
              <div class="mb-4">
                <label class="block text-gray-700 mb-1">Ngày bắt đầu</label>
                <input type="date" v-model="form.startDate" class="block w-full p-2 border rounded-lg" required />
              </div>
              <div class="mb-4">
                <label class="block text-gray-700 mb-1">Ngày kết thúc</label>
                <input type="date" v-model="form.endDate" class="block w-full p-2 border rounded-lg" required />
              </div>
              <div class="mb-4">
                <label class="block text-gray-700 mb-1">Loại</label>
                <select v-model="form.type" class="block w-full p-2 border rounded-lg" required>
                  <option value="personal">Cá nhân</option>
                  <option value="public">Mọi người</option>
                </select>
              </div>
              <div class="flex justify-end space-x-2">
                <button type="button" class="bg-gray-300 px-4 py-2 rounded" @click="cancel">Hủy</button>
                <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded">{{ selectedCoupon ? "Cập nhật" : "Thêm mới" }}</button>
              </div>
            </form>
          </div>
        </div>
  
        <!-- Bảng hiển thị phiếu giảm giá -->
        <div class="w-2/3 bg-white p-4 shadow rounded">
  <h2 class="text-lg font-bold mb-4">Danh sách phiếu giảm giá</h2>
  <div class="flex items-center">
    <input type="text" v-model="searchQuery" placeholder="Tìm kiếm phiếu giảm giá..."
      class="block w-full p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500 mb-3" />
  </div>

  <div class="relative overflow-x-auto sm:rounded-lg">
    <table class="w-full text-sm text-left text-gray-500">
      <thead class="text-xs text-gray-700 uppercase bg-gray-50">
        <tr>
          <th scope="col" class="px-6 py-3">Mã phiếu</th>
          <th scope="col" class="px-6 py-3">Mô tả</th>
          <th scope="col" class="px-6 py-3">Giảm giá</th>
          <th scope="col" class="px-6 py-3">Ngày hết hạn</th>
          <th scope="col" class="px-6 py-3">Hành động</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(coupon, index) in filteredCoupons" :key="index" class="bg-white border-b hover:bg-gray-50">
          <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
            {{ coupon.code }}
          </td>
          <td class="px-6 py-4">{{ coupon.description }}</td>
          <td class="px-6 py-4">{{ coupon.discount }}%</td>
          <td class="px-6 py-4">{{ coupon.expiryDate }}</td>
          <td class="px-6 py-4">
            <a href="#" @click.prevent="editCoupon(index)" class="font-medium text-blue-600 hover:underline">Edit</a>
            <a href="#" @click.prevent="removeCoupon(index)" class="font-medium text-red-600 hover:underline ml-3">Remove</a>
          </td>
        </tr>
        <tr v-if="!coupons.length" class="bg-white border-b hover:bg-gray-50">
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
  export default {
    data() {
      return {
        coupons: [],
        selectedCoupon: null,
        form: {
          name: "",
          code: "",
          quantity: 0,
          discount: 0,
          minPrice: 0,
          startDate: "",
          endDate: "",
          type: "personal",
        },
      };
    },
    methods: {
      handleSubmit() {
        if (this.selectedCoupon) {
          Object.assign(this.selectedCoupon, this.form);
        } else {
          this.coupons.push({ ...this.form });
        }
        this.cancel();
      },
      editCoupon(coupon) {
        this.selectedCoupon = coupon;
        this.form = { ...coupon };
      },
      deleteCoupon(index) {
        this.coupons.splice(index, 1);
      },
      cancel() {
        this.selectedCoupon = null;
        this.form = { name: "", code: "", quantity: 0, discount: 0, minPrice: 0, startDate: "", endDate: "", type: "personal" };
      },
    },
  };
  </script>
  
  <style scoped>
  /* CSS tùy chỉnh */
  </style>
  