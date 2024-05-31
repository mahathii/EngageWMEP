package com.engagewmep.querystudentdata.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "alumni")
public class Alumni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "personal_email_address")
    private String personalEmailAddress;

    @Column(name = "race_ethnicity")
    private String raceEthnicity;

    @Column(name = "gender")
    private String gender;

    @Column(name = "identity_information_for_connection", length = 1024)
    private String identityInformationForConnection;

    @Column(name = "ncsu_graduate")
    private String ncsuGraduate;

    @Column(name = "college_of_engineering_graduate")
    private String collegeOfEngineeringGraduate;

    @Column(name = "year_of_graduation")
    private String yearOfGraduation;

    @Column(name = "stp_participation_and_year")
    private String stpParticipationAndYear;

    @Column(name = "major")
    private String major;

    @Column(name = "current_employer")
    private String currentEmployer;

    @Column(name = "current_job_title")
    private String currentJobTitle;

    @Column(name = "current_city")
    private String currentCity;

    @Column(name = "current_state")
    private String currentState;

    @Column(name = "current_zip_code")
    private String currentZipCode;

    @Lob
    @Column(name = "post_graduation_achievements")
    private String postGraduationAchievements;

    @Column(name = "email_opt_in_for_mep_program_updates")
    private String emailOptInForMepProgramUpdates;

    @Column(name = "engagement_opportunities_for_ncsu_mep_alumni_and_supporters", length = 1024)
    private String engagementOpportunitiesForNcsuMepAlumniAndSupporters;

    @Column(name = "mentoring_opt_in")
    private String mentoringOptIn;
}
