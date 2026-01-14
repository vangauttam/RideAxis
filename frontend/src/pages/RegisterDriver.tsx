import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Button } from '../components/Button';
import { Input } from '../components/Input';
import { useAuth } from '../context/AuthContext';

const RegisterDriver = () => {
    const { registerDriver } = useAuth();
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(false);
    // Huge form
    const [formData, setFormData] = useState({
        dname: '',
        age: '',
        gender: 'MALE',
        mobileno: '',
        mailid: '',
        licenceno: '',
        upiid: '',
        vname: '',
        vehicleno: '',
        type: 'SEDAN',
        model: '',
        capacity: '4',
        priceperkm: '15',
        averagespeed: '40',
        latitude: 12.9716,
        longitude: 77.5946,
        password: ''
    });
    const [locationStatus, setLocationStatus] = useState<string>('Detecting location...');

    // Fetch location on mount
    React.useEffect(() => {
        if ("geolocation" in navigator) {
            navigator.geolocation.getCurrentPosition((position) => {
                setFormData(prev => ({
                    ...prev,
                    latitude: position.coords.latitude,
                    longitude: position.coords.longitude
                }));
                setLocationStatus('Location detected ✅');
            }, async (_error) => {
                console.warn("Location error (HTTP), trying IP fallback...");
                try {
                    const res = await fetch('https://ipapi.co/json/');
                    const data = await res.json();
                    if (data.latitude && data.longitude) {
                        setFormData(prev => ({
                            ...prev,
                            latitude: data.latitude,
                            longitude: data.longitude
                        }));
                        setLocationStatus('Location detected (via IP) ✅');
                    }
                } catch (e) {
                    console.error("IP fallback failed", e);
                    setLocationStatus('Location auto-detect failed. Please enter manually.');
                }
            });
        } else {
            setLocationStatus('Geolocation not supported');
        }
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        try {
            await registerDriver({
                ...formData,
                age: parseInt(formData.age),
                mobileno: parseInt(formData.mobileno),
                capacity: parseInt(formData.capacity),
                priceperkm: parseFloat(formData.priceperkm),
                averagespeed: parseFloat(formData.averagespeed)
            });
            navigate('/login');
        } catch (error: any) {
            console.error("Registration Error:", error);
            let msg = 'Registration failed';
            if (error.response?.status === 500) {
                msg = 'Registration failed. This mobile number might already be registered.';
            } else if (error.response?.data?.message) {
                msg = error.response.data.message;
            }
            alert(msg);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-4 py-8">
            <div className="w-full max-w-2xl bg-white rounded-xl shadow-lg p-8 space-y-6">
                <h1 className="text-2xl font-bold text-center">Register as Driver</h1>
                <form onSubmit={handleSubmit} className="space-y-4 grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div className="md:col-span-2 text-lg font-semibold border-b pb-2">Personal Details</div>
                    <Input name="dname" label="Name" value={formData.dname} onChange={handleChange} required />
                    <Input name="age" label="Age" type="number" value={formData.age} onChange={handleChange} required />
                    <Input name="mobileno" label="Mobile" type="tel" value={formData.mobileno} onChange={handleChange} required />
                    <Input name="mailid" label="Email" type="email" value={formData.mailid} onChange={handleChange} required />
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Gender</label>
                        <select name="gender" value={formData.gender} onChange={handleChange} className="flex h-10 w-full rounded-md border border-gray-300 bg-white px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
                            <option value="MALE">Male</option>
                            <option value="FEMALE">Female</option>
                        </select>
                    </div>
                    <Input name="licenceno" label="License No" value={formData.licenceno} onChange={handleChange} required />
                    <Input name="upiid" label="UPI ID" value={formData.upiid} onChange={handleChange} required />

                    <div className="md:col-span-2 text-lg font-semibold border-b pb-2 pt-4">Vehicle Details</div>
                    <Input name="vname" label="Vehicle Name" value={formData.vname} onChange={handleChange} required />
                    <Input name="vehicleno" label="Vehicle No" value={formData.vehicleno} onChange={handleChange} required />
                    <Input name="model" label="Model" value={formData.model} onChange={handleChange} required />
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Type</label>
                        <select name="type" value={formData.type} onChange={handleChange} className="flex h-10 w-full rounded-md border border-gray-300 bg-white px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
                            <option value="AUTO">Auto</option>
                            <option value="BIKE">Bike</option>
                            <option value="SEDAN">Sedan</option>
                            <option value="HATCHBACK">Hatchback</option>
                            <option value="SUV">SUV</option>
                            <option value="PREMIUM">Premium</option>
                            <option value="ELECTRIC">Electric</option>
                            <option value="OTHER">Other</option>
                        </select>
                    </div>
                    <Input name="capacity" label="Capacity" type="number" value={formData.capacity} onChange={handleChange} required />
                    <Input name="priceperkm" label="Price/KM" type="number" value={formData.priceperkm} onChange={handleChange} required />

                    <div className="md:col-span-2 text-lg font-semibold border-b pb-2 pt-4">Security</div>
                    <Input name="password" label="Password" type="password" value={formData.password} onChange={handleChange} required />

                    <div className="md:col-span-2 pt-4">
                        <Button type="submit" className="w-full" isLoading={isLoading}>Register Driver</Button>
                    </div>
                </form>
                <div className="text-center text-sm">
                    Already have an account? <Link to="/login" className="text-blue-600 hover:underline">Login</Link>
                </div>
                <div className="text-center text-xs text-gray-500 mt-2">
                    {locationStatus}
                </div>
            </div>
        </div>
    );
};

export default RegisterDriver;
