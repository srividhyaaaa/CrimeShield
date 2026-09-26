package com.example.crimeshield;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    Spinner spinnerCrime;
    Spinner spinnerSeverity;

    EditText etLocation;
    EditText etDescription;

    Button btnSubmit;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        spinnerCrime = findViewById(R.id.spinnerCrime);
        spinnerSeverity = findViewById(R.id.spinnerSeverity);

        etLocation = findViewById(R.id.etLocation);
        etDescription = findViewById(R.id.etDescription);

        btnSubmit = findViewById(R.id.btnSubmit);

        // Connect to Firebase Firestore
        firestore = FirebaseFirestore.getInstance();

        setupCrimeSpinner();
        setupSeveritySpinner();

        btnSubmit.setOnClickListener(v -> submitReport());
    }

    private void setupCrimeSpinner() {

        String[] crimeTypes = {
                "Select Crime Type",
                "Theft",
                "Fraud",
                "Assault",
                "Cyber Crime",
                "Harassment",
                "Vandalism",
                "Other"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_item,
                        crimeTypes) {

                    @NonNull
                    @Override
                    public View getView(
                            int position,
                            View convertView,
                            android.view.ViewGroup parent) {

                        TextView text =
                                (TextView) super.getView(
                                        position,
                                        convertView,
                                        parent
                                );

                        text.setTextColor(Color.WHITE);
                        text.setTextSize(16);

                        return text;
                    }
                };

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerCrime.setAdapter(adapter);
    }

    private void setupSeveritySpinner() {

        String[] severityLevels = {
                "Select Severity",
                "Low",
                "Medium",
                "High",
                "Critical"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_item,
                        severityLevels) {

                    @NonNull
                    @Override
                    public View getView(
                            int position,
                            View convertView,
                            android.view.ViewGroup parent) {

                        TextView text =
                                (TextView) super.getView(
                                        position,
                                        convertView,
                                        parent
                                );

                        text.setTextColor(Color.WHITE);
                        text.setTextSize(16);

                        return text;
                    }
                };

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerSeverity.setAdapter(adapter);
    }

    private void submitReport() {

        String crimeType =
                spinnerCrime.getSelectedItem().toString();

        String severity =
                spinnerSeverity.getSelectedItem().toString();

        String location =
                etLocation.getText().toString().trim();

        String description =
                etDescription.getText().toString().trim();

        // Validate crime type
        if (crimeType.equals("Select Crime Type")) {

            Toast.makeText(
                    this,
                    "Please select a crime type",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Validate location
        if (location.isEmpty()) {

            etLocation.setError(
                    "Enter the incident location"
            );

            etLocation.requestFocus();

            return;
        }

        // Validate severity
        if (severity.equals("Select Severity")) {

            Toast.makeText(
                    this,
                    "Please select severity",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Validate description
        if (description.isEmpty()) {

            etDescription.setError(
                    "Describe the incident"
            );

            etDescription.requestFocus();

            return;
        }

        // Create date
        String date =
                new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm",
                        Locale.getDefault()
                ).format(new Date());

        // Create Firestore document data
        Map<String, Object> report =
                new HashMap<>();

        report.put("crimeType", crimeType);
        report.put("location", location);
        report.put("severity", severity);
        report.put("description", description);
        report.put("date", date);
        report.put("status", "Pending");

        // Save report to Firestore
        firestore.collection("reports")
                .add(report)
                .addOnSuccessListener(documentReference -> {

                    Toast.makeText(
                            ReportActivity.this,
                            "Anonymous report submitted successfully",
                            Toast.LENGTH_LONG
                    ).show();

                    // Clear form
                    etLocation.setText("");
                    etDescription.setText("");

                    spinnerCrime.setSelection(0);
                    spinnerSeverity.setSelection(0);
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            ReportActivity.this,
                            "Failed to submit report: "
                                    + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
}