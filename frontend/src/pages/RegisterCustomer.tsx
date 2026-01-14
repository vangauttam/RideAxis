import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Loader2, ArrowRight, User, Eye, EyeOff } from 'lucide-react';
import loginBg from '../assets/login-bg.png';

const RegisterCustomer = () => {
    const { registerCustomer } = useAuth();
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(false);
    const [formData, setFormData] = useState({
        name: '',
        age: '',
        gender: 'MALE',
        mobileno: '',
        email: '',
        password: '',
        latitude: '12.9716', // Default for now
        longitude: '77.5946'
    });

    const [locationStatus, setLocationStatus] = useState<string>('Detecting location...');
    const [showPassword, setShowPassword] = useState(false);

    // Fetch location on mount
    React.useEffect(() => {
        if ("geolocation" in navigator) {
            navigator.geolocation.getCurrentPosition((position) => {
                setFormData(prev => ({
                    ...prev,
                    latitude: position.coords.latitude.toString(),
                    longitude: position.coords.longitude.toString()
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
                            latitude: data.latitude.toString(),
                            longitude: data.longitude.toString()
                        }));
                        setLocationStatus('Location detected (via IP) ✅');
                    }
                } catch (e) {
                    console.error("IP fallback failed", e);
                    setLocationStatus('Location auto-detect failed. Using default.');
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
            await registerCustomer({
                ...formData,
                age: parseInt(formData.age),
                mobileno: parseInt(formData.mobileno)
            });
            navigate('/login');
        } catch (error: any) {
            console.error("Registration Error:", error);
            let msg = error.response?.data?.message || 'Registration failed';
            if (error.response?.status === 500) {
                msg = 'Registration failed. Mobile number might already be registered.';
            }
            alert(msg);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="min-h-screen flex animate-fade-in">
            {/* Left Side - Hero Image */}
            <div className="hidden lg:flex lg:w-1/2 relative bg-black items-center justify-center overflow-hidden">
                <div
                    className="absolute inset-0 bg-cover bg-center transition-transform duration-1000 hover:scale-105"
                    style={{ backgroundImage: `url(${loginBg})` }}
                />
                <div className="absolute inset-0 bg-black/40 backdrop-blur-[2px]" />
                <div className="relative z-10 p-12 text-white space-y-6 max-w-lg">
                    <h2 className="text-4xl font-bold leading-tight animate-slide-up">
                        Join the <br /> <span className="text-blue-400">Revolution.</span>
                    </h2>
                    <p className="text-lg text-gray-200 animate-slide-up" style={{ animationDelay: '0.1s' }}>
                        Create your account to start riding with the best fleet in town.
                    </p>
                </div>
            </div>

            {/* Right Side - Register Form */}
            <div className="w-full lg:w-1/2 bg-white flex flex-col justify-center items-center p-8 lg:p-12 relative overflow-y-auto">
                <div className="w-full max-w-md space-y-8 animate-slide-up" style={{ animationDelay: '0.2s' }}>

                    <div className="text-center space-y-2">
                        <div className="inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-gray-100 text-black mb-4 shadow-sm">
                            <User className="w-8 h-8" />
                        </div>
                        <h1 className="text-3xl font-bold tracking-tight text-gray-900">Create Account</h1>
                        <p className="text-gray-500">Sign up as a Customer</p>
                    </div>

                    <form onSubmit={handleSubmit} className="space-y-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Full Name</label>
                            <input name="name" className="input-premium" placeholder="John Doe" value={formData.name} onChange={handleChange} required />
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                            <input name="email" type="email" className="input-premium" placeholder="john@example.com" value={formData.email} onChange={handleChange} required />
                        </div>

                        <div className="grid grid-cols-2 gap-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Mobile</label>
                                <input name="mobileno" type="tel" className="input-premium" placeholder="9876543210" value={formData.mobileno} onChange={handleChange} required />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Age</label>
                                <input name="age" type="number" className="input-premium" placeholder="25" value={formData.age} onChange={handleChange} required />
                            </div>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Gender</label>
                            <select name="gender" value={formData.gender} onChange={handleChange} className="input-premium">
                                <option value="MALE">Male</option>
                                <option value="FEMALE">Female</option>
                                <option value="OTHER">Other</option>
                            </select>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
                            <div className="relative">
                                <input
                                    name="password"
                                    type={showPassword ? "text" : "password"}
                                    className="input-premium pr-10"
                                    placeholder="••••••••"
                                    value={formData.password}
                                    onChange={handleChange}
                                    required
                                />
                                <button
                                    type="button"
                                    onClick={() => setShowPassword(!showPassword)}
                                    className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500 hover:text-gray-700 focus:outline-none"
                                >
                                    {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                                </button>
                            </div>
                        </div>

                        <button
                            type="submit"
                            className="btn-primary flex items-center justify-center group mt-6"
                            disabled={isLoading}
                        >
                            {isLoading ? <Loader2 className="w-5 h-5 animate-spin" /> : (
                                <>
                                    Register <ArrowRight className="w-5 h-5 ml-2 group-hover:translate-x-1 transition-transform" />
                                </>
                            )}
                        </button>
                    </form>

                    <div className="text-center text-sm text-gray-500">
                        Already have an account? <Link to="/login" className="text-blue-600 font-semibold hover:underline">Login</Link>
                    </div>

                    <div className="text-center text-xs text-gray-400 mt-2">
                        {locationStatus}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default RegisterCustomer;
