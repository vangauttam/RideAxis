import { useNavigate } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';

interface HeaderProps {
    title: string;
    showBack?: boolean;
    onBack?: () => void;
    className?: string;
}

export const Header = ({ title, showBack = true, onBack, className = '' }: HeaderProps) => {
    const navigate = useNavigate();

    const handleBack = () => {
        if (onBack) {
            onBack();
        } else {
            navigate(-1);
        }
    };

    return (
        <div className={`flex items-center space-x-4 mb-6 ${className}`}>
            {showBack && (
                <button
                    onClick={handleBack}
                    className="p-2 hover:bg-gray-100 rounded-full transition-colors focus:outline-none focus:ring-2 focus:ring-gray-200"
                    aria-label="Go back"
                >
                    <ArrowLeft className="w-6 h-6 text-gray-700" />
                </button>
            )}
            <h1 className="text-3xl font-bold tracking-tight text-gray-900">{title}</h1>
        </div>
    );
};
