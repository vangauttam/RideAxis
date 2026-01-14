import { useState, useEffect, useRef } from 'react';
import { useAuth } from '../context/AuthContext';
import { customerService } from '../services/customerService';
import { Button } from '../components/Button';
import MapComponent from '../components/MapComponent';
import type { ActiveBookingDTO, BookingDTO, VehicleDetailDTO } from '../types';
import { Car, Check, Star } from 'lucide-react';

const CustomerHome = () => {
    const { user } = useAuth();
    const [currentCity, setCurrentCity] = useState('Current Location');
    const [customerId, setCustomerId] = useState<number | null>(null);
    const [center, setCenter] = useState<[number, number]>([12.9716, 77.5946]);
    const [destination, setDestination] = useState('');
    const [availableVehicles, setAvailableVehicles] = useState<VehicleDetailDTO[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [activeBooking, setActiveBooking] = useState<ActiveBookingDTO | null>(null);
    const [selectedVehicle, setSelectedVehicle] = useState<VehicleDetailDTO | null>(null);
    const [vehicleFilter, setVehicleFilter] = useState('ALL');

    // Ride Summary State
    const [summaryData, setSummaryData] = useState<ActiveBookingDTO | null>(null);
    const [isZombiePayment, setIsZombiePayment] = useState(false);

    const [rating, setRating] = useState(0);
    const [lastHistoryStatus, setLastHistoryStatus] = useState<string>('Initializing Probe...');

    // Get User Location on Mount
    useEffect(() => {
        if ("geolocation" in navigator) {
            navigator.geolocation.getCurrentPosition(async (position) => {
                const { latitude, longitude } = position.coords;
                setCenter([latitude, longitude]);

                // Reverse Geocode to get readable address
                try {
                    const res = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${latitude}&lon=${longitude}`);
                    const data = await res.json();
                    if (data && data.address) {
                        // Prioritize: City -> Town -> Village -> Suburb -> or simplified display name
                        const city = data.address.city || data.address.town || data.address.village || data.address.suburb || data.address.county || "Unknown Location";
                        setCurrentCity(city);
                    }
                } catch (e) {
                    console.error("Reverse geocoding failed", e);
                    // Fallback is already 'Current Location'
                }
            }, async (_err) => { // Fixed unused variable
                console.warn("Geolocation restricted (HTTP), trying IP fallback...");
                try {
                    const res = await fetch('https://ipapi.co/json/');
                    const data = await res.json();
                    if (data.latitude && data.longitude) {
                        setCenter([data.latitude, data.longitude]);
                        setCurrentCity(data.city || "Detected Location");
                    }
                } catch (e) {
                    console.error("IP fallback failed", e);
                }
            });
        }
    }, []);

    // Get Customer Profile (ID) on Mount
    useEffect(() => {
        if (!user) return;
        const fetchProfile = async () => {
            try {
                const response = await customerService.getProfile(parseInt(user.mobile));
                if (response && response.data) {
                    setCustomerId(response.data.id);
                }
            } catch (e) {
                console.error("Failed to fetch profile", e);
            }
        };
        fetchProfile();
    }, [user]);

    // 1. Polling Effect - Just fetches Active Ride
    useEffect(() => {
        if (!user || isZombiePayment) return; // Don't poll active if we are in zombie mode

        const pollRide = async () => {
            try {
                const response = await customerService.seeActiveBooking(parseInt(user.mobile));
                const newData = (response && response.data && response.data.booking) ? response.data : null;
                setActiveBooking(newData);
            } catch (e) {
                console.error(e);
            }
        };

        pollRide(); // Initial call
        const interval = setInterval(pollRide, 6000);
        return () => clearInterval(interval);
    }, [user, isZombiePayment]);

    // 2. Transition Effect - Detects RIDE END -> ZOMBIE PAYMENT MODE
    const prevBookingRef = useRef<ActiveBookingDTO | null>(null);

    useEffect(() => {
        const currentHookBooking = activeBooking;
        const previousBooking = prevBookingRef.current;

        // Check if we lost the booking (Active -> Null)
        if (previousBooking && !currentHookBooking) {
            const status = previousBooking.booking.bookingstatus || previousBooking.booking.bookingStatus;

            // If ride was in progress or payment pending, and now it's gone -> ENTER ZOMBIE PAYMENT MODE
            if (status === 'IN_PROGRESS' || status === 'PAYMENT_PENDING') {
                setSummaryData(previousBooking); // Save data for display
                setIsZombiePayment(true);        // Start Zombie Mode
            }
        }

        // Update ref for next render
        prevBookingRef.current = currentHookBooking;
    }, [activeBooking]);

    // 3. Zombie Polling - Probes for Payment via History
    useEffect(() => {
        if (!isZombiePayment || !summaryData) return;

        const probePayment = async () => {
            try {
                if (!user) return;
                const response = await customerService.getBookingHistory(parseInt(user.mobile));
                if (response && response.data && response.data.length > 0) {
                    // Assuming the latest booking is the first one or at the end? 
                    // Usually history is sorted. Let's check the history list inside the response.
                    // Response is List<BookingHistoryDTO>, which contains List<RideDetailsDTO>.
                    // Actually, the structure in CustomerService is List<BookingHistoryDTO> where each DTO has a LIST of rides.
                    // And there is only ONE BookingHistoryDTO added to the list: responseList.add(historyDTO);

                    const history = response.data[0].history;
                    if (history && history.length > 0) {
                        // scan RECENT history for the SPECIFIC ride that just ended.
                        // We filter by FARE matching (since we don't have ID in history DTO).
                        // This prevents finding an old paid ride.
                        const expectedFare = summaryData.booking.fare || summaryData.booking.amount || 0;

                        const matchedRide = history.find(r =>
                            Math.abs(r.fare - expectedFare) < 1.0 // Float tolerance
                        );

                        console.log("History probe details:", {
                            totalRides: history.length,
                            expectedFare,
                            foundMatch: matchedRide,
                            status: matchedRide?.paymentstatus
                        });

                        if (matchedRide) {
                            if (matchedRide.paymentstatus && matchedRide.paymentstatus.toUpperCase() === 'PAID') {
                                setLastHistoryStatus('PAYMENT CONFIRMED');
                                setIsZombiePayment(false);
                            } else {
                                setLastHistoryStatus(`Ride Found. Waiting for Payment. Current Status: ${matchedRide.paymentstatus}`);
                            }
                        } else {
                            setLastHistoryStatus(`Scanning... Ride with fare ₹${expectedFare} not found in history yet.`);
                        }
                    } else {
                        setLastHistoryStatus('History Empty');
                    }
                }
            } catch (e) {
                console.error("Payment Probe Failed", e);
                setLastHistoryStatus('Probe Error: ' + String(e));
            }
        };

        const interval = setInterval(probePayment, 6000);
        return () => clearInterval(interval);
    }, [isZombiePayment, summaryData, user]);

    const [tripDistance, setTripDistance] = useState(0);

    const handleSearch = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        if (!user) return;
        try {
            const trimmedDestination = destination.trim();
            console.log("Sending request...", { mob: user.mobile, dest: trimmedDestination });
            const response = await customerService.getAvailableVehicles(parseInt(user.mobile), trimmedDestination);
            console.log("Response:", response);

            if (response && response.data) {
                if (response.data.source) {
                    setCurrentCity(response.data.source);
                }

                // Store Calculation
                if (response.data.distance) {
                    setTripDistance(response.data.distance);
                }

                if (response.data.availableVehicles && response.data.availableVehicles.length === 0) {
                    alert("No vehicles found in your area (" + response.data.source + "). Try a different location.");
                } else if (response.data.availableVehicles) {
                    setAvailableVehicles(response.data.availableVehicles);
                }
            } else {
                alert("No data received from server.");
            }
        } catch (error: any) {
            console.error("Search Error:", error);
            const msg = error.response?.data?.message || error.message || 'Could not fetch vehicles';
            alert("Error: " + msg);
        } finally {
            setIsLoading(false);
        }
    };

    const handleBook = async () => {
        if (!selectedVehicle || !user) return;
        setIsLoading(true);
        try {
            const bookingDto: BookingDTO = {
                vehicleId: selectedVehicle.vehicle.vehicleid, // Use vehicleid from backend entity
                sourceLoc: currentCity, // FIXED: Use the actual current city name
                destinationLoc: destination,
                fare: selectedVehicle.fare,
                distanceTravelled: tripDistance, // FIXED: Use the calculated distance
                estimatedTime: selectedVehicle.estimatedtime
            };
            await customerService.bookVehicle(parseInt(user.mobile), bookingDto);
            // Wait for next poll to update status
            setAvailableVehicles([]); // Clear selection
            alert('Ride Booked Successfully!');
        } catch (error: any) {
            console.error(error);
            const msg = error.response?.data?.message || 'Booking failed';
            alert('Error: ' + msg);
        } finally {
            setIsLoading(false);
        }
    };

    const handleCancel = async () => {
        if (!activeBooking || !activeBooking.booking.id || !user) return;
        if (!customerId) {
            alert("Customer ID not loaded. Please wait or refresh.");
            return;
        }
        if (!confirm("Are you sure you want to cancel the ride?")) return;

        try {
            await customerService.cancelRide(customerId, activeBooking.booking.id);
            // Poll will clear the active booking
        } catch (e: any) {
            console.error(e);
            alert("Failed to cancel: " + (e.response?.data?.message || e.message));
        }
    };

    return (
        <div className="relative h-screen w-full overflow-hidden flex flex-col">
            {/* Map Layer */}
            <div className="absolute inset-0 z-0">
                <MapComponent
                    center={center}
                    vehicles={availableVehicles.map(v => ({
                        id: v.vehicle.id,
                        lat: v.vehicle.latitude,
                        lng: v.vehicle.longitude,
                        type: v.vehicle.type
                    }))}
                />
            </div>

            {/* UI Layer */}
            <div className="pointer-events-none relative z-10 flex h-full flex-col justify-end p-4 md:p-6 pb-8">

                {/* Main Content Area */}
                {isZombiePayment && summaryData ? (
                    /* ZOMBIE PAYMENT PANEL (Payment Pending) */
                    <div className="pointer-events-auto w-full max-w-md bg-white/90 backdrop-blur-xl border border-white/40 rounded-3xl shadow-2xl p-6 mx-auto md:mx-0 space-y-6 animate-slide-up">
                        <div className="text-center space-y-6 py-4">
                            <div className="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mx-auto animate-pulse">
                                <span className="text-4xl">₹</span>
                            </div>
                            <div>
                                <h2 className="text-2xl font-bold tracking-tight">Payment Pending</h2>
                                <p className="text-gray-500 text-sm mt-1">Please pay the driver to complete the ride</p>
                            </div>
                            <div className="bg-gray-50 p-4 rounded-2xl border border-gray-100">
                                <p className="text-xs font-bold text-gray-400 uppercase tracking-widest mb-1">Total Amount</p>
                                <p className="text-5xl font-black text-gray-900">₹{Math.round(summaryData.booking.fare || summaryData.booking.amount || 0)}</p>
                            </div>
                            <div className="text-sm font-medium text-green-600 flex items-center justify-center gap-2">
                                <div className="w-2 h-2 rounded-full bg-green-600 animate-bounce"></div>
                                Waiting for driver confirmation...
                            </div>
                            {/* Debug Info for User/Dev */}
                            <p className="text-[10px] text-gray-400 mt-2 font-mono">
                                System Status: {lastHistoryStatus}
                            </p>
                        </div>
                    </div>
                ) : !activeBooking ? (
                    /* Search / Booking Panel */
                    <div className="pointer-events-auto w-full max-w-md glass-dark md:glass bg-white/95 md:bg-white/80 backdrop-blur-xl rounded-3xl shadow-2xl p-6 space-y-6 mx-auto md:mx-0 animate-slide-up">
                        <div className="flex items-center justify-between">
                            <h2 className="text-2xl font-bold tracking-tight">Where to?</h2>
                            {currentCity !== 'Current Location' && (
                                <span className="text-xs font-medium bg-black text-white px-2 py-1 rounded-full">{currentCity}</span>
                            )}
                        </div>

                        {/* Vehicle Type Filters */}
                        <div className="flex space-x-2 overflow-x-auto custom-scrollbar pb-2">
                            {['ALL', 'CAR', 'BIKE', 'AUTO', 'SUV', 'SEDAN', 'HATCHBACK', 'PREMIUM', 'ELECTRIC'].map((type) => (
                                <button
                                    key={type}
                                    type="button"
                                    onClick={() => setVehicleFilter(type)}
                                    className={`px-4 py-2 rounded-full text-xs font-bold whitespace-nowrap transition-all ${vehicleFilter === type
                                        ? 'bg-black text-white shadow-md'
                                        : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
                                        }`}
                                >
                                    {type}
                                </button>
                            ))}
                        </div>

                        <form onSubmit={handleSearch} className="space-y-4">
                            {/* Current Location Input Mock */}
                            <div className="relative group">
                                <div className="absolute left-4 top-1/2 -translate-y-1/2 w-2 h-2 bg-black rounded-full"></div>
                                <div className="absolute left-4.5 top-8 w-0.5 h-6 bg-gray-300"></div>
                                <div className="w-full bg-gray-50 border border-transparent group-hover:bg-white group-hover:border-gray-200 transition-all rounded-xl p-4 pl-10 text-sm font-medium text-gray-900 pointer-events-none">
                                    {currentCity}
                                </div>
                            </div>

                            {/* Destination Input */}
                            <div className="relative">
                                <div className="absolute left-4 top-1/2 -translate-y-1/2 w-2 h-2 bg-black rounded-full ring-4 ring-black/10"></div>
                                <input
                                    className="w-full bg-gray-50 border border-transparent focus:bg-white focus:border-black transition-all rounded-xl p-4 pl-10 text-sm font-medium outline-none shadow-sm"
                                    placeholder="Enter destination"
                                    value={destination}
                                    onChange={(e) => setDestination(e.target.value)}
                                    required
                                />
                            </div>

                            <Button type="submit" className="w-full h-12 shadow-lg hover:shadow-xl transform transition-transform active:scale-95" isLoading={isLoading && availableVehicles.length === 0}>
                                Find Ride
                            </Button>
                        </form>

                        {/* Vehicle List */}
                        {availableVehicles.length > 0 && (
                            <div className="mt-4 space-y-3 max-h-72 overflow-y-auto pr-1 custom-scrollbar animate-fade-in">
                                <h3 className="text-xs font-bold text-gray-400 uppercase tracking-wider mb-3">Available Rides</h3>
                                {availableVehicles
                                    .filter(v => vehicleFilter === 'ALL' || (v.vehicle.type && v.vehicle.type.toUpperCase() === vehicleFilter))
                                    .map((v) => (
                                        <div
                                            key={v.vehicle.id}
                                            onClick={() => setSelectedVehicle(v)}
                                            className={`relative overflow-hidden flex items-center justify-between p-4 rounded-2xl border-2 cursor-pointer transition-all duration-300 ${selectedVehicle?.vehicle.id === v.vehicle.id ? 'bg-black text-white shadow-xl scale-[1.02] border-black' : 'border-transparent hover:bg-gray-50 text-gray-900'}`}
                                        >
                                            <div className="flex items-center space-x-4">
                                                <div className={`w-14 h-14 rounded-xl flex items-center justify-center ${selectedVehicle?.vehicle.id === v.vehicle.id ? 'bg-gray-800' : 'bg-gray-100'}`}>
                                                    <Car size={32} className={selectedVehicle?.vehicle.id === v.vehicle.id ? 'text-white' : 'text-gray-800'} />
                                                </div>
                                                <div>
                                                    <div className={`font-bold text-lg ${selectedVehicle?.vehicle.id === v.vehicle.id ? 'text-white' : 'text-gray-900'}`}>{v.vehicle.vname}</div>
                                                    <div className={`text-xs font-medium ${selectedVehicle?.vehicle.id === v.vehicle.id ? 'text-gray-400' : 'text-gray-500'}`}>{v.estimatedtime} mins • {v.vehicle.type}</div>
                                                </div>
                                            </div>
                                            <div className="text-right">
                                                <div className={`font-bold text-xl ${selectedVehicle?.vehicle.id === v.vehicle.id ? 'text-white' : 'text-gray-900'}`}>₹{Math.round(v.fare)}</div>
                                            </div>
                                        </div>
                                    ))}
                                <Button
                                    className="w-full mt-4 h-14 text-lg shadow-xl"
                                    disabled={!selectedVehicle}
                                    onClick={handleBook}
                                    isLoading={isLoading}
                                >
                                    Confirm Booking
                                </Button>
                            </div>
                        )}
                    </div>
                ) : (
                    /* Active Ride Panel */
                    <div className="pointer-events-auto w-full max-w-md bg-white/90 backdrop-blur-xl border border-white/40 rounded-3xl shadow-2xl p-6 mx-auto md:mx-0 space-y-6 animate-slide-up">
                        {(activeBooking.booking.bookingstatus === 'PAYMENT_PENDING' || activeBooking.booking.bookingStatus === 'PAYMENT_PENDING') ? (
                            <div className="text-center space-y-6 py-4">
                                <div className="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mx-auto animate-pulse">
                                    <span className="text-4xl">₹</span>
                                </div>
                                <div>
                                    <h2 className="text-2xl font-bold tracking-tight">Payment Pending</h2>
                                    <p className="text-gray-500 text-sm mt-1">Please pay the driver to complete the ride</p>
                                </div>
                                <div className="bg-gray-50 p-4 rounded-2xl border border-gray-100">
                                    <p className="text-xs font-bold text-gray-400 uppercase tracking-widest mb-1">Total Amount</p>
                                    <p className="text-5xl font-black text-gray-900">₹{Math.round(activeBooking.booking.fare || activeBooking.booking.amount || 0)}</p>
                                </div>
                                <div className="text-sm font-medium text-green-600 flex items-center justify-center gap-2">
                                    <div className="w-2 h-2 rounded-full bg-green-600 animate-bounce"></div>
                                    Waiting for driver confirmation...
                                </div>
                            </div>
                        ) : (
                            <>
                                <div className="flex justify-between items-start">
                                    <div className="space-y-1">
                                        <h2 className="text-2xl font-bold tracking-tight">
                                            {(activeBooking.booking.bookingstatus === 'IN_PROGRESS' || activeBooking.booking.bookingStatus === 'IN_PROGRESS')
                                                ? 'Ride In Progress'
                                                : `Ride ${activeBooking.booking.bookingstatus || activeBooking.booking.bookingStatus}`}
                                        </h2>
                                        <div className="text-sm font-medium text-gray-500 flex items-center gap-2">
                                            <div className={`w-2 h-2 rounded-full ${activeBooking.booking.bookingstatus === 'IN_PROGRESS' || activeBooking.booking.bookingStatus === 'IN_PROGRESS' ? 'bg-green-500 animate-pulse' : 'bg-blue-500'}`}></div>
                                            {(activeBooking.booking.bookingstatus === 'IN_PROGRESS' || activeBooking.booking.bookingStatus === 'IN_PROGRESS')
                                                ? 'Heading to destination'
                                                : `Arriving in ${Math.round(activeBooking.booking.estimatedtimerequired || 5)} mins`}
                                        </div>
                                    </div>
                                    <div className="bg-black text-white px-4 py-2 rounded-xl text-lg font-bold shadow-lg">
                                        {activeBooking.booking.otp}
                                    </div>
                                </div>

                                {/* Progress Bar Visual */}
                                <div className="w-full bg-gray-100 h-1.5 rounded-full overflow-hidden">
                                    <div className={`h-full bg-black rounded-full transition-all duration-1000 ${(activeBooking.booking.bookingstatus === 'IN_PROGRESS' || activeBooking.booking.bookingStatus === 'IN_PROGRESS') ? 'w-full animate-pulse' : 'w-1/3'}`}></div>
                                </div>

                                <div className="flex items-center space-x-4 bg-gray-50 p-4 rounded-2xl border border-gray-100">
                                    <div className="w-16 h-16 bg-white rounded-full flex items-center justify-center shadow-sm border border-gray-100">
                                        <Car size={32} />
                                    </div>
                                    <div className="flex-1">
                                        <div className="font-bold text-lg">{activeBooking.booking.vehicle?.vname || 'Premium Ride'}</div>
                                        <div className="text-gray-500 text-sm font-medium">{activeBooking.booking.vehicle?.model} • {activeBooking.booking.vehicle?.vehicleno}</div>
                                    </div>
                                    <div className="text-right">
                                        <div className="font-bold text-xl">₹{Math.round(activeBooking.booking.fare || activeBooking.booking.amount || 0)}</div>
                                        <div className="text-xs text-gray-400">Fixed Fare</div>
                                    </div>
                                </div>

                                <div className="flex space-x-3 pt-2">
                                    {(activeBooking.booking.bookingstatus !== 'IN_PROGRESS' && activeBooking.booking.bookingStatus !== 'IN_PROGRESS') && (
                                        <Button variant="outline" className="w-full border-red-200 text-red-600 hover:bg-red-50 hover:border-red-300" onClick={handleCancel}>Cancel</Button>
                                    )}
                                </div>
                            </>
                        )}
                    </div>
                )}

                {/* Ride Summary Modal - Shows ONLY after Zombie Mode exits (Payment Confirmed) */}
                {summaryData && !isZombiePayment && (
                    <div className="pointer-events-auto fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm p-4 animate-fade-in">
                        <div className="bg-white w-full max-w-sm rounded-3xl p-6 shadow-2xl space-y-6 relative overflow-hidden text-center">
                            <div className="absolute top-0 left-0 w-full h-2 bg-green-500"></div>

                            <div className="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-2 animate-bounce">
                                <Check size={40} className="text-green-600" />
                            </div>

                            <h2 className="text-2xl font-bold text-gray-900">You've Arrived!</h2>
                            <p className="text-gray-500 text-sm">Hope you enjoyed your ride with {summaryData.booking.vehicle?.vname}</p>

                            <div className="py-4 border-t border-b border-gray-100 space-y-1">
                                <p className="text-xs font-bold text-gray-400 uppercase tracking-widest">Total Fare</p>
                                <p className="text-4xl font-black text-gray-900">₹{Math.round(summaryData.booking.fare || summaryData.booking.amount || 0)}</p>
                            </div>

                            <div className="space-y-2">
                                <p className="text-sm font-medium text-gray-700">Rate your driver</p>
                                <div className="flex justify-center space-x-2">
                                    {[1, 2, 3, 4, 5].map((star) => (
                                        <button
                                            key={star}
                                            onClick={() => setRating(star)}
                                            className="text-yellow-400 hover:scale-110 transition-transform focus:outline-none"
                                        >
                                            <Star fill={star <= rating ? "currentColor" : "none"} size={28} />
                                        </button>
                                    ))}
                                </div>
                            </div>

                            <Button onClick={() => setSummaryData(null)} className="w-full bg-black text-white h-12 shadow-lg hover:shadow-xl mt-4">
                                Close & Book New Ride
                            </Button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default CustomerHome;
