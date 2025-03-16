<template>
  <nav
    class="md:left-0 md:block md:fixed md:top-0 md:bottom-0 md:overflow-y-auto md:flex-row md:flex-nowrap md:overflow-hidden shadow-xl bg-white flex flex-wrap items-center justify-between relative md:w-64 z-10 py-4 px-6 font-roboto">
    <div
      class="md:flex-col md:items-stretch md:min-h-full md:flex-nowrap px-0 flex flex-wrap items-center justify-between w-full mx-auto">

      <!-- Toggler -->
      <button
        class="cursor-pointer text-black opacity-50 md:hidden px-3 py-1 text-xl leading-none bg-transparent rounded border border-solid border-transparent"
        type="button" @click="toggleCollapseShow('bg-white m-2 py-3 px-6')">
        <i class="fas fa-bars"></i>
      </button>
      <!-- Brand -->
      <router-link
        class="md:block text-left md:pb-2 text-blueGray-600 mr-0 inline-block whitespace-nowrap text-sm uppercase font-bold p-4 px-0"
        to="/">
        <img src="@/assets/img/logo.png" alt="Logo" style="width: 120px; margin-left: 35px;">
      </router-link>
      <!-- User Dropdown (hiển thị ở mobile) -->
      <ul class="md:hidden items-center flex flex-wrap list-none">
        <li class="inline-block relative">
          <notification-dropdown />
        </li>
        <li class="inline-block relative">
          <user-dropdown />
        </li>
      </ul>
      <!-- Collapse (Menu chính) -->
      <div
        class="md:flex md:flex-col md:items-stretch md:opacity-100 md:relative md:mt-4 md:shadow-none shadow absolute top-0 left-0 right-0 z-40 overflow-y-auto overflow-x-hidden h-auto items-center flex-1 rounded"
        :class="collapseShow">
        <!-- Collapse header (Mobile) -->
        <div class="md:min-w-full md:hidden block pb-4 mb-4 border-b border-solid border-blueGray-200">
          <div class="flex flex-wrap">
            <div class="w-6/12">
              <router-link
                class="md:block text-left md:pb-2 text-blueGray-600 mr-0 inline-block whitespace-nowrap text-sm uppercase font-bold p-4 px-0"
                to="/">
                <img src="@/assets/img/logo.png" alt="Logo" style="width: 50px;">
              </router-link>
            </div>
            <div class="w-6/12 flex justify-end">
              <button type="button"
                class="cursor-pointer text-black opacity-50 md:hidden px-3 py-1 text-xl leading-none bg-transparent rounded border border-solid border-transparent"
                @click="toggleCollapseShow('hidden')">
                <i class="fas fa-times"></i>
              </button>
            </div>
          </div>
        </div>
        <!-- Form tìm kiếm Mobile -->
        <form class="mt-6 mb-4 md:hidden">
          <div class="mb-3 pt-0">
            <input type="text" placeholder="Search"
              class="border-0 px-3 py-2 h-12 border border-solid border-blueGray-500 placeholder-blueGray-300 text-blueGray-600 bg-white rounded text-base leading-snug shadow-none outline-none focus:outline-none w-full font-normal" />
          </div>
        </form>

        <!-- Divider -->
        <hr class="my-4 md:min-w-full" />
        <!-- Heading: Bán hàng -->
        <h6 class="md:min-w-full text-blueGray-500 text-xs uppercase font-bold block pt-1 pb-4 no-underline">
          Bán hàng
        </h6>
        <!-- Navigation: Bán hàng -->
        <ul class="md:flex-col md:min-w-full flex flex-col list-none">
          <!-- Dashboard -->
          <li class="items-center">
            <router-link to="/admin/dashboard" v-slot="{ href, navigate, isActive }">
              <a :href="href" @click="navigate" class="text-xs uppercase py-3 font-bold block"
                :class="isActive ? 'text-blue-500 hover:text-blue-600' : 'text-blueGray-700 hover:text-blueGray-500'">
                <i class="fas fa-chart-line mr-2 text-sm" :class="isActive ? 'opacity-75' : 'text-blueGray-300'"></i>
                Dashboard
              </a>
            </router-link>
          </li>
          <!-- Bán hàng tại quầy -->
          <li class="items-center">
            <router-link to="/admin/ban-hang-tai-quay" v-slot="{ href, navigate, isActive }">
              <a :href="href" @click="navigate" class="text-xs uppercase py-3 font-bold block"
                :class="isActive ? 'text-blue-500 hover:text-blue-600' : 'text-blueGray-700 hover:text-blueGray-500'">
                <i class="fas fa-shopping-cart mr-2 text-sm" :class="isActive ? 'opacity-75' : 'text-blueGray-300'"></i>
                Bán hàng tại quầy
              </a>
            </router-link>
          </li>
          <!-- Hóa đơn -->
          <li class="items-center">
            <router-link to="/admin/hoa-don" v-slot="{ href, navigate, isActive }">
              <a :href="href" @click="navigate" class="text-xs uppercase py-3 font-bold block"
                :class="isActive ? 'text-blue-500 hover:text-blue-600' : 'text-blueGray-700 hover:text-blueGray-500'">
                <i class="fas fa-file-invoice-dollar mr-2 text-sm"
                  :class="isActive ? 'opacity-75' : 'text-blueGray-300'"></i>
                Hóa đơn
              </a>
            </router-link>
          </li>
        </ul>

        <!-- Divider -->
        <hr class="my-4 md:min-w-full" />
        <!-- Heading: Quản lý sách -->
        <h6 class="md:min-w-full text-blueGray-500 text-xs uppercase font-bold block pt-1 pb-4 no-underline">
          Quản lý sách
        </h6>
        <!-- Navigation: Quản lý sách -->
        <ul class="md:flex-col md:min-w-full flex flex-col list-none md:mb-4">
          <!-- Sách -->
          <li class="items-center">
            <router-link to="/admin/sach" v-slot="{ href, navigate, isActive }">
              <a :href="href" @click="navigate" class="text-xs uppercase py-3 font-bold block"
                :class="isActive ? 'text-blue-500 hover:text-blue-600' : 'text-blueGray-700 hover:text-blueGray-500'">
                <i class="fas fa-book mr-2 text-sm" :class="isActive ? 'opacity-75' : 'text-blueGray-300'"></i>
                Sách
              </a>
            </router-link>
          </li>
          <!-- Danh mục sách -->
          <li class="items-center">
            <router-link to="/admin/danh-muc" v-slot="{ href, navigate, isActive }">
              <a :href="href" @click="navigate" class="text-xs uppercase py-3 font-bold block"
                :class="isActive ? 'text-blue-500 hover:text-blue-600' : 'text-blueGray-700 hover:text-blueGray-500'">
                <i class="fas fa-list mr-2 text-sm" :class="isActive ? 'opacity-75' : 'text-blueGray-300'"></i>
                Danh mục sách
              </a>
            </router-link>
          </li>
          <!-- Thuộc tính sách -->
          <li class="items-center">
            <button @click="isThuocTinhOpen = !isThuocTinhOpen"
              class="text-xs uppercase py-3 font-bold block w-full text-left"
              :class="isThuocTinhOpen ? 'text-blue-500' : 'text-blueGray-700 hover:text-blueGray-500'">
              <i class="fas fa-tags mr-2 text-sm" :class="isThuocTinhOpen ? 'opacity-75' : 'text-blueGray-300'"></i>
              Thuộc tính sách
              <i class="fas" :class="isThuocTinhOpen ? 'fa-chevron-down' : 'fa-chevron-right'"></i>
            </button>
          </li>
          <ul v-show="isThuocTinhOpen" class="ml-6">
            <li v-for="thuocTinh in danhSachThuocTinh" :key="thuocTinh.path" class="items-center">
              <router-link :to="thuocTinh.path" v-slot="{ href, navigate, isActive }">
                <a :href="href" @click="navigate" class="text-xs uppercase py-2 font-bold block"
                  :class="isActive ? 'text-blue-500' : 'text-blueGray-700 hover:text-blueGray-500'">
                  <i :class="thuocTinh.icon + ' mr-2 text-xs'"></i> {{ thuocTinh.name }}
                </a>
              </router-link>
            </li>
          </ul>
        </ul>

        <!-- Divider -->
        <hr class="my-4 md:min-w-full" />
        <!-- Heading: Quản lý giảm giá -->
        <h6 class="md:min-w-full text-blueGray-500 text-xs uppercase font-bold block pt-1 pb-4 no-underline">
          Quản lý giảm giá
        </h6>
        <!-- Navigation: Quản lý giảm giá -->
        <ul class="md:flex-col md:min-w-full flex flex-col list-none md:mb-4">
          <!-- Phiếu giảm giá -->
          <li class="items-center">
            <router-link to="/admin/phieu-giam-gia" v-slot="{ href, navigate, isActive }">
              <a :href="href" @click="navigate" class="text-xs uppercase py-3 font-bold block"
                :class="isActive ? 'text-blue-500 hover:text-blue-600' : 'text-blueGray-700 hover:text-blueGray-500'">
                <i class="fas fa-ticket-alt mr-2 text-sm" :class="isActive ? 'opacity-75' : 'text-blueGray-300'"></i>
                Phiếu giảm giá
              </a>
            </router-link>
          </li>
          <li class="items-center">
            <router-link to="/admin/dot-giam-gia" v-slot="{ href, navigate, isActive }">
              <a :href="href" @click="navigate" class="text-xs uppercase py-3 font-bold block"
                 :class="isActive ? 'text-blue-500 hover:text-blue-600' : 'text-blueGray-700 hover:text-blueGray-500'">
                <i class="fas fa-ticket-alt mr-2 text-sm" :class="isActive ? 'opacity-75' : 'text-blueGray-300'"></i>
                Đợt giảm giá
              </a>
            </router-link>
          </li>
        </ul>

        <!-- Divider -->
        <hr class="my-4 md:min-w-full" />
        <!-- Heading: Tài khoản -->
        <h6 class="md:min-w-full text-blueGray-500 text-xs uppercase font-bold block pt-1 pb-4 no-underline">
          Tài khoản
        </h6>
        <!-- Navigation: Tài khoản -->
        <ul class="md:flex-col md:min-w-full flex flex-col list-none md:mb-4">
          <!-- Nhân viên -->
          <li class="items-center">
            <router-link to="/admin/nhan-vien" v-slot="{ href, navigate, isActive }">
              <a :href="href" @click="navigate" class="text-xs uppercase py-3 font-bold block"
                :class="isActive ? 'text-blue-500 hover:text-blue-600' : 'text-blueGray-700 hover:text-blueGray-500'">
                <i class="fas fa-users mr-2 text-sm" :class="isActive ? 'opacity-75' : 'text-blueGray-300'"></i>
                Nhân viên
              </a>
            </router-link>
          </li>
          <!-- Khách hàng -->
          <li class="items-center">
            <router-link to="/admin/khach-hang" v-slot="{ href, navigate, isActive }">
              <a :href="href" @click="navigate" class="text-xs uppercase py-3 font-bold block"
                :class="isActive ? 'text-blue-500 hover:text-blue-600' : 'text-blueGray-700 hover:text-blueGray-500'">
                <i class="fas fa-user mr-2 text-sm" :class="isActive ? 'opacity-75' : 'text-blueGray-300'"></i>
                Khách hàng
              </a>
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import NotificationDropdown from "@/components/Dropdowns/NotificationDropdown.vue";
import UserDropdown from "@/components/Dropdowns/UserDropdown.vue";

