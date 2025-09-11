import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json'
    }
});

// Add a request interceptor - Đã vô hiệu hóa token check
api.interceptors.request.use(
    config => {
        // Đã vô hiệu hóa: không gửi token
        // const token = localStorage.getItem('token');
        // if (token) {
        //     config.headers['Authorization'] = `Bearer ${token}`;
        // }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// Add a response interceptor - Đã vô hiệu hóa authentication redirect
api.interceptors.response.use(
    response => response,
    error => {
        if (error.response) {
            // Handle specific error cases - Đã vô hiệu hóa authentication redirect
            switch (error.response.status) {
                case 401:
                    // Unauthorized - đã vô hiệu hóa redirect
                    // localStorage.removeItem('token');
                    // window.location.href = '/auth/login';
                    break;
                case 403:
                    // Forbidden
                    break;
                case 404:
                    // Not found
                    break;
                default:
                    // Handle other cases
                    break;
            }
        }
        return Promise.reject(error);
    }
);

export default api; 