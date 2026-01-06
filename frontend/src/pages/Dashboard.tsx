import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import CustomerHome from './CustomerHome';
import DriverHome from './DriverHome';
import { History, User, LogOut } from 'lucide-react';

const Dashboard = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    if (!user) return <div className="min-h-screen flex items-center justify-center"><div className="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900"></div></div>;

    return (
        <div className="h-[100dvh] flex flex-col relative w-full overflow-hidden">
            {/* Logo Watermark */}
            <div className="absolute top-4 left-4 md:top-6 md:left-6 z-[9999]">
                <div className="w-10 h-10 md:w-14 md:h-14 rounded-2xl bg-black border-2 border-white shadow-xl overflow-hidden pointer-events-none">
                    <img src="/src/assets/logo.png" alt="RideAxis" className="w-full h-full object-cover scale-110" />
                </div>
            </div>

            {/* Responsive Header for Logout & History */}
            <div className="absolute top-4 right-4 md:top-6 md:right-6 z-[9999] flex space-x-2 md:space-x-3">
                <button
                    onClick={() => navigate('/history')}
                    className="bg-white/90 backdrop-blur-md p-2.5 md:px-6 md:py-2.5 rounded-full shadow-lg font-bold text-sm text-gray-800 hover:bg-gray-100 hover:text-black transition-all border border-gray-200 flex items-center justify-center"
                    title="History"
                >
                    <History size={20} className="md:mr-2" />
                    <span className="hidden md:inline">History</span>
                </button>
                <button
                    onClick={() => navigate('/profile')}
                    className="bg-white/90 backdrop-blur-md p-2.5 md:px-6 md:py-2.5 rounded-full shadow-lg font-bold text-sm text-gray-800 hover:bg-gray-100 hover:text-black transition-all border border-gray-200 flex items-center justify-center"
                    title="Profile"
                >
                    <User size={20} className="md:mr-2" />
                    <span className="hidden md:inline">Profile</span>
                </button>
                <button
                    onClick={handleLogout}
                    className="bg-white/90 backdrop-blur-md p-2.5 md:px-6 md:py-2.5 rounded-full shadow-lg font-bold text-sm text-red-600 hover:bg-red-50 hover:text-red-700 transition-all border border-gray-200 flex items-center justify-center"
                    title="Logout"
                >
                    <LogOut size={20} className="md:mr-2" />
                    <span className="hidden md:inline">Logout</span>
                </button>
            </div>

            {user.role === 'CUSTOMER' ? <CustomerHome /> : <DriverHome />}
        </div>
    );
};

export default Dashboard;
