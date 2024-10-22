
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

  const handleFileUpload = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await axios.post(`/api/event-attendance/${selectedEvent}/upload`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      console.log('File uploaded successfully. Response data:', response.data);
      alert('File uploaded successfully');
    } catch (error) {
      console.error('Error uploading file:', error.message);
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
      </form>
    </div>
  );
};

export default EventAttendancePage;