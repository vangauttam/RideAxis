import { createContext, useContext, useState, useEffect, type ReactNode } from 'react';
import { jwtDecode } from 'jwt-decode';
import { authService } from '../services/authService';
import type { LoginRequestDTO, RegCustomerDto, RegDriverVehicleDTO } from '../types';

interface AuthState {
    user: {
        mobile: string;
        role: 'CUSTOMER' | 'DRIVER';
    } | null;
    token: string | null;
    isAuthenticated: boolean;
    isLoading: boolean;
}

interface AuthContextType extends AuthState {
    login: (credentials: LoginRequestDTO) => Promise<void>;
    registerCustomer: (data: RegCustomerDto) => Promise<void>;
    registerDriver: (data: RegDriverVehicleDTO) => Promise<void>;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [state, setState] = useState<AuthState>({
        user: null,
        token: null,
        isAuthenticated: false,
        isLoading: true,
    });

    useEffect(() => {
        const token = sessionStorage.getItem('token');
        if (token) {
            try {
                // Backend returns "Bearer <token>", handled by login.
                // We strip "Bearer " for decoding.
                const strictToken = token.startsWith('Bearer ') ? token.split(' ')[1] : token;
                const decoded: any = jwtDecode(strictToken);

                // Check for expiration (exp is in seconds)
                const currentTime = Date.now() / 1000;
                if (decoded.exp && decoded.exp < currentTime) {
                    console.warn("Session expired. Logging out.");
                    sessionStorage.removeItem('token');
                    setState(prev => ({ ...prev, isLoading: false }));
                    return;
                }

                setState({
                    user: {
                        mobile: decoded.sub,
                        role: decoded.role,
                    },
                    token: token,
                    isAuthenticated: true,
                    isLoading: false,
                });
            } catch (e) {
                console.error("Invalid token", e);
                sessionStorage.removeItem('token');
                setState(prev => ({ ...prev, isLoading: false }));
            }
        } else {
            setState(prev => ({ ...prev, isLoading: false }));
        }
    }, []);

    const login = async (credentials: LoginRequestDTO) => {
        const response = await authService.login(credentials);
        // response is ResponseStructure<string>
        const token = response.data; // "Bearer <token>"

        if (!token) throw new Error("No token received");

        sessionStorage.setItem('token', token);
        const strictToken = token.startsWith('Bearer ') ? token.split(' ')[1] : token;
        const decoded: any = jwtDecode(strictToken);

        setState({
            user: {
                mobile: decoded.sub,
                role: decoded.role,
            },
            token: token,
            isAuthenticated: true,
            isLoading: false,
        });
    };

    const registerCustomer = async (dto: RegCustomerDto) => {
        await authService.registerCustomer(dto);
    };

    const registerDriver = async (dto: RegDriverVehicleDTO) => {
        await authService.registerDriver(dto);
    };

    const logout = () => {
        sessionStorage.removeItem('token');
        setState({
            user: null,
            token: null,
            isAuthenticated: false,
            isLoading: false,
        });
    };

    return (
        <AuthContext.Provider value={{ ...state, login, registerCustomer, registerDriver, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};
