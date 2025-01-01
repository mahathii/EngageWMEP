import React from "react";
import { useNavigate, Link } from 'react-router-dom';
import "./EventsPage.css";

const EventsPage = () => {
	const navigate = useNavigate();

	const handleViewStudentsByEvents = () => {
		navigate('/student-events');
	};

	const handleManageEvents = () => {
		navigate('/manage-events');
	};

	return (
		<div className="events-page-container">
			<div className="back-buttons-container">
        
        <Link to="/dashboard" className="back-to-dashboard-student">Dashboard</Link>
    </div>
			
			<div className="events-navigation">
				<button className="events-button" onClick={handleViewStudentsByEvents}>
					View Events by Student ID
				</button>
				<button className="events-button" onClick={handleManageEvents}>
					Manage Events
				</button>
			</div>
		</div>
	);
};

export default EventsPage;
