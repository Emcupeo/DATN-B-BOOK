<template>
  <div class="image-upload-container">
    <!-- Upload Area -->
    <div 
      class="upload-area"
      :class="{ 'dragover': isDragOver, 'uploading': uploading }"
      @drop="handleDrop"
      @dragover="handleDragOver"
      @dragleave="handleDragLeave"
      @click="triggerFileInput"
    >
      <input
        ref="fileInput"
        type="file"
        multiple
        accept="image/*"
        @change="handleFileSelect"
        class="hidden-input"
      />
      
      <div v-if="!uploading" class="upload-content">
        <div class="upload-icon">
          <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12"></path>
          </svg>
        </div>
        <p class="upload-text">Kéo thả ảnh vào đây hoặc click để chọn</p>
        <p class="upload-hint">Hỗ trợ: JPG, PNG, GIF (Tối đa 5MB)</p>
      </div>
      
      <div v-else class="uploading-content">
        <div class="loading-spinner"></div>
        <p>Đang upload ảnh...</p>
      </div>
    </div>

    <!-- Preview Images -->
    <div v-if="previewImages.length > 0" class="preview-container">
      <h4 class="preview-title">Ảnh đã chọn ({{ previewImages.length }})</h4>
      <div class="preview-grid">
        <div 
          v-for="(image, index) in previewImages" 
          :key="index"
          class="preview-item"
        >
          <img 
            :src="image.preview" 
            :alt="image.name"
            class="preview-image"
          />
          <div class="preview-overlay">
            <button 
              @click="removeImage(index)"
              class="remove-btn"
              type="button"
            >
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>
          <div class="upload-status">
            <span class="status pending">Chờ upload khi lưu</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Upload Button -->
    <div v-if="previewImages.length > 0" class="upload-actions">
      <p class="text-sm text-gray-600 text-center">
        Ảnh sẽ được upload khi lưu sản phẩm
      </p>
    </div>

    <!-- Error Message -->
    <div v-if="error" class="error-message">
      {{ error }}
    </div>
  </div>
</template>

<script>
import ImageKitService from '../service/ImageKitService'

export default {
  name: 'ImageKitUpload',
  props: {
    maxFiles: {
      type: Number,
      default: 5
    },
    maxFileSize: {
      type: Number,
      default: 5 * 1024 * 1024 // 5MB
    }
  },
  data() {
    return {
      isDragOver: false,
      uploading: false,
      previewImages: [],
      error: null
    }
  },
  computed: {
    allImagesUploaded() {
      return this.previewImages.length > 0 && this.previewImages.every(img => img.uploaded)
    }
  },
  methods: {
    triggerFileInput() {
      this.$refs.fileInput.click()
    },

    handleFileSelect(event) {
      const files = Array.from(event.target.files)
      this.processFiles(files)
    },

    handleDrop(event) {
      event.preventDefault()
      this.isDragOver = false
      
      const files = Array.from(event.dataTransfer.files)
      this.processFiles(files)
    },

    handleDragOver(event) {
      event.preventDefault()
      this.isDragOver = true
    },

    handleDragLeave(event) {
      event.preventDefault()
      this.isDragOver = false
    },

    processFiles(files) {
      console.log('[DEBUG] ImageKitUpload - Processing files:', files);
      const imageFiles = files.filter(file => file.type.startsWith('image/'))
      console.log('[DEBUG] ImageKitUpload - Filtered image files:', imageFiles);
      
      if (imageFiles.length === 0) {
        this.error = 'Vui lòng chọn file ảnh hợp lệ'
        return
      }

      if (this.previewImages.length + imageFiles.length > this.maxFiles) {
        this.error = `Chỉ được chọn tối đa ${this.maxFiles} ảnh`
        return
      }

      imageFiles.forEach(file => {
        if (file.size > this.maxFileSize) {
          this.error = `File ${file.name} quá lớn (tối đa ${this.maxFileSize / 1024 / 1024}MB)`
          return
        }

        const reader = new FileReader()
        reader.onload = (e) => {
          const imageData = {
            file: file,
            name: file.name,
            preview: e.target.result,
            uploading: false,
            uploaded: false,
            error: null,
            url: null,
            fileId: null
          }
          this.previewImages.push(imageData)
          console.log('[DEBUG] ImageKitUpload - Added image to preview:', imageData);
        }
        reader.readAsDataURL(file)
      })

      this.error = null
    },

    removeImage(index) {
      this.previewImages.splice(index, 1)
    },

    async uploadAllImages() {
      this.uploading = true
      this.error = null

      const uploadPromises = this.previewImages
        .filter(img => !img.uploaded)
        .map(async (image) => {
          const originalIndex = this.previewImages.indexOf(image)
          this.previewImages[originalIndex].uploading = true
          this.previewImages[originalIndex].error = null

          try {
            const result = await ImageKitService.uploadImage(image.file, image.name)
            
            if (result.success) {
              this.previewImages[originalIndex].uploaded = true
              this.previewImages[originalIndex].url = result.url
              this.previewImages[originalIndex].fileId = result.fileId
            } else {
              this.previewImages[originalIndex].error = result.error
            }
          } catch (error) {
            this.previewImages[originalIndex].error = error.message
          } finally {
            this.previewImages[originalIndex].uploading = false
          }
        })

      try {
        await Promise.all(uploadPromises)
        const uploadedImages = this.previewImages.filter(img => img.uploaded)
        console.log('[DEBUG] ImageKitUpload - Uploaded images:', uploadedImages)
        this.$emit('upload-complete', uploadedImages)
      } catch (error) {
        console.error('[ERROR] ImageKitUpload - Upload error:', error)
        this.error = 'Có lỗi xảy ra khi upload ảnh'
      } finally {
        this.uploading = false
      }
    },

    getUploadedImages() {
      return this.previewImages.filter(img => img.uploaded)
    },

    // Method để lấy tất cả files đã chọn (chưa upload)
    getSelectedFiles() {
      return this.previewImages.map(img => img.file)
    }
  }
}
</script>

