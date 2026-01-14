import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Loader2, ArrowRight, Eye, EyeOff } from 'lucide-react';
import loginBg from '../assets/login-bg.png';

const Login = () => {
    const [mobile, setMobile] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [error, setError] = useState('');
    const { login } = useAuth();
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setError('');
        try {
            await login({ mobileNo: parseInt(mobile), password });
            navigate('/dashboard');
        } catch (err: any) {
            console.error("Login Error:", err);
            let msg = err.response?.data?.message || err.message || "Failed to login";
            if (err.response?.status === 403 || err.response?.status === 401) {
                msg = "Invalid Mobile or Password. Please register if you don't have an account.";
            } else if (err.response?.status === 500) {
                msg = "Server Error. Check your connection or credentials.";
            }
            setError(msg);
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
                    <h2 className="text-5xl font-bold leading-tight animate-slide-up">
                        Move with <br /> <span className="text-blue-400">RideAxis.</span>
                    </h2>
                    <p className="text-lg text-gray-200 animate-slide-up" style={{ animationDelay: '0.1s' }}>
                        Experience the next generation of ride-hailing. Seamless, safe, and sophisticated.
                    </p>
                </div>
            </div>

            {/* Right Side - Login Form */}
            <div className="w-full lg:w-1/2 bg-white flex flex-col justify-center items-center p-8 lg:p-16 relative">
                <div className="w-full max-w-md space-y-8 animate-slide-up" style={{ animationDelay: '0.2s' }}>

                    {/* Header */}
                    <div className="text-center space-y-2">
                        <div className="mx-auto w-24 h-24 rounded-3xl bg-black border-4 border-white shadow-2xl overflow-hidden flex items-center justify-center transform hover:scale-105 transition-transform duration-300">
                            <img src="/logo.png" alt="RideAxis" className="w-full h-full object-cover scale-110" />
                        </div>
                        <h1 className="text-4xl font-bold tracking-tight text-gray-900 mt-4">Welcome Back</h1>
                        <p className="text-gray-500 text-lg">Enter your credentials to access your account</p>
                    </div>

                    <form onSubmit={handleSubmit} className="space-y-6 mt-8">
                        <div className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Mobile Number</label>
                                <input
                                    type="tel"
                                    className="input-premium"
                                    placeholder="9876543210"
                                    value={mobile}
                                    onChange={(e) => setMobile(e.target.value)}
                                    required
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
                                <div className="relative">
                                    <input
                                        type={showPassword ? "text" : "password"}
                                        className="input-premium pr-10"
                                        placeholder="••••••••"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
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
                        </div>

                        {error && (
                            <div className="p-4 bg-red-50 border border-red-100 text-red-600 rounded-xl text-sm text-center">
                                {error}
                            </div>
                        )}

                        <button
                            type="submit"
                            className="btn-primary flex items-center justify-center group"
                            disabled={isLoading}
                        >
                            {isLoading ? <Loader2 className="w-5 h-5 animate-spin" /> : (
                                <>
                                    Sign In <ArrowRight className="w-5 h-5 ml-2 group-hover:translate-x-1 transition-transform" />
                                </>
                            )}
                        </button>
                    </form>

                    <div className="relative my-8">
                        <div className="absolute inset-0 flex items-center">
                            <div className="w-full border-t border-gray-200"></div>
                        </div>
                        <div className="relative flex justify-center text-sm">
                            <span className="px-4 bg-white text-gray-500 font-medium tracking-wide">NEW TO RIDEAXIS?</span>
                        </div>
                    </div>

                    <div className="grid grid-cols-2 gap-4">
                        <Link to="/register/customer">
                            <button className="btn-secondary text-sm">Create Customer Account</button>
                        </Link>
                        <Link to="/register/driver">
                            <button className="btn-secondary text-sm">Become a Driver</button>
                        </Link>
                    </div>
                </div>

                <div className="absolute bottom-6 text-center text-xs text-gray-400">
                    &copy; 2024 RideAxis Technologies Inc.
                </div>
            </div>
        </div>
    );
};

export default Login;
