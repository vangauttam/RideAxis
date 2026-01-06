import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { driverService } from '../services/driverService';
import { Button } from '../components/Button';
import { Input } from '../components/Input';
import type { ActiveBookingDriverDTO } from '../types';
import { Car, MapPin, Phone, Check } from 'lucide-react';
import QRCode from 'react-qr-code';
import api from '../services/api'; // Direct API for profile fetch

const DriverHome = () => {
    const { user } = useAuth();
    const [driverId, setDriverId] = useState<number | null>(null);
    const [activeBooking, setActiveBooking] = useState<ActiveBookingDriverDTO | null>(null);
    const [otp, setOtp] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    // Fetch Driver ID
    useEffect(() => {
        if (!user) return;
        const fetchProfile = async () => {
            try {
                const response = await api.get('/drivers/FindDriver', { params: { mobno: user.mobile } });
                if (response.data && response.data.data) {
                    setDriverId(response.data.data.driverid);
                }
            } catch (e) {
                console.error("Failed to fetch driver profile", e);
            }
        };
        fetchProfile();
    }, [user]);

    // Poll for active booking
    useEffect(() => {
        if (!user) return;
        const interval = setInterval(async () => {
            try {
                const response = await driverService.seeActiveBooking(parseInt(user.mobile));
                // Response is ResponseStructure<ActiveBookingDriverDTO>
                if (response && response.data && response.data.booking) {
                    setActiveBooking(response.data);
                } else {
                    setActiveBooking(null);
                }
            } catch (e) {
                // Backend returns 500 when no booking is found, which is expected behavior for now.
                // We silently ignore it to keep the UI clean.
                setActiveBooking(null);
            }
        }, 6000);
        return () => clearInterval(interval);
    }, [user]);

    const handleStartRide = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!activeBooking || !otp) return;
        setIsLoading(true);
        try {
            await driverService.startRide(activeBooking.booking.id, parseInt(otp));
            setOtp('');
        } catch (e) {
            console.error(e);
            alert("Failed to start ride. Check OTP.");
        } finally {
            setIsLoading(false);
        }
    };

    const [completedBooking, setCompletedBooking] = useState<any | null>(null);
    const [qrCodeData, setQrCodeData] = useState<string | null>(null);

    const handleCompleteRide = async () => {
        if (!activeBooking) return;
        setIsLoading(true);
        try {
            await driverService.completeRide(activeBooking.booking.id);
            // Save booking details for payment before clearing active booking
            setCompletedBooking(activeBooking.booking); // Or fetch fresh from response
            setActiveBooking(null);
        } catch (e) {
            console.error(e);
            alert("Failed to complete ride.");
        } finally {
            setIsLoading(false);
        }
    };

    const [showSuccessModal, setShowSuccessModal] = useState(false);
    const [finalEarnings, setFinalEarnings] = useState<number>(0);

    const handlePaymentAction = async (type: 'CASH' | 'UPI') => {
        if (!completedBooking) return;

        if (type === 'CASH') {
            setIsLoading(true);
            try {
                await driverService.collectPayment(completedBooking.id, 'CASH');
                setFinalEarnings(completedBooking.fare || completedBooking.amount);
                setShowSuccessModal(true);
                // Don't clear completedBooking yet, wait for user to close modal
            } catch (e) {
                console.error(e);
                alert("Payment failed or already processed.");
                setCompletedBooking(null);
            } finally {
                setIsLoading(false);
            }
        } else {
            // UPI Flow - Frontend Generation
            const vpa = "hariteja.ramasahayam@okaxis";
            const name = "RideAxis";
            const amount = completedBooking.fare || completedBooking.amount;
            const upiUrl = `upi://pay?pa=${vpa}&pn=${name}&am=${amount}&cu=INR&tn=Ride Payment`;

            setQrCodeData(upiUrl);
        }
    }

    const handleConfirmUpi = async () => {
        if (!completedBooking) return;
        setIsLoading(true);
        try {
            await driverService.collectPayment(completedBooking.id, 'CASH');
            setFinalEarnings(completedBooking.fare || completedBooking.amount);
            setShowSuccessModal(true);
            setQrCodeData(null);
        } catch (e) {
            console.error("Confirmation Failed", e);
            alert("Could not confirm payment. Try again.");
        } finally {
            setIsLoading(false);
        }
    };

    const handleCloseSuccess = () => {
        setShowSuccessModal(false);
        setCompletedBooking(null);
        setFinalEarnings(0);
    };

    const handleCancelRide = async () => {
        if (!activeBooking || !driverId) return;
        if (!confirm("Are you sure you want to cancel?")) return;
        try {
            await driverService.cancelBooking(activeBooking.booking.id, driverId);
        } catch (e) {
            console.error(e);
        }
    };

    // -------------------------------------------------------------
    // RENDER: PAYMENT COLLECTION
    // -------------------------------------------------------------
    if (completedBooking) {
        return (
            <div className="h-screen flex flex-col items-center justify-center bg-gray-50 p-6 space-y-6 animate-fade-in relative">
                <div className="bg-white p-8 rounded-3xl shadow-2xl w-full max-w-md text-center space-y-6 relative overflow-hidden">
                    <div className="absolute top-0 left-0 w-full h-2 bg-green-500"></div>

                    {showSuccessModal ? (
                        <div className="space-y-6 animate-fade-in">
                            <div className="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-2 animate-bounce">
                                <Check size={40} className="text-green-600" />
                            </div>
                            <h2 className="text-3xl font-bold text-gray-900">Success!</h2>
                            <p className="text-gray-500">Ride completed & payment received.</p>

                            <div className="bg-gray-50 p-4 rounded-xl border border-gray-100">
                                <p className="text-xs font-bold text-gray-400 uppercase tracking-widest mb-1">You Earned</p>
                                <p className="text-5xl font-black text-green-600">₹{Math.round(finalEarnings)}</p>
                            </div>

                            <Button onClick={handleCloseSuccess} className="w-full bg-black text-white h-14 text-lg shadow-lg hover:shadow-xl mt-4">
                                Back to Online
                            </Button>
                        </div>
                    ) : (
                        !qrCodeData ? (
                            <>
                                <div className="bg-green-50 w-24 h-24 rounded-full flex items-center justify-center mx-auto mb-4 animate-slide-up">
                                    <span className="text-5xl font-bold text-green-600">₹</span>
                                </div>
                                <h2 className="text-3xl font-bold text-gray-900">Collect Payment</h2>
                                <div className="space-y-1">
                                    <p className="text-gray-500 font-medium uppercase tracking-wide text-xs">Total Fare</p>
                                    <div className="text-6xl font-bold text-green-600 tracking-tight">
                                        ₹{Math.round(completedBooking.fare || completedBooking.amount || 0)}
                                    </div>
                                </div>

                                <div className="pt-8 space-y-4">
                                    <Button className="w-full bg-black hover:bg-gray-800 h-14 text-xl shadow-lg hover:shadow-xl transition-all" onClick={() => handlePaymentAction('CASH')} isLoading={isLoading}>
                                        Cash Collected
                                    </Button>
                                    <Button variant="outline" className="w-full h-14 text-lg border-2" onClick={() => handlePaymentAction('UPI')} isLoading={isLoading}>
                                        Show UPI QR Code
                                    </Button>
                                </div>
                            </>
                        ) : (
                            <div className="space-y-6 animate-fade-in">
                                <div className="text-center space-y-2">
                                    <h3 className="text-2xl font-bold">Scan to Pay</h3>
                                    <p className="text-gray-500">Ask customer to scan this QR</p>
                                </div>

                                <div className="bg-white p-4 rounded-xl border-2 border-dashed border-gray-300 inline-block">
                                    <QRCode value={qrCodeData} size={200} />
                                </div>

                                <div className="text-xs text-gray-400 break-all px-4 hidden">
                                    {qrCodeData}
                                </div>

                                <div className="space-y-3">
                                    <Button className="w-full bg-blue-600 hover:bg-blue-700 h-12 text-lg shadow-lg" onClick={() => handleConfirmUpi()} isLoading={isLoading}>
                                        Payment Received
                                    </Button>
                                    <Button variant="ghost" className="w-full text-sm text-gray-500" onClick={() => setQrCodeData(null)}>
                                        Cancel / Back
                                    </Button>
                                </div>
                            </div>
                        )
                    )}
                </div>
            </div>
        );
    }

    if (!activeBooking) {
        return (
            <div className="h-screen flex flex-col items-center justify-center bg-gray-50 space-y-4">
                <div className="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center animate-pulse">
                    <Car size={40} className="text-green-600" />
                </div>
                <h2 className="text-2xl font-bold">You are Online</h2>
                <p className="text-gray-500">Waiting for ride requests...</p>
                {/* Debug Info */}
                <div className="absolute bottom-4 text-xs text-gray-400">Driver ID: {driverId || 'Loading...'}</div>
            </div>
        );
    }

    const booking = activeBooking.booking;
    const customer = booking.customer;
    const status = booking.bookingStatus || booking.bookingstatus; // Handle likely lowercase from backend

    return (
        <div className="min-h-screen bg-gray-100 p-4 flex flex-col items-center pt-20">
            {/* Active Ride Card */}
            <div className="w-full max-w-md bg-white rounded-2xl shadow-xl overflow-hidden">
                <div className="bg-black p-4 text-white flex justify-between items-center">
                    <div className="font-bold text-lg">Current Ride</div>
                    <div className="bg-white/20 px-3 py-1 rounded-full text-xs">{status}</div>
                </div>

                <div className="p-6 space-y-6">
                    {/* Customer Info */}
                    <div className="flex items-center space-x-4">
                        <div className="w-12 h-12 bg-gray-200 rounded-full flex items-center justify-center">
                            <span className="font-bold text-gray-600">{customer?.name?.[0] || 'U'}</span>
                        </div>
                        <div className="flex-1">
                            <h3 className="font-bold text-lg">{customer?.name || 'Customer'}</h3>
                            <a href={`tel:${customer?.mobileno}`} className="text-sm text-blue-600 flex items-center space-x-1">
                                <Phone size={14} /> <span>{customer?.mobileno}</span>
                            </a>
                        </div>
                        <div className="text-right">
                            <div className="text-xs text-gray-500">Fare</div>
                            <div className="font-bold text-xl">₹{booking.fare || booking.amount}</div>
                        </div>
                    </div>

                    {/* Locations */}
                    <div className="space-y-4 relative">
                        {/* Connecting Line */}
                        <div className="absolute left-3.5 top-8 bottom-8 w-0.5 bg-gray-300"></div>

                        <div className="flex items-start space-x-3">
                            <div className="p-1 min-w-8 text-green-600"><div className="w-3 h-3 bg-green-600 rounded-full mt-1.5 ml-2"></div></div>
                            <div>
                                <div className="text-xs text-gray-500">PICKUP</div>
                                <div className="font-medium">{booking.sourcelocation || booking.sourceLoc || 'Current Location'}</div>
                            </div>
                        </div>
                        <div className="flex items-start space-x-3">
                            <div className="p-1 min-w-8 text-red-600"><MapPin size={20} /></div>
                            <div>
                                <div className="text-xs text-gray-500">DROP</div>
                                <div className="font-medium">{booking.destinationlocation || booking.destinationLoc}</div>
                            </div>
                        </div>
                    </div>

                    {/* Actions */}
                    <div className="pt-4 border-t">
                        {(status === 'ACCEPTED' || status === 'BOOKED' || status === 'booked') && (
                            <form onSubmit={handleStartRide} className="space-y-3">
                                <Input
                                    label="Enter OTP to Start"
                                    placeholder="4-digit OTP"
                                    value={otp}
                                    onChange={(e) => setOtp(e.target.value)}
                                    className="text-center font-bold tracking-widest text-lg"
                                    maxLength={4}
                                />
                                <Button type="submit" className="w-full" isLoading={isLoading} disabled={otp.length !== 4}>
                                    START RIDE
                                </Button>
                            </form>
                        )}

                        {status === 'IN_PROGRESS' && (
                            <Button className="w-full bg-green-600 hover:bg-green-700" onClick={handleCompleteRide} isLoading={isLoading}>
                                COMPLETE RIDE
                            </Button>
                        )}

                        <Button variant="ghost" className="w-full text-red-500 mt-2" onClick={handleCancelRide} disabled={isLoading}>
                            Cancel Booking
                        </Button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default DriverHome;
