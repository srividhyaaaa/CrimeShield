# CrimeShield

## Intelligent Anonymous Crime Reporting and Incident Pattern Analysis System

CrimeShield is an Android-based crime reporting and incident analysis application designed to provide a secure and simple platform for reporting incidents anonymously and analyzing reported crime patterns.

The application allows users to submit incident details and provides dashboards and pattern analysis based on the collected reports.

---

## Features

- Anonymous crime incident reporting
- Crime category selection
- Location and incident description
- Severity classification
- Firebase Firestore database
- Incident dashboard
- Search and filtering
- High-risk incident identification
- Most reported crime analysis
- Most reported area analysis
- Pattern analysis
- Dark professional user interface
- Simple navigation between screens

---

## Application Screens

### Home
Provides access to the main features of the application:

- Report an Incident
- Incident Dashboard
- Pattern Analysis

### Report Incident
Users can submit:

- Crime type
- Location
- Severity
- Incident description

Submitted reports are stored in Firebase Firestore.

### Incident Dashboard
Displays information such as:

- Total reports
- High-risk incidents
- Most reported crime
- Most reported area
- Recent incidents

The dashboard also provides search and filtering options.

### Pattern Analysis
Analyzes the collected incident reports and displays:

- Total reports
- High-risk incidents
- Most reported crime
- Most reported area
- Risk analysis

---

## Technology Stack

- **Language:** Java
- **UI:** XML
- **Platform:** Android
- **IDE:** Android Studio
- **Database:** Firebase Cloud Firestore
- **Build System:** Gradle
- **Minimum/Target SDK:** Android SDK

---

## Firebase Database

Crime reports are stored in the Firestore collection:

```text
reports
