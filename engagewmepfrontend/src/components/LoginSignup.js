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

	const [loginError, setLoginError] = useState("");
	const [registerError, setRegisterError] = useState("");

	const handleRegisterClick = () => {
		setIsActive(true);
	};

	const handleLoginClick = () => {
		setIsActive(false);
	};

	const handleRegisterSubmit = async (e) => {
		e.preventDefault();
		setRegisterError(""); // Reset error message

		const errors = {};

		if (!registerData.username) {
			errors.username = "Username is required";
		}
		if (!registerData.email) {
			errors.email = "Email is required";
		} else if (!/\S+@\S+\.\S+/.test(registerData.email)) {
			errors.email = "Email address is invalid";
		}
		if (!registerData.password) {
			errors.password = "Password is required";
		} else if (registerData.password.length < 6) {
			errors.password = "Password should be at least 6 characters long";
		}
		if (registerData.password !== registerData.confirmPassword) {
			errors.confirmPassword = "Passwords do not match";
		}

		if (Object.keys(errors).length > 0) {
			setRegisterError(errors);
			return;
		}

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
				setRegisterError({ server: error.response.data });
			} else {
				console.error("Registration error:", error.response || error.message);
			}
		}
	};
	

	const handleLoginSubmit = async (e) => {
		e.preventDefault();
		setLoginError(""); // Reset error message

		const errors = {};

		if (!loginData.email) {
			errors.email = "Email is required";
		} else if (!/\S+@\S+\.\S+/.test(loginData.email)) {
			errors.email = "Email address is invalid";
		}
		if (!loginData.password) {
			errors.password = "Password is required";
		}
	
		if (Object.keys(errors).length > 0) {
			setLoginError(errors);
			return;
		}

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
				setLoginError({ server: "User not found. Please check your email." });
			} else if (error.response && error.response.status === 401) {
				setLoginError({ server: "Incorrect password. Please try again." });
			} else {
				setLoginError({ server: "Login failed. Please try again later." });
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
		<div>
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
					{registerError.username && <p className="error-message">{registerError.username}</p>}
					<input
						type="email"
						name="email"
						placeholder="Email"
						value={registerData.email}
						onChange={(e) => handleInputChange(e, "register")}
					/>
					{registerError.email && <p className="error-message">{registerError.email}</p>}
					<input
						type="password"
						name="password"
						placeholder="Password"
						value={registerData.password}
						onChange={(e) => handleInputChange(e, "register")}
					/>
					{registerError.password && <p className="error-message">{registerError.password}</p>}
					<input
						type="password"
						name="confirmPassword"
						placeholder="Confirm Password"
						value={registerData.confirmPassword}
						onChange={(e) => handleInputChange(e, "register")}
					/>
					{registerError.confirmPassword && <p className="error-message">{registerError.confirmPassword}</p>}
					{registerError.server && <p className="error-message">{registerError.server}</p>}
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
					{loginError.email && <p className="error-message">{loginError.email}</p>}
					<input
						type="password"
						placeholder="Password"
						name="password"
						value={loginData.password}
						onChange={(e) => handleInputChange(e, "login")}
					/>
					{loginError.password && <p className="error-message">{loginError.password}</p>}
					{loginError.server && <p className="error-message">{loginError.server}</p>}
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
		<div>
		<button className="alumni-link" onClick={() => navigate("/")}>
	Are you an alumni? Navigate here
</button>
</div>
		</div>
	);
};

export default LoginSignup;
