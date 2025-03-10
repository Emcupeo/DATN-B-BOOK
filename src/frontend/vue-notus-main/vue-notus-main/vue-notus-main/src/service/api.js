import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json'
    }
});

// Add a request interceptor
api.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// Add a response interceptor
api.interceptors.response.use(
    response => response,
    error => {
        if (error.response) {
            // Handle specific error cases
            switch (error.response.status) {
                case 401:
                    // Unauthorized - redirect to login
                    localStorage.removeItem('token');
                    window.location.href = '/auth/login';
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