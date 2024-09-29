import React, { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import axios from "axios";

const VerifyEmail = () => {
	const [searchParams] = useSearchParams();
	const token = searchParams.get("token");
	const [verificationStatus, setVerificationStatus] = useState("Verifying...");
	const navigate = useNavigate();

	useEffect(() => {
		const verifyEmail = async () => {
			try {
				const response = await axios.get(`http://localhost:8080/api/auth/verify?token=${token}`);
				if (response.status === 200) {
					setVerificationStatus("Email verified successfully! You can now log in.");
					setTimeout(() => {
						navigate("/signup"); // Redirect to login after a short delay
					}, 3000);
				}
			} catch (error) {
				setVerificationStatus("Verification failed. Token may be expired or invalid.");
			}
		};

		if (token) {
			verifyEmail();
		}
	}, [token, navigate]);

	return <h1>{verificationStatus}</h1>;
};

export default VerifyEmail;
