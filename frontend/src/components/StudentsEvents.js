import React, { useState } from "react";
import axios from "axios";
import "./EventsPage.css";
import { Link } from 'react-router-dom';

const StudentsEvents = () => {
	const [studentIdInput, setStudentIdInput] = useState("");
	const [studentEvents, setStudentEvents] = useState([]);

	// Function to fetch events by student ID
	const fetchStudentEvents = () => {
		axios
			.get(`/events/${studentIdInput}/events`)
			.then((response) => setStudentEvents(response.data))
			.catch((error) =>
				console.error("Error fetching student's events:", error)
			);
	};

	return (
		<div className="events-page-container">
			<div className="back-buttons-container">
        <Link to="/events" className="back-to-dashboard-student">Back</Link>
        <Link to="/dashboard" className="back-to-dashboard-student">Dashboard</Link>
    </div>
			<div className="search-container">
				<input
					type="text"
					value={studentIdInput}
					onChange={(e) => setStudentIdInput(e.target.value)}
					className="input-field"
					placeholder="Enter student ID"
				/>
				<button className="events-button" onClick={fetchStudentEvents}>
					Find Student's Events
				</button>
			</div>

			<div>
				<br />
				<h2>Total Events: {studentEvents.length}</h2>
				{studentEvents.length > 0 && (
					<table className="events-table">
						<thead>
							<tr>
								<th>Event Name</th>
								<th>Event Date</th> {/* Event date column */}
							</tr>
						</thead>
						<tbody>
							{studentEvents.map((event, index) => (
								<tr key={index}>
									<td>{event.name}</td>
									<td>{event.eventDate}</td> {/* Display event date */}
								</tr>
							))}
						</tbody>
					</table>
				)}
			</div>
		</div>
	);
};

export default StudentsEvents;
