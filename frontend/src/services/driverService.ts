import api from './api';
import type { ActiveBookingDriverDTO, ResponseStructure } from '../types';

export const driverService = {
    seeActiveBooking: async (mobileno: number) => {
        try {
            const response = await api.get<ResponseStructure<ActiveBookingDriverDTO>>('/drivers/seeActiveBooking', {
                params: { mobileno },
            });
            return response.data;
        } catch (e) {
            return null;
        }
    },

    startRide: async (bookingId: number, otp: number) => {
        const response = await api.post<ResponseStructure<any>>('/booking/startride', null, {
            params: { bookingId, otp }
        });
        return response.data;
    },

    completeRide: async (bookingId: number) => {
        const response = await api.put<ResponseStructure<any>>('/booking/completeride', null, {
            params: { bookingId }
        });
        return response.data;
    },

    cancelBooking: async (bookingId: number, driverId: number) => {
        const response = await api.put<ResponseStructure<string>>('/drivers/cancelbooking', null, {
            params: { bookingId, driverId }
        });
        return response.data;
    },

    collectPayment: (bookingId: number, paytype: 'CASH' | 'UPI') => api.get<ResponseStructure<string>>('/drivers/payment', { params: { bookingId, paytype } }),

    confirmUpiPayment: (bookingId: number) => api.post<ResponseStructure<string>>('/drivers/confirm', null, { params: { bookingId } }),

    getBookingHistory: async (mobileno: number) => {
        const response = await api.get<ResponseStructure<import('../types').BookingHistoryDTO[]>>('/drivers/bookingHistory', { params: { mobno: mobileno } });
        return response.data;
    },

    updateCurrentCity: async (driverId: number, location: import('../types').CurrentLocationDTO) => {
        const response = await api.put<ResponseStructure<any>>(`/drivers/${driverId}/currentcity`, location);
        return response.data;
    },

    deleteDriver: async (mobno: number) => {
        const response = await api.delete<ResponseStructure<string>>('/drivers/deleteDriver', { params: { mobno } });
        return response.data;
    }
};
