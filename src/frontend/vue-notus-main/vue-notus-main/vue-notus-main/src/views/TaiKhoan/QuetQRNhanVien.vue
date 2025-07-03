<template>
  <div>
    <video ref="video" autoplay></video>
    <!-- Canvas dùng để xử lý khung hình, ẩn đi -->
    <canvas ref="canvas" style="display: none;"></canvas>
    <p v-if="result">Dữ liệu QR: {{ result }}</p>
  </div>
</template>

<script>
import jsQR from "jsqr";

export default {
  data() {
    return {
      result: null,
    };
  },
  async mounted() {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ video: true });
      this.$refs.video.srcObject = stream;
      this.scanQRCode();
    } catch (error) {
      console.error("Không truy cập được camera:", error);
    }
  },
  methods: {
    scanQRCode() {
      const video = this.$refs.video;
      const canvas = this.$refs.canvas;
      const ctx = canvas.getContext("2d");

      const scan = () => {
        if (video.readyState === video.HAVE_ENOUGH_DATA) {
          canvas.width = video.videoWidth;
          canvas.height = video.videoHeight;
          ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
          const imageData = ctx.getImageData(0, 0, canvas.width, canvas.height);
          const code = jsQR(imageData.data, imageData.width, imageData.height);
          if (code) {
            this.result = code.data;
            // Sau khi giải mã thành công, bạn có thể gọi hàm gửi dữ liệu lên backend nếu cần:
            // this.sendDataToBackend(code.data);
            return; // Dừng quét nếu đã nhận được kết quả (hoặc comment dòng này để quét liên tục)
          }
        }
        requestAnimationFrame(scan);
      };

      scan();
    },
    // Ví dụ gửi dữ liệu mã QR lên backend
    async sendDataToBackend(qrData) {
      try {
        const response = await fetch("http://localhost:8080/api/save-cccd", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ qrData }),
        });
        const result = await response.json();
        console.log("Kết quả lưu:", result);
      } catch (error) {
        console.error("Lỗi gửi dữ liệu:", error);
      }
    }
  },
};
</script>
