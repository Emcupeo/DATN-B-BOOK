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

// Auth
import { useAuthStore } from "@/store/auth";

// views for Admin layout
import Dashboard from "@/views/admin/Dashboard.vue";
import Settings from "@/views/admin/Settings.vue";
import Tables from "@/views/admin/Tables.vue";
import Maps from "@/views/admin/Maps.vue";

// views for Auth layout
import Login from "@/views/auth/Login.vue";
import Register from "@/views/auth/Register.vue";
import LoginCustomer from "@/views/auth/LoginCustomer.vue";
import RegisterCustomer from "@/views/auth/RegisterCustomer.vue";
import LoginAdmin from "@/views/auth/LoginAdmin.vue";
import RegisterAdmin from "@/views/auth/RegisterAdmin.vue";

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
import Sach from "@/views/QuanLySach/Sach.vue";
// thuoc tinh cua thuoc tinh
import ChatLieu from "@/views/QuanLySach/ThuocTinh/ChatLieu.vue";
import LoaiBia from "@/views/QuanLySach/ThuocTinh/LoaiBia.vue";
import NgonNgu from "@/views/QuanLySach/ThuocTinh/NgonNgu.vue";
import NguoiDich from "@/views/QuanLySach/ThuocTinh/NguoiDich.vue";
import NhaXuatBan from "@/views/QuanLySach/ThuocTinh/NhaXuatBan.vue";
import TacGia from "@/views/QuanLySach/ThuocTinh/TacGia.vue";
import TheLoai from "@/views/QuanLySach/ThuocTinh/TheLoai.vue";
import DanhMucSach from "@/views/QuanLySach/DanhMucSach.vue";
//
import NhanVien from "@/views/TaiKhoan/NhanVien.vue";
import KhachHang from "@/views/TaiKhoan/KhachHang.vue";
import QuanLyHoaDon from "@/views/BanHang/QuanLyHoaDon.vue";
import AddChiTietSach from "./views/QuanLySach/AddChiTietSach.vue";
import EditChiTietSach from "./views/QuanLySach/EditChiTietSach.vue";
import ChiTietSach from "./views/QuanLySach/ChiTietSach.vue";
import KhachHangDetail from "./views/TaiKhoan/KhachHangDetail.vue";
import AllSach from "@/views/QuanLySach/AllSach.vue";
import KhachHangForm from "@/views/TaiKhoan/KhachHangForm.vue";
import BoSach from "@/views/QuanLySach/BoSach.vue";
import AddBoSach from "@/views/QuanLySach/AddBoSach.vue";
import ChiTietBoSach from "@/views/QuanLySach/ChiTietBoSach.vue";
import ThemNhanVien from "@/views/TaiKhoan/ThemNhanVien.vue";
import SuaNhanVien from "@/views/TaiKhoan/SuaNhanVien.vue";

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
        path: "/admin/bo-sach/edit/:id",
        component: AddBoSach
      },
      {
        path: '/admin/bo-sach/detail/:id',
        component: ChiTietBoSach
      },
      {
        path: "/admin/thuoc-tinh",
        redirect: "/admin/thuoc-tinh/chat-lieu",
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
      {
        path: "/admin/danh-muc",
        name: "DanhMuc",
        component: DanhMucSach,
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
        path: "/admin/nhan-vien/them-moi",
        name: "ThemNhanVien",
        component: ThemNhanVien,
      },
      {
        path: "/admin/nhan-vien/chinh-sua/:id",
        name: "ChinhSuaNhanVien",
        component: SuaNhanVien,
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

  {
    path: "/login",
    name: "LoginCustomer",
    component: LoginCustomer,
  },
  {
    path: "/register",
    name: "RegisterCustomer",
    component: RegisterCustomer,
  },
  {
    path: "/admin/login",
    name: "LoginAdmin",
    component: LoginAdmin,
  },
  {
    path: "/admin/register",
    name: "RegisterAdmin",
    component: RegisterAdmin,
  },

  // Shop routes with layout (base at "/")
  {
    path: "/",
    component: Shop,
    children: [
      { path: "", component: ShopHome },
      { path: "products", component: ShopProducts },
      { path: "book/:id", name: "ShopBookDetail", component: ShopBookDetail },
      { path: "bo-sach/:id", name: "ShopBoSachDetail", component: () => import("@/views/shop/views/BoSachDetail.vue") },
      { path: "cart", component: ShopCart },
      { path: "checkout", component: ShopCheckout },
      { path: "payment-success", component: () => import("@/views/shop/views/PaymentSuccess.vue") },
      { path: "payment-cancel", component: () => import("@/views/shop/views/PaymentCancel.vue") },
      { path: "payment-error", component: () => import("@/views/shop/views/PaymentError.vue") },
      { path: "order-success", component: () => import("@/views/shop/views/OrderSuccess.vue") },
      { path: "wishlist", component: ShopWishlist },
      { path: "contact", component: ShopContact },
      { path: "profile", component: ShopProfile },
      { path: "order/:id", name: "ShopOrderDetail", component: ShopOrderDetail },
      { path: "order-lookup", name: "OrderLookup", component: () => import("@/views/shop/views/OrderLookup.vue") },
    ]
  },

  { path: "/payment/cancel", component: () => import("@/views/shop/views/PaymentCancel.vue") },
  { path: "/:pathMatch(.*)*", name: "NotFound", component: () => import("@/views/NotFound.vue") },

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

// Create app instance
const app = createApp(App);

// Initialize auth store
const authStore = useAuthStore();
authStore.initializeAuth();

// Router Guards
router.beforeEach((to, from, next) => {
  // Handle VNPAY return anywhere: capture query and redirect to checkout for processing
  if (to.query && to.query.vnp_ResponseCode) {
    try {
      const search = window.location.search || ''
      localStorage.setItem('vnpayCallbackQuery', search)
    } catch (e) {
      // eslint-disable-next-line no-unused-vars
      const _ignored = e
    }
    return next('/checkout')
  }
  const isAuthenticated = authStore.isAuthenticated;

  console.log('Router Guard:', {
    to: to.path,
    from: from.path,
    isAuthenticated,
    user: authStore.user,
    userRole: authStore.user?.loaiNguoiDung
  });

  // Public routes (không cần đăng nhập)
  // Cho phép mọi route không thuộc /admin, cộng thêm /auth/* và trang đăng nhập/đăng ký admin
  const isPublicRoute = (
    to.path === '/' ||
    to.path.startsWith('/auth') ||
    to.path.startsWith('/admin/login') ||
    to.path.startsWith('/admin/register') ||
    !to.path.startsWith('/admin')
  );

  // Nếu chưa đăng nhập và không phải public route
  if (!isAuthenticated && !isPublicRoute) {
    console.log('Redirecting to login - not authenticated');
    return next('/auth/login');
  }

  // Nếu đã đăng nhập và đang ở auth routes
  if (isAuthenticated && to.path.startsWith('/auth')) {
    const defaultRoute = authStore.defaultRoute;
    console.log('Redirecting from auth to defaultRoute:', defaultRoute);
    return next(defaultRoute);
  }

  // Kiểm tra quyền truy cập theo role
  if (isAuthenticated) {
    const canAccess = authStore.canAccessRoute(to.path);
    console.log('Can access route:', to.path, '=', canAccess);

    if (!canAccess) {
      // Redirect về trang phù hợp với role
      const defaultRoute = authStore.defaultRoute;
      console.log('Access denied, redirecting to defaultRoute:', defaultRoute);
      return next(defaultRoute);
    }
  }

  console.log('Route allowed:', to.path);
  // Route hợp lệ
  next();
});

// Use router and mount app
app.use(router).mount("#app");
