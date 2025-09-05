const API_URL = 'http://localhost:8080/api/provinces';

class AddressService {
    async getProvinces() {
        try {
            const response = await fetch(`${API_URL}/p`);
            const data = await response.json();
            return data;
        } catch (error) {
            console.error('Error fetching provinces:', error);
            throw error;
        }
    }

    async getDistricts(provinceCode) {
        try {
            const response = await fetch(`${API_URL}/p/${provinceCode}?depth=2`);
            const data = await response.json();
            return data.districts || [];
        } catch (error) {
            console.error('Error fetching districts:', error);
            throw error;
        }
    }

    async getWards(districtCode) {
        try {
            const response = await fetch(`${API_URL}/d/${districtCode}?depth=2`);
            const data = await response.json();
            return data.wards || [];
        } catch (error) {
            console.error('Error fetching wards:', error);
            throw error;
        }
    }

    // Alias methods để tương thích với code cũ
    async getDistrictsByProvinceCode(provinceCode) {
        return this.getDistricts(provinceCode);
    }

    async getWardsByDistrictCode(districtCode) {
        return this.getWards(districtCode);
    }
}

export default new AddressService();