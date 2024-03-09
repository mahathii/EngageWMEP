import React, { useState } from "react";
import "./LoginSignup.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const LoginSignup = () => {
	const [isActive, setIsActive] = useState(false);
	const navigate = useNavigate();

	const [registerData, setRegisterData] = useState({
		username: "",
		email: "", // If you're using email as username, make sure to adjust accordingly in your User model and everywhere else.
		password: "",
		confirmPassword: "",
	});

	const [loginData, setLoginData] = useState({
		username: "",
		password: "",
	});

	const handleRegisterClick = () => {
		setIsActive(true);
	};

	const handleLoginClick = () => {
		setIsActive(false);
	};

	const handleRegisterSubmit = async (e) => {
		e.preventDefault();
		try {
			const response = await axios.post("/api/auth/register", registerData);
			console.log(registerData);
			if (response.data) navigate("/dashboard");
		} catch (error) {
			console.error("Registration error:", error);
		}
	};

	const handleLoginSubmit = async (e) => {
		e.preventDefault();
		try {
			const response = await axios.post("/api/auth/login", loginData);
			if (response.data) {
				localStorage.setItem("token", response.data.jwt);
				navigate("/dashboard");
			}
		} catch (error) {
			console.error("Login error:", error);
		}
	};

	const handleInputChange = (e) => {
		const { name, value } = e.target;
		setRegisterData({
			...registerData,
			[name]: value,
		});
	};

	// Apply the 'active' class based on the state
	const containerClasses = isActive ? "container active" : "container";

	return (
		<div className={containerClasses} id="container">
			<div
				className={
					isActive ? "form-container sign-up active" : "form-container sign-up"
				}
			>
				<form onSubmit={handleRegisterSubmit}>
					<h1>Create Account</h1>
					<input
						type="text"
						name="username"
						placeholder="Username"
						value={registerData.username}
						onChange={handleInputChange}
					/>
					<input
						type="email"
						name="email"
						placeholder="Email"
						value={registerData.email}
						onChange={handleInputChange}
					/>
					<input
						type="password"
						name="password"
						placeholder="Password"
						value={registerData.password}
						onChange={handleInputChange}
					/>
					<input
						type="password"
						name="confirmPassword"
						placeholder="Confirm Password"
						value={registerData.confirmPassword}
						onChange={handleInputChange}
					/>
					<button type="submit">Sign Up</button>
				</form>
				;
			</div>
			<div
				className={
					!isActive ? "form-container sign-in active" : "form-container sign-in"
				}
			>
				<form onSubmit={handleLoginSubmit}>
					<h1>Sign In</h1>
					<input
						type="text"
						placeholder="Username"
						name="username"
						value={loginData.username}
						onChange={(e) => handleInputChange(e, setLoginData)}
					/>
					<input
						type="password"
						placeholder="Password"
						name="password"
						value={loginData.password}
						onChange={(e) => handleInputChange(e, setLoginData)}
					/>
					<button type="submit">Sign In</button>
				</form>
			</div>
			<div className="toggle-container">
				<div className="toggle">
					<div className="toggle-panel toggle-left">
						<h1>Welcome Back!</h1>
						<button
							className={!isActive ? "hidden" : ""}
							id="Login"
							onClick={handleLoginClick}
						>
							Sign In
						</button>
					</div>
					<div className="toggle-panel toggle-right">
						<h1>Welcome!</h1>

						<button
							className={isActive ? "hidden" : ""}
							id="Register"
							onClick={handleRegisterClick}
						>
							Sign Up
						</button>
					</div>
				</div>
			</div>
		</div>
	);
};

export default LoginSignup;
