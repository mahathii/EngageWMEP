import { BrowserRouter as Router, Routes, Route, useNavigate } from "react-router-dom";
import React, { useEffect, useRef, useCallback } from "react";
import "./App.css";
import Header from "./components/Header";
import LoginSignup from "./components/LoginSignup";
import Dashboard from "./components/Dashboard";
import StudentsPage from "./components/StudentsPage";
import EventsPage from "./components/EventsPage";
import AlumniPage from "./components/AlumniPage";
import AlumniDetail from "./components/AlumniDetail";
import AlumniSearch from "./components/AlumniSearch";
import AlumniForm from "./components/AlumniForm";
import PrivateRoute from "./PrivateRoute";
import ViewStudentsByDate from "./components/ViewStudentsByDate";
import ViewStudentsByEvent from "./components/ViewStudentsByEvent";
import ManageEvents from "./components/ManageEvents";
import StudentsEvents from "./components/StudentsEvents";
import EventAttendancePage from "./components/EventAttendancePage";
import VerifyEmail from "./components/VerifyEmail";

const SessionTimeout = ({ timeout = 300000 }) => { // 5 minutes (300,000 ms) timeout
  const navigate = useNavigate();
  const logoutTimerRef = useRef(null); // useRef to keep the timer value consistent across renders

  
  const handleLogout = useCallback(() => {
    
    localStorage.removeItem("isLoggedIn"); // Adjust this based on how you handle auth
    navigate("/signup"); // Redirect to login page
  }, [navigate]);

  // Reset the timer
  const resetTimer = useCallback(() => {
    if (logoutTimerRef.current) clearTimeout(logoutTimerRef.current);
    logoutTimerRef.current = setTimeout(() => {
      handleLogout();
    }, timeout);
  }, [handleLogout, timeout]);

  useEffect(() => {
    // Listen for user activity
    window.addEventListener("mousemove", resetTimer);
    window.addEventListener("keydown", resetTimer);

    // Start the timeout timer
    resetTimer();

    // Clean up event listeners on component unmount
    return () => {
      window.removeEventListener("mousemove", resetTimer);
      window.removeEventListener("keydown", resetTimer);
      clearTimeout(logoutTimerRef.current); // Clean up the timer
    };
  }, [resetTimer]);

  return null; // This component does not need to render anything
};

function App() {
  useEffect(() => {
    // When the app loads, store a flag in sessionStorage to indicate the page is active
    sessionStorage.setItem('isRefreshed', 'true');

    const handleBeforeUnload = (event) => {
      // Check if sessionStorage still has the 'isRefreshed' flag
      const isRefreshed = sessionStorage.getItem('isRefreshed');

      // If the flag is missing, it means the user is closing the tab or window
      if (!isRefreshed) {
        localStorage.clear(); // Clear localStorage only on tab/window close
      }
    };

    // Add the event listener for `beforeunload`
    window.addEventListener('beforeunload', handleBeforeUnload);

    // Clean up the event listener when the component unmounts
    return () => {
      window.removeEventListener('beforeunload', handleBeforeUnload);
    };
  }, []);

  return (
    <Router>
      <div className="App">
        <Header />
        <SessionTimeout /> {/* Add session timeout to monitor inactivity */}
        <Routes>
          <Route path="/" element={<AlumniSearch />} />
          <Route path="/dashboard" element={<PrivateRoute />}>
            <Route path="" element={<Dashboard />} />
          </Route>
          <Route path="/signup" element={<LoginSignup />} />
          <Route path="/students" element={<PrivateRoute />}>
            <Route path="" element={<StudentsPage />} />
          </Route>
          <Route path="/events" element={<PrivateRoute />}>
            <Route path="" element={<EventsPage />} />
          </Route>
          <Route path="/alumni" element={<PrivateRoute />}>
            <Route path="" element={<AlumniPage />} />
          </Route>
          <Route path="/alumni/:id" element={<PrivateRoute />}>
            <Route path="" element={<AlumniDetail />} />
          </Route>
          <Route path="/alumni-form" element={<AlumniForm />} />
          <Route path="/verify" element={<VerifyEmail />} />
          <Route path="/view-by-date" element={<PrivateRoute />}>
            <Route path="" element={<ViewStudentsByDate />} />
          </Route>
          <Route path="/view-by-event" element={<PrivateRoute />}>
            <Route path="" element={<ViewStudentsByEvent />} />
          </Route>
          <Route path="/manage-events" element={<PrivateRoute />}>
            <Route path="" element={<ManageEvents />} />
          </Route>
          <Route path="/student-events" element={<PrivateRoute />}>
            <Route path="" element={<StudentsEvents />} />
          </Route>
          <Route path="/event-attendance" element={<PrivateRoute />}>
            <Route path="" element={<EventAttendancePage />} />
          </Route>
        </Routes>
      </div>
    </Router>
  );
}

export default App;
