import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
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

function App() {
  return (
    <Router>
      <div className="App">
        <Header />
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
        </Routes>
      </div>
    </Router>
  );
}

export default App;
