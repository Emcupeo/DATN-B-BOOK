import { reactive, computed } from 'vue'

// Store data
const state = reactive({
  books: [
    {
      id: 1,
      title: 'Đắc Nhân Tâm',
      author: 'Dale Carnegie',
      price: 89000,
      originalPrice: 120000,
      image: 'https://images.pexels.com/photos/159711/books-bookstore-book-reading-159711.jpeg?auto=compress&cs=tinysrgb&w=400',
      description: 'Cuốn sách kinh điển về nghệ thuật giao tiếp và ứng xử. Đây là một trong những cuốn sách bán chạy nhất mọi thời đại, giúp độc giả hiểu rõ hơn về cách xây dựng mối quan hệ tốt đẹp với mọi người.',
      category: 'Kỹ năng sống',
      rating: 4.8,
      inStock: true,
      publisher: 'NXB Tổng Hợp',
      language: 'Tiếng Việt',
      format: 'Bìa mềm',
      pages: 320,
      reviewCount: 1250,
      publishedDate: '2020-01-15',
      sales: 5000
    },
    {
      id: 2,
      title: 'Sapiens: Lược sử loài người',
      author: 'Yuval Noah Harari',
      price: 159000,
      originalPrice: 200000,
      image: 'https://images.pexels.com/photos/1029141/pexels-photo-1029141.jpeg?auto=compress&cs=tinysrgb&w=400',
      description: 'Câu chuyện về sự tiến hóa của loài người từ thời tiền sử đến hiện đại. Tác giả đưa ra những góc nhìn mới mẻ về lịch sử nhân loại và những thách thức trong tương lai.',
      category: 'Lịch sử',
      rating: 4.9,
      inStock: true,
      publisher: 'NXB Văn Học',
      language: 'Tiếng Việt',
      format: 'Bìa cứng',
      pages: 480,
      reviewCount: 890,
      publishedDate: '2019-08-20',
      sales: 3200
    },
    {
      id: 3,
      title: 'Atomic Habits',
      author: 'James Clear',
      price: 129000,
      originalPrice: 160000,
      image: 'https://images.pexels.com/photos/1370295/pexels-photo-1370295.jpeg?auto=compress&cs=tinysrgb&w=400',
      description: 'Hướng dẫn xây dựng thói quen tốt và loại bỏ thói quen xấu thông qua những thay đổi nhỏ nhưng có tác động lớn. Cuốn sách cung cấp các phương pháp thực tế để cải thiện cuộc sống.',
      category: 'Phát triển bản thân',
      rating: 4.7,
      inStock: true,
      publisher: 'NXB Trẻ',
      language: 'Tiếng Anh',
      format: 'Bìa mềm',
      pages: 320,
      reviewCount: 2100,
      publishedDate: '2021-03-10',
      sales: 4100
    },
    {
      id: 4,
      title: 'Tôi Tài Giỏi, Bạn Cũng Thế',
      author: 'Adam Khoo',
      price: 99000,
      originalPrice: 130000,
      image: 'https://images.pexels.com/photos/1181244/pexels-photo-1181244.jpeg?auto=compress&cs=tinysrgb&w=400',
      description: 'Phương pháp học tập hiệu quả và phát triển trí tuệ. Cuốn sách chia sẻ những bí quyết học tập từ một người từng là học sinh kém trở thành học sinh xuất sắc.',
      category: 'Giáo dục',
      rating: 4.6,
      inStock: true,
      publisher: 'NXB Giáo Dục',
      language: 'Tiếng Việt',
      format: 'Bìa mềm',
      pages: 280,
      reviewCount: 1560,
      publishedDate: '2018-11-05',
      sales: 2800
    },
    {
      id: 5,
      title: 'Nhà Giả Kim',
      author: 'Paulo Coelho',
      price: 79000,
      originalPrice: 100000,
      image: 'https://images.pexels.com/photos/1181675/pexels-photo-1181675.jpeg?auto=compress&cs=tinysrgb&w=400',
      description: 'Câu chuyện về hành trình tìm kiếm ước mơ và ý nghĩa cuộc sống. Một tác phẩm văn học đầy cảm hứng về việc theo đuổi giấc mơ và khám phá bản thân.',
      category: 'Văn học',
      rating: 4.8,
      inStock: true,
      publisher: 'NXB Văn Học',
      language: 'Tiếng Việt',
      format: 'Bìa mềm',
      pages: 200,
      reviewCount: 3200,
      publishedDate: '2017-06-15',
      sales: 6500
    },
    {
      id: 6,
      title: 'Think and Grow Rich',
      author: 'Napoleon Hill',
      price: 119000,
      originalPrice: 150000,
      image: 'https://images.pexels.com/photos/1181467/pexels-photo-1181467.jpeg?auto=compress&cs=tinysrgb&w=400',
      description: 'Bí quyết thành công và làm giàu từ tư duy. Cuốn sách phân tích những nguyên tắc thành công của những người giàu có và thành đạt nhất thế giới.',
      category: 'Kinh doanh',
      rating: 4.7,
      inStock: true,
      publisher: 'NXB Tổng Hợp',
      language: 'Tiếng Anh',
      format: 'Bìa cứng',
      pages: 350,
      reviewCount: 980,
      publishedDate: '2020-09-12',
      sales: 2200
    },
    {
      id: 7,
      title: 'Đọc Vị Bất Kỳ Ai',
      author: 'David J. Lieberman',
      price: 85000,
      originalPrice: 110000,
      image: 'https://images.pexels.com/photos/1181676/pexels-photo-1181676.jpeg?auto=compress&cs=tinysrgb&w=400',
      description: 'Nghệ thuật thấu hiểu tâm lý con người và đọc vị người khác. Cuốn sách cung cấp các kỹ năng để hiểu rõ suy nghĩ và cảm xúc của người khác.',
      category: 'Kỹ năng sống',
      rating: 4.5,
      inStock: false,
      publisher: 'NXB Kim Đồng',
      language: 'Tiếng Việt',
      format: 'Bìa mềm',
      pages: 240,
      reviewCount: 750,
      publishedDate: '2019-04-08',
      sales: 1800
    },
    {
      id: 8,
      title: 'Rich Dad Poor Dad',
      author: 'Robert T. Kiyosaki',
      price: 139000,
      originalPrice: 180000,
      image: 'https://images.pexels.com/photos/1181677/pexels-photo-1181677.jpeg?auto=compress&cs=tinysrgb&w=400',
      description: 'Những bài học về tài chính cá nhân và đầu tư từ hai người cha khác nhau. Cuốn sách thay đổi cách nhìn về tiền bạc và đầu tư.',
      category: 'Kinh doanh',
      rating: 4.6,
      inStock: true,
      publisher: 'NXB Trẻ',
      language: 'Tiếng Việt',
      format: 'Bìa mềm',
      pages: 300,
      reviewCount: 1100,
      publishedDate: '2021-01-20',
      sales: 2900
    },
    {
      id: 9,
      title: '7 Thói Quen Hiệu Quả',
      author: 'Stephen R. Covey',
      price: 149000,
      originalPrice: 190000,
      image: 'https://images.pexels.com/photos/1181678/pexels-photo-1181678.jpeg?auto=compress&cs=tinysrgb&w=400',
      description: 'Bảy thói quen quan trọng để đạt được hiệu quả cao trong công việc và cuộc sống. Cuốn sách kinh điển về phát triển bản thân.',
      category: 'Phát triển bản thân',
      rating: 4.8,
      inStock: true,
      publisher: 'NXB Tổng Hợp',
      language: 'Tiếng Việt',
      format: 'Bìa cứng',
      pages: 400,
      reviewCount: 1800,
      publishedDate: '2018-12-03',
      sales: 3500
    },
    {
      id: 10,
      title: 'Hackers Ielts',
      author: 'Jo Gang-soo',
      price: 169000,
      originalPrice: 220000,
      image: 'https://images.pexels.com/photos/1181679/pexels-photo-1181679.jpeg?auto=compress&cs=tinysrgb&w=400',
      description: 'Bộ sách luyện thi IELTS hiệu quả với các chiến lược và phương pháp học tập được thiết kế đặc biệt cho người Việt Nam.',
      category: 'Giáo dục',
      rating: 4.7,
      inStock: true,
      publisher: 'NXB Giáo Dục',
      language: 'Tiếng Anh',
      format: 'Bìa mềm',
      pages: 350,
      reviewCount: 650,
      publishedDate: '2022-02-14',
      sales: 1200
    },
    {
      id: 11,
      title: 'Sức Mạnh Của Thói Quen',
      author: 'Charles Duhigg',
      price: 109000,
      originalPrice: 140000,
      image: 'https://images.pexels.com/photos/1181680/pexels-photo-1181680.jpeg?auto=compress&cs=tinysrgb&w=400',
      description: 'Khám phá khoa học về cách thói quen hoạt động và cách chúng ta có thể thay đổi chúng. Cuốn sách dựa trên nghiên cứu khoa học về não bộ.',
      category: 'Phát triển bản thân',
      rating: 4.6,
      inStock: true,
      publisher: 'NXB Văn Học',
      language: 'Tiếng Việt',
      format: 'Bìa mềm',
      pages: 320,
      reviewCount: 950,
      publishedDate: '2020-07-18',
      sales: 2100
    },
    {
      id: 12,
      title: 'Nghĩ Giàu Làm Giàu',
      author: 'Napoleon Hill',
      price: 99000,
      originalPrice: 130000,
      image: 'https://images.pexels.com/photos/1181681/pexels-photo-1181681.jpeg?auto=compress&cs=tinysrgb&w=400',
      description: 'Bản dịch tiếng Việt của cuốn sách kinh điển về thành công và làm giàu. Những nguyên tắc bất hủ về tư duy thành công.',
      category: 'Kinh doanh',
      rating: 4.5,
      inStock: true,
      publisher: 'NXB Trẻ',
      language: 'Tiếng Việt',
      format: 'Bìa mềm',
      pages: 280,
      reviewCount: 820,
      publishedDate: '2019-10-25',
      sales: 1900
    }
  ],
  cart: [],
  categories: ['Tất cả', 'Kỹ năng sống', 'Lịch sử', 'Phát triển bản thân', 'Giáo dục', 'Văn học', 'Kinh doanh']
})

// Computed properties
const cartItemCount = computed(() => {
  return state.cart.reduce((total, item) => total + item.quantity, 0)
})

const cartTotal = computed(() => {
  return state.cart.reduce((total, item) => total + (item.price * item.quantity), 0)
})

// Actions
const addToCart = (book) => {
  const existingItem = state.cart.find(item => item.id === book.id)
  if (existingItem) {
    existingItem.quantity++
  } else {
    state.cart.push({ ...book, quantity: 1 })
  }
}

const removeFromCart = (bookId) => {
  state.cart = state.cart.filter(item => item.id !== bookId)
}

const updateCartQuantity = ({ bookId, quantity }) => {
  const item = state.cart.find(item => item.id === bookId)
  if (item) {
    item.quantity = quantity
  }
}

const clearCart = () => {
  state.cart = []
}

const getBookById = (id) => {
  return state.books.find(book => book.id === parseInt(id))
}

// Export store
export const useShopStore = () => {
  return {
    // State
    books: computed(() => state.books),
    cart: computed(() => state.cart),
    categories: computed(() => state.categories),
    
    // Computed
    cartItemCount,
    cartTotal,
    
    // Actions
    addToCart,
    removeFromCart,
    updateCartQuantity,
    clearCart,
    getBookById
  }
}