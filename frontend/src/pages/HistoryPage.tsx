import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { customerService } from '../services/customerService';
import { driverService } from '../services/driverService';
import type { BookingHistoryDTO } from '../types';
import { Car } from 'lucide-react';
import { Header } from '../components/Header';

const HistoryPage = () => {
    const { user } = useAuth();
    const [history, setHistory] = useState<BookingHistoryDTO | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchHistory = async () => {
            if (!user) return;
            try {
                let response;
                if (user.role === 'CUSTOMER') {
                    response = await customerService.getBookingHistory(parseInt(user.mobile));
                } else {
                    response = await driverService.getBookingHistory(parseInt(user.mobile));
                }

                if (response && response.data) {
                    const list = response.data;
                    if (Array.isArray(list) && list.length > 0) {
                        setHistory(list[0]);
                    }
                }
            } catch (error) {
                console.error("Failed to fetch history", error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchHistory();
    }, [user]);

    if (!user) return null;

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <div className="flex-1 max-w-2xl w-full mx-auto p-4 md:p-6 space-y-6">

                <Header title="Your Rides" />

                {isLoading ? (
                    <div className="flex flex-col items-center justify-center py-20 space-y-4">
                        <div className="w-10 h-10 border-4 border-gray-200 border-t-black rounded-full animate-spin"></div>
                        <p className="text-gray-500">Loading history...</p>
                    </div>
                ) : !history || !history.history || history.history.length === 0 ? (
                    <div className="bg-white rounded-3xl p-10 text-center shadow-sm border border-gray-100">
                        <div className="w-16 h-16 bg-gray-50 rounded-full flex items-center justify-center mx-auto mb-4">
                            <Car className="w-8 h-8 text-gray-400" />
                        </div>
                        <h3 className="text-xl font-bold text-gray-900">No rides yet</h3>
                        <p className="text-gray-500 mt-2">Your completed rides will appear here.</p>
                    </div>
                ) : (
                    <div className="space-y-6 animate-fade-in">
                        {/* Summary Card */}
                        <div className="bg-gradient-to-r from-gray-900 to-gray-800 text-white p-6 rounded-3xl shadow-lg flex items-center justify-between">
                            <div>
                                <div className="text-gray-400 text-sm font-medium uppercase tracking-wider mb-1">
                                    {user.role === 'CUSTOMER' ? 'Total Spent' : 'Total Earnings'}
                                </div>
                                <div className="text-3xl font-bold">
                                    ₹{Math.round(history.totalamount)}
                                </div>
                            </div>
                            <div className="w-12 h-12 bg-white/10 rounded-full flex items-center justify-center">
                                <span className="text-xl font-bold">{history.history.length || 0}</span>
                            </div>
                        </div>

                        <div className="space-y-4">
                            {history.history.slice().reverse().map((ride, index) => (
                                <div key={index} className="bg-white p-6 rounded-3xl shadow-sm border border-gray-100 hover:shadow-md transition-shadow">
                                    <div className="flex justify-between items-start mb-4">
                                        <div className="flex items-center space-x-3">
                                            <div className="w-10 h-10 bg-gray-50 rounded-full flex items-center justify-center">
                                                <Car className="w-5 h-5 text-gray-700" />
                                            </div>
                                            <div>
                                                <div className="font-bold text-gray-900">Completed Ride</div>
                                                <div className="text-xs text-green-600 font-medium px-2 py-0.5 bg-green-50 rounded-full w-fit mt-1">SUCCESS</div>
                                            </div>
                                        </div>
                                        <div className="text-right">
                                            <div className="font-bold text-xl text-gray-900">₹{Math.round(ride.fare)}</div>
                                            <div className="text-xs text-gray-400">{Math.round(ride.distanceTravelled)} km</div>
                                        </div>
                                    </div>

                                    <div className="space-y-4 relative">
                                        {/* Connecting Line */}
                                        <div className="absolute left-2.5 top-3 bottom-3 w-0.5 bg-gray-100"></div>

                                        <div className="flex items-start space-x-3 relative z-10">
                                            <div className="w-5 h-5 rounded-full border-2 border-white shadow-sm bg-green-500 mt-0.5 flex-shrink-0"></div>
                                            <div>
                                                <div className="text-xs font-bold text-gray-400 uppercase">Pickup</div>
                                                <div className="text-sm font-medium text-gray-900">{ride.sourceloc || 'Unknown Location'}</div>
                                            </div>
                                        </div>

                                        <div className="flex items-start space-x-3 relative z-10">
                                            <div className="w-5 h-5 rounded-full border-2 border-white shadow-sm bg-black mt-0.5 flex-shrink-0"></div>
                                            <div>
                                                <div className="text-xs font-bold text-gray-400 uppercase">Dropoff</div>
                                                <div className="text-sm font-medium text-gray-900">{ride.destinationloc}</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default HistoryPage;
