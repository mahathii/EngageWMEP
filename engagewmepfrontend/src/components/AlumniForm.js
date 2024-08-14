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

  const [otherRaceEthnicity, setOtherRaceEthnicity] = useState("");

  

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleRaceEthnicityChange = (e) => {
    const { value } = e.target;
    setFormData({
      ...formData,
      raceEthnicity: value,
    });

    if (value !== "Other") {
      setOtherRaceEthnicity("");
    }
  };

  const handleOtherChange = (e) => {
    const { value } = e.target;
    setOtherRaceEthnicity(value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const finalFormData = {
      ...formData,
      raceEthnicity: formData.raceEthnicity === "Other" ? otherRaceEthnicity : formData.raceEthnicity,
    };

    try {
      const response = await axios.post("/api/alumni", finalFormData);
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
          <label><span style={{ color: 'red' }}>*</span> Indicates required question</label>
          <label>First Name: <span style={{ color: 'red' }}>*</span></label>
          <input
            type="text"
            name="firstName"
            placeholder="First Name"
            value={formData.firstName}
            onChange={handleChange}
            required
          />
          <label>Last Name: <span style={{ color: 'red' }}>*</span></label>
          <input
            type="text"
            name="lastName"
            placeholder="Last Name"
            value={formData.lastName}
            onChange={handleChange}
            required
          />
          <label>Email Address: <span style={{ color: 'red' }}>*</span> </label>
          <input
            type="email"
            name="emailAddress"
            placeholder="Email Address"
            value={formData.emailAddress}
            onChange={handleChange}
            required
          />
          <label>Personal Email Address:</label>
          <input
            type="email"
            name="personalEmailAddress"
            placeholder="Personal Email Address"
            value={formData.personalEmailAddress}
            onChange={handleChange}
          />
          <label>Race/Ethnicity (choose all that apply): <span style={{ color: 'red' }}>*</span></label>
          <select
            name="raceEthnicity"
            value={formData.raceEthnicity}
            onChange={handleRaceEthnicityChange}
            required
          >
            <option value="">Select</option>
            <option value="African-American/Black">African-American/Black</option>
            <option value="Asian American, Pacific Islander, or Native Hawaiian">
              Asian American, Pacific Islander, or Native Hawaiian
            </option>
            <option value="Caucasian/White">Caucasian/White</option>
            <option value="Hispanic/Latinx">Hispanic/Latinx</option>
            <option value="Native American">Native American</option>
            <option value="Prefer not to share">Prefer not to share</option>
            <option value="Other">Other</option>
          </select>
          {formData.raceEthnicity === "Other" && (
            <input
              type="text"
              name="otherRaceEthnicity"
              placeholder="Please specify"
              value={otherRaceEthnicity}
              onChange={handleOtherChange}
            />
          )}
          <label>What is your gender? <span style={{ color: 'red' }}>*</span></label>
          <select
            name="gender"
            value={formData.gender}
            onChange={handleChange}
            required
          >
            <option value="">Select</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
            <option value="Non-Binary">Non-Binary</option>
            <option value="Prefer not to say">Prefer not to say</option>
          </select>
          <label>
            Is there any other identity information you want to share with us to
            be able to connect students who share similar backgrounds?
          </label>
          <input
            type="text"
            name="identityInformationForConnection"
            placeholder="Identity Information"
            value={formData.identityInformationForConnection}
            onChange={handleChange}
          />
          <label>Are you an NCSU graduate? <span style={{ color: 'red' }}>*</span></label>
          <select
            name="ncsuGraduate"
            value={formData.ncsuGraduate}
            onChange={handleChange}
            required
          >
            <option value="">Select</option>
            <option value="Yes">Yes</option>
            <option value="No">No</option>
          </select>
          <label>Are you a College of Engineering graduate? <span style={{ color: 'red' }}>*</span></label>

          <select
            name="collegeOfEngineeringGraduate"
            value={formData.collegeOfEngineeringGraduate}
            onChange={handleChange}
            required
          >
            <option value="">Select</option>
            <option value="Yes">Yes</option>
            <option value="No">No</option>
          </select>
          <label>
            What year did you graduate from NCSU (Note: please write the full
            year. For ex., 1974). Skip this question if you are not an NCSU
            graduate.
          </label>
          <input
            type="text"
            name="yearOfGraduation"
            placeholder="Year of Graduation"
            value={formData.yearOfGraduation}
            onChange={handleChange}
          />
          <label>
            As a student, did you participate in the Summer Transition Program
            (STP) at NCSU? If so, which year? (Note: if not, just reply "No")?
            <span style={{ color: 'red' }}>*</span>
          </label>
          <input
            type="text"
            name="stpParticipationAndYear"
            placeholder="STP Participation and Year"
            value={formData.stpParticipationAndYear}
            onChange={handleChange}
            required
          />
          <label>
            If you graduated with an engineering degree, what major did you
            graduate in? <span style={{ color: 'red' }}>*</span>
          </label>
          <select
            name="major"
            value={formData.major}
            onChange={handleChange}
            required
          >
            <option value="">Select</option>
            <option value="None. I am not an Engineering graduate.">None. I am not an Engineering graduate.</option>
            <option value="Aerospace">Aerospace</option>
            <option value="Biological">Biological</option>
            <option value="Biomedical">Biomedical</option>
            <option value="Chemical">Chemical</option>
            <option value="Civil">Civil</option>
            <option value="Computer Engineering">Computer Engineering</option>
            <option value="Computer Science">Computer Science</option>
            <option value="Construction">Construction</option>
            <option value="Electrical">Electrical</option>
            <option value="Industrial and Systems">Industrial and Systems</option>
            <option value="Material Science">Material Science</option>
            <option value="Mechanical">Mechanical</option>
            <option value="Mechanical Engineering Systems (site-based)">Mechanical Engineering Systems (site-based)</option>
            <option value="Mechatronics (site-based)">Mechatronics (site-based)</option>
            <option value="Paper Science">Paper Science</option>
            <option value="Textile">Textile</option>
          </select>
          <label>
            Current employer? (If self-employed you can provide write "self" or
            provide your company name.)
          </label>
          <input
            type="text"
            name="currentEmployer"
            placeholder="Current Employer"
            value={formData.currentEmployer}
            onChange={handleChange}
          />
          <label>Current job title?</label>
          <input
            type="text"
            name="currentJobTitle"
            placeholder="Current Job Title"
            value={formData.currentJobTitle}
            onChange={handleChange}
          />
          <label>What city do you live in currently? <span style={{ color: 'red' }}>*</span></label>
          <input
            type="text"
            name="currentCity"
            placeholder="Current City"
            value={formData.currentCity}
            onChange={handleChange}
            required
          />
          <label>What state do you live in currently? <span style={{ color: 'red' }}>*</span></label>
          <input
            type="text"
            name="currentState"
            placeholder="Current State"
            value={formData.currentState}
            onChange={handleChange}
            required
          />
          <label>What zip code do you live in currently?</label>
          <input
            type="text"
            name="currentZipCode"
            placeholder="Current Zip Code"
            value={formData.currentZipCode}
            onChange={handleChange}
          />
          <label>
            (Optional) Have you received any awards, honors, or recognitions
            since graduating that you could share with us? (Note: we ask this
            because we want to share the success of our alums with our students
            in the future)
          </label>
          <input
            type="text"
            name="postGraduationAchievements"
            placeholder="Post Graduation Achievements"
            value={formData.postGraduationAchievements}
            onChange={handleChange}
          />
          <label>
            Permission: Do you give us permission to email you MEP programmatic
            updates? Note: The frequency of our emails will not exceed 1 email
            per week at the maximum. We will only email occasional program/event
            updates and requests for support/ideas/feedback.
            <span style={{ color: 'red' }}>*</span>
          </label>
          <select
            name="emailOptInForMepProgramUpdates"
            value={formData.emailOptInForMepProgramUpdates}
            onChange={handleChange}
            required
          >
            <option value="">Select</option>
            <option value="Yes">Yes</option>
            <option value="No">No</option>
          </select>
          <label>
            Would you be open to be contacted with opportunities for alumni and
            supporters to engage students at NCSU MEP programs and events?
          </label>
          {/* <input
            type="text"
            name="engagementOpportunitiesForNcsuMepAlumniAndSupporters"
            placeholder="Engagement Opportunities for NCSU MEP Alumni and Supporters"
            value={formData.engagementOpportunitiesForNcsuMepAlumniAndSupporters}
            onChange={handleChange}
          /> */}
          <select
            name="engagementOpportunitiesForNcsuMepAlumniAndSupporters"
            value={formData.engagementOpportunitiesForNcsuMepAlumniAndSupporters}
            onChange={handleChange}
          >
            <option value="">Select</option>
            <option value="Yes">Yes</option>
            <option value="No">No</option>
          </select>
          <label>
            Mentoring Opt-In: Would you be open to being contacted regarding
            opportunities to mentor NCSU students in the future, who might want
            to work in your area of expertise, with your company, or who are
            considering taking a job in the same city you live/work in?
          </label>
          {/* <input
            type="text"
            name="mentoringOptIn"
            placeholder="Mentoring Opt-In"
            value={formData.mentoringOptIn}
            onChange={handleChange}
          /> */}
          <select
            name="mentoringOptIn"
            value={formData.mentoringOptIn}
            onChange={handleChange}
          >
            <option value="">Select</option>
            <option value="Yes">Yes</option>
            <option value="No">No</option>
          </select>
          <button className="submit-button" type="submit">
            Submit
          </button>
        </form>
        {successMessage && (
          <div className="success-message">{successMessage}</div>
        )}
      </div>
    </div>
  );
};

export default AlumniForm;
