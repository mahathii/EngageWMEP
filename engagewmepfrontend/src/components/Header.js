import React from "react";
import { useNavigate } from "react-router-dom";
import "./Header.css"; // Make sure to create a corresponding CSS file

const Header = () => {
	const navigate = useNavigate(); // Hook for navigation

	// Function to handle navigation
	const handleNavigation = () => {
		navigate("/signup");
	};
	return (
		<header className="header">
			<h1>EngageWMEP</h1>
			<button onClick={handleNavigation} className="btn">
				Log In / Sign Up
			</button>
		</header>
	);
};

export default Header;
