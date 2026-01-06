export interface User {
    mobileno: number;
    role: 'CUSTOMER' | 'DRIVER';
    // Add other user fields if returned by login/profile
}

export interface RegCustomerDto {
    name: string;
    age: number;
    gender: string;
    mobileno: number;
    email: string;
    latitude: string;
    longitude: string;
    password?: string;
}

export interface RegDriverVehicleDTO {
    licenceno: string;
    upiid: string;
    dname: string;
    age: number;
    mobileno: number;
    gender: string;
    mailid: string; // backend uses mailid, check DTO
    vname: string;
    vehicleno: string;
    type: string;
    model: string;
    capacity: number;
    latitude: number;
    longitude: number;
    priceperkm: number;
    averagespeed: number;
    password?: string;
}

export interface LoginRequestDTO {
    mobileNo: number;
    password?: string;
}

export interface ResponseStructure<T> {
    statuscode: number;
    message: string;
    data: T;
}

export interface Vehicle {
    id: number; // Mapped from backend vehicleid if needed, but backend sends vehicleid field
    vehicleid: number; // Actual backend core field
    vname: string;
    vehicleno: string;
    type: string;
    model: string;
    capacity: number;
    priceperkm: number;
    averagespeed: number;
    availableStatus: string;
    latitude: number;
    longitude: number;
}

export interface VehicleDetailDTO {
    vehicle: Vehicle;
    fare: number;
    estimatedtime: number;
}

export interface AvailableVehicleDTO {
    customer: any; // Using any for now to avoid circular dependency or deep typing if not needed
    distance: number;
    source: string;
    destination: string; // Backend typo corrected
    availableVehicles: VehicleDetailDTO[];
}

export interface ActiveBookingDriverDTO {
    drivername: string;
    drivermobno: number;
    booking: Booking; // This booking object likely contains customer relation
    currentlocation: string;
}

export interface Booking {
    id: number;
    bookingStatus: string;
    bookingstatus?: string; // Backend mixed casing?
    otp: number;
    amount?: number;
    fare: number; // Backend sends fare
    sourcelocation?: string; // Backend entity field
    destinationlocation?: string; // Backend entity field
    estimatedtimerequired?: number;
    customer?: any;
    vehicle?: Vehicle; // To access vehicle details
    sourceLoc?: string; // Additional frontend alias if used
    destinationLoc?: string; // Additional frontend alias if used
}


export interface ActiveBookingDTO {
    customername: string;
    customermobno: number;
    booking: Booking;
    currentlocation: string;
}

export interface BookingDTO {
    vehicleId: number;
    sourceLoc: string;
    destinationLoc: string;
    fare: number;
    distanceTravelled: number;
    estimatedTime: number;
    customer?: any;
    vehicle?: any;
}

export interface RideDetailsDTO {
    sourceloc: string;
    destinationloc: string;
    distanceTravelled: number;
    fare: number;
    paymentstatus?: string;
}

export interface CurrentLocationDTO {
    latitude: number;
    longitude: number;
    currentcity?: string;
}

export interface BookingHistoryDTO {
    history: RideDetailsDTO[];
    totalamount: number;
}
