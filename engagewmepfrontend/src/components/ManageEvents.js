import React, { useState, useEffect } from "react";
import axios from "axios";
import "./EventsPage.css";
import { Link } from 'react-router-dom';

const ManageEvents = () => {
    const [studentEvents, setStudentEvents] = useState([]);
    const [newEventName, setNewEventName] = useState("");
    const [newEventDate, setNewEventDate] = useState("");
    const [newEventTime, setNewEventTime] = useState("");
    const [newEventLocation, setNewEventLocation] = useState("");
    const [editingEvent, setEditingEvent] = useState(null);
    const [minTime, setMinTime] = useState(""); 
    const [confirmationMessage, setConfirmationMessage] = useState("");

    useEffect(() => {
        fetchEvents();
    }, []);

    const fetchEvents = () => {
        axios.get("/events")
            .then(response => setStudentEvents(response.data))
            .catch(error => console.error("Error fetching events:", error));
    };

    const getTodayDate = () => {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0');
        const day = String(today.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    };

    const getCurrentTime = () => {
        const now = new Date();
        const hours = String(now.getHours()).padStart(2, '0');
        const minutes = String(now.getMinutes()).padStart(2, '0');
        return `${hours}:${minutes}`;
    };

    useEffect(() => {
        if (newEventDate === getTodayDate()) {
            setMinTime(getCurrentTime());
        } else {
            setMinTime("00:00");  // Reset to midnight for future dates
        }
    }, [newEventDate]);

    const addEvent = () => {
        if (!newEventName.trim() || !newEventDate || !newEventTime || !newEventLocation) {
            setConfirmationMessage("Event name, date, time, and location are required.");
            setTimeout(() => setConfirmationMessage(""), 3000);
            return;
        }

        const selectedDateTime = new Date(`${newEventDate}T${newEventTime}`);
        const now = new Date();

        if (selectedDateTime < now) {
            setConfirmationMessage("Event cannot be created in the past.");
            setTimeout(() => setConfirmationMessage(""), 3000);
            return;
        }
    
        const newEvent = {
            name: newEventName,
            eventDate: newEventDate,  // directly use the date string from the input
            eventTime: newEventTime,
            eventLocation: newEventLocation,
        };

        
        axios.post("/events", newEvent)
        .then(response => {
            setStudentEvents([...studentEvents, response.data]);
            setNewEventName("");
            setNewEventDate("");
            setNewEventTime("");
            setNewEventLocation("");
            setConfirmationMessage("Event added successfully!");
            setTimeout(() => setConfirmationMessage(""), 3000);
        })
        .catch(error => console.error("Error adding new event:", error));
};

    const editEvent = (event) => {
        setEditingEvent(event);
        setNewEventName(event.name);
        setNewEventDate(event.eventDate);
        setNewEventTime(event.eventTime);
        setNewEventLocation(event.eventLocation);
    };

    const updateEvent = () => {
        if (!editingEvent) return;

        axios.put(`/events/${editingEvent.id}`, { name: newEventName, eventDate: newEventDate, eventTime: newEventTime, eventLocation: newEventLocation })
            .then(response => {
                const updatedEvents = studentEvents.map(event =>
                    event.id === editingEvent.id ? response.data : event
                );
                setStudentEvents(updatedEvents);
                setEditingEvent(null);
                setNewEventName("");
                setNewEventDate("");
                setNewEventTime("");
                setNewEventLocation("");
                setConfirmationMessage("Event updated successfully!");
                setTimeout(() => setConfirmationMessage(""), 3000);
            })
            .catch(error => console.error("Error updating event:", error));
    };

    const resetEditState = () => {
        setEditingEvent(null);
        setNewEventName("");
        setNewEventDate("");
        setNewEventTime("");
        setNewEventLocation("");
    };

    const deleteEvent = (id) => {
        axios.delete(`/events/${id}`)
            .then(() => {
                setStudentEvents(studentEvents.filter(event => event.id !== id));
                setConfirmationMessage("Event deleted successfully!");
                setTimeout(() => setConfirmationMessage(""), 3000);
            })
            .catch(error => console.error("Error deleting event:", error));
    };

    
    const isPastEvent = (eventDate, eventTime) => {
        const now = new Date();
    
        if (!eventTime) {
            // If eventTime is not provided, consider only the eventDate
            const [year, month, day] = eventDate.split('-').map(Number);
            const eventDateOnly = new Date(year, month - 1, day);
            return eventDateOnly < new Date(now.getFullYear(), now.getMonth(), now.getDate());
        } else {
            // If eventTime is provided, consider both eventDate and eventTime
            const [year, month, day] = eventDate.split('-').map(Number);
            const [hours, minutes] = eventTime.split(':').map(Number);
            const eventDateTime = new Date(year, month - 1, day, hours, minutes);
            return eventDateTime < now;
        }
    };


    return (
        <div className="events-page-container">
            <Link to="/events" className="back-to-dashboard-event">Back to Events</Link>

            <div className="add-event-container">
                <input
                    type="text"
                    value={newEventName}
                    onChange={(e) => setNewEventName(e.target.value)}
                    className="input-field"
                    placeholder="Event Name"
                />
                <input
                    type="date"
                    value={newEventDate}
                    onChange={(e) => setNewEventDate(e.target.value)}
                    className="input-field"
                    min={getTodayDate()} 
                />
                <input
                    type="time"
                    value={newEventTime}
                    onChange={(e) => setNewEventTime(e.target.value)}
                    className="input-field"
                    min={minTime} // Dynamically set minimum time
                />
                <input
                    type="text"
                    value={newEventLocation}
                    onChange={(e) => setNewEventLocation(e.target.value)}
                    className="input-field"
                    placeholder="Event Location"
                />
                {editingEvent ? (
                    <>
                        <button className="events-button" onClick={updateEvent}>
                            Update Event
                        </button>
                        <button className="events-button cancel-button" onClick={resetEditState}>
                            Cancel
                        </button>
                    </>
                ) : (
                    <button className="events-button" onClick={addEvent}>
                        Add Event
                    </button>
                )}
                
            </div>

            {confirmationMessage && (
                <p className="confirmation-message">{confirmationMessage}</p>
            )}

            <div>
                <br />
                <h2>Total Events: {studentEvents.length}</h2>
                {studentEvents.length > 0 && (
                    <table className="events-table">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Date</th>
                                <th>Time</th>
                                <th>Location</th>
                                <th>Actions</th> 
                            </tr>
                        </thead>
                        <tbody>
                            {studentEvents.map((event, index) => (
                                <tr key={index}>
                                    <td>{event.name}</td>
                                    <td>{event.eventDate}</td>
                                    <td>{event.eventTime}</td>
                                    <td>{event.eventLocation}</td>
                                    <td>
    <button 
        className="actions-button" 
        onClick={() => editEvent(event)} 
        disabled={isPastEvent(event.eventDate, event.eventTime)}
    >
        Edit
    </button>
    <button 
        className="actions-button" 
        onClick={() => deleteEvent(event.id)} 
        disabled={isPastEvent(event.eventDate, event.eventTime)}
    >
        Delete
    </button>
</td>

                                </tr>
                            ))}
                        </tbody>
                    </table>
                )}
            </div>
        </div>
    );
};

export default ManageEvents;
