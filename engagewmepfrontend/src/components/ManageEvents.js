import React, { useState } from "react";
import axios from "axios";
import "./EventsPage.css";
import { Link } from 'react-router-dom';

const ManageEvents = () => {
	const [studentEvents, setStudentEvents] = useState([]);
	const [newEventName, setNewEventName] = useState(""); // State for new event name
	const [newEventDate, setNewEventDate] = useState(""); // State for new event date
	const [confirmationMessage, setConfirmationMessage] = useState(""); // State for confirmation message

	// Function to add a new event
	const addEvent = () => {
		const newEvent = {
			name: newEventName,
			eventDate: newEventDate,
		};
		
		axios
			.post("/events", newEvent) // POST request to add a new event
			.then((response) => {
				setStudentEvents([...studentEvents, response.data]); // Update the list with the newly added event
				setNewEventName(""); // Clear input fields
				setNewEventDate(""); // Clear input fields
				setConfirmationMessage("Event added successfully!"); // Set confirmation message
				setTimeout(() => setConfirmationMessage(""), 3000); // Clear the message after 3 seconds
			})
			.catch((error) =>
				console.error("Error adding new event:", error)
			);
	};

	return (
		<div className="events-page-container">
			<Link to="/events" className="back-to-dashboard-event">Back to Events</Link>
			{/* New event input form */}
			<div className="add-event-container">
				<input
					type="text"
					value={newEventName}
					onChange={(e) => setNewEventName(e.target.value)}
					className="input-field"
					placeholder="Event Name"
				/>
				<input
					type="date"
					value={newEventDate}
					onChange={(e) => setNewEventDate(e.target.value)}
					className="input-field"
				/>
				<button className="events-button" onClick={addEvent}>
					Add Event
				</button>
			</div>

			{/* Display confirmation message */}
			{confirmationMessage && (
				<p className="confirmation-message">{confirmationMessage}</p>
			)}

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

export default ManageEvents;
