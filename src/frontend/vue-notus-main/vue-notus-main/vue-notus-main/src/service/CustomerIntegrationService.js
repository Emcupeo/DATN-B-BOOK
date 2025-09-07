import KhachHangService from './KhachHangService';
import KhachHang from '@/models/KhachHang';

class CustomerIntegrationService {
    constructor() {
        this.STORAGE_KEY = 'customer_integration_data';
        this.CUSTOMER_SEARCH_CACHE = 'customer_search_cache';
        this.CACHE_EXPIRY = 5 * 60 * 1000; // 5 minutes
    }

    /**
     * Tìm kiếm khách hàng theo số điện thoại hoặc email
     */
    async searchCustomer(searchTerm) {
        try {
            // Check cache first
            const cached = this.getCachedSearch(searchTerm);
            if (cached) {
                return cached;
            }

            // Search by phone number
            let customers = [];
            if (this.isPhoneNumber(searchTerm)) {
                customers = await this.searchByPhone(searchTerm);
            } else if (this.isEmail(searchTerm)) {
                customers = await this.searchByEmail(searchTerm);
            } else {
                // Search by name
                customers = await KhachHangService.search(searchTerm);
            }

            // Cache the result
            this.setCachedSearch(searchTerm, customers);
            return customers;
        } catch (error) {
            console.error('Error searching customer:', error);
            return [];
        }
    }

    /**
     * Tìm kiếm khách hàng theo số điện thoại
     */
    async searchByPhone(phoneNumber) {
        try {
            const allCustomers = await KhachHangService.getAll();
            return allCustomers.filter(customer => 
                customer.soDienThoai && customer.soDienThoai.includes(phoneNumber)
            );
        } catch (error) {
            console.error('Error searching by phone:', error);
            return [];
        }
    }

    /**
     * Tìm kiếm khách hàng theo email
     */
    async searchByEmail(email) {
        try {
            const allCustomers = await KhachHangService.getAll();
            return allCustomers.filter(customer => 
                customer.email && customer.email.toLowerCase().includes(email.toLowerCase())
            );
        } catch (error) {
            console.error('Error searching by email:', error);
            return [];
        }
    }

    /**
     * Lấy thông tin khách hàng đầy đủ bao gồm địa chỉ
     */
    async getCustomerWithAddresses(customerId) {
        try {
            const customer = await KhachHangService.getById(customerId);
            return customer;
        } catch (error) {
            console.error('Error getting customer with addresses:', error);
            return null;
        }
    }

    /**
     * Tạo khách hàng mới từ thông tin checkout
     */
    async createCustomerFromCheckout(customerData, addressData) {
        try {
            const customer = new KhachHang({
                hoTen: customerData.fullName,
                email: customerData.email,
                soDienThoai: customerData.phone,
                gioiTinh: 1, // Default
                trangThai: 1, // Active
                danhSachDiaChi: [{
                    diaChiChiTiet: addressData.address,
                    tinhThanh: addressData.city,
                    quanHuyen: addressData.district,
                    xaPhuong: addressData.ward,
                    macDinh: true
                }]
            });

            const createdCustomer = await KhachHangService.createWithAddresses(customer.toJSON());
            return createdCustomer;
        } catch (error) {
            console.error('Error creating customer from checkout:', error);
            throw error;
        }
    }

    /**
     * Cập nhật địa chỉ cho khách hàng hiện có
     */
    async addAddressToCustomer(customerId, addressData) {
        try {
            const customer = await KhachHangService.getById(customerId);
            
            // Add new address
            const newAddress = {
                diaChiChiTiet: addressData.address,
                tinhThanh: addressData.city,
                quanHuyen: addressData.district,
                xaPhuong: addressData.ward,
                macDinh: customer.danhSachDiaChi.length === 0 // Set as default if no addresses
            };

            customer.danhSachDiaChi.push(newAddress);
            
            const updatedCustomer = await KhachHangService.update(customerId, customer);
            return updatedCustomer;
        } catch (error) {
            console.error('Error adding address to customer:', error);
            throw error;
        }
    }

