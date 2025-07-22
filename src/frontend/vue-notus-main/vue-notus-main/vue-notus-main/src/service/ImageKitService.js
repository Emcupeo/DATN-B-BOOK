 

class ImageKitService {
  constructor() {
    this.authParams = null
  }

  async initialize() {
    if (!this.authParams) {
      try {
        // Sử dụng Basic Auth với private key
        this.authParams = {
          publicKey: 'public_5XG5VTNIYTdeOVWhFzTxDgFz6po=',
          urlEndpoint: 'https://ik.imagekit.io/emcupeo',
          privateKey: 'private_xIPOEb8sE7AREhsSpGDva56+gRU='
        }
      } catch (error) {
        console.error('Error initializing ImageKit:', error)
        throw error
      }
    }
  }

  // Upload ảnh lên ImageKit
  async uploadImage(file, fileName = null) {
    try {
      console.log('[DEBUG] ImageKitService - Starting upload for file:', fileName)
      await this.initialize()
      
      // Tạo FormData với Basic Auth
      const formData = new FormData()
      formData.append('file', file)
      formData.append('fileName', fileName || `product_${Date.now()}`)
      formData.append('folder', '/products')
      
      // Sử dụng fetch với Basic Auth
      const response = await fetch('https://upload.imagekit.io/api/v1/files/upload', {
        method: 'POST',
        headers: {
          'Authorization': 'Basic ' + btoa(this.authParams.privateKey + ':')
        },
        body: formData
      })
      
      if (!response.ok) {
        throw new Error(`Upload failed: ${response.status} ${response.statusText}`)
      }
      
      const uploadResponse = await response.json()
      console.log('[DEBUG] ImageKitService - Upload successful:', uploadResponse)
      
      return {
        success: true,
        url: uploadResponse.url,
        fileId: uploadResponse.fileId,
        name: uploadResponse.name
      }
    } catch (error) {
      console.error('[ERROR] ImageKitService - Upload failed:', error)
      return {
        success: false,
        error: error.message
      }
    }
  }

  // Tạo URL ảnh với transformation
  getImageUrl(path, transformations = []) {
    if (!path) return null
    
    const baseUrl = 'https://ik.imagekit.io/emcupeo'
    let url = `${baseUrl}${path}`
    
    if (transformations.length > 0) {
      const transformStr = transformations.map(t => 
        Object.entries(t).map(([k, v]) => `${k}-${v}`).join(',')
      ).join('/')
      url += `?tr=${transformStr}`
    }
    
    return url
  }

  // Tạo URL ảnh thumbnail
  getThumbnailUrl(path, width = 300, height = 300) {
    return this.getImageUrl(path, [{
      height: height.toString(),
      width: width.toString(),
      crop: 'center'
    }])
  }

  // Xóa ảnh từ ImageKit
  async deleteImage(fileId) {
    try {
      await this.initialize()
      
      const response = await fetch(`https://api.imagekit.io/v1/files/${fileId}`, {
        method: 'DELETE',
        headers: {
          'Authorization': 'Basic ' + btoa(this.authParams.privateKey + ':'),
          'Content-Type': 'application/json'
        }
      })
      
      if (!response.ok) {
        throw new Error(`Delete failed: ${response.status} ${response.statusText}`)
      }
      
      return { success: true }
    } catch (error) {
      console.error('Error deleting image from ImageKit:', error)
      return {
        success: false,
        error: error.message
      }
    }
  }
}

export default new ImageKitService() 