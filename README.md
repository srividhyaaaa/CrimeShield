# CrimeShield

## Intelligent Anonymous Crime Reporting and Incident Pattern Analysis System

CrimeShield is an Android-based application designed to provide a secure and anonymous platform for reporting incidents and analyzing crime patterns.

The application allows users to submit crime reports without revealing their identity and provides a dashboard for viewing incident statistics, searching reports, filtering incidents, and identifying common crime patterns.

---

## Features

### 🔐 Anonymous Crime Reporting
- Submit crime reports without providing personal identity.
- Select the crime type and severity.
- Enter the incident location and description.
- Validate required information before submission.

### 📊 Incident Dashboard
- View the total number of reported incidents.
- Display high-risk incidents.
- Identify the most frequently reported crime.
- Identify commonly reported areas.
- View recent incident reports.
- Search incidents using keywords.
- Filter reports by crime type and severity.

### 📈 Pattern Analysis
- Analyze crime distribution.
- Identify frequently occurring crime types.
- Analyze incident severity.
- Identify commonly reported locations.
- Present useful crime statistics for better understanding of incident patterns.

### 💾 Local Database
- Uses SQLite for storing incident reports.
- Stores crime type, location, severity, description, date, and status.
- Dashboard and analysis screens retrieve data directly from the database.

### 🎨 Professional User Interface
- Dark-themed safety and security design.
- Simple navigation.
- Consistent layouts and buttons.
- Back navigation on application screens.
- Scrollable dashboard for viewing multiple sections.

---

## Technology Stack

| Technology | Purpose |
|------------|---------|
| Java | Application development |
| XML | User interface design |
| Android Studio | Development environment |
| SQLite | Local database |
| Gradle | Project build system |

---

## Application Flow

Home Screen
     |
     ├── Report an Incident
     |       |
     |       └── Submit Report
     |               |
     |               └── SQLite Database
     |
     ├── Incident Dashboard
     |       |
     |       ├── Statistics
     |       ├── Search
     |       ├── Crime Filter
     |       ├── Severity Filter
     |       └── Recent Incidents
     |
     └── Pattern Analysis
             |
             ├── Crime Patterns
             ├── Severity Analysis
             └── Location Patterns
