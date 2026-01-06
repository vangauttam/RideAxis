import api from './api';
import type { LoginRequestDTO, RegCustomerDto, RegDriverVehicleDTO, ResponseStructure } from '../types';

export const authService = {
    login: async (credentials: LoginRequestDTO) => {
        const response = await api.post<ResponseStructure<string>>('/auth/login', credentials);
        return response.data;
    },

    registerCustomer: async (data: RegCustomerDto) => {
        const response = await api.post<ResponseStructure<string>>('/auth/register/customer', data);
        return response.data;
    },

    registerDriver: async (data: RegDriverVehicleDTO) => {
        const response = await api.post<ResponseStructure<string>>('/auth/register/driver', data);
        return response.data;
    },
};
