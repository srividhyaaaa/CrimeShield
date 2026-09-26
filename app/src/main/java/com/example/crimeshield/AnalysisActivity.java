package com.example.crimeshield;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class AnalysisActivity extends AppCompatActivity {
    TextView tvTotal;
    TextView tvHighRisk;
    TextView tvTopCrime;
    TextView tvTopArea;
    TextView tvAnalysis;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        
        db = new DatabaseHelper(this);
        
        tvTotal = findViewById(R.id.tvTotal);
        tvHighRisk = findViewById(R.id.tvHighRisk);
        tvTopCrime = findViewById(R.id.tvTopCrime);
        tvTopArea = findViewById(R.id.tvTopArea);
        tvAnalysis = findViewById(R.id.tvAnalysis);
        
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        analyzeData();
    }

    private void analyzeData() {
        int total = getCount("SELECT COUNT(*) FROM reports");
        int high = getCount("SELECT COUNT(*) FROM reports WHERE severity IN ('High', 'Critical')");
        
        String topC = getVal("SELECT crime_type FROM reports GROUP BY crime_type ORDER BY COUNT(*) DESC LIMIT 1");
        String topA = getVal("SELECT location FROM reports GROUP BY location ORDER BY COUNT(*) DESC LIMIT 1");
        
        tvTotal.setText("TOTAL INCIDENTS\n" + total);
        tvHighRisk.setText("HIGH-RISK INCIDENTS\n" + high);
        tvTopCrime.setText("DOMINANT CRIME TYPE\n" + topC);
        tvTopArea.setText("HIGH-ACTIVITY AREA\n" + topA);

        if (total == 0) {
            tvAnalysis.setText("PATTERN SUMMARY\n\nNo data available.");
            return;
        }
        
        double p = (high * 100.0) / total;
        String level = p >= 60 ? "HIGH" : (p >= 30 ? "MODERATE" : "LOW");
        String insight = p >= 60 ? "High percentage of high-risk incidents. Attention needed." : (p >= 30 ? "Moderate level of high-risk incidents. Monitoring needed." : "Most incidents are currently below high-risk.");

        StringBuilder dist = new StringBuilder("\nCRIME DISTRIBUTION:\n");
        Cursor c = db.getReadableDatabase().rawQuery("SELECT crime_type, COUNT(*) FROM reports GROUP BY crime_type ORDER BY COUNT(*) DESC", null);
        while (c.moveToNext()) {
            dist.append("• ")
                .append(c.getString(0))
                .append(": ")
                .append(c.getInt(1))
                .append("\n");
        }
        c.close();

        tvAnalysis.setText(String.format(Locale.getDefault(), 
            "PATTERN SUMMARY\n\nTotal: %d\nHigh-risk: %d\nRisk: %.1f%%\nLevel: %s\n%s\nINSIGHT\n%s", 
            total, high, p, level, dist.toString(), insight));
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

    private String getVal(String q) {
        Cursor c = db.getReadableDatabase().rawQuery(q, null);
        String res = "No data";
        if (c.moveToFirst()) {
            res = c.getString(0);
        }
        c.close();
        return res;
    }

    @Override
    protected void onResume() {
        super.onResume();
        analyzeData();
    }
}