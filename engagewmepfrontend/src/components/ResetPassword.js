import React, { useState } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";
import "./ResetPassword.css";

const ResetPassword = () => {
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [error, setError] = useState("");
    const [message, setMessage] = useState("");
    const { token } = useParams(); // Get the token from URL
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        setMessage("");

        if (password !== confirmPassword) {
            setError("Passwords do not match");
            return;
        }

        try {
            await axios.post("http://localhost:8080/api/auth/reset-password", {
                token,
                password,
            });
            setMessage("Password reset successful! Please login.");
            // Optionally, redirect to login after a delay
            setTimeout(() => navigate("/signup"), 3000);
        } catch (error) {
            console.error("Error resetting password:", error.response || error.message);
            setError("Error resetting password. Please try again.");
        }
    };

    return (
        <div className="reset-password-container">
            <h1 className="reset-password-heading">Reset Password</h1>
            <form className="reset-password-form" onSubmit={handleSubmit}>
                <input
                    type="password"
                    placeholder="New Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="Confirm New Password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    required
                />
                {error && <p className="error-message">{error}</p>}
                {message && <p className="reset-password-message">{message}</p>}
                <button type="submit">Reset Password</button>
            </form>
        </div>
    );
};

export default ResetPassword;
