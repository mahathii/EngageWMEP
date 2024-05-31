import React, { useEffect, useState } from "react";
import axios from "axios";
import Multiselect from "multiselect-react-dropdown";
import { PDFDownloadLink } from "@react-pdf/renderer";
import MyDocument from "./MyDocument";
import "./StudentsPage.css";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

const StudentsPage = () => {
	const [events, setEvents] = useState([]);
	const [students, setStudents] = useState([]);
	const [selectedEventsForMultiselect, setSelectedEventsForMultiselect] =
		useState([]);
	const [fetchStrategy, setFetchStrategy] = useState("ANY");
	const [prepareDownload, setPrepareDownload] = useState(false);
	const [selectedColumns, setSelectedColumns] = useState({});
	const [columnOptions, setColumnOptions] = useState([]);
	const [selectedTimeFrame, setSelectedTimeFrame] = useState({});

	useEffect(() => {
		axios
			.get(`/events`)
			.then((response) => {
				setEvents(
					response.data.map((event) => ({ name: event.name, id: event.id }))
				);
				fetchStudentsForSelectedEvents();
			})
			.catch((error) => {
				console.error("Error fetching events:", error);
			});

		axios
			.get(`/events/columns`)
			.then((response) => {
				const options = response.data.map((column) => ({
					name: column,
					// formattedName: formatColumnName(column),
				}));
				setColumnOptions(options);
			})
			.catch((error) => {
				console.error("Error fetching student columns:", error);
			});
	}, []);

	const onSelectedEventsChange = (selectedList) => {
		setSelectedEventsForMultiselect(selectedList);
		setPrepareDownload(false);
	};

	// const handleDisplayStudentsClick = () => {
	// 	const selectedIds = selectedEventsForMultiselect.map((event) => event.id);
	// 	fetchStudentsForSelectedEvents(selectedIds, fetchStrategy);
	// 	setPrepareDownload(false);
	// };

	// const fetchStudentsForSelectedEvents = (selectedIds = [], strategy) => {
	// 	let url = `/events/students?strategy=${strategy}`;
	// 	if (selectedIds.length > 0) {
	// 		const queryParam = selectedIds.map((id) => `eventId=${id}`).join("&");
	// 		url += `&${queryParam}`;
	// 	}

	// 	axios
	// 		.get(url)
	// 		.then((response) => {
	// 			setStudents(response.data);
	// 		})
	// 		.catch((error) => {
	// 			console.error("Error fetching students:", error);
	// 		});
	// };

	const handleDisplayStudentsClick = () => {
		const selectedIds = selectedEventsForMultiselect.map((event) => event.id);
		fetchStudentsForSelectedEvents(
			selectedIds,
			fetchStrategy,
			selectedTimeFrame
		);
		setPrepareDownload(false);
	};

	const fetchStudentsForSelectedEvents = (
		selectedIds = [],
		strategy,
		timeFrame
	) => {
		let url = `/events/students?strategy=${strategy}`;
		if (selectedIds.length > 0) {
			const queryParam = selectedIds.map((id) => `eventId=${id}`).join("&");
			url += `&${queryParam}`;
		}
		if (timeFrame.startDate && timeFrame.endDate) {
			url += `&startDate=${timeFrame.startDate.toISOString()}&endDate=${timeFrame.endDate.toISOString()}`;
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

	const handlePrepareDownloadClick = () => {
		setPrepareDownload(true);
	};

	const handleFetchStrategyChange = (e) => {
		setFetchStrategy(e.target.value);
		setPrepareDownload(false);
	};

	const handleColumnChange = (selectedList) => {
		const updatedSelectedColumns = {};
		columnOptions.forEach((option) => {
			updatedSelectedColumns[option.name] = selectedList.some(
				(selected) => selected.name === option.name
			);
		});
		setSelectedColumns(updatedSelectedColumns);
		setPrepareDownload(false);
	};

	const formatColumnName = (columnName) => {
		return columnName

			.replace(/([A-Z])/g, " $1")

			.replace(/_/g, " ")

			.trim()
			.replace(/^\w/, (c) => c.toUpperCase());
	};

	const formattedColumnOptions = columnOptions.map((option) => ({
		...option,
		formattedName: formatColumnName(option.name),
	}));

	const handleTimeFrameChange = (timeFrame) => {
		setSelectedTimeFrame((prevTimeFrame) => ({
			...prevTimeFrame,
			...timeFrame,
		}));
		setPrepareDownload(false);
	};

	const fetchStudentsForTimeFrame = (timeFrame) => {
		const { startDate, endDate } = timeFrame;
		const params = {};
		if (startDate) params.startDate = startDate.toISOString().split("T")[0]; // Ensure only the date part is sent
		if (endDate) params.endDate = endDate.toISOString().split("T")[0]; // Ensure only the date part is sent

		axios
			.get(`/events/students/timeframe`, { params })
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
			width: "300px",
			height: "40px",
		},
	};

	return (
		<div className="scrollable-table">
			<div className="content-container">
				<div className="time-frame-container">
					<DatePicker
						selected={selectedTimeFrame.startDate}
						onChange={(date) => handleTimeFrameChange({ startDate: date })}
						selectsStart
						startDate={selectedTimeFrame.startDate}
						endDate={selectedTimeFrame.endDate}
						placeholderText="Start Date"
					/>
					<DatePicker
						selected={selectedTimeFrame.endDate}
						onChange={(date) => handleTimeFrameChange({ endDate: date })}
						selectsEnd
						startDate={selectedTimeFrame.startDate}
						endDate={selectedTimeFrame.endDate}
						minDate={selectedTimeFrame.startDate}
						placeholderText="End Date"
					/>
					<button onClick={() => fetchStudentsForTimeFrame(selectedTimeFrame)}>
						Apply Time Frame
					</button>
					<button onClick={() => fetchStudentsForTimeFrame({})}>
						Clear Time Frame
					</button>
				</div>
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
				</div>
				<div className="fetch-strategy-container">
					<input
						type="radio"
						id="any"
						name="fetchStrategy"
						value="ANY"
						checked={fetchStrategy === "ANY"}
						onChange={handleFetchStrategyChange}
					/>
					<label htmlFor="any">Any</label>
					<input
						type="radio"
						id="all"
						name="fetchStrategy"
						value="ALL"
						checked={fetchStrategy === "ALL"}
						onChange={handleFetchStrategyChange}
					/>
					<label htmlFor="all">All</label>
				</div>

				<div className="dropdown-container">
					<Multiselect
						// options={columnOptions}
						options={formattedColumnOptions}
						selectedValues={Object.keys(selectedColumns)
							.filter((column) => selectedColumns[column])
							.map((column) => ({ name: column }))}
						onSelect={handleColumnChange}
						onRemove={handleColumnChange}
						displayValue="name"
						showCheckbox={true}
						style={multiselectStyles}
						closeIcon="close"
						placeholder="Select Columns"
					/>
				</div>
				<div>
					<button
						className="students-button"
						onClick={handleDisplayStudentsClick}
					>
						Display Students
					</button>

					<button
						className="students-button"
						onClick={handlePrepareDownloadClick}
					>
						Prepare Download
					</button>

					{prepareDownload && (
						<PDFDownloadLink
							document={
								<MyDocument
									students={students}
									selectedEvents={selectedEventsForMultiselect}
									fetchStrategy={fetchStrategy}
									selectedColumns={selectedColumns}
								/>
							}
							fileName={`students_${new Date().toISOString()}.pdf`}
						>
							{({ blob, url, loading, error }) =>
								loading ? "Loading document..." : "Download PDF"
							}
						</PDFDownloadLink>
					)}
				</div>
				<div>
					<br></br>
					<h2>Number of students: {students.length}</h2>
					<br></br>
				</div>
				<div>
					<table className="table-text-small">
						<thead className="thead-dark">
							<tr>
								{Object.keys(selectedColumns).map(
									(columnName) =>
										selectedColumns[columnName] && (
											<th key={columnName}>{formatColumnName(columnName)}</th>
										)
								)}
							</tr>
						</thead>
						<tbody>
							{students.map((student, index) => (
								<tr key={index}>
									{Object.keys(selectedColumns).map((columnName) => {
										if (selectedColumns[columnName]) {
											return (
												<td key={`${columnName}-${index}`}>
													{student[columnName] !== null
														? student[columnName]
														: "N/A"}
												</td>
											);
										}
										return null;
									})}
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
