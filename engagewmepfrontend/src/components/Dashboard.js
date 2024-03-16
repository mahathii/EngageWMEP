import React, { useEffect, useState } from "react";
import axios from "axios";

const Dashboard = () => {
	const [selectedEvent, setSelectedEvent] = useState("");
	const [events, setEvents] = useState([]);
	const [students, setStudents] = useState([]);

	useEffect(() => {
		axios
			.get(`/events`)
			.then((response) => {
				setEvents(response.data);
			})
			.catch((error) => {
				console.error("Error fetching events:", error);
			});
	}, []);

	useEffect(() => {
		if (selectedEvent) {
			axios
				.get(`/events/${selectedEvent}/students`)
				.then((response) => {
					setStudents(response.data);
				})
				.catch((error) => {
					console.error("Error fetching students for event:", error);
				});
		}
	}, [selectedEvent]);

	return (
		<div>
			<h1>Dashboard</h1>
			<select
				onChange={(e) => setSelectedEvent(e.target.value)}
				value={selectedEvent}
			>
				<option value="">Select an Event</option>
				{events.map((event, index) => (
					<option key={index} value={event.id}>
						{event.name}
					</option>
				))}
			</select>
			<div>
				{students.map((student, index) => (
					<div key={index}>
						{student.lastName} {student.firstName}
					</div>
				))}
			</div>
		</div>
	);
};

export default Dashboard;
