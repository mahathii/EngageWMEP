import React, { useEffect, useState } from "react";
import axios from "axios";
import Multiselect from "multiselect-react-dropdown";
import "./StudentsPage.css";

const StudentsPage = () => {
	const [events, setEvents] = useState([]);
	const [students, setStudents] = useState([]);
	const [selectedEventsForMultiselect, setSelectedEventsForMultiselect] =
		useState([]);

	const [fetchStrategy, setFetchStrategy] = useState("ANY");

	useEffect(() => {
		axios
			.get(`/events`)
			.then((response) => {
				setEvents(
					response.data.map((event) => ({ name: event.name, id: event.id }))
				);
				fetchStudentsForSelectedEvents(); // Fetch all students initially if that's the intention
			})
			.catch((error) => {
				console.error("Error fetching events:", error);
			});
	}, []);

	const onSelectedEventsChange = (selectedList) => {
		setSelectedEventsForMultiselect(selectedList);
	};

	const handleDisplayStudentsClick = () => {
		const selectedIds = selectedEventsForMultiselect.map((event) => event.id);
		fetchStudentsForSelectedEvents(selectedIds, fetchStrategy);
	};

	const fetchStudentsForSelectedEvents = (selectedIds = [], strategy) => {
		let url = `/events/students?strategy=${strategy}`;
		if (selectedIds.length > 0) {
			const queryParam = selectedIds.map((id) => `eventId=${id}`).join("&");
			url += `&${queryParam}`;
		}

		axios
			.get(url)
			.then((response) => {
				setStudents(response.data);
			})
			.catch((error) => {
				console.error("Error fetching students:", error);
			});
	};

	const multiselectStyles = {
		chips: {
			background: "linear-gradient(to bottom, lightgrey, #808080)",
			color: "black",
			borderColor: "black",
		},
		option: {
			color: "black",
			background: "white",
			border: "black",
		},
		searchBox: {
			borderColor: "black",
			fontSize: "20px",
			minHeight: "50px",
			width: "1000px",
		},
		inputField: {
			width: "300px", // Adjust the width as needed
			height: "40px", // Adjust the height as needed
		},
	};

	return (
		<div className="scrollable-table">
			<div className="content-container">
				<div className="dropdown-container">
					<Multiselect
						options={events}
						selectedValues={selectedEventsForMultiselect}
						onSelect={onSelectedEventsChange}
						onRemove={onSelectedEventsChange}
						displayValue="name"
						showCheckbox={true}
						style={multiselectStyles}
						closeIcon="close"
						placeholder="Select Events"
					/>
					<button
						className="students-button"
						onClick={handleDisplayStudentsClick}
					>
						Display Students
					</button>
				</div>
				<div className="fetch-strategy-container">
					<input
						type="radio"
						id="any"
						name="fetchStrategy"
						value="ANY"
						checked={fetchStrategy === "ANY"}
						onChange={(e) => setFetchStrategy(e.target.value)}
					/>
					<label htmlFor="any">Any</label>
					<input
						type="radio"
						id="all"
						name="fetchStrategy"
						value="ALL"
						checked={fetchStrategy === "ALL"}
						onChange={(e) => setFetchStrategy(e.target.value)}
					/>
					<label htmlFor="all">All</label>
				</div>

				<div>
					<br></br>
					<h2>Total Students: {students.length}</h2>
					<br></br>
				</div>
				<div>
					<table className="table-text-small">
						<thead className="thead-dark">
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
				</div>
			</div>
		</div>
	);
};

export default StudentsPage;
