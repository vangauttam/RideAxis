import { useNavigate } from 'react-router-dom';
import { Home } from 'lucide-react';

const NotFound = () => {
    const navigate = useNavigate();

    return (
        <div className="min-h-screen flex flex-col items-center justify-center p-4 bg-gray-50 text-center">
            <h1 className="text-9xl font-extrabold text-gray-200">404</h1>
            <h2 className="text-3xl font-bold text-gray-900 mt-4">Page Not Found</h2>
            <p className="text-gray-500 mt-2 max-w-md">
                The page you are looking for doesn't exist or has been moved.
            </p>
            <button
                onClick={() => navigate('/dashboard')}
                className="mt-8 flex items-center space-x-2 bg-black text-white px-6 py-3 rounded-full hover:bg-gray-800 transition-all font-medium"
            >
                <Home size={20} />
                <span>Back to Home</span>
            </button>
        </div>
    );
};

export default NotFound;
