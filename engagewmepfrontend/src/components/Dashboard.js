import React, { useEffect, useState } from "react";
import axios from "axios";
import Multiselect from "multiselect-react-dropdown";
import "./Dashboard.css";

const Dashboard = () => {
	const [selectedEvent, setSelectedEvent] = useState([]);
	const [events, setEvents] = useState([]);
	const [students, setStudents] = useState([]);
	const [studentIdInput, setStudentIdInput] = useState("");
	const [studentEvents, setStudentEvents] = useState([]);
	const [selectedEventsForMultiselect, setSelectedEventsForMultiselect] =
		useState([]);

	useEffect(() => {
		axios
			.get(`/events`)
			.then((response) => {
				setEvents(
					response.data.map((event) => ({ name: event.name, id: event.id }))
				);
			})
			.catch((error) => {
				console.error("Error fetching events:", error);
			});
	}, []);

	useEffect(() => {
		if (selectedEvent.length > 0) {
			const queryParam = selectedEvent.map((id) => `eventId=${id}`).join("&");
			axios
				.get(`/events/students?${queryParam}`)
				.then((response) => {
					setStudents(response.data);
				})
				.catch((error) => {
					console.error("Error fetching students for events:", error);
				});
		} else {
			setStudents([]);
		}
	}, [selectedEvent]);

	const onSelectedEventsChange = (selectedList, selectedItem) => {
		setSelectedEventsForMultiselect(selectedList);
		// Assuming the selectedList contains objects with event 'id', extract these ids.
		const selectedIds = selectedList.map((event) => event.id);
		setSelectedEvent(selectedIds); // Now using setSelectedEvent correctly to update the state.
	};

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
			{/* Multiselect dropdown for multiple event selections */}
			<Multiselect
				options={events} // Options to display in the dropdown
				selectedValues={selectedEventsForMultiselect} // Preselected value to persist in dropdown
				onSelect={onSelectedEventsChange} // Function will trigger on select event
				onRemove={onSelectedEventsChange} // Function will trigger on remove event
				displayValue="name" // Property name to display in the dropdown options
				showCheckbox={true}
			/>

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
