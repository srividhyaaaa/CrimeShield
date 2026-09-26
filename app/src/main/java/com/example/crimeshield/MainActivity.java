package com.example.crimeshield;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btnReport).setOnClickListener(v -> {
            nav(ReportActivity.class);
        });
        
        findViewById(R.id.btnDashboard).setOnClickListener(v -> {
            nav(DashboardActivity.class);
        });
        
        findViewById(R.id.btnAnalysis).setOnClickListener(v -> {
            nav(AnalysisActivity.class);
        });
    }

    private void nav(Class<?> c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}