import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import "./App.css";
import Header from "./components/Header";
import LoginSignup from "./components/LoginSignup";
import Dashboard from "./components/Dashboard";

function App() {
	return (
		<Router>
			<div className="App">
				<Header />
				<Routes>
					{" "}
					{/* Use Routes to wrap Route components */}
					<Route path="/dashboard" element={<Dashboard />} />
					<Route path="/signup" element={<LoginSignup />} />
					{/* You can add more Route components here */}
				</Routes>
			</div>
		</Router>
	);
}

export default App;
