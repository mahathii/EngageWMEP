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

function App() {
  return (
    <Router>
      <div className="App">
        <Header />
        <Routes>
          <Route path="/" element={<AlumniSearch />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/signup" element={<LoginSignup />} />
          <Route path="/students" element={<StudentsPage />} />
          <Route path="/events" element={<EventsPage />} />
          <Route path="/alumni" element={<AlumniPage />} />
          <Route path="/alumni/:id" element={<AlumniDetail />} />
          <Route path="/alumni-form" element={<AlumniForm />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
