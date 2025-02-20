import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import './EventAttendancePage.css';

const EventAttendancePage = () => {
  const [events, setEvents] = useState([]);
  const [selectedEvent, setSelectedEvent] = useState('');
  const [file, setFile] = useState(null);
  const [attendanceRecords, setAttendanceRecords] = useState([]);
  const [isAttendanceUploaded, setIsAttendanceUploaded] = useState(false);

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

  useEffect(() => {
    if (selectedEvent) {
      fetchAttendance(selectedEvent);
    }
  }, [selectedEvent]);

  const fetchAttendance = async (eventId) => {
    try {
      const response = await axios.get(`/api/event-attendance/${eventId}`);
      console.log("Fetched Attendance Records:", response.data);
      setAttendanceRecords(response.data);
      setIsAttendanceUploaded(response.data.length > 0);
    } catch (error) {
      console.error('Error fetching attendance records:', error);
      setIsAttendanceUploaded(false);
    }
  };

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

      if (response.data.includes('already exists')) {
        alert('Attendance already uploaded for this event.');
      } else {
        alert('File uploaded successfully.');
        fetchAttendance(selectedEvent);
      }
    } catch (error) {
      console.error('Error uploading file:', error.message);
      alert('Failed to upload the file.');
    }
  };

  const handleDeleteAllAttendance = async () => {
    if (!window.confirm("Are you sure you want to delete all attendance records for this event?")) return;
  
    try {
      await axios.delete(`/api/event-attendance/${selectedEvent}/delete`);
      alert("All attendance records deleted successfully.");
  
      // Clear attendance records from UI
      setAttendanceRecords([]);
      setIsAttendanceUploaded(false);
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
  
        {!isAttendanceUploaded && (
          <>
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
          </>
        )}
      </form>
  
      {isAttendanceUploaded && (
        <div className="attendance-list">
          <h3>Uploaded Attendance Records</h3>
          <ul>
            {attendanceRecords.map((record) => (
              <li key={record.student.id}>
                {record.student.firstName} {record.student.lastName} ({record.student.studentId})
              </li>
            ))}
          </ul>
          <button className="delete-button" onClick={handleDeleteAllAttendance}>
            Delete All Attendance
          </button>
        </div>
      )}
    </div>
  );
};

export default EventAttendancePage;
