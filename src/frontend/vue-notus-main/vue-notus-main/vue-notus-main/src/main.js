import { createApp } from "vue";
import { createWebHistory, createRouter } from "vue-router";
import "@/assets/styles/index.css";
import "@/assets/styles/tailwind.css";
import "@/assets/styles/custom.css";

// styles
import "@fortawesome/fontawesome-free/css/all.min.css";
import "@/assets/styles/tailwind.css";

// mouting point for the whole app
import App from "@/App.vue";

// layouts
import Admin from "@/layouts/Admin.vue";
import Auth from "@/layouts/Auth.vue";

// views for Admin layout
import Dashboard from "@/views/admin/Dashboard.vue";
import Settings from "@/views/admin/Settings.vue";
import Tables from "@/views/admin/Tables.vue";
import Maps from "@/views/admin/Maps.vue";

// views for Auth layout
import Login from "@/views/auth/Login.vue";
import Register from "@/views/auth/Register.vue";

// views without layouts
// import Landing from "@/views/Landing.vue";
// import Profile from "@/views/Profile.vue";
// import Index from "@/views/Index.vue";

//guest

// import Home from "@/views/guest/Home.vue"
// import Home from "@/views/guest/BookStore.vue"
// import Home from "@/views/guest/BookStore.vue"

import Home from "./views/guest/Home.vue";
import ProductDetailPage from "./views/guest/ProductDetail.vue";
import CartPage from "./views/guest/Cart.vue";
// pages
import BanHang from "@/views/BanHang/BanHangTaiQuay.vue";
import HoaDon from "@/views/BanHang/HoaDon.vue";
import PhieuGiamGia from "@/views/QuanLyGiamGia/PhieuGiamGia.vue";
import DanhMuc from "@/views/QuanLySach/DanhMucSach.vue";
import Sach from "@/views/QuanLySach/Sach.vue";
import EditSach from "@/views/QuanLySach/EditSach.vue";
import AddSach from "@/views/QuanLySach/AddSach.vue";
import ThuocTinh from "@/views/QuanLySach/ThuocTinh.vue";
import NhanVien from "@/views/TaiKhoan/NhanVien.vue";
import KhachHang from "@/views/TaiKhoan/KhachHang.vue";
import ListSach from "./views/QuanLySach/ListSach.vue";
import QuanLyDonHang from "@/views/BanHang/QuanLyDonHang.vue";

const routes = [
  {
    path: "/admin",
    redirect: "/admin/dashboard",
    component: Admin,
    children: [
      {
        path: "/admin/dashboard",
        component: Dashboard,
      },
      {
        path: "/admin/settings",
        component: Settings,
      },
      {
        path: "/admin/tables",
        component: Tables,
      },
      {
        path: "/admin/maps",
        component: Maps,
      },

      {
        path: "/admin/ban-hang-tai-quay",
        component: BanHang,
      },
      {
        path: "/admin/hoa-don",
        component: HoaDon,
      },
      {
        path: "/admin/hoa-don-chi-tiet/:id",
        component: QuanLyDonHang,
      },

      {
        path: "/admin/sach",
        name: "Sản phẩm",
        component: Sach,
      },
      {
        path: "/admin/sach/edit",
        component: EditSach,
      },

      {
        path: "/admin/sach/add",
        component: AddSach,
      },

      {
        path: "/admin/danh-muc",
        component: DanhMuc,
      },

      {
        path: "/admin/thuoc-tinh",
        component: ThuocTinh,
      },

      {
        path: "/admin/phieu-giam-gia",
        component: PhieuGiamGia,
      },

      {
        path: "/admin/nhan-vien",
        component: NhanVien,
      },

      {
        path: "/admin/khach-hang",
        component: KhachHang,
      },
    ],
  },
  {
    path: "/san-pham",
    component: ListSach,
  },
  {
    path: "/auth",
    redirect: "/auth/login",
    component: Auth,
    children: [
      {
        path: "/auth/login",
        component: Login,
      },
      {
        path: "/auth/register",
        component: Register,
      },
    ],
  },
  { path: "/", component: Home },
  { path: "/product/:id", name: "ProductDetail", component: ProductDetailPage },
  { path: "/cart", component: CartPage },
  // {
  //   path: "/profile",
  //   component: Profile,
  // },
  // {
  //   path: "/",
  //   component: Index,
  // },
  { path: "/:pathMatch(.*)*", redirect: "/" },

  {
    path: "/ban-hang",
    redirect: "/ban-hang/ban-hang-tai-quay",
    component: BanHang,
    children: [],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

createApp(App).use(router).mount("#app");
