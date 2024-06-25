import React, { useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import "./AlumniSearch.css";

const AlumniSearch = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [alumni, setAlumni] = useState(null);
  const [notFound, setNotFound] = useState(false); // Add a state to track if the user is not found
  const navigate = useNavigate();

  const handleSearch = async () => {
    try {
      const response = await axios.get(`/api/alumni/search?term=${searchTerm}`);
      if (response.data && response.data.length > 0) {
        setAlumni(response.data);
        setNotFound(false); // User found, reset not found state
      } else {
        setAlumni(null);
        setNotFound(true); // User not found, set not found state
      }
    } catch (error) {
      console.error("Error searching alumni", error);
    }
  };

  return (
    <div className="alumni-search-page">
      <Link to="/dashboard" className="back-to-dashboard">Back to Dashboard</Link>
      <div className="alumni-search-container">
        <input
          type="text"
          placeholder="Search by name or email"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="search-input-alumni"
        />
        <button className="search-button" onClick={handleSearch}>Search</button>
        {alumni ? (
          <div className="search-result">
            {alumni.firstName ? "You are already in the database" : null}
          </div>
        ) : notFound ? (
          <div>
            <div className="not-found-message">User not found. Join our alumni database:</div>
            <button className="join-button" onClick={() => navigate('/alumni-form')}>
              Join our alumni database
            </button>
          </div>
        ) : null}
      </div>
    </div>
  );
};

export default AlumniSearch;
