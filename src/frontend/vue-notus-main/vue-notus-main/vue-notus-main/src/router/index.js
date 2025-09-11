import { createRouter, createWebHistory } from "vue-router";

// Đảm bảo đường dẫn chính xác
import ThemNhanVien from "../views/TaiKhoan/ThemNhanVien.vue";
import SuaNhanVien from "../views/TaiKhoan/SuaNhanVien.vue";

const routes = [
  // Admin routes with layout
  {
    path: "/admin",
    redirect: "/admin/dashboard",
    component: () => import("../layouts/Admin.vue"),
    children: [
      {
        path: "dashboard",
        name: "AdminDashboard",
        component: () => import("../views/admin/Dashboard.vue"),
      },
      {
        path: "settings",
        name: "AdminSettings",
        component: () => import("../views/admin/Settings.vue"),
      },
      {
        path: "tables",
        name: "AdminTables",
        component: () => import("../views/admin/Tables.vue"),
      },
      {
        path: "maps",
        name: "AdminMaps",
        component: () => import("../views/admin/Maps.vue"),
      },
      {
        path: "ban-hang-tai-quay",
        name: "BanHangTaiQuay",
        component: () => import("../views/BanHang/BanHangTaiQuay.vue"),
      },
      {
        path: "hoa-don",
        name: "HoaDon",
        component: () => import("../views/BanHang/HoaDon.vue"),
      },
      {
        path: "hoa-don-chi-tiet/:id",
        name: "QuanLyHoaDon",
        component: () => import("../views/BanHang/QuanLyHoaDon.vue"),
      },
      {
        path: "sach",
        name: "Sach",
        component: () => import("../views/QuanLySach/Sach.vue"),
      },
      {
        path: "sach/add",
        name: "AddChiTietSach",
        component: () => import("../views/QuanLySach/AddChiTietSach.vue"),
      },
      {
        path: "sach/edit/:id",
        name: "EditChiTietSach",
        component: () => import("../views/QuanLySach/EditChiTietSach.vue"),
      },
      {
        path: "sach/:id",
        name: "ChiTietSach",
        component: () => import("../views/QuanLySach/ChiTietSach.vue"),
      },
      {
        path: "sach/all-sach",
        name: "AllSach",
        component: () => import("../views/QuanLySach/AllSach.vue"),
      },
      {
        path: "bo-sach",
        name: "BoSach",
        component: () => import("../views/QuanLySach/BoSach.vue"),
      },
      {
        path: "bo-sach/add",
        name: "AddBoSach",
        component: () => import("../views/QuanLySach/AddBoSach.vue"),
      },
      {
        path: "bo-sach/edit/:id",
        name: "EditBoSach",
        component: () => import("../views/QuanLySach/AddBoSach.vue"),
        props: true,
      },
      {
        path: "bo-sach/detail/:id",
        name: "ChiTietBoSach",
        component: () => import("../views/QuanLySach/ChiTietBoSach.vue"),
      },
      {
        path: "thuoc-tinh",
        redirect: "thuoc-tinh/chat-lieu",
      },
      {
        path: "thuoc-tinh/chat-lieu",
        name: "ChatLieu",
        component: () => import("../views/QuanLySach/ThuocTinh/ChatLieu.vue"),
      },
      {
        path: "thuoc-tinh/loai-bia",
        name: "LoaiBia",
        component: () => import("../views/QuanLySach/ThuocTinh/LoaiBia.vue"),
      },
      {
        path: "thuoc-tinh/ngon-ngu",
        name: "NgonNgu",
        component: () => import("../views/QuanLySach/ThuocTinh/NgonNgu.vue"),
      },
      {
        path: "thuoc-tinh/nguoi-dich",
        name: "NguoiDich",
        component: () => import("../views/QuanLySach/ThuocTinh/NguoiDich.vue"),
      },
      {
        path: "thuoc-tinh/nha-xuat-ban",
        name: "NhaXuatBan",
        component: () => import("../views/QuanLySach/ThuocTinh/NhaXuatBan.vue"),
      },
      {
        path: "thuoc-tinh/tac-gia",
        name: "TacGia",
        component: () => import("../views/QuanLySach/ThuocTinh/TacGia.vue"),
      },
      {
        path: "thuoc-tinh/the-loai",
        name: "TheLoai",
        component: () => import("../views/QuanLySach/ThuocTinh/TheLoai.vue"),
      },
      {
        path: "danh-muc",
        name: "DanhMuc",
        component: () => import("../views/QuanLySach/DanhMucSach.vue"),
      },
      {
        path: "phieu-giam-gia",
        name: "PhieuGiamGia",
        component: () => import("../views/QuanLyGiamGia/PhieuGiamGia.vue"),
      },
      {
        path: "phieu-giam-gia/add",
        name: "PhieuGiamGiaAdd",
        component: () => import("../views/QuanLyGiamGia/PhieuGiamGiaForm.vue"),
      },
      {
        path: "phieu-giam-gia/edit/:id",
        name: "PhieuGiamGiaEdit",
        component: () => import("../views/QuanLyGiamGia/PhieuGiamGiaForm.vue"),
        props: true,
      },
      {
        path: "dot-giam-gia",
        name: "DotGiamGia",
        component: () => import("../views/QuanLyGiamGia/DotGiamGia.vue"),
      },
      {
        path: "nhan-vien",
        name: "NhanVien",
        component: () => import("../views/TaiKhoan/NhanVien.vue"),
      },
      {
        path: "nhan-vien/them-moi",
        name: "ThemNhanVien",
        component: ThemNhanVien,
      },
      {
        path: "nhan-vien/chinh-sua/:id",
        name: "ChinhSuaNhanVien",
        component: SuaNhanVien,
      },
      {
        path: "khach-hang",
        name: "KhachHang",
        component: () => import("../views/TaiKhoan/KhachHang.vue"),
      },
      {
        path: "khach-hang/:id",
        name: "KhachHangDetail",
        component: () => import("../views/TaiKhoan/KhachHangDetail.vue"),
      },
      {
        path: "khach-hang/them-moi",
        name: "ThemKhachHang",
        component: () => import("../views/TaiKhoan/KhachHangForm.vue"),
      },
      {
        path: "khach-hang/chinh-sua/:id",
        name: "ChinhSuaKhachHang",
        component: () => import("../views/TaiKhoan/KhachHangForm.vue"),
      },
    ],
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
    path: "/payment/cancel",
    name: "PaymentCancel",
    component: () => import("../views/shop/views/PaymentCancel.vue"),
  },
  {
    path: "/order-success",
    name: "OrderSuccess",
    component: () => import("../views/shop/views/OrderSuccess.vue"),
  },
  {
    path: "/404",
    name: "NotFound",
    component: () => import("../views/NotFound.vue"),
  },
  // Shop routes with layout (base at "/")
  {
    path: "/",
    component: () => import("../layouts/Shop.vue"),
    children: [
      { path: "", name: "Home", component: () => import("../views/shop/views/Home.vue") },
      { path: "products", name: "Products", component: () => import("../views/shop/views/Products.vue") },
      { path: "book/:id", name: "ShopBookDetail", component: () => import("../views/shop/views/BookDetail.vue") },
      { path: "bo-sach/:id", name: "ShopBoSachDetail", component: () => import("../views/shop/views/BoSachDetail.vue") },
      { path: "cart", name: "Cart", component: () => import("../views/shop/views/Cart.vue") },
      { path: "checkout", name: "Checkout", component: () => import("../views/shop/views/Checkout.vue") },
      { path: "payment-success", name: "PaymentSuccess", component: () => import("../views/shop/views/PaymentSuccess.vue") },
      { path: "order-success", name: "OrderSuccess", component: () => import("../views/shop/views/OrderSuccess.vue") },
      { path: "payment-cancel", name: "PaymentCancel", component: () => import("../views/shop/views/PaymentCancel.vue") },
      { path: "payment-error", name: "PaymentError", component: () => import("../views/shop/views/PaymentError.vue") },
      { path: "wishlist", name: "Wishlist", component: () => import("../views/shop/views/Wishlist.vue") },
      { path: "contact", name: "Contact", component: () => import("../views/shop/views/Contact.vue") },
      { path: "profile", name: "Profile", component: () => import("../views/shop/views/Profile.vue") },
      { path: "order/:id", name: "OrderDetail", component: () => import("../views/shop/views/OrderDetail.vue") },
    ]
  },
  {
    path: "/:pathMatch(.*)*",
    name: "CatchAll",
    component: () => import("../views/NotFound.vue"),
  },

];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
