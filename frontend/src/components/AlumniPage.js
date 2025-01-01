// src/components/AlumniPage.js
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import './AlumniPage.css'; 

const AlumniPage = () => {
  const [alumni, setAlumni] = useState([]);
  const [searchTerm, setSearchTerm] = useState(''); // State for search term
  const [filteredAlumni, setFilteredAlumni] = useState([]);
  const [filterValues, setFilterValues] = useState({});
  const [selectedFilters, setSelectedFilters] = useState({
    raceEthnicity: '',
    gender: '',
    ncsuGraduate: '',
    collegeOfEngineeringGraduate: '',
    yearOfGraduation: '',
    stpParticipationAndYear: '',
    major: '',
    currentCity: '',
    currentState: '',
    currentZipCode: '',
    mentoringOptIn: ''
  });

  // useEffect(() => {
  //   const fetchAlumni = async () => {
  //     try {
  //       const response = await axios.get('/api/alumni');
  //       setAlumni(response.data);
  //       setFilteredAlumni(response.data); // Initially set filtered alumni to all alumni
  //     } catch (error) {
  //       console.error('Error fetching alumni data', error);
  //     }
  //   };

  //   fetchAlumni();
  // }, []);

  useEffect(() => {
    const fetchAlumni = async () => {
      try {
        const response = await axios.get('/api/alumni');
        setAlumni(response.data);
        setFilteredAlumni(response.data);
      } catch (error) {
        console.error('Error fetching alumni data', error);
      }
    };

    const fetchFilterValues = async () => {
      try {
        const response = await axios.get('/api/alumni/filters');
        setFilterValues(response.data);
      } catch (error) {
        console.error('Error fetching filter values', error);
      }
    };

    fetchAlumni();
    fetchFilterValues();
  }, []);

  useEffect(() => {
    const results = alumni.filter(alum =>
      `${alum.firstName} ${alum.lastName}`.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setFilteredAlumni(results);
  }, [searchTerm, alumni]);

  useEffect(() => {
    let results = alumni;

    Object.keys(selectedFilters).forEach((key) => {
      if (selectedFilters[key]) {
        results = results.filter(alum => alum[key] === selectedFilters[key]);
      }
    });

    setFilteredAlumni(results);
  }, [selectedFilters, alumni]);

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setSelectedFilters(prevState => ({ ...prevState, [name]: value }));
  };

  return (
    <div className="alumni-page">
      <div className="back-buttons-container">
        <Link to="/dashboard" className="back-to-dashboard-student">Dashboard</Link>
    </div>
      <h1 className="alumni-list-heading">Alumni List</h1>
      <h3 className="alumni-count">Total Alumni: {filteredAlumni.length}</h3>
      <input
        type="text"
        placeholder="Search by name"
        value={searchTerm}
        onChange={e => setSearchTerm(e.target.value)}
        className="search-input-alumni"
      />
      <div className="filters">
        {Object.keys(filterValues).map(key => (
          <select
            key={key}
            name={key}
            value={selectedFilters[key]}
            onChange={handleFilterChange}
            className="filter-dropdown"
          >
            <option value="">{`Select ${key}`}</option>
            {filterValues[key].map(value => (
              <option key={value} value={value}>{value}</option>
            ))}
          </select>
        ))}
      </div>
      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>Sno</th>
              <th>Name</th>
            </tr>
          </thead>
          <tbody>
            {filteredAlumni.map((alum, index) => (
              <tr key={alum.id}>
                <td>{index + 1}</td>
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
