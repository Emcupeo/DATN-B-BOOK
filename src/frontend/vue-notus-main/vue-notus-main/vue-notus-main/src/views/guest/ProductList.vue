<template>
    <div class="container mx-auto p-4">
      <h1 class="text-2xl font-bold mb-4">Danh Sách Sản Phẩm</h1>
      <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
        <ProductItem 
          v-for="product in products" 
          :key="product.id" 
          :product="product"
          @view-detail="viewDetail"
        />
      </div>
    </div>
  </template>
  
  <script>
  import ProductItem from './ProductItem.vue';
  
  export default {
    components: { ProductItem },
    data() {
      return {
        products: [
          { id: 1, name: "Laptop ASUS", price: 15000000, image: "https://via.placeholder.com/200" },
          { id: 2, name: "Điện thoại Samsung", price: 10000000, image: "https://via.placeholder.com/200" },
          { id: 3, name: "Tai nghe Sony", price: 2000000, image: "https://via.placeholder.com/200" }
        ]
      };
    },
    methods: {
      viewDetail(id) {
        this.$router.push({ name: 'ProductDetail', params: { id } });
      }
    }
  };
  </script>
  