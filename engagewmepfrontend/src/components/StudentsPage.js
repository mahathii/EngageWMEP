import React, { useEffect, useState } from "react";
import axios from "axios";
import Multiselect from "multiselect-react-dropdown";
import { pdf } from "@react-pdf/renderer"; // Import the pdf function from @react-pdf/renderer
import MyDocument from "./MyDocument";
import "./StudentsPage.css";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { Link } from 'react-router-dom';

const StudentsPage = () => {
    const [events, setEvents] = useState([]);
    const [students, setStudents] = useState([]);
    const [selectedEventsForMultiselect, setSelectedEventsForMultiselect] =
        useState([]);
    const [fetchStrategy, setFetchStrategy] = useState("ANY");
    const [filteredStudents, setFilteredStudents] = useState([]); // State for filtered students
    const [searchTerm, setSearchTerm] = useState(''); 
    const [selectedColumns, setSelectedColumns] = useState({});
    const [columnOptions, setColumnOptions] = useState([]);
    const [selectedTimeFrame, setSelectedTimeFrame] = useState({ startDate: null, endDate: null });

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

    useEffect(() => {
        const results = students.filter(student =>
            `${student.firstName} ${student.lastName}`.toLowerCase().includes(searchTerm.toLowerCase()) ||
            student.studentId.toString().includes(searchTerm) // Student ID 
        );
        setFilteredStudents(results);
    }, [searchTerm, students]);

    const onSelectedEventsChange = (selectedList) => {
        setSelectedEventsForMultiselect(selectedList);
    };

    const handleDisplayStudentsClick = () => {
        const selectedIds = selectedEventsForMultiselect.map((event) => event.id);
        fetchStudentsForSelectedEvents(
            selectedIds,
            fetchStrategy,
            selectedTimeFrame
        );
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
                setStudents(Array.isArray(response.data) ? response.data : []);
                setFilteredStudents(Array.isArray(response.data) ? response.data : []);
            })
            .catch((error) => {
                console.error("Error fetching students:", error);
            });
    };

    const handleDownloadClick = async () => {
        const documentComponent = (
            <MyDocument
                students={students}
                selectedEvents={selectedEventsForMultiselect}
                fetchStrategy={fetchStrategy}
                selectedColumns={selectedColumns}
            />
        );

        const blob = await pdf(documentComponent).toBlob();
        const url = URL.createObjectURL(blob);
        const a = window.document.createElement("a");
        a.href = url;
        a.download = `students_${new Date().toISOString()}.pdf`;
        window.document.body.appendChild(a);
        a.click();
        window.document.body.removeChild(a);
        URL.revokeObjectURL(url);
    };

    const handleFetchStrategyChange = (e) => {
        setFetchStrategy(e.target.value);
    };

    const handleColumnChange = (selectedList) => {
        const updatedSelectedColumns = {};
        columnOptions.forEach((option) => {
            updatedSelectedColumns[option.name] = selectedList.some(
                (selected) => selected.name === option.name
            );
        });
        setSelectedColumns(updatedSelectedColumns);
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
    };

    const handleClearTimeFrameClick = () => {
        setSelectedTimeFrame({ startDate: null, endDate: null });
        fetchStudentsForTimeFrame({});
    };

    const fetchStudentsForTimeFrame = (timeFrame) => {
        const { startDate, endDate } = timeFrame;
        const params = {};
        if (startDate) params.startDate = startDate.toISOString().split("T")[0]; // Ensure only the date part is sent
        if (endDate) params.endDate = endDate.toISOString().split("T")[0]; // Ensure only the date part is sent

        axios
            .get(`/events/students/timeframe`, { params })
            .then((response) => {
                setStudents(Array.isArray(response.data) ? response.data : []);
                setFilteredStudents(Array.isArray(response.data) ? response.data : []);
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
        }
    };

    const searchInputStyles = {
        width: "90%",
        maxWidth: "1000px",
        height: "40px",
        padding: "10px",
        borderRadius: "5px",
        border: "1px solid black",
        boxShadow: "inset 0 1px 2px rgba(0,0,0,0.1)",
        backgroundColor: "white",
        margin: "0 0 20px 0" /* Only bottom margin is set to 20px */
    };

    return (
        <div className="scrollable-table">
            <div className="content-container">
                <Link to="/dashboard" className="back-to-dashboard-student">Back to Dashboard</Link>
                <div className="datepicker-container">
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
                    <button onClick={handleClearTimeFrameClick}>
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
                    {selectedEventsForMultiselect.length > 0 && ( // Conditionally render Display Students button
                        <button
                            className="students-button"
                            onClick={handleDisplayStudentsClick}
                        >
                            Display Students
                        </button>
                    )}
                    {students.length > 0 && Object.keys(selectedColumns).some(column => selectedColumns[column]) && (
                        <button
                            className="students-button"
                            onClick={handleDownloadClick}
                        >
                            Download
                        </button>
                    )}
                </div>
                <div className="dropdown-container">
                    <input
                        type="text"
                        placeholder="Search by name or student ID"
                        value={searchTerm}
                        onChange={e => setSearchTerm(e.target.value)}
                        style={searchInputStyles}
                    />
                </div>
                <div>
                    <br></br>
                    <h2>Number of students: {students.length}</h2>
                    <br></br>
                </div>
                <div className="table-wrapper">
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
                            {filteredStudents.length > 0 ? (
                                filteredStudents.map((student, index) => (
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
                                ))
                            ) : (
                                <tr>
                                    <td colSpan={Object.keys(selectedColumns).length}>No students found</td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default StudentsPage;
