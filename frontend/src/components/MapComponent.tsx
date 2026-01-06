import React, { useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import L from 'leaflet';
import { Car, MapPin } from 'lucide-react';
import { renderToStaticMarkup } from 'react-dom/server';

// Fix for default markers
const createIcon = (icon: React.ReactNode, color: string) => {
    const iconMarkup = renderToStaticMarkup(
        <div className={`text-${color}-500 w-8 h-8 flex items-center justify-center bg-white rounded-full shadow-lg border-2 border-${color}-500`}>
            {icon}
        </div>
    );
    return L.divIcon({
        html: iconMarkup,
        className: 'custom-marker',
        iconSize: [32, 32],
        iconAnchor: [16, 32],
    });
};

const userIcon = createIcon(<MapPin size={20} fill="currentColor" />, 'blue');
const vehicleIcon = createIcon(<Car size={20} />, 'black');

// HOC to update map center
const ChangeView = ({ center }: { center: [number, number] }) => {
    const map = useMap();
    useEffect(() => {
        map.setView(center);
    }, [center, map]);
    return null;
};

interface MapComponentProps {
    center: [number, number];
    vehicles?: { id: number; lat: number; lng: number; type: string }[];
    destination?: [number, number] | null;
}

const MapComponent: React.FC<MapComponentProps> = ({ center, vehicles = [], destination }) => {
    return (
        <MapContainer center={center} zoom={13} style={{ height: '100%', width: '100%' }} className="z-0">
            <ChangeView center={center} />
            <TileLayer
                url="https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png"
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>'
            />

            {/* User Location */}
            <Marker position={center} icon={userIcon}>
                <Popup>You are here</Popup>
            </Marker>

            {/* Destination */}
            {destination && (
                <Marker position={destination} icon={userIcon}>
                    <Popup>Destination</Popup>
                </Marker>
            )}

            {/* Vehicles */}
            {vehicles.map(v => (
                <Marker key={v.id} position={[v.lat, v.lng]} icon={vehicleIcon}>
                    <Popup>{v.type}</Popup>
                </Marker>
            ))}
        </MapContainer>
    );
};

export default MapComponent;
