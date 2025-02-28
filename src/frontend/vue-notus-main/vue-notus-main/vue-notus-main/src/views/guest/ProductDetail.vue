<template>
    <div class="container mx-auto p-4">
      <div v-if="product" class="max-w-3xl mx-auto">
        <img :src="product.image" class="w-full h-64 object-cover mb-4 rounded-lg" />
        <h1 class="text-2xl font-bold">{{ product.name }}</h1>
        <p class="text-gray-600">{{ product.price.toLocaleString() }} VND</p>
        <button 
          @click="addToCart" 
          class="mt-4 bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700">
          Thêm vào giỏ hàng
        </button>
      </div>
      <div v-else>Không tìm thấy sản phẩm!</div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        product: null,
        products: [
          { id: 1, name: "Laptop ASUS", price: 15000000, image: "https://via.placeholder.com/200" },
          { id: 2, name: "Điện thoại Samsung", price: 10000000, image: "https://via.placeholder.com/200" },
          { id: 3, name: "Tai nghe Sony", price: 2000000, image: "https://via.placeholder.com/200" }
        ]
      };
    },
    mounted() {
      const id = parseInt(this.$route.params.id);
      this.product = this.products.find(p => p.id === id);
    },
    methods: {
      addToCart() {
        let cart = JSON.parse(localStorage.getItem('cart')) || [];
        cart.push(this.product);
        localStorage.setItem('cart', JSON.stringify(cart));
        alert('Đã thêm vào giỏ hàng!');
      }
    }
  };
  </script>
  