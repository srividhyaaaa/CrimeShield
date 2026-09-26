package com.example.crimeshield;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {
    Spinner spinnerCrime;
    Spinner spinnerSeverity;
    EditText etLocation;
    EditText etDescription;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        
        db = new DatabaseHelper(this);
        
        spinnerCrime = findViewById(R.id.spinnerCrime);
        spinnerSeverity = findViewById(R.id.spinnerSeverity);
        etLocation = findViewById(R.id.etLocation);
        etDescription = findViewById(R.id.etDescription);
        
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        setupSpinner(spinnerCrime, new String[]{
                "Select Crime Type", 
                "Theft", 
                "Fraud", 
                "Assault", 
                "Cyber Crime", 
                "Harassment", 
                "Vandalism", 
                "Other"
        });
        
        setupSpinner(spinnerSeverity, new String[]{
                "Select Severity", 
                "Low", 
                "Medium", 
                "High", 
                "Critical"
        });
        
        findViewById(R.id.btnSubmit).setOnClickListener(v -> submitReport());
    }

    private void setupSpinner(Spinner s, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items) {
            @Override
            public View getView(int p, View v, ViewGroup pr) {
                TextView t = (TextView) super.getView(p, v, pr);
                t.setTextColor(Color.WHITE);
                t.setTextSize(16);
                return t;
            }

            @Override
            public View getDropDownView(int p, View v, ViewGroup pr) {
                TextView t = (TextView) super.getDropDownView(p, v, pr);
                t.setTextColor(Color.WHITE);
                t.setTextSize(16);
                t.setBackgroundColor(Color.rgb(27, 32, 40));
                t.setPadding(16, 16, 16, 16);
                return t;
            }
        };
        s.setAdapter(adapter);
    }

    private void submitReport() {
        String c = spinnerCrime.getSelectedItem().toString();
        String sv = spinnerSeverity.getSelectedItem().toString();
        String l = etLocation.getText().toString().trim();
        String d = etDescription.getText().toString().trim();
        
        if (c.equals("Select Crime Type") || sv.equals("Select Severity") || l.isEmpty() || d.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String dt = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        
        if (db.insertReport(c, l, sv, d, dt)) {
            Toast.makeText(this, "Anonymous report submitted successfully", Toast.LENGTH_LONG).show();
            etLocation.setText("");
            etDescription.setText("");
            spinnerCrime.setSelection(0);
            spinnerSeverity.setSelection(0);
        } else {
            Toast.makeText(this, "Failed to submit report", Toast.LENGTH_LONG).show();
        }
    }
}