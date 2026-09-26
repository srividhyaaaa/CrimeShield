package com.example.crimeshield;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AnalysisActivity extends AppCompatActivity {
    TextView tvTotal, tvHighRisk, tvCrime, tvArea, tvAnalysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        tvTotal = findViewById(R.id.tvTotal);
        tvHighRisk = findViewById(R.id.tvHighRisk);
        tvCrime = findViewById(R.id.tvCrime);
        tvArea = findViewById(R.id.tvArea);
        tvAnalysis = findViewById(R.id.tvAnalysis);

        FirebaseFirestore.getInstance().collection("reports").get().addOnSuccessListener(result -> {
            if (result.isEmpty()) {
                tvTotal.setText("Total Reports\n0");
                tvHighRisk.setText("High-Risk Incidents\n0");
                tvCrime.setText("Most Reported Crime\n-");
                tvArea.setText("Most Reported Area\n-");
                tvAnalysis.setText("No incidents available.");
                return;
            }
            Map<String, Integer> crimes = new HashMap<>(), areas = new HashMap<>();
            int highRisk = 0;
            for (var doc : result) {
                String c = doc.getString("crimeType"), a = doc.getString("location"), s = doc.getString("severity");
                if (c != null) crimes.merge(c, 1, Integer::sum);
                if (a != null) areas.merge(a, 1, Integer::sum);
                if ("High".equals(s) || "Critical".equals(s)) highRisk++;
            }
            String commonCrime = getMost(crimes), commonArea = getMost(areas);
            tvTotal.setText("Total Reports\n" + result.size());
            tvHighRisk.setText("High-Risk Incidents\n" + highRisk);
            tvCrime.setText("Most Reported Crime\n" + commonCrime);
            tvArea.setText("Most Reported Area\n" + commonArea);
            tvAnalysis.setText("Total Reports: " + result.size() + "\n\nHigh-Risk Incidents: " + highRisk + "\n\nMost Reported Crime: " + commonCrime + "\n\nMost Reported Area: " + commonArea);
        }).addOnFailureListener(e -> tvAnalysis.setText("Unable to load analysis."));
    }

    private String getMost(Map<String, Integer> map) {
        String most = "-";
        int max = 0;
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            if (e.getValue() > max) { max = e.getValue(); most = e.getKey(); }
        }
        return most;
    }
}