export default {
  name: "Sidebar",
  data() {
    return {
      collapseShow: "hidden",
      isThuocTinhOpen: false,
      danhSachThuocTinh: [
        { path: "/admin/thuoc-tinh/chat-lieu", name: "Chất liệu", icon: "fas fa-paint-roller" },
        { path: "/admin/thuoc-tinh/loai-bia", name: "Loại bìa", icon: "fas fa-book-open" },
        { path: "/admin/thuoc-tinh/ngon-ngu", name: "Ngôn ngữ", icon: "fas fa-language" },
        { path: "/admin/thuoc-tinh/nguoi-dich", name: "Người dịch", icon: "fas fa-user-edit" },
        { path: "/admin/thuoc-tinh/nha-xuat-ban", name: "Nhà xuất bản", icon: "fas fa-building" },
        { path: "/admin/thuoc-tinh/tac-gia", name: "Tác giả", icon: "fas fa-pen" },
        { path: "/admin/thuoc-tinh/the-loai", name: "Thể loại", icon: "fas fa-bookmark" },
      ],
    };
  },
  methods: {
    toggleCollapseShow(classes) {
      this.collapseShow = classes;
    },
  },
  components: {
    NotificationDropdown,
    UserDropdown,
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');

.font-roboto {
  font-family: 'Roboto', sans-serif;
}
</style>