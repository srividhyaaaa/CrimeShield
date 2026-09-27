# CrimeShield

## Intelligent Anonymous Crime Reporting and Incident Pattern Analysis System

CrimeShield is an Android-based crime reporting and incident analysis application designed to provide a secure and simple platform for reporting incidents anonymously and analyzing reported crime patterns.

The application allows users to submit incident details and provides dashboards and pattern analysis based on the collected reports.

## Features

- Anonymous crime incident reporting
- Crime category selection
- Location and incident description
- Severity classification
- Firebase Cloud Firestore database
- Incident dashboard
- Search and filtering
- High-risk incident identification
- Most reported crime analysis
- Most reported area analysis
- Pattern analysis
- Dark professional user interface
- Simple navigation between screens

## Application Screens

### Home
Provides access to:
- Report an Incident
- Incident Dashboard
- Pattern Analysis

### Report Incident
Users can submit:
- Crime type
- Location
- Severity
- Incident description

Submitted reports are stored in Firebase Cloud Firestore.

### Incident Dashboard
Displays:
- Total reports
- High-risk incidents
- Most reported crime
- Most reported area
- Recent incidents

The dashboard also provides search and filtering options.

### Pattern Analysis
Analyzes collected incident reports and displays:
- Total reports
- High-risk incidents
- Most reported crime
- Most reported area
- Risk analysis

## Technology Stack

- **Language:** Java
- **UI:** XML
- **Platform:** Android
- **IDE:** Android Studio
- **Database:** Firebase Cloud Firestore
- **Build System:** Gradle

## Firebase Database

Crime reports are stored in the Firestore collection:

```text
reports
```

Each report contains:

```text
crimeType
location
severity
description
date
status
```

## Application Flow

```text
Home
 │
 ├── Report Incident
 │       │
 │       └── Save Report → Firebase Firestore
 │
 ├── Incident Dashboard
 │       │
 │       └── Read & Filter Reports
 │
 └── Pattern Analysis
         │
         └── Analyze Reports
```

## Project Structure

```text
CrimeShield
│
├── app
│   ├── src
│   │   └── main
│   │       ├── java
│   │       │   └── com.example.crimeshield
│   │       │       ├── MainActivity.java
│   │       │       ├── ReportActivity.java
│   │       │       ├── DashboardActivity.java
│   │       │       └── AnalysisActivity.java
│   │       │
│   │       └── res
│   │           ├── layout
│   │           ├── drawable
│   │           └── values
│   │
│   └── google-services.json
│
└── README.md
```

## Crime Categories

The application supports different crime categories through the incident reporting form.

Examples include:
- Theft
- Cyber Crime
- Harassment
- Assault
- Vandalism
- Other



## Future Enhancements

Possible future improvements include:

- Anonymous user authentication
- Admin dashboard
- Real-time notifications
- Advanced crime trend visualization
- Geographic crime visualization
- Improved incident verification
- More detailed analytics
- Secure production-level Firestore rules

## Objective

The main objective of CrimeShield is to provide a simple digital platform for reporting incidents while using collected reports to identify common crime patterns and high-risk areas.

## Disclaimer

CrimeShield is an academic/project application developed for educational and demonstration purposes. It should not be considered a replacement for official emergency services or law-enforcement reporting systems.


