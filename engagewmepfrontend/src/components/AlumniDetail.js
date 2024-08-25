import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import jsPDF from 'jspdf';
import 'jspdf-autotable';
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

  const generatePDF = () => {
    if (!alumni) return;

    const doc = new jsPDF();

    // Title
    doc.setFontSize(20);
    doc.text(`${alumni.firstName} ${alumni.lastName} - Alumni Details`, 10, 10);

    // Alumni Details
    const details = [
      { label: 'Email', value: alumni.emailAddress },
      { label: 'Personal Email', value: alumni.personalEmailAddress },
      { label: 'Race/Ethnicity', value: alumni.raceEthnicity },
      { label: 'Gender', value: alumni.gender },
      { label: 'Identity Information', value: alumni.identityInformationForConnection },
      { label: 'NCSU Graduate', value: alumni.ncsuGraduate },
      { label: 'College of Engineering Graduate', value: alumni.collegeOfEngineeringGraduate },
      { label: 'Year of Graduation', value: alumni.yearOfGraduation },
      { label: 'STP Participation and Year', value: alumni.stpParticipationAndYear },
      { label: 'Major', value: alumni.major },
      { label: 'Current Employer', value: alumni.currentEmployer },
      { label: 'Current Job Title', value: alumni.currentJobTitle },
      { label: 'Current City', value: alumni.currentCity },
      { label: 'Current State', value: alumni.currentState },
      { label: 'Current Zip Code', value: alumni.currentZipCode },
      { label: 'Post Graduation Achievements', value: alumni.postGraduationAchievements },
      { label: 'Email Opt-In for MEP Program Updates', value: alumni.emailOptInForMepProgramUpdates },
      { label: 'Engagement Opportunities', value: alumni.engagementOpportunitiesForNcsuMepAlumniAndSupporters },
      { label: 'Mentoring Opt-In', value: alumni.mentoringOptIn },
    ];

    doc.setFontSize(12);

    details.forEach((detail, index) => {
      doc.text(`${detail.label}: ${detail.value || 'N/A'}`, 10, 30 + index * 10);
    });

    doc.save(`${alumni.firstName}_${alumni.lastName}_AlumniDetails.pdf`);
  };

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
        <button onClick={generatePDF} className="alumni-button">Download as PDF</button>
        <button onClick={() => window.location.href = '/alumni'} className="alumni-button">
  Back to Alumni List
</button>

      </div>
    </div>
  );
};

export default AlumniDetail;
