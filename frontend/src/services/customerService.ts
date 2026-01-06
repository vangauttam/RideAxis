import api from './api';
import type { AvailableVehicleDTO, BookingDTO, ActiveBookingDTO, ResponseStructure } from '../types';

export const customerService = {
    getAvailableVehicles: async (mobno: number, destination: string) => {
        const response = await api.get<ResponseStructure<AvailableVehicleDTO>>('/customer/seeallavailablevehicles', {
            params: { mobno, destination },
        });
        return response.data;
    },

    bookVehicle: async (mobno: number, bookingDto: BookingDTO) => {
        const response = await api.post<ResponseStructure<any>>('/booking/bookVehicle', bookingDto, {
            params: { mobno },
        });
        return response.data;
    },

    seeActiveBooking: async (mobno: number) => {
        try {
            const response = await api.get<ResponseStructure<ActiveBookingDTO>>('/booking/seeactivebooking', {
                params: { mobno },
            });
            return response.data;
        } catch (e) {
            return null;
        }
    },

    cancelRide: async (customerId: number, bookingId: number) => {
        const response = await api.put<ResponseStructure<any>>('/booking/cancelride', null, {
            params: { customerId, bookingId }
        });
        return response.data;
    },

    getProfile: async (mobno: number) => {
        const response = await api.get<ResponseStructure<any>>('/customer/findcustomer', {
            params: { mobno }
        });
        return response.data;
    },

    getBookingHistory: async (mobno: number) => {
        const response = await api.get<ResponseStructure<import('../types').BookingHistoryDTO[]>>(`/customer/history/${mobno}`);
        return response.data;
    },

    deleteCustomer: async (mobno: number) => {
        const response = await api.delete<ResponseStructure<string>>('/customer/deletecustomer', { params: { mobno } });
        return response.data;
    },

    getOtp: async (bookingId: number) => {
        const response = await api.get<ResponseStructure<number>>('/booking/getotp', { params: { bookingid: bookingId } });
        return response.data;
    }
};