<style scoped>
.image-upload-container {
  @apply space-y-4;
}

.upload-area {
  @apply border-2 border-dashed border-gray-300 rounded-lg p-8 text-center cursor-pointer transition-all duration-200;
}

.upload-area:hover {
  @apply border-blue-400 bg-blue-50;
}

.upload-area.dragover {
  @apply border-blue-500 bg-blue-100;
}

.upload-area.uploading {
  @apply cursor-not-allowed opacity-75;
}

.hidden-input {
  @apply hidden;
}

.upload-content {
  @apply space-y-2;
}

.upload-icon {
  @apply text-gray-400 mx-auto;
}

.upload-text {
  @apply text-lg font-medium text-gray-700;
}

.upload-hint {
  @apply text-sm text-gray-500;
}

.uploading-content {
  @apply space-y-2;
}

.loading-spinner {
  @apply w-8 h-8 border-4 border-blue-200 border-t-blue-600 rounded-full animate-spin mx-auto;
}

.preview-container {
  @apply space-y-3;
}

.preview-title {
  @apply text-lg font-medium text-gray-700;
}

.preview-grid {
  @apply grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4;
}

.preview-item {
  @apply relative rounded-lg overflow-hidden border border-gray-200;
}

.preview-image {
  @apply w-full h-32 object-cover;
}

.preview-overlay {
  @apply absolute inset-0 bg-black bg-opacity-0 hover:bg-opacity-20 transition-all duration-200 flex items-center justify-center;
}

.remove-btn {
  @apply bg-red-500 text-white p-1 rounded-full hover:bg-red-600 transition-colors;
}

.upload-status {
  @apply absolute bottom-0 left-0 right-0 bg-black bg-opacity-75 text-white text-xs p-1;
}

.status {
  @apply px-2 py-1 rounded text-xs font-medium;
}

.status.uploading {
  @apply bg-yellow-500 text-white;
}

.status.success {
  @apply bg-green-500 text-white;
}

.status.error {
  @apply bg-red-500 text-white;
}

.status.pending {
  @apply bg-gray-500 text-white;
}

.upload-actions {
  @apply flex justify-center;
}

.upload-btn {
  @apply bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors;
}

.error-message {
  @apply text-red-600 text-sm bg-red-50 p-3 rounded-lg;
}
</style> 