import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./AlumniForm.css";

const AlumniForm = () => {
  const [formData, setFormData] = useState({
    emailAddress: "",
    firstName: "",
    lastName: "",
    personalEmailAddress: "",
    raceEthnicity: "",
    gender: "",
    identityInformationForConnection: "",
    ncsuGraduate: "",
    collegeOfEngineeringGraduate: "",
    yearOfGraduation: "",
    stpParticipationAndYear: "",
    major: "",
    currentEmployer: "",
    currentJobTitle: "",
    currentCity: "",
    currentState: "",
    currentZipCode: "",
    postGraduationAchievements: "",
    emailOptInForMepProgramUpdates: "",
    engagementOpportunitiesForNcsuMepAlumniAndSupporters: "",
    mentoringOptIn: "",
  });

  const [successMessage, setSuccessMessage] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post("/api/alumni", formData);
      console.log("Alumni added:", response.data);
      setSuccessMessage("User is successfully added to alumni database");
    } catch (error) {
      console.error("Error adding alumni", error);
    }
  };

  return (
    <div className="alumni-form-page">
      <button className="back-button" onClick={() => navigate("/")}>
        Back to Alumni Search
      </button>
      <div className="alumni-form-container">
        <form className="alumni-form" onSubmit={handleSubmit}>
          <input
            type="text"
            name="firstName"
            placeholder="First Name"
            value={formData.firstName}
            onChange={handleChange}
          />
          <input
            type="text"
            name="lastName"
            placeholder="Last Name"
            value={formData.lastName}
            onChange={handleChange}
          />
          <input
            type="email"
            name="emailAddress"
            placeholder="Email Address"
            value={formData.emailAddress}
            onChange={handleChange}
          />
          <input
            type="email"
            name="personalEmailAddress"
            placeholder="Personal Email Address"
            value={formData.personalEmailAddress}
            onChange={handleChange}
          />
          <input
            type="text"
            name="raceEthnicity"
            placeholder="Race/Ethnicity"
            value={formData.raceEthnicity}
            onChange={handleChange}
          />
          <input
            type="text"
            name="gender"
            placeholder="Gender"
            value={formData.gender}
            onChange={handleChange}
          />
          <input
            type="text"
            name="identityInformationForConnection"
            placeholder="Identity Information"
            value={formData.identityInformationForConnection}
            onChange={handleChange}
          />
          <input
            type="text"
            name="ncsuGraduate"
            placeholder="NCSU Graduate"
            value={formData.ncsuGraduate}
            onChange={handleChange}
          />
          <input
            type="text"
            name="collegeOfEngineeringGraduate"
            placeholder="College of Engineering Graduate"
            value={formData.collegeOfEngineeringGraduate}
            onChange={handleChange}
          />
          <input
            type="text"
            name="yearOfGraduation"
            placeholder="Year of Graduation"
            value={formData.yearOfGraduation}
            onChange={handleChange}
          />
          <input
            type="text"
            name="stpParticipationAndYear"
            placeholder="STP Participation and Year"
            value={formData.stpParticipationAndYear}
            onChange={handleChange}
          />
          <input
            type="text"
            name="major"
            placeholder="Major"
            value={formData.major}
            onChange={handleChange}
          />
          <input
            type="text"
            name="currentEmployer"
            placeholder="Current Employer"
            value={formData.currentEmployer}
            onChange={handleChange}
          />
          <input
            type="text"
            name="currentJobTitle"
            placeholder="Current Job Title"
            value={formData.currentJobTitle}
            onChange={handleChange}
          />
          <input
            type="text"
            name="currentCity"
            placeholder="Current City"
            value={formData.currentCity}
            onChange={handleChange}
          />
          <input
            type="text"
            name="currentState"
            placeholder="Current State"
            value={formData.currentState}
            onChange={handleChange}
          />
          <input
            type="text"
            name="currentZipCode"
            placeholder="Current Zip Code"
            value={formData.currentZipCode}
            onChange={handleChange}
          />
          <input
            type="text"
            name="postGraduationAchievements"
            placeholder="Post Graduation Achievements"
            value={formData.postGraduationAchievements}
            onChange={handleChange}
          />
          <input
            type="text"
            name="emailOptInForMepProgramUpdates"
            placeholder="Email Opt-In for MEP Program Updates"
            value={formData.emailOptInForMepProgramUpdates}
            onChange={handleChange}
          />
          <input
            type="text"
            name="engagementOpportunitiesForNcsuMepAlumniAndSupporters"
            placeholder="Engagement Opportunities for NCSU MEP Alumni and Supporters"
            value={formData.engagementOpportunitiesForNcsuMepAlumniAndSupporters}
            onChange={handleChange}
          />
          <input
            type="text"
            name="mentoringOptIn"
            placeholder="Mentoring Opt-In"
            value={formData.mentoringOptIn}
            onChange={handleChange}
          />
          <button className="submit-button" type="submit">
            Submit
          </button>
        </form>
        {successMessage && <div className="success-message">{successMessage}</div>}
      </div>
    </div>
  );
};

export default AlumniForm;
