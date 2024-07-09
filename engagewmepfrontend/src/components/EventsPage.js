import React, { useState } from "react";
import axios from "axios";
import "./EventsPage.css";
import { Link } from 'react-router-dom';

const EventsPage = () => {
	const [studentIdInput, setStudentIdInput] = useState("");
	const [studentEvents, setStudentEvents] = useState([]);

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
			<Link to="/dashboard" className="back-to-dashboard-event">Back to Dashboard</Link>
			{" "}
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
			<div>
				<br></br>
				<h2>Total Events: {studentEvents.length}</h2>
				{studentEvents.length > 0 && (
					<table className="events-table">
						<thead>
							<tr>
								<th>Event Name</th>
							</tr>
						</thead>
						<tbody>
							{studentEvents.map((event, index) => (
								<tr key={index}>
									<td>{event.name}</td>
								</tr>
							))}
						</tbody>
					</table>
				)}
			</div>
		</div>
	);
};

export default EventsPage;
