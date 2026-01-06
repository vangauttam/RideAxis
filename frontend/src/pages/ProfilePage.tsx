import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { driverService } from '../services/driverService';
import { customerService } from '../services/customerService';
import api from '../services/api';
import { MapPin, Phone, Mail, User as UserIcon } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { Button } from '../components/Button';
import { Header } from '../components/Header';

const ProfilePage = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const [profile, setProfile] = useState<any>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [isUpdating, setIsUpdating] = useState(false);

    useEffect(() => {
        const fetchProfile = async () => {
            if (!user) return;
            try {
                let response;
                if (user.role === 'CUSTOMER') {
                    // Re-using find customer endpoint
                    response = await api.get('/customer/findcustomer', { params: { mobno: user.mobile } });
                } else {
                    response = await api.get('/drivers/FindDriver', { params: { mobno: user.mobile } });
                }

                if (response && response.data && response.data.data) {
                    setProfile(response.data.data);
                }
            } catch (error) {
                console.error("Failed to fetch profile", error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchProfile();
    }, [user]);

    const handleUpdateLocation = async () => {
        if (!user || user.role !== 'DRIVER' || !profile) return;
        setIsUpdating(true);

        if ("geolocation" in navigator) {
            navigator.geolocation.getCurrentPosition(async (position) => {
                try {
                    const { latitude, longitude } = position.coords;

                    const response = await driverService.updateCurrentCity(profile.driverid || profile.id, {
                        latitude,
                        longitude
                    });

                    alert("Location & Vehicle Data Synced!");

                    if (response && response.data) {
                        setProfile((prev: any) => ({
                            ...prev,
                            vehicle: response.data
                        }));
                    }
                } catch (e) {
                    console.error(e);
                    alert("Failed to update location.");
                } finally {
                    setIsUpdating(false);
                }
            }, (error) => {
                console.warn("Location permission denied or restricted (HTTP):", error);
                alert("Could not access GPS. Please ensure Location Permissions are allowed.");
                setIsUpdating(false);
            });
        }
    };

    if (!user) return null;

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <div className="flex-1 max-w-2xl w-full mx-auto p-4 md:p-6 space-y-6">
                <Header title="Profile" />

                {isLoading ? (
                    <div className="flex justify-center py-20"><div className="animate-spin rounded-full h-10 w-10 border-b-2 border-black"></div></div>
                ) : !profile ? (
                    <div className="text-center py-20 text-gray-500">Could not load profile.</div>
                ) : (
                    <div className="space-y-6 animate-fade-in">
                        {/* Profile Header */}
                        <div className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100 flex flex-col items-center text-center space-y-4">
                            <div className="w-24 h-24 bg-gray-100 rounded-full flex items-center justify-center mb-2">
                                <span className="text-3xl font-bold text-gray-400">
                                    {(profile.name || profile.dname || 'U')[0].toUpperCase()}
                                </span>
                            </div>
                            <div>
                                <h2 className="text-2xl font-bold text-gray-900">{profile.name || profile.dname}</h2>
                                <span className="inline-block px-3 py-1 bg-black text-white text-xs font-bold rounded-full mt-2 uppercase tracking-wide">
                                    {user.role}
                                </span>
                            </div>
                        </div>

                        {/* Details Card */}
                        <div className="bg-white p-6 rounded-3xl shadow-sm border border-gray-100 space-y-6">
                            <h3 className="font-bold text-gray-900 text-lg">Contact Info</h3>

                            <div className="space-y-4">
                                <div className="flex items-center space-x-4 p-4 bg-gray-50 rounded-2xl">
                                    <Phone className="text-gray-400" size={20} />
                                    <div>
                                        <div className="text-xs text-gray-400 font-bold uppercase">Mobile</div>
                                        <div className="font-medium">{profile.mobileno}</div>
                                    </div>
                                </div>
                                <div className="flex items-center space-x-4 p-4 bg-gray-50 rounded-2xl">
                                    <Mail className="text-gray-400" size={20} />
                                    <div>
                                        <div className="text-xs text-gray-400 font-bold uppercase">Email</div>
                                        {/* Backend uses emailid for Customer, mailid for Driver */}
                                        <div className="font-medium">{profile.emailid || profile.mailid || 'Not provided'}</div>
                                    </div>
                                </div>
                                <div className="flex items-center space-x-4 p-4 bg-gray-50 rounded-2xl">
                                    <UserIcon className="text-gray-400" size={20} />
                                    <div>
                                        <div className="text-xs text-gray-400 font-bold uppercase">Gender</div>
                                        <div className="font-medium">{profile.gender || 'Not specified'}</div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* Additional Stats */}
                        {user.role === 'CUSTOMER' && profile.penaltyamount > 0 && (
                            <div className="bg-red-50 p-4 rounded-3xl border border-red-100 flex items-center justify-between">
                                <span className="font-bold text-red-900">Penalty Due</span>
                                <span className="font-bold text-red-600 text-xl">₹{profile.penaltyamount}</span>
                            </div>
                        )}

                        {/* Driver Specifics */}
                        {user.role === 'DRIVER' && (
                            <div className="bg-white p-6 rounded-3xl shadow-sm border border-gray-100 space-y-6">
                                <h3 className="font-bold text-gray-900 text-lg">Vehicle & Location</h3>

                                <div className="p-4 bg-blue-50 text-blue-900 rounded-2xl flex items-start space-x-3">
                                    <MapPin className="text-blue-600 mt-1" size={20} />
                                    <div>
                                        <div className="font-bold">Current City</div>
                                        <div className="text-sm opacity-80 mb-3">{profile.vehicle?.currentcity || 'Sync GPS to see location'}</div>
                                        <Button onClick={handleUpdateLocation} isLoading={isUpdating} className="bg-blue-600 hover:bg-blue-700 border-none">
                                            Update to Current GPS
                                        </Button>
                                    </div>
                                </div>

                                <div className="grid grid-cols-2 gap-4">
                                    <div className="p-4 bg-gray-50 rounded-2xl">
                                        <div className="text-xs text-gray-400 font-bold uppercase">Vehicle</div>
                                        <div className="font-bold">{profile.vehicle?.vname || 'N/A'}</div>
                                        <div className="text-xs text-gray-500">{profile.vehicle?.model} • {profile.vehicle?.vehicleno}</div>
                                    </div>
                                    <div className="p-4 bg-gray-50 rounded-2xl">
                                        <div className="text-xs text-gray-400 font-bold uppercase">Status</div>
                                        <div className="font-bold text-green-600">{profile.status}</div>
                                    </div>
                                    <div className="p-4 bg-gray-50 rounded-2xl col-span-2 flex justify-between items-center">
                                        <div>
                                            <div className="text-xs text-gray-400 font-bold uppercase">Avg Speed</div>
                                            <div className="font-bold">{profile.vehicle?.averagespeed ? `${profile.vehicle.averagespeed} km/h` : 'N/A'}</div>
                                        </div>
                                        <div>
                                            <div className="text-xs text-gray-400 font-bold uppercase">Price/KM</div>
                                            <div className="font-bold">₹{profile.vehicle?.priceperkm || 0}</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}

                        {/* Danger Zone */}
                        <div className="bg-red-50 p-6 rounded-3xl border border-red-100 space-y-4">
                            <h3 className="font-bold text-red-900 text-lg">Danger Zone</h3>
                            <p className="text-sm text-red-600/80">Once you delete your account, there is no going back. Please be certain.</p>
                            <Button
                                variant="outline"
                                className="w-full bg-white text-red-600 border-red-200 hover:bg-red-100 hover:border-red-300 transition-colors"
                                onClick={async () => {
                                    if (confirm("CRITICAL WARNING: Are you sure you want to delete your account? This action cannot be undone.")) {
                                        if (confirm("Please confirm one last time to delete your account.")) {
                                            if (!user) return;
                                            setIsLoading(true);
                                            try {
                                                if (user.role === 'CUSTOMER') {
                                                    await customerService.deleteCustomer(parseInt(user.mobile));
                                                } else {
                                                    await driverService.deleteDriver(parseInt(user.mobile));
                                                }
                                                alert("Account deleted successfully.");
                                                logout();
                                                navigate('/login'); // Redirect to login, not register
                                            } catch (e) {
                                                console.error(e);
                                                alert("Failed to delete account. Please try again.");
                                                setIsLoading(false);
                                            }
                                        }
                                    }
                                }}
                            >
                                Delete Account
                            </Button>
                        </div>

                        <Button variant="outline" className="w-full h-14 text-gray-600 border-gray-200 hover:bg-gray-50" onClick={() => {
                            logout();
                            navigate('/login');
                        }}>
                            Log Out
                        </Button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default ProfilePage;
