import axios from 'axios';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL || '',
    headers: {
        'Content-Type': 'application/json',
    },
});

// Add a request interceptor to add the auth token to every request
api.interceptors.request.use(
    (config) => {
        const token = sessionStorage.getItem('token');
        if (token) {
            // Handle Bearer prefix if not already present in token storage
            const authHeader = token.startsWith('Bearer ') ? token : `Bearer ${token}`;
            config.headers.Authorization = authHeader;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default api;
