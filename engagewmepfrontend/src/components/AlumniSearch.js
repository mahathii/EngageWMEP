import React, { useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import "./AlumniSearch.css";

const AlumniSearch = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [alumni, setAlumni] = useState(null);
  const [notFound, setNotFound] = useState(false);
  const navigate = useNavigate();

  const handleSearch = async () => {
    try {
      const response = await axios.get(`/api/alumni/search?term=${searchTerm}`);
      if (response.data) {
        setAlumni(response.data);
        setNotFound(false);
      } else {
        setAlumni(null);
        setNotFound(true);
      }
    } catch (error) {
      console.error("Error searching alumni", error);
      setAlumni(null);
      setNotFound(true);
    }
  };

  return (
    <div className="alumni-search-page">
      <div className="alumni-search-container">
        <h3 className="student-heading">Check if you are in our Alumni database:</h3>
        <input
          type="text"
          placeholder="Search by email"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="search-input-alumni-search"
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
