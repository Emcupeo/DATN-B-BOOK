import { createApp } from "vue";
import { createWebHistory, createRouter } from "vue-router";
import "@/assets/styles/index.css";
import "@/assets/styles/custom.css";

// styles
import "@fortawesome/fontawesome-free/css/all.min.css";

// mouting point for the whole app
import App from "@/App.vue";

// layouts
import Admin from "@/layouts/Admin.vue";
import Auth from "@/layouts/Auth.vue";
import Shop from "@/layouts/Shop.vue";

// views for Admin layout
import Dashboard from "@/views/admin/Dashboard.vue";
import Settings from "@/views/admin/Settings.vue";
import Tables from "@/views/admin/Tables.vue";
import Maps from "@/views/admin/Maps.vue";

// views for Auth layout
import Login from "@/views/auth/Login.vue";
import Register from "@/views/auth/Register.vue";

// Shop views
import ShopHome from "@/views/shop/views/Home.vue";
import ShopBookDetail from "@/views/shop/views/BookDetail.vue";
import ShopCart from "@/views/shop/views/Cart.vue";
import ShopCheckout from "@/views/shop/views/Checkout.vue";
import ShopProducts from "@/views/shop/views/Products.vue";
import ShopWishlist from "@/views/shop/views/Wishlist.vue";
import ShopContact from "@/views/shop/views/Contact.vue";
import ShopProfile from "@/views/shop/views/Profile.vue";
import ShopOrderDetail from "@/views/shop/views/OrderDetail.vue";

// pages
import BanHang from "@/views/BanHang/BanHangTaiQuay.vue";
import HoaDon from "@/views/BanHang/HoaDon.vue";
import PhieuGiamGia from "@/views/QuanLyGiamGia/PhieuGiamGia.vue";
import PhieuGiamGiaForm from "@/views/QuanLyGiamGia/PhieuGiamGiaForm.vue";
import DotGiamGia from "@/views/QuanLyGiamGia/DotGiamGia.vue";
import DanhMuc from "@/views/QuanLySach/DanhMucSach.vue";
import Sach from "@/views/QuanLySach/Sach.vue";
import ThuocTinh from "@/views/QuanLySach/ThuocTinh.vue";
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
import ListSach from "./views/QuanLySach/ListSach.vue";
import QuanLyHoaDon from "@/views/BanHang/QuanLyHoaDon.vue";
import AddChiTietSach from "./views/QuanLySach/AddChiTietSach.vue";
import EditChiTietSach from "./views/QuanLySach/EditChiTietSach.vue";
import ChiTietSach from "./views/QuanLySach/ChiTietSach.vue";
import KhachHangDetail from "./views/TaiKhoan/KhachHangDetail.vue";
import AllSach from "@/views/QuanLySach/AllSach.vue";
import KhachHangForm from "@/views/TaiKhoan/KhachHangForm.vue";

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
        path: "/admin/danh-muc",
        component: DanhMuc,
      },

      {
        path: "/admin/thuoc-tinh",
        component: ThuocTinh,
      },
      //
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
      {
        path: "/admin/khach-hang/them-moi",
        name: "ThemKhachHang",
      component: KhachHangForm
      },
      {
        path: "/admin/khach-hang/chinh-sua/:id",
        name: "ChinhSuaKhachHang",
      component: KhachHangForm
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
  
  // Shop routes with layout
  {
    path: "/shop",
    component: Shop,
    children: [
      { path: "", component: ShopHome },
      { path: "products", component: ShopProducts },
      { path: "book/:id", name: "ShopBookDetail", component: ShopBookDetail },
      { path: "cart", component: ShopCart },
      { path: "checkout", component: ShopCheckout },
      { path: "wishlist", component: ShopWishlist },
      { path: "contact", component: ShopContact },
      { path: "profile", component: ShopProfile },
      { path: "order/:id", name: "ShopOrderDetail", component: ShopOrderDetail },
    ]
  },
  
  { path: "/:pathMatch(.*)*", redirect: "/shop" },

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
