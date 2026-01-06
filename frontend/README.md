# RideAxis Frontend

This is the React-based frontend for the RideAxis ride-booking platform (an Uber clone). It is built with Vite, React, and Tailwind CSS.

## 🚀 Getting Started

### Prerequisites
*   Node.js (v18 or higher recommended)
*   npm or yarn
*   The **RideAxis Backend** must be running on `http://localhost:8080`.

### Installation

1.  Navigate to the `frontend` folder:
    ```bash
    cd frontend
    ```
2.  Install dependencies:
    ```bash
    npm install
    ```

### Running the App

Start the development server:

```bash
npm run dev
```

Open [http://localhost:5173](http://localhost:5173) in your browser.

## 📱 Features

### **Customer App** (`/dashboard`)
*   **Smart Location:** Auto-detects location via GPS or **IP Fallback** (for mobile web).
*   **Ride Search:** Real-time vehicle availability.
*   **Premium Selection:** High-contrast "Black Theme" UI for clear vehicle selection.
*   **Live Tracking:** Visual progress bar and status updates.
*   **Completion:** "You've Arrived" summary modal with **Interactive Star Rating**.

### **Driver App** (`/driver-dashboard`)
*   **Ride Requests:** Polls for new bookings automatically.
*   **Start Ride:** Secure OTP verification.
*   **Payments:**
    *   **Cash:** Record cash payments.
    *   **UPI:** Generates dynamic **UPI QR Codes** for scanning.
    *   **Success Modal:** "You Earned" screen with earnings summary.
*   **Status:** Auto-updates to "Available" after payment.

## 🛠️ Tech Stack
*   **Framework:** React (Vite)
*   **Styling:** Tailwind CSS + Lucide Icons
*   **State Management:** React Context API (`AuthContext`)
*   **Maps:** Leaflet (via `react-leaflet`) with OpenStreetMap
*   **API Client:** Axios

## ⚠️ Troubleshooting

1.  **"No vehicles found":**
    *   Ensure a driver is registered and "Online".
    *   Make sure the driver's registered city matches the customer's detected city.
    *   Ensure both endpoints (LocationIQ) return the same city string (e.g. "Bengaluru").

2.  **"Invalid ID":**
    *   If booking fails, restart the backend to ensure latest code changes are active.

3.  **Map not loading:**
    *   Check your internet connection (Leaflet tiles require internet).
