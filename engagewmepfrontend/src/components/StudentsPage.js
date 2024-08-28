import React from "react";
import { useNavigate, Link} from "react-router-dom";
import "./StudentsPage.css";

const StudentsPage = () => {
    const navigate = useNavigate();

    const handleNavigate = (path) => {
        navigate(path);
    };

    return (
        <div className="scrollable-table">
            <div className="content-container">
            <div className="back-buttons-container">
        
        <Link to="/dashboard" className="back-to-dashboard-student">Dashboard</Link>
    </div>
                <div className="button-container">
                    <h1 className="student-heading">Student Management</h1>
                    <button 
                        className="students-button"
                        onClick={() => handleNavigate("/view-by-date")}
                    >
                        View students by event date
                    </button>
                    <button 
                        className="students-button"
                        onClick={() => handleNavigate("/view-by-event")}
                    >
                        View students by event name
                    </button>
                </div>             
            </div>
        </div>
    );
};

export default StudentsPage;
