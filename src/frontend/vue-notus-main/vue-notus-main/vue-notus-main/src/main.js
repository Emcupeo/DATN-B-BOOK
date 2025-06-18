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
import PhieuGiamGiaForm from "@/views/QuanLyGiamGia/PhieuGiamGiaForm.vue";
import DotGiamGia from "@/views/QuanLyGiamGia/DotGiamGia.vue";
import BoSach from "@/views/QuanLySach/BoSach.vue";
import Sach from "@/views/QuanLySach/Sach.vue";
// import EditSach from "@/views/QuanLySach/EditSach.vue";
// import AddSach from "@/views/QuanLySach/AddSach.vue";
// thuoc tinh cua thuoc tinh
import ChatLieu from "@/views/QuanLySach/ThuocTinh/ChatLieu.vue";
import LoaiBia from "@/views/QuanLySach/ThuocTinh/LoaiBia.vue";
import NgonNgu from "@/views/QuanLySach/ThuocTinh/NgonNgu.vue";
import NguoiDich from "@/views/QuanLySach/ThuocTinh/NguoiDich.vue";
import NhaXuatBan from "@/views/QuanLySach/ThuocTinh/NhaXuatBan.vue";
import TacGia from "@/views/QuanLySach/ThuocTinh/TacGia.vue";
import TheLoai from "@/views/QuanLySach/ThuocTinh/TheLoai.vue";
//
import NhanVien from "@/views/TaiKhoan/NhanVien.vue";
import KhachHang from "@/views/TaiKhoan/KhachHang.vue";
import QuanLyHoaDon from "@/views/BanHang/QuanLyHoaDon.vue";
import AddChiTietSach from "./views/QuanLySach/AddChiTietSach.vue";
import EditChiTietSach from "./views/QuanLySach/EditChiTietSach.vue";
import ChiTietSach from "./views/QuanLySach/ChiTietSach.vue";
import KhachHangDetail from "./views/TaiKhoan/KhachHangDetail.vue";
import AllSach from "@/views/QuanLySach/AllSach.vue";
import AddBoSach from "@/views/QuanLySach/AddBoSach.vue";

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
        component: QuanLyHoaDon,
      },
      {
        path: "/admin/sach",
        name: "Sản phẩm",
        component: Sach,
      },
      {
        path: "/admin/sach/add/",
        name: "AddChiTietSach",
        component: AddChiTietSach,
      },

      {
        path: "/admin/sach/edit/:id",
        name: "EditChiTietSach",
        component: EditChiTietSach,
      },
      {
        path: "/admin/sach/:id",
        name: "ChiTietSach",
        component: ChiTietSach,
      },
      {
        path: '/admin/sach/all-sach',
        name: 'AllSach',
        component: AllSach,
      },
      {
        path: "/admin/bo-sach",
        component: BoSach,
      },
      {
        path: "/admin/bo-sach/add",
        component: AddBoSach
      },



      {
        path: "/admin/thuoc-tinh",
        redirect: "/admin/thuoc-tinh/chat-lieu",
      },
      {
        path: "/admin/thuoc-tinh/chat-lieu",
        component: ChatLieu,
      },

      {
        path: "/admin/thuoc-tinh/loai-bia",
        component: LoaiBia,
      },

      {
        path: "/admin/thuoc-tinh/ngon-ngu",
        component: NgonNgu,
      },

      {
        path: "/admin/thuoc-tinh/nguoi-dich",
        component: NguoiDich,
      },

      {
        path: "/admin/thuoc-tinh/nha-xuat-ban",
        component: NhaXuatBan,
      },

      {
        path: "/admin/thuoc-tinh/tac-gia",
        component: TacGia,
      },


      {
        path: "/admin/thuoc-tinh/the-loai",
        component: TheLoai,
      },
      //
      {
        path: "/admin/phieu-giam-gia",
        component: PhieuGiamGia,
      },

      {
        path: '/admin/phieu-giam-gia/add',
        name: 'PhieuGiamGiaAdd',
        component: PhieuGiamGiaForm
      },
      {
        path: '/admin/phieu-giam-gia/edit/:id',
        name: 'PhieuGiamGiaEdit',
        component: PhieuGiamGiaForm,
        props: true
      },

      { path: "/admin/dot-giam-gia",
        component: DotGiamGia },

      {
        path: "/admin/nhan-vien",
        component: NhanVien,
      },

      {
        path: "/admin/khach-hang",
        component: KhachHang,
      },
      {
        path: "/admin/khach-hang/:id",
        name: "KhachHangDetail",
        component: KhachHangDetail,
      },
    ],
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
