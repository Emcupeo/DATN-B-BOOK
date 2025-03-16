import axios from 'axios';

const API_URL = 'https://provinces.open-api.vn/api';

class AddressService {
    async getProvinces() {
        const response = await axios.get(`${API_URL}/p/`);
        return response.data;
    }

    async getDistrictsByProvinceCode(provinceCode) {
        const response = await axios.get(`${API_URL}/p/${provinceCode}?depth=2`);
        return response.data.districts;
    }

    async getWardsByDistrictCode(districtCode) {
        const response = await axios.get(`${API_URL}/d/${districtCode}?depth=2`);
        return response.data.wards;
    }
}

export default new AddressService(); 