    /**
     * Lưu thông tin khách hàng tạm thời vào localStorage
     */
    saveCustomerToStorage(customerData) {
        try {
            const data = {
                ...customerData,
                timestamp: Date.now()
            };
            localStorage.setItem(this.STORAGE_KEY, JSON.stringify(data));
        } catch (error) {
            console.error('Error saving customer to storage:', error);
        }
    }

    /**
     * Lấy thông tin khách hàng từ localStorage
     */
    getCustomerFromStorage() {
        try {
            const data = localStorage.getItem(this.STORAGE_KEY);
            if (!data) return null;

            const parsed = JSON.parse(data);
            // Check if data is not expired (24 hours)
            if (Date.now() - parsed.timestamp > 24 * 60 * 60 * 1000) {
                localStorage.removeItem(this.STORAGE_KEY);
                return null;
            }

            return parsed;
        } catch (error) {
            console.error('Error getting customer from storage:', error);
            return null;
        }
    }

    /**
     * Xóa thông tin khách hàng khỏi localStorage
     */
    clearCustomerFromStorage() {
        try {
            localStorage.removeItem(this.STORAGE_KEY);
        } catch (error) {
            console.error('Error clearing customer from storage:', error);
        }
    }

    /**
     * Cache search results
     */
    setCachedSearch(searchTerm, results) {
        try {
            const cache = this.getSearchCache();
            cache[searchTerm] = {
                results: results,
                timestamp: Date.now()
            };
            localStorage.setItem(this.CUSTOMER_SEARCH_CACHE, JSON.stringify(cache));
        } catch (error) {
            console.error('Error setting cached search:', error);
        }
    }

    /**
     * Get cached search results
     */
    getCachedSearch(searchTerm) {
        try {
            const cache = this.getSearchCache();
            const cached = cache[searchTerm];
            
            if (cached && Date.now() - cached.timestamp < this.CACHE_EXPIRY) {
                return cached.results;
            }
            
            // Remove expired cache
            if (cached) {
                delete cache[searchTerm];
                localStorage.setItem(this.CUSTOMER_SEARCH_CACHE, JSON.stringify(cache));
            }
            
            return null;
        } catch (error) {
            console.error('Error getting cached search:', error);
            return null;
        }
    }

    /**
     * Get search cache from localStorage
     */
    getSearchCache() {
        try {
            const cache = localStorage.getItem(this.CUSTOMER_SEARCH_CACHE);
            return cache ? JSON.parse(cache) : {};
        } catch (error) {
            console.error('Error getting search cache:', error);
            return {};
        }
    }

    /**
     * Validate phone number format
     */
    isPhoneNumber(str) {
        return /^[0-9]{10,11}$/.test(str.replace(/\s/g, ''));
    }

    /**
     * Validate email format
     */
    isEmail(str) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(str);
    }

    /**
     * Format phone number for display
     */
    formatPhoneNumber(phone) {
        if (!phone) return '';
        const cleaned = phone.replace(/\D/g, '');
        if (cleaned.length === 10) {
            return cleaned.replace(/(\d{4})(\d{3})(\d{3})/, '$1 $2 $3');
        } else if (cleaned.length === 11) {
            return cleaned.replace(/(\d{4})(\d{3})(\d{4})/, '$1 $2 $3');
        }
        return phone;
    }

    /**
     * Get full address string
     */
    getFullAddress(address) {
        if (!address) return '';
        const parts = [
            address.diaChiChiTiet,
            address.xaPhuong,
            address.quanHuyen,
            address.tinhThanh
        ].filter(part => part && part.trim());
        return parts.join(', ');
    }

    /**
     * Convert customer data to checkout format
     */
    convertToCheckoutFormat(customer) {
        return {
            fullName: customer.hoTen,
            email: customer.email,
            phone: customer.soDienThoai,
            addresses: customer.danhSachDiaChi || []
        };
    }

    /**
     * Convert checkout data to customer format
     */
    convertToCustomerFormat(checkoutData) {
        return {
            hoTen: checkoutData.fullName,
            email: checkoutData.email,
            soDienThoai: checkoutData.phone,
            gioiTinh: 1,
            trangThai: 1
        };
    }
}

export default new CustomerIntegrationService();


