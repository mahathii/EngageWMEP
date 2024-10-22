import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./ForgotPassword.css";

const ForgotPassword = () => {
    const [email, setEmail] = useState("");
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
    
        try {
            await axios.post("http://localhost:8080/api/auth/forgot-password", { email });
            setMessage("A password reset link has been sent to your email.");
        } catch (error) {
            console.error("Error sending reset link:", error.response || error.message);
            setMessage("Error sending reset link. Please try again.");
        }
    };
    

    return (
        <div className="forgot-password-container">
        <h1 className="forgot-password-heading">Forgot Password</h1>
        <form className="forgot-password-form" onSubmit={handleSubmit}>
            <input
                type="email"
                name="email"
                placeholder="Enter your email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
            />
            <button type="submit">Send Reset Link</button>
        </form>
        {message && <p className="forgot-password-message">{message}</p>}
        <button className="back-to-login-button" onClick={() => navigate("/signup")}>
            Back to Login
        </button>
    </div>
);
};

export default ForgotPassword;
