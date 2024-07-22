import React, { useState } from "react";
import "./LoginSignup.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const LoginSignup = () => {
	const [isActive, setIsActive] = useState(false);
	const navigate = useNavigate();

	const [registerData, setRegisterData] = useState({
		username: "",
		email: "",
		password: "",
	});

	const [loginData, setLoginData] = useState({
		email: "",
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
			await axios.post("http://localhost:8080/register", registerData, {
				headers: {
					"Content-Type": "application/json",
					Accept: "application/json",
				},
			});
			navigate("/dashboard");
		} catch (error) {
			if (error.response && error.response.status === 400) {
				alert(error.response.data);
			} else {
				console.error("Registration error:", error.response || error.message);
			}
		}
	};

	const handleLoginSubmit = async (e) => {
		e.preventDefault();
		try {
			const response = await axios.post(
				"http://localhost:8080/login",
				loginData,
				{
					headers: {
						Accept: "application/json",
						"Content-Type": "application/json",
					},
				}
			);
			if (response.data) {
				localStorage.setItem("isLoggedIn", "true");
				navigate("/dashboard");
			}
		} catch (error) {
			if (error.response && error.response.status === 404) {
				alert("User not found. Please check your email.");
			} else if (error.response && error.response.status === 401) {
				alert("Incorrect password. Please try again.");
			} else {
				alert("Login failed. Please try again later.");
			}
		}
	};

	const handleInputChange = (e, stateType) => {
		const { name, value } = e.target;
		if (stateType === "register") {
			setRegisterData((prev) => ({
				...prev,
				[name]: value,
			}));
		} else if (stateType === "login") {
			setLoginData((prev) => ({
				...prev,
				[name]: value,
			}));
		}
	};

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
						onChange={(e) => handleInputChange(e, "register")}
					/>
					<input
						type="email"
						name="email"
						placeholder="Email"
						value={registerData.email}
						onChange={(e) => handleInputChange(e, "register")}
					/>
					<input
						type="password"
						name="password"
						placeholder="Password"
						value={registerData.password}
						onChange={(e) => handleInputChange(e, "register")}
					/>
					<input
						type="password"
						name="confirmPassword"
						placeholder="Confirm Password"
						value={registerData.confirmPassword}
						onChange={(e) => handleInputChange(e, "register")}
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
						placeholder="Email"
						name="email"
						value={loginData.email}
						onChange={(e) => handleInputChange(e, "login")}
					/>
					<input
						type="password"
						placeholder="Password"
						name="password"
						value={loginData.password}
						onChange={(e) => handleInputChange(e, "login")}
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
					{/* <div className="alumni-link-container">
						<a href="/" className="alumni-link">
							Are you an alumni? Navigate here
    					</a>
					</div> */}
					</div>
				</div>
			</div>
		</div>
	);
};

export default LoginSignup;
