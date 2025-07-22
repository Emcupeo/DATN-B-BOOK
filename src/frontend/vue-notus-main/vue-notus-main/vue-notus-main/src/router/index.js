import { createRouter, createWebHistory } from "vue-router";

// Đảm bảo đường dẫn chính xác
import ThemNhanVien from "../views/TaiKhoan/ThemNhanVien.vue";
import SuaNhanVien from "../views/TaiKhoan/SuaNhanVien.vue";
import BookDetail from '../views/shop/views/BookDetail.vue';
import BoSachDetail from '../views/shop/views/BoSachDetail.vue';

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
  {
    path: "/login",
    name: "LoginCustomer",
    component: () => import("../views/auth/LoginCustomer.vue"),
  },
  {
    path: "/register",
    name: "RegisterCustomer",
    component: () => import("../views/auth/RegisterCustomer.vue"),
  },
  {
    path: "/admin/login",
    name: "LoginAdmin",
    component: () => import("../views/auth/LoginAdmin.vue"),
  },
  {
    path: "/admin/register",
    name: "RegisterAdmin",
    component: () => import("../views/auth/RegisterAdmin.vue"),
  },
  {
    path: '/shop/book/:id',
    name: 'BookDetail',
    component: BookDetail,
  },
  {
    path: '/shop/bo-sach/:id',
    name: 'BoSachDetail',
    component: BoSachDetail,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
