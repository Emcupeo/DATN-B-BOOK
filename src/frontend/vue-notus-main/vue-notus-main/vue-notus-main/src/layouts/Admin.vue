<template>
  <div class="flex flex-col min-h-screen bg-gray-100">
    <Sidebar />
    <div class="relative md:ml-64 flex flex-col flex-grow">
      <!-- <admin-navbar /> -->
      <HeaderStats />
      <div class="px-4 md:px-10 mx-auto w-full flex-grow">
        <router-view />
      </div>
      <FooterAdmin />
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
