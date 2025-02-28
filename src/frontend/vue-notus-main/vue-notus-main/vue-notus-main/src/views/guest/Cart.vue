<template>
    <div class="container mx-auto p-4">
      <h1 class="text-2xl font-bold mb-4">Giỏ Hàng</h1>
      <div v-if="cart.length > 0">
        <div v-for="item in cart" :key="item.id" class="border p-2 rounded-lg mb-2">
          <h2 class="text-lg font-bold">{{ item.name }}</h2>
          <p class="text-gray-600">{{ item.price.toLocaleString() }} VND</p>
        </div>
        <button 
          @click="clearCart"
          class="mt-4 bg-red-500 text-white px-4 py-2 rounded hover:bg-red-700">
          Xóa giỏ hàng
        </button>
      </div>
      <div v-else>Chưa có sản phẩm nào trong giỏ hàng.</div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return { cart: JSON.parse(localStorage.getItem('cart')) || [] };
    },
    methods: {
      clearCart() {
        localStorage.removeItem('cart');
        this.cart = [];
      }
    }
  };
  </script>
  