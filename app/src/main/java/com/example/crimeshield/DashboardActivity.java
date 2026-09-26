package com.example.crimeshield;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    TextView tvTotalReports;
    TextView tvHighRisk;
    TextView tvCommonCrime;
    TextView tvCommonArea;
    TextView tvRecentIncidents;

    EditText etSearch;

    Spinner spinnerFilterCrime;
    Spinner spinnerFilterSeverity;

    Button btnApplyFilter;
    Button btnClearFilter;

    ScrollView dashboardScroll;

    FirebaseFirestore firestore;

    List<ReportModel> allReports = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        dashboardScroll = findViewById(R.id.dashboardScroll);

        tvTotalReports = findViewById(R.id.tvTotalReports);
        tvHighRisk = findViewById(R.id.tvHighRisk);
        tvCommonCrime = findViewById(R.id.tvCommonCrime);
        tvCommonArea = findViewById(R.id.tvCommonArea);
        tvRecentIncidents = findViewById(R.id.tvRecentIncidents);

        etSearch = findViewById(R.id.etSearch);

        spinnerFilterCrime =
                findViewById(R.id.spinnerFilterCrime);

        spinnerFilterSeverity =
                findViewById(R.id.spinnerFilterSeverity);

        btnApplyFilter =
                findViewById(R.id.btnApplyFilter);

        btnClearFilter =
                findViewById(R.id.btnClearFilter);

        firestore = FirebaseFirestore.getInstance();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        setupFilters();

        loadReportsFromFirebase();

        btnApplyFilter.setOnClickListener(
                v -> applyFilter()
        );

        btnClearFilter.setOnClickListener(
                v -> clearFilter()
        );
    }

    private void setupFilters() {

        String[] crimeTypes = {
                "All Crime Types",
                "Theft",
                "Fraud",
                "Assault",
                "Cyber Crime",
                "Harassment",
                "Vandalism",
                "Other"
        };

        ArrayAdapter<String> crimeAdapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_item,
                        crimeTypes) {

                    @Override
                    public View getView(
                            int position,
                            View convertView,
                            ViewGroup parent) {

                        TextView text =
                                (TextView) super.getView(
                                        position,
                                        convertView,
                                        parent
                                );

                        text.setTextColor(Color.WHITE);
                        text.setTextSize(15);

                        return text;
                    }

                    @Override
                    public View getDropDownView(
                            int position,
                            View convertView,
                            ViewGroup parent) {

                        TextView text =
                                (TextView) super.getDropDownView(
                                        position,
                                        convertView,
                                        parent
                                );

                        text.setTextColor(Color.WHITE);
                        text.setTextSize(15);

                        text.setBackgroundColor(
                                Color.rgb(27, 32, 40)
                        );

                        text.setPadding(
                                16,
                                16,
                                16,
                                16
                        );

                        return text;
                    }
                };

        crimeAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerFilterCrime.setAdapter(crimeAdapter);


        String[] severityLevels = {
                "All Severity",
                "Low",
                "Medium",
                "High",
                "Critical"
        };

        ArrayAdapter<String> severityAdapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_item,
                        severityLevels) {

                    @Override
                    public View getView(
                            int position,
                            View convertView,
                            ViewGroup parent) {

                        TextView text =
                                (TextView) super.getView(
                                        position,
                                        convertView,
                                        parent
                                );

                        text.setTextColor(Color.WHITE);
                        text.setTextSize(15);

                        return text;
                    }

                    @Override
                    public View getDropDownView(
                            int position,
                            View convertView,
                            ViewGroup parent) {

                        TextView text =
                                (TextView) super.getDropDownView(
                                        position,
                                        convertView,
                                        parent
                                );

                        text.setTextColor(Color.WHITE);
                        text.setTextSize(15);

                        text.setBackgroundColor(
                                Color.rgb(27, 32, 40)
                        );

                        text.setPadding(
                                16,
                                16,
                                16,
                                16
                        );

                        return text;
                    }
                };

        severityAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerFilterSeverity.setAdapter(severityAdapter);
    }

    private void loadReportsFromFirebase() {

        firestore.collection("reports")
                .orderBy(
                        "date",
                        Query.Direction.DESCENDING
                )
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    allReports.clear();

                    for (DocumentSnapshot document :
                            queryDocumentSnapshots.getDocuments()) {

                        String crimeType =
                                getString(document, "crimeType");

                        String location =
                                getString(document, "location");

                        String severity =
                                getString(document, "severity");

                        String description =
                                getString(document, "description");

                        String date =
                                getString(document, "date");

                        allReports.add(
                                new ReportModel(
                                        crimeType,
                                        location,
                                        severity,
                                        description,
                                        date
                                )
                        );
                    }

                    updateStatistics();

                    displayIncidents(allReports);
                })
                .addOnFailureListener(e -> {

                    tvRecentIncidents.setText(
                            "Unable to load incidents.\n\n"
                                    + e.getMessage()
                    );
                });
    }

    private String getString(
            DocumentSnapshot document,
            String field) {

        String value = document.getString(field);

        if (value == null) {
            return "-";
        }

        return value;
    }

    private void updateStatistics() {

        int totalReports =
                allReports.size();

        int highRisk =
                0;

        String commonCrime =
                "-";

        String commonArea =
                "-";

        java.util.HashMap<String, Integer>
                crimeCount =
                new java.util.HashMap<>();

        java.util.HashMap<String, Integer>
                areaCount =
                new java.util.HashMap<>();

        for (ReportModel report : allReports) {

            if (report.severity.equals("High")
                    || report.severity.equals("Critical")) {

                highRisk++;
            }

            crimeCount.put(
                    report.crimeType,
                    crimeCount.getOrDefault(
                            report.crimeType,
                            0
                    ) + 1
            );

            areaCount.put(
                    report.location,
                    areaCount.getOrDefault(
                            report.location,
                            0
                    ) + 1
            );
        }

        int highestCrimeCount = 0;

        for (String crime :
                crimeCount.keySet()) {

            int count =
                    crimeCount.get(crime);

            if (count > highestCrimeCount) {

                highestCrimeCount = count;
                commonCrime = crime;
            }
        }

        int highestAreaCount = 0;

        for (String area :
                areaCount.keySet()) {

            int count =
                    areaCount.get(area);

            if (count > highestAreaCount) {

                highestAreaCount = count;
                commonArea = area;
            }
        }

        tvTotalReports.setText(
                "TOTAL REPORTS\n"
                        + totalReports
        );

        tvHighRisk.setText(
                "HIGH-RISK INCIDENTS\n"
                        + highRisk
        );

        tvCommonCrime.setText(
                "MOST REPORTED CRIME\n"
                        + commonCrime
        );

        tvCommonArea.setText(
                "MOST REPORTED AREA\n"
                        + commonArea
        );
    }

    private void applyFilter() {

        String search =
                etSearch.getText()
                        .toString()
                        .trim()
                        .toLowerCase();

        String selectedCrime =
                spinnerFilterCrime
                        .getSelectedItem()
                        .toString();

        String selectedSeverity =
                spinnerFilterSeverity
                        .getSelectedItem()
                        .toString();

        List<ReportModel> filteredReports =
                new ArrayList<>();

        for (ReportModel report :
                allReports) {

            boolean matchesSearch =
                    search.isEmpty()
                            || report.crimeType
                            .toLowerCase()
                            .contains(search)
                            || report.location
                            .toLowerCase()
                            .contains(search)
                            || report.description
                            .toLowerCase()
                            .contains(search);

            boolean matchesCrime =
                    selectedCrime.equals(
                            "All Crime Types"
                    )
                            || report.crimeType.equals(
                            selectedCrime
                    );

            boolean matchesSeverity =
                    selectedSeverity.equals(
                            "All Severity"
                    )
                            || report.severity.equals(
                            selectedSeverity
                    );

            if (matchesSearch
                    && matchesCrime
                    && matchesSeverity) {

                filteredReports.add(report);
            }
        }

        displayIncidents(filteredReports);
    }

    private void clearFilter() {

        etSearch.setText("");

        spinnerFilterCrime.setSelection(0);

        spinnerFilterSeverity.setSelection(0);

        displayIncidents(allReports);

        dashboardScroll.fullScroll(
                View.FOCUS_UP
        );
    }

    private void displayIncidents(
            List<ReportModel> reports) {

        StringBuilder incidents =
                new StringBuilder();

        if (reports.isEmpty()) {

            incidents.append(
                    "No matching incidents found."
            );

        } else {

            for (ReportModel report :
                    reports) {

                incidents.append(
                                "Crime: "
                        )
                        .append(report.crimeType)
                        .append("\n");

                incidents.append(
                                "Location: "
                        )
                        .append(report.location)
                        .append("\n");

                incidents.append(
                                "Severity: "
                        )
                        .append(report.severity)
                        .append("\n");

                incidents.append(
                                "Description: "
                        )
                        .append(report.description)
                        .append("\n");

                incidents.append(
                                "Date: "
                        )
                        .append(report.date)
                        .append("\n");

                incidents.append(
                        "--------------------\n\n"
                );
            }
        }

        tvRecentIncidents.setText(
                incidents.toString()
        );
    }

    private static class ReportModel {

        String crimeType;
        String location;
        String severity;
        String description;
        String date;

        ReportModel(
                String crimeType,
                String location,
                String severity,
                String description,
                String date) {

            this.crimeType = crimeType;
            this.location = location;
            this.severity = severity;
            this.description = description;
            this.date = date;
        }
    }
}