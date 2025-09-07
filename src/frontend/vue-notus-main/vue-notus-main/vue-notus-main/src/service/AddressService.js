const API_URL = 'http://localhost:8080/api/provinces';

class AddressService {
    async getProvinces() {
        try {
            const response = await fetch(`${API_URL}/p`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            return data;
        } catch (error) {
            console.error('Error fetching provinces:', error);
            // Fallback to direct API call if backend fails
            try {
                const fallbackResponse = await fetch('https://provinces.open-api.vn/api/p/');
                const fallbackData = await fallbackResponse.json();
                return fallbackData;
            } catch (fallbackError) {
                console.error('Fallback API also failed:', fallbackError);
                throw error;
            }
        }
    }

    async getDistricts(provinceCode) {
        try {
            console.log('Getting districts for provinceCode:', provinceCode);
            
            // If provinceCode is a name (string), try to find the code
            let actualCode = provinceCode;
            if (isNaN(provinceCode)) {
                console.log('ProvinceCode is a name, looking for code...');
                const provinces = await this.getProvinces();
                const province = provinces.find(p => p.name === provinceCode);
                if (province) {
                    actualCode = province.code;
                    console.log('Found code for', provinceCode, ':', actualCode);
                } else {
                    console.error('Could not find code for province:', provinceCode);
                    return [];
                }
            }
            
            const response = await fetch(`${API_URL}/p/${actualCode}?depth=2`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            return data.districts || [];
        } catch (error) {
            console.error('Error fetching districts from backend:', error);
            console.error('ProvinceCode that failed:', provinceCode);
            // Return empty array as fallback to prevent UI breaking
            console.warn('Returning empty districts array as fallback');
            return [];
        }
    }

    async getWards(districtCode) {
        try {
            console.log('Getting wards for districtCode:', districtCode);
            
            // If districtCode is a name (string), try to find the code
            let actualCode = districtCode;
            if (isNaN(districtCode)) {
                console.log('DistrictCode is a name, looking for code...');
                // We need to search through all provinces to find the district
                const provinces = await this.getProvinces();
                let foundDistrict = null;
                
                for (const province of provinces) {
                    const districts = await this.getDistricts(province.code);
                    const district = districts.find(d => d.name === districtCode);
                    if (district) {
                        foundDistrict = district;
                        break;
                    }
                }
                
                if (foundDistrict) {
                    actualCode = foundDistrict.code;
                    console.log('Found code for district', districtCode, ':', actualCode);
                } else {
                    console.error('Could not find code for district:', districtCode);
                    return [];
                }
            }
            
            const response = await fetch(`${API_URL}/d/${actualCode}?depth=2`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            return data.wards || [];
        } catch (error) {
            console.error('Error fetching wards from backend:', error);
            console.error('DistrictCode that failed:', districtCode);
            // Return empty array as fallback to prevent UI breaking
            console.warn('Returning empty wards array as fallback');
            return [];
        }
    }

    // Alias methods để tương thích với code cũ
    async getDistrictsByProvinceCode(provinceCode) {
        return this.getDistricts(provinceCode);
    }

    async getWardsByDistrictCode(districtCode) {
        return this.getWards(districtCode);
    }

    // Utility functions for address handling
    getFullAddressDisplay = (address) => {
        // Format địa chỉ: địa chỉ chi tiết + xã/phường, quận/huyện, tỉnh/thành phố
        const detailAddress = address.address || address.diaChiChiTiet || ''
        const fullLocation = this.getFullAddress(address)
        return `${detailAddress}, ${fullLocation}`
    }

    getFullAddress = (address) => {
        // Tạo chuỗi địa chỉ từ các thành phần
        const parts = []
        if (address.ward || address.xaPhuong) parts.push(address.ward || address.xaPhuong)
        if (address.district || address.quanHuyen) parts.push(address.district || address.quanHuyen)
        if (address.city || address.tinhThanh) parts.push(address.city || address.tinhThanh)
        return parts.join(', ')
    }

    validatePhoneNumber = (phone) => {
        // Validate số điện thoại Việt Nam
        const phoneRegex = /^(0|\+84)[3|5|7|8|9][0-9]{8}$/
        return phoneRegex.test(phone.replace(/\s/g, ''))
    }

    // Address data transformation functions
    transformAddressForAPI = (addressData, provinces = [], districts = [], wards = []) => {
        // Chuyển đổi từ form data (có codes) sang API format (có names)
        const province = provinces.find(p => p.code === addressData.city)
        const district = districts.find(d => d.code === addressData.district)
        const ward = wards.find(w => w.code === addressData.ward)

        return {
            diaChiChiTiet: addressData.address,
            tinhThanh: province ? province.name : addressData.city,
            quanHuyen: district ? district.name : addressData.district,
            xaPhuong: ward ? ward.name : addressData.ward,
            macDinh: addressData.isDefault || false,
            tenNguoiNhan: addressData.fullName,
            soDienThoaiNguoiNhan: addressData.phone
        }
    }

    transformAddressFromAPI = (addr, customerInfo = null) => {
        // Chuyển đổi từ API response sang format hiển thị
        return {
            id: addr.id,
            fullName: addr.tenNguoiNhan && addr.tenNguoiNhan.trim() ? addr.tenNguoiNhan : (customerInfo ? customerInfo.hoTen : ''),
            phone: addr.soDienThoaiNguoiNhan && addr.soDienThoaiNguoiNhan.trim() ? addr.soDienThoaiNguoiNhan : (customerInfo ? customerInfo.soDienThoai : ''),
            address: addr.diaChiChiTiet,
            // Sử dụng code thay vì tên để tương thích với API
            city: addr.tinhThanhCode || addr.tinhThanh, // Ưu tiên code, fallback về tên
            district: addr.quanHuyenCode || addr.quanHuyen, // Ưu tiên code, fallback về tên
            ward: addr.xaPhuongCode || addr.xaPhuong, // Ưu tiên code, fallback về tên
            isDefault: addr.macDinh,
            // Thêm các field để tương thích với getFullAddressDisplay
            diaChiChiTiet: addr.diaChiChiTiet,
            tinhThanh: addr.tinhThanh,
            quanHuyen: addr.quanHuyen,
            xaPhuong: addr.xaPhuong,
            // Thêm code fields để tương thích với form selection
            cityCode: addr.tinhThanhCode || null,
            districtCode: addr.quanHuyenCode || null,
            wardCode: addr.xaPhuongCode || null
        }
    }

    // API functions for address CRUD
    async loadCustomerAddresses(customerId, token) {
        try {
            const response = await fetch(`http://localhost:8080/api/dia-chi/khach-hang/${customerId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })

            if (response.ok) {
                const addressList = await response.json()
                
                if (addressList && addressList.length > 0) {
                    // Get customer info for fallback names
                    const customerResponse = await fetch(`http://localhost:8080/api/khach-hang/${customerId}`, {
                        method: 'GET',
                        headers: {
                            'Authorization': `Bearer ${token}`
                        }
                    })
                    
                    let customerInfo = null
                    if (customerResponse.ok) {
                        customerInfo = await customerResponse.json()
                    }
                    
                    return addressList.map(addr => this.transformAddressFromAPI(addr, customerInfo))
                }
                return []
            } else {
                throw new Error(`Failed to load addresses: ${response.status}`)
            }
        } catch (error) {
            console.error('Error loading addresses:', error)
            throw error
        }
    }

    async saveAddress(addressData, customerId, token, isUpdate = false, addressId = null) {
        try {
            const url = isUpdate 
                ? `http://localhost:8080/api/dia-chi/${addressId}`
                : `http://localhost:8080/api/dia-chi/khach-hang/${customerId}`
            
            const method = isUpdate ? 'PUT' : 'POST'
            
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(addressData)
            })

            if (response.ok) {
                return await response.json()
            } else {
                throw new Error(`${isUpdate ? 'Update' : 'Create'} address failed`)
            }
        } catch (error) {
            console.error(`Error ${isUpdate ? 'updating' : 'creating'} address:`, error)
            throw error
        }
    }

    async deleteAddress(addressId, token) {
        try {
            const response = await fetch(`http://localhost:8080/api/dia-chi/${addressId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })

            if (response.ok) {
                return true
            } else {
                throw new Error('Delete address failed')
            }
        } catch (error) {
            console.error('Error deleting address:', error)
            throw error
        }
    }
}

export default new AddressService();