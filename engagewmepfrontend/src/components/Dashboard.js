import React, { useEffect, useState } from "react";
import axios from "axios";
import "./Dashboard.css";

const Dashboard = () => {
	const [selectedEvent, setSelectedEvent] = useState("");
	const [events, setEvents] = useState([]);
	const [students, setStudents] = useState([]);
	const [studentIdInput, setStudentIdInput] = useState("");
	const [studentEvents, setStudentEvents] = useState([]);

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

	// Fetch events attended by a specific student
	const fetchStudentEvents = () => {
		axios
			.get(`/events/student/${studentIdInput}/events`)
			.then((response) => setStudentEvents(response.data))
			.catch((error) =>
				console.error("Error fetching student's events:", error)
			);
	};

	return (
		<div className="card-body p-0">
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

			<h2>Total Students: {students.length}</h2>
			<table className="table-text-small">
				<thead className="thead-dark sticky-top">
					<tr>
						<th>ID</th>
						<th>Last Name</th>
						<th>First Name</th>
						<th>Degree Level</th>
						<th>Graduation Date</th>
						<th>Major</th>
						<th>College</th>
						<th>Admin Major</th>
					</tr>
				</thead>
				<tbody>
					{students.map((student, index) => (
						<tr key={index}>
							<td>{student.studentId}</td>
							<td>{student.lastName}</td>
							<td>{student.firstName}</td>
							<td>{student.degreeLevel}</td>
							<td>{student.graduationDate}</td>
							<td>{student.major}</td>
							<td>{student.college}</td>
							<td>{student.adminMajor}</td>
						</tr>
					))}
				</tbody>
			</table>
			<input
				type="text"
				value={studentIdInput}
				onChange={(e) => setStudentIdInput(e.target.value)}
				placeholder="Enter student ID"
			/>
			<button onClick={fetchStudentEvents}>Find Student's Events</button>
			<div>
				{studentEvents.length > 0 && (
					<ul>
						{studentEvents.map((event, index) => (
							<li key={index}>{event.name}</li>
						))}
					</ul>
				)}
			</div>
		</div>
	);
};

export default Dashboard;
