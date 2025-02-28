<template>
  <div class="container mx-auto p-4">
    <!-- Banner -->
    <div class="w-full h-64 bg-blue-500 text-white flex items-center justify-center text-3xl font-bold rounded-lg mb-6">
      Chào mừng đến với cửa hàng của chúng tôi!
    </div>

    <!-- Danh sách sản phẩm -->
    <ProductList />
  </div>
</template>

<script>
import ProductList from '@/views/guest/ProductDetail.vue';

export default {
  components: { ProductList }
};
</script>
