<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <Header />
    
    <!-- Main Content -->
    <main>
      <router-view />
    </main>
    
    <!-- Footer -->
    <Footer />
  </div>
</template>

<script>
import Header from '@/views/shop/components/Header.vue'
import Footer from '@/views/shop/components/Footer.vue'

export default {
  name: 'ShopLayout',
  components: {
    Header,
    Footer
  }
}
</script> 