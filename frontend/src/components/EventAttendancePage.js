
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import './EventAttendancePage.css';

const EventAttendancePage = () => {
  const [events, setEvents] = useState([]);
  const [selectedEvent, setSelectedEvent] = useState('');
  const [file, setFile] = useState(null);

  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const response = await axios.get('/events');
        setEvents(response.data);
      } catch (error) {
        console.error('Error fetching events', error);
      }
    };

    fetchEvents();
  }, []);

  // const handleFileUpload = async (e) => {
  //   e.preventDefault();
  //   const formData = new FormData();
  //   formData.append('file', file);

  //   try {
  //     const response = await axios.post(`/api/event-attendance/${selectedEvent}/upload`, formData, {
  //       headers: {
  //         'Content-Type': 'multipart/form-data'
  //       }
  //     });
  //     console.log('File uploaded successfully. Response data:', response.data);
  //     alert('File uploaded successfully');
  //   } catch (error) {
  //     console.error('Error uploading file:', error.message);
  //   }
  // };

  const handleFileUpload = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append('file', file);
  
    try {
      const response = await axios.post(
        `/api/event-attendance/${selectedEvent}/upload`,
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }
      );
  
      // Handle backend response messages
      if (response.data.includes('already exists')) {
        alert('Attendance already uploaded for this event.');
      } else {
        alert('File uploaded successfully.');
      }
    } catch (error) {
      console.error('Error uploading file:', error.message);
      if (error.response && error.response.data.includes('Incorrect Excel sheet format')) {
        alert('Incorrect Excel sheet format. Please upload a valid file.');
      } else if (error.response && error.response.data.includes('already exists')) {
        alert('Attendance already uploaded for this event.');
      } else if (error.response && error.response.data) {
        alert(`Failed to upload the file. Error: ${error.response.data}`);
      } else {
        alert('Failed to upload the file. Please try again.');
      }
    }
  };

  const [attendanceRecords, setAttendanceRecords] = useState([]);

const fetchAttendance = async (eventId) => {
  try {
    const response = await axios.get(`/api/event-attendance/${eventId}`);
    setAttendanceRecords(response.data);
  } catch (error) {
    console.error('Error fetching attendance records:', error);
  }
};

useEffect(() => {
  if (selectedEvent) {
    fetchAttendance(selectedEvent);
  }
}, [selectedEvent]);

const handleDeleteAttendance = async (studentId) => {
  if (!window.confirm("Are you sure you want to delete this attendance record?")) return;

  try {
    await axios.delete(`/api/event-attendance/${selectedEvent}/delete/${studentId}`);
    alert("Attendance record deleted successfully.");
    fetchAttendance(selectedEvent); // Refresh records after deletion
  } catch (error) {
    console.error("Error deleting attendance:", error);
    alert("Failed to delete attendance.");
  }
};

  return (
    <div className="event-attendance-page">
      <div className="back-buttons-container">
        <Link to="/dashboard" className="back-to-dashboard-student">Dashboard</Link>
      </div>
      <h2 className="event-attendance-heading">Upload Event Attendance</h2>
      <form className="event-attendance-form" onSubmit={handleFileUpload}>
        <label htmlFor="event-select">Select Event:</label>
        <select
          id="event-select"
          className="event-attendance-input"
          value={selectedEvent}
          onChange={(e) => setSelectedEvent(e.target.value)}
          required
        >
          <option value="">Select an event</option>
          {events.map(event => (
            <option key={event.id} value={event.id}>
              {event.name} - {event.eventDate}
            </option>
          ))}
        </select>

        <label htmlFor="file-upload">Upload File:</label>
        <input
          id="file-upload"
          className="event-attendance-input"
          type="file"
          accept=".xlsx, .xls"
          onChange={(e) => setFile(e.target.files[0])}
          required
        />

        <button type="submit" className="upload-button">Upload Attendance</button>
        {attendanceRecords.length > 0 && (
        <div className="attendance-list">
          <h3>Uploaded Attendance Records</h3>
          <ul>
            {attendanceRecords.map((record) => (
              <li key={record.student.id}>
                {record.student.firstName} {record.student.lastName} ({record.student.studentId})
                <button className="delete-button" onClick={() => handleDeleteAttendance(record.student.id)}>Delete</button>
              </li>
            ))}
          </ul>
        </div>
      )}
      </form>
      
    </div>
    
  );
};

export default EventAttendancePage;