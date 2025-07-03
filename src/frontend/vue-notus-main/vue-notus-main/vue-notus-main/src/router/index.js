import { createRouter, createWebHistory } from "vue-router";

// Đảm bảo đường dẫn chính xác
import ThemNhanVien from "../views/TaiKhoan/ThemNhanVien.vue";
import SuaNhanVien from "../views/TaiKhoan/SuaNhanVien.vue";

const routes = [
  {
    path: "/admin/khach-hang",
    name: "KhachHang",
    component: () => import("../views/TaiKhoan/KhachHang.vue"),
  },
  {
    path: "/admin/khach-hang/them-moi",
    name: "ThemKhachHang",
    component: () => import("../views/TaiKhoan/KhachHangForm.vue"),
  },
  {
    path: "/admin/khach-hang/chinh-sua/:id",
    name: "ChinhSuaKhachHang",
    component: () => import("../views/TaiKhoan/KhachHangForm.vue"),
  },

];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
