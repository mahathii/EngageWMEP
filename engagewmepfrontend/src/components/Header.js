import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./Header.css"; // Make sure to create a corresponding CSS file

const Header = () => {
	const navigate = useNavigate(); // Hook for navigation
	const [isLoggedIn, setIsLoggedIn] = useState(false);

	useEffect(() => {
		// Check if the login flag is set in local storage
		const loggedIn = localStorage.getItem("isLoggedIn") === "true";
		setIsLoggedIn(loggedIn);
	}, []);

	const handleLogout = () => {
		// Clear the login flag from local storage
		localStorage.removeItem("isLoggedIn");
		setIsLoggedIn(false);
		navigate("/signup");
	};

	// Function to handle navigation
	const handleNavigation = () => {
		navigate("/signup");
	};
	return (
		<header className="header">
			<h1>EngageWMEP</h1>

			{!isLoggedIn ? (
				<button onClick={handleNavigation} className="btn">
					Log In / Sign Up
				</button>
			) : (
				<button onClick={handleLogout} className="btn">
					Log Out
				</button>
			)}
		</header>
	);
};

export default Header;
