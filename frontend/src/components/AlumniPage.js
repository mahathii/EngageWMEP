import React, { useState, useEffect, useMemo } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import './AlumniPage.css';

const AlumniPage = () => {
  const [alumni, setAlumni] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
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
    mentoringOptIn: '',
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [alumniResponse, filtersResponse] = await Promise.all([
          axios.get('/api/alumni'),
          axios.get('/api/alumni/filters'),
        ]);
        setAlumni(alumniResponse.data);
        setFilterValues(filtersResponse.data);
      } catch (error) {
        console.error('Error fetching data', error);
      }
    };
    fetchData();
  }, []);

  // Filter alumni based on selected filters
  const filteredAlumni = useMemo(() => {
    return alumni.filter(alum => {
      // Apply search filter
      const nameMatch = `${alum.firstName} ${alum.lastName}`
        .toLowerCase()
        .includes(searchTerm.toLowerCase());

      // Apply selected filters
      const filtersMatch = Object.keys(selectedFilters).every((key) =>
        selectedFilters[key] ? alum[key] === selectedFilters[key] : true
      );

      return nameMatch && filtersMatch;
    });
  }, [searchTerm, selectedFilters, alumni]);

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setSelectedFilters((prev) => ({ ...prev, [name]: value }));
  };

  return (
    <div className="alumni-page">
      <div className="back-buttons-container">
        <Link to="/dashboard" className="back-to-dashboard-student">
          Dashboard
        </Link>
      </div>
      <h1 className="alumni-list-heading">Alumni List</h1>
      <h3 className="alumni-count">Total Alumni: {filteredAlumni.length}</h3>
      <input
        type="text"
        placeholder="Search by name"
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        className="search-input-alumni"
      />
      <div className="filters">
        {Object.keys(filterValues).map((key) => (
          <select
            key={key}
            name={key}
            value={selectedFilters[key]}
            onChange={handleFilterChange}
            className="filter-dropdown"
          >
            <option value="">{`Select ${key}`}</option>
            {Array.isArray(filterValues[key]) &&
              filterValues[key].map((value) => (
                <option key={value} value={value}>
                  {value}
                </option>
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
