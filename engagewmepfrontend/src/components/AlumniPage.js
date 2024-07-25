// src/components/AlumniPage.js
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import './AlumniPage.css'; // Import CSS for styling

const AlumniPage = () => {
  const [alumni, setAlumni] = useState([]);
  const [searchTerm, setSearchTerm] = useState(''); // State for search term
  const [filteredAlumni, setFilteredAlumni] = useState([]); // State for filtered alumni

  useEffect(() => {
    const fetchAlumni = async () => {
      try {
        const response = await axios.get('/api/alumni');
        setAlumni(response.data);
        setFilteredAlumni(response.data); // Initially set filtered alumni to all alumni
      } catch (error) {
        console.error('Error fetching alumni data', error);
      }
    };

    fetchAlumni();
  }, []);

  useEffect(() => {
    const results = alumni.filter(alum =>
      `${alum.firstName} ${alum.lastName}`.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setFilteredAlumni(results);
  }, [searchTerm, alumni]);

  return (
    <div className="alumni-page"> {/* Added class for styling */}
      <Link to="/dashboard" className="back-to-dashboard-alumni">Back to Dashboard</Link>
      <h1 className="alumni-list-heading">Alumni List</h1>
      <input
        type="text"
        placeholder="Search by name"
        value={searchTerm}
        onChange={e => setSearchTerm(e.target.value)}
        className="search-input-alumni" // Added class for search input styling
      />
      <div className="table-container"> {/* Added container for scrollable table */}
        <table>
          <thead>
            <tr>
              <th>Sno</th> {/* Added Sno column */}
              <th>Name</th> {/* Modified column name */}
            </tr>
          </thead>
          <tbody>
            {filteredAlumni.map((alum, index) => (
              <tr key={alum.id}>
                <td>{index + 1}</td> {/* Display Sno */}
                <td>
                  <Link to={`/alumni/${alum.id}`}>
                    {alum.firstName} {alum.lastName}
                  </Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default AlumniPage;
