import React from "react";
import { Link } from "react-router-dom";
import "./StudentsPage.css";

const StudentsPage = () => {
    return (
        <div className="scrollable-table">
            <div className="content-container">
                <h1>Student Management</h1>
                <button to="/view-by-date" className="students-button">
                    View students by event date
                </button>
                <button to="/view-by-event" className="students-button">
                    View students by event name
                </button>
            </div>
        </div>
    );
};

export default StudentsPage;

