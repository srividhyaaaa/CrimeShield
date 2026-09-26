package com.example.crimeshield;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    TextView tvTotalReports;
    TextView tvHighRisk;
    TextView tvCommonCrime;
    TextView tvCommonArea;
    TextView tvRecentIncidents;
    EditText etSearch;
    Spinner spinnerFilterCrime;
    Spinner spinnerFilterSeverity;
    ScrollView dashboardScroll;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dashboardScroll = findViewById(R.id.dashboardScroll);
        
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        tvTotalReports = findViewById(R.id.tvTotalReports);
        tvHighRisk = findViewById(R.id.tvHighRisk);
        tvCommonCrime = findViewById(R.id.tvCommonCrime);
        tvCommonArea = findViewById(R.id.tvCommonArea);
        tvRecentIncidents = findViewById(R.id.tvRecentIncidents);
        
        etSearch = findViewById(R.id.etSearch);
        spinnerFilterCrime = findViewById(R.id.spinnerFilterCrime);
        spinnerFilterSeverity = findViewById(R.id.spinnerFilterSeverity);
        
        db = new DatabaseHelper(this);

        setupSpinner(spinnerFilterCrime, new String[]{
                "All Crime Types", 
                "Theft", 
                "Fraud", 
                "Assault", 
                "Cyber Crime", 
                "Harassment", 
                "Vandalism", 
                "Other"
        });
        
        setupSpinner(spinnerFilterSeverity, new String[]{
                "All Severity", 
                "Low", 
                "Medium", 
                "High", 
                "Critical"
        });

        findViewById(R.id.btnApplyFilter).setOnClickListener(v -> {
            applyFilter();
        });
        
        findViewById(R.id.btnClearFilter).setOnClickListener(v -> {
            etSearch.setText("");
            spinnerFilterCrime.setSelection(0);
            spinnerFilterSeverity.setSelection(0);
            applyFilter();
            dashboardScroll.fullScroll(View.FOCUS_UP);
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        
        loadDashboard();
    }

    private void setupSpinner(Spinner spinner, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items) {
            @Override
            public View getView(int p, View v, ViewGroup pr) {
                TextView t = (TextView) super.getView(p, v, pr);
                t.setTextColor(Color.WHITE);
                t.setTextSize(15);
                return t;
            }

            @Override
            public View getDropDownView(int p, View v, ViewGroup pr) {
                TextView t = (TextView) super.getDropDownView(p, v, pr);
                t.setTextColor(Color.WHITE);
                t.setTextSize(15);
                t.setBackgroundColor(Color.rgb(27, 32, 40));
                t.setPadding(16, 16, 16, 16);
                return t;
            }
        };
        spinner.setAdapter(adapter);
    }

    private void loadDashboard() {
        tvTotalReports.setText("TOTAL REPORTS\n" + getCount("SELECT COUNT(*) FROM reports"));
        
        tvHighRisk.setText("HIGH-RISK INCIDENTS\n" + getCount("SELECT COUNT(*) FROM reports WHERE severity IN ('High', 'Critical')"));
        
        tvCommonCrime.setText("MOST REPORTED CRIME\n" + getStringVal("SELECT crime_type FROM reports GROUP BY crime_type ORDER BY COUNT(*) DESC LIMIT 1"));
        
        tvCommonArea.setText("MOST REPORTED AREA\n" + getStringVal("SELECT location FROM reports GROUP BY location ORDER BY COUNT(*) DESC LIMIT 1"));
        
        applyFilter();
    }

    private int getCount(String q) {
        Cursor c = db.getReadableDatabase().rawQuery(q, null);
        int res = 0;
        if (c.moveToFirst()) {
            res = c.getInt(0);
        }
        c.close();
        return res;
    }

    private String getStringVal(String q) {
        Cursor c = db.getReadableDatabase().rawQuery(q, null);
        String res = "-";
        if (c.moveToFirst()) {
            res = c.getString(0);
        }
        c.close();
        return res;
    }

    private void applyFilter() {
        String s = etSearch.getText().toString().trim();
        String c = spinnerFilterCrime.getSelectedItem().toString();
        String sv = spinnerFilterSeverity.getSelectedItem().toString();
        
        StringBuilder q = new StringBuilder("SELECT crime_type, location, severity, description, date FROM reports WHERE 1=1");
        ArrayList<String> args = new ArrayList<>();
        
        if (!s.isEmpty()) {
            q.append(" AND (crime_type LIKE ? OR location LIKE ? OR description LIKE ?)");
            String v = "%" + s + "%";
            args.add(v);
            args.add(v);
            args.add(v);
        }
        
        if (!c.equals("All Crime Types")) {
            q.append(" AND crime_type = ?");
            args.add(c);
        }
        
        if (!sv.equals("All Severity")) {
            q.append(" AND severity = ?");
            args.add(sv);
        }
        
        q.append(" ORDER BY id DESC");
        
        Cursor cr = db.getReadableDatabase().rawQuery(q.toString(), args.toArray(new String[0]));
        StringBuilder sb = new StringBuilder();
        
        if (cr.getCount() == 0) {
            sb.append("No matching incidents found.");
        } else {
            while (cr.moveToNext()) {
                sb.append("Crime: ")
                  .append(cr.getString(0))
                  .append("\nLocation: ")
                  .append(cr.getString(1))
                  .append("\nSeverity: ")
                  .append(cr.getString(2))
                  .append("\nDescription: ")
                  .append(cr.getString(3))
                  .append("\nDate: ")
                  .append(cr.getString(4))
                  .append("\n--------------------\n\n");
            }
        }
        cr.close();
        tvRecentIncidents.setText(sb.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db != null) {
            loadDashboard();
        }
    }
}