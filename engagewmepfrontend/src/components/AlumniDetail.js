import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';
import './AlumniDetail.css';

const AlumniDetail = () => {
  const { id } = useParams();
  const [alumni, setAlumni] = useState(null);

  useEffect(() => {
    const fetchAlumni = async () => {
      try {
        const response = await axios.get(`/api/alumni/${id}`);
        setAlumni(response.data);
      } catch (error) {
        console.error('Error fetching alumni details', error);
      }
    };

    fetchAlumni();
  }, [id]);

  if (!alumni) {
    return <div>Loading...</div>;
  }

  return (
    <div className="alumni-detail-page">
      <div className="alumni-detail-card">
        <h1 className="alumni-name">{alumni.firstName} {alumni.lastName}</h1>
        <div className="alumni-info">
          <p><strong>Email:</strong> {alumni.emailAddress}</p>
          <p><strong>Personal Email:</strong> {alumni.personalEmailAddress}</p>
          <p><strong>Race/Ethnicity:</strong> {alumni.raceEthnicity}</p>
          <p><strong>Gender:</strong> {alumni.gender}</p>
          <p><strong>Identity Information:</strong> {alumni.identityInformationForConnection}</p>
          <p><strong>NCSU Graduate:</strong> {alumni.ncsuGraduate}</p>
          <p><strong>College of Engineering Graduate:</strong> {alumni.collegeOfEngineeringGraduate}</p>
          <p><strong>Year of Graduation:</strong> {alumni.yearOfGraduation}</p>
          <p><strong>STP Participation and Year:</strong> {alumni.stpParticipationAndYear}</p>
          <p><strong>Major:</strong> {alumni.major}</p>
          <p><strong>Current Employer:</strong> {alumni.currentEmployer}</p>
          <p><strong>Current Job Title:</strong> {alumni.currentJobTitle}</p>
          <p><strong>Current City:</strong> {alumni.currentCity}</p>
          <p><strong>Current State:</strong> {alumni.currentState}</p>
          <p><strong>Current Zip Code:</strong> {alumni.currentZipCode}</p>
          <p><strong>Post Graduation Achievements:</strong> {alumni.postGraduationAchievements}</p>
          <p><strong>Email Opt-In for MEP Program Updates:</strong> {alumni.emailOptInForMepProgramUpdates}</p>
          <p><strong>Engagement Opportunities:</strong> {alumni.engagementOpportunitiesForNcsuMepAlumniAndSupporters}</p>
          <p><strong>Mentoring Opt-In:</strong> {alumni.mentoringOptIn}</p>
        </div>
        <Link className="back-link" to="/alumni">Back to Alumni List</Link>
      </div>
    </div>
  );
};

export default AlumniDetail;
