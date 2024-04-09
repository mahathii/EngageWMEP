import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import "./App.css";
import Header from "./components/Header";
import LoginSignup from "./components/LoginSignup";
import Dashboard from "./components/Dashboard";
import StudentsPage from "./components/StudentsPage";
import EventsPage from "./components/EventsPage";

function App() {
	return (
		<Router>
			<div className="App">
				<Header />
				<Routes>
					{" "}
					<Route path="/dashboard" element={<Dashboard />} />
					<Route path="/signup" element={<LoginSignup />} />
					<Route path="/students" element={<StudentsPage />} />
					<Route path="/events" element={<EventsPage />} />
				</Routes>
			</div>
		</Router>
	);
}

export default App;
