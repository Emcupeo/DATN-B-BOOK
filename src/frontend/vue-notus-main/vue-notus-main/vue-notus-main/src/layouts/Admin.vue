<template>
  <div class="flex flex-col min-h-screen bg-gray-100">
    <Sidebar />
    <div class="relative md:ml-64 flex flex-col flex-grow">
      <!-- <admin-navbar /> -->
      <HeaderStats />
      <div class="px-4 md:px-11 mx-auto w-full flex-grow font-roboto">
        <!-- Thêm transition ngang quanh router-view -->
        <div class="min-h-[calc(100vh-56px)] bg-white px-4 pt-4 pb-1 shadow rounded w-full">
          <transition name="slide" mode="out-in">
            <router-view />
          </transition>
        </div>
      </div>
      <!-- Thu gọn FooterAdmin -->
      <FooterAdmin class="py-2"/> <!-- Giảm padding-y từ mặc định xuống py-2 -->
    </div>
  </div>
</template>

<script>
// import AdminNavbar from "@/components/Navbars/AdminNavbar.vue";
import Sidebar from "@/components/Sidebar/Sidebar.vue";
import HeaderStats from "@/components/Headers/HeaderStats.vue";
import FooterAdmin from "@/components/Footers/FooterAdmin.vue";

export default {
  name: "admin-layout",
  components: {
    // AdminNavbar,
    Sidebar,
    HeaderStats,
    FooterAdmin,
  },
};
</script>

<style scoped>
/* Hiệu ứng trượt ngang cho router-view */
.slide-enter-active,
.slide-leave-active {
  transition: all 0.5s ease; /* 0.5 giây, easing mượt mà */
}
.slide-enter-from {
  opacity: 0;
  transform: translateX(20px); /* Trượt từ phải vào */
}
.slide-enter-to {
  opacity: 1;
  transform: translateX(0);
}
.slide-leave-from {
  opacity: 1;
  transform: translateX(0);
}
.slide-leave-to {
  opacity: 0;
  transform: translateX(-20px); /* Trượt ra bên trái */
}

/* Tùy chỉnh thêm cho FooterAdmin nếu cần */
footer {
  min-height: 40px; /* Giảm chiều cao tối thiểu của footer */
}
</style>